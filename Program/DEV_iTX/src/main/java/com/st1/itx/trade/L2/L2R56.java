package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R56")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */

public class L2R56 extends TradeBuffer {

	@Autowired
	public FacProdService facProdService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R56 ");
		this.totaVo.init(titaVo);

		// TITA
		String iBreachFlag = titaVo.getParam("BreachFlag"); // 是否限制清償
		int iProhibitMonth = parse.stringToInteger(titaVo.getParam("ProhibitMonth")); // 限制清償期限
		int iBreachStartPercent = parse.stringToInteger(titaVo.getParam("BreachStartPercent")); // 還款起算比例%
		String iBreachCode = titaVo.getParam("BreachCode"); // 違約適用方式
		BigDecimal iBreachPercent = parse.stringToBigDecimal(titaVo.getParam("BreachPercent")); // 違約金百分比
		int iBreachDecreaseMonth = parse.stringToInteger(titaVo.getParam("BreachDecreaseMonth")); // 違約金分段月數
		BigDecimal iBreachDecrease = parse.stringToBigDecimal(titaVo.getParam("BreachDecrease")); // 分段遞減百分比
		String iBreachGetCode = titaVo.getParam("BreachGetCode");// 違約金收取方式

		// wk
		String wkBreachDescription = "";
		String wkBreachA = "";
		String wkBreachB = "";
		String wkBreachC = "";
		String wkBreachD = "";
		String wkBreachE = "";
		String wkBreachF = "";

		if ("Y".equals(iBreachFlag)) {
			wkBreachA = "自借款日起算，於未滿 " + iProhibitMonth + "個月期間提前清償者";
			if (iBreachStartPercent != 0) {
				wkBreachB = "，還款金額達 " + iBreachStartPercent + "% 以上時";
			}
			switch (iBreachCode) {
			case "001":
				wkBreachC = "，按各次提前清償金額";
				break;
			case "002":
				wkBreachC = "，按各次提前清償金額";
				break;
			case "003":
				wkBreachC = "，每次還款按核准額度";
				break;
			case "004":
				wkBreachC = "，每次還款依撥款金額";
				break;
			case "005":
				wkBreachC = "，按各次提前清償金額";
				break;
			}
			if (iBreachPercent.compareTo(BigDecimal.ZERO) > 0) {
				wkBreachD = "，" + iBreachPercent + "% 計付違約金";
			}
			if (iBreachDecreaseMonth != 0) {
				wkBreachE = "，但每" + iBreachDecreaseMonth + "個月遞減違約金" + iBreachDecrease + "%";
			}
			switch (iBreachGetCode) {
			case "1":
				wkBreachF = "，即時收取";
				break;

			case "2":
				wkBreachF = "，領清償證明時收取";
				break;
			}

		}
		wkBreachDescription = wkBreachA + wkBreachB + wkBreachC + wkBreachD + wkBreachE + wkBreachF;
		if ("".equals(wkBreachDescription)) {
			wkBreachDescription = "無綁約條件";
		}
		this.totaVo.putParam("OBreach", wkBreachDescription);
		this.addList(this.totaVo);
		return this.sendList();
	}
}