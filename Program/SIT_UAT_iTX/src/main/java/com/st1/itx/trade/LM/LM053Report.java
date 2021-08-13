package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM053ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM053Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM053Report.class);

	@Autowired
	LM053ServiceImpl LM053ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
 
	@Override
	public void printTitle() {

	}
 
	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM053Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM053", "法務分配款明細表_內部控管", "LM053法務分配款明細表_內部控管",
				"LM053-法務分配款明細表_內部控管.xlsx", "法務分配表");

		try {
			fnAllList = LM053ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM053ServiceImpl.findAll error = " + errors.toString());
		}
		int row = 2;
		if (fnAllList.size() > 0) {
			String fdnm = "";
			String xdate = "";

			for (Map<String, String> tLDVo : fnAllList) {
				this.info("tLDVo-------->" + tLDVo.toString());
				row++;
				for (int i = 0; i < tLDVo.size(); i++) {
					fdnm = "F" + String.valueOf(i);
					switch (i) {
					case 0:
					case 8:
						// 民國年
						xdate = tLDVo.get(fdnm);

						// 可以進入者：不是0並且不是空
						if (xdate.length() > 7) {
							// 如果是0 就掛0 不是零 應為正常西元年日期
							xdate = String.valueOf(Integer.valueOf(xdate) == 0 ? 0 : Integer.valueOf(xdate) - 19110000);
							if (i == 0) {
								makeExcel.setValue(row, i + 1, xdate);
							} else {
								if (xdate.length() == 7) {
									makeExcel.setValue(row, i + 1, xdate.substring(0, 3) + "." + xdate.substring(3, 5)
											+ "." + xdate.substring(5, 7));
								} else if (xdate.length() == 6) {
									makeExcel.setValue(row, i + 1, xdate.substring(0, 2) + "." + xdate.substring(2, 4)
											+ "." + xdate.substring(4, 6));
								}

							}
						}
						break;
					case 2:
					case 3:
						// 戶號(數字右靠)
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)));
						}
						break;
					case 5:
					case 6:
					case 7:
						// 金額
						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0, "#,##0");
						} else {
							makeExcel.setValue(row, i + 1, Float.valueOf(tLDVo.get(fdnm)), "#,##0");
						}
						break;
					case 9:
						break;
					default:
						// 字串左靠
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
						break;
					}
				}
			}
		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}
		makeExcel.setValue(1, 3, row - 2);
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
