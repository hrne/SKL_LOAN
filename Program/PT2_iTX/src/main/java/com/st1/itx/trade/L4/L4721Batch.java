package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.springjpa.cm.L4721ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
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
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	private TxToDoCom txToDoCom;

	@Autowired
	private CustMainService custMainService;

	@Autowired
	private MakeReport makeReport;

	@Autowired
	public L4721Report l4721Report;
	@Autowired
	public L4721Report2 l4721Report2;
	@Autowired
	public L4721ServiceImpl sL4721ServiceImpl;

	private int isAdjDate = 0;
	private int ieAdjDate = 0;
	// 利率種類
	private int iTxKind = 0;
	private String kindItem = "";
	private String prodNos = "";

//	輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//	客戶檔 0:個金1:企金2:企金自然人
	private int iCustType = 0;
	private String sendMsg = "";
	private Boolean flag = true;

	int CntPaper = 0;
	int CntEmail = 0;
	int CntMsg = 0;

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

		// 利率種類0 全部(1~5)
		if (this.iTxKind == 0) {
			for (int txkind = 1; txkind <= 5; txkind++) {

				try {
					l4721Report.exec(titaVo, this.txBuffer, mainDataBatxRateChange(titaVo, txkind), this.kindItem);

					dealMessageData(titaVo, l4721Report.lTmpCustFacm);

					l4721Report2.exec(titaVo, this.txBuffer, mainDataBatxRateChange(titaVo, txkind), txkind,
							this.kindItem);
				} catch (LogicException e) {
					sendMsg = e.getErrorMsg();
					flag = false;
				}

			}
		} else {

			try {
				l4721Report.exec(titaVo, this.txBuffer, mainDataBatxRateChange(titaVo, this.iTxKind), this.kindItem);

				dealMessageData(titaVo, l4721Report.lTmpCustFacm);

				l4721Report2.exec(titaVo, this.txBuffer, mainDataBatxRateChange(titaVo, this.iTxKind), this.iTxKind,
						this.kindItem);
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				flag = false;
			}

		}

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
	 * 主要資料來源
	 * 
	 * @param titaVo
	 * @param txkind 利率種類
	 * @throws LogicException
	 */
	private List<Map<String, String>> mainDataBatxRateChange(TitaVo titaVo, int txkind) throws LogicException {

//		List<BatxRateChange> lBatxRateChange = new ArrayList<BatxRateChange>();
//
//		Slice<BatxRateChange> sBatxRateChange = batxRateChangeService.findL4321Report(this.isAdjDate, this.ieAdjDate,
//				custType1, custType2, this.iTxKind, 0, 9, 2, this.index, this.limit, titaVo);
//
//		lBatxRateChange = sBatxRateChange == null ? null : sBatxRateChange.getContent();
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {

			data = sL4721ServiceImpl.findAll(txkind, this.iCustType, this.isAdjDate, this.ieAdjDate, this.prodNos,
					titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L9711ServiceImpl.LoanBorTx error = " + errors.toString());
			return null;

		}

		if (data == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		return data;
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
	 * 處理簡訊資料
	 */
	private void dealMessageData(TitaVo titaVo, List<Map<String, String>> tmpCustFacm) throws LogicException {

		for (Map<String, String> t : tmpCustFacm) {

			TempVo tempVo = new TempVo();
			tempVo = custNoticeCom.getCustNotice("L4721", parse.stringToInteger(t.get("CustNo")),
					parse.stringToInteger(t.get("FacmNo")), titaVo);

			int noticeFlag = parse.stringToInteger(tempVo.getParam("NoticeFlag"));
			String noticePhoneNo = tempVo.getParam("MessagePhoneNo");
			String noticeEmail = tempVo.getParam("EmailAddress");
			String noticeAddress = tempVo.getParam("LetterAddress");

			this.info("noticeFlag : " + noticeFlag);
			this.info("noticePhoneNo : " + noticePhoneNo);
			this.info("noticeEmail : " + noticeEmail);
			this.info("noticeAddress : " + noticeAddress);

			if ("Y".equals(tempVo.getParam("isLetter"))) {
				CntPaper = CntPaper + 1;
			}
			if ("Y".equals(tempVo.getParam("isMessage"))) {
				CntMsg = CntMsg + 1;
				setTextFileVO(t, 0, noticePhoneNo, titaVo);
			}
			if ("Y".equals(tempVo.getParam("isEmail"))) {
				CntEmail = CntEmail + 1;
			}

		}
	}

	/**
	 * 設定簡訊訊息
	 */
	private void setTextFileVO(Map<String, String> tmpCustFacm, int flag, String noticePhoneNo, TitaVo titaVo)
			throws LogicException {
		if (flag == 1) {
			this.info("Delete Text...");
		} else {
			this.info("set Text...");
		}

		custMainService.custNoFirst(parse.stringToInteger(tmpCustFacm.get("CustNo")),
				parse.stringToInteger(tmpCustFacm.get("FacmNo")), titaVo);

		txToDoCom.setTxBuffer(this.getTxBuffer());
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(parse.stringToInteger(tmpCustFacm.get("CustNo")));
		tTxToDoDetail.setFacmNo(parse.stringToInteger(tmpCustFacm.get("FacmNo")));
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("<利率調整通知>");
		tTxToDoDetail.setItemCode("TEXT00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(txToDoCom.getProcessNoteForText(noticePhoneNo,
				"親愛的客戶您好，新光人壽通知您，房貸額度 " + tmpCustFacm.get("FacmNo") + " 自"
						+ makeReport.showRocDate(parse.stringToInteger(tmpCustFacm.get("rateChangeDate"))) + "起利率由"
						+ parse.stringToInteger(tmpCustFacm.get("originRate")) + "% 調整為"
						+ parse.stringToInteger(tmpCustFacm.get("newRate")) + "%，敬請留意帳戶餘額以利扣款。",
				this.getTxBuffer().getMgBizDate().getTbsDy()));

		txToDoCom.addDetail(true, flag, tTxToDoDetail, titaVo);
	}

}