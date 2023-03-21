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

/**
 * L9137
 * 
 * @author Ted
 * @version 1.0.0
 */
@Service("L9137")
@Scope("prototype")
public class L9137 extends TradeBuffer {

	@Autowired
	L9137Report L9137report;

	@Autowired
	Parse parse;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9137 ");

		String reportCode = "";
		
		if ("L6101".equals(titaVo.getTxcd())) {
			reportCode = "2";
		} else {
			reportCode = titaVo.getParam("inputShowType");
		}

		// 2：全部印
		if ("2".equals(reportCode)) {

			L9137report.exec(titaVo, "0");
			L9137report.exec(titaVo, "1");
		} else {
			L9137report.exec(titaVo, reportCode);
		}

		String infoNotification = "L9137 放款餘額總表 已完成";

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO") + "L9137", infoNotification, titaVo);

		this.addList(this.totaVo);

		return this.sendList();
	}

}