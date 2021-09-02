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

/**
 * L9110 首次撥款審核資料表
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9110")
@Scope("prototype")
public class L9110 extends TradeBuffer {

	/* 報表服務注入 */
	@Autowired
	L9110Report l9110Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9110 ");
		this.totaVo.init(titaVo);

		this.info("L9110 titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9110Report.setParentTranCode(parentTranCode);

//		boolean isFinished = l9110Report.exec(titaVo, this.totaVo);

		l9110Report.exec(titaVo, this.totaVo);

//		String nowBcDate = dDateUtil.getNowStringBc();
//		String nowTime = dDateUtil.getNowStringTime();
//
//		String tlrNo = titaVo.getTlrNo();

//		if (isFinished) {
//			webClient.sendPost(nowBcDate, nowTime, tlrNo, "Y", "LC009", tlrNo, "L9110首次撥款審核資料表已完成", titaVo);
//		} else {
//			webClient.sendPost(nowBcDate, nowTime, tlrNo, "Y", "L9110", tlrNo, "L9110首次撥款審核資料表查無資料", titaVo);
//		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}