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

	@Autowired
	LP004ServiceImpl lP004ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	List<Map<String, String>> findList = new ArrayList<>();

	int row = 0;

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP004", "內網業績統計報表", "LP004單位成績(內部網站)",
				"LP004_底稿_單位成績(內部網站).xlsx", "放款審查各區");

		List<Map<String, String>> wkSsnList = new ArrayList<>();
		Map<String, String> wkSsnVo = null;
		try {
			wkSsnList = lP004ServiceImpl.wkSsn(titaVo);

			wkSsnVo = wkSsnList.get(0);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP004ServiceImpl.wkSsn error = " + errors.toString());
		}

		if (wkSsnList.size() > 0) {

			String iENTDY = titaVo.get("ENTDY").substring(1, 4) + "." + titaVo.get("ENTDY").substring(4, 6) + "."
					+ titaVo.get("ENTDY").substring(6, 8);
			String iYYMM = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911) + "." + wkSsnVo.get("F1");

			makeExcel.setSheet("放款審查各區");
			makeExcel.setValue(1, 1, iYYMM + "工作月　放款審查各區業績統計報表");
			makeExcel.setValue(2, 1, "結算日期：" + iENTDY, "R");
			setArea(titaVo, wkSsnVo);

			// 同LP003之Sheet2
			makeExcel.setSheet("房貸部專");
			makeExcel.setValue(1, 1, iYYMM + "工作月房貸部專業績統計報表");
			makeExcel.setValue(2, 1, "結算日期：" + iENTDY, "R");
			setDeptSpecialist(titaVo, wkSsnVo);

			makeExcel.setSheet("房貸專員");
			makeExcel.setValue(1, 1, iYYMM + "工作月房貸專員業績統計報表");
			makeExcel.setValue(2, 1, "結算日期：" + iENTDY, "R");
			setBsOfficer(titaVo, wkSsnVo);

			makeExcel.setSheet("部室");
			makeExcel.setValue(1, 1, iYYMM + "工作月各部室房貸業績達成率排行　                        結算日期：" + iENTDY);
			setDept(titaVo, wkSsnVo);

			makeExcel.setSheet("區部");
			makeExcel.setValue(1, 1, iYYMM + "工作月各區部房貸業績達成率排行　                        結算日期：" + iENTDY);
			setDist(titaVo, wkSsnVo);

			makeExcel.setSheet("營管");
			makeExcel.setValue(1, 1, iYYMM + "工作月營管部通訊處房貸業績達成率排行　                        結算日期：" + iENTDY);
			exportExcelUnitTable(titaVo, wkSsnVo, "A0B000");

			makeExcel.setSheet("營推");
			makeExcel.setValue(1, 1, iYYMM + "工作月營推部通訊處房貸業績達成率排行　                        結算日期：" + iENTDY);
			exportExcelUnitTable(titaVo, wkSsnVo, "A0F000");

			makeExcel.setSheet("業推");
			makeExcel.setValue(1, 1, iYYMM + "工作月業推部通訊處房貸業績達成率排行　                        結算日期：" + iENTDY);
			exportExcelUnitTable(titaVo, wkSsnVo, "A0E000");

			makeExcel.setSheet("業開");
			makeExcel.setValue(1, 1, iYYMM + "工作月業開部通訊處房貸業績達成率排行　                        結算日期：" + iENTDY);
			exportExcelUnitTable(titaVo, wkSsnVo, "A0M000");

			makeExcel.setSheet("單位總表");
			makeExcel.setValue(1, 1, iYYMM + "工作月單位總表房貸業績達成率排行　                        結算日期：" + iENTDY);
			exportExcelUnitTable(titaVo, wkSsnVo, "");

		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	/**
	 * 放款審查課各區
	 */
	private void setArea(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setArea");
		try {
			findList = lP004ServiceImpl.findArea(titaVo, wkSsnVo);
		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP004ServiceImpl.setArea error = " + errors.toString());

		}

		if (findList.size() > 0) {

			row = 3;
			// 達成件數 總計
			BigDecimal lcntBDTotal = BigDecimal.ZERO;
			// 達成金額 總計
			BigDecimal lamtBDTotal = BigDecimal.ZERO;
			// 季累計件數 總計
			BigDecimal ncntBDTotal = BigDecimal.ZERO;
			// 季累計金額 總計
			BigDecimal namtBDTotal = BigDecimal.ZERO;


			for (Map<String, String> tLDVo : findList) {

				row++;
				// 區域中心
				makeExcel.setValue(row, 1, tLDVo.get("F0"));
				// 經理
				makeExcel.setValue(row, 2, tLDVo.get("F1"));

				// 上月件數
				BigDecimal lcntBD = getBigDecimal(tLDVo.get("F2"));
				lcntBD = lcntBD.setScale(1, 4);
				makeExcel.setValue(row, 3, lcntBD, "0.0", "R");

				// 上月金額
				BigDecimal lamtBD =getBigDecimal(tLDVo.get("F3"));

				makeExcel.setValue(row, 4, lamtBD, "#,##0", "R");

				// 當月件數
				BigDecimal ncntBD = getBigDecimal(tLDVo.get("F4"));

				ncntBD = ncntBD.setScale(1, 4);
				makeExcel.setValue(row, 5, ncntBD, "0.0", "R");

				// 當月金額
				BigDecimal namtBD = getBigDecimal(tLDVo.get("F5"));

				makeExcel.setValue(row, 6, namtBD, "#,##0", "R");

				// 月增件數
				makeExcel.setValue(row, 7, ncntBD.subtract(lcntBD), "0.0", "R");

				// 月增金額
				makeExcel.setValue(row, 8, namtBD.subtract(lamtBD), "#,##0", "R");

				// 金額/件數
//				makeExcel.setValue(row, 9, namtBD.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : ncntBD.divide(namtBD,0,BigDecimal.ROUND_HALF_UP),
//						"#,##0", "R");
				makeExcel.setValue(row, 9, this.computeDivide(ncntBD, namtBD, 0),
						"#,##0", "R");
				
				lcntBDTotal = lcntBDTotal.add(lcntBD);
				lamtBDTotal = lamtBDTotal.add(lamtBD);
				ncntBDTotal = ncntBDTotal.add(ncntBD);
				namtBDTotal = namtBDTotal.add(namtBD);

			}

			row++;
			makeExcel.setValue(row, 3, lcntBDTotal, "0.0", "R");
			makeExcel.setValue(row, 4, lamtBDTotal, "#,##0", "R");
			makeExcel.setValue(row, 5, ncntBDTotal, "0.0", "R");
			makeExcel.setValue(row, 6, namtBDTotal, "#,##0", "R");
			makeExcel.setValue(row, 7, ncntBDTotal.subtract(lcntBDTotal), "0.0", "R");
			makeExcel.setValue(row, 8, namtBDTotal.subtract(lamtBDTotal), "#,##0", "R");
			
			makeExcel.setValue(row, 9, this.computeDivide(ncntBDTotal, namtBDTotal, 0), "#,##0", "R");
		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 房貸部專
	 */
	private void setDeptSpecialist(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setAllDept");
		try {
			findList = lP004ServiceImpl.setDeptSpecialist(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP004ServiceImpl.setDeptSpecialist error = " + errors.toString());
		}

		if (findList.size() > 0) {

			row = 3;
			// 房貸責任額總計
			BigDecimal liabilityTotal = BigDecimal.ZERO;
			// 房貸撥款總計
			BigDecimal drawdownAmtTotal = BigDecimal.ZERO;
			// 房貸件數
			BigDecimal numberOfPiecesTotal = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : findList) {
				row++;
				// 排行
				makeExcel.setValue(row, 1, Integer.parseInt(tLDVo.get("F0")));
				// 駐在地
				makeExcel.setValue(row, 2, tLDVo.get("F1"));
				// 姓名
				makeExcel.setValue(row, 3, tLDVo.get("F2"));
				// 電腦編號(員工編號)
				makeExcel.setValue(row, 4, tLDVo.get("F3"));

				// 責任額
				BigDecimal liability = getBigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(row, 5, liability, "#,##0", "R");

				// 撥款金額
				BigDecimal drawdownAmt = getBigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(row, 6, drawdownAmt, "#,##0", "R");

				// 件數
				BigDecimal numberOfPieces = getBigDecimal(tLDVo.get("F6"));
				makeExcel.setValue(row, 7, numberOfPieces, "0.0", "R");

				// 達成率
				makeExcel.setValue(row, 8, this.computeDivide(drawdownAmt, liability, 4), "0.00%");

				// 部室
				makeExcel.setValue(row, 9, tLDVo.get("F7"), "C");

				// 責任額總計
				liabilityTotal = liabilityTotal.add(liability);
				// 撥款金額
				drawdownAmtTotal = drawdownAmtTotal.add(drawdownAmt);
				// 件數
				numberOfPiecesTotal = numberOfPiecesTotal.add(numberOfPieces);

			}

			row++;

			makeExcel.setValue(row, 1, "總計", "C");
			makeExcel.setValue(row, 5, liabilityTotal, "#,##0", "R");
			makeExcel.setValue(row, 6, drawdownAmtTotal, "#,##0", "R");
			makeExcel.setValue(row, 7, numberOfPiecesTotal, "0.0", "R");
			makeExcel.setValue(row, 8, this.computeDivide(drawdownAmtTotal, liabilityTotal, 4), "0.00%");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 房貸專員
	 */
	private void setBsOfficer(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setBsOfficer");
		try {
			findList = lP004ServiceImpl.findPfBsOfficer(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP004ServiceImpl.setBsOfficer error = " + errors.toString());
		}

		if (findList.size() > 0) {

			// 房貸責任額總計
			BigDecimal liabilityTotal = BigDecimal.ZERO;
			// 房貸撥款總計
			BigDecimal drawdownAmtTotal = BigDecimal.ZERO;
			// 房貸件數
			BigDecimal numberOfPiecesTotal = BigDecimal.ZERO;

			row = 3;

			for (Map<String, String> tLDVo : findList) {
				row++;
				// 排行
				makeExcel.setValue(row, 1, Integer.parseInt(tLDVo.get("F0")));
				// 姓名
				makeExcel.setValue(row, 2, tLDVo.get("F1"));
				// 電腦編號(員工編號)
				makeExcel.setValue(row, 3, tLDVo.get("F2"));

				// 責任額
				BigDecimal liability = getBigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(row, 4, liability, "#,##0", "R");

				// 撥款金額
				BigDecimal drawdownAmt = getBigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(row, 5, drawdownAmt, "#,##0", "R");

				// 件數
				BigDecimal numberOfPieces = getBigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(row, 6, numberOfPieces, "0.0", "R");

				// 達成率
				makeExcel.setValue(row, 7, this.computeDivide(drawdownAmt, liability, 4), "0.00%");

				// 部室
				makeExcel.setValue(row, 8, tLDVo.get("F6"), "C");

				// 服務區部
				makeExcel.setValue(row, 9, tLDVo.get("F7"), "C");

				// 責任額總計
				liabilityTotal = liabilityTotal.add(liability);
				// 撥款金額
				drawdownAmtTotal = drawdownAmtTotal.add(drawdownAmt);
				// 件數
				numberOfPiecesTotal = numberOfPiecesTotal.add(numberOfPieces);

			}
			row++;

			makeExcel.setValue(row, 3, "總計", "C");
			makeExcel.setValue(row, 4, liabilityTotal, "#,##0", "R");
			makeExcel.setValue(row, 5, drawdownAmtTotal, "#,##0", "R");
			makeExcel.setValue(row, 6, numberOfPiecesTotal, "0.0", "R");
			makeExcel.setValue(row, 7, this.computeDivide(drawdownAmtTotal, liabilityTotal, 4), "0.00%");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 部室
	 */
	private void setDept(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setDept");

		try {
			findList = lP004ServiceImpl.findDept(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP004ServiceImpl.setDept error = " + errors.toString());
		}

		// 排行用
		int i = 1;
		if (findList.size() > 0) {

			row = 2;

			// 達成件數 總計
			BigDecimal lcntBDTotal = BigDecimal.ZERO;
			// 達成金額 總計
			BigDecimal lamtBDTotal = BigDecimal.ZERO;
			// 季累計件數 總計
			BigDecimal ncntBDTotal = BigDecimal.ZERO;
			// 季累計金額 總計
			BigDecimal namtBDTotal = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : findList) {
				row++;
				makeExcel.setValue(row, 1, i);
				makeExcel.setValue(row, 2, tLDVo.get("F0"));
				makeExcel.setValue(row, 3, tLDVo.get("F1"));
				makeExcel.setValue(row, 4, tLDVo.get("F2"));

				// 件數
				BigDecimal lcntBD = getBigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(row, 5, lcntBD, "0.0", "R");

				// 金額
				BigDecimal lamtBD = getBigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(row, 6, lamtBD, "#,##0", "R");

				// 季累計件數
				BigDecimal ncntBD = getBigDecimal(tLDVo.get("F5"));

				makeExcel.setValue(row, 7, ncntBD, "0.0", "R");

				// 季累計金額
				BigDecimal namtBD = getBigDecimal(tLDVo.get("F6"));

				makeExcel.setValue(row, 8, ncntBD, "#,##0", "R");

				i++;

				lcntBDTotal = lcntBDTotal.add(lcntBD);
				lamtBDTotal = lamtBDTotal.add(lamtBD);
				ncntBDTotal = ncntBDTotal.add(ncntBD);
				namtBDTotal = namtBDTotal.add(namtBD);
			}

			row++;

			makeExcel.setValue(row, 4, "合計", "C");
			makeExcel.setValue(row, 5, lcntBDTotal, "#,##0", "R");
			makeExcel.setValue(row, 6, lamtBDTotal, "#,##0", "R");
			makeExcel.setValue(row, 7, ncntBDTotal, "#,##0", "R");
			makeExcel.setValue(row, 8, namtBDTotal, "#,##0", "R");

		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}
	}

	/**
	 * 區部
	 */
	private void setDist(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		this.info("===========setDept");

//		List<Map<String, String>> findMortgageSpecialist = new ArrayList<>();
		try {
			findList = lP004ServiceImpl.findDist(titaVo, wkSsnVo);
//			findMortgageSpecialist = lP004ServiceImpl.findMortgageSpecialist(titaVo, wkSsnVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP004ServiceImpl.setDept error = " + errors.toString());
		}
		String mortgageSpecialist = "";

		makeExcel.setSheet("區部");
		// 排行用
		int i = 1;
		if (findList.size() > 0) {

			// 達成件數 總計
			BigDecimal lcntBDTotal = BigDecimal.ZERO;
			// 達成金額 總計
			BigDecimal lamtBDTotal = BigDecimal.ZERO;
			// 季累計件數 總計
			BigDecimal ncntBDTotal = BigDecimal.ZERO;
			// 季累計金額 總計
			BigDecimal namtBDTotal = BigDecimal.ZERO;
			row = 2;

			for (Map<String, String> tLDVo : findList) {
				row++;
				makeExcel.setValue(row, 1, i);
				makeExcel.setValue(row, 2, tLDVo.get("F0"));
				makeExcel.setValue(row, 3, tLDVo.get("F1"));

//				if (findMortgageSpecialist.size() > 0) {
//					mortgageSpecialist = "";
//					for (Map<String, String> tLDVo2 : findMortgageSpecialist) {
//
//						if (tLDVo2.get("F2").length() > 4) {
//							if (tLDVo.get("F1").substring(0, 2).equals(tLDVo2.get("F2").substring(0, 2))
//									|| tLDVo.get("F1").substring(0, 2).equals(tLDVo2.get("F2").substring(3, 5))) {
//								this.info("tLDVo2.get(\"F0\")" + tLDVo2.get("F0"));
//								this.info("tLDVo2.get(\"F2\")" + tLDVo2.get("F2").substring(0, 2));
//								this.info("tLDVo.get(\"F1\")" + tLDVo.get("F1").substring(0, 2));
//								mortgageSpecialist = tLDVo2.get("F0");
//							}
//						} else {
//							if (tLDVo.get("F1").substring(0, 2).equals(tLDVo2.get("F2").substring(0, 2))) {
//								this.info("tLDVo2.get(\"F0\")" + tLDVo2.get("F0"));
//								this.info("tLDVo2.get(\"F2\")" + tLDVo2.get("F2").substring(0, 2));
//								this.info("tLDVo.get(\"F1\")" + tLDVo.get("F1").substring(0, 2));
//								mortgageSpecialist = tLDVo2.get("F0");
//							}
//						}
//					}
//				}
				makeExcel.setValue(row, 4, tLDVo.get("F2"));

				makeExcel.setValue(row, 5, mortgageSpecialist);
				
				// 達成件數
				BigDecimal lcntBD = getBigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(row, 6, lcntBD, "##0", "R");
				// 達成金額
				BigDecimal lamtBD = getBigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(row, 7, lamtBD, "#,##0", "R");
				// 季累計件數
				BigDecimal ncntBD = getBigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(row, 8, ncntBD, "##0", "R");
				// 季累計金額
				BigDecimal namtBD = getBigDecimal(tLDVo.get("F6"));
				makeExcel.setValue(row, 9, namtBD, "#,##0", "R");

				lcntBDTotal = lcntBDTotal.add(lcntBD);
				lamtBDTotal = lamtBDTotal.add(lamtBD);
				ncntBDTotal = ncntBDTotal.add(ncntBD);
				namtBDTotal = namtBDTotal.add(namtBD);
				i++;

			}
			
			row++;
			
			if (findList.size() == (i - 1)) {
				makeExcel.setValue(row , 5, "合計");
				makeExcel.setValue(row, 6, lcntBDTotal, "##0");
				makeExcel.setValue(row , 7, lamtBDTotal, "#,##0");
				makeExcel.setValue(row, 8, ncntBDTotal, "##0");
				makeExcel.setValue(row , 9, namtBDTotal, "#,##0");
			}
		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}
	}

	/**
	 * 各單位
	 */
	private void exportExcelUnitTable(TitaVo titaVo, Map<String, String> wkSsnVo, String deptCode)
			throws LogicException {
		this.info("===========exportExcelUnitTable");

		try {
			findList = lP004ServiceImpl.findAllDept(titaVo, wkSsnVo, deptCode);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP004ServiceImpl.exportExcelUnitTable error = " + errors.toString());
		}

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

			// 達成件數 總計
			BigDecimal lcntBDTotal = BigDecimal.ZERO;
			// 達成金額 總計
			BigDecimal lamtBDTotal = BigDecimal.ZERO;
			// 季累計件數 總計
			BigDecimal ncntBDTotal = BigDecimal.ZERO;
			// 季累計金額 總計
			BigDecimal namtBDTotal = BigDecimal.ZERO;

			row = 2;
			for (Map<String, String> tLDVo : findList) {
				row++;
				makeExcel.setValue(row, 1, i);
				makeExcel.setValue(row, 2, tLDVo.get("F0"));
				makeExcel.setValue(row, 3, tLDVo.get("F1"));
				makeExcel.setValue(row, 4, tLDVo.get("F2"));
				makeExcel.setValue(row, 5, tLDVo.get("F3"));

				// 達成件數
				BigDecimal lcntBD = getBigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(row, 6, lcntBD, "##0", "R");
				// 達成金額
				BigDecimal lamtBD = getBigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(row, 7, lamtBD, "#,##0", "R");
				// 季累計件數
				BigDecimal ncntBD = getBigDecimal(tLDVo.get("F6"));
				makeExcel.setValue(row, 8, ncntBD, "##0", "R");
				// 季累計金額
				BigDecimal namtBD = getBigDecimal(tLDVo.get("F7"));
				makeExcel.setValue(row, 9, namtBD, "#,##0", "R");

				i++;

				lcntBDTotal = lcntBDTotal.add(lcntBD);
				lamtBDTotal = lamtBDTotal.add(lamtBD);
				ncntBDTotal = ncntBDTotal.add(ncntBD);
				namtBDTotal = namtBDTotal.add(namtBD);
			}

			row++;

			if (!deptCode.isEmpty()) {
				makeExcel.setValue(row, 5, "合計");
				makeExcel.setValue(row, 6, lcntBDTotal, "##0");
				makeExcel.setValue(row, 7, lamtBDTotal, "#,##0");
				makeExcel.setValue(row, 8, ncntBDTotal, "##0");
				makeExcel.setValue(row, 9, namtBDTotal, "#,##0");
			}
		} else {
			makeExcel.setValue(3, 1, "本日無資料");
		}
	}
}