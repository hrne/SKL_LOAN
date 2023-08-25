package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.DailyTavService;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 債協暫收款處理 <BR>
 * 1.run ： 債協暫收款入帳更新債協入帳檔 call by AcEntetCom<BR>
 * 1.1 L3210 暫收款登錄，如為債協暫收款則產生NegTrans(債協入帳明細檔)<BR>
 * 1.2 L3210 暫收款登錄，如為一般債權撥付入帳時同時更新NegAppr02(一般債權撥付資料檔) <BR>
 * 2.getAcctCode 以戶號取得債協暫收款科目 call by LXXXX<BR>
 * 3.getReturnAcctCode 以戶號取得債協退還款科目call by LXXXX<BR>
 * 4.ApprAcctCode 以戶號取得債協應付代收款科目call by LXXXX<BR>
 * 5.isNegCustNo 是否為債協暫收款帳戶 call by call by LXXXX <BR>
 * 6.getNegAppr02CustNo 找債協專戶的匯入款，為一般債權匯入款的戶號 call by L3210暫收入帳 <BR>
 * 
 * @author st1
 *
 */
@Service("BST03")
@Scope("prototype")
public class BST03 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public NegTransService negTransService;

	@Autowired
	public NegMainService negMainService;

	@Autowired
	public DailyTavService dailyTavService;
	@Autowired
	public BankRmtfService bankRmtfService;
	@Autowired
	public NegAppr02Service negAppr02Service;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public AcReceivableCom acReceivableCom;
	@Autowired
	public AcDetailCom acDetailCom;
	@Autowired
	WebClient webClient;

	@Override
	/* 產生債協入帳明細 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BST03 run  ");
		String[] strAr = titaVo.getParam("Parm").split(",");
		int iEntryDate = this.parse.stringToInteger(strAr[0]);
		int iAcDate = this.parse.stringToInteger(strAr[1]);
		this.info("iEntryDate=" + iEntryDate + ",iAcDate=" + iAcDate);
		titaVo.putParam("SECNO", "09");
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
		Slice<BankRmtf> slBankRmtf = bankRmtfService.findEntryDate(iEntryDate, iAcDate, 0, Integer.MAX_VALUE, titaVo);

		for (BankRmtf t : slBankRmtf.getContent()) {
			// 客戶債協暫收款
			if (t.getReconCode().equals("A6") || t.getReconCode().equals("A7")) {
				if (!t.getTitaTlrNo().isEmpty()) {
					this.info(t.toString());
					int custNo = getAppr02CustNo(t, titaVo);// 債協主檔戶號
					t.setCustNo(custNo);
					// 一般債權不寫AcDetail但要維護NegTrans
					NegMain tNegMain = new NegMain();
					tNegMain = negMainService.statusFirst("0", custNo, titaVo); // 0-正常
					if (tNegMain != null) {
						if ("Y".equals(tNegMain.getIsMainFin())) {
							// 暫收存入、暫收轉入，產生債協入帳明細
							String acctCode = getAcctCode(t.getCustNo(), titaVo);
							/* 貸：債協暫收款－抵繳款 */
							AcDetail acDetail = new AcDetail();
							acDetail.setDbCr("C");
							acDetail.setAcctCode(acctCode);
							acDetail.setTxAmt(t.getRepayAmt()); //
							acDetail.setCustNo(t.getCustNo());// 戶號
							lAcDetail.add(acDetail);
						}
						updateNegTrans(t, titaVo);
						// dailyTav(acctCode, t, titaVo);

					}
				}
			}
		}
		/* 產生會計分錄 */
		acDetailCom.setTxBuffer(this.txBuffer);
		this.txBuffer.setAcDetailList(lAcDetail);
		acDetailCom.run(titaVo);
		for (AcDetail ac : lAcDetail) {
			deleteAcReceivable(ac, titaVo);
		}
		acReceivableCom.setTxBuffer(this.txBuffer);
		acReceivableCom.run(titaVo);
		lAcDetail = new ArrayList<AcDetail>();
		this.txBuffer.setAcDetailList(lAcDetail);
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "", "BST03已完成", titaVo);

		return null;

	}

	/* 產生債協入帳明細 */
	private int getAppr02CustNo(BankRmtf t, TitaVo titaVo) throws LogicException {
		int custNo = t.getCustNo();
		NegMain tNegMain = negMainService.payerCustNoFirst(custNo, titaVo);
		if (tNegMain != null && "Y".equals(tNegMain.getIsMainFin())) {
			return tNegMain.getCustNo();
		}
		if (tNegMain != null && "N".equals(tNegMain.getIsMainFin())) {
			Slice<NegAppr02> slNegAppr02 = negAppr02Service.acDateEq(0, 0, Integer.MAX_VALUE, titaVo);
			if (slNegAppr02 != null) {
				for (NegAppr02 n : slNegAppr02.getContent()) {
					if (t.getRepayAmt().compareTo(n.getTxAmt()) == 0) {
						custNo = n.getCustNo();
					}
				}
			}
		}
		return custNo;
	}

	private void deleteAcReceivable(AcDetail ac, TitaVo titaVo) throws LogicException {
		AcReceivableId tAcReceivableId = new AcReceivableId();
		tAcReceivableId.setAcctCode(ac.getAcctCode());
		tAcReceivableId.setCustNo(ac.getCustNo());
		tAcReceivableId.setFacmNo(ac.getFacmNo());
		tAcReceivableId.setRvNo(" ");
		AcReceivable tAcReceivable = acReceivableService.holdById(tAcReceivableId, titaVo); // holdById
		if (tAcReceivable != null && "LC899".equals(tAcReceivable.getOpenTxCd())) {
			try {
				acReceivableService.delete(tAcReceivable, titaVo); // insert
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E6003", "AcReceivable insert " + tAcReceivableId + e.getErrorMsg());
			}
		}
	}

	/* 產生債協入帳明細 */
	private void updateNegTrans(BankRmtf t, TitaVo titaVo) throws LogicException {
		Slice<NegTrans> slNegTrans = negTransService.custAndEntryDate(t.getCustNo(), t.getEntryDate() + 19110000,
				t.getEntryDate() + 19110000, 0, Integer.MAX_VALUE, titaVo);
		if (slNegTrans != null) {
			this.info("skip dup " + slNegTrans.toString() + t.toString());
			return;
		}

		NegTransId tNegTransId = new NegTransId();
		NegTrans tNegTrans = new NegTrans();
		NegMain tNegMain = new NegMain();

		tNegTransId.setAcDate(t.getAcDate());
		tNegTransId.setTitaTlrNo(t.getTitaTlrNo());
		tNegTransId.setTitaTxtNo(parse.stringToInteger(t.getTitaTxtNo()));
		tNegTransId.setCustNo(t.getCustNo()); // 戶號
		tNegTrans.setNegTransId(tNegTransId);
		// 正常交易新增、訂正交易要刪除
		if (this.txBuffer.getTxCom().getBookAcHcode() == 0) { // 帳務訂正記號 AcHCode 0.正常 1.訂正 2.3.沖正
			tNegMain = negMainService.statusFirst("0", t.getCustNo(), titaVo); // 0-正常
			if (tNegMain == null) {
				throw new LogicException(titaVo, "E6003", "BST03 非債協戶 " + t.getCustNo());
			}
			tNegTrans.setCaseSeq(tNegMain.getCaseSeq()); // 案件序號
			tNegTrans.setEntryDate(t.getEntryDate()); // 入帳日期
			tNegTrans.setTxStatus(0); // 交易狀態 0:未入帳
			tNegTrans.setTxKind("9"); // 交易別 9:未處理
			tNegTrans.setTxAmt(t.getRepayAmt()); // 還款金額
			try {
				negTransService.insert(tNegTrans, titaVo); // insert
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "BST03 insert " + tNegTransId + e.getErrorMsg());
			}
		} else {
			tNegTrans = negTransService.holdById(tNegTransId, titaVo); // hold by id
			if (tNegTrans == null) {
				throw new LogicException(titaVo, "E6003", "BST03 NegTrans Notfound " + tNegTransId);
			}
			if (tNegTrans.getTxStatus() == 2) { // 2:已入帳
				throw new LogicException(titaVo, "E6003", "BST03  債協已入帳不可訂正 " + tNegTransId);
			}
			try {
				negTransService.delete(tNegTrans, titaVo); // delete
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "BST03 delete " + tNegTransId + e.getErrorMsg());
			}
		}
	}

	/**
	 * 以戶號取得債協暫收款科目 acDetail.setAcctCode(BST03.getAcctCode(CustNo,titaVo))
	 * 
	 * @param custNo 戶號
	 * @param titaVo TitaVo
	 * @return 債協暫收款科目
	 * @throws LogicException ...
	 */
	public String getAcctCode(int custNo, TitaVo titaVo) throws LogicException {
// TAV  債協暫收款－收款專戶                                601776戶號(前置協商收款專戶)                              
// T11  債協暫收款－抵繳款                                  案件種類 1:債協                  
// T12  前調暫收款－抵繳款                                  案件種類 2:調解                  
// T13  更生暫收款－抵繳款                                  案件種類 3:更生   4:清算                
		NegMain tNegMain = new NegMain();
		String acctCode = null;
		if (custNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) // 專戶
			acctCode = "TAV";
		else {
			tNegMain = negMainService.custNoFirst(custNo, titaVo);
			if (tNegMain == null)
				throw new LogicException(titaVo, "E6003", "BST03 getAcctCode 該戶非債協戶 " + custNo);
			else {
				switch (tNegMain.getCaseKindCode()) {

				case "2":
					acctCode = "T12";
					break;

				case "3":
					acctCode = "T13";
					break;

				case "4":
					acctCode = "T13";
					break;

				default:
					acctCode = "T11";
					break;

				}
			}
		}
		return acctCode;
	}

	/**
	 * 是否為債協暫收款帳戶 if (BST03.isNegCustNo(CustNo,titaVo));
	 * 
	 * @param custNo 戶號
	 * @param titaVo TitaVo
	 * @return true/false
	 * @throws LogicException ...
	 */
	public boolean isNegCustNo(int custNo, TitaVo titaVo) throws LogicException {
		NegMain tNegMain = new NegMain();
		boolean isNegCustNo = false;
		if (custNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) // 專戶
			isNegCustNo = true;
		else {
			tNegMain = negMainService.statusFirst("0", custNo, titaVo); // 0-正常
			if (tNegMain != null)
				isNegCustNo = true;
		}
		return isNegCustNo;
	}

}
