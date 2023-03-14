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
import com.st1.itx.db.service.springjpa.cm.LP002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")
// 20201204 T
public class LP002Report extends MakeReport {

	@Autowired
	LP002ServiceImpl lP002ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	List<Map<String, String>> findList = new ArrayList<>();

	BigDecimal[] cntArr = null;
	BigDecimal[] amtArr = null;

	// 前工作月累計
	BigDecimal cntTotal = BigDecimal.ZERO;
	BigDecimal amtTotal = BigDecimal.ZERO;

	// 兩欄位一組
	// 第1組欄位 縱向累計
	BigDecimal cntColTotal1 = BigDecimal.ZERO;
	BigDecimal amtColTotal1 = BigDecimal.ZERO;

	// 第2組欄位 縱向累計
	BigDecimal cntColTotal2 = BigDecimal.ZERO;
	BigDecimal amtColTotal2 = BigDecimal.ZERO;

	// 第3組欄位 縱向累計
	BigDecimal cntColTotal3 = BigDecimal.ZERO;
	BigDecimal amtColTotal3 = BigDecimal.ZERO;

	// 第4組欄位 縱向累計
	BigDecimal cntColTotal4 = BigDecimal.ZERO;
	BigDecimal amtColTotal4 = BigDecimal.ZERO;

	// 第5組欄位 縱向累計
	BigDecimal cntColTotal5 = BigDecimal.ZERO;
	BigDecimal amtColTotal5 = BigDecimal.ZERO;

	// 最後一列的 縱向累計
	BigDecimal cntColTotal = BigDecimal.ZERO;
	BigDecimal amtColTotal = BigDecimal.ZERO;

	// 單一列 合計
	BigDecimal cntRowTotal = BigDecimal.ZERO;
	BigDecimal amtRowTotal = BigDecimal.ZERO;

	// 全工作月合計
	BigDecimal cntColAllTotal = BigDecimal.ZERO;
	BigDecimal amtColAllTotal = BigDecimal.ZERO;

	int dmm = 0;
	int cnt = 0;
	int amt = 0;
	int scol = 0;

	// 欄位文字 月分head
	String monthHead = "";

	// 起始列
	int sRow = 0;
	int tRow = 0;

	// 陣列大小
	int size = 0;

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LP002Report exec");
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LP002";
		String fileItem = "部室、區部、通訊處業績";
		String fileName = "LP002部室、區部、通訊處業績";
		String defaultExcel = "LP002_底稿_推展_部室、區部、通訊處業績.xlsx";
		String defaultSheet = "部室";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);


