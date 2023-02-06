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
 * L9744Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("L9744Batch")
@Scope("prototype")
public class L9744Batch extends TradeBuffer {

	@Autowired
	L9744Report l9744Report;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "L9744";
	String tranName = "三階放款明細統計";


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9744Batch");
		this.totaVo.init(titaVo);

		this.info(tranCode + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9744Report.setParentTranCode(parentTranCode);

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		CdWorkMonth cdWorkMonth = sCdWorkMonthService.findDateFirst(tbsdyf, tbsdyf, titaVo);
		
		if (cdWorkMonth == null)
			throw new LogicException("E0001", "放款業績工作月對照檔查無本日資料");

		int year = cdWorkMonth.getYear() - 1911;
		int month = cdWorkMonth.getMonth();
		
		// ServiceImpl.findAll 接收民國年月
		titaVo.putParam("workMonthStart", year * 100 + month);
		titaVo.putParam("workMonthEnd", year * 100 + month);
		titaVo.putParam("custNo", 0);
		titaVo.putParam("facmNo", 0);
		titaVo.putParam("Introducer","");
		
		boolean isFinish = l9744Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), tranCode + tranName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), tranCode + tranName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}