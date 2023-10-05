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
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegAppr02Id;
import com.st1.itx.db.domain.NegFinShare;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.db.service.NegAppr01Service;
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
 * 7.getNegAppr01CustNo 找債協專戶的匯入款，為最大債權匯入款的戶號 call by L3210暫收入帳 <BR>
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
	@Autowired
	public NegAppr01Service negAppr01Service;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public NegCom negCom;
	@Autowired
	BaTxCom baTxCom;
	@Autowired
	LoanCom loanCom;

	@Override
	/* 產生債協入帳明細 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("acNegCom run ... " + titaVo.get("EntryDate"));

		for (AcDetail ac : this.txBuffer.getAcDetailList()) {
			// 暫收存入、暫收轉入 客戶債協暫收款，產生債協入帳明細
			if ("L3210".equals(titaVo.getTxcd()) || "L3230".equals(titaVo.getTxcd())) {
				if (ac.getAcctCode().equals("T11") || ac.getAcctCode().equals("T12")
						|| ac.getAcctCode().equals("T13")) {
					if ("最大債權撥付失敗匯回款".equals(ac.getSlipNote())) {
						this.info("最大債權撥付失敗匯回款,不產生債協入帳明細");
					} else {
						updateNegTrans(ac.getCustNo(), ac.getTxAmt(), titaVo);
					}
				}
				if (ac.getAcctCode().equals("TAV") && "一般債權".equals(ac.getSlipNote())) {
					int custNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
					if ("L3230".equals(titaVo.getTxcd())) {
						custNo = ac.getCustNo();
						TempVo tTempVo = new TempVo();
						tTempVo = tTempVo.getVo(ac.getJsonFields());
						if (tTempVo.get("RmCustNo") != null) {
							custNo = parse.stringToInteger(tTempVo.get("RmCustNo"));// 使用JsonFields裡存的原戶號才抓的到債協戶號
						}
					}
					updateNegTrans(custNo, ac.getTxAmt(), titaVo);
				}
			}
			// 債協專戶的匯入款A7，為一般債權撥付款，直接入可抵繳TAV for L3210暫收入帳
			if (ac.getAcctCode().equals("TAV") && "一般債權撥付".equals(ac.getSlipNote())
					&& "L3210".equals(titaVo.getTxcd())) {
				updateNegAppr02(ac, titaVo);
			}
		}

		return null;

	}

	/* 更新 NegAppr02一般債權撥付資料檔 */
	private void updateNegAppr02(AcDetail lAcDetail, TitaVo titaVo) throws LogicException {
		// NegAppr02一般債權撥付資料檔，提兌日 = 入帳日，金額相同，會計日=0，檢核成功
		AcDetail ac = lAcDetail;
		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(ac.getJsonFields());
		if (!"".equals(tTempVo.getParam("BringUpDate"))) {
			NegAppr02Id tNegAppr02Id = new NegAppr02Id();
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
			} else {// 整批入帳入專戶資料找配對的batchtx02同金額,同時寫NegTrans,訂正限制由NegTrans判斷
				tNegAppr02.setAcDate(0);
			}

			try {
				negAppr02Service.update(tNegAppr02, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "一般債權撥付資料檔 + tNegAppr02Id)"); // 更新資料時，發生錯誤
			}
			updateNegTrans(tNegAppr02.getCustNo(), ac.getTxAmt(), titaVo);
		}
	}

	/* 產生債協入帳明細 */
	private void updateNegTrans(int custNo, BigDecimal txAmt, TitaVo titaVo) throws LogicException {
		int entryDate;// 入帳日期
		if (titaVo.get("EntryDate") == null) {
			entryDate = titaVo.getEntDyI();
		} else {
			entryDate = parse.stringToInteger(titaVo.get("EntryDate"));
		}
		NegTransId tNegTransId = new NegTransId();
		NegTrans tNegTrans = new NegTrans();
		NegMain tNegMain = new NegMain();
		tNegTransId.setAcDate(this.txBuffer.getTxCom().getReldy());
		tNegTransId.setTitaTlrNo(this.txBuffer.getTxCom().getRelTlr());
		tNegTransId.setTitaTxtNo(this.txBuffer.getTxCom().getRelTno());
		tNegTransId.setCustNo(custNo); // 戶號
		tNegTrans.setNegTransId(tNegTransId);
		// 正常交易新增、訂正交易要刪除
		if (this.txBuffer.getTxCom().getBookAcHcode() == 0) { // 帳務訂正記號 AcHCode 0.正常 1.訂正 2.3.沖正
			tNegMain = negMainService.statusFirst("0", custNo, titaVo); // 0-正常
			if (tNegMain == null) {// 無戶況正常且為L3230:若為撥付失敗重撥需再找最新一筆,允許戶況非0正常
				if (!"L3230".equals(titaVo.getTxcd())) {
					throw new LogicException(titaVo, "E6003", "acNegCom 非債協戶 " + custNo);
				}
				tNegMain = negMainService.custNoFirst(custNo, titaVo);
				if (tNegMain == null) {
					throw new LogicException(titaVo, "E6003", "acNegCom 非債協戶 " + custNo);
				}
				if ("N".equals(tNegMain.getIsMainFin())) {
					throw new LogicException(titaVo, "E6003", "acNegCom 非債協戶 " + custNo);
				}
				if (!ishasfincode(custNo, txAmt, titaVo)) {
					throw new LogicException(titaVo, "E6003", "acNegCom 金額與撥付失敗總金額不符,不允許戶況非正常,戶號= " + custNo);
				}
			}
			tNegTrans.setCaseSeq(tNegMain.getCaseSeq()); // 案件序號
			tNegTrans.setEntryDate(entryDate); // 入帳日期
			tNegTrans.setTxStatus(0); // 交易狀態 0:未入帳
			tNegTrans.setTxKind("9"); // 交易別 9:未處理
			tNegTrans.setTxAmt(txAmt); // 交易金額
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
				throw new LogicException(titaVo, "E6003", "acNegCom  債協已入帳 " + tNegTransId);
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
// TAV  債協暫收款－收款專戶                                601776戶號(前置協商收款專戶)                              
// T11  債協暫收款－抵繳款                                  案件種類 1:債協                  
// T12  前調暫收款－抵繳款                                  案件種類 2:調解                  
// T13  更生暫收款－抵繳款                                  案件種類 3:更生   4:清算                
		NegMain tNegMain = new NegMain();
		String acctCode = null;
		if (custNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) { // 專戶
			acctCode = "TAV";
		} else {
			tNegMain = negMainService.custNoFirst(custNo, titaVo);
			if (tNegMain == null) {
				throw new LogicException(titaVo, "E6003", "acNegCom getAcctCode 該戶非債協戶 " + custNo);
			}
			if ("N".equals(tNegMain.getIsMainFin())) {
				acctCode = "TAV";
			} else {
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
	 * @param iCustNo 戶號
	 * @param titaVo  TitaVo
	 * @return 債協退還款科目
	 * @throws LogicException LogicException
	 */
	public TempVo getReturnAcctCode(int iCustNo, TitaVo titaVo) throws LogicException {
		TempVo tTempVo = new TempVo();
		String acctCode = "TAV"; // 暫收可抵繳
		int custNo = iCustNo;
		int facmNo = 0;
		if (custNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) {
			acctCode = "TAV"; // 債協暫收款－收款專戶
		} else {
			NegMain tNegMain = new NegMain();
			tNegMain = negMainService.statusFirst("0", iCustNo, titaVo); // 0-正常
			if (tNegMain != null && tNegMain.getPayerCustNo() > 0) {
				custNo = tNegMain.getPayerCustNo();
			}
			List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
			lStatus.add(2);
			lStatus.add(3);
			Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(custNo, 0, 999, 0, 990, 1, 999,
					lStatus, 0, Integer.MAX_VALUE, titaVo);
			facmNo = 1;
			if (slLoanOverdue != null) {
				int badDebtDate = 0;// 最小轉呆帳日期
				for (LoanOverdue od : slLoanOverdue.getContent()) {
					if (badDebtDate == 0 && od.getBadDebtDate() > 0) {
						badDebtDate = od.getBadDebtDate();
						facmNo = od.getFacmNo();
					}
					if (badDebtDate > 0 && od.getBadDebtDate() < badDebtDate) {
						badDebtDate = od.getBadDebtDate();
						facmNo = od.getFacmNo();
					}
				}
			}
		}
		tTempVo.putParam("RmCustNo", iCustNo); // 匯款人戶號
		tTempVo.putParam("CustNo", custNo); // 實際借款人戶號
		tTempVo.putParam("FacmNo", facmNo);
		tTempVo.putParam("AcctCode", acctCode);

		return tTempVo;
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
			acctCode = "TAV";
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
	 * 債協專戶的匯入款，如為一般債權撥付款，則轉入借款戶的暫收可抵繳
	 * 
	 * @param entryDate 入帳日
	 * @param txAmt     金額
	 * @param iCustNo   戶號
	 * @param titaVo    TitaVo
	 * @return TempVo
	 * @throws LogicException LogicException
	 */
	public List<AcDetail> getNegAppr02CustNo(int entryDate, BigDecimal txAmt, int iCustNo, TitaVo titaVo)
			throws LogicException {
		this.info("NegAppr02 entryDate=" + entryDate + ", txAmt=" + txAmt);
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
		// NegAppr02一般債權撥付資料檔，提兌日 <= 入帳日 且 會計日=0，若金額相同或加總相同，檢核成功
		Slice<NegAppr02> slNegAppr02 = negAppr02Service.bringUpDate2(entryDate + 19110000, 0, 0, Integer.MAX_VALUE,
				titaVo);
		if (slNegAppr02 == null) {
			return lAcDetail;
		}
		BigDecimal appr02Total = BigDecimal.ZERO;
		List<NegAppr02> lNegAppr02 = new ArrayList<NegAppr02>();
		// 提兌總額 (須為檢核成功、未入帳之資料
		for (NegAppr02 tNegAppr02 : slNegAppr02.getContent()) {
			if (tNegAppr02.getAcDate() == 0 && ("4001".equals(tNegAppr02.getStatusCode()))) {
				appr02Total = appr02Total.add(tNegAppr02.getTxAmt());
				lNegAppr02.add(tNegAppr02);
			}
		}
		TempVo tTempVo = new TempVo();
		for (NegAppr02 tNegAppr02 : lNegAppr02) {
			if (appr02Total.compareTo(txAmt) == 0 || tNegAppr02.getTxAmt().compareTo(txAmt) == 0) {
				AcDetail acDetail = new AcDetail();
				// 貸: 客戶暫收可抵繳
				TempVo tempVo = new TempVo();
				tempVo = getReturnAcctCode(tNegAppr02.getCustNo(), titaVo);
				int custNo = parse.stringToInteger(tempVo.getParam("CustNo"));
				int facmNo = parse.stringToInteger(tempVo.getParam("FacmNo"));
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setSumNo("092"); // 092 :轉客戶暫收可抵繳
				acDetail.setAcctCode("TAV");
				acDetail.setTxAmt(tNegAppr02.getTxAmt());
				acDetail.setCustNo(custNo);// 實際借款人戶號
				acDetail.setFacmNo(facmNo);
				acDetail.setSlipNote("一般債權撥付");

				tTempVo.putParam("BringUpDate", tNegAppr02.getBringUpDate()); // 提兌日
				tTempVo.putParam("FinCode", tNegAppr02.getFinCode()); // 債權機構代號
				tTempVo.putParam("TxSeq", tNegAppr02.getTxSeq()); // 資料檔交易序號
				tTempVo.putParam("NegCustNo", tNegAppr02.getCustNo()); // 債協戶號
				this.info("NegAppr02 tempVo=" + tTempVo.toString());
				acDetail.setJsonFields(tTempVo.getJsonString()); // for AcNegCom Updae
				lAcDetail.add(acDetail);

				if (tNegAppr02.getTxAmt().compareTo(txAmt) == 0) {// 若入專戶是一筆總金額,則不可跳掉
					break;
				}
			}
		}
		return lAcDetail;
	}

	/**
	 * 債協專戶的匯入款為最大債權撥付失敗匯回款<BR>
	 * 1.L3210 由債協專戶暫收款轉入最大債權客戶的債協款<BR>
	 * 2.將客戶的債協款轉入應付代收款
	 * 
	 * @param entryDate 入帳日
	 * @param txAmt     金額
	 * @param iCustNo   戶號
	 * @param titaVo    TitaVo
	 * @return TempVo
	 * @throws LogicException LogicException
	 */
	public List<AcDetail> getNegAppr01ReRemit(int entryDate, BigDecimal txAmt, int iCustNo, TitaVo titaVo)
			throws LogicException {
		this.info("NegAppr01 entryDate=" + entryDate + ", txAmt=" + txAmt);
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
		// NegAppr01最大債權撥付資料檔，回應代碼為失敗且提兌日 >= 入帳日 減二個月 ，若加總(提兌日+債權機構代號)相同，檢核成功
		Slice<NegAppr01> slNegAppr01 = negAppr01Service.findReplyCodeNotEq("4001", entryDate + 19110000 - 0200, 0,
				Integer.MAX_VALUE, titaVo);
		if (slNegAppr01 == null) {
			return lAcDetail;
		}
		BigDecimal appr01Total = BigDecimal.ZERO;
		List<NegAppr01> lNegAppr01 = new ArrayList<NegAppr01>();
		int bringUpDate = slNegAppr01.getContent().get(0).getBringUpDate();
		String finCode = slNegAppr01.getContent().get(0).getFinCode();
		boolean isFind = false;
		for (NegAppr01 tNegAppr01 : slNegAppr01.getContent()) {
			if (tNegAppr01.getBringUpDate() == bringUpDate && tNegAppr01.getFinCode().equals(finCode)) {
				lNegAppr01.add(tNegAppr01);
				appr01Total = appr01Total.add(tNegAppr01.getApprAmt());
			} else {
				if (appr01Total.compareTo(txAmt) == 0) {
					isFind = true;
					break;
				} else {// 提兌日與分攤機構不符則重新累計
					appr01Total = BigDecimal.ZERO;
					lNegAppr01 = new ArrayList<NegAppr01>();
					// 新key值重新累計
					lNegAppr01.add(tNegAppr01);
					bringUpDate = tNegAppr01.getBringUpDate();
					finCode = tNegAppr01.getFinCode();
					appr01Total = appr01Total.add(tNegAppr01.getApprAmt());
				}
			}
		}
		if (appr01Total.compareTo(txAmt) == 0) {
			isFind = true;
		}
		if (isFind) {
			negCom.setTxBuffer(this.txBuffer);
			TempVo tTempVo = new TempVo();
			tTempVo.putParam("BringUpDate", lNegAppr01.get(0).getBringUpDate()); // 提兌日
			tTempVo.putParam("FinCode", lNegAppr01.get(0).getFinCode()); // 債權機構代號
			for (NegAppr01 tNegAppr01 : lNegAppr01) {
				/* 貸：債協暫收款科目 */
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode(getAcctCode(tNegAppr01.getCustNo(), titaVo));
				acDetail.setTxAmt(tNegAppr01.getApprAmt());
				acDetail.setCustNo(tNegAppr01.getCustNo());
				acDetail.setSlipNote("最大債權撥付失敗匯回款");
				acDetail.setJsonFields(tTempVo.getJsonString());
				lAcDetail.add(acDetail);
				/* 借：債協暫收款科目 */
				acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode(getAcctCode(tNegAppr01.getCustNo(), titaVo));
				acDetail.setTxAmt(tNegAppr01.getApprAmt());
				acDetail.setCustNo(tNegAppr01.getCustNo());
				acDetail.setSlipNote("最大債權撥付失敗匯回款");
				acDetail.setJsonFields(tTempVo.getJsonString());
				lAcDetail.add(acDetail);
				/* 貸：應付代收款 */
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode(getApprAcctCode(tNegAppr01.getCustNo(), titaVo));
				acDetail.setTxAmt(tNegAppr01.getApprAmt());
				acDetail.setCustNo(tNegAppr01.getCustNo());
				acDetail.setSlipNote("最大債權撥付失敗匯回款");
				acDetail.setJsonFields(tTempVo.getJsonString());
				lAcDetail.add(acDetail);
			}
		}
		return lAcDetail;
	}

	/**
	 * L5708 最大債權撥付入帳產生放款交易明細(新壽攤分款、結清退還款轉入客戶暫收可抵繳)
	 * 
	 * @param acDetail 帳務分錄
	 * @param titaVo   TitaVo
	 * @throws LogicException ....
	 */
	public void addL5708LoanBorTxHcodeNormal(AcDetail acDetail, TitaVo titaVo) throws LogicException {

		if (acDetail.getTxAmt().compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		baTxCom.setTxBuffer(this.txBuffer);
		loanCom.setTxBuffer(this.txBuffer);
		LoanBorTx tLoanBorTx = new LoanBorTx();
		LoanBorTxId tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, acDetail.getCustNo(), acDetail.getFacmNo(), titaVo);
//	      092:暫收轉帳     (戶號+額度)  TAV 暫收款－可抵繳
		// 3235 轉入放款暫收款
		tLoanBorTx.setTxDescCode("3235");
		tLoanBorTx.setEntryDate(titaVo.getEntDyI());
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setTempAmt(BigDecimal.ZERO);
		tLoanBorTx.setOverflow(acDetail.getTxAmt());
		tLoanBorTx.setAcctCode(acDetail.getAcctCode());

		TempVo tTempVo = new TempVo();
		// 新增摘要
		tTempVo.putParam("Note", acDetail.getSlipNote());
		baTxCom.settingUnPaid(titaVo.getEntDyI(), acDetail.getCustNo(), acDetail.getFacmNo(), 0, 96,
				acDetail.getTxAmt(), titaVo);
		tTempVo.putParam("Excessive", baTxCom.getExcessive().add(baTxCom.getExcessiveOther()));

		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		// step 9. 寫入放款交易內容檔
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}

	}

	/**
	 * L5708 最大債權撥付入帳訂正產生放款交易明細(新壽攤分款、結清退還款轉入客戶暫收可抵繳)
	 * 
	 * @param titaVo TitaVo
	 * @throws LogicException ....
	 */
	public void addL5708LoanBorTxHcodeErase(TitaVo titaVo) throws LogicException {
		loanCom.setTxBuffer(this.txBuffer);
		Slice<LoanBorTx> slLoanBortx = loanBorTxService.acDateTxtNoEq(titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBortx != null) {
			List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>(slLoanBortx.getContent());
			for (LoanBorTx tx : lLoanBorTx) {
				loanCom.checkEraseCustNoTxSeqNo(tx.getCustNo(), titaVo);// 檢查到同戶帳務交易需由最近一筆交易開始訂正
				loanCom.setFacmBorTxHcode(tx.getCustNo(), tx.getFacmNo(), tx.getBorxNo(), titaVo);
			}
		}
	}

	/**
	 * 是否為債協撥付失敗重撥 if (acNegCom.ishasfincode(CustNo,titaVo));
	 * 
	 * @param custNo 戶號
	 * @param txAmt  金額
	 * @param titaVo TitaVo
	 * @return true/false
	 * @throws LogicException ...
	 */
	public boolean ishasfincode(int custNo, BigDecimal txAmt, TitaVo titaVo) throws LogicException {

		NegAppr01 lNegAppr01 = new NegAppr01();
		boolean hasfincodefg = false;
		BigDecimal tshareAmSum = BigDecimal.ZERO;
		// 本會計日前2個月
		dDateUtil.init();
		dDateUtil.setDate_1(titaVo.getEntDyI());
		dDateUtil.setMons(-2);
		int lastmonthDate = dDateUtil.getCalenderDay() + 19110000;

		lNegAppr01 = negAppr01Service.bringUpDateCustNoFirst(custNo, lastmonthDate, titaVo);// 找該戶最近一個提兌日
		if (lNegAppr01 != null) {
			int bringUpDate = lNegAppr01.getBringUpDate() + 19110000;
			Slice<NegAppr01> slNegAppr01 = negAppr01Service.bringUpDateCustNoEq(custNo, bringUpDate, this.index,
					this.limit, titaVo);
			if (slNegAppr01 != null) {
				List<NegAppr01> tNegAppr01 = slNegAppr01 == null ? null : slNegAppr01.getContent();
				for (int i = 0; i < tNegAppr01.size(); i++) {
					lNegAppr01 = tNegAppr01.get(i);
					if (!lNegAppr01.getReplyCode().equals("4001")) {
						hasfincodefg = true;
						tshareAmSum = tshareAmSum.add(lNegAppr01.getApprAmt());
					}
				}
				if (hasfincodefg && tshareAmSum.compareTo(txAmt) != 0) {// 金額不符
					hasfincodefg = false;
				}
			}
		}

		return hasfincodefg;
	}

}
