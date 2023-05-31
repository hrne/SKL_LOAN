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
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.TxToDoCom;
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

	private int isAdjDate = 0;
	private int ieAdjDate = 0;
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
	String noticeEmail = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4721Batch ");
		this.totaVo.init(titaVo);
		// 設定分頁、筆數
		this.index = titaVo.getReturnIndex();
		this.limit = Integer.MAX_VALUE;
		this.iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		this.iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		this.isAdjDate = Integer.parseInt(titaVo.getParam("sAdjDate")) + 19110000;
		this.ieAdjDate = Integer.parseInt(titaVo.getParam("eAdjDate")) + 19110000;

		for (int i = 1; i <= 50; i++) {
			if (titaVo.getParam("ProdNo" + i).length() != 0) {
				this.prodNos = this.prodNos + "'" + titaVo.getParam("ProdNo" + i) + "',";
			}
		}

		if (this.prodNos.length() == 0) {
			this.prodNos = "";

		} else {
			this.prodNos = this.prodNos.substring(0, this.prodNos.length() - 1);
		}
		this.info("this.prodNos=" + this.prodNos);

		this.kindItem = this.iTxKind == 0 ? "定期機動利率、指數型利率、機動利率、員工利率、按商品別利率變動利率" : titaVo.getParam("TxKindX");
		String[] tmpKindItem = this.kindItem.split("、");

		for (int txkind = 1; txkind <= 5; txkind++) {
			CntPaper = 0;

			if (this.iTxKind == 0 || txkind == this.iTxKind) {
				List<Map<String, String>> custList = new ArrayList<Map<String, String>>();
				try {

					custList = sL4721ServiceImpl.findAll(txkind, this.iCustType, this.isAdjDate, this.ieAdjDate,
							this.prodNos, titaVo);

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
											tmpKindItem[txkind - 1]);
									this.info("CustNo =" + custNoTmp);
									this.info("sno =" + this.sno);

									cntTrans++;

									if (cntTrans > this.commitCnt) {
										cntTrans = 0;
										this.batchTransaction.commit();
									}

								}

								if (this.sno > 0) {
									setMailMFileVO(data, this.noticeEmail, titaVo);
								}

								// 書面通知
							} else if ("Y".equals(tempVo.getParam("isLetter"))) {
								if (custNoTmp != custNoLast) {
									CntPaper = CntPaper + 1;
									letterCustList.add(data);
								}

								// 簡訊通知
							} else if ("Y".equals(tempVo.getParam("isMessage"))) {
								dealMessageData(titaVo, data);
							}

						} catch (LogicException e) {
							sendMsg = e.getErrorMsg();
							flag = false;
						}

						custNoLast = custNoTmp;
				

					} // for
				} // if

			} // if

			this.info("CntPaper = " + CntPaper);
			if (CntPaper > 0) {
				l4721Report2.exec(titaVo, this.txBuffer, letterCustList, tmpKindItem[txkind - 1]);
			}
		} // for

		String msg = "";

		if (this.iTxKind != 0) {
			msg = "書面通知筆數：" + CntPaper + "筆,電子郵件通知筆數：" + CntEmail + "筆,簡訊通知筆數：" + CntMsg + "筆。";
		}
		webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "", "", titaVo.getParam("TLRNO"), msg,
				titaVo);

		// 送出通知訊息
		sendMessage(titaVo);

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
	 * 簡訊通知 資料處理
	 */
	private void dealMessageData(TitaVo titaVo, Map<String, String> iData) throws LogicException {

		int ieday = titaVo.getEntDyI() + 19110000;
		dateUtil.setDate_1(ieday);
		dateUtil.setMons(-6);
		// 起日為會計日前六個月的一日
		int isday = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");

		int iCustNo = parse.stringToInteger(iData.get("CustNo"));
		int iFacmNo = parse.stringToInteger(iData.get("FacmNo"));

		if (iCustNo == custNoLast && iFacmNo == facmNoLast) {
			return;
		}
		Map<String, String> mTmpCustFacm = new HashMap<>();
		List<Map<String, String>> lTmpCustFacm = new ArrayList<Map<String, String>>();

		List<Map<String, String>> listL4721Head = new ArrayList<Map<String, String>>();

		try {
			listL4721Head = sL4721ServiceImpl.doQuery(iCustNo, isday, ieday, titaVo);
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
		this.noticeEmail = tempVo.getParam("EmailAddress");

		this.info("isMessageCust : " + iCustNo);
		this.info("isMessageLastCust : " + custNoLast);
		this.info("lTmpCustFacm : " + lTmpCustFacm.toString());
		for (Map<String, String> t : lTmpCustFacm) {
			CntMsg = CntMsg + 1;
			setTextFileVO(t, noticePhoneNo, titaVo);
		}

		custNoLast = iCustNo;
		facmNoLast = iFacmNo;

//		return isNotice;
	}

	/**
	 * 設定簡訊訊息
	 */
	private void setTextFileVO(Map<String, String> tmpCustFacm, String noticePhoneNo, TitaVo titaVo)
			throws LogicException {

		txToDoCom.setTxBuffer(this.getTxBuffer());

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(parse.stringToInteger(tmpCustFacm.get("CustNo")));
		tTxToDoDetail.setFacmNo(parse.stringToInteger(tmpCustFacm.get("FacmNo")));
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setExcuteTxcd("L4721");
		tTxToDoDetail.setDtlValue("<利率調整通知>");
		tTxToDoDetail.setItemCode("TEXT00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(txToDoCom.getProcessNoteForText(noticePhoneNo,
				"親愛的客戶您好，新光人壽通知您，房貸額度 " + tmpCustFacm.get("FacmNo") + " 自" + tmpCustFacm.get("rateChangeDate") + "起利率由"
						+ tmpCustFacm.get("originRate") + " 調整為" + tmpCustFacm.get("newRate") + "，敬請留意帳戶餘額以利扣款。",
				this.getTxBuffer().getMgBizDate().getTbsDy()));

		txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

	}

	// EMAIL通知設定
	private void setMailMFileVO(Map<String, String> tmpCustFacm, String noticeEmail, TitaVo titaVo)
			throws LogicException {

		txToDoCom.setTxBuffer(this.getTxBuffer());

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(parse.stringToInteger(tmpCustFacm.get("CustNo")),
				parse.stringToInteger(tmpCustFacm.get("CustNo")), titaVo);

		String dataLines = "<" + noticeEmail + ">";

		// L4711>String[] processNotes = tTxToDoDetail.getProcessNote().split(",");
		// L4711>String email = processNotes[2];
		// L4711>long pdfno = Long.parseLong(processNotes[3]);
		// 若未來有修改此段落時,請一併修改L4711
		dataLines += "\"H1\",\"" + tCustMain.getCustId() + "\"," + noticeEmail + "," + this.sno
				+ ",\"親愛的客戶您好，新光人壽通知您，房貸利率調整，敬請留意帳戶餘額以利扣款。\",\"" + this.getTxBuffer().getMgBizDate().getTbsDy() + "\"";

		dataLines = "親愛的客戶您好，新光人壽通知您，房貸利率調整，敬請留意帳戶餘額以利扣款。";

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(parse.stringToInteger(tmpCustFacm.get("CustNo")));
		tTxToDoDetail.setFacmNo(0);
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setExcuteTxcd("L4721");
		tTxToDoDetail.setDtlValue("<利率調整通知>");
		tTxToDoDetail.setItemCode("MAIL00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(dataLines);

		txToDoCom.addDetail(true, 9, tTxToDoDetail, titaVo);
	}

}