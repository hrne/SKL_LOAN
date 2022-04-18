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

@Service("L9704p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9704p extends TradeBuffer {
	@Autowired
	L9704Report l9704report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9704p ");
		this.totaVo.init(titaVo);

		// 輸入參數檢核
		int thisMonth = Integer.parseInt(titaVo.getParam("ThisMonth"));
		int lastMonth = Integer.parseInt(titaVo.getParam("LastMonth"));

		if (lastMonth == 0 || thisMonth == 0) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", titaVo.getTxcd(), titaVo.getParam("TLRNO"), "L9704催收款明細表輸入參數[年月]有誤", titaVo);
			this.addList(this.totaVo);
			return this.sendList();
		}

		thisMonth += 191100;
		lastMonth += 191100;
		this.info("L9704p thisMonth = " + thisMonth);
		this.info("L9704p lastMonth = " + lastMonth);

		this.info("L9704p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9704report.setParentTranCode(parentTranCode);

		l9704report.exec(lastMonth, thisMonth, titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO")+"L9704", "L9704催收款明細表已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}