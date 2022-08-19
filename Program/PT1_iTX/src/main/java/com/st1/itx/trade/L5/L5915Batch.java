package com.st1.itx.trade.L5;

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
 * L5915Batch
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L5915Batch")
@Scope("prototype")
public class L5915Batch extends TradeBuffer {

	@Autowired
	L5915Report l5915Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	private static final String TRAN_CODE = "L5915";
	private static final String TRAN_NAME = "協辦人員業績統計_件數及金額明細";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5915Batch");
		this.totaVo.init(titaVo);

		boolean isFinish = l5915Report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", (isFinish ? "LC009" : "L5915"),
				titaVo.getTlrNo(), TRAN_CODE + TRAN_NAME + (isFinish ? "已完成" : "查無資料"), titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}