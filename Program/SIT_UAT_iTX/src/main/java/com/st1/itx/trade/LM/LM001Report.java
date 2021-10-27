package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM001ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")
public class LM001Report extends MakeReport {

	@Autowired
	LM001ServiceImpl lM001ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	DateUtil dateUtil;

	private final BigDecimal tenThousand = new BigDecimal("10000");// 萬
	int row = 3;
	
	int thisMonth = 0;
	int lastMonth = 0;
	BigDecimal highestRate = BigDecimal.ZERO;

	@Override
	public void printHeader() {

		this.print(-2, 112, "密等：密", "R");
		this.print(-3, 60, "新光人壽保險股份有限公司", "C");
		this.print(-4, 60, "辦理無自用住宅購買自用住宅放款戶數及金額統計表", "C");
		this.print(-5, 60, "中華民國 " + showRocDate(this.getReportDate(), 4), "C");
		this.print(-5, 112, "單位:戶、萬元", "R");

		this.setBeginRow(6);

		this.setMaxRows(100);

	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LM001Report exec start ...");

		this.setFontSize(11);

		this.setCharSpaces(0);
		
		int bsEntDy = titaVo.getEntDyI() + 19110000; // 西元帳務日

		dDateUtil.setDate_1(bsEntDy);
		dDateUtil.setMons(-1);

		int lastMonthEndDay = dDateUtil.getCalenderDay();

		this.info("LM001Report bsEntDy = " + bsEntDy);
		this.info("LM001Report lastMonthEndDay = " + lastMonthEndDay);

		thisMonth = bsEntDy / 100;
		lastMonth = lastMonthEndDay / 100;

		this.info("LM001Report thisMonth = " + thisMonth);
		this.info("LM001Report lastMonth = " + lastMonth);

		highestRate = BigDecimal.ZERO;

		makePdf(titaVo);

		makeExcel(titaVo);
	}

