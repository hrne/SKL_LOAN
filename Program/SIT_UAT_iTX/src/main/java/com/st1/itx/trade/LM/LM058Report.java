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
import com.st1.itx.db.service.springjpa.cm.LM058ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM058Report extends MakeReport {

	@Autowired
	LM058ServiceImpl lm058ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param nowDate          今天日期(民國)
	 * @param thisMonthEndDate 當月底日期(民國)
	 * 
	 */
	public void exec(TitaVo titaVo, int nowDate, int thisMonthEndDate) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM058Report exec");

		// 民國年
		int iYear = nowDate / 10000;
		// 月
		int iMonth = (nowDate / 100) % 100;

		// 當民國年月
		int thisYM = 0;

		// 判斷帳務日與月底日是否同一天
		if (nowDate < thisMonthEndDate) {
			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
		}

		// 11103
		thisYM = (iYear + 1911) * 100 + iMonth;
		// 1110331
		String dateRocYMD = String.valueOf(thisMonthEndDate);
		this.info("thisYM=" + thisYM);
		this.info("dateRocYMD=" + dateRocYMD);

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM058", "表A19_會計部申報表",
				"LM058_表A19_會計部申報表_" + dateRocYMD.substring(0, 5), "LM058_底稿_表A19_會計部申報表.xlsx", "108.04");
		makeExcel.setSheet("108.04", dateRocYMD.substring(0, 3) + "." + dateRocYMD.substring(3, 5));

		try {
			fnAllList = lm058ServiceImpl.findAll(titaVo, thisYM);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM058ServiceImpl.findAll error = " + errors.toString());
		}

		// 民國年月日
		String date = "民國" + dateRocYMD.substring(0, 3).replaceFirst("^0", "") + "年"
				+ dateRocYMD.substring(3, 5).replaceFirst("^0", "") + "月"
				+ dateRocYMD.substring(5, 7).replaceFirst("^0", "") + "日";

		makeExcel.setValue(2, 2, date);

		if (fnAllList.size() > 0) {
			String fdnm = "";

			// 起始列(不含)
			int row = 5;

			Double total = 0.0;
			Double maxTotal = 0.0;
			for (Map<String, String> tLDVo : fnAllList) {

				row++;

				for (int i = 0; i < tLDVo.size(); i++) {
					fdnm = "F" + String.valueOf(i);
					switch (i) {
					// 戶號
					case 0:

						if (tLDVo.get(fdnm).equals("")) {
							makeExcel.setValue(row, i + 1, 0);
						} else {
							makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)));
						}
						break;

					// 戶名
					case 1:
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "L");
						break;

					// 身分證/統編
					case 2:
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "L");
						break;

					// 利害關係情形/公司名稱(字串)
					case 3:
					case 5:
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "C");
						break;

					// 公司別/交易項目代號(數字)
					case 4:
					case 6:
						makeExcel.setValue(row, i + 1, Integer.valueOf(tLDVo.get(fdnm)), "C");
						break;

					case 7:// 當月最高餘額累計(百萬單位)
						makeExcel.setValue(row, i + 1, cpRate(tLDVo.get(fdnm)), "#,##0", "R");
						maxTotal += cpRate(tLDVo.get(fdnm));
						break;
					case 8:// 帳列餘額(百萬單位)
						makeExcel.setValue(row, i + 1, cpRate(tLDVo.get(fdnm)), "#,##0", "R");
						// 當月(最新)餘額累計
						total += cpRate(tLDVo.get(fdnm));
						break;
					case 9:// 較上月增減(百萬單位)
						makeExcel.setValue(row, i + 1, cpRate(tLDVo.get(fdnm)), "#,##0", "R");

						break;
					case 10:
						// 金額
						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "#,##0", "R");

						break;

					case 11:

						makeExcel.setValue(row, i + 1, tLDVo.get(fdnm), "C");
						break;
					default:

						break;
					}
				}

			}
			// 當月最高餘額累計
			makeExcel.setValue(row + 1, 8, maxTotal, "#,###");

			// 當月(最新)餘額累計
			makeExcel.setValue(row + 1, 9, total, "#,###");

		} else {
			makeExcel.setValue(6, 1, "本日無資料");
		}

		makeExcel.close();
		// makeExcel.toExcel(sno);
	}

	/**
	 * 金額單位轉換(百萬單位)
	 * 
	 * @param iamt 金額
	 */
	private double cpRate(String iamt) {
		if (iamt == null || iamt.equals("") || iamt.equals("0")) {
			return 0.00;
		} else {
			BigDecimal gal = new BigDecimal("1000000");
			BigDecimal amt = new BigDecimal(iamt);

			amt = amt.divide(gal, 6, 1);
			return amt.doubleValue();
		}
	}
}
