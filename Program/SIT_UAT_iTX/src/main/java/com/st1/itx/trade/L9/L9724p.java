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

@Service("L9724p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9724p extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9724p.class);

	@Autowired
	L9724Report l9724Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	String TXCD = "L9724";
	String TXName = "應收利息之帳齡分析表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + TXCD + "p");
		this.totaVo.init(titaVo);

		this.info(TXCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9724Report.setParentTranCode(parentTranCode);

		boolean isFinish = l9724Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), TXCD + TXName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), TXCD + TXName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}