	private void printSheetHeader(String sheet) throws LogicException {

		// 調整欄寬
		makeExcel.setWidth(1, 20);
		makeExcel.setWidth(2, 20);
		makeExcel.setWidth(3, 20);
		makeExcel.setWidth(4, 30);
		makeExcel.setWidth(5, 30);
		makeExcel.setWidth(6, 20);
		makeExcel.setWidth(7, 30);

		ExcelFontStyleVo headerStyleVo = new ExcelFontStyleVo();
		headerStyleVo.setBold(true);

		// 第1列
		makeExcel.setValue(1, 1, showBcDate(this.getReportDate(), 1), "R", headerStyleVo);
		makeExcel.setValue(1, 2, dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6), "R",
				headerStyleVo);
		makeExcel.setValue(1, 3, "LM001", "L", headerStyleVo);
		makeExcel.setValue(1, 4, sheet, "L", headerStyleVo);

	}

	private void makePdf(TitaVo titaVo) throws LogicException
	{
		this.open(titaVo
				, titaVo.getEntDyI()
				, titaVo.getKinbr()
				, "LM001"
				, "公會無自用住宅統計"
				, ""
				, "A4"
				, "P");

		List<Map<String, String>> listLM001 = lM001ServiceImpl.doQuery(thisMonth, lastMonth, titaVo);

		// 表一
		if (listLM001 != null && listLM001.size() > 0) {

			Map<String, String> tLM001 = listLM001.get(0);

			BigDecimal thisMonthCnt = getBigDecimal(tLM001.get("F0")); // 專案運用-期末有效戶數
			BigDecimal thisMonthLoanBalTotal = getBigDecimal(tLM001.get("F1")); // 專案運用-期末有效金額
			BigDecimal lastMonthCnt = getBigDecimal(tLM001.get("F2")); // 專案運用-期初有效戶數
			BigDecimal lastMonthLoanBalTotal = getBigDecimal(tLM001.get("F3")); // 專案運用-期初有效金額
			BigDecimal thisMonthAddCnt = getBigDecimal(tLM001.get("F4")); // 專案運用-本期增加戶數
			BigDecimal thisMonthAddLoanBalTotal = getBigDecimal(tLM001.get("F5")); // 專案運用-本期增加有效金額

			// 期末、期初、本期增加 先取到萬元
			thisMonthLoanBalTotal = thisMonthLoanBalTotal.divide(tenThousand, 0, BigDecimal.ROUND_HALF_UP);
			lastMonthLoanBalTotal = lastMonthLoanBalTotal.divide(tenThousand, 0, BigDecimal.ROUND_DOWN);
			thisMonthAddLoanBalTotal = thisMonthAddLoanBalTotal.divide(tenThousand, 0, BigDecimal.ROUND_DOWN);

			BigDecimal thisMonthLessCnt = BigDecimal.ZERO; // 專案運用-本期減少戶數
			BigDecimal thisMonthLessLoanBalTotal = BigDecimal.ZERO; // 專案運用-本期減少有效金額

			BigDecimal thisMonthNetCnt = thisMonthCnt.subtract(lastMonthCnt); // 專案運用-本期淨增減戶數
			BigDecimal thisMonthNetLoanBalTotal = thisMonthLoanBalTotal.subtract(lastMonthLoanBalTotal); // 專案運用-本期淨增減有效金額

			// "期末金額" < "期初金額"+"本期增加" 時, 計算 "本期減少" 金額
			if (thisMonthLoanBalTotal.compareTo(lastMonthLoanBalTotal.add(thisMonthAddLoanBalTotal)) < 0) {

				thisMonthLessCnt = lastMonthCnt.add(thisMonthAddCnt).subtract(thisMonthCnt);

				thisMonthLessLoanBalTotal = lastMonthLoanBalTotal.add(thisMonthAddLoanBalTotal).subtract(thisMonthLoanBalTotal);
			}

			print(1, 1, "　　　　╔════════╦═════════════╦═════════════╦═════════════╗");
			print(1, 1, "　　　　║　　　　　　　　║　　　　專案運用　　　　　║　　　　非專案運用　　　　║　　　　　小計　　　　　　║");
			print(1, 1, "　　　　║　　　　　　　　╠══════╦══════╬══════╦══════╬══════╦══════╣");
			print(1, 1, "　　　　║　　項　　　目　║　　戶數　　║　　金額　　║　　戶數　　║　　金額　　║　　戶數　　║　　金額　　║");
			print(1, 1, "　　　　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　　　　║　　期初有效　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　　　　║　　　《1》　　 ║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 期初
			print(0, 40, lastMonthCnt.toString(), "R");
			print(0, 54, formatAmt(lastMonthLoanBalTotal, 0), "R");

			print(0, 68, "0", "R");
			print(0, 82, "0", "R");

			print(0, 96, lastMonthCnt.toString(), "R");
			print(0, 110, formatAmt(lastMonthLoanBalTotal, 0), "R");

			print(1, 1, "　　　　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　　　　║　　本期增加　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　　　　║　　　《2》　　 ║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 本期增加
			print(0, 40, thisMonthAddCnt.toString(), "R");
			print(0, 54, formatAmt(thisMonthAddLoanBalTotal, 0), "R");

			print(0, 68, "0", "R");
			print(0, 82, "0", "R");

			print(0, 96, thisMonthAddCnt.toString(), "R");
			print(0, 110, formatAmt(thisMonthAddLoanBalTotal, 0), "R");

			print(1, 1, "　　　　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　　　　║　　本期減少　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　　　　║　　　《3》　　 ║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 本期減少
			print(0, 40, thisMonthLessCnt.toString(), "R");
			print(0, 54, formatAmt(thisMonthLessLoanBalTotal, 0), "R");

			print(0, 68, "0", "R");
			print(0, 82, "0", "R");

			print(0, 96, thisMonthLessCnt.toString(), "R");
			print(0, 110, formatAmt(thisMonthLessLoanBalTotal, 0), "R");

			print(1, 1, "　　　　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　　　　║　本期淨增(減)　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　　　　║　(4)=(2)-(3)　 ║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 本期淨增減
			print(0, 40, thisMonthNetCnt.toString(), "R");
			print(0, 54, formatAmt(thisMonthNetLoanBalTotal, 0), "R");

			print(0, 68, "0", "R");
			print(0, 82, "0", "R");

			print(0, 96, thisMonthNetCnt.toString(), "R");
			print(0, 110, formatAmt(thisMonthNetLoanBalTotal, 0), "R");

			print(1, 1, "　　　　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　　　　║　　期末有效　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　　　　║　(5)=(1)+(4)　 ║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 期末
			print(0, 40, thisMonthCnt.toString(), "R");
			print(0, 54, formatAmt(thisMonthLoanBalTotal, 0), "R");

			print(0, 68, "0", "R");
			print(0, 82, "0", "R");

			print(0, 96, thisMonthCnt.toString(), "R");
			print(0, 110, formatAmt(thisMonthLoanBalTotal, 0), "R");

			print(1, 1, "　　　　╚════════╩══════╩══════╩══════╩══════╩══════╩══════╝");

		} else {
			// 若表一無資料
		}

		List<Map<String, String>> listLM001Rate = lM001ServiceImpl.doQueryRate(thisMonth, titaVo);

		print(1, 10, "表二：");
		print(1, 60, "新光人壽保險股份有限公司", "C");
		print(1, 60, "辦理無自用住宅購買自用住宅放款利率表", "C");
		print(1, 1, "　　　　╔════════════════════════╦═════════════════════════╗");
		print(1, 1, "　　　　║　　　　　　　　　首期放款利率　　　　　　　　　║　　　　　　　　　　續期放款利率　　　　　　　　　║");
		print(1, 1, "　　　　╠═══════════╦════════════╬════════════╦════════════╣");
		print(1, 1, "　　　　║　　　　最　　低　　　║　　　　最　　高　　　　║　　　　最　　低　　　　║　　　　最　　高　　　　║");
		print(1, 1, "　　　　╠═══════════╬════════════╬════════════╬════════════╣");
		print(1, 1, "　　　　║　　　　　　　　　　　║　　　　　　　　　　　　║　　　　　　　　　　　　║　　　　　　　　　　　　║");
		// 表二
		if (listLM001Rate != null && !listLM001Rate.isEmpty()) {

			Map<String, String> tLM001Rate = listLM001Rate.get(0);

			BigDecimal firstRateMinimum = getBigDecimal(tLM001Rate.get("F0"));

			if (firstRateMinimum.compareTo(BigDecimal.ZERO) > 0) {
				print(0, 25, formatAmt(firstRateMinimum, 2) + "%", "R");
			} else {
				print(0, 25, "---", "R");
			}

			BigDecimal firstRateMaximum = getBigDecimal(tLM001Rate.get("F1"));

			if (firstRateMaximum.compareTo(BigDecimal.ZERO) > 0) {
				print(0, 49, formatAmt(firstRateMaximum, 2) + "%", "R");
			} else {
				print(0, 49, "---", "R");
			}

			BigDecimal continueRateMinimum = getBigDecimal(tLM001Rate.get("F2"));

			if (continueRateMinimum.compareTo(BigDecimal.ZERO) > 0) {
				print(0, 75, formatAmt(continueRateMinimum, 2) + "%", "R");
			} else {
				print(0, 75, "---", "R");
			}

			BigDecimal continueRateMaximum = getBigDecimal(tLM001Rate.get("F3"));

			if (continueRateMaximum.compareTo(BigDecimal.ZERO) > 0) {
				print(0, 101, formatAmt(continueRateMaximum, 2) + "%", "R");
				highestRate = continueRateMaximum;
			} else {
				print(0, 101, "---", "R");
			}

		} else {
			// 表二出空表加在這裡
			print(0, 25, "---", "R");
			print(0, 49, "---", "R");
			print(0, 75, "---", "R");
			print(0, 101, "---", "R");
		}

		print(1, 1, "　　　　╚═══════════╩════════════╩════════════╩════════════╝");
		print(1, 1, "　　　　 說明：");
		print(1, 1, "　　　　 一、依據財政部88.6.28台財保第882409201號函核定辦理");
		print(1, 1, "　　　　 二、首期放款利率：指本月份內〔月初至月底〕新核准之放款戶數所採用之放款利率");
		print(1, 1, "　　　　        ，依其最低級最高放款利率填報。");
		print(1, 1, "　　　　 三、續期放款利率：指本月份內〔月初至月底〕依契約規定調整有效放款戶數所採用");
		print(1, 1, "　　　　     之放款利率，依其最高及最低放款利率填報。");

		long sno = this.close();

		this.toPdf(sno);
	}

	private void makeExcel(TitaVo titaVo) throws LogicException
	{
		// 印3份Query
				makeExcel.open(titaVo
						, titaVo.getEntDyI()
						, titaVo.getKinbr()
						, "LM001"
						, "公會報送無自用住宅統計"
						, "LM001-公會報送無自用住宅統計");

				// 新撥款之戶號
				makeExcel.setSheet("LM001", "新撥款之戶號");
				List<Map<String, String>> newCaseList = null;
				try {
					newCaseList = lM001ServiceImpl.findNewCase(thisMonth, titaVo);
					printSheetHeader("新撥款之戶號");
					makeExcel.setValue(2, 2, "利率");
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error("lM008ServiceImpl.findNewCase error = " + e.getMessage());
				}
				if (newCaseList != null && !newCaseList.isEmpty()) {
					for (Map<String, String> detail : newCaseList) {
						makeExcel.setValue(3, 1, detail.get("F0"));
					}
				} else {
					makeExcel.setValue(3, 1, "無資料");
				}

				// 續期放款利率 最低、最高
				makeExcel.newSheet("續期放款利率　最低、最高");
				List<Map<String, String>> minMaxRate = null;
				try {
					minMaxRate = lM001ServiceImpl.findMinMaxRate(thisMonth, titaVo);
					printSheetHeader("續期放款利率　最低、最高");
					makeExcel.setValue(2, 2, "利率");
					makeExcel.setValue(3, 1, "FINAL");
					makeExcel.setValue(3, 2, "TOTALS");
					makeExcel.setValue(4, 1, "MIN");
					makeExcel.setValue(5, 1, "MAX");
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error("lM008ServiceImpl.findMinMaxRate error = " + e.getMessage());
				}
				if (minMaxRate != null && minMaxRate.size() != 0) {
					for (Map<String, String> detail : minMaxRate) {
						makeExcel.setValue(4, 2, detail.get("F0"));
						makeExcel.setValue(5, 2, detail.get("F1"));
					}
				} else {
					makeExcel.setValue(6, 1, "無資料");
				}

				// 利率超過最高利率之借戶
				makeExcel.newSheet("利率超過" + formatAmt(highestRate, 2) + "%之借戶");
				List<Map<String, String>> higherRateList = null;
				try {
					higherRateList = lM001ServiceImpl.findHigherRate(thisMonth, highestRate, titaVo);
					printSheetHeader("利率超過" + formatAmt(highestRate, 2) + "%之借戶");
					makeExcel.setValue(2, 1, "戶號", "C");
					makeExcel.setValue(2, 2, "額度", "C");
					makeExcel.setValue(2, 3, "撥款", "C");
					makeExcel.setValue(2, 4, "撥款日期", "C");
					makeExcel.setValue(2, 5, "撥款金額", "C");
					makeExcel.setValue(2, 6, "利率", "C");
					makeExcel.setValue(2, 7, "繳息迄日", "C");
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error("lM008ServiceImpl.findHigherRate error = " + e.getMessage());
				}
				if (higherRateList != null && !higherRateList.isEmpty()) {
					for (Map<String, String> detail : higherRateList) {
						makeExcel.setValue(row, 1, detail.get("F0"), "R");
						makeExcel.setValue(row, 2, detail.get("F1"), "R");
						makeExcel.setValue(row, 3, detail.get("F2"), "R");
						makeExcel.setValue(row, 4, detail.get("F3"), "R");
						makeExcel.setValue(row, 5, detail.get("F4"), "R");
						makeExcel.setValue(row, 6, detail.get("F5"), "R");
						makeExcel.setValue(row, 7, detail.get("F6"), "R");
						row++;
					}
				} else {
					makeExcel.setValue(3, 1, "無資料");
				}
				long sno1 = makeExcel.close();
				makeExcel.toExcel(sno1);
	}
}
