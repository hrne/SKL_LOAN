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
import com.st1.itx.util.common.data.ReportVo;
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

//	@Override
//	public void printHeader() {
//
//		this.print(-2, 97, "密等：密", "R");
//		this.print(-3, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
//		this.print(-4, this.getMidXAxis(), "辦理無自用住宅購買自用住宅放款戶數及金額統計表", "C");
//		this.print(-5, this.getMidXAxis(), "中華民國 " + showRocDate(this.getReportDate(), 4), "C");
//		this.print(-5, 97, "單位:戶、萬元", "R");
//
//		this.setBeginRow(6);
//
//		this.setMaxRows(100);
//
//	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LM001Report exec start ...");

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
		

		// Excel格式
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM001";
		String fileItem = "公會報送無自用住宅統計";
		String fileName = "LM001-公會報送無自用住宅統計";
		String defaultExcel = "LM001_底稿_公會報送無自用住宅統計.xlsx";
		String defaultSheet = "YYYMM";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		makeExcel.setSheet("YYYMM", String.valueOf(titaVo.getEntDyI()/100));
	

		makeExcel(titaVo);
	}

	private void makeExcel(TitaVo titaVo) throws LogicException {
		int reportDate = titaVo.getEntDyI() + 19110000;
		List<Map<String, String>> lM001List = null;
		try {
			lM001List = lM001ServiceImpl.doQuery(thisMonth, lastMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM001ServiceImpl.findNewCase error = " + e.getMessage());
		}

		
		
		makeExcel.setValue(3, 1, "中華民國 " + showRocDate(reportDate, 4)+"              單位：戶、萬元 ", "R");
		if (lM001List != null && !lM001List.isEmpty()) {
			Map<String, String> tLM001 = lM001List.get(0);
			
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

				thisMonthLessLoanBalTotal = lastMonthLoanBalTotal.add(thisMonthAddLoanBalTotal)
						.subtract(thisMonthLoanBalTotal);
			}
			// 期初
			makeExcel.setValue(7, 3, lastMonthCnt.toString(), "R");
			makeExcel.setValue(7, 4, formatAmt(lastMonthLoanBalTotal, 0), "R");
			makeExcel.setValue(7, 5, "0", "R");
			makeExcel.setValue(7, 6, "0", "R");
			makeExcel.setValue(7, 7, lastMonthCnt.toString(), "R");
			makeExcel.setValue(7, 8, formatAmt(lastMonthLoanBalTotal, 0), "R");

			// 本期增加
			makeExcel.setValue(9, 3, thisMonthAddCnt.toString(), "R");
			makeExcel.setValue(9, 4, formatAmt(thisMonthAddLoanBalTotal, 0), "R");
			makeExcel.setValue(9, 5, "0", "R");
			makeExcel.setValue(9, 6, "0", "R");
			makeExcel.setValue(9, 7, thisMonthAddCnt.toString(), "R");
			makeExcel.setValue(9, 8, formatAmt(thisMonthAddLoanBalTotal, 0), "R");

			// 本期減少
			makeExcel.setValue(11, 3, thisMonthLessCnt.toString(), "R");
			makeExcel.setValue(11, 4, formatAmt(thisMonthLessLoanBalTotal, 0), "R");
			makeExcel.setValue(11, 5, "0", "R");
			makeExcel.setValue(11, 6, "0", "R");
			makeExcel.setValue(11, 7, thisMonthLessCnt.toString(), "R");
			makeExcel.setValue(11, 8, formatAmt(thisMonthLessLoanBalTotal, 0), "R");

			// 本期淨增減
			makeExcel.setValue(13, 3, thisMonthNetCnt.toString(), "R");
			makeExcel.setValue(13, 4, formatAmt(thisMonthNetLoanBalTotal, 0), "R");
			makeExcel.setValue(13, 5, "0", "R");
			makeExcel.setValue(13, 6, "0", "R");
			makeExcel.setValue(13, 7, thisMonthNetCnt.toString(), "R");
			makeExcel.setValue(13, 8, formatAmt(thisMonthNetLoanBalTotal, 0), "R");

			// 期末
			makeExcel.setValue(15, 3, thisMonthCnt.toString(), "R");
			makeExcel.setValue(15, 4, formatAmt(thisMonthLoanBalTotal, 0), "R");
			makeExcel.setValue(15, 5, "0", "R");
			makeExcel.setValue(15, 6, "0", "R");
			makeExcel.setValue(15, 7, thisMonthCnt.toString(), "R");
			makeExcel.setValue(15, 8, formatAmt(thisMonthLoanBalTotal, 0), "R");
			
			List<Map<String, String>> listLM001Rate = null;
			try {
				listLM001Rate = lM001ServiceImpl.doQueryRate(thisMonth, titaVo);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error("lM001ServiceImpl.findNewCase error = " + e.getMessage());
			}
			// 表二
			if (listLM001Rate != null && !listLM001Rate.isEmpty()) {

				Map<String, String> tLM001Rate = listLM001Rate.get(0);

				BigDecimal firstRateMinimum = getBigDecimal(tLM001Rate.get("F0"));

				if (firstRateMinimum.compareTo(BigDecimal.ZERO) > 0) {					
					makeExcel.setValue(21, 1,  formatAmt(firstRateMinimum, 2)+"%", "R");
				} else {
					makeExcel.setValue(21, 1,  "---", "R");
					
				}

				BigDecimal firstRateMaximum = getBigDecimal(tLM001Rate.get("F1"));

				if (firstRateMaximum.compareTo(BigDecimal.ZERO) > 0) {					
					makeExcel.setValue(21, 3,  formatAmt(firstRateMaximum, 2)+"%", "R");
				} else {					
					makeExcel.setValue(21, 3,  "---", "R");
				}

				BigDecimal continueRateMinimum = getBigDecimal(tLM001Rate.get("F2"));

				if (continueRateMinimum.compareTo(BigDecimal.ZERO) > 0) {					
					makeExcel.setValue(21, 5,  formatAmt(continueRateMinimum, 2)+"%", "R");
				} else {					
					makeExcel.setValue(21, 5,  "---", "R");
				}

				BigDecimal continueRateMaximum = getBigDecimal(tLM001Rate.get("F3"));

				if (continueRateMaximum.compareTo(BigDecimal.ZERO) > 0) {					
					makeExcel.setValue(21, 7,  formatAmt(continueRateMaximum, 2)+"%", "R");
					highestRate = continueRateMaximum;
				} else {					
					makeExcel.setValue(21, 7,  "---", "R");
				}

			} else {
				// 表二出空表加在這裡
				makeExcel.setValue(21, 1,  "---", "R");
				makeExcel.setValue(21, 3,  "---", "R");
				makeExcel.setValue(21, 5,  "---", "R");
				makeExcel.setValue(21, 7,  "---", "R");
				
			}
			

		} else {
			makeExcel.setValue(7, 3, "無資料");
		}

		makeExcel.close();
	}

	private void makePdf(TitaVo titaVo) throws LogicException {
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM001", "公會無自用住宅統計", "", "A4", "P");
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM001";
		String reportItem = "公會無自用住宅統計";
		String security = "";
		String pageSize = "A4";
		String pageOrientation = "P";
		
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(reportItem).setSecurity(security).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();
		this.open(titaVo, reportVo);
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

				thisMonthLessLoanBalTotal = lastMonthLoanBalTotal.add(thisMonthAddLoanBalTotal)
						.subtract(thisMonthLoanBalTotal);
			}

			print(1, 1, "　╔════════╦═════════════╦═════════════╦═════════════╗");
			print(1, 1, "　║　　　　　　　　║　　　　專案運用　　　　　║　　　　非專案運用　　　　║　　　　　小計　　　　　　║");
			print(1, 1, "　║　　　　　　　　╠══════╦══════╬══════╦══════╬══════╦══════╣");
			print(1, 1, "　║　　項　　　目　║　　戶數　　║　　金額　　║　　戶數　　║　　金額　　║　　戶數　　║　　金額　　║");
			print(1, 1, "　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　║　　期初有效　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　║　　　《1》　　 ║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 期初
			print(0, 32, lastMonthCnt.toString(), "R");
			print(0, 45, formatAmt(lastMonthLoanBalTotal, 0), "R");

			print(0, 58, "0", "R");
			print(0, 71, "0", "R");

			print(0, 84, lastMonthCnt.toString(), "R");
			print(0, 96, formatAmt(lastMonthLoanBalTotal, 0), "R");

			print(1, 1, "　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　║　　本期增加　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　║　　　《2》　　 ║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 本期增加
			print(0, 32, thisMonthAddCnt.toString(), "R");
			print(0, 45, formatAmt(thisMonthAddLoanBalTotal, 0), "R");

			print(0, 58, "0", "R");
			print(0, 71, "0", "R");

			print(0, 84, thisMonthAddCnt.toString(), "R");
			print(0, 96, formatAmt(thisMonthAddLoanBalTotal, 0), "R");

			print(1, 1, "　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　║　　本期減少　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　║　　　《3》　　 ║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 本期減少
			print(0, 32, thisMonthLessCnt.toString(), "R");
			print(0, 45, formatAmt(thisMonthLessLoanBalTotal, 0), "R");

			print(0, 58, "0", "R");
			print(0, 71, "0", "R");

			print(0, 84, thisMonthLessCnt.toString(), "R");
			print(0, 96, formatAmt(thisMonthLessLoanBalTotal, 0), "R");

			print(1, 1, "　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　║　本期淨增(減)　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　║　(4)=(2)-(3)　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 本期淨增減
			print(0, 32, thisMonthNetCnt.toString(), "R");
			print(0, 45, formatAmt(thisMonthNetLoanBalTotal, 0), "R");

			print(0, 58, "0", "R");
			print(0, 71, "0", "R");

			print(0, 84, thisMonthNetCnt.toString(), "R");
			print(0, 96, formatAmt(thisMonthNetLoanBalTotal, 0), "R");

			print(1, 1, "　╠════════╬══════╬══════╬══════╬══════╬══════╬══════╣");
			print(1, 1, "　║　　期末有效　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");
			print(1, 1, "　║　(5)=(1)+(4)　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║　　　　　　║");

			// 期末
			print(0, 32, thisMonthCnt.toString(), "R");
			print(0, 45, formatAmt(thisMonthLoanBalTotal, 0), "R");

			print(0, 58, "0", "R");
			print(0, 71, "0", "R");

			print(0, 84, thisMonthCnt.toString(), "R");
			print(0, 96, formatAmt(thisMonthLoanBalTotal, 0), "R");

			print(1, 1, "　╚════════╩══════╩══════╩══════╩══════╩══════╩══════╝");

		} else {
			// 若表一無資料
		}

		List<Map<String, String>> listLM001Rate = lM001ServiceImpl.doQueryRate(thisMonth, titaVo);

		print(1, 7, "表二：");
		print(1, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		print(1, this.getMidXAxis(), "辦理無自用住宅購買自用住宅放款利率表", "C");
		print(1, 1, "　╔════════════════════════╦═════════════════════════╗");
		print(1, 1, "　║　　　　　　　　　首期放款利率　　　　　　　　　║　　　　　　　　　　續期放款利率　　　　　　　　　║");
		print(1, 1, "　╠═══════════╦════════════╬════════════╦════════════╣");
		print(1, 1, "　║　　　　最　　低　　　║　　　　最　　高　　　　║　　　　最　　低　　　　║　　　　最　　高　　　　║");
		print(1, 1, "　╠═══════════╬════════════╬════════════╬════════════╣");
		print(1, 1, "　║　　　　　　　　　　　║　　　　　　　　　　　　║　　　　　　　　　　　　║　　　　　　　　　　　　║");
		// 表二
		if (listLM001Rate != null && !listLM001Rate.isEmpty()) {

			Map<String, String> tLM001Rate = listLM001Rate.get(0);

			BigDecimal firstRateMinimum = getBigDecimal(tLM001Rate.get("F0"));

			if (firstRateMinimum.compareTo(BigDecimal.ZERO) > 0) {
				print(0, 22, formatAmt(firstRateMinimum, 2) + "%", "R");
			} else {
				print(0, 22, "---", "R");
			}

			BigDecimal firstRateMaximum = getBigDecimal(tLM001Rate.get("F1"));

			if (firstRateMaximum.compareTo(BigDecimal.ZERO) > 0) {
				print(0, 46, formatAmt(firstRateMaximum, 2) + "%", "R");
			} else {
				print(0, 46, "---", "R");
			}

			BigDecimal continueRateMinimum = getBigDecimal(tLM001Rate.get("F2"));

			if (continueRateMinimum.compareTo(BigDecimal.ZERO) > 0) {
				print(0, 72, formatAmt(continueRateMinimum, 2) + "%", "R");
			} else {
				print(0, 72, "---", "R");
			}

			BigDecimal continueRateMaximum = getBigDecimal(tLM001Rate.get("F3"));

			if (continueRateMaximum.compareTo(BigDecimal.ZERO) > 0) {
				print(0, 96, formatAmt(continueRateMaximum, 2) + "%", "R");
				highestRate = continueRateMaximum;
			} else {
				print(0, 96, "---", "R");
			}

		} else {
			// 表二出空表加在這裡
			print(0, 22, "---", "R");
			print(0, 46, "---", "R");
			print(0, 72, "---", "R");
			print(0, 96, "---", "R");
		}

		print(1, 1, "　╚═══════════╩════════════╩════════════╩════════════╝");
		print(1, 1, "　　說明：");
		print(1, 1, "　　一、依據財政部88.6.28台財保第882409201號函核定辦理");
		print(1, 1, "　　二、首期放款利率：指本月份內〔月初至月底〕新核准之放款戶數所採用之放款利率");
		print(1, 1, "　　       ，依其最低級最高放款利率填報。");
		print(1, 1, "　　三、續期放款利率：指本月份內〔月初至月底〕依契約規定調整有效放款戶數所採用");
		print(1, 1, "　　　   之放款利率，依其最高及最低放款利率填報。");

		this.close();
		// this.toPdf(sno);
	}

}
