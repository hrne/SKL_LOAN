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
import com.st1.itx.db.service.springjpa.cm.LM070ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM070Report extends MakeReport {

	@Autowired
	LM070ServiceImpl lm070ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	List<Map<String, String>> fnAllList;

	boolean sheet13, sheet23;

	public boolean exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM070", "介紹人加碼獎勵津貼明細", "LM070介紹人加碼獎勵津貼明細", "LM070_底稿_介紹人加碼獎勵津貼明細.xlsx", "Q2573_13");

		sheet13 = excelQ2573_13(titaVo);

		makeExcel.setSheet("Q2573_23");

		sheet23 = excelQ2573_23(titaVo);

		makeExcel.close();

		if (sheet13 && sheet23) {
			return true;
		} else {
			return false;
		}

	}

	private boolean excelQ2573_13(TitaVo titaVo) throws LogicException {
		this.info("LM070Report exec");
		fnAllList = new ArrayList<>();
		try {
			fnAllList = lm070ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM070ServiceImpl.findAll error = " + errors.toString());
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
					case 3:
					case 4:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 15:
					case 18:
					case 19:
						// 字串左靠
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
						break;
					case 22:// 介紹人中文名
					case 23:// 戶號中文名
						break;

					default:
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm));
						break;
					}
				}
			}

		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		return true;
	}

	private boolean excelQ2573_23(TitaVo titaVo) throws LogicException {

		fnAllList = new ArrayList<>();
		try {
			fnAllList = lm070ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM070ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {

			int row = 2;

			for (Map<String, String> tLDVo : fnAllList) {

				row++;
				// F21 介紹人姓名
				makeExcel.setValue(row, 1, tLDVo.get("F21"));
				// F6 介紹人員編
				makeExcel.setValue(row, 2, tLDVo.get("F6"));
				// F7 介紹人ID
				makeExcel.setValue(row, 3, tLDVo.get("F7"));
				// F22用戶姓名
				makeExcel.setValue(row, 4, tLDVo.get("F22"));
				// F0 戶號
				makeExcel.setValue(row, 5, tLDVo.get("F0"));
				// F1 額度
				makeExcel.setValue(row, 6, tLDVo.get("F1"));
				// F14 首撥日
				makeExcel.setValue(row, 7, tLDVo.get("F14"));
				// F2 撥款金額
				makeExcel.setValue(row, 8, tLDVo.get("F2"));
				// F5 累計撥款金額
				makeExcel.setValue(row, 9, tLDVo.get("F5"));
				// F15 基本利率代碼
				makeExcel.setValue(row, 10, tLDVo.get("F15"));
				// F3 計件代碼
				makeExcel.setValue(row, 11, tLDVo.get("F3"));
				// F4 是否計件
				makeExcel.setValue(row, 12, tLDVo.get("F4"));
				// F8 部室代號
				makeExcel.setValue(row, 13, tLDVo.get("F8"));
				// F9 區部代號
				makeExcel.setValue(row, 14, tLDVo.get("F9"));
				// F10 單位代號
				makeExcel.setValue(row, 15, tLDVo.get("F10"));
				// F11 部室名稱
				makeExcel.setValue(row, 16, tLDVo.get("F11"));
				// F12 區部名稱
				makeExcel.setValue(row, 17, tLDVo.get("F12"));
				// F13 單位名稱
				makeExcel.setValue(row, 18, tLDVo.get("F13"));
				// F18 換算業績
				makeExcel.setValue(row, 19, tLDVo.get("F18"));
				// F19 業務報酬
				makeExcel.setValue(row, 20, tLDVo.get("F19"));
				// F20 核發獎金
				makeExcel.setValue(row, 21, tLDVo.get("F20"));

			}

		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}

		return true;
	}

}
