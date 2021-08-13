package com.st1.itx.trade.L9;

import java.util.ArrayList;

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

@Service("L9714p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9714p extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L9714p.class);

	@Autowired
	L9714Report lL9714Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9714p");
		this.totaVo.init(titaVo);

		this.info("L9714p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();
		String content = "";
		lL9714Report.setParentTranCode(parentTranCode);

		boolean isFinish = lL9714Report.exec(titaVo);
		if (isFinish) {
			content = "L9714繳息證明單已完成";
		} else {
			content = "L9714繳息證明單查無資料";
		}
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), content, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}