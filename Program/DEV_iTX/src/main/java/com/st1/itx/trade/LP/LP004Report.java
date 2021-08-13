package com.st1.itx.trade.LP;

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
import com.st1.itx.db.service.springjpa.cm.LP004ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LP004Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LP004Report.class);

	@Autowired
	LP004ServiceImpl LP004ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {
 
	}

	List<Map<String, String>> findList = new ArrayList<>();

	BigDecimal lcntT = BigDecimal.ZERO;
	BigDecimal lamtT = BigDecimal.ZERO;
	BigDecimal ncntT = BigDecimal.ZERO;
	BigDecimal namtT = BigDecimal.ZERO;

	int row = 0;

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP004", "內網業績統計報表", "LP004單位成績(內部網站)",
				"單位成績(內部網站).xls", "區域中心");

		List<Map<String, String>> wkSsnList = new ArrayList<>();

		try {
			wkSsnList = LP004ServiceImpl.wkSsn(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP003ServiceImpl.wkSsn error = " + errors.toString());
		}

		if (wkSsnList.size() > 0) {
			exportExcel(titaVo, wkSsnList.get(0));

			// 區域中心
//			exportExcelfindArea(titaVo, wkSsnList.get(0));
			// 房貸部專
//			exportExcelAllDeptTable(titaVo, wkSsnList.get(0));
			// 房貸專員
//			exportExcelBsOfficer(titaVo, wkSsnList.get(0));
			// 部室
//			exportExcelDistTable(titaVo, wkSsnList.get(0));
			// 區部
//			exportExcelsDeptTable(titaVo, wkSsnList.get(0));
			// 四個個別部室
			exportExcelUnitTable(titaVo, wkSsnList.get(0), "A0B000");
			exportExcelUnitTable(titaVo, wkSsnList.get(0), "A0F000");
			exportExcelUnitTable(titaVo, wkSsnList.get(0), "A0E000");
			exportExcelUnitTable(titaVo, wkSsnList.get(0), "A0M000");

			// 單位總表
			exportExcelUnitTable(titaVo, wkSsnList.get(0), "");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {

		String iENTDY = titaVo.get("ENTDY").substring(1, 4) + "." + titaVo.get("ENTDY").substring(4, 6) + "."
				+ titaVo.get("ENTDY").substring(6, 8);
		String iYYMM = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911) + "." + wkSsnVo.get("F1");

		makeExcel.setSheet("區域中心");
		makeExcel.setValue(1, 1, iYYMM + "工作月　區域中心業績統計報表");
		makeExcel.setValue(2, 1, "結算日期：" + iENTDY, "R");
		setArea(titaVo, wkSsnVo);

		makeExcel.setSheet("房貸部專");
		makeExcel.setValue(1, 1, iYYMM + "工作月房貸部專業績統計報表");
		makeExcel.setValue(2, 1, "結算日期：" + iENTDY, "R");
		setAllDept(titaVo, wkSsnVo);

		makeExcel.setSheet("房貸專員");
		makeExcel.setValue(1, 1, iYYMM + "工作月房貸專員業績統計報表");
		makeExcel.setValue(2, 1, "結算日期：" + iENTDY, "R");
		setBsOfficer(titaVo, wkSsnVo);

		makeExcel.setSheet("部室");
		makeExcel.setValue(1, 1, iYYMM + "工作月各部室房貸業績達成率排行　                        結算日期：" + iENTDY);
		setDist(titaVo, wkSsnVo);

		makeExcel.setSheet("區部");
		makeExcel.setValue(1, 1, iYYMM + "工作月各區部房貸業績達成率排行　                        結算日期：" + iENTDY);
		setDept(titaVo, wkSsnVo);

		makeExcel.setSheet("營管");
		makeExcel.setValue(1, 1, iYYMM + "工作月營管部通訊處房貸業績達成率排行　                        結算日期：" + iENTDY);

		makeExcel.setSheet("營推");
		makeExcel.setValue(1, 1, iYYMM + "工作月營推部通訊處房貸業績達成率排行　                        結算日期：" + iENTDY);

		makeExcel.setSheet("業推");
		makeExcel.setValue(1, 1, iYYMM + "工作月業推部通訊處房貸業績達成率排行　                        結算日期：" + iENTDY);

		makeExcel.setSheet("業開");
		makeExcel.setValue(1, 1, iYYMM + "工作月業開部通訊處房貸業績達成率排行　                        結算日期：" + iENTDY);

		makeExcel.setSheet("單位總表");
		makeExcel.setValue(1, 1, iYYMM + "工作月單位總表房貸業績達成率排行　                        結算日期：" + iENTDY);
	}

	private void setArea(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setArea");

		try {

			findList = LP004ServiceImpl.findArea(titaVo, wkSsnVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP004ServiceImpl.setArea error = " + errors.toString());

		}

		if (findList.size() > 0) {

			row = 3;

			lcntT = BigDecimal.ZERO;
			lamtT = BigDecimal.ZERO;
			ncntT = BigDecimal.ZERO;
			namtT = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : findList) {

				row++;
				// 區域中心
				makeExcel.setValue(row, 1, tLDVo.get("F0"));
				// 經理
				makeExcel.setValue(row, 2, tLDVo.get("F1"));

				// 上月件數
				BigDecimal lcntBD = tLDVo.get("F2") != null || tLDVo.get("F2") == "0" ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F2"));

				lcntBD = lcntBD.setScale(1, 4);
				makeExcel.setValue(row, 3, lcntBD, "0.0", "R");

				// 上月金額
				BigDecimal lamtBD = tLDVo.get("F3") != null || tLDVo.get("F3") == "0" ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));

				makeExcel.setValue(row, 4, lamtBD, "#,##0", "R");

				// 當月件數
				BigDecimal ncntBD = tLDVo.get("F4") != null || tLDVo.get("F4") == "0" ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));

				ncntBD = ncntBD.setScale(1, 4);
				makeExcel.setValue(row, 5, ncntBD, "0.0", "R");

				// 當月金額
				BigDecimal namtBD = tLDVo.get("F5") != null || tLDVo.get("F5") == "0" ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));

				makeExcel.setValue(row, 6, namtBD, "#,##0", "R");

				// 月增件數
				makeExcel.setValue(row, 7, ncntBD.subtract(lcntBD), "0.0", "R");

				// 月增金額
				makeExcel.setValue(row, 8, namtBD.subtract(lamtBD), "#,##0", "R");

				// 金額/件數
				makeExcel.setValue(row, 9, namtBD.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : ncntBD.divide(namtBD),
						"#,##0", "R");

				lcntT = lcntT.add(lcntBD);
				lamtT = lcntT.add(lamtBD);
				ncntT = ncntT.add(ncntBD);
				namtT = namtT.add(namtBD);

			}

			row++;
			makeExcel.setValue(row, 3, lcntT, "0.0", "R");
			makeExcel.setValue(row, 4, lamtT, "#,##0", "R");
			makeExcel.setValue(row, 5, ncntT, "0.0", "R");
			makeExcel.setValue(row, 6, namtT, "#,##0", "R");
			makeExcel.setValue(row, 7, ncntT.subtract(lcntT), "0.0", "R");
			makeExcel.setValue(row, 8, namtT.subtract(lamtT), "#,##0", "R");

			makeExcel.setValue(row, 9, ncntT.equals(BigDecimal.ZERO) || namtT.equals(BigDecimal.ZERO) ? BigDecimal.ZERO
					: ncntT.divide(namtT), "#,##0", "R");
		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	private void setAllDept(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setAllDept");

		try {
			findList = LP004ServiceImpl.findAllDept(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP004ServiceImpl.setAllDept error = " + errors.toString());
		}

		// 排行用
		int i = 1;
		if (findList.size() > 0) {

			row = 3;

			lcntT = BigDecimal.ZERO;
			lamtT = BigDecimal.ZERO;
			ncntT = BigDecimal.ZERO;
			namtT = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : findList) {
				row++;

				makeExcel.setValue(row, 1, i);
				makeExcel.setValue(row, 2, tLDVo.get("F0"));
				makeExcel.setValue(row, 3, tLDVo.get("F1"));
				makeExcel.setValue(row, 4, tLDVo.get("F2"));

				// 責任額
				BigDecimal lgamtBD = tLDVo.get("F3") != null || tLDVo.get("F3") == "0" ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));

				makeExcel.setValue(row, 5, lgamtBD, "#,##0", "R");

				// 撥款金額
				BigDecimal lamtBD = tLDVo.get("F4") != null || tLDVo.get("F4") == "0" ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));

				makeExcel.setValue(row, 6, lamtBD, "#,##0", "R");

				// 件數
				BigDecimal ncntBD = tLDVo.get("F5") != null || tLDVo.get("F5") == "0" ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));

				ncntBD = ncntBD.setScale(1, 4);
				makeExcel.setValue(row, 7, ncntBD, "0.0", "R");

				// 達成率
				BigDecimal goal = tLDVo.get("F6") != null || tLDVo.get("F6") == "0" ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				makeExcel.setValue(row, 8, goal.multiply(new BigDecimal("100")) + "%", "#,##0.00", "R");

				// 部室
				makeExcel.setValue(row, 9, tLDVo.get("F7"), "C");

				i++;

				// 責任額總計
				lcntT = lcntT.add(lgamtBD);
				// 撥款金額
				lamtT = lcntT.add(lamtBD);
				// 件數
				ncntT = ncntT.add(ncntBD);
				// 達成率
				namtT = namtT.add(goal);

			}

			row++;

			makeExcel.setValue(row, 1, "總計", "C");
			makeExcel.setValue(row, 5, lcntT, "#,##0");
			makeExcel.setValue(row, 6, lamtT, "#,##0");
			makeExcel.setValue(row, 7, ncntT, "0.0");
			makeExcel.setValue(row, 8, namtT.multiply(new BigDecimal("100")) + "%", "#,##0.00", "R");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	private void setBsOfficer(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setBsOfficer");

		try {
			findList = LP004ServiceImpl.findBsOfficer(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP004ServiceImpl.setBsOfficer error = " + errors.toString());
		}

		// 排行用
		int i = 1;
		if (findList.size() > 0) {

			lcntT = BigDecimal.ZERO;
			lamtT = BigDecimal.ZERO;
			ncntT = BigDecimal.ZERO;
			namtT = BigDecimal.ZERO;

			row = 3;

			for (Map<String, String> tLDVo : findList) {
				row++;
				makeExcel.setValue(row, 1, i);
				makeExcel.setValue(row, 2, tLDVo.get("F0"));
				makeExcel.setValue(row, 3, tLDVo.get("F1"));

				// 責任額
				BigDecimal gamtBD = tLDVo.get("F2") != null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F2"));

				makeExcel.setValue(row, 4, gamtBD, "#,##0", "R");

				// 撥款金額
				BigDecimal lamtBD = tLDVo.get("F3") != null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));

				makeExcel.setValue(row, 5, lamtBD, "#,##0", "R");

				// 件數
				BigDecimal ncntBD = tLDVo.get("F4") != null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));

				makeExcel.setValue(row, 6, ncntBD, "0.0", "R");

				// 達成率
				makeExcel.setValue(row, 7, gamtBD.equals(BigDecimal.ZERO) ? 0
						: lamtBD.divide(gamtBD, 1, 2).multiply(new BigDecimal("100")) + "%", "#,##0.00", "R");
				// 部室
				makeExcel.setValue(row, 8, tLDVo.get("F5"), "C");
				// 區部
				if (tLDVo.get("F6").length() > 3) {
					makeExcel.setValue(row, 9, tLDVo.get("F6"), "R");
				} else {
					makeExcel.setValue(row, 9, tLDVo.get("F6") + "區部", "R");
				}

				lcntT = lcntT.add(gamtBD);
				lamtT = lcntT.add(lamtBD);
				ncntT = ncntT.add(ncntBD);

				namtT = gamtBD.equals(BigDecimal.ZERO) ? namtT.add(BigDecimal.ZERO)
						: namtT.add(lamtBD.divide(gamtBD, 1, 2));

				i++;

			}
			row++;

			makeExcel.setValue(row, 3, "總計", "C");
			makeExcel.setValue(row, 4, lcntT, "#,##0", "R");
			makeExcel.setValue(row, 5, lamtT, "#,##0", "R");
			makeExcel.setValue(row, 6, ncntT, "0.0", "R");
			makeExcel.setValue(row, 7, namtT + "%", "#,##0.00", "R");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	private void setDist(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setDist");

		try {
			findList = LP004ServiceImpl.findDist(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP004ServiceImpl.setDist error = " + errors.toString());
		}

		// 排行用
		int i = 1;
		if (findList.size() > 0) {

			row = 2;

			lcntT = BigDecimal.ZERO;
			lamtT = BigDecimal.ZERO;
			ncntT = BigDecimal.ZERO;
			namtT = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : findList) {
				row++;
				makeExcel.setValue(row, 1, i);
				makeExcel.setValue(row, 2, tLDVo.get("F0"));
				makeExcel.setValue(row, 3, tLDVo.get("F1"));
				makeExcel.setValue(row, 4, tLDVo.get("F2"));

				// 件數
				BigDecimal lcntBD = tLDVo.get("F2") != null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F2"));
				makeExcel.setValue(row, 5, lcntBD, "0.0", "R");

				// 金額
				BigDecimal lamtBD = tLDVo.get("F3") != null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(row, 6, lamtBD, "#,##0", "R");

				// 季累計件數
				BigDecimal ncntBD = tLDVo.get("F4") != null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));

				makeExcel.setValue(row, 7, ncntBD, "0.0", "R");

				// 季累計金額
				BigDecimal namtBD = tLDVo.get("F6") != null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				makeExcel.setValue(row, 8, ncntBD, "#,##0", "R");

				i++;

				lcntT = lcntT.add(lcntBD);
				lamtT = lcntT.add(lamtBD);
				ncntT = ncntT.add(ncntBD);
				namtT = namtT.add(namtBD);
			}

			row++;

			makeExcel.setValue(row, 4, "合計", "C");
			makeExcel.setValue(row, 5, lcntT, "#,##0", "R");
			makeExcel.setValue(row, 6, lamtT, "#,##0", "R");
			makeExcel.setValue(row, 7, ncntT, "#,##0", "R");
			makeExcel.setValue(row, 8, namtT, "#,##0", "R");

		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}
	}

	private void setDept(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setDept");

		List<Map<String, String>> findMortgageSpecialist = new ArrayList<>();
		try {
			findList = LP004ServiceImpl.findAsDept(titaVo, wkSsnVo);
			findMortgageSpecialist = LP004ServiceImpl.findMortgageSpecialist(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP004ServiceImpl.setDept error = " + errors.toString());
		}
		String MortgageSpecialist = "";
		int cnt = 0;
		int amt = 0;
		int tcnt = 0;
		int tamt = 0;
		int wkcnt = 0;
		int wkamt = 0;
		int qcnt = 0;
		int qamt = 0;
		makeExcel.setSheet("區部");
		// 排行用
		int i = 1;
		if (findList.size() > 0) {

			lcntT = BigDecimal.ZERO;
			lamtT = BigDecimal.ZERO;
			ncntT = BigDecimal.ZERO;
			namtT = BigDecimal.ZERO;

			row = 2;

			for (Map<String, String> tLDVo : findList) {
				row++;
				makeExcel.setValue(row, 1, i);
				makeExcel.setValue(row, 2, tLDVo.get("F0"));
				makeExcel.setValue(row, 3, tLDVo.get("F1"));

				if (findMortgageSpecialist.size() > 0) {
					MortgageSpecialist = "";
					for (Map<String, String> tLDVo2 : findMortgageSpecialist) {

						if (tLDVo2.get("F2").length() > 4) {
							if (tLDVo.get("F1").substring(0, 2).equals(tLDVo2.get("F2").substring(0, 2))
									|| tLDVo.get("F1").substring(0, 2).equals(tLDVo2.get("F2").substring(3, 5))) {
								this.info("tLDVo2.get(\"F0\")" + tLDVo2.get("F0"));
								this.info("tLDVo2.get(\"F2\")" + tLDVo2.get("F2").substring(0, 2));
								this.info("tLDVo.get(\"F1\")" + tLDVo.get("F1").substring(0, 2));
								MortgageSpecialist = tLDVo2.get("F0");
							}
						} else {
							if (tLDVo.get("F1").substring(0, 2).equals(tLDVo2.get("F2").substring(0, 2))) {
								this.info("tLDVo2.get(\"F0\")" + tLDVo2.get("F0"));
								this.info("tLDVo2.get(\"F2\")" + tLDVo2.get("F2").substring(0, 2));
								this.info("tLDVo.get(\"F1\")" + tLDVo.get("F1").substring(0, 2));
								MortgageSpecialist = tLDVo2.get("F0");
							}
						}
					}
				}
				makeExcel.setValue(row, 4, tLDVo.get("F2"));

				makeExcel.setValue(row, 5, MortgageSpecialist);
				BigDecimal lcntBD = tLDVo.get("F3") != null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));

				BigDecimal lamtBD = tLDVo.get("F4") != null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));

				BigDecimal ncntBD = tLDVo.get("F5") != null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));

				BigDecimal namtBD = tLDVo.get("F6") != null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				cnt = lcntBD.intValue();
				amt = lamtBD.intValue();
				tcnt = ncntBD.intValue();
				tamt = namtBD.intValue();

				makeExcel.setValue(row, 6, cnt, "##0");
				makeExcel.setValue(row, 7, amt, "#,##0");
				makeExcel.setValue(row, 8, tcnt, "##0");
				makeExcel.setValue(row, 9, tamt, "#,##0");
				i++;

				wkcnt += cnt;
				wkamt += amt;
				qcnt += tcnt;
				qamt += tamt;
			}
			if (findList.size() == (i - 1)) {
				makeExcel.setValue(row + 1, 5, "合計");
				makeExcel.setValue(row + 1, 6, wkcnt, "##0");
				makeExcel.setValue(row + 1, 7, wkamt, "#,##0");
				makeExcel.setValue(row + 1, 8, qcnt, "##0");
				makeExcel.setValue(row + 1, 9, qamt, "#,##0");
			}
		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}
	}

	private void exportExcelUnitTable(TitaVo titaVo, Map<String, String> wkSsnVo, String deptCode)
			throws LogicException {
		this.info("===========exportExcelUnitTable");

		try {
			findList = LP004ServiceImpl.findAll(titaVo, wkSsnVo, deptCode);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP004ServiceImpl.exportExcelUnitTable error = " + errors.toString());
		}

		int cnt = 0;
		int amt = 0;
		int tcnt = 0;
		int tamt = 0;
		int wkcnt = 0;
		int wkamt = 0;
		int qcnt = 0;
		int qamt = 0;

		switch (deptCode) {
		case "A0B000":
			makeExcel.setSheet("營管");
			break;
		case "A0F000":
			makeExcel.setSheet("營推");
			break;
		case "A0E000":
			makeExcel.setSheet("業推");
			break;
		case "A0M000":
			makeExcel.setSheet("業開");
			break;
		default:
			makeExcel.setSheet("單位總表");
			break;
		}
		// 排行用
		int i = 1;
		if (findList.size() > 0) {

			row = 2;
			for (Map<String, String> tLDVo : findList) {
				row++;
				makeExcel.setValue(row, 1, i);
				makeExcel.setValue(row, 2, tLDVo.get("F0"));
				makeExcel.setValue(row, 3, tLDVo.get("F1"));
				makeExcel.setValue(row, 4, tLDVo.get("F2"));
				makeExcel.setValue(row, 5, tLDVo.get("F3"));

				BigDecimal lcntBD = tLDVo.get("F4") != null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));

				BigDecimal lamtBD = tLDVo.get("F5") != null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));

				BigDecimal ncntBD = tLDVo.get("F6") != null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				BigDecimal namtBD = tLDVo.get("F7") != null || tLDVo.get("F7").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F7"));
				this.info("主管:" + tLDVo.get("F3") + ",lcntBD件數:" + lcntBD + ",lamtBD金額:" + lamtBD + ",ncntBD季累件數:"
						+ ncntBD + ",namtBD季累金額:" + namtBD);
				cnt = lcntBD.intValue();
				amt = lamtBD.intValue();
				tcnt = ncntBD.intValue();
				tamt = namtBD.intValue();

				makeExcel.setValue(row, 6, cnt, "##0");
				makeExcel.setValue(row, 7, amt, "#,##0");
				makeExcel.setValue(row, 8, tcnt, "##0");
				makeExcel.setValue(row, 9, tamt, "#,##0");
				i++;

				wkcnt += cnt;
				wkamt += amt;
				qcnt += tcnt;
				qamt += tamt;
			}
			if (!deptCode.isEmpty()) {
				makeExcel.setValue(row + 1, 5, "合計");
				makeExcel.setValue(row + 1, 6, wkcnt, "##0");
				makeExcel.setValue(row + 1, 7, wkamt, "#,##0");
				makeExcel.setValue(row + 1, 8, qcnt, "##0");
				makeExcel.setValue(row + 1, 9, qamt, "#,##0");
			}
		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}
	}
}