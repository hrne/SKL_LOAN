package com.st1.itx.trade.LM;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;

@Service("LM016p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LM016p extends TradeBuffer {

	@Autowired
	LM016Report lM016Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM016p");
		this.totaVo.init(titaVo);

		this.info("LM016p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lM016Report.setParentTranCode(parentTranCode);

		int iAcDate = Integer.parseInt(titaVo.getEntDy());

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("LM016", 60) + iAcDate;

		this.info("ntxbuf = " + ntxbuf);

		boolean isFinish = lM016Report.exec(titaVo);
		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf, "LM016寬限條件控管繳息表 已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf, "LM016寬限條件控管繳息表 查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}