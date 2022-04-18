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

@Service("L9718p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9718p extends TradeBuffer {

	@Autowired
	L9718Report l9718Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	String TXCD = "L9718";
	String TXName = "逾期放款催繳處理結果";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + TXCD + "p");
		this.totaVo.init(titaVo);

		this.info(TXCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9718Report.setParentTranCode(parentTranCode);

		boolean isFinish = l9718Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO")+"L9718", TXCD + TXName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO")+"L9718", TXCD + TXName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}