package com.st1.itx.trade.L9;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9710p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9710p extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L9710p.class);

	@Autowired
	L9710Report l9710Report;

//	@Autowired
//	L9710Report2 l9710Report1;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9710p ");
//		TxBuffer txbuffer = this.getTxBuffer();
		String infoNotification = "";

		this.totaVo.init(titaVo);

		this.info("L9710p titaVo.getTxcd() = " + titaVo.getTxcd());

		String parentTranCode = titaVo.getTxcd();

		l9710Report.setParentTranCode(parentTranCode);

		List<Map<String, String>> l9710List = l9710Report.exec(titaVo);

		if (l9710List != null && !l9710List.isEmpty()) {

			infoNotification = "L9710寬限到期明細表已完成";

		} else {

			infoNotification = "L9710寬限到期明細表查無資料";

		}

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), infoNotification, titaVo);

		this.addList(this.totaVo);

		return this.sendList();
	}

}