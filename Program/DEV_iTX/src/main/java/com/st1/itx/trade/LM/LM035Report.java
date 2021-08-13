package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM035ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM035Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM035Report.class);

	@Autowired
	LM035ServiceImpl lM035ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}
	int col = 2;
	int setLoanBal = 0;
	public void exec(TitaVo titaVo) throws LogicException {
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM035", "地區逾放比", "LM035地區逾放比", "LM035地區逾放比.xlsx", "10804");
		int entdy1 = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100;
		List<Integer> yearSeason = new ArrayList<Integer>();
		this.info("entdy1 = " + entdy1);
		yearSeason.add(entdy1);
		int result = entdy1;
		int remain = 0;
		if ((entdy1 % 100 % 3) != 0) { // 不是3得倍數月
			remain = entdy1 % 100 % 3;
		} else {
			result -= 3;
		}
		if((entdy1 % 100) < 4) {
			result = (result / 100 - 1) * 100 + 12;
		}else {
			result -= remain;
		}
		yearSeason.add(result);
		int temp = 0;
		while(temp == 0) {
			if((entdy1 / 100) - (result / 100) < 2) {
				if((result % 100) == 3) {
					result -= 91;
				}else {
					result -= 3;
				}
			} else {
				result = (result / 100 - 1) * 100 + 12;
			}
			yearSeason.add(result);
			if((entdy1 / 100) - (result / 100) == 5) {
				temp = 1;
			}
		}

		this.info("yearSeason = " + yearSeason);
		List<Map<String, String>> LM035List = null;
		for(int i = yearSeason.size() - 1 ; i >= 0 ; i--) {
			if(yearSeason.get(i) % 100 % 3 == 0) {
				makeExcel.setValue(2, col, String.valueOf(yearSeason.get(i) / 100 - 1911) + "Q" + String.valueOf(yearSeason.get(i) % 100 / 3));
			}else {
				makeExcel.setValue(2, col, String.valueOf(yearSeason.get(i) - 191100));
			}
			if(i == 0) {
				setLoanBal = 1;
				makeExcel.setValue(1, yearSeason.size() + 2, "單位：百萬元");
				makeExcel.setValue(2, yearSeason.size() + 2, "放款餘額");
			}
			try {
				LM035List = lM035ServiceImpl.findAll(titaVo, String.valueOf(yearSeason.get(i)));
				exportExcel(titaVo, LM035List);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM035ServiceImpl.testExcel error = " + errors.toString());
			}
			col++;
		}
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("LM035Report exportExcel");
		BigDecimal ovduBal = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		int row = 3;
		int count = 1;
		for (Map<String, String> tLDVo : LDList) {
			makeExcel.setValue(row, col, new BigDecimal(tLDVo.get("F5")), "0.00%", "C");
			if(setLoanBal == 1) {
				makeExcel.setValue(row, col + 1, new BigDecimal(tLDVo.get("F2")), "#,##0.00", "R");
			}
			total = total.add(new BigDecimal(tLDVo.get("F2")));
			ovduBal = ovduBal.add(new BigDecimal(tLDVo.get("F3")).add(new BigDecimal(tLDVo.get("F4"))));
			row++;
			if(count == LDList.size()) {
				if(total.compareTo(BigDecimal.ZERO) == 1) {
					this.info("Print Avg !!!" + "    row = " + row + "    col = " + col + "    value = " + ovduBal.divide(total, 4, 4));
					makeExcel.setValue(row, col, ovduBal.divide(total, 4, 4), "0.00%", "C");
				} else {
					this.info("Print Avg0 !!!" + "    row = " + row + "    col = " + col + "    value = " + BigDecimal.ZERO);
					makeExcel.setValue(row, col, BigDecimal.ZERO, "0.00%", "C");
				}
			}
			count++;
		}
		if(setLoanBal == 1) {
			makeExcel.setValue(row, col + 1, total.divide(new BigDecimal("1"), 4), "#,##0", "R");
		}
	}
	
}
