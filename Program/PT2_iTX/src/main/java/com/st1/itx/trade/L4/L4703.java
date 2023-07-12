package com.st1.itx.trade.L4;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustNotice;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.db.service.springjpa.cm.L4703ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9703ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.MailVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * CustNo=9,7<br>
 * RepayCode=9,1<br>
 * NoticeFlag=9,1<br>
 * END=X,1<br>
 */

@Service("L4703")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4703 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	CollListService collListService;

	@Autowired
	public CustNoticeCom custNoticeCom;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public CustNoticeService custNoticeService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public L4703ServiceImpl l4703ServiceImpl;

	@Autowired
	public L9703ServiceImpl l9703ServiceImpl;
	@Autowired
	private MakeFile makeFileText;
	@Autowired
	private MakeFile makeFileMail;

	@Autowired
	public WebClient webClient;

	private String noticePhoneNo = "";
	private String noticeEmail = "";
	private int iEntryDate = 0;
	private String sEntryDate = "";
	private int totalCnt = 0;
	private int deleteCnt = 0;
	private int wkCalDy = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4703 ");
		this.totaVo.init(titaVo);
		txToDoCom.setTxBuffer(txBuffer);
		wkCalDy = dateUtil.getNowIntegerForBC();
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L4703").setRptItem("期款扣款通知").build();
		// 開啟報表
		makeFileText.open(titaVo, reportVo, "簡訊檔.txt");
		ReportVo mailReportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L4703").setRptItem("期款扣款通知").build();
		// 開啟報表
		makeFileMail.open(titaVo, mailReportVo, "email檔.txt");
		if (titaVo.isHcodeErase()) {
//			刪除TxToDoDetail
			dele("TEXT00", titaVo);
			dele("MAIL00", titaVo);
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6001", titaVo.getTlrNo(),
					"L4703  訂正完畢，總筆數：" + totalCnt + ", 刪除筆數：" + deleteCnt, titaVo);
		} else {

			iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;

			sEntryDate = ("" + iEntryDate).substring(0, 4) + "/" + ("" + iEntryDate).substring(4, 6) + "/"
					+ ("" + iEntryDate).substring(6);

			TempVo tempVo = new TempVo();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
			this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
			this.limit = 500;

//		產生滯繳通知單RPDL00
//		1.每日入帳作業L4002->L420B入帳完成後寫一筆進[應處理清單]提醒櫃員執行L4703抓取collList滯繳天數>=7者
//		2.額外(個別)通知：優先順序為客戶報表檔設定，未設定者為簡訊、email。

//		由L6001連動者為2.整批，頁面點入者為1.個別
			int functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
			int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
			int facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

			titaVo.putParam("UnpaidCond", 2); // 2 滯繳日數
			// titaVo.putParam("UnpaidDaySt", 7); // 輸入
			// titaVo.putParam("UnpaidDayEd", 30); // 輸入
			titaVo.putParam("UnpaidTermSt", 0);
			titaVo.putParam("UnpaidTermEd", 0);
			titaVo.putParam("RepayType", 0);
			titaVo.putParam("CustType", 0);
			titaVo.putParam("NoticeFlag", 1);

			switch (functionCode) {
//		1.個別
			case 1:
				tempVo = new TempVo();
				tempVo = custNoticeCom.getCustNotice("L9703", custNo, facmNo, titaVo);

				String tran = titaVo.getTxCode().isEmpty() ? "L9705" : titaVo.getTxCode();

				if (custNoticeCom.checkIsLetterSendable(titaVo.get("CUSTNO"), custNo, facmNo, tran, titaVo)) {

					noticePhoneNo = tempVo.getParam("MessagePhoneNo");
					noticeEmail = tempVo.getParam("EmailAddress");
					if ("2".equals(titaVo.getParam("NoticeFlag"))) {
						if (!"".equals(noticePhoneNo)) {
							setTextFileVO(custNo, facmNo, titaVo);
						}
					}
					if ("3".equals(titaVo.getParam("NoticeFlag"))) {
						if (!"".equals(noticeEmail)) {
							setEMailFileVO(custNo, facmNo, titaVo);
						}
					}
				}
//		2.整批
//		若設定寄送簡訊或mail，則額外多寄，但信依舊要寄
			case 2:
				List<Map<String, String>> L9703List = null;

				try {
					L9703List = l9703ServiceImpl.queryForNotice(titaVo);

				} catch (Exception e) {
					this.info("L9711ServiceImpl.LoanBorTx error = " + e.toString());
				}

				if (L9703List != null && L9703List.size() != 0) {
					for (Map<String, String> tL9703Vo : L9703List) {

						int custNo1 = parse.stringToInteger(tL9703Vo.get("F4"));
						int facmNo1 = parse.stringToInteger(tL9703Vo.get("F5"));

						tempVo = new TempVo();
						tempVo = custNoticeCom.getCustNotice("L4703", custNo1, facmNo1, titaVo);

						noticePhoneNo = tempVo.getParam("MessagePhoneNo");
						noticeEmail = tempVo.getParam("EmailAddress");
						if (!"".equals(noticePhoneNo)) {
							setTextFileVO(custNo1, facmNo1, titaVo);
						} else if (!"".equals(noticeEmail)) {
							setEMailFileVO(custNo1, facmNo1, titaVo);
						}
					}
				}
				break;
			default:
				break;
			}

			makeFileText.close();
			makeFileMail.close();
//		滯繳明細表 通知方式為書信者，於9703產出
//		l9703p.run(titaVo);
			MySpring.newTask("L9703p", this.txBuffer, titaVo);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setTextFileVO(int custNo, int facmNo, TitaVo titaVo) throws LogicException {

		List<CustNotice> lCustNoticeC = new ArrayList<CustNotice>();

		Slice<CustNotice> sCustNoticeC = null;

		sCustNoticeC = custNoticeService.findCustNo(custNo, this.index, this.limit);

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(custNo, custNo);

		ArrayList<String> dataList = new ArrayList<String>();
		String dataLines = "";

		lCustNoticeC = sCustNoticeC == null ? null : sCustNoticeC.getContent();

		if (lCustNoticeC != null && lCustNoticeC.size() != 0) {
			for (CustNotice tCustNotice : lCustNoticeC) {

				if ("L4703".equals(tCustNotice.getFormNo()) && "Y".equals(tCustNotice.getMsgNotice())) {
					dataLines = "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticePhoneNo
							+ "\",\"親愛的客戶，您好：房貸繳款通知；如已繳納則無須理會本訊息。新光人壽關心您。”,\"" + sEntryDate + "\"";
					dataList.add(dataLines);

					TxToDoDetail tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setCustNo(custNo);
					tTxToDoDetail.setFacmNo(facmNo);
					tTxToDoDetail.setBormNo(0);
					tTxToDoDetail.setDtlValue("<滯繳通知>");
					tTxToDoDetail.setItemCode("TEXT00");
					tTxToDoDetail.setStatus(0);
					tTxToDoDetail.setProcessNote(dataLines);
					makeFileText
							.put(parse.IntegerToString(custNo, 7) + "-" + parse.IntegerToString(facmNo, 3) + dataLines);
					txToDoCom.addDetail(false, 9, tTxToDoDetail, titaVo);
				}
			}
		}
	}

	private void setEMailFileVO(int custNo, int facmNo, TitaVo titaVo) throws LogicException {

		List<CustNotice> lCustNoticeC = new ArrayList<CustNotice>();

		Slice<CustNotice> sCustNoticeC = null;

		sCustNoticeC = custNoticeService.findCustNo(custNo, this.index, this.limit);

		lCustNoticeC = sCustNoticeC == null ? null : sCustNoticeC.getContent();

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(custNo, custNo);

		ArrayList<String> dataList = new ArrayList<String>();
		String dataLines = "";

		if (lCustNoticeC != null && lCustNoticeC.size() != 0) {
			for (CustNotice tCustNotice : lCustNoticeC) {

				if ("L4703".equals(tCustNotice.getFormNo()) && "Y".equals(tCustNotice.getEmailNotice())) {
					MailVo mailVo = new MailVo();
					String processNote = mailVo.generateProcessNotes(noticeEmail, "滯繳通知",
							"親愛的客戶，您好：房貸繳款通知；如已繳納則無須理會本訊息。新光人壽關心您。", 0);

					dataLines = "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticeEmail
							+ "\",\"親愛的客戶，您好：房貸繳款通知；如已繳納則無須理會本訊息。新光人壽關心您。”,\"" + sEntryDate + "\"";
					dataList.add(dataLines);

					TxToDoDetail tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setCustNo(custNo);
					tTxToDoDetail.setFacmNo(facmNo);
					tTxToDoDetail.setBormNo(0);
					tTxToDoDetail.setDtlValue("<滯繳通知>");
					tTxToDoDetail.setItemCode("MAIL00");
					tTxToDoDetail.setStatus(0);
					tTxToDoDetail.setProcessNote(processNote);
					makeFileMail
							.put(parse.IntegerToString(custNo, 7) + "-" + parse.IntegerToString(facmNo, 3) + dataLines);

					txToDoCom.addDetail(false, 9, tTxToDoDetail, titaVo);
				}
			}
		}
	}

//	刪除TxToDoDetail 同L4454 須同步更改
	private void dele(String itemCode, TitaVo titaVo) throws LogicException {
//		刪除未處理
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.findTxNoEq(itemCode, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), parse.stringToInteger(titaVo.getOrgTno()), 0, Integer.MAX_VALUE,
				titaVo);
		if (slTxToDoDetail != null) {
			for (TxToDoDetail tTxToDoDetail : slTxToDoDetail.getContent()) {
				totalCnt++;
				if (tTxToDoDetail.getStatus() == 0) {
					deleteCnt++;
					try {
						this.info("DeleteAll...");
						txToDoCom.addDetail(true, 1, tTxToDoDetail, titaVo);
					} catch (LogicException e) {
						this.info("DeleteAll Error : " + e.getErrorMsg());
					}
				}
			}
		}
	}
}