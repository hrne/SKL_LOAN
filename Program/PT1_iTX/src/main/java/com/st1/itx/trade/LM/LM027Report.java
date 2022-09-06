package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM027ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM027Report extends MakeReport {

	@Autowired
	LM027ServiceImpl lM027ServiceImpl;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	MakeExcel makeExcel;

//	自訂表頭
	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

//		printHeaderP();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(1);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(80);
	}

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("--------------lM027report excel");
		exportExcel(titaVo);
	}

	public void exportExcel(TitaVo titaVo) throws LogicException {
		// 開啟EXCE
		// 取民國年月(日)
		String ROCyymm = titaVo.get("ENTDY").substring(1, 4) + "年" + titaVo.get("ENTDY").substring(4, 6) + "月";
		String sheetROCyymm = titaVo.get("ENTDY").substring(1, 4) + titaVo.get("ENTDY").substring(4, 6);
		String ROCyymmdd = titaVo.get("ENTDY").substring(1, 4) + "." + titaVo.get("ENTDY").substring(4, 6) + "." + titaVo.get("ENTDY").substring(6, 8);

		this.info("ROCyymm=" + ROCyymm);

//		"LM027轉銷呆帳備忘錄.xls"
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM027", "轉銷呆帳備忘錄", "LM027_轉銷呆帳備忘錄", sheetROCyymm);

		// 調整欄寬
		makeExcel.setWidth(1, 18);
		makeExcel.setWidth(2, 18);
		makeExcel.setWidth(3, 25);
		makeExcel.setWidth(4, 18);
		makeExcel.setWidth(5, 18);
		makeExcel.setWidth(6, 18);
		makeExcel.setWidth(7, 18);
		makeExcel.setWidth(8, 20);

		// 合併
		makeExcel.setIBU("B");
		makeExcel.setSize(20);
		makeExcel.setMergedRegionValue(1, 1, 1, 4, "至" + ROCyymm + "呆帳備忘分錄", "L");
		makeExcel.setMergedRegionValue(1, 1, 7, 8, "單位：元\n基準日：" + ROCyymmdd, "L");
		makeExcel.setValue(2, 1, "戶號", "C");
		makeExcel.setValue(2, 2, "額\n度", "C");
		makeExcel.setValue(2, 3, "戶名", "C");
		makeExcel.setValue(2, 4, "轉呆\n金額", "C");
		makeExcel.setValue(2, 5, "累計\n回收金額", "C");

		int lastyymm = Integer.parseInt(titaVo.get("ENTDY").substring(1, 6)) - 1;
		makeExcel.setValue(2, 6, lastyymm + "止\n回收金額", "C");

		String thisyymm = titaVo.get("ENTDY").substring(1, 6);
		makeExcel.setValue(2, 7, thisyymm + "收回", "C");

		makeExcel.setValue(2, 8, "收回金額", "C");

		List<Map<String, String>> LM027List = null;

		try {
			LM027List = lM027ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lM027ServiceImpl.findAll error = " + errors.toString());
		}

		int row = 2;
		DecimalFormat df1 = new DecimalFormat("#,##0");
		BigDecimal total1 = new BigDecimal("0");
		BigDecimal total2 = new BigDecimal("0");
		BigDecimal total3 = new BigDecimal("0");

//		int i = 0;
		if (LM027List.size() > 0) {
			BigDecimal sumAmt1 = new BigDecimal("0");
			BigDecimal sumAmt2 = new BigDecimal("0");
			BigDecimal sumAmt3 = new BigDecimal("0");
			String custNo = "";
			String tempNo = "";

			for (Map<String, String> tLM027Vo : LM027List) {

				BigDecimal F3 = tLM027Vo.get("F3") == null || tLM027Vo.get("F3").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLM027Vo.get("F3"));
				// 轉呆金額
				BigDecimal F4 = tLM027Vo.get("F4") == null || tLM027Vo.get("F4").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLM027Vo.get("F4"));
				// 累計回收金額
				BigDecimal F5 = tLM027Vo.get("F5") == null || tLM027Vo.get("F5").length() == 0 ? BigDecimal.ZERO : new BigDecimal(tLM027Vo.get("F5"));
				// 本月收回金額
				// 上月止回額

				// 補0 滿7位數
				if (!custNo.equals(String.format("%07d", Integer.parseInt(tLM027Vo.get("F0"))))) {
					row++;
					// 換戶號 加總歸零
					sumAmt1 = BigDecimal.ZERO;
					sumAmt2 = BigDecimal.ZERO;
					sumAmt3 = BigDecimal.ZERO;
					// 再戶號的第一次值
					sumAmt1 = F3;
					sumAmt2 = F4;
					sumAmt3 = F5;
					// 先清空上一個戶號的的暫存
					tempNo = "";
					custNo = String.format("%07d", Integer.parseInt(tLM027Vo.get("F0")));
//					this.info("custNo=" + custNo); 65000 64000

					makeExcel.setValue(row, 1, Integer.parseInt(tLM027Vo.get("F0")), "0000000", "C");

					makeExcel.setValue(row, 2, tLM027Vo.get("F1"), "C");

					makeExcel.setValue(row, 3, tLM027Vo.get("F2"), "L");

					makeExcel.setValue(row, 4, F3, "#,##0", "R");

					makeExcel.setValue(row, 5, F4, "#,##0", "R");

					makeExcel.setValue(row, 6, F5, "#,##0", "R");

					if (F4.subtract(F5).intValue() == 0) {
						makeExcel.setValue(row, 7, "", "C");

						makeExcel.setValue(row, 8, BigDecimal.ZERO, "C");

					} else {
						makeExcel.setValue(row, 7, thisyymm, "C");

						makeExcel.setValue(row, 8, F4.subtract(F5).intValue(), "#,##0", "C");

					}

				} else {
					// 一定是同戶號的第二筆才會進來
					if (tempNo != "") {
						tempNo = tempNo + "、" + tLM027Vo.get("F1");
						makeExcel.setValue(row, 2, tempNo, "C");

						sumAmt1 = sumAmt1.add(F3);
						sumAmt2 = sumAmt2.add(F4);
						sumAmt3 = sumAmt3.add(F5);
					}
					makeExcel.setValue(row, 4, sumAmt1.intValue(), "#,##0", "R");

					makeExcel.setValue(row, 5, sumAmt2.intValue(), "#,##0", "R");

					makeExcel.setValue(row, 6, sumAmt3.intValue(), "#,##0", "R");

					// F4-F5

					if (sumAmt2.subtract(sumAmt3).intValue() == 0) {
						makeExcel.setValue(row, 7, "", "C");

						makeExcel.setValue(row, 8, "", "R");

					} else {
						makeExcel.setValue(row, 7, thisyymm, "C");

						makeExcel.setValue(row, 8, sumAmt2.subtract(sumAmt3).intValue(), "#,##0", "R");

					}

				}
				// 第一筆都要賦值
				if (tempNo == "") {
					tempNo = tLM027Vo.get("F1");
				}

				total1 = total1.add(F3);
				total2 = total2.add(F4);
				total3 = total3.add(F5);

			}

			makeExcel.setValue(row + 1, 1, "總計");

			makeExcel.setValue(row + 1, 4, total1.intValue(), "#,##0", "R");

			makeExcel.setValue(row + 1, 5, total2.intValue(), "#,##0", "R");

			makeExcel.setValue(row + 1, 6, total3.intValue(), "#,##0", "R");

			makeExcel.setValue(row + 1, 8, total2.subtract(total3).intValue(), "#,##0", "R");

			makeExcel.setValue(row + 2, 4, total1.subtract(total2).intValue(), "#,##0", "R");

			makeExcel.setValue(row + 2, 8, "(" + thisyymm + "收回金額)", "R");

		} else {
			makeExcel.setValue(3, 1, "本日無資料", "L");
		}
		// 畫框線
		makeExcel.setAddRengionBorder("A", 2, "H", row + 1, 1);
		makeExcel.setMergedRegionValue(row + 5, row + 5, 1, 2, "經辦：", "L");

		makeExcel.setMergedRegionValue(row + 5, row + 5, 3, 4, "經理：", "L");

		makeExcel.setMergedRegionValue(row + 5, row + 5, 5, 6, "協理：", "L");

		makeExcel.setMergedRegionValue(row + 7, row + 7, 1, 8, "1.擬調整會計科子目金額\n追索債權-放款\n等抵銷追索債權-放款", "L");

		makeExcel.setMergedRegionValue(row + 8, row + 8, 1, 8, "2.前次餘額 " + df1.format(total1.subtract(total3)) + " 元", "L");

		makeExcel.setMergedRegionValue(row + 9, row + 9, 1, 8, "3.收回 " + df1.format(total2.subtract(total3)) + " 元，出傳票回沖後，餘額 " + df1.format(total1.subtract(total2)) + "元", "L");

		makeExcel.setMergedRegionValue(row + 10, row + 10, 1, 8, "4.敬會放款服務課接續帳務作業（出傳票、入帳）", "L");

		makeExcel.setMergedRegionValue(row + 12, row + 12, 1, 4, "放款服務課", "C");

		makeExcel.setHeight(row + 13, 140);
		makeExcel.setMergedRegionValue(row + 13, row + 13, 1, 4, "", "C");

		makeExcel.close();
		// makeExcel.toExcel(closeExcel);

	}

	// 自訂明細標題
