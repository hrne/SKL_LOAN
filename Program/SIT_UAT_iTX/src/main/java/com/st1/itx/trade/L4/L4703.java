package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
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
	private static final Logger logger = LoggerFactory.getLogger(L4703.class);

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
	
	private int noticeFlag = 0;
	private String noticePhoneNo = "";
	private String noticeEmail = "";
	private String noticeAddress = "";
	private int iEntryDate = 0;
	private String sEntryDate = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4703 ");
		this.totaVo.init(titaVo);

		iEntryDate = this.getTxBuffer().getTxCom().getTbsdyf();

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

		/*
		 * L9703 titaVo 
		 * ["#R1+@會計日期",#AcctDate],
		 * ["#R2+@戶號",#CustNo,"-",#FacmNo],
		 * ["#R3+@滯繳條件",#UnpaidCond,#UnpaidCondx],
		 * ["#R4+@滯繳期數",#UnpaidTermSt,"~",#UnpaidTermEd],
		 * ["#R5+@滯繳日數",#UnpaidDaySt,"~",#UnpaidDayEd],
		 * ["#R6+@繳款方式",#RepayType,#RepayTypex],
		 * ["#R7+@戶別",#CustType,#CustTypex],
		 */

		titaVo.putParam("AcctDate", this.getTxBuffer().getTxCom().getTbsdy());
		titaVo.putParam("UnpaidCond", 2); // 2 滯繳日數
		titaVo.putParam("UnpaidDaySt", 7);
		titaVo.putParam("UnpaidDayEd", 30);
		titaVo.putParam("UnpaidTermEd", 0);
		titaVo.putParam("RepayType", 0);
		titaVo.putParam("CustType", 0);
		titaVo.putParam("NoticeFlag", 1);

		switch (functionCode) {
//		1.個別
		case 1:
			tempVo = new TempVo();
			tempVo = custNoticeCom.getReportCode("L9703", custNo, facmNo);

			noticeFlag = parse.stringToInteger(tempVo.getParam("ReportCode"));
			noticePhoneNo = tempVo.getParam("ReportPhoneNo");
			noticeEmail = tempVo.getParam("ReportEmailAd");
			noticeAddress = tempVo.getParam("ReportAddress");

			this.info("noticeFlag : " + parse.stringToInteger(tempVo.getParam("ReportCode")));
			this.info("noticePhoneNo : " + tempVo.getParam("ReportPhoneNo"));
			this.info("noticeEmail : " + tempVo.getParam("ReportEmailAd"));
			this.info("noticeAddress : " + tempVo.getParam("ReportAddress"));

			if (!"".equals(noticePhoneNo)) {
				setTextFileVO(custNo, facmNo, titaVo);
			} else if(!"".equals(noticeEmail)) {
				setEMailFileVO(custNo, facmNo, titaVo);
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
					tempVo = custNoticeCom.getReportCode("L9703", custNo1, facmNo1);

					noticeFlag = parse.stringToInteger(tempVo.getParam("ReportCode"));
					noticePhoneNo = tempVo.getParam("ReportPhoneNo");
					noticeEmail = tempVo.getParam("ReportEmailAd");
					noticeAddress = tempVo.getParam("ReportAddress");

					this.info("noticeFlag : " + parse.stringToInteger(tempVo.getParam("ReportCode")));
					this.info("noticePhoneNo : " + tempVo.getParam("ReportPhoneNo"));
					this.info("noticeEmail : " + tempVo.getParam("ReportEmailAd"));
					this.info("noticeAddress : " + tempVo.getParam("ReportAddress"));

					if (!"".equals(noticePhoneNo)) {
						setTextFileVO(custNo, facmNo, titaVo);
					} else if(!"".equals(noticeEmail)) {
						setEMailFileVO(custNo, facmNo, titaVo);
					}
				}

//				TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
//
//				tTxToDoDetailId.setItemCode("RPDL00");
//				tTxToDoDetailId.setCustNo(0);
//				tTxToDoDetailId.setFacmNo(0);
//				tTxToDoDetailId.setBormNo(0);
//				tTxToDoDetailId.setItemCode(" ");
//
//				TxToDoCom tTxToDoCom = new TxToDoCom();
//
//				tTxToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
			}
			break;
		default:
			break;
		}

		this.info("AcctDate ... " + titaVo.getParam("AcctDate"));
		this.info("UnpaidCond ... " + titaVo.getParam("UnpaidCond"));
		this.info("UnpaidDaySt ... " + titaVo.getParam("UnpaidDaySt"));
		this.info("UnpaidDayEd ... " + titaVo.getParam("UnpaidDayEd"));
		this.info("RepayType ... " + titaVo.getParam("RepayType"));
		this.info("CustType ... " + titaVo.getParam("CustType"));
		this.info("NoticeFlag ... " + titaVo.getParam("NoticeFlag"));

//		滯繳明細表 通知方式為書信者，於9703產出
//		l9703p.run(titaVo);
		MySpring.newTask("L9703p", this.txBuffer, titaVo);

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

				if ("L9703".equals(tCustNotice.getFormNo()) && "Y".equals(tCustNotice.getMsgNotice())) {
					dataLines = "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticePhoneNo
							+ "\",\"親愛的客戶，繳款通知；新光人壽關心您。”,\"" + sEntryDate + "\"";
					dataList.add(dataLines);

					TxToDoDetail tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setCustNo(custNo);
					tTxToDoDetail.setFacmNo(facmNo);
					tTxToDoDetail.setBormNo(0);
					tTxToDoDetail.setDtlValue("<Text>");
					tTxToDoDetail.setItemCode("TEXT00");
					tTxToDoDetail.setStatus(0);
					tTxToDoDetail.setProcessNote(dataLines);

					txToDoCom.addDetail(false, 0, tTxToDoDetail, titaVo);
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

				if ("L9703".equals(tCustNotice.getFormNo()) && "Y".equals(tCustNotice.getEmailNotice())) {
					dataLines = "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticeEmail
							+ "\",\"親愛的客戶，繳款通知；新光人壽關心您。”,\"" + sEntryDate + "\"";
					dataList.add(dataLines);

					TxToDoDetail tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setCustNo(custNo);
					tTxToDoDetail.setFacmNo(facmNo);
					tTxToDoDetail.setBormNo(0);
					tTxToDoDetail.setDtlValue("<EMail>");
					tTxToDoDetail.setItemCode("MAIL00");
					tTxToDoDetail.setStatus(0);
					tTxToDoDetail.setProcessNote(dataLines);

					txToDoCom.addDetail(false, 0, tTxToDoDetail, titaVo);
				}
			}
		}
	}
}