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

@Service("L9731p")
@Scope("prototype")
/**
 * 
 * 
 * @author
 * @version 1.0.0
 */
public class L9731p extends TradeBuffer {

	@Autowired
	L9731Report l9731Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public WebClient webClient;

	String TXCD = "L9731";
	String TXName = "人工檢核工作表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + TXCD + "p");
		this.totaVo.init(titaVo);

		this.info(TXCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9731Report.setParentTranCode(parentTranCode);

		int iYear = Integer.valueOf(titaVo.getParam("InputYear")) + 1911;
		int iMonth = Integer.valueOf(titaVo.getParam("InputMonth"));
		int iYearMonth = iYear * 100 + iMonth;

		this.info("iYearMonth= " + iYearMonth);

		boolean isFinish = l9731Report.exec(titaVo, iYearMonth);
		
		

		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));

		String tradeName = "";

		for (int i = 1; i <= totalItem; i++) {
			if (titaVo.getParam("BtnShell" + i).equals("V")) {
				tradeName += (titaVo.getParam("TradeName" + i) + "、");
			}
		}

		tradeName = tradeName.substring(0, tradeName.length() - 1);


		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), TXCD + TXName + "(" + tradeName + ")已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), TXCD + TXName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}