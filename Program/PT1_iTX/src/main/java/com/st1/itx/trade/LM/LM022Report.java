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
import com.st1.itx.db.service.springjpa.cm.LM022ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM022Report extends MakeReport {

	@Autowired
	LM022ServiceImpl lM022ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printHeader() {

//		printHeaderL();
//
//		this.setBeginRow(8);
//
//		this.setMaxRows(45);
	}

	public void printHeaderL() {
		this.print(-3, 120, "機密等級："+makeExcel.getSecurity());
		this.print(-5, 3, showDate(titaVo.get("ENTDY").toString(), 1));
		this.print(-6, 7, "戶號　額度　撥款　公司名稱　　放款餘額　　　繳息迄日　　利率＝＝＞　利率");

	}

	public void exec(TitaVo titaVo) throws LogicException {
		exportExcel(titaVo);

	}

	public void exportExcel(TitaVo titaVo) throws LogicException {
		// 開啟EXCE
		// 取民國年月(日)
		String ROCyymm = titaVo.get("ENTDY").substring(1, 4) + "." + titaVo.get("ENTDY").substring(4, 6);
		String ROCyymmdd = titaVo.get("ENTDY").substring(1, 4) + "." + titaVo.get("ENTDY").substring(4, 6) + "." + titaVo.get("ENTDY").substring(6, 8);

		this.info("ROCyymm=" + ROCyymm);
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM022";
		String fileItem = "中央銀行業務局921補貼息";
		String fileName = "LM022_中央銀行業務局921補貼息" ;
		String defaultExcel = ROCyymm;
		String defaultSheet = "明細";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM022", "中央銀行業務局921補貼息", "LM022_中央銀行業務局921補貼息", ROCyymm + "明細");

		// 設定欄寬
		makeExcel.setWidth(1, 18);
		makeExcel.setWidth(2, 18);
		makeExcel.setWidth(3, 10);
		makeExcel.setWidth(4, 18);
		makeExcel.setWidth(5, 18);
		makeExcel.setWidth(6, 18);
		makeExcel.setWidth(7, 18);
		makeExcel.setWidth(8, 18);
		makeExcel.setWidth(9, 18);
		makeExcel.setWidth(10, 18);
		makeExcel.setWidth(11, 18);
		makeExcel.setWidth(12, 8);
		makeExcel.setWidth(13, 20);
		makeExcel.setWidth(14, 15);
		// 民國年
		makeExcel.setMergedRegionValue(1, 1, 2, 3, ROCyymmdd, "L");

		makeExcel.setValue(2, 1, "戶號額度", "C");
		makeExcel.setValue(2, 2, "戶號", "C");
		makeExcel.setValue(2, 3, "額度", "C");
		makeExcel.setValue(2, 4, "撥款序號", "C");
		makeExcel.setValue(2, 5, "公司名稱", "C");
		makeExcel.setValue(2, 6, "放款餘額", "C");

		makeExcel.setValue(2, 7, "繳息迄日", "C");
		makeExcel.setValue(2, 8, "利率==>", "C");
		makeExcel.setValue(2, 9, "利率", "C");
		makeExcel.setValue(2, 10, "", "C");
		makeExcel.setValue(2, 11, "", "C");
		makeExcel.setValue(2, 12, "", "C");
		makeExcel.setValue(2, 13, "中央明細(餘額)", "C");
		makeExcel.setValue(2, 14, "大於央行", "C");

		List<Map<String, String>> LM022List = null;

		try {
			LM022List = lM022ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lM022ServiceImpl.findAll error = " + errors.toString());
		}

		DecimalFormat df1 = new DecimalFormat("#,##0");
		if (LM022List.size() > 0) {
			int row = 2;
			int i = 0;
			// 放款餘額全戶號加總
			BigDecimal sumAmt = new BigDecimal("0.00");
			// 單一戶號的攤還金額
			BigDecimal dueAmt = new BigDecimal("0.00");

			String tempCustNo = "";

			// 放款餘額total
			BigDecimal f4total = new BigDecimal("0.00");

			for (Map<String, String> tLM022Vo : LM022List) {
				row++;
				// 補0 滿7位數
				if (!tempCustNo.equals(String.format("%07d", Integer.parseInt(tLM022Vo.get("F0"))))) {
					tempCustNo = String.format("%07d", Integer.parseInt(tLM022Vo.get("F0")));

					if (row > 1) {
						makeExcel.setValue(row - 1, 11, dueAmt, "#,##0.00", "R");
					}

					dueAmt = BigDecimal.ZERO;
				}

				// 戶號額度(A欄)
				makeExcel.setValue(row, 1, tLM022Vo.get("F0") + tLM022Vo.get("F1") + tLM022Vo.get("F2"), "R");

				// 戶號(B欄)
				makeExcel.setValue(row, 2, Integer.parseInt(tLM022Vo.get("F0")), "0000000", "R");

				// 額度(C欄)
				makeExcel.setValue(row, 3, Integer.parseInt(tLM022Vo.get("F1")), "C");

				// 撥款序號(D欄)
				makeExcel.setValue(row, 4, Integer.parseInt(tLM022Vo.get("F2")), "C");

				// 公司名稱(E欄)
				makeExcel.setValue(row, 5, tLM022Vo.get("F3"), "L");

				// 放款餘額(F欄)
				if (tLM022Vo.get("F4") == null || tLM022Vo.get("F4").length() == 0 || tLM022Vo.get("F4").equals("0")) {
					makeExcel.setValue(row, 6, "-", "C");
				} else {
					BigDecimal f4 = new BigDecimal(tLM022Vo.get("F4"));
					if (f4.toString().equals("0")) {
						makeExcel.setValue(row, 6, "-", "C");
					} else {
						makeExcel.setValue(row, 6, f4, "#,##0", "R");
					}
				}

				f4total = tLM022Vo.get("F4") == null || tLM022Vo.get("F4").length() == 0 || tLM022Vo.get("F4").equals("0") ? BigDecimal.ZERO : new BigDecimal(tLM022Vo.get("F4").toString());

				// 繳息訖日(G欄)
				String f5 = tLM022Vo.get("F5") == null || tLM022Vo.get("F5").length() == 0 || tLM022Vo.get("F4") == null || tLM022Vo.get("F4").length() == 0 || tLM022Vo.get("F4").equals("0") ? ""
						: showRocDate(Integer.valueOf(tLM022Vo.get("F5")) - 19110000, 1);
				makeExcel.setValue(row, 7, f5, "C");

				// 利率==>(H欄) + 利率(I欄)
				if (tLM022Vo.get("F6") == null || tLM022Vo.get("F6").length() == 0) {
					makeExcel.setValue(row, 8, "-", "C");
					makeExcel.setValue(row, 9, "免利息", "L");
				} else {
					if (tLM022Vo.get("F6").equals("0")) {
						makeExcel.setValue(row, 8, "-", "C");
						makeExcel.setValue(row, 9, "免利息", "L");
					} else {

						makeExcel.setValue(row, 8, formatAmt(tLM022Vo.get("F6"), 4), "#.##0", "R");
						makeExcel.setValue(row, 9, "固定利率", "L");
					}
				}

				// 每期攤還金額(J欄)
				if (tLM022Vo.get("F7") == null || tLM022Vo.get("F7") == "" || tLM022Vo.get("F4") == null || tLM022Vo.get("F4").length() == 0 || tLM022Vo.get("F4").equals("0")) {
					makeExcel.setValue(row, 10, "-", "C");
				} else {
					BigDecimal f7 = new BigDecimal(tLM022Vo.get("F7"));

					if (f7.toString().equals("0")) {
						makeExcel.setValue(row, 10, "-", "C");
					} else {
						makeExcel.setValue(row, 10, f7, "#,##0.00", "R");
					}

					if (tempCustNo.equals(String.format("%07d", Integer.parseInt(tLM022Vo.get("F0"))))) {

						dueAmt = dueAmt.add(f7);// 攤還金額加總
					}
				}

				i++;

				sumAmt = sumAmt.add(f4total);

				if (i == LM022List.size()) {

					// 放款餘額全戶加總(K欄)
					makeExcel.setValue(row + 1, 6, df1.format(sumAmt), "R");

					// 進行最後一個戶號的攤還金額加總
					makeExcel.setValue(row, 11, dueAmt, "#,##0.00", "R");
					sumAmt = BigDecimal.ZERO;
				}
				/* 直接在底稿設定公式 */
//				makeExcel.setValue(row, 13, "-", "C");
//				makeExcel.setValue(row, 14, "-", "C");

				makeExcel.setValue(row, 15, tLM022Vo.get("F8") == null || tLM022Vo.get("F8").length() == 0 ? "" : tLM022Vo.get("F8"), "C");

			}
			makeExcel.setAddRengionBorder("A", 2, "K", row, 1);
		} else {
			makeExcel.setValue(3, 1, "本日無資料", "C");

		}

		makeExcel.setHeight(1, 20);
		long closeExcel = makeExcel.close();
		//makeExcel.toExcel(closeExcel);

	}

//	private String padStart(String temp, int len, String tran) {
//		if (temp.length() < len) {
//			for (int i = temp.length(); i < len; i++) {
//				temp = tran + temp;
//			}
//		}
//		return temp;
//	}

	private String showDate(String date, int iType) {
		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);
		if (iType == 1) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "." + rocdatex.substring(2, 4) + "." + rocdatex.substring(4, 6);
			} else {
				return rocdatex.substring(0, 3) + "." + rocdatex.substring(3, 5) + "." + rocdatex.substring(5, 7);
			}
		} else if (iType == 2) {
			if (rocdatex.length() == 6) {
				return rocdatex.substring(0, 2) + "年" + rocdatex.substring(2, 4) + "月" + rocdatex.substring(4, 6) + "日";
			} else {
				return rocdatex.substring(0, 3) + "年" + rocdatex.substring(3, 5) + "月" + rocdatex.substring(5, 7) + "日";
			}
		} else {
			return rocdatex;
		}
	}

