package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM067ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM067Report extends MakeReport {

	@Autowired
	LM067ServiceImpl lm067ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {
	}

	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {

		this.info("LM067Report exec");

		// 取得會計日(同頁面上會計日)
		// 年月日
//		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = yearMonth / 100;
		// 月
		int iMonth = yearMonth % 100;

		String iYearMonth = String.valueOf(((iYear - 1911) * 100) + iMonth);

		this.info("yymm=" + iYearMonth);

		String txCD = "LM067";
		String itemName = "土地覆審";

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txCD, "06-土地追蹤-" + iYearMonth, txCD + "_06-土地追蹤-" + iYearMonth, "LM067_底稿_土地追蹤.xls", "簡表");

		// 設定欄寬
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 7);
		makeExcel.setWidth(4, 28);
		makeExcel.setWidth(5, 13);
		makeExcel.setWidth(6, 10);
		makeExcel.setWidth(7, 10);
		makeExcel.setWidth(8, 20);
		makeExcel.setWidth(9, 20);
		makeExcel.setWidth(10, 20);
		makeExcel.setWidth(11, 20);
		makeExcel.setWidth(12, 20);
		makeExcel.setWidth(13, 15);
		makeExcel.setWidth(14, 15);
		makeExcel.setWidth(15, 15);
		makeExcel.setWidth(16, 50);

		// 粗體
		makeExcel.setIBU("B");

		// 機密等級及基準日期
		makeExcel.setValue(1, 16, "機密等級：機密\n" + (iYear - 1911) + "." + String.format("%02d", iMonth), "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();
		List<Map<String, String>> fnAllList2 = new ArrayList<>();

		try {

			fnAllList = lm067ServiceImpl.findAll(titaVo, yearMonth);
			fnAllList2 = lm067ServiceImpl.findList(titaVo, yearMonth, 6);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM067ServiceImpl.findAll error = " + errors.toString());

		}

		// 資料比數是否大於0筆
		if (fnAllList.size() > 0) {

			// 初始列
			int row = 2;
			String tempNo = "";
			String fdnm = "";

			// 總計
			BigDecimal tot = new BigDecimal("0");

			// 撥款日期
			int day = 0;
			int lday = 0;

			String date = "";
			for (Map<String, String> tLDVo : fnAllList) {
//				this.info("tLDVo-------->" + tLDVo.toString());

				row++;

				for (int col = 0; col < tLDVo.size(); col++) {

					fdnm = "F" + String.valueOf(col);
					// 實際EXCEL 欄位分配
					int i = col + 2;
					switch (i) {

					case 2:
						// B欄 戶號(數字右靠)
						if (!tempNo.equals(tLDVo.get(fdnm))) {
							tempNo = tLDVo.get(fdnm);
							makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO : Integer.valueOf(tLDVo.get(fdnm)), "R");
						} else {
							makeExcel.setValue(row, i, "", "R");
						}

						break;
					case 3:
						// C欄 額度
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO : Integer.valueOf(tLDVo.get(fdnm)), "C");
						break;
					case 4:
						// D欄 戶名
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 5:
						// E欄 客戶別
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					case 6:
						// F欄 用途別
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");

						break;

					case 7:
						// G欄位 地區別
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");

						break;

					case 8:
						// H欄 覆審月份
						day = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? 0 : Integer.valueOf(tLDVo.get(fdnm));
						day = day % 100;

						makeExcel.setValue(row, i, day, "C");

						break;
					case 9:
						// I欄 撥款日期
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO : Integer.parseInt(tLDVo.get(fdnm)), "R");

						// 與前一筆是否相同
						if (!date.equals(tLDVo.get(fdnm))) {
							date = tLDVo.get(fdnm);
							if (lday == 0) {
								// 第一次賦值
								lday = Integer.parseInt(tLDVo.get(fdnm));
							} else {
								if (lday > Integer.parseInt(tLDVo.get(fdnm))) {
									lday = Integer.parseInt(tLDVo.get(fdnm));
								}
							}
						}
						break;
					case 10:
						// J欄 放款餘額
						BigDecimal f10 = tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm));
						makeExcel.setValue(row, i, f10, "#,##0", "R");
						// 所有金額總計

						tot = tot.add(f10);

						break;

					case 11:
						// K欄 全戶餘額
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(fdnm)), "#,##0", "R");
						break;
					case 12:
						// L欄 展期記號
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;

					case 13:
						// M欄 是否追蹤
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 14:
						// N欄 應覆審單位
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "C");
						break;
					case 15:
						// O欄 評等
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 || tLDVo.get(fdnm).equals("0") ? "" : tLDVo.get(fdnm), "C");
						break;
					case 16:
						// P欄 1備註
						makeExcel.setValue(row, i, tLDVo.get(fdnm) == null || tLDVo.get(fdnm).length() == 0 ? "" : tLDVo.get(fdnm), "L");
						break;
					default:

						break;
					}
				}
			}

			makeExcel.setValue(row + 1, 2, "總計", "L");
			makeExcel.setValue(row + 1, 9, lday, "R");
			makeExcel.setValue(row + 1, 10, tot, "#,##0", "R");

		} else {

			makeExcel.setValue(3, 2, "本日無資料");
		}

		dataList(fnAllList2, itemName);

		makeExcel.close();

	}

	public void dataList(List<Map<String, String>> list, String sheetName) throws LogicException {

		makeExcel.setSheet(sheetName);

		int row = 2;

		for (Map<String, String> r : list) {

//			int conditionCode = Integer.valueOf(r.get("F0"));
			int custNo = Integer.valueOf(r.get("F1"));
			int facmNo = Integer.valueOf(r.get("F2"));
			int bormNo = Integer.valueOf(r.get("F3"));
			String custName = r.get("F4");
			int drawdownDate = Integer.valueOf(r.get("F5"));
			BigDecimal lineAmt = new BigDecimal(r.get("F6"));
			BigDecimal utilAmt = new BigDecimal(r.get("F7"));
//			BigDecimal cif = new BigDecimal(r.get("F8"));
			String provider = r.get("F9");
			int seq = Integer.valueOf(r.get("F10"));
			int landNo1 = Integer.valueOf(r.get("F11"));
			int landNo2 = Integer.valueOf(r.get("F12"));
			BigDecimal area = new BigDecimal(r.get("F13"));
			int custTypeCode = Integer.valueOf(r.get("F14"));
			String custTypeName = r.get("F15");
			int usageCode = Integer.valueOf(r.get("F16"));
			String usageName = r.get("F17");
			int cityCode = Integer.valueOf(r.get("F18"));
			String cityName = r.get("F19");
			int rechYM = Integer.valueOf(r.get("F20"));
			String remark = r.get("F21");

			// 戶號
			makeExcel.setValue(row, 1, custNo, "#######", "R");
			// 額度
			makeExcel.setValue(row, 2, facmNo, "R");
			// 撥款
			makeExcel.setValue(row, 3, bormNo, "R");
			// 戶名
			makeExcel.setValue(row, 4, custName, "L");
			// 撥款日期
			makeExcel.setValue(row, 5, drawdownDate, "", "R");
			// 核准額度
			makeExcel.setValue(row, 6, lineAmt, "#,##0", "R");
			// 貸出金額
			makeExcel.setValue(row, 7, utilAmt, "R");
			// 提供人CIF
//			makeExcel.setValue(row,8,"","R");
			// 提供人名稱
			makeExcel.setValue(row, 9, provider, "L");
			// 序號
			makeExcel.setValue(row, 10, seq, "R");
			// 地號1
			makeExcel.setValue(row, 11, landNo1, "R");
			// 地號2
			makeExcel.setValue(row, 12, landNo2, "R");
			// 面積
			makeExcel.setValue(row, 13, area, "##.#0", "R");
			// 客戶別
			makeExcel.setValue(row, 14, custTypeCode, "L");
			// 客戶別中文
			makeExcel.setValue(row, 15, custTypeName, "L");
			// 用途別
			makeExcel.setValue(row, 16, usageCode, "L");
			// 用途別中文
			makeExcel.setValue(row, 17, usageName, "L");
			// 地區別
			makeExcel.setValue(row, 18, cityCode, "R");
			// 地區別名稱
			makeExcel.setValue(row, 19, cityName, "L");
			// 覆審月份
			makeExcel.setValue(row, 20, rechYM, "R");
			// 資料說明(備註)
			makeExcel.setValue(row, 21, remark, "L");

			row++;
		}

	}
}
