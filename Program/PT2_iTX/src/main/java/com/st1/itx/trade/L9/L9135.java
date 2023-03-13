package com.st1.itx.trade.L9;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9135ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9135ServiceImpl2;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9135")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9135 extends TradeBuffer {

	@Autowired
	L9135Report l9135Report;
	@Autowired
	L9135ServiceImpl l9135ServiceImpl;
	@Autowired
	L9135ServiceImpl2 l9135ServiceImpl2;
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9135 ");

		this.totaVo.init(titaVo);

		this.info("L9135 titaVo.getTxcd() = " + titaVo.getTxcd());

		String infoNotification = "";

		String parentTranCode = titaVo.getTxcd();

		l9135Report.setParentTranCode(parentTranCode);

		List<Map<String, String>> l9135List = null;

		int acDate = Integer.valueOf(titaVo.getParam("AcDate"));

		this.info("acDate=" + acDate);

		try {

			l9135List = l9135ServiceImpl.findAll(titaVo);

		} catch (Exception e) {

			this.info("L9135ServiceImpl.LoanBorTx error = " + e.toString());

		}

		if (l9135List != null && !l9135List.isEmpty()) {

			this.info("active L9135report data detail");
			infoNotification = "L9135 銀行存款媒體明細表(總帳) 及媒體檔";
			l9135Report.exec(titaVo, l9135List, acDate);

		} else {

			infoNotification = "L9135 查無資料";

		}
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO") + "L9135", infoNotification, titaVo);

		this.addList(this.totaVo);

		return this.sendList();
	}

}