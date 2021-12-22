package com.st1.itx.trade.LB;

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
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */

@Service("LB211p")
@Scope("prototype")

public class LB211p extends TradeBuffer {

	@Autowired
	public LB211Report lb211Report;

	@Autowired
	DateUtil dDateUtil; 

	@Autowired
	WebClient webClient; 

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LB211p ");
		this.totaVo.init(titaVo);

		String tranCode = "LB211";
		String tranName = "聯徵每日授信餘額變動資料檔";

		boolean isFinish = lb211Report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				tranCode + tranName + (isFinish ? "已完成" : "查無資料"), titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}