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

@Service("L9706p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9706p extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9706p.class);

	@Autowired
	public L9706Report l9706Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9706p");
		this.totaVo.init(titaVo);

		this.info("L9706p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		String content = "";
		l9706Report.setParentTranCode(parentTranCode);

		if (l9706Report.exec(titaVo)) {
			content = "L9706貸款餘額證明書已完成";
		} else {
			content = "L9706貸款餘額證明書查無資料";
		}

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), content, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}