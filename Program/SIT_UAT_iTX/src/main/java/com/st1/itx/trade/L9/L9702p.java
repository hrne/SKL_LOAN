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

/**
 * L9702p
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("L9702p")
@Scope("prototype")
public class L9702p extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L9702p.class);

	@Autowired
	L9702Report l9702Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9702p ");
		this.totaVo.init(titaVo);

		this.info("L9702p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9702Report.setParentTranCode(parentTranCode);

		l9702Report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L9702放款餘額及財收統計表已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}