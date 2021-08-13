package com.st1.itx.trade.L4;

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
import com.st1.itx.util.parse.Parse;

@Service("L4R17")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R17 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4R17.class);

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R17 ");
		this.totaVo.init(titaVo);

//		運算-[terms]個月
//		-1天

		int terms = parse.stringToInteger(titaVo.getParam("RimTerms"));
		int deductDate = parse.stringToInteger(titaVo.getParam("RimDeductDate"));

		terms = 0 - terms;

		dateUtil.init();

		dateUtil.setDate_1(deductDate);
		dateUtil.setDate_2(deductDate);
		dateUtil.setMons(terms);
		dateUtil.setDays(-1);

		int date = dateUtil.getCalenderDay();

		this.totaVo.putParam("L4r17DeductDate", date);

		this.addList(this.totaVo);
		return this.sendList();
	}
}