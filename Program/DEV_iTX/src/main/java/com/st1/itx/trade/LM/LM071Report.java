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

@Component
@Scope("prototype")

public class LM071Report extends MakeReport {

	@Autowired
	LM071ServiceImpl lm071ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM071", "推展_退休員工利率名單", "LM071_推展_退休員工利率名單", "LM071_底稿_推展_退休員工利率名單.xls", "退休員工利率");

		try {
			fnAllList = lm071ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM071ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			String fdnm = "";
			int row = 1;

			for (Map<String, String> tLDVo : fnAllList) {
				this.info("tLDVo-------->" + tLDVo.toString());
				row++;
				for (int i = 0; i < tLDVo.size(); i++) {
					fdnm = "F" + String.valueOf(i);
					switch (i) {
					case 0:
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
						break;
					case 1:
					case 3:
						// 字串左靠
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "L");
						break;
					case 10:
						// 利率
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0.00, "#0.00");
						} else {
							makeExcel.setValue(row, i + 1, Float.valueOf(tLDVo.get(fdnm)), "#0.00");
						}
						break;
					case 11:
					case 12:
						// 金額
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0, "#,##0");
						} else {
							makeExcel.setValue(row, i + 1, Float.valueOf(tLDVo.get(fdnm)), "#,##0");
						}
						break;
					case 7:
					case 8:
					case 9:
						// 日期
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)));
						}
						break;
					default:
						// 戶號(數字右靠)
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)));
						}
						break;
					}
				}
			}
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		long sno = makeExcel.close();
		// makeExcel.toExcel(sno);

	}
}
