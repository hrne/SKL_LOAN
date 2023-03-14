package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM071ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")
public class LM071Report extends MakeReport {

	@Autowired
	private LM071ServiceImpl lM071ServiceImpl;

	@Autowired
	private MakeExcel makeExcel;

	private static final String REPORT_CODE = "LM071";
	private static final String REPORT_ITEM = "推展_退休員工利率名單";
	private static final String DEFAULT_EXCEL = "LM071_底稿_推展_退休員工利率名單.xls";
	private static final String DEFAULT_SHEET = "退休員工利率";

	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {

		List<Map<String, String>> queryResultList = new ArrayList<>();

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getKinbr()).setRptDate(titaVo.getEntDyI())
				.setRptCode(REPORT_CODE).setRptItem(REPORT_ITEM).build();

		makeExcel.open(titaVo, reportVo, REPORT_CODE + "_" + REPORT_ITEM, DEFAULT_EXCEL, DEFAULT_SHEET);

		try {
			queryResultList = lM071ServiceImpl.findAll(titaVo, yearMonth);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lM071ServiceImpl findAll error = " + errors.toString());
		}

		if (queryResultList != null && !queryResultList.isEmpty()) {
			String fdnm = "";
			int row = 1;

			for (Map<String, String> queryResult : queryResultList) {
				row++;
				for (int i = 0; i < queryResult.size(); i++) {
					fdnm = "F" + i;
					switch (i) {
					case 0:
						makeExcel.setValue(row, i + 1, queryResult.get(fdnm));
						break;
					case 1:
					case 3:
						// 字串左靠
						makeExcel.setValue(row, i + 1, queryResult.get(fdnm), "L");
						break;
					case 10:
						// 利率
						if (queryResult.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0.00, "#0.00");
						} else {
							makeExcel.setValue(row, i + 1, Float.valueOf(queryResult.get(fdnm)), "#0.00");
						}
						break;
					case 11:
					case 12:
						// 金額
						if (queryResult.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0, "#,##0");
						} else {
							makeExcel.setValue(row, i + 1, Float.valueOf(queryResult.get(fdnm)), "#,##0");
						}
						break;
					case 2:
						// 西曆轉國曆
						String v = queryResult.get(fdnm);
						if (v != null && !v.isEmpty()) {
							int rocDate = Integer.parseInt(v);
							if (rocDate >= 19110000) {
								rocDate -= 19110000;
							}
							makeExcel.setValue(row, i + 1, rocDate);
						}
						break;
					case 7:
					case 8:
					case 9:
						// 日期
						if (queryResult.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, Integer.valueOf(queryResult.get(fdnm)));
						}
						break;
					default:
						// 戶號(數字右靠)
						if (queryResult.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, Integer.valueOf(queryResult.get(fdnm)));
						}
						break;
					}
				}
			}
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		makeExcel.close();
	}
}
