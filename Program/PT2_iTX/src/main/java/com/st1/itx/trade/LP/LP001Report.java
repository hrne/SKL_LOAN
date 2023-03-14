package com.st1.itx.trade.LP;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LP001ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LP001Report extends MakeReport {

	@Autowired
	LP001ServiceImpl lP001ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	DateUtil dateUtil;

	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		this.print(-2, 145, "機密等級：" + this.getSecurity());
		this.print(-3, 2, "程式ID：" + this.getParentTranCode());
		this.print(-3, 80, "新光人壽保險股份有限公司", "C");
		this.print(-4, 2, "報  表：" + this.getRptCode());
		this.print(-4, 80, "工作月放款審查課各區業績累計", "C");
		this.print(-3, 145, "日　　期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));
		this.print(-4, 145, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-5, 145, "頁　　次：" + this.getNowPage());
		String yearMon = this.showRocDate(this.getReportDate());
		this.print(-6, 80, yearMon, "C");
		this.print(-7, 145, "單　　位：元");

		this.setBeginRow(8);
//		this.setMaxRows(35);
		this.setCharSpaces(0);
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LP001Report exec");

		List<Map<String, String>> wkSsnList = new ArrayList<>();

		try {
			wkSsnList = lP001ServiceImpl.wkSsn(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP001ServiceImpl.wkSsn error = " + errors.toString());
		}

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {
			if (wkSsnList != null && !wkSsnList.isEmpty()) {
				fnAllList = lP001ServiceImpl.findAll(titaVo, wkSsnList.get(0));
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LP001ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, wkSsnList.get(0), fnAllList);

		exportReport(titaVo, wkSsnList.get(0), fnAllList);

	}

	private String reportCode = "LP001";
	private String reportItem = "工作月放款審查課各區業績累計";

	private String pageSize = "A4";
	private String pageOrientation = "L";

	private void exportReport(TitaVo titaVo, Map<String, String> wkVo, List<Map<String, String>> fnAllList)
			throws LogicException {
		this.info("===========in PDF");

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
				.setRptCode(reportCode).setRptItem(reportItem).setRptSize(pageSize)
				.setPageOrientation(pageOrientation).build();

		this.open(titaVo, reportVo);

//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), reportCode, reportItem, security, pageSize,
//				pageOrientation);

		this.setCharSpaces(0);

		int iYEAR = 0;
		int iMM = 0;

		// 以當月工作月 推回上一工作月
		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1"));
		}

		// 民國年
		String rocYear = String.valueOf(iYEAR - 1911);
		// 民國年月
//		String yymm = rocYear + String.format("%02d", iMM);
		// 月份
		String mm = String.valueOf(iMM);

		// 標題
		String title = "第1~" + mm;

		// 畫表格(上&下)
		drawTable(0);
		drawTable(240);

		// 北區經理名字
		String northMan = "";
		// 中區經理名字
		String centralMan = "";
		// 南區經理名字
		String southMan = "";

		// 北區件數
		double[] northCnt = new double[13];
		// 中區件數
		double[] centralCnt = new double[13];
		// 南區件數
		double[] southCnt = new double[13];
		// 全區件數
		double[] totalCnt = new double[13];

		// 北區金額
		int[] northAmt = new int[13];
		// 中區金額
		int[] centralAmt = new int[13];
		// 南區金額
		int[] southAmt = new int[13];
		// 全區金額
		int[] totalAmt = new int[13];

		// 工作月位置
		int position = 0;

		// 區域 經理 控制上下表格
		int cn = 0;
		int cc = 0;
		int cs = 0;

		// 區域 經理 標題 行數
		int deInt = 4;
		int maInt = 10;
		// 區域 經理 值 行數
		int deVal = 4;
		int maVal = 10;

		// 區域 經理 起始列
		int row = 0;

		//
//		文字格式化成數字樣式
		DecimalFormat dnb = new DecimalFormat("#,##0.0");
		DecimalFormat inb = new DecimalFormat("#,##0");

		// 重新排序至陣列
		for (Map<String, String> tLDVo : fnAllList) {
			// 用工作月識別欄位位置
			position = Integer.parseInt(tLDVo.get("F1").substring(4, 6)) - 1;

			// 區域中心
			switch (tLDVo.get("F4")) {
			case "10HC00":
				if (cn == 0) {
					row = -14;
					this.print(row, deVal, "北區", "C");
					this.print(row, maVal, northMan.isEmpty() ? tLDVo.get("F0") : northMan, "C");
					row = -34;
					this.print(row, deVal, "北區", "C");
					this.print(row, maVal, northMan.isEmpty() ? tLDVo.get("F0") : northMan, "C");
					cn++;
				}

				// 北區 各X工作月 件數 及 金額 累加
				northCnt[position] += Double.parseDouble(tLDVo.get("F2"));
				northAmt[position] += Integer.parseInt(tLDVo.get("F3"));

				break;
			case "10HJ00":
				if (cc == 0) {
					row = -18;
					this.print(row, deVal, "中區", "C");
					this.print(row, maVal, centralMan.isEmpty() ? tLDVo.get("F0") : centralMan, "C");
					row = -38;
					this.print(row, deVal, "中區", "C");
					this.print(row, maVal, centralMan.isEmpty() ? tLDVo.get("F0") : centralMan, "C");
					cc++;
				}

				// 中區 各X工作月 件數 及 金額 累加
				centralCnt[position] += Double.parseDouble(tLDVo.get("F2"));
				centralAmt[position] += Integer.parseInt(tLDVo.get("F3"));

				break;
			case "10HL00":
				if (cs == 0) {
					row = -22;
					this.print(row, deVal, "南區", "C");
					this.print(row, maVal, southMan.isEmpty() ? tLDVo.get("F0") : southMan, "C");
					row = -42;
					this.print(row, deVal, "南區", "C");
					this.print(row, maVal, southMan.isEmpty() ? tLDVo.get("F0") : southMan, "C");
					cs++;
				}

				// 南區 各X工作月 件數 及 金額 累加
				southCnt[position] += Double.parseDouble(tLDVo.get("F2"));
				southAmt[position] += Integer.parseInt(tLDVo.get("F3"));

				break;
			default:
				break;
			}

			// 全區 件數 及 金額 累加
			totalCnt[position] += Double.parseDouble(tLDVo.get("F2"));
			totalAmt[position] += Integer.parseInt(tLDVo.get("F3"));

		}

		this.print(1, 1, "");
		// 放款區域中心業績累計明細表--> 放款審查課各區業績累計明細表
		this.print(-7, 2, rocYear + "年" + title + "工作月 放款審查課各區業績累計");
		// 工作月 起始列
		int t1 = -9;
		// 區域 經理 起始列
		int t2 = -10;
		// 房貸 房貸 起始列
		int t3 = -11;
		// 件數 撥款金額 起始列
		int t4 = -12;

		// 北部 起始列
		int tn = -14;

		// 中部 起始列
		int tc = -18;

		// 南部 起始列
		int ts = -22;

		// 合計 起始列
		int tt = -25;

		// 房貸件數 房貸撥款金額 起始欄(靠右)
		int colCnt = 20;
		int colAmt = 35;

		// 房貸件數 房貸撥款金額 標題 起始欄(置中)
		int colTitleCnt = 17;
		int colTitleAmt = 28;

		// 工作月 起始欄
		int colmonth = 24;

		this.setCharSpaces(0);

		// tab=0 上表格 tab =20 下表格
		for (int tab = 0; tab / 20 < 2; tab += 20) {

			// 月份兼位置
			for (int m = 0; m < 7; m++) {

				// 間距
				int startColumn = 22;

				// 上表格為m+1 下表格為m+8
				int month = tab == 20 ? (m + 8) : (m + 1);

				// 為下表格並且是全工作月欄位
				if (tab == 20 && m == 6) {

					this.print(t1 - tab, colmonth + startColumn * m, title + "工作月", "C");

					// 北區 全工作月 總計 件數及金額
					this.print(tn - tab, colCnt + startColumn * m, dnb.format(sum(northCnt)), "R");
					this.print(tn - tab, colAmt + startColumn * m, inb.format(sum(northAmt)), "R");

					// 中區 全工作月 總計 件數及金額
					this.print(tc - tab, colCnt + startColumn * m, dnb.format(sum(centralCnt)), "R");
					this.print(tc - tab, colAmt + startColumn * m, inb.format(sum(centralAmt)), "R");

					// 南區 全工作月 總計 件數及金額
					this.print(ts - tab, colCnt + startColumn * m, dnb.format(sum(southCnt)), "R");
					this.print(ts - tab, colAmt + startColumn * m, inb.format(sum(southAmt)), "R");

					// 全區 全工作月 總計 件數及金額
					this.print(tt - tab, colCnt + startColumn * m, dnb.format(sum(totalCnt)), "R");
					this.print(tt - tab, colAmt + startColumn * m, inb.format(sum(totalAmt)), "R");

				} else {

					// 陣列的位置
					int pos = tab == 20 ? (m + 7) : m;

					this.print(t1 - tab, colmonth + startColumn * m, "第" + month + "工作月", "C");

					// 北區 X工作月 總計 件數及金額
					this.print(tn - tab, colCnt + startColumn * m, dnb.format(northCnt[pos]), "R");
					this.print(tn - tab, colAmt + startColumn * m, inb.format(northAmt[pos]), "R");

					// 中區 X工作月 總計 件數及金額
					this.print(tc - tab, colCnt + startColumn * m, dnb.format(centralCnt[pos]), "R");
					this.print(tc - tab, colAmt + startColumn * m, inb.format(centralAmt[pos]), "R");

					// 南區 X工作月 總計 件數及金額
					this.print(ts - tab, colCnt + startColumn * m, dnb.format(southCnt[pos]), "R");
					this.print(ts - tab, colAmt + startColumn * m, inb.format(southAmt[pos]), "R");

					// 全區 X工作月 總計 件數及金額
					this.print(tt - tab, colCnt + startColumn * m, dnb.format(totalCnt[pos]), "R");
					this.print(tt - tab, colAmt + startColumn * m, inb.format(totalAmt[pos]), "R");

				}

				this.print(t3 - tab, colTitleCnt + startColumn * m, "房貸", "C");
				this.print(t4 - tab, colTitleCnt + startColumn * m, "件數", "C");

				this.print(t3 - tab, colTitleAmt + startColumn * m, "房貸", "C");
				this.print(t4 - tab, colTitleAmt + startColumn * m, "撥款金額", "C");

			}
			this.print(t2 - tab, deInt, "區域", "C");
			this.print(t2 - tab, maInt, "經理", "C");

			this.print(-25 - tab, 7, "總　計", "C");

		}

		long snoPdf = this.close();
		this.toPdf(snoPdf, reportCode + "_" + reportItem);
	}

	/**
	 * 劃表格
	 * 
	 * @param startColumn 表格起始列
	 */
	public void drawTable(int startColumn) {
		// x軸 最後一條線的位置
		int yFirstPoint = 6;
		int yLastPoint = 835;

		// 橫線(x軸)

		this.drawLine(yFirstPoint, 345 + startColumn, yLastPoint, 345 + startColumn); // x1
		this.drawLine(64, 370 + startColumn, yLastPoint, 370 + startColumn); // x2
		this.drawLine(yFirstPoint, 400 + startColumn, yLastPoint, 400 + startColumn); // x3
		this.drawLine(yFirstPoint, 445 + startColumn, yLastPoint, 445 + startColumn); // x4
		this.drawLine(yFirstPoint, 490 + startColumn, yLastPoint, 490 + startColumn); // x5
		this.drawLine(yFirstPoint, 535 + startColumn, yLastPoint, 535 + startColumn); // x6
		this.drawLine(yFirstPoint, 565 + startColumn, yLastPoint, 565 + startColumn); // x7

		// 直線(y軸)
		// 第一條直線
		this.drawLine(yFirstPoint, 345 + startColumn, yFirstPoint, 565 + startColumn); // y1

		// 第二條直線
		this.drawLine(35, 345 + startColumn, 35, 535 + startColumn); // y2

		// 第三條直線
		int x1 = 64;
		this.drawLine(x1, 345 + startColumn, x1, 565 + startColumn); // y3

		// 第四條直線
		int x2 = 101;

		// 第五條直線
		int x3 = 65;

		// 第四&五條直線 重複往後繪製 間距100
		for (int x = 1; x <= 7; x++) {

			// 間距
			int columnInterval = 110;
			this.drawLine(x2 + columnInterval * (x - 1), 370 + startColumn, x2 + columnInterval * (x - 1),
					565 + startColumn); // y4

			this.drawLine(x3 + columnInterval * x, 345 + startColumn, x3 + columnInterval * x, 565 + startColumn); // y5
		}

	}

	private void exportExcel(TitaVo titaVo, Map<String, String> wkVo, List<Map<String, String>> fnAllList)
			throws LogicException {

		this.info("===========in Excel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), reportCode, reportItem,
				reportCode + "_" + reportItem, "LP001_底稿_區域中心業績累計.xlsx", "10805");

		int iYEAR = 0;
		int iMM = 0;

		// 以當月工作月 推回上一工作月
		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1"));
		}

		// 民國年
		String rocYear = String.valueOf(iYEAR - 1911);
		// 民國年月
		String yymm = rocYear + String.format("%02d", iMM);
		// 月份
		String mm = String.valueOf(iMM);

		makeExcel.setSheet("10805", yymm);

		// 標題
		String title = "第1~" + mm;

		makeExcel.setValue(1, 1, rocYear + "年" + title + "工作月 放款審查課各區業績累計表");

		makeExcel.setValue(10, 15, title + "工作月累計");

		// 北區經理名字
		String northMan = "";
		// 中區經理名字
		String centralMan = "";
		// 南區經理名字
		String southMan = "";

		// 北區件數
		double[] northCnt = new double[13];
		// 中區件數
		double[] centralCnt = new double[13];
		// 南區件數
		double[] southCnt = new double[13];
		// 全區件數
		double[] totalCnt = new double[13];

		// 北區金額
		int[] northAmt = new int[13];
		// 中區金額
		int[] centralAmt = new int[13];
		// 南區金額
		int[] southAmt = new int[13];
		// 全區金額
		int[] totalAmt = new int[13];

		// 工作月位置
		int position = 0;

		// 區域 經理 控制上下表格
		int cn = 0;
		int cc = 0;
		int cs = 0;

		int startCol = 3;

		if (fnAllList.size() > 0) {

			// 放至陣列 重新排序 以及做 加總處理
			for (Map<String, String> tLDVo : fnAllList) {

//				this.info("tLDVo-------->" + tLDVo.toString());
				// 用工作月識別欄位位置
				position = Integer.parseInt(tLDVo.get("F1").substring(4, 6)) - 1;

				// 區域中心
				switch (tLDVo.get("F4")) {
				case "10HC00":
					if (cn == 0) {

						makeExcel.setValue(5, 2, northMan.isEmpty() ? tLDVo.get("F0") : northMan, "C");
						makeExcel.setValue(13, 2, northMan.isEmpty() ? tLDVo.get("F0") : northMan, "C");
						cn++;
					}

					// 北區 各X工作月 件數 及 金額 累加
					northCnt[position] += Double.parseDouble(tLDVo.get("F2"));
					northAmt[position] += Integer.parseInt(tLDVo.get("F3"));

					break;
				case "10HJ00":
					if (cc == 0) {
						makeExcel.setValue(6, 2, centralMan.isEmpty() ? tLDVo.get("F0") : centralMan, "C");
						makeExcel.setValue(14, 2, centralMan.isEmpty() ? tLDVo.get("F0") : centralMan, "C");
						cc++;
					}

					// 中區 各X工作月 件數 及 金額 累加
					centralCnt[position] += Double.parseDouble(tLDVo.get("F2"));
					centralAmt[position] += Integer.parseInt(tLDVo.get("F3"));

					break;
				case "10HL00":

					if (cs == 0) {
						makeExcel.setValue(7, 2, southMan.isEmpty() ? tLDVo.get("F0") : southMan, "C");
						makeExcel.setValue(15, 2, southMan.isEmpty() ? tLDVo.get("F0") : southMan, "C");
						cs++;
					}

					// 南區 各X工作月 件數 及 金額 累加
					southCnt[position] += Double.parseDouble(tLDVo.get("F2"));
					southAmt[position] += Integer.parseInt(tLDVo.get("F3"));

					break;
				default:
					break;
				}
				totalCnt[position] += Double.parseDouble(tLDVo.get("F2"));
				totalAmt[position] += Integer.parseInt(tLDVo.get("F3"));

			}

			for (int j = 0; j <= 12; j++) {

				int col = j < 7 ? startCol + (j * 2) : startCol + ((j - 7) * 2);
				int addRow = j < 7 ? 0 : 8;
				// 北區 X工作月 總計 件數及金額
				makeExcel.setValue(5 + addRow, col, getBigDecimal(northCnt[j]), "#,##0.0", "R");
				makeExcel.setValue(5 + addRow, col + 1, getBigDecimal(northAmt[j]), "#,##0", "R");

				// 中區 X工作月 總計 件數及金額
				makeExcel.setValue(6 + addRow, col, getBigDecimal(centralCnt[j]), "#,##0.0", "R");
				makeExcel.setValue(6 + addRow, col + 1, getBigDecimal(centralAmt[j]), "#,##0", "R");

				// 南區 X工作月 總計 件數及金額
				makeExcel.setValue(7 + addRow, col, getBigDecimal(southCnt[j]), "#,##0.0", "R");
				makeExcel.setValue(7 + addRow, col + 1, getBigDecimal(southAmt[j]), "#,##0", "R");

				// 全區 X工作月 總計 件數及金額
				makeExcel.setValue(8 + addRow, col, getBigDecimal(totalCnt[j]), "#,##0.0", "R");
				makeExcel.setValue(8 + addRow, col + 1, getBigDecimal(totalAmt[j]), "#,##0", "R");

			}
			// 北區 全工作月 總計 件數及金額
			makeExcel.setValue(13, 15, getBigDecimal(sum(northCnt)), "#,##0.0", "R");
			makeExcel.setValue(13, 16, getBigDecimal(sum(northAmt)), "#,##0", "R");

			// 中區 全工作月 總計 件數及金額
			makeExcel.setValue(14, 15, getBigDecimal(sum(centralCnt)), "#,##0.0", "R");
			makeExcel.setValue(14, 16, getBigDecimal(sum(centralAmt)), "#,##0", "R");

			// 南區 全工作月 總計 件數及金額
			makeExcel.setValue(15, 15, getBigDecimal(sum(southCnt)), "#,##0.0", "R");
			makeExcel.setValue(15, 16, getBigDecimal(sum(southAmt)), "#,##0", "R");

			// 全區 全工作月 總計 件數及金額
			makeExcel.setValue(16, 15, getBigDecimal(sum(totalCnt)), "#,##0.0", "R");
			makeExcel.setValue(16, 16, getBigDecimal(sum(totalAmt)), "#,##0", "R");

		} else {
			makeExcel.setValue(5, 3, "本日無資料");
		}
		long snoExcel = makeExcel.close();
		makeExcel.toExcel(snoExcel);
	}

	/**
	 * 合計
	 * 
	 * @param num 陣列(浮點數)
	 * @return double
	 */
	public double sum(double[] num) {
		if (num == null || num.length == 0) {
			return 0.0;
		}
		double sum = 0.0;
		for (double n : num) {
			sum += n;
		}
		return sum;
	}

	/**
	 * 合計
	 * 
	 * @param num 陣列(數值)
	 * @return int
	 */
	public int sum(int[] num) {
		if (num == null || num.length == 0) {
			return 0;
		}
		int sum = 0;
		for (int n : num) {
			sum += n;
		}
		return sum;
	}

}
