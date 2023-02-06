package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * L9746Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("L9746Batch")
@Scope("prototype")
public class L9746Batch extends TradeBuffer {

	@Autowired
	L9746Report l9746Report;
	
	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "L9746";
	String tranName = "介紹人換算業績報酬檢核表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9746Batch ");
		this.totaVo.init(titaVo);

		this.info(tranCode + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9746Report.setParentTranCode(parentTranCode);

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		CdWorkMonth cdWorkMonth = sCdWorkMonthService.findDateFirst(tbsdyf, tbsdyf, titaVo);

		int year = cdWorkMonth.getYear() - 1911;
		int month = cdWorkMonth.getMonth();

		titaVo.putParam("inputWorkMonth", year * 100 + month);
		
		boolean isFinish = l9746Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), tranCode + tranName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), tranCode + tranName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}