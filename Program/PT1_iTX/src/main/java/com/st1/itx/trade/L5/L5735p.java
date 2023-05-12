package com.st1.itx.trade.L5;

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

@Service("L5735p")
@Scope("prototype")
/**
 * L5735 建商餘額明細
 * 
 * @author
 * @version 1.0.0
 */
public class L5735p extends TradeBuffer {

	@Autowired
	L5735Report l5735Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txCD = "L5735";
	String txName = "建商餘額明細";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txCD + "p");
		this.totaVo.init(titaVo);

		this.info(txCD + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l5735Report.setParentTranCode(parentTranCode);

		boolean isFinish = false;

		isFinish = l5735Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), txCD + txName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), txCD + txName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}