//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP002", "部室、區部、通訊處業績", "LP002部室、區部、通訊處業績",
//				"LP002_底稿_推展_部室、區部、通訊處業績.xlsx", "部室");

		List<Map<String, String>> wkSsnList = new ArrayList<>();

		try {
			// 找工作月
			wkSsnList = lP002ServiceImpl.wkSsn(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP002ServiceImpl.WorkSeason error = " + errors.toString());
		}

		exportExcel(titaVo, wkSsnList.get(0));

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	/**
	 * 執行Excel產表
	 * 
	 * @param titaVo
	 * @param wkSsnVo 工作年&月
	 * 
	 */
	private void exportExcel(TitaVo titaVo, Map<String, String> wkSsnVo) throws LogicException {
		// 民國年
		String rocYear = "";
		// 當前工作月
		int wkMonth = 0;
		// 工作月
//		monthHead = wkSsnVo.get("F1") == "1" ? "1" : "1~" + wkSsnVo.get("F1");

//		int iYEAR = Integer.parseInt(wkSsnVo.get("F0"));
//		int iMM = Integer.parseInt(wkSsnVo.get("F1"));

		//
		if (Integer.parseInt(wkSsnVo.get("F1")) == 1) {
			rocYear = String.valueOf(Integer.parseInt(wkSsnVo.get("F0")) - 1912);
			wkMonth = 13;
			monthHead = "13";
		} else {
			rocYear = String.valueOf(Integer.valueOf(wkSsnVo.get("F0")) - 1911);
			wkMonth = Integer.parseInt(wkSsnVo.get("F1"));
			monthHead = String.valueOf(Integer.parseInt(wkSsnVo.get("F1")));
		}

		// 起始欄位
		int col = 0;

		// 上季末 工作月
		int lastWkMonth = 0;

		// 陣列大小
		int pos = 6;

		// 區分上季末 工作月
		if (wkMonth <= 3) {

			lastWkMonth = 0;

		} else if (wkMonth <= 6) {

			lastWkMonth = 3;

		} else if (wkMonth <= 9) {

			lastWkMonth = 6;

		} else if (wkMonth <= 13) {

			lastWkMonth = 9;

		}

		cntArr = new BigDecimal[pos];

		amtArr = new BigDecimal[pos];

		/*--------------------------------------------------------------------------*/
		// 起始欄
		col = 4;

		makeExcel.setSheet("部室");
		makeExcel.setValue(1, 1, rocYear + "年第1~" + monthHead + "工作月部室業績統計");

		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);

		// 產出部室
		setDept(titaVo, wkSsnVo, wkMonth, lastWkMonth);

		/*--------------------------------------------------------------------------*/
		// 起始欄
		col = 5;

		makeExcel.setSheet("區部");
		makeExcel.setValue(1, 1, rocYear + "年第1~" + monthHead + "工作月區部業績統計");

		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);

		// 產出區部
		setDist(titaVo, wkSsnVo, wkMonth, lastWkMonth);

		/*--------------------------------------------------------------------------*/
		// 起始欄
		col = 4;

		// 單位代號
		String unit = "";

		makeExcel.setSheet("營管");
		makeExcel.setValue(1, 1, rocYear + "年第1~" + monthHead + "工作月營管部業績統計");

		// 單位代號
		unit = "A0B000";

		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);

		// 產出單位
		setUnit(titaVo, wkSsnVo, wkMonth, lastWkMonth, unit);
		/*--------------------------------------------------------------------------*/

		makeExcel.setSheet("營推");
		makeExcel.setValue(1, 1, rocYear + "年第1~" + monthHead + "工作月營推部業績統計");

		// 單位代號
		unit = "A0F000";

		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);

		// 產出單位
		setUnit(titaVo, wkSsnVo, wkMonth, lastWkMonth, unit);

		/*--------------------------------------------------------------------------*/

		makeExcel.setSheet("業推");
		makeExcel.setValue(1, 1, rocYear + "年第1~" + monthHead + "工作月業推部業績統計");

		// 單位代號
		unit = "A0E000";

		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);

		// 產出單位
		setUnit(titaVo, wkSsnVo, wkMonth, lastWkMonth, unit);

		/*--------------------------------------------------------------------------*/

		makeExcel.setSheet("業開");
		makeExcel.setValue(1, 1, rocYear + "年第1~" + monthHead + "工作月業開部業績統計");

		// 單位代號
		unit = "A0M000";

		// 建攔位
		setColTitle(col, wkMonth, lastWkMonth);

		// 產出單位
		setUnit(titaVo, wkSsnVo, wkMonth, lastWkMonth, unit);

	}

	/**
	 * 建立標頭
	 * 
	 * @param startCol    起始攔
	 * @param wkMonth     本工作月
	 * @param lastWkMonth 上季末工作月
	 */
	private void setColTitle(int startCol, int wkMonth, int lastWkMonth) throws LogicException {
		// 起始列
		int row2 = 2;
		int row3 = 3;

		// 是否建立前工作月累計 欄位
		boolean firstCreate = true;

		for (int i = 1; i <= wkMonth; i++) {

			// 只有 3工作月以前 不建累計欄位
			if (wkMonth <= 3) {

				// 設置當前攔寬度
				makeExcel.setWidth(startCol, 10);
				makeExcel.setWidth(startCol + 1, 15);

				makeExcel.setMergedRegionValue(row2, row2, startCol, startCol + 1, "第" + i + "工作月", "C");

				makeExcel.setValue(row3, startCol, "件數");

				makeExcel.setValue(row3, startCol + 1, "達成金額", "C");
				startCol += 2;

			} else {
				// 當 第i工作月 超過 上季末工作月 再開始建攔位
				if (i > lastWkMonth) {

					// 建立一次 累計欄位
					if (firstCreate) {
						// 設置當前攔寬度
						makeExcel.setWidth(startCol, 10);
						makeExcel.setWidth(startCol + 1, 15);

						makeExcel.setMergedRegionValue(row2, row2, startCol, startCol + 1,
								"第1~" + lastWkMonth + "工作月累計", "C");

						makeExcel.setValue(row3, startCol, "累計\n件數", "C");
						makeExcel.setValue(row3, startCol + 1, "累　　計\n達成金額", "C");

						firstCreate = false;
						startCol += 2;
					}

					// 設置當前攔寬度
					makeExcel.setWidth(startCol, 10);
					makeExcel.setWidth(startCol + 1, 15);

					makeExcel.setMergedRegionValue(row2, row2, startCol, startCol + 1, "第" + i + "工作月", "C");

					makeExcel.setValue(row3, startCol, "件數", "C");
					makeExcel.setValue(row3, startCol + 1, "達成金額", "C");
					startCol += 2;
				}

			}

		}

		// 設置當前攔寬度
		makeExcel.setWidth(startCol, 10);
		makeExcel.setWidth(startCol + 1, 15);

		makeExcel.setMergedRegionValue(row2, row2, startCol, startCol + 1, "第1~" + wkMonth + "工作月累計", "C");

		makeExcel.setValue(row3, startCol, "累計\n件數", "C");
		makeExcel.setValue(row3, startCol + 1, "累　　計\n達成金額", "C");

	}

	/**
	 * 產部室資料
	 * 
	 * @param titaVo
	 * @param wkSsnVo     工作年&月
	 * @param wkMonth     本工作月
	 * @param lastWkMonth 上季末工作月
	 * 
	 */
	private void setDept(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth, int lastWkMonth)
			throws LogicException {
		this.info("===========exportExcelDept");

		try {

			findList = lP002ServiceImpl.findDept(titaVo, wkSsnVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP002ServiceImpl.findDept error = " + errors.toString());
		}

		if (findList.size() > 0) {

			// 部室
			String deptcode = "";
			// 部室簡稱
			String sName = "";

			// 起始列
			sRow = 4;
			tRow = 3;

			// 起始欄
			scol = 4;

			// 陣列大小
			size = 0;
			// 陣列大小(暫存)
			int sizeT = 0;

			cntColTotal1 = BigDecimal.ZERO;
			amtColTotal1 = BigDecimal.ZERO;

			cntColTotal2 = BigDecimal.ZERO;
			amtColTotal2 = BigDecimal.ZERO;

			cntColTotal3 = BigDecimal.ZERO;
			amtColTotal3 = BigDecimal.ZERO;

			cntColTotal4 = BigDecimal.ZERO;
			amtColTotal4 = BigDecimal.ZERO;

			cntColTotal5 = BigDecimal.ZERO;
			amtColTotal5 = BigDecimal.ZERO;

			cntColAllTotal = BigDecimal.ZERO;
			amtColAllTotal = BigDecimal.ZERO;

			cntColTotal = BigDecimal.ZERO;
			amtColTotal = BigDecimal.ZERO;

			// 是否 產資料至 前工作月累計 欄位
			boolean firstCreate = true;

			for (Map<String, String> tLDVo : findList) {
//				this.info("list" + tLDVo);
				BigDecimal tmpCnt = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));
				BigDecimal tmpAmt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));

				if (!deptcode.equals(tLDVo.get("F6"))) {

					switch (tLDVo.get("F6")) {
					case "A0B000":
						sName = "營管部";
						break;
					case "A0F000":
						sName = "營推部";
						break;
					case "A0E000":
						sName = "業推部";
						break;
					case "A0M000":
						sName = "業開部";
						break;
					default:
						break;
					}
					// 部室代號
					deptcode = tLDVo.get("F6");
					// 部室
					makeExcel.setValue(sRow, 1, sName);
					// 主管
					makeExcel.setValue(sRow, 2, tLDVo.get("F1"));
					// 房貸部專
					makeExcel.setValue(sRow, 3, tLDVo.get("F2"));

					scol = 4;

					sRow++;
					tRow++;

					cntTotal = BigDecimal.ZERO;
					amtTotal = BigDecimal.ZERO;

					cntRowTotal = BigDecimal.ZERO;
					amtRowTotal = BigDecimal.ZERO;

					firstCreate = true;
				}

				// 月份
				dmm = Integer.valueOf(tLDVo.get("F3").substring(4, 6));

				this.info("dmm = " + dmm);

				if (wkMonth <= 3) {

					makeExcel.setValue(tRow, scol, tmpCnt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 1, tmpAmt, "#,##0", "R");

					// 累計+陸續工作月的 總計
					cntRowTotal = cntRowTotal.add(tmpCnt);
					amtRowTotal = amtRowTotal.add(tmpAmt);

					scol += 2;

				} else {
					// 只會加總到前一季工作月
					if (dmm <= lastWkMonth) {

						// 列 前工作月總計
						cntTotal = cntTotal.add(tmpCnt);
						amtTotal = amtTotal.add(tmpAmt);

						// 累計+陸續工作月的 總計
						cntRowTotal = cntRowTotal.add(tmpCnt);
						amtRowTotal = amtRowTotal.add(tmpAmt);

						// 累計的總計
						cntColTotal = cntColTotal.add(tmpCnt);
						amtColTotal = amtColTotal.add(tmpAmt);

					} else if (dmm > lastWkMonth) {
						// 當 第i工作月 超過 上季末工作月 再開始建攔位

						// 第一次 建立累計欄位
						if (firstCreate) {
							this.info("建立累計欄位, tRow = " + tRow + " , scol = " + scol + " ,cntTotal = " + cntTotal
									+ " ,amtTotal = " + amtTotal);

							makeExcel.setValue(tRow, scol, cntTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 1, amtTotal, "#,##0", "R");

							firstCreate = false;
							scol += 2;
						}

						// 累計+陸續工作月的 總計
						cntRowTotal = cntRowTotal.add(tmpCnt);
						amtRowTotal = amtRowTotal.add(tmpAmt);

						this.info("累計+陸續工作月的 總計, tRow = " + tRow + " , scol = " + scol + " ,tmpCnt = " + tmpCnt
								+ " ,tmpAmt = " + tmpAmt);

						makeExcel.setValue(tRow, scol, tmpCnt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 1, tmpAmt, "#,##0", "R");
						scol += 2;

					}

				}

				// 這沒有防，一直覆蓋同一位置 暫時
				makeExcel.setValue(tRow, scol, cntRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtRowTotal, "#,##0", "R");

				if (dmm > lastWkMonth) {

					sizeT = toWkMonth(dmm, tmpCnt, tmpAmt);

				}
				cntColAllTotal = cntColAllTotal.add(tmpCnt);
				amtColAllTotal = amtColAllTotal.add(tmpAmt);

			}
			tRow++;

			// 全總計
			cntArr[5] = cntColAllTotal;
			amtArr[5] = amtColAllTotal;

			scol = 4;

			// 最後一行合計
			int cj = 0;
			// 超過第3工作月 會插入累計
			if (wkMonth > 3) {

				makeExcel.setValue(tRow, scol, cntColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtColTotal, "#,##0", "R");
				scol += 2;
				cj = 1;

			}

			for (int j = cj; j < sizeT - 1; j++) {

				makeExcel.setValue(tRow, scol, cntArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtArr[j], "#,##0", "R");
				scol += 2;

			}

			makeExcel.setMergedRegionValue(tRow, tRow, 1, 3, "合計", "C");
			makeExcel.setValue(tRow, scol, cntColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 1, amtColAllTotal, "#,##0", "R");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 產區部資料
	 * 
	 * @param titaVo
	 * @param wkSsnVo     工作年&月
	 * @param wkMonth     本工作月
	 * @param lastWkMonth 上季末工作月
	 * 
	 */
	private void setDist(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth, int lastWkMonth)
			throws LogicException {
		this.info("===========exportExcelDist");

		try {

			findList = lP002ServiceImpl.findDist(titaVo, wkSsnVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP002ServiceImpl.findDist error = " + errors.toString());
		}

		if (findList.size() > 0) {

			// 區部
			String distcode = "";
			String sName = "";
			// 起始列
			sRow = 4;
			tRow = 3;

			// 起始欄
			scol = 54;

			// 陣列大小
			size = 0;
			// 陣列大小(暫存)
			int sizeT = 0;

			// 是否 產資料至 前工作月累計 欄位
			boolean firstCreate = true;

			cntColTotal1 = BigDecimal.ZERO;
			amtColTotal1 = BigDecimal.ZERO;

			cntColTotal2 = BigDecimal.ZERO;
			amtColTotal2 = BigDecimal.ZERO;

			cntColTotal3 = BigDecimal.ZERO;
			amtColTotal3 = BigDecimal.ZERO;

			cntColTotal4 = BigDecimal.ZERO;
			amtColTotal4 = BigDecimal.ZERO;

			cntColTotal5 = BigDecimal.ZERO;
			amtColTotal5 = BigDecimal.ZERO;

			cntColAllTotal = BigDecimal.ZERO;
			amtColAllTotal = BigDecimal.ZERO;

			cntColTotal = BigDecimal.ZERO;
			amtColTotal = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : findList) {
//				this.info("list" + tLDVo);

				BigDecimal tmpCnt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));
				BigDecimal tmpAmt = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F6"));

				if (!distcode.equals(tLDVo.get("F7"))) {

					switch (tLDVo.get("F0")) {
					case "A0B000":
					case "營業管理部":
						sName = "營管部";
						break;
					case "A0F000":
					case "營業推展部":
						sName = "營推部";
						break;
					case "A0E000":
					case "業務推展部":
						sName = "業推部";
						break;
					case "A0M000":
					case "營業開發部":
						sName = "業開部";
						break;
					default:
						break;
					}
					// 部室代號
					distcode = tLDVo.get("F7");
					// 部室
					makeExcel.setValue(sRow, 1, sName);
					// 部室
					makeExcel.setValue(sRow, 2, tLDVo.get("F1"));
					// 主管
					makeExcel.setValue(sRow, 3, tLDVo.get("F2"));
					// 房貸部專
					makeExcel.setValue(sRow, 4, tLDVo.get("F3"));

					scol = 5;
					sRow++;
					tRow++;

					cntTotal = BigDecimal.ZERO;
					amtTotal = BigDecimal.ZERO;

					cntRowTotal = BigDecimal.ZERO;
					amtRowTotal = BigDecimal.ZERO;

					firstCreate = true;
				}

				// 月份
				dmm = Integer.valueOf(tLDVo.get("F4").substring(4, 6));

				if (wkMonth <= 3) {

					makeExcel.setValue(tRow, scol, tmpCnt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 1, tmpAmt, "#,##0", "R");

					cntRowTotal = cntRowTotal.add(tmpCnt);
					amtRowTotal = amtRowTotal.add(tmpAmt);

					scol += 2;

				} else {
					// 只會加總到前一季工作月
					if (dmm <= lastWkMonth) {

						// 列 前工作月總計
						cntTotal = cntTotal.add(tmpCnt);
						amtTotal = amtTotal.add(tmpAmt);

						// 累計+陸續工作月的 總計
						cntRowTotal = cntRowTotal.add(tmpCnt);
						amtRowTotal = amtRowTotal.add(tmpAmt);

						// 累計的總計
						cntColTotal = cntColTotal.add(tmpCnt);
						amtColTotal = amtColTotal.add(tmpAmt);

					} else if (dmm > lastWkMonth) {
						// 當 第i工作月 超過 上季末工作月 再開始建攔位

						// 第一次 建立累計欄位
						if (firstCreate) {

//							this.info("-------------------------");
//							this.info(ccc+"cntTotal=" + cntTotal + "~tRow=" + tRow + "~scol=" + scol);
//							this.info(ccc+"amtTotal=" + amtTotal + "~tRow=" + tRow + "~scol=" + scol);

							makeExcel.setValue(tRow, scol, cntTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 1, amtTotal, "#,##0", "R");

							firstCreate = false;
							scol += 2;
						}

						// 累計+陸續工作月的 總計
						cntRowTotal = cntRowTotal.add(tmpCnt);
						amtRowTotal = amtRowTotal.add(tmpAmt);

//						this.info(ccc+"cntTotal=" + tmpCnt + "~tRow=" + tRow + "~scol=" + scol);
//						this.info(ccc+"amtTotal=" + tmpAmt + "~tRow=" + tRow + "~scol=" + scol);

						makeExcel.setValue(tRow, scol, tmpCnt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 1, tmpAmt, "#,##0", "R");
						scol += 2;

					}

				}

//				this.info(ccc+"cntTotal=" + cntRowTotal + "~tRow=" + tRow + "~scol=" + scol);
//				this.info(ccc+"amtTotal=" + amtRowTotal + "~tRow=" + tRow + "~scol=" + scol);

				// 這沒有防，一直覆蓋同一位置 暫時
				makeExcel.setValue(tRow, scol, cntRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtRowTotal, "#,##0", "R");

				if (dmm > lastWkMonth) {

					sizeT = toWkMonth(dmm, tmpCnt, tmpAmt);

				}
				cntColAllTotal = cntColAllTotal.add(tmpCnt);
				amtColAllTotal = amtColAllTotal.add(tmpAmt);

			}
			tRow++;

			// 全總計
			cntArr[5] = cntColAllTotal;
			amtArr[5] = amtColAllTotal;

			scol = 5;
			this.info("sizeT=" + sizeT);
			// 最後一行合計
			int cj = 0;
			// 超過第3工作月 會插入累計
			if (wkMonth > 3) {

				makeExcel.setValue(tRow, scol, cntColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtColTotal, "#,##0", "R");
				scol += 2;
				cj = 1;

			}

			for (int j = cj; j < sizeT - 1; j++) {
//				this.info(j + "cntArr=" + cntArr[j] + "~tRow=" + tRow + "~scol=" + scol);
//				this.info(j + "amtArr=" + amtArr[j] + "~tRow=" + tRow + "~scol=" + scol);
				makeExcel.setValue(tRow, scol, cntArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtArr[j], "#,##0", "R");
				scol += 2;

			}
