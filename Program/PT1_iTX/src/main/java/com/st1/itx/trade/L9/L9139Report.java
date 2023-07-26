package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9139ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class L9139Report extends MakeReport {

	@Autowired
	L9139ServiceImpl oL9139ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;
	
	@Autowired
	public DateUtil dateUtil;

	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> oL9139List = new ArrayList<>();

		int startDate = parse.stringToInteger(titaVo.getParam("StartDate")) + 19110000;
		
		//輸入日期非營業時間
		dateUtil.init();
		dateUtil.setDate_2(startDate);
		if (dateUtil.isHoliDay()) {
			throw new LogicException(titaVo, "E0001", "輸入日期非營業時間");			
		}

		try {
			oL9139List = oL9139ServiceImpl.findAll(titaVo, startDate);
		} catch (Exception e) {
			this.info("L9139ServiceImpl.findAll error = " + e.toString());
			return false;
		}
		setExcel(titaVo, oL9139List);
		return true;
	}

	private void setExcel(TitaVo titaVo, List<Map<String, String>> oL9139List) throws LogicException {

		int reportDate = titaVo.getEntDyI() + 19110000;
		int startDate = parse.stringToInteger(titaVo.getParam("StartDate")) + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9139";
		String fileItem = "暫收款日餘額前後差異比較表";
		String fileName = "L9139暫收款日餘額前後差異比較表";
		String defaultExcel = "L9139_底稿_暫收款日餘額前後差異比較表.xlsx";
		String defaultSheet = "暫收款日餘額前後差異比較表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		this.info("oL9139List.size() = " + oL9139List.size());

		int row = 3; // 起始資料

		// 小計
		BigDecimal subTdCkBal = BigDecimal.ZERO; // 今日暫收支票
		BigDecimal subTdAvBal = BigDecimal.ZERO; // 今日暫收非支票
		BigDecimal subYdCkBal = BigDecimal.ZERO; // 昨日暫收支票
		BigDecimal subYdAvBal = BigDecimal.ZERO; // 昨日暫收非支票
		// 總計
		BigDecimal totalTdCkBal = BigDecimal.ZERO;
		BigDecimal totalTdAvBal = BigDecimal.ZERO;
		BigDecimal totalYdCkBal = BigDecimal.ZERO;
		BigDecimal totalYdAvBal = BigDecimal.ZERO;

		if (oL9139List != null && oL9139List.size() != 0) {

			for (Map<String, String> map : oL9139List) {

				String value = "";
				BigDecimal bValue = getBigDecimal(0);
				int col = 0;

				for (int i = 0; i <= 6; i++) {

					value = map.get("F" + i);

					if (value == null) {
						value = "";
					}

					col++;
					switch (i) {
					case 0: // 會計備份日期
						makeExcel.setValue(row, col, value);
						break;
					case 1: // 戶號
						makeExcel.setValue(row, col, value);
						break;
					case 2: // 戶名
						makeExcel.setValue(row, col, value);
						break;
					case 3: // 今日暫收支票
						bValue = getBigDecimal(value);
						makeExcel.setValue(row, col, bValue, "#,##0");
						subTdCkBal = subTdCkBal.add(bValue);
						totalTdCkBal = totalTdCkBal.add(bValue);
						break;
					case 4: // 今日暫收非支票
						bValue = getBigDecimal(value);
						makeExcel.setValue(row, col, bValue, "#,##0");
						subTdAvBal = subTdAvBal.add(bValue);
						totalTdAvBal = totalTdAvBal.add(bValue);
						break;
					case 5: // 昨日暫收支票
						bValue = getBigDecimal(value);
						makeExcel.setValue(row, col, bValue, "#,##0");
						subYdCkBal = subYdCkBal.add(bValue);
						totalYdCkBal = totalYdCkBal.add(bValue);
						break;
					case 6: // 昨日暫收非支票
						bValue = getBigDecimal(value);
						makeExcel.setValue(row, col, bValue, "#,##0");
						subYdAvBal = subYdAvBal.add(bValue);
						totalYdAvBal = totalYdAvBal.add(bValue);
						break;
					default:
						break;
					}
				} // for
				row++;
			} // for
			if (subTdCkBal.compareTo(BigDecimal.ZERO) > 0 || subTdAvBal.compareTo(BigDecimal.ZERO) > 0
					|| subYdCkBal.compareTo(BigDecimal.ZERO) > 0 || subYdAvBal.compareTo(BigDecimal.ZERO) > 0) {
				makeExcel.setValue(row, 1, "小計");
				makeExcel.setValue(row, 4, subTdCkBal, "#,##0");
				makeExcel.setValue(row, 5, subTdAvBal, "#,##0");
				makeExcel.setValue(row, 6, subYdCkBal, "#,##0");
				makeExcel.setValue(row, 7, subYdAvBal, "#,##0");
				row++;
				row++;
			}
			
			if (totalTdCkBal.compareTo(BigDecimal.ZERO) > 0 || totalTdAvBal.compareTo(BigDecimal.ZERO) > 0
					|| totalYdCkBal.compareTo(BigDecimal.ZERO) > 0 || totalYdAvBal.compareTo(BigDecimal.ZERO) > 0) {
				makeExcel.setValue(row, 1, "總計");
				makeExcel.setValue(row, 4, totalTdCkBal, "#,##0");
				makeExcel.setValue(row, 5, totalTdAvBal, "#,##0");
				makeExcel.setValue(row, 6, totalYdCkBal, "#,##0");
				makeExcel.setValue(row, 7, totalYdAvBal, "#,##0");
				row++;
			}
			makeExcel.setValue(1, 2, startDate);

		} else {
			makeExcel.setValue(1, 2, "本日無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	public List<Integer> getDateDifferenceList(int number1, int number2) {
		// 將數字轉換為日期
		LocalDate date1 = convertToDate(number1);
		LocalDate date2 = convertToDate(number2);

		// 計算日期之間的差異
		int difference = date2.compareTo(date1);

		// 構建日期差異表
		List<Integer> dateDiffList = new ArrayList<>();
		for (int i = 0; i <= difference; i++) {
			LocalDate currentDate = date1.plusDays(i);
			int currentNumber = convertToNumber(currentDate);
			dateDiffList.add(currentNumber);
		}

		return dateDiffList;
	}

	public LocalDate convertToDate(int number) {
		String dateString = String.valueOf(number);
		int year = Integer.parseInt(dateString.substring(0, 4));
		int month = Integer.parseInt(dateString.substring(4, 6));
		int day = Integer.parseInt(dateString.substring(6, 8));
		return LocalDate.of(year, month, day);
	}

	public int convertToNumber(LocalDate date) {
		String dateString = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		return Integer.parseInt(dateString);
	}

}
