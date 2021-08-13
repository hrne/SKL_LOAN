package com.st1.itx.trade.BS;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.trade.L4.L4721Report;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("BS432")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class BS432 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(BS432.class);
	@Autowired
	public Parse parse;
	@Autowired
	public WebClient webClient;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public L4721Report l4721Report;

//	寄送筆數
	private int commitCnt = 200;

	private int iAdjDate = 0;
	private int iTxKind = 0;
	private int iCustType = 0;
	private String sendMsg = "";
	private Boolean flag = true;

//	輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//	客戶檔 0:個金1:企金2:企金自然人

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS432 ");
		this.totaVo.init(titaVo);
		this.iAdjDate = parse.stringToInteger(titaVo.getParam("AdjDate")) + 19110000;
		this.iTxKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		this.iCustType = parse.stringToInteger(titaVo.getParam("CustType"));
		// 設定分頁、筆數
		this.index = titaVo.getReturnIndex();
		this.limit = Integer.MAX_VALUE;


		try {
			l4721Report.exec(titaVo, this.txBuffer);
		} catch (LogicException e) {
			sendMsg = e.getErrorMsg();
			flag = false;
		}

		// 送出通知訊息
		sendMessage(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void sendMessage(TitaVo titaVo) throws LogicException {
		if (flag) {
			// 設定訊息
			if (iTxKind <= 3) {
				if (this.iCustType == 1) {
					sendMsg = "個金，" + sendMsg;
				} else {
					sendMsg = "企金，" + sendMsg;
				}
			}

			switch (this.iTxKind) {
			case 1:
				sendMsg = sendMsg + "，定期機動利率變動資料";
				break;
			case 2:
				sendMsg = sendMsg + "，指數型利率變動資料";
				break;
			case 3:
				sendMsg = sendMsg + "，機動利率變動資料";
				break;
			case 4:
				sendMsg = sendMsg + "，員工利率變動資料";
				break;
			case 5:
				sendMsg = sendMsg + "，按商品別利率變動資料";
				break;
			default:
				break;
			}

			sendMsg = sendMsg + "，報表產出完畢。";

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getEmpNot() + "L4721", sendMsg, titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4721", "", sendMsg, titaVo);
		}
	}
}