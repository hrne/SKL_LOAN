package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.CustNoticeId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.FacMainService;
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
	private FacMainService sFacMainService;

	@Autowired
	public CustNoticeService sCustNoticeService;

	@Autowired
	private TxToDoCom txToDoCom;

	@Autowired
	public L4721Report l4721Report;
	@Autowired
	public L4721Report2 l4721Report2;
	@Autowired
	public L4721ServiceImpl sL4721ServiceImpl;
	@Autowired
	public CdReportService sCdReportService;
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
	int checkSendCode = 0;
	String tranCode = "";

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
				.setRptCode("L4721").setRptItem("期款扣款通知(簡訊)").build();
		// 開啟報表
		makeFileText.open(titaVo, reportVo, "簡訊檔.txt");
		ReportVo mailReportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L4721").setRptItem("期款扣款通知(EMail)").build();
		// 開啟報表
		makeFileMail.open(titaVo, mailReportVo, "email檔.txt");

		isAdjDate = Integer.parseInt(titaVo.getParam("sAdjDate")) + 19110000;
		ieAdjDate = Integer.parseInt(titaVo.getParam("eAdjDate")) + 19110000;
		eEntryDate = titaVo.getEntDyI() + 19110000;
//		eEntryDate = ieAdjDate;

		dateUtil.setDate_1(eEntryDate);
		dateUtil.setMons(-6);
		sEntryDate = Integer.parseInt(String.valueOf(dateUtil.getCalenderDay()).substring(0, 6) + "01");

		tranCode = titaVo.getTxcd();

		checkSendCode = checkSendCode(tranCode, titaVo);
		// 利率調整名稱
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

		// txkind = 0 表示全選
		this.kindItem = this.iTxKind == 0 ? "定期機動利率、指數型利率、機動利率、員工利率、按商品別利率變動利率" : titaVo.getParam("TxKindX");

		String[] tmpKindItem = this.kindItem.split("、");

		for (int txkind = 1; txkind <= 5; txkind++) {

			if (this.iTxKind == 0 || txkind == this.iTxKind) {
				List<Map<String, String>> custList = new ArrayList<Map<String, String>>();
				try {
					// 查詢 指定利率調整期間的資料清單
					custList = sL4721ServiceImpl.findAll(txkind, iCustType, isAdjDate, ieAdjDate, prodNos, titaVo);
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.info("L4721ServiceImpl error = " + errors.toString());
				}

				if (custList != null) {

					int cntTrans = 0;
					// custNo 已distinct
					for (Map<String, String> data : custList) {
						// 序號初始化
						this.sno = 0;
						int custNo = parse.stringToInteger(data.get("CustNo"));
						// 先判斷是否為email，才產表，最後才設定寄信
//							if ("Y".equals(isEmail)) {
						this.info("isEmailCust : " + custNo);

						this.sno = l4721Report.exec(titaVo, this.txBuffer, custNo, tmpKindItem[txkind - 1], isAdjDate,
								ieAdjDate, sEntryDate, eEntryDate);

						this.info("sno =" + this.sno);

						cntTrans++;
						if (cntTrans > this.commitCnt) {
							cntTrans = 0;
							this.batchTransaction.commit();
						}

						if (this.sno > 0) {
							CntEmail = CntEmail + 1;

							CustMain tCustMain = custMainService.custNoFirst(custNo, custNo, titaVo);
							if (tCustMain == null) {
								continue;
							}

							String noticeEmail = tCustMain.getEmail();

							// mail
							setMailMFileVO(data, noticeEmail, titaVo);
						}

						// msg
						setTextFileVO(titaVo, custList);
					} // for
				} // if

				this.batchTransaction.commit();

				// letter
				l4721Report2.setBatchTransaction(this.batchTransaction);
				CntPaper = l4721Report2.exec(titaVo, this.txBuffer, custList,
						this.iTxKind == 0 ? tmpKindItem[txkind - 1] : titaVo.getParam("TxKindX"), isAdjDate, ieAdjDate,
						sEntryDate, eEntryDate);

			} // if

		} // for

		if (CntEmail == 0) {
			makeFileMail.put("本日無資料");
		}
		if (CntMsg == 0) {
			makeFileText.put("本日無資料");
		}

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
	private void setTextFileVO(TitaVo titaVo, List<Map<String, String>> iData) throws LogicException {

		for (Map<String, String> r : iData) {

			int iCustNo = parse.stringToInteger(r.get("CustNo"));
			int iFacmNo = parse.stringToInteger(r.get("FacmNo"));

			if (iCustNo == custNoLast && iFacmNo == facmNoLast) {
				return;
			}

			List<Map<String, String>> listL4721Head = new ArrayList<Map<String, String>>();

			try {
				listL4721Head = sL4721ServiceImpl.doQuery(iCustNo, isAdjDate, ieAdjDate, titaVo);
			} catch (Exception e) {
				this.error("bankStatementServiceImpl doQuery = " + e.getMessage());
				throw new LogicException("E9003", "放款本息對帳單及繳息通知單產出錯誤");
			}

			listL4721Head = checkNotice(3, checkSendCode, tranCode, listL4721Head, titaVo);

			Map<String, String> mTmpCustFacm = new HashMap<>();
			List<Map<String, String>> lTmpCustFacm = new ArrayList<Map<String, String>>();

			if (listL4721Head != null && !listL4721Head.isEmpty()) {
				CntMsg = CntMsg + 1;
				
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

				// 設定簡訊
				txToDoCom.setTxBuffer(this.getTxBuffer());
				String dataLines = txToDoCom.getProcessNoteForText(noticePhoneNo,
						"親愛的客戶您好，新光人壽通知您，房貸額度 " + t.get("FacmNo") + " 自" + t.get("rateChangeDate") + "起利率由"
								+ t.get("originRate") + " 調整為" + t.get("newRate") + "，敬請留意帳戶餘額以利扣款。",
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

	}

	// EMAIL通知設定
	private void setMailMFileVO(Map<String, String> tmpCustFacm, String noticeEmail, TitaVo titaVo)
			throws LogicException {

		txToDoCom.setTxBuffer(this.getTxBuffer());

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

	/**
	 * 檢查寄送通知(只檢查指定一種通知)
	 * 
	 * @param noticeType 通知種類(1=Email,2=Letter,3=Message)
	 * @param sendCode   寄送記號(0=不送,1=利率變動通知,2=優先序)
	 * @param formNo     交易代號
	 * @param list       (必須有戶號,額度,交易代號)
	 * @param titaVo
	 * @throws LogicException
	 * @return List<Map<String, String>> (必須有戶號,額度,交易代號)
	 * 
	 */

	// 注意：可能要用複寫
	// 就是如果只有custno 就必須走多筆然後回傳 所以可能要用 for 迴圈 回傳 List
	// 單筆單筆就照就

	private List<Map<String, String>> checkNotice(int noticeType, int sendCode, String formNo,
			List<Map<String, String>> list, TitaVo titaVo) throws LogicException {

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		for (Map<String, String> r : list) {
			// 預設三種通知方式皆為[要通知]
			boolean emailFg = true;
			boolean messageFg = true;
			boolean letterFg = true;

			int custNo = 0;
			int facmNo = 0;

			if (noticeType != 1 || noticeType != 2 || noticeType != 3) {
				this.info("NoticeType is null");
				break;
//				return null;
			}
			if (sendCode != 0 || sendCode != 1 || sendCode != 2) {
				this.info("SendCode is null");
				break;
//				return null;
			}
			if (formNo == "") {
				this.info("FormNo is null");
				break;
//				return null;
			}
			if (r.get("CustNo") == null) {
				this.info("CustNo is null");
				continue;
//				return null;
			}
			if (r.get("FacmNo") == null) {
				this.info("FacmNo is null");
				continue;
//				return null;
			}

			custNo = parse.stringToInteger(r.get("CustNo"));
			facmNo = parse.stringToInteger(r.get("FacmNo"));

			Map<String, String> mapResult = new HashMap<String, String>();

			// 利率調整通知(只會有一種通知)
			if (sendCode == 1) {
				this.info("find FacMain.Notice...");
				FacMainId facMainId = new FacMainId();
				facMainId.setCustNo(custNo);
				facMainId.setFacmNo(facmNo);
				FacMain facMain = sFacMainService.findById(facMainId, titaVo);

				String rateAdjNoticeCode = facMain.getRateAdjNoticeCode();
				this.info("rateAdjNoticeCode = " + rateAdjNoticeCode);
//			1: 電子郵件 
//			2: 書面通知 
//			3: 簡訊通知

				if ("1".equals(rateAdjNoticeCode)) {
					emailFg = true;
					letterFg = false;
					messageFg = false;
				}

				if ("2".equals(rateAdjNoticeCode)) {
					emailFg = false;
					letterFg = true;
					messageFg = false;
				}

				if ("3".equals(rateAdjNoticeCode)) {
					emailFg = false;
					letterFg = false;
					messageFg = true;
				}

			}
			this.info("find CustNotice.Notice...");

			CustNotice custNotice = new CustNotice();

			Slice<CustNotice> sCustNotice = null;
			// 判斷by戶號 by額度
			if (facmNo == 0) {
//			sCustNotice = sCustNoticeService.findCustNoFormNo(custNo, formNo, 0, 999, titaVo);
//			if (sCustNotice != null) {
//				for (CustNotice r : sCustNotice.getContent()) {
//					Map<String, String> t = new HashMap<String, String>();
//					
//					
//					
//				}
//			}

			} else {
				// 最後檢查CustNotice是否通知
				CustNoticeId custNoticeId = new CustNoticeId();
				custNoticeId.setCustNo(custNo);
				custNoticeId.setFacmNo(facmNo);
				custNoticeId.setFormNo(formNo);
				custNotice = sCustNoticeService.findById(custNoticeId, titaVo);
			}
			// 除了custNotice為N以外 其它都為[要通知]

			// Email
			if (emailFg && noticeType == 1) {
				if (custNotice == null || "Y".equals(custNotice.getEmailNotice())) {
					mapResult = r;
				} else {
					mapResult = null;
				}
			}

			// 書面
			if (letterFg && noticeType == 2) {
				if (custNotice == null || "Y".equals(custNotice.getEmailNotice())) {
					mapResult = r;
				} else {
					mapResult = null;
				}
			}

			// 簡訊
			if (messageFg && noticeType == 3) {
				if (custNotice == null || "Y".equals(custNotice.getEmailNotice())) {
					mapResult = r;
				} else {
					mapResult = null;
				}
			}

			// map有值才add
			if (mapResult != null) {
				result.add(mapResult);
			}

		}

		return result;

	}

	/**
	 * 檢查CdReport通知方式
	 * 
	 */
	private int checkSendCode(String tranCode, TitaVo titaVo) throws LogicException {

		this.info("checkSendCode tranCode=" + tranCode);

//		Map<String, String> result = new HashMap<String, String>();

		CdReport cdReport = sCdReportService.FormNoFirst(tranCode, titaVo);

		if (cdReport != null) {
			// 利率調整通知記號SendCode=1
			if (cdReport.getSendCode() == 1) {
				// 利率調整通知 一定是Y
				if ("Y".equals(cdReport.getLetterFg()) && "Y".equals(cdReport.getMessageFg())
						&& "Y".equals(cdReport.getEmailFg())) {
				} else {
					throw new LogicException("E004", "利率調整通知的簡訊,電子郵件,書面應皆為開啟");

				}
			}
//			result.put("SendCode", cdReport.getSendCode() + "");
//			result.put("LetterFg", cdReport.getLetterFg());
//			result.put("MessageFg", cdReport.getMessageFg());
//			result.put("EmailFg", cdReport.getEmailFg());
		}

		return cdReport.getSendCode();

	}

}