//	public void exec(TitaVo titaVo) throws LogicException {
//
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM022", "中央銀行業務局921補貼息", "密", "", "L");
//
//		List<Map<String, String>> LM022List = null;
//		try {
//			LM022List = lM022ServiceImpl.findAll(titaVo);
//		} catch (Exception e) {
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.info("lM022ServiceImpl.findAll error = " + errors.toString());
//		}
//
//		if (LM022List.size() != 0) {
//
//			DecimalFormat df1 = new DecimalFormat("#,##0");
//
//			int i = 0;
//			BigDecimal SumAmt = new BigDecimal("0.00");
//			BigDecimal dueAmt = new BigDecimal("0.00");
//
//			String CusNo = "";
//			if (LM022List.size() > 0) {
//				CusNo = LM022List.get(0).get("F0");
//			}
//
//			for (Map<String, String> tLM022Vo : LM022List) {
//
//				this.setFontSize(8);
//				this.print(1, 1, "");
//
//				if ((CusNo).equals(padStart(tLM022Vo.get("F0"), 7, "0"))) {
//
//					this.print(-7 - i, 9, padStart(tLM022Vo.get("F0"), 7, "0"), "C");
//					this.print(-7 - i, 15, tLM022Vo.get("F1"), "R");
//					this.print(-7 - i, 20, tLM022Vo.get("F2"), "R");
//					this.print(-7 - i, 27, tLM022Vo.get("F3"), "C");
//
//					BigDecimal f4 = new BigDecimal(tLM022Vo.get("F4").toString());
//					this.print(-7 - i, 44, df1.format(f4), "R");
//
//					if (tLM022Vo.get("F5").toString() != null && tLM022Vo.get("F5").toString() != "") {
//						this.print(-7 - i, 47, showRocDate(Integer.valueOf(tLM022Vo.get("F5")) - 19110000, 1));
//					}
//
//					if (tLM022Vo.get("F6") == null || tLM022Vo.get("F6") == "") {
//						this.print(-7 - i, 62, "-", "C");
//					} else {
//
//						this.print(-7 - i, 59, formatAmt(tLM022Vo.get("F6"), 4), "C");
//					}
//
//					if (tLM022Vo.get("F6") == null || tLM022Vo.get("F6") == "") {
//						this.print(-7 - i, 71, "免利息", "C");
//					} else {
//						this.print(-7 - i, 70, "固定利率", "C");
//					}
//
//					if (tLM022Vo.get("F7") == null || tLM022Vo.get("F7") == "") {// 每期攤還金額
//						this.print(-7 - i, 80, "-");
//					} else {
//						BigDecimal f7 = new BigDecimal(tLM022Vo.get("F7").toString());
//						this.print(-7 - i, 82, df1.format(f7) + ".00", "R");
//						dueAmt = dueAmt.add(f7);// 攤還金額加總
//					}
//
//				} else {
//					// 不同戶號
//					this.print(-6 - i, 98, df1.format(dueAmt) + ".00", "R");
//					dueAmt = new BigDecimal("0.00");
//
//					this.print(-6 - i, 5,
//							"__________________________________________________________________________________________________");
//					this.print(-7 - i, 9, padStart(tLM022Vo.get("F0"), 7, "0"), "C");
//					this.print(-7 - i, 15, tLM022Vo.get("F1"), "R");
//					this.print(-7 - i, 20, tLM022Vo.get("F2"), "R");
//					this.print(-7 - i, 27, tLM022Vo.get("F3"), "C");
//
//					BigDecimal f4 = new BigDecimal(tLM022Vo.get("F4").toString());
//					this.print(-7 - i, 44, df1.format(f4), "R");
//
//					if (tLM022Vo.get("F5").toString() != null && tLM022Vo.get("F5").toString() != "") {
//						this.print(-7 - i, 47, showRocDate(Integer.valueOf(tLM022Vo.get("F5")) - 19110000, 1));
//					}
//
//					if (tLM022Vo.get("F6") == null || tLM022Vo.get("F6") == "") {
//						this.print(-7 - i, 59, "-", "C");
//					} else {
//						this.print(-7 - i, 59, formatAmt(tLM022Vo.get("F6"), 4), "C");
//					}
//
//					if (tLM022Vo.get("F6") == null || tLM022Vo.get("F6") == "") {
//						this.print(-7 - i, 71, "免利息", "C");
//					} else {
//						this.print(-7 - i, 70, "固定利率", "C");
//					}
//
//					if (tLM022Vo.get("F7") == null || tLM022Vo.get("F7") == "") {// 每期攤還金額
//						this.print(-7 - i, 80, "-");
//					} else {
//						BigDecimal f7 = new BigDecimal(tLM022Vo.get("F7").toString());
//						this.print(-7 - i, 82, df1.format(f7) + ".00", "R");
//						dueAmt = dueAmt.add(f7);// 攤還金額加總
//
//					}
//					CusNo = padStart(tLM022Vo.get("F0"), 7, "0");
//
//				}
//
//				// 列印行數用
//				i++;
//
//				// 放款餘額total
//				BigDecimal f4 = new BigDecimal(tLM022Vo.get("F4").toString());
//				SumAmt = SumAmt.add(f4);
//				int size = LM022List.size();
//
//				if (i == size) {
//					this.print(-6 - i, 5,
//							"__________________________________________________________________________________________________");
//					this.print(-7 - i, 44, df1.format(SumAmt), "R");
//
//					// 右邊央行明細
//					this.print(-6, 110, "╔════════════╗");
//					this.print(-7, 110, "║央行明細（餘額大於央行）║");
//
//					for (int row = 1; row < size; row++) {
//						this.print(-7 - row, 110, "║　　　　　　　　　　　　║");
//					}
//
//					this.print(-7 - size, 110, "╚════════════╝");
//				}
//
////			this.print(-13,113,"219,673","R" );
//
//			}
//
//		} // if
//
//		long sno = this.close();
//		//this.toPdf(sno);
//	}
}
