package com.st1.itx.trade.LP;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.domain.PfCoOfficerLog;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.PfCoOfficerLogService;
import com.st1.itx.db.service.springjpa.cm.LP005ServiceImpl;
import com.st1.itx.trade.L5.L5407;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LP005Report extends MakeReport {

	@Autowired
	private PfCoOfficerLogService pfCoOfficerLogService;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	LP005ServiceImpl lp005ServiceImpl;

	@Autowired
	L5407 l5407;

	@Autowired
	private Parse parse;

	@Autowired
	private DateUtil dateUtil;

	private int effectiveDateS = 0;
	private int effectiveDateE = 0;
	private int effectiveDateL = 0;
	private List<Map<String, String>> listEmpClass = new ArrayList<>();

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LP005Report exec ...");

		int workMonth = parse.stringToInteger(titaVo.getParam("Ym")) + 191100;

		// 業績年度
		int pfYear = workMonth / 100;

		// 業績月份
		int pfMonth = workMonth % 100;

		int pfSeason = 0;
		int pfMonths = 0;

		// 判斷季度
		if (pfMonth < 4) {
			pfSeason = 1;
			pfMonths = 1;
		} else if (pfMonth < 7) {
			pfSeason = 2;
			pfMonths = 4;
		} else if (pfMonth < 10) {
			pfSeason = 3;
			pfMonths = 7;
		} else {
			pfSeason = 4;
			pfMonths = 10;
		}

		CdWorkMonth tCdWorkMonth = sCdWorkMonthService.findById(new CdWorkMonthId(pfYear, pfMonths), titaVo);
		if (tCdWorkMonth != null) {
			effectiveDateS = tCdWorkMonth.getStartDate() + 19110000;
		}
		tCdWorkMonth = sCdWorkMonthService.findById(new CdWorkMonthId(pfYear, pfMonth), titaVo);
		if (tCdWorkMonth != null) {
			effectiveDateE = tCdWorkMonth.getEndDate() + 19110000;
		}

		// 前一季度
		int lPfYear = pfYear;
		int lPfMonth = 0;
		// 判斷季度
		if (pfSeason == 1) {
			lPfYear--;
			lPfMonth = 10;
		} else if (pfSeason == 2) {
			lPfMonth = 1;
		} else if (pfSeason == 3) {
			lPfMonth = 4;
		} else if (pfSeason == 4) {
			lPfMonth = 7;
		}
		tCdWorkMonth = sCdWorkMonthService.findById(new CdWorkMonthId(lPfYear, lPfMonth), titaVo);
		if (tCdWorkMonth != null) {
			effectiveDateL = tCdWorkMonth.getEndDate() + 19110000;
		}

		this.info("effectiveDateS = " + effectiveDateS);
		this.info("effectiveDateE = " + effectiveDateE);
		this.info("effectiveDateL = " + effectiveDateL);

		exportExcel(pfYear, pfSeason, titaVo);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
		// 考核核算底稿寫入歷程檔
		insertPfCoOfficerLog(titaVo);

	}

	private void exportExcel(int pfYear, int pfSeason, TitaVo titaVo) throws LogicException {

		this.info("exportExcel ... ");

		int maxOfLoops = 3;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LP005";
		String fileItem = "協辦考核核算底稿";
		String defaultSheet = "1月件數";
		// ReportVo reportVo =
		String seasonItem = (pfYear - 1911) + "Q" + pfSeason;
		if (pfSeason == 4) {
			maxOfLoops = 4;
			String fileName = "LP005_" + seasonItem + "協辦考核核算底稿";
			String defaultExcel = "LP005_底稿_協辦考核核算_第四季特別版.xlsx";
			ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
					.setRptItem(fileItem + seasonItem).build();
			// 開啟報表
			this.info("reportVo open");
			makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		} else {

			String fileName = "LP005_" + seasonItem + "協辦考核核算底稿";
			String defaultExcel = "LP005_底稿_協辦考核核算.xlsx";
			ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
					.setRptItem(fileItem + seasonItem).build();
			// 開啟報表
			this.info("reportVo open");
			makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		}

		for (int i = 1; i <= maxOfLoops; i++) {

			int inputWorkMonth = pfYear * 100 + (i + (3 * (pfSeason - 1)));

			this.info("inputWorkMonth = " + inputWorkMonth);

			setCounts(i, inputWorkMonth, titaVo);

			setAmt(i, inputWorkMonth, titaVo);
		}

		listEmpClass = setDept(listEmpClass, "A0B000", "營管", pfYear, pfSeason, titaVo);
		listEmpClass = setDept(listEmpClass, "A0F000", "營推", pfYear, pfSeason, titaVo);
		listEmpClass = setDept(listEmpClass, "A0E000", "業推", pfYear, pfSeason, titaVo);
		listEmpClass = setDept(listEmpClass, "A0M000", "業開", pfYear, pfSeason, titaVo);

		setEmpClassList(listEmpClass, pfYear, pfSeason, 0);
		setEmpClassList(listEmpClass, pfYear, pfSeason, 1);

	}

	// 考核核算底稿寫入歷程檔
	private void insertPfCoOfficerLog(TitaVo titaVo) throws LogicException {
		this.info("insertPfCoOfficerLog  ... ");
		if (listEmpClass == null || listEmpClass.isEmpty()) {
			return;
		}
		// 設定生效日期為工作月止日+1日
		dateUtil.init();
		dateUtil.setDate_1(effectiveDateE - 19110000);
		dateUtil.setDays(1);
		int evaluteEffectiveDate = dateUtil.getCalenderDay();

		List<PfCoOfficerLog> lPfCoOfficerLog = new ArrayList<PfCoOfficerLog>();

		// 刪除重跑前資料
		Slice<PfCoOfficerLog> slPfCoOfficerLog = pfCoOfficerLogService.findAll(0, Integer.MAX_VALUE, titaVo);
		if (slPfCoOfficerLog != null) {
			for (PfCoOfficerLog tPfCoOfficerLog : slPfCoOfficerLog.getContent()) {
				if (tPfCoOfficerLog.getEffectiveDate() == evaluteEffectiveDate
						&& tPfCoOfficerLog.getFunctionCode() == 9) {
					lPfCoOfficerLog.add(tPfCoOfficerLog);
				}
			}
			if (lPfCoOfficerLog.size() > 0) {
				try {
					pfCoOfficerLogService.deleteAll(lPfCoOfficerLog, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "刪除歷程資料時發生錯誤");
				}
			}
		}

		// 核算底稿新增至歷程檔(考核後職級不同)
		for (Map<String, String> m : listEmpClass) {
			if (!m.get("AfterEmpClass").equals(m.get("OriEmpClass"))) {
				this.info("insertPfCoOfficerLog " + m.toString());
				String evalueEmpClass = "";
				switch (m.get("AfterEmpClass")) {
				case "初級":
					evalueEmpClass = "1";
					break;
				case "中級":
					evalueEmpClass = "2";
					break;
				case "高級":
					evalueEmpClass = "3";
					break;
				default:
					break;
				}

				l5407.insertEvalutePfCoOfficerLog(m.get("EmpNo"),
						parse.stringToInteger(m.get("EffectiveDate")) - 19110000, evaluteEffectiveDate, evalueEmpClass,
						titaVo);
			}
		}
	}

	private List<Map<String, String>> putDataToListEmpClass(List<Map<String, String>> listEmpClass,
			Map<String, String> m, String deptSheetName, String oriEmpClass, String afterEmpClass) {

		Map<String, String> mapEmpClass = new HashMap<>();

		mapEmpClass.put("Dept", deptSheetName + "部"); // 部室
		mapEmpClass.put("Dist", m.get("F0")); // 區部
		mapEmpClass.put("Unit", m.get("F1")); // 單位
		mapEmpClass.put("EmpName", m.get("F2")); // 姓名
		mapEmpClass.put("EmpNo", m.get("F3")); // 員工代號
		mapEmpClass.put("EffectiveDate", m.get("F15")); // 生效日期
		mapEmpClass.put("OriEmpClass", oriEmpClass); // 考核前職級
		mapEmpClass.put("AfterEmpClass", afterEmpClass); // 考核後職級
		int effectiveDate = Integer.valueOf(m.get("EffectiveDate"));
		int ineffectiveDate = Integer.valueOf(m.get("IneffectiveDate"));
		int quitDate = Integer.valueOf(m.get("QuitDate"));// 離職/停約日
		int agPostChgDate = Integer.valueOf(m.get("AgPostChgDate")); // 職務異動日
		// 離職/停約日在有在有效期間、 單位不同且職務異動日在有效期間
		if (effectiveDate < quitDate && ineffectiveDate > quitDate) {
			mapEmpClass.put("Remark", "離職/停約日" + (quitDate - 19110000));
		}
		if (!m.get("CenterCode").equals(m.get("AreaCode"))) {
			if (effectiveDate < agPostChgDate && ineffectiveDate > agPostChgDate) {
				mapEmpClass.put("Remark", "職務異動日" + (agPostChgDate - 19110000));
			}
		}
		String lastEmpClass = " ";
		switch (m.get("LastEmpClass")) {
		case "1":
			lastEmpClass = "初級";
			break;
		case "2":
			lastEmpClass = "中級";
			break;
		case "3":
			lastEmpClass = "高級";
			break;
		default:
			break;
		}

		mapEmpClass.put("LastEmpClass", lastEmpClass); // 前季職級
		listEmpClass.add(mapEmpClass);
		this.info("putDataToListEmpClass " + mapEmpClass.toString());

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

	private List<Map<String, String>> setDept(List<Map<String, String>> listEmpClass, String deptCode,
			String deptSheetName, int pfYear, int pfSeason, TitaVo titaVo) throws LogicException {
		this.info("setDept ... ");
		this.info("setDept deptCode = " + deptCode);
		this.info("setDept deptSheetName = " + deptSheetName);

		makeExcel.setSheet(deptSheetName);

		String title = (pfYear - 1911) + "年" + deptSheetName + "部第" + pfSeason + "季房貸協辦人員考核後職級明細表";

		makeExcel.setValue(1, 1, title);

		List<Map<String, String>> listDept = lp005ServiceImpl.queryDept(pfYear, pfSeason, deptCode, effectiveDateS,
				effectiveDateE, titaVo);

		if (listDept == null || listDept.isEmpty()) {
			return listEmpClass;
		}

		int rowCursor = 4;
//		int rowCursorTotal = 5;

		if (listDept.size() > 1) {
			makeExcel.setShiftRow(rowCursor + 1, listDept.size() - 1);
//			rowCursorTotal += listDept.size() - 1;
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

		int refreshFormulaRow = rowCursor;

		// 設公式
		makeExcel.setFormula(rowCursor, 6, BigDecimal.ZERO, "SUM(F4:F" + (rowCursor - 1) + ")", "0");
		makeExcel.setFormula(rowCursor, 7, BigDecimal.ZERO, "SUM(G4:G" + (rowCursor - 1) + ")", "0");
		makeExcel.setFormula(rowCursor, 8, BigDecimal.ZERO, "SUM(H4:H" + (rowCursor - 1) + ")", "0");
		makeExcel.setFormula(rowCursor, 9, BigDecimal.ZERO, "SUM(I4:I" + (rowCursor - 1) + ")", "0");
		makeExcel.setFormula(rowCursor, 10, BigDecimal.ZERO, "SUM(J4:J" + (rowCursor - 1) + ")", "0");
		makeExcel.setFormula(rowCursor, 11, BigDecimal.ZERO, "SUM(K4:K" + (rowCursor - 1) + ")", "0");
		makeExcel.setFormula(rowCursor, 12, BigDecimal.ZERO, "SUM(L4:L" + (rowCursor - 1) + ")", "0");
		makeExcel.setFormula(rowCursor, 13, BigDecimal.ZERO, "SUM(M4:M" + (rowCursor - 1) + ")", "0");
		if (pfSeason == 4) {
			makeExcel.setFormula(rowCursor, 13, BigDecimal.ZERO, "SUM(N4:N" + (rowCursor - 1) + ")", "0");
			makeExcel.setFormula(rowCursor, 14, BigDecimal.ZERO, "SUM(O4:O" + (rowCursor - 1) + ")", "0");
		}

		rowCursor++;

		// 設公式
		makeExcel.setFormula(rowCursor, 6, BigDecimal.ZERO, "SUBTOTAL(3,C4:C" + (rowCursor - 2) + ")", "0");
		makeExcel.setFormula(rowCursor, 8, BigDecimal.ZERO, "SUBTOTAL(3,C4:C" + (rowCursor - 2) + ")", "0");
		makeExcel.setFormula(rowCursor, 10, BigDecimal.ZERO, "SUBTOTAL(3,C4:C" + (rowCursor - 2) + ")", "0");
		makeExcel.setFormula(rowCursor, 12, BigDecimal.ZERO, "SUBTOTAL(3,C4:C" + (rowCursor - 2) + ")", "0");
		if (pfSeason == 4) {
			makeExcel.setFormula(rowCursor, 14, BigDecimal.ZERO, "SUBTOTAL(3,C4:C" + (rowCursor - 2) + ")", "0");
		}

		rowCursor++;
		// 設公式
		makeExcel.setFormula(rowCursor, 6, BigDecimal.ZERO, "COUNTIF(F4:F" + (rowCursor - 3) + ",\"<>0\")", "0");
		makeExcel.setFormula(rowCursor, 8, BigDecimal.ZERO, "COUNTIF(H4:H" + (rowCursor - 3) + ",\"<>0\")", "0");
		makeExcel.setFormula(rowCursor, 10, BigDecimal.ZERO, "COUNTIF(J4:J" + (rowCursor - 3) + ",\"<>0\")", "0");
		makeExcel.setFormula(rowCursor, 12, BigDecimal.ZERO, "COUNTIF(L4:L" + (rowCursor - 3) + ",\"<>0\")", "0");
		if (pfSeason == 4) {
			makeExcel.setFormula(rowCursor, 14, BigDecimal.ZERO, "COUNTIF(N4:N" + (rowCursor - 3) + ",\"<>0\")", "0");
		}

		// 公式重整
		makeExcel.formulaCaculate(refreshFormulaRow, 6);
		makeExcel.formulaCaculate(refreshFormulaRow, 7);
		makeExcel.formulaCaculate(refreshFormulaRow, 8);
		makeExcel.formulaCaculate(refreshFormulaRow, 9);
		makeExcel.formulaCaculate(refreshFormulaRow, 10);
		makeExcel.formulaCaculate(refreshFormulaRow, 11);
		makeExcel.formulaCaculate(refreshFormulaRow, 12);
		makeExcel.formulaCaculate(refreshFormulaRow, 13);
		if (pfSeason == 4) {
			makeExcel.formulaCaculate(refreshFormulaRow, 14);
			makeExcel.formulaCaculate(refreshFormulaRow, 15);
		}

		refreshFormulaRow++;

		// 公式重整
		makeExcel.formulaCaculate(refreshFormulaRow, 6);
		makeExcel.formulaCaculate(refreshFormulaRow, 8);
		makeExcel.formulaCaculate(refreshFormulaRow, 10);
		makeExcel.formulaCaculate(refreshFormulaRow, 12);
		if (pfSeason == 4) {
			makeExcel.formulaCaculate(refreshFormulaRow, 14);
		}

		refreshFormulaRow++;

		// 公式重整
		makeExcel.formulaCaculate(refreshFormulaRow, 6);
		makeExcel.formulaCaculate(refreshFormulaRow, 8);
		makeExcel.formulaCaculate(refreshFormulaRow, 10);
		makeExcel.formulaCaculate(refreshFormulaRow, 12);
		if (pfSeason == 4) {
			makeExcel.formulaCaculate(refreshFormulaRow, 14);
		}

		refreshFormulaRow++;

		makeExcel.formulaCaculate(refreshFormulaRow, 6);
		makeExcel.formulaCaculate(refreshFormulaRow, 8);
		makeExcel.formulaCaculate(refreshFormulaRow, 10);
		makeExcel.formulaCaculate(refreshFormulaRow, 12);
		if (pfSeason == 4) {
			makeExcel.formulaCaculate(refreshFormulaRow, 14);
		}

		return listEmpClass;
	}

	private void setEmpClassList(List<Map<String, String>> listEmpClass, int pfYear, int pfSeason, int type)
			throws LogicException {
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

			switch (type) {
			case 0:
				// 前季職級
				makeExcel.setValue(rowCursor, 7, m.get("LastEmpClass"));
				// 現在職級
				makeExcel.setValue(rowCursor, 8, m.get("OriEmpClass"));
				// 備註
				makeExcel.setValue(rowCursor, 9, m.get("Remark"));
				break;
			case 1:
				// 考核前職級
				makeExcel.setValue(rowCursor, 7, m.get("OriEmpClass"));
				// 考核後職級
				makeExcel.setValue(rowCursor, 8, m.get("AfterEmpClass"));
				// 職級異動(Y)
				if (!m.get("OriEmpClass").equals(m.get("AfterEmpClass"))) {
					makeExcel.setValue(rowCursor, 9, "Yes");
				}
				break;
			}
			rowCursor++;
		}
	}
}
