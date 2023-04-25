package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
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
	public L4721Report l4721Report;
	@Autowired
	public L4721Report2 l4721Report2;
//	寄送筆數
//	private int commitCnt = 200;

//	private int isAdjDate = 0;
//	private int ieAdjDate = 0;
	private int iTxKind = 0;
	private int iCustType = 0;
	private String sendMsg = "";
	private Boolean flag = true;

//	輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//	客戶檔 0:個金1:企金2:企金自然人

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4721Batch ");
		this.totaVo.init(titaVo);
		this.iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		this.iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		// 設定分頁、筆數
		this.index = titaVo.getReturnIndex();
		this.limit = Integer.MAX_VALUE;

		Map<String, String> countCustNotice = new HashMap<String, String>();

		if (this.iTxKind == 0) {
			for (int txkind = 1; txkind <= 5; txkind++) {

				try {
					countCustNotice = l4721Report.exec(titaVo, this.txBuffer, txkind, kindItem(txkind));
				} catch (LogicException e) {
					sendMsg = e.getErrorMsg();
					flag = false;
				}

				try {
					l4721Report2.exec(titaVo, this.txBuffer, txkind, kindItem(txkind));
				} catch (LogicException e) {
					sendMsg = e.getErrorMsg();
					flag = false;
				}
			}
		} else {

			try {
				countCustNotice = l4721Report.exec(titaVo, this.txBuffer, this.iTxKind, kindItem(this.iTxKind));
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				flag = false;
			}

			try {
				l4721Report2.exec(titaVo, this.txBuffer, this.iTxKind, kindItem(this.iTxKind));
			} catch (LogicException e) {
				sendMsg = e.getErrorMsg();
				flag = false;
			}
		}

//		this.totaVo.putParam("CntPaper", countCustNotice.get("CntPaper"));
//		this.totaVo.putParam("CntEmail", countCustNotice.get("CntEmail"));
//		this.totaVo.putParam("CntMsg", countCustNotice.get("CntMsg"));

//		this.info("CntPaper=" + countCustNotice.get("CntPaper"));
//		this.info("CntEmail=" +  countCustNotice.get("CntEmail"));
//		this.info("CntMsg=" +  countCustNotice.get("CntMsg"));
		String msg = "書面通知筆數：" + countCustNotice.get("CntPaper") + "筆,電子郵件通知筆數：" + countCustNotice.get("CntEmail")
				+ "筆,簡訊通知筆數：" + countCustNotice.get("CntMsg") + "筆。";
		webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "", "", titaVo.getParam("TLRNO"), msg, titaVo);

		// 送出通知訊息
		sendMessage(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void sendMessage(TitaVo titaVo) throws LogicException {
		if (flag) {
			// 設定訊息

			if (iTxKind >= 1 && iTxKind <= 3) {
				if (this.iCustType == 1) {
					sendMsg = "個金" + sendMsg;
				} else {
					sendMsg = "企金" + sendMsg;
				}
			}

			sendMsg = sendMsg + "," + kindItem(this.iTxKind);

			sendMsg = sendMsg + "，報表產出完畢。";

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getEmpNot() + "L4721", sendMsg, titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4721", "", sendMsg, titaVo);
		}
	}

	private String kindItem(int txKind) {

		String kindItem = "";
		switch (txKind) {
		case 0:
			sendMsg = "定期機動利率、指數型利率、機動利率、員工利率、按商品別利率變動利率";
			break;
		case 1:
			kindItem = "定期機動利率變動資料";
			break;
		case 2:
			kindItem = "指數型利率變動資料";
			break;
		case 3:
			kindItem = "機動利率變動資料";
			break;
		case 4:
			kindItem = "員工利率變動資料";
			break;
		case 5:
			kindItem = "按商品別利率變動資料";
			break;
		default:
			break;
		}
		return kindItem;
	}
}