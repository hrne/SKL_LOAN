package com.st1.itx.trade.LP;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.springjpa.cm.LP005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LP005Report extends MakeReport {

	@Autowired
	LP005ServiceImpl lp005ServiceImpl;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LP005Report exec ...");

		// 系統會計日期
		int iEntdy = titaVo.getEntDyI() + 19110000;

		CdWorkMonth tCdWorkMonth = sCdWorkMonthService.findDateFirst(iEntdy, iEntdy, titaVo);

		if (tCdWorkMonth == null) {
			return;
		}

		// 業績年度
		int pfYear = tCdWorkMonth.getYear();

		// 業績月份
		int pfMonth = tCdWorkMonth.getMonth();

		int pfSeason = 0;

		// 判斷季度
		if (pfMonth < 4) {
			pfSeason = 1;
		} else if (pfMonth < 7) {
			pfSeason = 2;
		} else if (pfMonth < 10) {
			pfSeason = 3;
		} else {
			pfSeason = 4;
		}

		this.info("pfYear = " + pfYear);
		this.info("pfMonth = " + pfMonth);
		this.info("pfSeason = " + pfSeason);

		exportExcel(pfYear, pfSeason, titaVo);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(int pfYear, int pfSeason, TitaVo titaVo) throws LogicException {

		this.info("exportExcel ... ");

		int maxOfLoops = 3;

		if (pfSeason == 4) {
			maxOfLoops = 4;
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP005", "協辦考核核算底稿", "LP005_" + (pfYear - 1911) + "Q" + pfSeason + "協辦考核核算底稿", "LP005_底稿_協辦考核核算_第四季特別版.xlsx", "1月件數");
		} else {
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP005", "協辦考核核算底稿", "LP005_" + (pfYear - 1911) + "Q" + pfSeason + "協辦考核核算底稿", "LP005_底稿_協辦考核核算.xlsx", "1月件數");
		}

		for (int i = 1; i <= maxOfLoops; i++) {

			int inputWorkMonth = pfYear * 100 + (i + (3 * (pfSeason - 1)));

			this.info("inputWorkMonth = " + inputWorkMonth);

			setCounts(i, inputWorkMonth, titaVo);

			setAmt(i, inputWorkMonth, titaVo);
		}

		List<Map<String, String>> listEmpClass = new ArrayList<>();

		listEmpClass = setDept(listEmpClass, "A0B000", "營管", pfYear, pfSeason, titaVo);
		listEmpClass = setDept(listEmpClass, "A0F000", "營推", pfYear, pfSeason, titaVo);
		listEmpClass = setDept(listEmpClass, "A0E000", "業推", pfYear, pfSeason, titaVo);
		listEmpClass = setDept(listEmpClass, "A0M000", "業開", pfYear, pfSeason, titaVo);

		setEmpClassList(listEmpClass, pfYear, pfSeason, 0);

		setEmpClassList(listEmpClass, pfYear, pfSeason, 1);
	}

	private List<Map<String, String>> putDataToListEmpClass(List<Map<String, String>> listEmpClass, Map<String, String> m, String deptSheetName, String oriEmpClass, String afterEmpClass) {
		this.info("putDataToListEmpClass ... ");

		Map<String, String> mapEmpClass = new HashMap<>();

		mapEmpClass.put("Dept", deptSheetName + "部"); // 部室
		mapEmpClass.put("Dist", m.get("F0")); // 區部
		mapEmpClass.put("Unit", m.get("F1")); // 單位
		mapEmpClass.put("EmpName", m.get("F2")); // 姓名
		mapEmpClass.put("EmpNo", m.get("F3")); // 員工代號
		mapEmpClass.put("OriEmpClass", oriEmpClass); // 考核前職級
		mapEmpClass.put("AfterEmpClass", afterEmpClass); // 考核後職級

		listEmpClass.add(mapEmpClass);

		return listEmpClass;
	}

	private void setAmt(int i, int inputWorkMonth, TitaVo titaVo) throws LogicException {
		this.info("setAmt ... ");
		this.info("setAmt i = " + i);
		this.info("setAmt inputWorkMonth = " + inputWorkMonth);

		String month = String.valueOf(inputWorkMonth);

		month = month.substring(month.length() - 2);

		makeExcel.setSheet(i + "月金額", month + "月金額");

		List<Map<String, String>> listAmt = lp005ServiceImpl.queryAmt(inputWorkMonth, titaVo);

		if (listAmt == null || listAmt.isEmpty()) {
			return;
		}

		int rowCursor = 2;

		for (Map<String, String> m : listAmt) {

			// 戶號
			makeExcel.setValue(rowCursor, 1, Integer.valueOf(m.get("F0")));
			// 額度
			makeExcel.setValue(rowCursor, 2, Integer.valueOf(m.get("F1")));
			// 撥款金額
			makeExcel.setValue(rowCursor, 3, getBigDecimal(m.get("F2")), "#,##0");
			// 計件代碼
			makeExcel.setValue(rowCursor, 4, m.get("F3"), "L");
			// 員工代碼
			makeExcel.setValue(rowCursor, 5, m.get("F4"), "L");
			// 員工姓名
			makeExcel.setValue(rowCursor, 6, m.get("F5"), "L");
			// 部室
			makeExcel.setValue(rowCursor, 7, m.get("F6"), "L");
			// 區部
			makeExcel.setValue(rowCursor, 8, m.get("F7"), "L");
			// 單位
			makeExcel.setValue(rowCursor, 9, m.get("F8"), "L");

			rowCursor++;
		}
	}

	private void setCounts(int i, int inputWorkMonth, TitaVo titaVo) throws LogicException {
		this.info("setCounts ... ");
		this.info("setCounts i = " + i);
		this.info("setCounts inputWorkMonth = " + inputWorkMonth);

		String month = String.valueOf(inputWorkMonth);

		month = month.substring(month.length() - 2);

		makeExcel.setSheet(i + "月件數", month + "月件數");

		List<Map<String, String>> listCnt1 = lp005ServiceImpl.queryCounts(inputWorkMonth, titaVo);

		if (listCnt1 == null || listCnt1.isEmpty()) {
			return;
		}

		int rowCursor = 2;

		for (Map<String, String> m : listCnt1) {

			// 獎金類別
			makeExcel.setValue(rowCursor, 1, Integer.valueOf(m.get("F0")));
			// 戶號
			makeExcel.setValue(rowCursor, 2, Integer.valueOf(m.get("F1")));
			// 戶名
			makeExcel.setValue(rowCursor, 3, m.get("F2"), "L");
			// 車馬費發放額
			makeExcel.setValue(rowCursor, 4, getBigDecimal(m.get("F3")), "#,##0");
			// 介紹人
			makeExcel.setValue(rowCursor, 5, m.get("F4"), "L");
			// 員工姓名
			makeExcel.setValue(rowCursor, 6, m.get("F5"), "L");
			// 部室
			makeExcel.setValue(rowCursor, 7, m.get("F6"), "L");
			// 區部
			makeExcel.setValue(rowCursor, 8, m.get("F7"), "L");
			// 單位
			makeExcel.setValue(rowCursor, 9, m.get("F8"), "L");

			rowCursor++;
		}
	}

	private List<Map<String, String>> setDept(List<Map<String, String>> listEmpClass, String deptCode, String deptSheetName, int pfYear, int pfSeason, TitaVo titaVo) throws LogicException {
		this.info("setDept ... ");
		this.info("setDept deptCode = " + deptCode);
		this.info("setDept deptSheetName = " + deptSheetName);

		makeExcel.setSheet(deptSheetName);

		String title = (pfYear - 1911) + "年" + deptSheetName + "部第" + pfSeason + "季房貸協辦人員考核後職級明細表";

		makeExcel.setValue(1, 1, title);

		List<Map<String, String>> listDept = lp005ServiceImpl.queryDept(pfYear, pfSeason, deptCode, titaVo);

		if (listDept == null || listDept.isEmpty()) {
			return listEmpClass;
		}

		int rowCursor = 4;
		int rowCursorTotal = 5;

		if (listDept.size() > 1) {
			makeExcel.setShiftRow(rowCursor + 1, listDept.size() - 1);
			rowCursorTotal += listDept.size() - 1;
		}

		for (Map<String, String> m : listDept) {

			// 區部
			makeExcel.setValue(rowCursor, 1, m.get("F0"), "C");
			// 單位
			makeExcel.setValue(rowCursor, 2, m.get("F1"), "C");
			// 姓名
			makeExcel.setValue(rowCursor, 3, m.get("F2"), "C");
			// 員工代號
			makeExcel.setValue(rowCursor, 4, m.get("F3"), "C");
			// 考核前400職級
			String oriEmpClassCode = m.get("F4");
			String oriEmpClass = "";
			switch (oriEmpClassCode) {
			case "1":
				oriEmpClass = "初級";
				break;
			case "2":
				oriEmpClass = "中級";
				break;
			case "3":
				oriEmpClass = "高級";
				break;
			default:
				break;
			}
			makeExcel.setValue(rowCursor, 5, oriEmpClass, "C");

			makeExcel.setValue(rowCursor, 6, getBigDecimal(m.get("F5")), "#,##0");
			makeExcel.setValue(rowCursor, 7, getBigDecimal(m.get("F6")), "#,##0");

			makeExcel.setValue(rowCursor, 8, getBigDecimal(m.get("F7")), "#,##0");
			makeExcel.setValue(rowCursor, 9, getBigDecimal(m.get("F8")), "#,##0");

			makeExcel.setValue(rowCursor, 10, getBigDecimal(m.get("F9")), "#,##0");
			makeExcel.setValue(rowCursor, 11, getBigDecimal(m.get("F10")), "#,##0");

			int columnShift = 0;

			if (pfSeason == 4) {
				columnShift = 2;
				makeExcel.setValue(rowCursor, 12, getBigDecimal(m.get("F11")), "#,##0");
				makeExcel.setValue(rowCursor, 13, getBigDecimal(m.get("F12")), "#,##0");
			}

			// 合計 件數&金額
			BigDecimal countTotal = getBigDecimal(m.get("F13"));
			makeExcel.setValue(rowCursor, 12 + columnShift, countTotal, "#,##0");
			makeExcel.setValue(rowCursor, 13 + columnShift, getBigDecimal(m.get("F14")), "#,##0");

			// 考核後400職級
			String afterEmpClass = "";
			switch (oriEmpClassCode) {
			case "3": // 原本是高級
				if (countTotal.compareTo(getBigDecimal("5")) >= 0) {
					afterEmpClass = "高級"; // 高級維持高級需合計五件以上(含五件)
				} else {
					afterEmpClass = "中級"; // 否則降至中級
				}
				break;
			case "2": // 原本是中級
				if (countTotal.compareTo(getBigDecimal("8")) >= 0) {
					afterEmpClass = "高級"; // 中級若合計八件以上(含八件)，升級至高級
				} else if (countTotal.compareTo(getBigDecimal("3")) >= 0) {
					afterEmpClass = "中級"; // 中級維持中級需合計三件以上(含三件)
				} else {
					afterEmpClass = "初級"; // 否則降至初級
				}
				break;
			case "1": // 原本是初級
				if (countTotal.compareTo(getBigDecimal("8")) >= 0) {
					afterEmpClass = "高級"; // 初級若合計八件以上(含八件)，升級至高級
				} else if (countTotal.compareTo(getBigDecimal("5")) >= 0) {
					afterEmpClass = "中級"; // 初級若合計五件以上(含五件)，升級至中級
				} else {
					afterEmpClass = "初級"; // 否則維持初級
				}
				break;
			default:
				break;
			}
			makeExcel.setValue(rowCursor, 15 + columnShift, afterEmpClass, "C");

			listEmpClass = putDataToListEmpClass(listEmpClass, m, deptSheetName, oriEmpClass, afterEmpClass);

			rowCursor++;
		}

		// 公式重新計算
		for (int j = 0; j < 2; j++) {
			for (int k = 6; k <= 15; k++) {
				makeExcel.formulaCaculate(rowCursorTotal, k);
			}
			rowCursorTotal++;
		}
		for (int j = 0; j < 3; j++) {
			for (int k = 6; k <= 14; k += 2) {
				makeExcel.formulaCaculate(rowCursorTotal, k);
			}
			rowCursorTotal++;
		}

		return listEmpClass;
	}

	private void setEmpClassList(List<Map<String, String>> listEmpClass, int pfYear, int pfSeason, int type) throws LogicException {
		this.info("setEmpClassList ... ");

		String sheetName = "職級名冊";

		if (type == 1) {
			if (pfSeason == 4) {
				pfYear++;
				pfSeason = 1;
			} else {
				pfSeason++;
			}
			sheetName += "(稿)";
		}

		makeExcel.setSheet(sheetName, (pfYear - 1911) + "Q" + pfSeason);

		makeExcel.setValue(1, 1, (pfYear - 1911) + "年第" + pfSeason + "季房貸協辦職級名冊");

		if (listEmpClass == null || listEmpClass.isEmpty()) {
			return;
		}

		int rowCursor = 3;

		if (listEmpClass.size() > 1) {
			makeExcel.setShiftRow(rowCursor + 1, listEmpClass.size() - 1);
		}

		for (Map<String, String> m : listEmpClass) {

			// 序號
			makeExcel.setValue(rowCursor, 1, rowCursor - 2);
			// 部室
			makeExcel.setValue(rowCursor, 2, m.get("Dept"));
			// 區部
			makeExcel.setValue(rowCursor, 3, m.get("Dist"));
			// 單位
			makeExcel.setValue(rowCursor, 4, m.get("Unit"));
			// 姓名
			makeExcel.setValue(rowCursor, 5, m.get("EmpName"));
			// 員工代號
			makeExcel.setValue(rowCursor, 6, m.get("EmpNo"));

			if (type == 1) {
				// 考核後職級
				makeExcel.setValue(rowCursor, 7, m.get("AfterEmpClass"));
			} else {
				// 考核前職級
				makeExcel.setValue(rowCursor, 7, m.get("OriEmpClass"));
			}

			// 考核後職級
			makeExcel.setValue(rowCursor, 8, m.get("AfterEmpClass"));

			if (type == 0) {
				String oriEmpClass = m.get("OriEmpClass");
				String afterEmpClass = m.get("AfterEmpClass");
				this.info("oriEmpClass = " + oriEmpClass);
				this.info("afterEmpClass = " + afterEmpClass);
				if (oriEmpClass.equals(afterEmpClass)) {
					this.info("EmpClass same");
				} else {
					this.info("EmpClass different");
					makeExcel.setValue(rowCursor, 9, "Yes");
				}
			}

			rowCursor++;
		}
	}
}
