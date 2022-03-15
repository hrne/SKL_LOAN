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

/**
 * L9720p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9720p")
@Scope("prototype")
public class L9720p extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9720p.class);

	@Autowired
	L9720Report l9720Report;

	@Autowired
	L9720Report2 l9720Report2;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	String TXCD = "L9720";
	String TXName = "理財型商品續約檢核報表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + TXCD + "p");
		this.totaVo.init(titaVo);

		this.info(TXCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9720Report.setParentTranCode(parentTranCode);
		l9720Report2.setParentTranCode(parentTranCode);

		// excel
		boolean isFinish1 = l9720Report.exec(titaVo);

		// pdf
		boolean isFinish2 = l9720Report2.exec(titaVo);

		if (isFinish1 && isFinish2) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), TXCD + TXName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), TXCD + TXName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}