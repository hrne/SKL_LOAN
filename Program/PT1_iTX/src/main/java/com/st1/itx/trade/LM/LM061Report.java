package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.st1.itx.db.service.springjpa.cm.LM061ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")
public class LM061Report extends MakeReport {

	@Autowired
	LM061ServiceImpl lM061ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	List<Map<String, Object>> mergeList = new ArrayList<Map<String, Object>>();
	Map<String, Object> mergeMap = null;
	int countAmt = 1;

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth    西元年月
	 * @param yearMonthEnd 月底日
	 */
	public void exec(TitaVo titaVo, int yearMonth, int yearMonthEnd) throws LogicException {

		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM061Report exec");

		int iYear = (yearMonth - 191100) / 100;
		int iMonth = (yearMonth - 191100) % 100;
		int iDay = (yearMonthEnd - 19110000) % 100;
//		int iYYYMM = yearMonth - 191100;

		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM061";
		String fileItem = "逾清償期二年案件追蹤控管表";
		String fileName = "LM061_逾清償期二年案件追蹤控管表";
		String defaultExcel = "LM061-逾清償期二年案件追蹤控管表.xlsx";
		String defaultSheet = "1080430";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM061", "逾清償期二年案件追蹤控管表", "LM061_逾清償期二年案件追蹤控管表",
//				"LM061-逾清償期二年案件追蹤控管表.xlsx", "1080430");

		String iENTDY = titaVo.get("ENTDY");

		makeExcel.setSheet("1080430", iENTDY.substring(1, 8));

		makeExcel.setValue(1, 23, "機密等級："+this.getSecurity()+"\n單位：元\n" + iYear + "." + iMonth + "." + iDay + "止");

		try {
			fnAllList = lM061ServiceImpl.findAll(titaVo,yearMonth,yearMonthEnd);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM061ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {

			String tempCustNo = "";

			// 暫存 轉催收金額
//			BigDecimal tempOvduBal = BigDecimal.ZERO;

			// 鑑價金額
			BigDecimal F11 = BigDecimal.ZERO;

			// 暫存 鑑價金額
//			BigDecimal tempF11 = BigDecimal.ZERO;

			// 同戶號多額度 筆數
//			int tempCount = 0;
//			int tempCount2 = 0;

			// 從第三列開始塞值
			int row = 3;

			// 序列
			int num = 0;

			// 筆數
			int count = 0;

			for (Map<String, String> tLDVo : fnAllList) {

				// 筆數
				count++;

				// 第二筆資料起,先將下一列以下的資料向下搬移一列
				if (row > 3) {
					makeExcel.setShiftRow(row, 1);
				}

				// 上一筆與這一筆戶號不同 序列加一
				if (!tempCustNo.equals(tLDVo.get("F0"))) {
					num++;
				}

				// 欄位：序列
				makeExcel.setValue(row, 2, num);

				// 轉催收金額
				BigDecimal ovduBal = tLDVo.get("F3") == null || tLDVo.get("F3").isEmpty() ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));

				this.info("tLDVo:" + tLDVo);

				// 戶號
//				makeExcel.setValue(row, 3,
//						tLDVo.get("F0").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F0")), "R");

				// 額度
				makeExcel.setValue(row, 4,
						tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1")), "R");

				// 戶名
//				makeExcel.setValue(row, 5, tLDVo.get("F2"), "R");

				// 核貸金額
				makeExcel.setValue(row, 6,
						tLDVo.get("F3").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3")), "$* #,##0", "R");

				// 轉催收本息
				makeExcel.setValue(row, 7,
						tLDVo.get("F4").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F4")), "$* #,##0", "R");

				// 催收款餘額
				makeExcel.setValue(row, 8,
						tLDVo.get("F5").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F5")), "$* #,##0", "R");

				// 繳息迄日
				makeExcel.setValue(row, 9,
						tLDVo.get("F6").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F6")), "R");

				// 利率
				makeExcel.setValue(row, 10,
						tLDVo.get("F7").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F7")), "0.0000", "R");

				// 到期日
				makeExcel.setValue(row, 11,
						tLDVo.get("F8").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F8")), "R");
				// 轉催收日
				makeExcel.setValue(row, 12,
						tLDVo.get("F9").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F9")), "R");

				// 轉呆金額
				makeExcel.setValue(row, 17,
						tLDVo.get("F13").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F13")), "$* #,##0",
						"R");

				// 擔保品坐落
				makeExcel.setValue(row, 20, tLDVo.get("F14"), "R");
				// 符合規範
//				makeExcel.setValue(row, 21, tLDVo.get(""), "R");
				// 催收人員
				makeExcel.setValue(row, 22, tLDVo.get("F16"), "R");

				// 法務進度(F10)
				makeExcel.setValue(row, 15, tLDVo.get("F10"), "L");

				// 金額(F11)
				// 法務進度代號(F12)

				// 鑑價金額
				F11 = tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11"));

				// 代號 56 拍定金額 58 分配金額
				if (!tLDVo.get("F12").equals("056")) {
					F11 = BigDecimal.ZERO;
				}

				// 20211025Test

				// 代號 56 拍定金額 58 分配金額 (含戶號 戶名)
				checkMergeRegionValue(tLDVo.get("F0").toString(), tLDVo.get("F2").toString(), F11, ovduBal, row);

				// 代號 77 協議達成
				if (tLDVo.get("F12").equals("077")) {
					makeExcel.setValue(row, 18, " ", "C");
					makeExcel.setValue(row, 19,
							tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11")), "$* #,##0",
							"R");
				}

				// 代號 901 拍定不足額
				if (tLDVo.get("F12").equals("901")) {
					makeExcel.setValue(row, 16,
							tLDVo.get("F11").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F11")), "$* #,##0",
							"R");
				}

				row++;
			}
		} else {
			makeExcel.setValue(3, 3, "本日無資料");
		}

		BigDecimal tempAmt = null;
		BigDecimal tempLTV = null;
		int sRow = 0;
		int eRow = 0;

		this.info("mergeList=" + mergeList.toString());

		for (Map<String, Object> mergeData : mergeList) {

			tempAmt = new BigDecimal(mergeData.get("amt").toString());
			tempLTV = new BigDecimal(mergeData.get("ltv").toString());
			eRow = Integer.valueOf(mergeData.get("eRow").toString());
			sRow = eRow - Integer.valueOf(mergeData.get("count").toString()) + 1;

			this.info("merge=" + sRow + "," + eRow);

			if (Integer.valueOf(mergeData.get("count").toString()) > 1) {

				// 戶號
				makeExcel.setMergedRegionValue(sRow, eRow, 3, 3, Integer.valueOf(mergeData.get("cust").toString()));
				// 戶名
				makeExcel.setMergedRegionValue(sRow, eRow, 5, 5, mergeData.get("name").toString());
				makeExcel.setMergedRegionValue(sRow, eRow, 13, 13, tempAmt, "#,##0");
				makeExcel.setMergedRegionValue(sRow, eRow, 14, 14, tempLTV, "##0.00%");

			} else {

				// 戶號
				makeExcel.setValue(sRow, 3, Integer.valueOf(mergeData.get("cust").toString()));
				// 戶名
				makeExcel.setValue(sRow, 5, mergeData.get("name").toString());
				makeExcel.setValue(sRow, 13, tempAmt, "#,##0");
				makeExcel.setValue(sRow, 14, tempLTV, "##0.00%");

			}

		}

		makeExcel.close();
		// makeExcel.toExcel(sno);
	}

	private void checkMergeRegionValue(String custNo, String custName, BigDecimal eAmt, BigDecimal ovduAmt, int row) {

		String tempCustNo = "";
		BigDecimal ltvPercent = BigDecimal.ZERO;

		mergeMap = new HashMap<String, Object>();

		if (mergeList.size() > 0) {

			int as = mergeList.size() - 1;

			BigDecimal tempAmt = new BigDecimal(mergeList.get(as).get("amt").toString());
			BigDecimal tempOvduAmt = new BigDecimal(mergeList.get(as).get("ovduamt").toString());

			tempCustNo = mergeList.get(as).get("cust").toString();

			// 與前一筆 戶號額度是否一樣
			if (tempCustNo.equals(custNo)) {

				countAmt++;

				ltvPercent = this.computeDivide(tempOvduAmt, tempAmt, 4);

				mergeMap.put("count", countAmt);
				mergeMap.put("cust", custNo);
				mergeMap.put("ovduamt", ovduAmt);
				mergeMap.put("ltv", ltvPercent);
				mergeMap.put("eRow", row);
				mergeMap.put("name", custName);

				if (tempAmt.compareTo(eAmt) == 0) {

					mergeMap.put("amt", eAmt);

				} else if (tempAmt.compareTo(eAmt) == -1) {

					mergeMap.put("amt", eAmt);

				} else {

					mergeMap.put("amt", tempAmt);
				}

				mergeList.set(as, mergeMap);

			} else {

				countAmt = 1;

				mergeMap.put("count", countAmt);
				mergeMap.put("cust", custNo);
				mergeMap.put("amt", eAmt);
				mergeMap.put("ovduamt", ovduAmt);
				mergeMap.put("ltv", ltvPercent);
				mergeMap.put("eRow", row);
				mergeMap.put("name", custName);

				mergeList.add(mergeMap);

			}

		} else {

			countAmt = 1;

			mergeMap.put("count", countAmt);
			mergeMap.put("cust", custNo);
			mergeMap.put("amt", eAmt);
			mergeMap.put("ovduamt", ovduAmt);
			mergeMap.put("ltv", ltvPercent);
			mergeMap.put("eRow", row);
			mergeMap.put("name", custName);

			mergeList.add(mergeMap);

		}

	}
}
