package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegAppr02Id;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
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
@Component("acNegCom")
@Scope("prototype")
public class AcNegCom extends TradeBuffer {
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
	public NegAppr02Service negAppr02Service;

	@Override
	/* 產生債協入帳明細 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("acNegCom run ... " + titaVo.get("EntryDate"));

		for (AcDetail ac : this.txBuffer.getAcDetailList()) {
			// 客戶債協暫收款
			if (ac.getAcctCode().equals("T11") || ac.getAcctCode().equals("T12") || ac.getAcctCode().equals("T13")) {
				// 債協專戶的匯入款，為一般債權撥付款 for L3210暫收入帳
				if ("L3210".equals(titaVo.getTxcd()) && "094".equals(ac.getSumNo())) {
					updateNegAppr02(ac, titaVo);
				}
				// 暫收存入、暫收轉入，產生債協入帳明細
				if ("L3210".equals(titaVo.getTxcd()) || "L3230".equals(titaVo.getTxcd())) {
					int entryDate;// 入帳日期
					if (titaVo.get("EntryDate") == null) {
						entryDate = ac.getAcDate();
					} else {
						entryDate = parse.stringToInteger(titaVo.get("EntryDate"));
					}
					updateNegTrans(ac, entryDate, titaVo);
				}
			}
		}

		return null;

	}

	/* 更新 NegAppr02一般債權撥付資料檔 */
	private void updateNegAppr02(AcDetail ac, TitaVo titaVo) throws LogicException {
		// NegAppr02一般債權撥付資料檔，提兌日 = 入帳日，金額相同，會計日=0，檢核成功
		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(ac.getJsonFields());
		NegAppr02Id tNegAppr02Id = new NegAppr02Id();
		if (!"".equals(tTempVo.getParam("BringUpDate"))) {
			tNegAppr02Id.setBringUpDate(parse.stringToInteger(tTempVo.getParam("BringUpDate"))); // 提兌日
			tNegAppr02Id.setFinCode(tTempVo.getParam("FinCode")); // 債權機構代號
			tNegAppr02Id.setTxSeq(tTempVo.getParam("TxSeq")); // 資料檔交易序號
			NegAppr02 tNegAppr02 = negAppr02Service.holdById(tNegAppr02Id, titaVo);
			if (tNegAppr02 == null) {
				throw new LogicException(titaVo, "E0006", " " + "一般債權撥付資料檔 " + tNegAppr02Id); // 鎖定資料時，發生錯誤
			}
			// 正常交易更新會計日期、訂正交易會計日期 = 0
			if (this.txBuffer.getTxCom().getBookAcHcode() == 0) { // 帳務訂正記號 AcHCode 0.正常 1.當日訂正 2.隔日訂正
				tNegAppr02.setAcDate(this.txBuffer.getTxCom().getTbsdy());
			} else {// 2022-3-22取消L5712暫收解入功能,改由整批入帳入專戶資料找配對的batchtx02同金額,同時寫NegTrans,訂正限制由NegTrans判斷
//				if (tNegAppr02.getTxStatus() != 0) {// 已做暫收解入或入帳,不可訂正
//					if (tNegAppr02.getTxStatus() == 2) {
//						throw new LogicException(titaVo, "E0007", " " + "一般債權撥付資料檔已入帳，不可訂正 " + tNegAppr02Id); //
//					} else {
//						throw new LogicException(titaVo, "E0007", " " + "一般債權撥付資料檔已作暫收解入，不可訂正 " + tNegAppr02Id); //
//					}
//				}
				tNegAppr02.setAcDate(0);
			}

			try {
				negAppr02Service.update(tNegAppr02);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "一般債權撥付資料檔 + tNegAppr02Id)"); // 更新資料時，發生錯誤
			}
		}
	}

	/* 產生債協入帳明細 */
	private void updateNegTrans(AcDetail ac, int entryDate, TitaVo titaVo) throws LogicException {
		NegTransId tNegTransId = new NegTransId();
		NegTrans tNegTrans = new NegTrans();
		NegMain tNegMain = new NegMain();

		tNegTransId.setAcDate(this.txBuffer.getTxCom().getReldy());
		tNegTransId.setTitaTlrNo(this.txBuffer.getTxCom().getRelTlr());
		tNegTransId.setTitaTxtNo(this.txBuffer.getTxCom().getRelTno());
		tNegTrans.setNegTransId(tNegTransId);
		// 正常交易新增、訂正交易要刪除
		if (this.txBuffer.getTxCom().getBookAcHcode() == 0) { // 帳務訂正記號 AcHCode 0.正常 1.當日訂正 2.隔日訂正
			tNegMain = negMainService.statusFirst("0", ac.getCustNo(), titaVo); // 0-正常
			if (tNegMain == null) {
				throw new LogicException(titaVo, "E6003", "acNegCom 非債協戶 " + ac.getCustNo());
			}
			tNegTrans.setCustNo(ac.getCustNo()); // 戶號
			tNegTrans.setCaseSeq(tNegMain.getCaseSeq()); // 案件序號
			tNegTrans.setEntryDate(entryDate); // 入帳日期
			tNegTrans.setTxStatus(0); // 交易狀態 0:未入帳
			tNegTrans.setTxKind("9"); // 交易別 9:未處理
			tNegTrans.setTxAmt(ac.getTxAmt()); // 交易金額
			try {
				negTransService.insert(tNegTrans, titaVo); // insert
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "acNegCom insert " + tNegTransId + e.getErrorMsg());
			}
		} else {
			tNegTrans = negTransService.holdById(tNegTransId, titaVo); // hold by id
			if (tNegTrans == null) {
				throw new LogicException(titaVo, "E6003", "acNegCom NegTrans Notfound " + tNegTransId);
			}
			if (tNegTrans.getTxStatus() == 2) { // 2:已入帳
				throw new LogicException(titaVo, "E6003", "acNegCom  債協已入帳不可訂正 " + tNegTransId);
			}
			try {
				negTransService.delete(tNegTrans, titaVo); // delete
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "acNegCom delete " + tNegTransId + e.getErrorMsg());
			}
		}
	}

	/**
	 * 以戶號取得債協暫收款科目 acDetail.setAcctCode(acNegCom.getAcctCode(CustNo,titaVo))
	 * 
	 * @param custNo 戶號
	 * @param titaVo TitaVo
	 * @return 債協暫收款科目
	 * @throws LogicException ...
	 */
	public String getAcctCode(int custNo, TitaVo titaVo) throws LogicException {
// T10  債協暫收款－收款專戶                                601776戶號(前置協商收款專戶)                              
// T11  債協暫收款－抵繳款                                  案件種類 1:債協                  
// T12  前調暫收款－抵繳款                                  案件種類 2:調解                  
// T13  更生暫收款－抵繳款                                  案件種類 3:更生   4:清算                
		NegMain tNegMain = new NegMain();
		String acctCode = null;
		if (custNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) // 專戶
			acctCode = "T10";
		else {
			tNegMain = negMainService.custNoFirst(custNo, titaVo);
			if (tNegMain == null)
				throw new LogicException(titaVo, "E6003", "acNegCom getAcctCode 該戶非債協戶 " + custNo);
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
	 * 以戶號取得債協退還款科目入帳acDetail.setAcctCode(acNegCom.getReturnAcctCode(CustNo,titaVo));
	 * 
	 * @param custNo 戶號
	 * @param titaVo TitaVo
	 * @return 債協退還款科目
	 * @throws LogicException LogicException
	 */
	public String getReturnAcctCode(int custNo, TitaVo titaVo) throws LogicException {
// T21  債協暫收款－退還款                                  案件種類 1:債協
// T22  前調暫收款－退還款                                  案件種類 2:調解 
// T23  更生暫收款－退還款                                  案件種類 3:更生   4:清算
		NegMain tNegMain = new NegMain();
		String acctCode = null;
		if (custNo == this.txBuffer.getSystemParas().getNegDeptCustNo())
			acctCode = "T10";
		else {
			tNegMain = negMainService.custNoFirst(custNo, titaVo);
			if (tNegMain == null)
				throw new LogicException(titaVo, "E6003", "acNegCom getReturnAcctCode 該戶非債協戶 " + custNo);
			else {
				switch (tNegMain.getCaseKindCode()) {

				case "2":
					acctCode = "T22";
					break;

				case "3":
					acctCode = "T23";
					break;

				case "4":
					acctCode = "T23";
					break;

				default:
					acctCode = "T21";
					break;
				}
			}
		}
		return acctCode;
	}

	/**
	 * 以戶號取得債協應付代收款科目 acDetail.setAcctCode(acNegCom.getApprAcctCode(CustNo,titaVo));
	 * 
	 * @param custNo 戶號
	 * @param titaVo TitaVo
	 * @return 債協應付代收款科目
	 * @throws LogicException LogicException
	 */
	public String getApprAcctCode(int custNo, TitaVo titaVo) throws LogicException {
// NA1	應付代收款－債權協商                                 案件種類 1:債協
// NA2	應付代收款－前置調解                                 案件種類 2:調解 
// NA3	應付代收款－更生統一收付                           案件種類 3:更生   4:清算
		NegMain tNegMain = new NegMain();
		String acctCode = null;
		if (custNo == this.txBuffer.getSystemParas().getNegDeptCustNo())
			acctCode = "T10";
		else {
			tNegMain = negMainService.custNoFirst(custNo, titaVo);
			if (tNegMain == null)
				throw new LogicException(titaVo, "E6003", "acNegCom getApprAcctCode 該戶非債協戶 " + custNo);
			else {
				switch (tNegMain.getCaseKindCode()) {

				case "2":
					acctCode = "NA2";
					break;

				case "3":
					acctCode = "NA3";
					break;

				case "4":
					acctCode = "NA3";
					break;

				default:
					acctCode = "NA1";
					break;
				}
			}
		}
		return acctCode;
	}

	/**
	 * 是否為債協暫收款帳戶 if (acNegCom.isNegCustNo(CustNo,titaVo));
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

	/**
	 * 找債協專戶的匯入款，為一般債權匯入款的戶號
	 * 
	 * @param entryDate 入帳日
	 * @param txAmt     金額
	 * @param custNo    戶號
	 * @param titaVo    TitaVo
	 * @return TempVo
	 * @throws LogicException LogicException
	 */
	public List<AcDetail> getNegAppr02CustNo(int entryDate, BigDecimal txAmt, int custNo, TitaVo titaVo) throws LogicException {
		this.info("NegAppr02 entryDate=" + entryDate + ", txAmt=" + txAmt);
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
		TempVo tTempVo = new TempVo();
		// NegAppr02一般債權撥付資料檔，提兌日 = 入帳日，金額相同，會計日=0，檢核成功 ,抓第一筆符合條件之資料
		Slice<NegAppr02> slNegAppr02 = negAppr02Service.bringUpDateEq(entryDate + 19110000, 0, Integer.MAX_VALUE, titaVo);
		if (slNegAppr02 != null) {
			for (NegAppr02 tNegAppr02 : slNegAppr02.getContent()) {
				if (tNegAppr02.getAcDate() == 0 && ("4001".equals(tNegAppr02.getStatusCode()) && tNegAppr02.getTxAmt().compareTo(txAmt) == 0)) {// 須為檢核成功之資料
					AcDetail acDetail = new AcDetail();
					acDetail.setDbCr("C");
					acDetail.setSumNo("094"); // 094 :轉債協暫收款
					acDetail.setAcctCode(getAcctCode(tNegAppr02.getCustNo(), titaVo));
					acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
					acDetail.setTxAmt(tNegAppr02.getTxAmt());
					acDetail.setCustNo(tNegAppr02.getCustNo());
					acDetail.setSlipNote("一般債權撥付");
					tTempVo.putParam("BringUpDate", tNegAppr02.getBringUpDate()); // 提兌日
					tTempVo.putParam("FinCode", tNegAppr02.getFinCode()); // 債權機構代號
					tTempVo.putParam("TxSeq", tNegAppr02.getTxSeq()); // 資料檔交易序號
					tTempVo.putParam("NegCustNo", tNegAppr02.getCustNo()); // 債協戶號
					this.info("NegAppr02 tempVo=" + tTempVo.toString());
					acDetail.setJsonFields(tTempVo.getJsonString()); // for AcNegCom Updae
					lAcDetail.add(acDetail);

					break;
				}
			}
		}
		return lAcDetail;
	}

}