//	@Override
//	public void printTitle(){
//		this.info("printTitle nowRow = " + this.NowRow);
//						this.setFontSize(12);
//			this.print(-3, 4, "至108年02月呆帳備忘分錄");
//			this.setFontSize(8);
//			this.print(-4, 60, "單 位 :元");
//			this.print(-5, 60, "基準日:108.02.28");
//	
//	}

//	public void exec(TitaVo titaVo) throws LogicException {
//		// 讀取VAR參數
//
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM027", "LM027轉銷呆帳備忘錄", "密", "", "P");
//
//		List<Map<String, String>> LM027List = null;
//		try {
//			LM027List = lM027ServiceImpl.findAll(titaVo);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.info("LM027ServiceImpl.findAll error = " + errors.toString());
//		}
//
//		String entdy = titaVo.get("ENTDY").toString();
//		int i = 0, times = 0;
//		BigDecimal F3Count = new BigDecimal("0");
//		BigDecimal F4Count = new BigDecimal("0");
//		BigDecimal F5Count = new BigDecimal("0");
//		BigDecimal MonthCount = new BigDecimal("0");
//
//		this.print(1, 3, "");
//		this.setFontSize(12);
//		this.setFontSize(8);
//		this.print(1, 60, "單 位 :元");
//		this.setFontSize(12);
//		this.print(1, 4, "至" + showDate(entdy, 2) + "呆帳備忘分錄");
//		this.setFontSize(8);
//		this.print(01, 60, "基準日:" + showDate(entdy, 1));
//
//		if (LM027List.size() > 0) {
//			this.setCharSpaces(0);
//			this.print(1, 3, "┌────┬───┬─────────┬─────────┬─────────┬─────────┬──────┬─────────┐");
//			this.print(1, 3, "│　　　　│　額　│　　　　　　　　　│　　　轉呆　　　　│　　　累計回　　　│　　　　　　　　　│　　　　　　│　　　　　　　　　│");
//			this.print(1, 3, "│　戶號　│　度　│　　　戶名　　　　│　　　金額　　　　│　　　收金額　　　│　　回收金額　　　│　　　　收回│　　　收回金額　　│");
//			this.print(1, 3, "├────┼───┼─────────┼─────────┼─────────┼─────────┼──────┼─────────┤");
//			DecimalFormat df1 = new DecimalFormat("#,##0");
//			for (Map<String, String> tLM027Vo : LM027List) {
//
//				BigDecimal F3 = new BigDecimal(tLM027Vo.get("F3"));
//				// 轉呆金額
//				BigDecimal F4 = new BigDecimal(tLM027Vo.get("F4"));
//				// 累計回收金額
//				BigDecimal F5 = new BigDecimal(tLM027Vo.get("F5"));
//				// 本月收回金額
//				// 上月止回額
//				BigDecimal MoneyCount = F4.subtract(F5);
//				this.info("MoneyCount=" + MoneyCount);
//
//				if (i < 36) {
//					this.info("i=" + i);
//
//					this.print(1, 3, "│　　　　│　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　│　　　　　　　　　│");
//					this.print(0, 9, tLM027Vo.get("F0"), "C");
//					this.print(0, 18, tLM027Vo.get("F1"), "C");
//					this.print(0, 23, tLM027Vo.get("F2"));
//					this.print(0, 61, df1.format(F3), "R");
//					this.print(0, 81, df1.format(F4), "R");
//
//					this.print(0, 101, df1.format(MoneyCount), "R");
//
//					if (MoneyCount.intValue() != 0) {
//						this.print(0, 135, df1.format(F5), "R");
//					}
//
//					
//					times++;
//				} else {
//					this.info("newPage i=" + i);
//					this.print(1, 3, "└────┴───┴─────────┴─────────┴─────────┴─────────┴──────┴─────────┘");
//					this.newPage();
//					this.print(1, 3, "┌────┬───┬─────────┬─────────┬─────────┬─────────┬──────┬─────────┐");
//					this.print(1, 3, "│　　　　│　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　│　　　　　　　　　│");
//					this.print(0, 9, tLM027Vo.get("F0"), "C");
//					this.print(0, 18, tLM027Vo.get("F1"), "C");
//					this.print(0, 23, tLM027Vo.get("F2"));
//					this.print(0, 61, df1.format(F3), "R");
//					this.print(0, 81, df1.format(F4), "R");
//
//					this.print(0, 101, df1.format(MoneyCount), "R");
//
//					if (MoneyCount.intValue() != 0) {
//						this.print(0, 135, df1.format(F5), "R");
//					}
//					this.print(1, 3, "├────┼───┼─────────┼─────────┼─────────┼─────────┼──────┼─────────┤");
//
//					i = 0;
//					times++;
//				}
//				// 各項總金額
//				F3Count = F3Count.add(F3);
//				this.info("F3Count=" + F3Count);
//				F4Count = F4Count.add(F4);
//				this.info("F4Count=" + F4Count);
//				F5Count = F5Count.add(F5);
//				this.info("F5Count=" + F5Count);
//				MonthCount = MonthCount.add(MoneyCount);
//				this.info("MonthCount=" + MonthCount);
//
//				if (times < LM027List.size()) {
//					this.print(1, 3, "├────┼───┼─────────┼─────────┼─────────┼─────────┼──────┼─────────┤");
//				}
//			} // for
//
//			this.print(1, 3, "├────┼───┼─────────┼─────────┼─────────┼─────────┼──────┼─────────┤");
//			this.print(1, 3, "│　總計　│　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　　　　│　　　　　　│　　　　　　　　　│");
//			this.print(0, 61, df1.format(F3Count), "R");
//			this.print(0, 81, df1.format(F4Count), "R");
//			this.print(0, 101, df1.format(MonthCount), "R");
//			this.print(0, 135, df1.format(F5Count), "R");
//			this.print(1, 3, "└────┴───┴─────────┴─────────┴─────────┴─────────┴──────┴─────────┘");
//
//		} // if
//		long sno = this.close();
//		//this.toPdf(sno);
//	}
//
//	private String showDate(String date, int iType) {
//		this.info("MakeReport.toPdf showRocDate1 = " + date);
//		if (date == null || date.equals("") || date.equals("0")) {
//			return "";
//		}
//		int rocdate = Integer.valueOf(date);
//		if (rocdate > 19110000) {
//			rocdate -= 19110000;
//		}
//		String rocdatex = String.valueOf(rocdate);
//		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);
//		if (iType == 1) {
//			if (rocdatex.length() == 6) {
//				return rocdatex.substring(0, 2) + "." + rocdatex.substring(2, 4) + "." + rocdatex.substring(4, 6);
//			} else {
//				return rocdatex.substring(0, 3) + "." + rocdatex.substring(3, 5) + "." + rocdatex.substring(5, 7);
//			}
//		} else if (iType == 2) {
//			if (rocdatex.length() == 6) {
//				return rocdatex.substring(0, 2) + "年" + rocdatex.substring(2, 4) + "月";
//			} else {
//				return rocdatex.substring(0, 3) + "年" + rocdatex.substring(3, 5) + "月";
//			}
//		} else {
//			return rocdatex;
//		}
//	}
}