//			this.info(5 + "cntArr=" + cntArr[5] + "~tRow=" + tRow + "~scol=" + scol);
//			this.info(5 + "amtArr=" + amtArr[5] + "~tRow=" + tRow + "~scol=" + scol);
			makeExcel.setMergedRegionValue(tRow, tRow, 1, 4, "合計", "C");
			makeExcel.setValue(tRow, scol, cntColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 1, amtColAllTotal, "#,##0", "R");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 產單位資料
	 * 
	 * @param titaVo
	 * @param wkSsnVo     工作年&月
	 * @param wkMonth     本工作月
	 * @param lastWkMonth 上季末工作月
	 * 
	 */
	private void setUnit(TitaVo titaVo, Map<String, String> wkSsnVo, int wkMonth, int lastWkMonth, String unitCode)
			throws LogicException {
		this.info("===========exportExcelUnit");

		try {

			findList = lP002ServiceImpl.findUnit(titaVo, wkSsnVo, unitCode);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP002ServiceImpl.findUnit error = " + errors.toString());
		}

		if (findList.size() > 0) {

			// 單位
			String unitcode = "";
			// 起始列
			sRow = 4;
			tRow = 3;

			// 起始欄
			scol = 4;

			// 陣列大小
			size = 0;
			// 陣列大小(暫存)
			int sizeT = 0;

			// 是否 產資料至 前工作月累計 欄位
			boolean firstCreate = true;

			cntColTotal1 = BigDecimal.ZERO;
			amtColTotal1 = BigDecimal.ZERO;

			cntColTotal2 = BigDecimal.ZERO;
			amtColTotal2 = BigDecimal.ZERO;

			cntColTotal3 = BigDecimal.ZERO;
			amtColTotal3 = BigDecimal.ZERO;

			cntColTotal4 = BigDecimal.ZERO;
			amtColTotal4 = BigDecimal.ZERO;

			cntColTotal5 = BigDecimal.ZERO;
			amtColTotal5 = BigDecimal.ZERO;

			cntColAllTotal = BigDecimal.ZERO;
			amtColAllTotal = BigDecimal.ZERO;

			cntColTotal = BigDecimal.ZERO;
			amtColTotal = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : findList) {
//				this.info("list" + tLDVo);
				BigDecimal tmpCnt = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));
				BigDecimal tmpAmt = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));

				if (!unitcode.equals(tLDVo.get("F7"))) {

					// 單位代號
					unitcode = tLDVo.get("F7");
					// 區部
					makeExcel.setValue(sRow, 1, tLDVo.get("F0"));
					// 單位
					makeExcel.setValue(sRow, 2, tLDVo.get("F1"));
					// 主管
					makeExcel.setValue(sRow, 3, tLDVo.get("F2"));
					scol = 4;
					sRow++;
					tRow++;

					cntTotal = BigDecimal.ZERO;
					amtTotal = BigDecimal.ZERO;

					cntRowTotal = BigDecimal.ZERO;
					amtRowTotal = BigDecimal.ZERO;

					firstCreate = true;
				}

				// 月份
				dmm = Integer.valueOf(tLDVo.get("F3").substring(4, 6));

				if (wkMonth <= 3) {

					makeExcel.setValue(tRow, scol, tmpCnt, "#,##0", "R");
					makeExcel.setValue(tRow, scol + 1, tmpAmt, "#,##0", "R");

					cntRowTotal = cntRowTotal.add(tmpCnt);
					amtRowTotal = amtRowTotal.add(tmpAmt);

					scol += 2;

				} else {
					// 只會加總到前一季工作月
					if (dmm <= lastWkMonth) {

						// 列 前工作月總計
						cntTotal = cntTotal.add(tmpCnt);
						amtTotal = amtTotal.add(tmpAmt);

						// 累計+陸續工作月的 總計
						cntRowTotal = cntRowTotal.add(tmpCnt);
						amtRowTotal = amtRowTotal.add(tmpAmt);

						// 累計的總計
						cntColTotal = cntColTotal.add(tmpCnt);
						amtColTotal = amtColTotal.add(tmpAmt);

					} else if (dmm > lastWkMonth) {
						// 當 第i工作月 超過 上季末工作月 再開始建攔位

						// 第一次 建立累計欄位
						if (firstCreate) {

							makeExcel.setValue(tRow, scol, cntTotal, "#,##0", "R");
							makeExcel.setValue(tRow, scol + 1, amtTotal, "#,##0", "R");

							firstCreate = false;
							scol += 2;
						}

						// 累計+陸續工作月的 總計
						cntRowTotal = cntRowTotal.add(tmpCnt);
						amtRowTotal = amtRowTotal.add(tmpAmt);

						makeExcel.setValue(tRow, scol, tmpCnt, "#,##0", "R");
						makeExcel.setValue(tRow, scol + 1, tmpAmt, "#,##0", "R");
						scol += 2;

					}

				}

				// 這沒有防，一直覆蓋同一位置 暫時
				makeExcel.setValue(tRow, scol, cntRowTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtRowTotal, "#,##0", "R");

				if (dmm > lastWkMonth) {

					sizeT = toWkMonth(dmm, tmpCnt, tmpAmt);

				}

				cntColAllTotal = cntColAllTotal.add(tmpCnt);
				amtColAllTotal = amtColAllTotal.add(tmpAmt);

			}
			tRow++;

			// 全總計
			cntArr[5] = cntColAllTotal;
			amtArr[5] = amtColAllTotal;

			scol = 4;

			// 最後一行合計
			int cj = 0;
			// 超過第3工作月 會插入累計
			if (wkMonth > 3) {

				makeExcel.setValue(tRow, scol, cntColTotal, "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtColTotal, "#,##0", "R");
				scol += 2;
				cj = 1;

			}

			for (int j = cj; j < sizeT - 1; j++) {

				makeExcel.setValue(tRow, scol, cntArr[j], "#,##0", "R");
				makeExcel.setValue(tRow, scol + 1, amtArr[j], "#,##0", "R");
				scol += 2;

			}

			makeExcel.setMergedRegionValue(tRow, tRow, 1, 3, "合計", "C");
			makeExcel.setValue(tRow, scol, cntColAllTotal, "#,##0", "R");
			makeExcel.setValue(tRow, scol + 1, amtColAllTotal, "#,##0", "R");

		} else {
			makeExcel.setValue(4, 1, "本日無資料");
		}
	}

	/**
	 * 各工作月合計
	 * 
	 * @param dmm 工作月
	 * @param cnt 件數
	 * @param amt 餘額
	 * 
	 */
	private int toWkMonth(int dmm, BigDecimal cnt, BigDecimal amt) {
		// 各工作月 加總
		switch (dmm) {
		case 1:
			cntColTotal1 = cntColTotal1.add(cnt);
			amtColTotal1 = amtColTotal1.add(amt);

			cntArr[0] = cntColTotal1;
			amtArr[0] = amtColTotal1;

			size = 2;
			break;
		case 2:
		case 4:
		case 7:
		case 10:
			cntColTotal2 = cntColTotal2.add(cnt);
			amtColTotal2 = amtColTotal2.add(amt);

			cntArr[1] = cntColTotal2;
			amtArr[1] = amtColTotal2;

			size = 3;
			break;
		case 3:
		case 5:
		case 8:
		case 11:
			cntColTotal3 = cntColTotal3.add(cnt);
			amtColTotal3 = amtColTotal3.add(amt);

			cntArr[2] = cntColTotal3;
			amtArr[2] = amtColTotal3;

			size = 4;
			break;

		case 6:
		case 9:
		case 12:
			cntColTotal4 = cntColTotal4.add(cnt);
			amtColTotal4 = amtColTotal4.add(amt);

			cntArr[3] = cntColTotal4;
			amtArr[3] = amtColTotal4;

			size = 5;
			break;
		case 13:
			cntColTotal5 = cntColTotal5.add(cnt);
			amtColTotal5 = amtColTotal5.add(amt);

			cntArr[4] = cntColTotal5;
			amtArr[4] = amtColTotal5;

			size = 6;
			break;
		default:
			break;
		}

		return size;
	}

}
