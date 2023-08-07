package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L4721ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.MailVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4721Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4721Batch extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public WebClient webClient;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	private CustNoticeCom custNoticeCom;

	@Autowired
	private CustMainService custMainService;

	@Autowired
	private TxToDoCom txToDoCom;

	@Autowired
	public L4721Report l4721Report;
	@Autowired
	public L4721Report2 l4721Report2;
	@Autowired
	public L4721ServiceImpl sL4721ServiceImpl;
	@Autowired
	private MakeReport makeReport;
	@Autowired
	private MakeFile makeFileText;
	@Autowired
	private MakeFile makeFileMail;
	private int wkCalDy = 0;

	// 利率調整日
	private int isAdjDate = 0;
	private int ieAdjDate = 0;
	// 入帳日期
	private int sEntryDate = 0;
	private int eEntryDate = 0;
	// 利率種類
	private int iTxKind = 0;
	private String kindItem = "";
	private String prodNos = "";

	private List<Map<String, String>> letterCustList = new ArrayList<Map<String, String>>();

//	輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//	客戶檔 0:個金1:企金2:企金自然人
	private int iCustType = 0;
	private String sendMsg = "";
	private Boolean flag = true;
	private int custNoLast = 0;
	private int facmNoLast = 0;
	private long sno = 0;
	int CntPaper = 0;
	int CntEmail = 0;
	int CntMsg = 0;
	int commitCnt = 50;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4721Batch ");
		this.totaVo.init(titaVo);
		// 設定分頁、筆數
		this.index = titaVo.getReturnIndex();
		this.limit = Integer.MAX_VALUE;
		iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		wkCalDy = dateUtil.getNowIntegerForBC();
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L8102").setRptItem("期款扣款通知(簡訊)").build();
		// 開啟報表
		makeFileText.open(titaVo, reportVo, "簡訊檔.txt");
		ReportVo mailReportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L8102").setRptItem("期款扣款通知(EMail)").build();
		// 開啟報表
		makeFileMail.open(titaVo, mailReportVo, "email檔.txt");

		isAdjDate = Integer.parseInt(titaVo.getParam("sAdjDate")) + 19110000;
		ieAdjDate = Integer.parseInt(titaVo.getParam("eAdjDate")) + 19110000;
		eEntryDate = titaVo.getEntDyI() + 19110000;

		dateUtil.setDate_1(eEntryDate);
		dateUtil.setMons(-6);
		sEntryDate = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");

		for (int i = 1; i <= 50; i++) {
			if (titaVo.getParam("ProdNo" + i).length() != 0) {
				prodNos = prodNos + "'" + titaVo.getParam("ProdNo" + i) + "',";
			}
		}

		if (prodNos.length() == 0) {
			prodNos = "";

		} else {
			prodNos = prodNos.substring(0, prodNos.length() - 1);
		}
		this.info("this.prodNos=" + prodNos);

		this.kindItem = this.iTxKind == 0 ? "定期機動利率、指數型利率、機動利率、員工利率、按商品別利率變動利率" : titaVo.getParam("TxKindX");
		String[] tmpKindItem = this.kindItem.split("、");

		for (int txkind = 1; txkind <= 5; txkind++) {
	

			if (this.iTxKind == 0 || txkind == this.iTxKind) {
				List<Map<String, String>> custList = new ArrayList<Map<String, String>>();
				try {

					custList = sL4721ServiceImpl.findAll(txkind, iCustType, isAdjDate, ieAdjDate, prodNos, titaVo);

				} catch (Exception e) {

					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.info("L4721ServiceImpl error = " + errors.toString());

				}

				if (custList != null) {

					int cntTrans = 0;
					for (Map<String, String> data : custList) {

						// 序號初始化
						this.sno = 0;
						int custNoTmp = parse.stringToInteger(data.get("CustNo"));

						try {

							TempVo tempVo = new TempVo();
							tempVo = custNoticeCom.getCustNotice("L4721", custNoTmp, 0, titaVo);

							// 先判斷是否為email，才產表，最後才設定寄信
							if ("Y".equals(tempVo.getParam("isEmail"))) {
								this.info("isEmailCust : " + custNoTmp);
								this.info("isEmailLastCust : " + custNoLast);

								if (custNoTmp != custNoLast) {
									CntEmail = CntEmail + 1;

									this.sno = l4721Report.exec(titaVo, this.txBuffer, custNoTmp,
											tmpKindItem[txkind - 1], isAdjDate, ieAdjDate, sEntryDate, eEntryDate);

									this.info("sno =" + this.sno);

									cntTrans++;

									if (cntTrans > this.commitCnt) {
										cntTrans = 0;
										this.batchTransaction.commit();
									}

								}

								if (this.sno > 0) {

									String noticeEmail = tempVo.getParam("EmailAddress");

									setMailMFileVO(data, noticeEmail, titaVo);
								}

								// 書面通知
							} else if ("Y".equals(tempVo.getParam("isLetter"))) {
								if (custNoTmp != custNoLast) {
									CntPaper = CntPaper + 1;
									letterCustList.add(data);
								}

								// 簡訊通知
							} else if ("Y".equals(tempVo.getParam("isMessage"))) {
								setTextFileVO(titaVo, data);
							}

						} catch (LogicException e) {
							sendMsg = e.getErrorMsg();
							flag = false;
						}

						custNoLast = custNoTmp;

					} // for
				} // if

				// 郵局
				if (CntPaper > 0) {
					this.batchTransaction.commit();
					l4721Report2.setBatchTransaction(this.batchTransaction);
					CntPaper = l4721Report2.exec(titaVo, this.txBuffer, letterCustList,
							this.iTxKind == 0 ? tmpKindItem[txkind - 1] : titaVo.getParam("TxKindX"), isAdjDate,
							ieAdjDate, sEntryDate, eEntryDate);

				}
			} // if

		} // for

		String msg = "";

		if (this.iTxKind != 0) {
			msg = "書面通知筆數：" + CntPaper + "筆,電子郵件通知筆數：" + CntEmail + "筆,簡訊通知筆數：" + CntMsg + "筆。";
		}
		webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "", "", titaVo.getParam("TLRNO"), msg,
				titaVo);

		// 送出通知訊息
		sendMessage(titaVo);
		makeFileText.close();
		makeFileMail.close();
		this.addList(this.totaVo);
		return this.sendList();
	}

	/**
	 * 送出通知訊息
	 */
	private void sendMessage(TitaVo titaVo) throws LogicException {
		if (this.flag) {

			// 1 定期機動調整
			// 2 機動指數調整
			// 3 機動非指數調整
			// 4 員工利率調整
			// 5 按商品別調整
			if (this.iTxKind >= 1 && this.iTxKind <= 3) {
				sendMsg = this.iCustType == 1 ? "個金," : "企金,";
			}
			// 企金別(或空白) + 利率種類
			sendMsg = sendMsg + kindItem + "，報表產出完畢。";

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getEmpNot() + "L4721", sendMsg, titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4721", "", sendMsg, titaVo);
		}
	}

	/**
	 * 簡訊通知
	 */
	private void setTextFileVO(TitaVo titaVo, Map<String, String> iData) throws LogicException {

		int iCustNo = parse.stringToInteger(iData.get("CustNo"));
		int iFacmNo = parse.stringToInteger(iData.get("FacmNo"));

		if (iCustNo == custNoLast && iFacmNo == facmNoLast) {
			return;
		}
		Map<String, String> mTmpCustFacm = new HashMap<>();
		List<Map<String, String>> lTmpCustFacm = new ArrayList<Map<String, String>>();

		List<Map<String, String>> listL4721Head = new ArrayList<Map<String, String>>();

		try {
			listL4721Head = sL4721ServiceImpl.doQuery(iCustNo, isAdjDate, ieAdjDate, titaVo);
		} catch (Exception e) {
			this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
			throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
		}

		if (listL4721Head != null && !listL4721Head.isEmpty()) {

			for (Map<String, String> data : listL4721Head) {
				if ("Y".equals(data.get("Flag")) && parse.stringToInteger(data.get("TxEffectDate")) != 0) {

					String rateChangeDate = makeReport.showRocDate(data.get("TxEffectDate"), 0);

					// 原利率
					String originRate = makeReport.formatAmt(data.get("PresentRate"), 2) + "%";
					// 現在利率
					String newRate = makeReport.formatAmt(data.get("AdjustedRate"), 2) + "%";

					mTmpCustFacm = new HashMap<>();
					mTmpCustFacm.put("CustNo", FormatUtil.pad9("" + iCustNo, 7));
					mTmpCustFacm.put("FacmNo", FormatUtil.pad9("" + data.get("FacmNo"), 3));
					mTmpCustFacm.put("rateChangeDate", rateChangeDate);
					mTmpCustFacm.put("originRate", originRate);
					mTmpCustFacm.put("newRate", newRate);
					lTmpCustFacm.add(mTmpCustFacm);
				}
			}

		}

		TempVo tempVo = new TempVo();
		tempVo = custNoticeCom.getCustNotice("L4721", iCustNo, 0, titaVo);

		String noticePhoneNo = tempVo.getParam("MessagePhoneNo");

		this.info("isMessageCust : " + iCustNo);
		this.info("isMessageLastCust : " + custNoLast);
		this.info("lTmpCustFacm : " + lTmpCustFacm.toString());
		for (Map<String, String> t : lTmpCustFacm) {
			CntMsg = CntMsg + 1;

			// 設定簡訊
			txToDoCom.setTxBuffer(this.getTxBuffer());
			String dataLines = txToDoCom
					.getProcessNoteForText(
							noticePhoneNo, "親愛的客戶您好，新光人壽通知您，房貸額度 " + t.get("FacmNo") + " 自" + t.get("rateChangeDate")
									+ "起利率由" + t.get("originRate") + " 調整為" + t.get("newRate") + "，敬請留意帳戶餘額以利扣款。",
							wkCalDy);

			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setCustNo(parse.stringToInteger(t.get("CustNo")));
			tTxToDoDetail.setFacmNo(parse.stringToInteger(t.get("FacmNo")));
			tTxToDoDetail.setBormNo(0);
			tTxToDoDetail.setExcuteTxcd("L4721");
			tTxToDoDetail.setDtlValue("<利率調整通知>");
			tTxToDoDetail.setItemCode("TEXT00");
			tTxToDoDetail.setStatus(0);
			tTxToDoDetail.setProcessNote(dataLines);
			makeFileText.put(parse.IntegerToString(parse.stringToInteger(t.get("CustNo")), 7) + "-"
					+ parse.IntegerToString(parse.stringToInteger(t.get("FacmNo")), 3) + dataLines);

			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

		}

		custNoLast = iCustNo;
		facmNoLast = iFacmNo;

	}

	// EMAIL通知設定
	private void setMailMFileVO(Map<String, String> tmpCustFacm, String noticeEmail, TitaVo titaVo)
			throws LogicException {

		txToDoCom.setTxBuffer(this.getTxBuffer());

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(parse.stringToInteger(tmpCustFacm.get("CustNo")),
				parse.stringToInteger(tmpCustFacm.get("CustNo")), titaVo);

		MailVo mailVo = new MailVo();
		String processNote = mailVo.generateProcessNotes(noticeEmail, "利率調整通知", "親愛的客戶您好，新光人壽通知您，房貸利率調整，敬請留意帳戶餘額以利扣款。",
				this.sno);

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(parse.stringToInteger(tmpCustFacm.get("CustNo")));
		tTxToDoDetail.setFacmNo(0);
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setExcuteTxcd("L4721");
		tTxToDoDetail.setDtlValue("<利率調整通知>");
		tTxToDoDetail.setItemCode("MAIL00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(processNote);
		makeFileMail.put(parse.IntegerToString(parse.stringToInteger(tmpCustFacm.get("CustNo")), 7) + "-"
				+ parse.IntegerToString(0, 3) + processNote);

		txToDoCom.addDetail(true, 9, tTxToDoDetail, titaVo);
	}

}