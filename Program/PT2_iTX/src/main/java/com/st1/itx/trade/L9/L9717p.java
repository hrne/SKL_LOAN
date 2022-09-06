package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9717ServiceImpl.OutputSortBy;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9717p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9717p extends TradeBuffer {

	@Autowired
	L9717Report l9717Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	String TXCD = "L9717";
	String TXName = "逾期及轉催收件統計表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + TXCD + "p");
		this.totaVo.init(titaVo);

		this.info(TXCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9717Report.setParentTranCode(parentTranCode);

		boolean isFinish = false;

		for (OutputSortBy o : OutputSortBy.values()) {
			boolean result = l9717Report.exec(titaVo, o);
			isFinish = result || isFinish; // 只要有任何一次執行是true, 就回傳有資料
		}

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO") + "L9717", TXCD + TXName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO") + "L9717", TXCD + TXName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}