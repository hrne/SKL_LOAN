package com.st1.itx.trade.L9;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.trade.LM.LM062Report;
import com.st1.itx.trade.LM.LM063Report;
import com.st1.itx.trade.LM.LM064Report;
import com.st1.itx.trade.LM.LM065Report;
import com.st1.itx.trade.LM.LM066Report;
import com.st1.itx.trade.LM.LM067Report;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9734Report extends MakeReport {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	LM062Report lm062report;

	@Autowired
	LM063Report lm063report;

	@Autowired
	LM064Report lm064report;

	@Autowired
	LM065Report lm065report;

	@Autowired
	LM066Report lm066report;

	@Autowired
	LM067Report lm067report;

	String txCD = "L9734";
	String txName = "覆審報表產製";

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param rptCode   報表代號
	 *
	 * 
	 */
	public boolean exec(TitaVo titaVo, int yearMonth, String rptCode) throws LogicException {
		this.info(txCD + " exec-" + rptCode);

		switch (rptCode) {
		case "LM062":
			lm062report.exec(titaVo, yearMonth);
			break;
		case "LM063":
			lm063report.exec(titaVo, yearMonth);
			break;
		case "LM064":
			lm064report.exec(titaVo, yearMonth);
			break;
		case "LM065":
			lm065report.exec(titaVo, yearMonth);
			break;
		case "LM066":
			lm066report.exec(titaVo, yearMonth);
			break;
		case "LM067":
			lm067report.exec(titaVo, yearMonth);
			break;
		default:
			break;
		}
		return true;

	}

}
