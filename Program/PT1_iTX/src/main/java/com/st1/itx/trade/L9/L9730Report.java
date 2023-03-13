package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9730ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9730Report extends MakeReport {

	@Autowired
	L9730ServiceImpl l9730ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "L9730";
	String txName = "定期機動資料檢核";

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(txcd + "Report exec start ...");

		List<Map<String, String>> listL9730 = null;

		int inputStartDateFirst = parse.stringToInteger(titaVo.get("InputStartDateFirst")) + 19110000;
		int inputEndDateFirst = parse.stringToInteger(titaVo.get("InputEndDateFirst")) + 19110000;
		int inputStartDateNext = parse.stringToInteger(titaVo.get("InputStartDateNext")) + 19110000;
		int inputEndDateNext = parse.stringToInteger(titaVo.get("InputEndDateNext")) + 19110000;

		try {
			listL9730 = l9730ServiceImpl.findAll(inputStartDateFirst, inputEndDateFirst, inputStartDateNext,
					inputEndDateNext, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		return exportExcel(listL9730, titaVo); // 不 catch 它的 Exceptions, 讓前端接
	}

	public boolean exportExcel(List<Map<String, String>> listL9730, TitaVo titaVo) throws LogicException {
		if (listL9730 == null || listL9730.isEmpty())
			return false;

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno()).setRptCode(txcd)
				.setRptItem(txName).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, txcd + "_" + txName, txcd + "_" + txName + ".xlsx", "X800");

		int row = 2;

		String lastCustNo = "";
		String lastFacmNo = "";
		String lastBormNo = "";
		String lastLRCRateIncr = "";

		for (Map<String, String> l9730Vo : listL9730) {

			String thisCustNo = l9730Vo.get("CustNo");
			String thisFacmNo = l9730Vo.get("FacmNo");
			String thisBormNo = l9730Vo.get("BormNo");
			String thisLRCRateIncr = l9730Vo.get("LRCRateIncr");

			if (lastCustNo.equals(thisCustNo) && lastFacmNo.equals(thisFacmNo) && lastBormNo.equals(thisBormNo)
					&& lastLRCRateIncr.equals(thisLRCRateIncr)) {
				// 賴桑指示: 連續相同 RateIncr 時，不重覆出
				continue;
			} else {
				lastCustNo = thisCustNo;
				lastFacmNo = thisFacmNo;
				lastBormNo = thisBormNo;
				lastLRCRateIncr = thisLRCRateIncr;
			}

			for (int i = 0; i < 10; i++) {
				String valueStr = l9730Vo.get("F" + i);
				switch (i) {
				case 5:
					makeExcel.setValue(row, i + 1, parse.isNumeric(valueStr) ? getBigDecimal(valueStr) : valueStr, "L");
					break;
				case 6:
					makeExcel.setValue(row, i + 1, getBigDecimal(valueStr), "0.000");
					break;
				case 8:
					makeExcel.setValue(row, i + 1, getBigDecimal(valueStr), "0.0000");
					break;
				case 9:
					makeExcel.setValue(row, i + 1, getBigDecimal(valueStr), "#,##0");
					break;
				default:
					makeExcel.setValue(row, i + 1, parse.isNumeric(valueStr) ? getBigDecimal(valueStr) : valueStr);
					break;
				}
			}

			row++;
		}

//		long sno = 
		makeExcel.close();
//		makeExcel.toExcel(sno);

		return true;
	}
}
