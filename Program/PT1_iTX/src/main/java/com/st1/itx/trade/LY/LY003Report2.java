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
import com.st1.itx.util.common.data.ReportVo;

@Component("LY003Report")
@Scope("prototype")

public class LY003Report2 extends MakeReport {

	@Autowired
	public LY003ServiceImpl lY003ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("LY003.exportExcel active");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LY003";
		String fileItem = "A142放款餘額彙總表";
		String fileName = "LY003-A142放款餘額彙總表";
		String defaultExcel = "LY003_底稿_A142放款餘額彙總表.xlsx";
		String defaultSheet = "A142放款餘額彙總表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		int rocYear = Integer.valueOf(titaVo.getParam("RocYear"));
		int rocMonth = 12;

		makeExcel.setValue(2, 3, (rocYear + 1911) * 100 + rocMonth);

		List<Map<String, String>> lY003List = null;

		boolean isNotEmpty = true;

		int endOfYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;

		try {

			lY003List = lY003ServiceImpl.findAll3(titaVo, endOfYearMonth);
			reportExcelA142(lY003List);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY003ServiceImpl.findAll error = " + errors.toString());
		}

		makeExcel.close();

		return isNotEmpty;

	}

	private void reportExcelA142(List<Map<String, String>> listData) throws LogicException {

		this.info("reportExcelA142 ");

		makeExcel.setSheet("A142放款餘額彙總表");

		int col = 0;
		int row = 0;

		String type = "";
		int kind = 0;
		BigDecimal amount = BigDecimal.ZERO;

		for (Map<String, String> r : listData) {
			type = r.get("F0");
			kind = Integer.valueOf(r.get("F1"));
			amount = new BigDecimal(r.get("F2"));
			this.info("type=" + type);
			this.info("kind=" + kind);
			this.info("amount=" + amount);
			switch (type) {
			case "A":
				row = 8;
				break;
			case "B":
				row = 9;
				break;
			case "C":
				row = 10;
				break;
			case "D":
				row = 11;
				break;
			case "Z":
				row = 12;
				break;
			case "ZZ":
				row = 13;
				break;
			default:
				break;
			}

			col = kind + 5;

			makeExcel.setValue(row, col, amount, "#,##0");
		}

	}

}
