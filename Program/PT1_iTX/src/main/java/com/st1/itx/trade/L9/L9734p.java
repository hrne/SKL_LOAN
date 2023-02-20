package com.st1.itx.trade.L9;

import java.util.ArrayList;

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

@Service("L9734p")
@Scope("prototype")
/**
 * 
 * 
 * @author
 * @version 1.0.0
 */
public class L9734p extends TradeBuffer {

	@Autowired
	L9734Report l9734Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public WebClient webClient;

	String txCD = "L9734";
	String txName = "覆審報表產製";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txCD + "p");
		this.totaVo.init(titaVo);

		this.info(txCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9734Report.setParentTranCode(parentTranCode);

		int iYearMonth = Integer.valueOf(titaVo.getParam("YearMonth")) + 191100;

		this.info("iYearMonth= " + iYearMonth);

		boolean isFinish = false;

		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));

		String tradeName = "";

		for (int i = 1; i <= totalItem; i++) {

			if (titaVo.getParam("BtnShell" + i).equals("V")) {

				tradeName += (titaVo.getParam("ReportName" + i) + "、");
				isFinish = l9734Report.exec(titaVo, iYearMonth, Integer.valueOf(titaVo.getParam("Condition" + i)));
			}
		}

		if (isFinish) {
			tradeName = tradeName.substring(0, tradeName.length() - 1);
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO") + txCD, txCD + txName + "(" + tradeName + ")已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO") + txCD, txCD + txName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}