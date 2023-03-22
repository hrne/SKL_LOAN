package com.st1.itx.trade.LY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CdVarValueService;

import com.st1.itx.db.service.springjpa.cm.LY006ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LY006Report extends MakeReport {

	@Autowired
	CdVarValueService sCdVarValueService;

	@Autowired
	LY006ServiceImpl lY006ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;
	// 初始列
	int row = 8;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LY006Report");

		int inputYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LY006";
		String fileItem = "B117關係人明細表";
		String fileName = "LY006_B117關係人明細表_" + titaVo.getParam("RocYear");
		String defaultExcel = "LY006_底稿_B117關係人明細表.xlsx";
		String defaultSheet = "B117關係人明細表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		makeExcel.setValue(2, 2, inputYearMonth);
		this.info("rdate   = " + inputYearMonth);

		ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
		headerStyleVo.setBold(true);

		List<Map<String, String>> lY006List = null;

		try {

			// 各筆資料
			lY006List = lY006ServiceImpl.queryDetail(inputYearMonth, titaVo);
			this.info("lY006List=" + lY006List);
			eptExcel(lY006List);
		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY006erviceImpl.exportExcel error = " + errors.toString());
		}
		return true;

	}

	private void eptExcel(List<Map<String, String>> lY006List) throws LogicException {
		this.info("eptExcel");
		// TODO Auto-generated method stub
		if (lY006List != null && !lY006List.isEmpty()) {
			int shiftCounts = lY006List.size();
			makeExcel.setShiftRow(8, shiftCounts);
			for (Map<String, String> r : lY006List) {
				makeExcel.setValue(row, 1, r.get("F0"));// 與本公司之關係
				makeExcel.setValue(row, 2, r.get("F1"));// 關係人代號
				makeExcel.setValue(row, 3, r.get("F2"));// 關係人名稱
				makeExcel.setValue(row, 4, r.get("F3"));// 關係人職稱
				makeExcel.setValue(row, 5, r.get("F4"));// 親屬代號
				makeExcel.setValue(row, 6, r.get("F5"));// 親屬姓名
				makeExcel.setValue(row, 7, r.get("F6"));// 親屬親筆
				makeExcel.setValue(row, 8, r.get("F7"));// 親屬稱謂
				makeExcel.setValue(row, 9, r.get("F8"));// 所屬事業代號
				makeExcel.setValue(row, 10, r.get("F9"));// 所屬事業名稱
				makeExcel.setValue(row, 11, (Integer.valueOf(r.get("F10")) / 100.0), "#0.0000");// 所屬事業持股比率%
				makeExcel.setValue(row, 12, r.get("F11"));// 所屬事業待任要職
				// makeExcel.setValue(row, 13,r.get("F12"));//備註
				row++;
			}
		} else {
			// 無資料時處理
			makeExcel.setValue(8, 1, "本日無資料", "L");
		}
		makeExcel.close();
	}
}
