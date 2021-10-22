package com.st1.itx.trade.LY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LY003ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("LY003Report")
@Scope("prototype")

public class LY003Report extends MakeReport {

	@Autowired
	public LY003ServiceImpl lY003ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("LY003.exportExcel active");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY003", "非RBC_表14-2_會計部年度檢查報表",
				"LY003-非RBC_表14-2_會計部年度檢查報表", "LY003_底稿_非RBC_表14-2_會計部年度檢查報表.xlsx", "表14-2");

		int rocYear = Integer.valueOf(titaVo.getParam("RocYear"));
		int rocMonth = 12;

		makeExcel.setValue(1, 1, "新光人壽保險股份有限公司 " + rocYear + "年度(" + rocMonth + ")報表");

		List<Map<String, String>> lY003List = null;

		boolean isNotEmpty = true;

		int endOfYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;
		
		for (int f = 1; f <= 6; f++) {

			try {

				lY003List = lY003ServiceImpl.findAll(titaVo, f,endOfYearMonth);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LY003ServiceImpl.findAll error = " + errors.toString());
			}

			if (lY003List.size() > 0) {

				exportExcel(lY003List, f);
				
				isNotEmpty = true;

			} else {

				makeExcel.setValue(6, 3, "本日無資料");

				isNotEmpty = false;
			}

		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

		return isNotEmpty;

	}

	private void exportExcel(List<Map<String, String>> LDList, int formNum) throws LogicException {
		int row = 0;

		BigDecimal evaAmt = BigDecimal.ZERO;
		BigDecimal lineAmt = BigDecimal.ZERO;
		BigDecimal loanAmt = BigDecimal.ZERO;
		
		for (Map<String, String> tLDVo : LDList) {

			switch (formNum) {
			case 1:
				row = tLDVo.get("F0").equals("C") ? 8 : tLDVo.get("F0").equals("D") ? 9 : 10;
				break;
			case 2:
				row = tLDVo.get("F0").equals("A") ? 14
						: tLDVo.get("F0").equals("B") ? 15 : tLDVo.get("F0").equals("C") ? 16 : 17;
				break;
			case 3:
				row = tLDVo.get("F0").equals("A") ? 19
						: tLDVo.get("F0").equals("B") ? 20 : tLDVo.get("F0").equals("C") ? 21 : 22;
				break;
			case 4:
				row = 23;
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			default:
				break;
			}

			evaAmt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
			lineAmt = tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
			loanAmt = tLDVo.get("F3").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3"));

			makeExcel.setValue(row, 4, evaAmt, "#,##0");
			makeExcel.setValue(row, 5, lineAmt, "#,##0");
			makeExcel.setValue(row, 8, loanAmt, "#,##0");
		}
		
		
		
		

	}

}
