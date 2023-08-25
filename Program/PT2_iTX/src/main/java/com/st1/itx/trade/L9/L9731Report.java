package com.st1.itx.trade.L9;

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
import com.st1.itx.db.service.springjpa.cm.L9731ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9731Report extends MakeReport {

	@Autowired
	L9731ServiceImpl l9731ServiceImpl;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "L9731";
	String txname = "人工檢核工作表";
	// 底稿1：五類資產檢核表
	// 底稿2：放款總歸戶明細表
	// 底稿3：放款額度明細表
	// 底稿4：放款餘額明細表

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @return
	 * @throws LogicException
	 *
	 * 
	 */
	public boolean exec(TitaVo titaVo, int yearMonth) throws LogicException {
		this.info(" exec");

		List<Map<String, String>> findList = null;

		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));

		int rocYearMonth = yearMonth - 191100;

		// L9731
		String txcd = titaVo.getTxcd();
		// 檔案名稱
		String rptItem = "";
		// 輸出檔名
		String fileName = "";
		// 底稿名稱
		String defaultName = "";

		for (int i = 1; i <= totalItem; i++) {

			if (titaVo.getParam("BtnShell" + i).equals("V")) {

				String tradeCode = titaVo.getParam("TradeCode" + i);
				String tradeName = titaVo.getParam("TradeName" + i);

				rptItem = "人工檢核表(" + tradeName + ")";
				// L9731-人工檢核表(XXXX)-YYYMM
				fileName = txcd + "-" + rptItem + "-" + rocYearMonth;

				defaultName = "L9731_底稿_人工檢核表" + i + ".xlsx";

				// 開啟報表
				ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
						.setRptCode(txcd).setRptItem(rptItem).build();

				try {

					switch (tradeCode) {
					// 五類資產檢核表
					case "HANDMADE1":

						makeExcel.open(titaVo, reportVo, fileName, defaultName, tradeName);

//						makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9731",
//								"人工檢核表(" + tradeName + ")", "L9731-人工檢核表(" + tradeName + ")-" + rocYearMonth,
//								"L9731_底稿_人工檢核表" + i + ".xlsx", tradeName);

						findList = l9731ServiceImpl.findSheet3_1(titaVo, yearMonth);

						exportSheet1(titaVo, findList, rocYearMonth);

						break;

					// 放款總歸戶明細表
					case "HANDMADE2":

						makeExcel.open(titaVo, reportVo, fileName, defaultName, tradeName);

						findList = l9731ServiceImpl.findSheet2(titaVo, yearMonth);

						exportSheet2(titaVo, findList);

						break;

					// 放款額度明細表
					case "HANDMADE3":

						makeExcel.open(titaVo, reportVo, fileName, defaultName, tradeName);

						findList = l9731ServiceImpl.findSheet3_1(titaVo, yearMonth);

						exportSheet3(titaVo, findList, 1);
//						findList = l9731ServiceImpl.findSheet3_2(titaVo, yearMonth);

//						exportSheet3(titaVo, findList, 2);

						break;

					// 放款餘額明細表
					case "HANDMADE4":

						makeExcel.open(titaVo, reportVo, fileName, defaultName, tradeName);

						findList = l9731ServiceImpl.findSheet1(titaVo, yearMonth);

						exportSheet4(titaVo, findList, 1);

						break;
					}

				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.info("L9731ServiceImpl.findAll error = " + errors.toString());

				}

				makeExcel.close();
			}
		}

		return true;

	}

	/**
	 * Sheet1 五類資產檢核表
	 */
	private void exportSheet1(TitaVo titaVo, List<Map<String, String>> listL9731, int rocYearMonth)
			throws LogicException {
		this.info("L9731Report exportSheet1");

		if (listL9731 == null || listL9731.isEmpty()) {

			makeExcel.setValue(2, 1, "本日無資料", "L");
			makeExcel.setValue(2, 2, 12313, "0000000", "C");
		} else {

			int row = 2;
			makeExcel.setValue(1, 11, rocYearMonth, "C");

			for (Map<String, String> tLDVo : listL9731) {
				makeExcel.setValue(row, 1, Integer.valueOf(tLDVo.get("F0").toString() + tLDVo.get("F1").toString()),
						"######0", "L");
				for (int i = 0; i < tLDVo.size(); i++) {

					String fieldValue = tLDVo.get("F" + i);

					switch (i) {
					case 0:// 戶號
						makeExcel.setValue(row, 2, fieldValue, "L");
						break;
					case 1:// 額度
						makeExcel.setValue(row, 3, fieldValue, "L");
						break;
					case 9:// 繳息迄日

						makeExcel.setValue(row, 4, showBcDate(fieldValue, 0), "C");

						break;
					case 10:// 到期日

						makeExcel.setValue(row, 5, showBcDate(fieldValue, 0), "C");

						break;
					case 11:// 放款餘額
						BigDecimal amt = getBigDecimal(fieldValue);
						makeExcel.setValue(row, 6, amt, "#,##0", "R");
						break;
					case 12:// 逾期數

						makeExcel.setValue(row, 7, fieldValue, "C");
						break;
					case 14:// 資產分類2
						makeExcel.setValue(row, 8, fieldValue, "C");
					case 17:// 無擔保金額
						BigDecimal amt901 = getBigDecimal(fieldValue);
						makeExcel.setValue(row, 9, amt901, "#,##0", "R");
					case 18:// 無擔保資產分類
						makeExcel.setValue(row, 10, fieldValue, "C");

					default:

						break;
					}
				} // for

				row++;
			} // for

			// excel formula
			makeExcel.formulaCaculate(1, 6);

		}

	}

	/**
	 * Sheet2 放款總歸戶明細表
	 */
	private void exportSheet2(TitaVo titaVo, List<Map<String, String>> listL9731) throws LogicException {
		this.info("L9731Report exportSheet2");

		if (listL9731 == null || listL9731.isEmpty()) {

			makeExcel.setValue(2, 1, "本日無資料", "L");

		} else {

			int row = 2;

			for (Map<String, String> tLDVo : listL9731) {

				int custNo = tLDVo.get("F0").isEmpty() ? 0 : Integer.valueOf(tLDVo.get("F0"));
				makeExcel.setValue(row, 1, custNo, "R");

				String name = tLDVo.get("F1").isEmpty() ? " " : tLDVo.get("F1");
				makeExcel.setValue(row, 2, name, "L");

				String custID = tLDVo.get("F2").isEmpty() ? " " : tLDVo.get("F2");
				makeExcel.setValue(row, 3, custID, "L");

				BigDecimal amt = tLDVo.get("F3").isEmpty() || tLDVo.get("F0") == null ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(row, 4, amt, "#,000", "R");

				row++;
			} // for

			// excel formula
			makeExcel.formulaCaculate(1, 3);
			makeExcel.formulaCaculate(1, 4);
		}

	}

	/**
	 * Sheet3 放款額度明細表
	 */
	private void exportSheet3(TitaVo titaVo, List<Map<String, String>> listL9731, int form) throws LogicException {
		this.info("L9731Report exportSheet3");

		if (listL9731 == null || listL9731.isEmpty()) {

			makeExcel.setValue(2, 1, "本日無資料", "L");

		} else {

			if (form == 1) {

				int row = 2;

				for (Map<String, String> tLDVo : listL9731) {
					makeExcel.setValue(row, 1, Integer.valueOf(tLDVo.get("F0").toString() + tLDVo.get("F1").toString()),
							"######0", "R");

					for (int i = 0; i < tLDVo.size(); i++) {

						String fieldValue = tLDVo.get("F" + i);

						// 起始欄位 2
						int col = i + 2;

						switch (i) {
						case 0:// 戶號
						case 1:// 額度
							makeExcel.setValue(row, col, fieldValue, "L");
							break;
						case 2:// 利變
							makeExcel.setValue(row, col, fieldValue, "C");
							break;
						case 3:// 戶名
						case 4:// CUSTID
							makeExcel.setValue(row, col, fieldValue, "L");
							break;
						case 5:// 地區
						case 6:// 科目
							makeExcel.setValue(row, col, fieldValue, "R");
							break;
						case 7:// 利率
							BigDecimal rate = getBigDecimal(fieldValue);
							makeExcel.setValue(row, col, rate, "0.00000", "R");
							break;
						case 8:// 撥款日
						case 9:// 繳息迄日
						case 10:// 到期日
//							if (fieldValue != null && !fieldValue.isEmpty() && !fieldValue.equals("0")) {
							makeExcel.setValue(row, col, showBcDate(fieldValue, 0), "C");
//							}
							break;
						case 11:// 放款餘額
							BigDecimal amt = getBigDecimal(fieldValue);
							makeExcel.setValue(row, col, amt, "#,##0", "R");
							break;
						case 12:// 逾期數
						case 13:// 資產評估
						case 14:// 資產分類
						case 15:// 擔保品分類
						case 16:// 產品利率代碼
							makeExcel.setValue(row, col, fieldValue, "C");
							break;
						default:
//							makeExcel.setValue(row, col, fieldValue, "L");
							break;
						}
					} // for

					row++;
				} // for

				// excel formula
				makeExcel.formulaCaculate(1, 6);
				makeExcel.formulaCaculate(1, 13);

//				for (int i = 1; i <= listL9731.size(); i++) {
//					makeExcel.formulaCalculate(i + 1, 1);
//				}

			}
//			if (form == 2) {
//
//				int row = 2;
//
//				for (Map<String, String> tLDVo : listL9731) {
//					// 戶號額度
//					String custfacm = tLDVo.get("F0") == null && !tLDVo.get("F0").isEmpty() ? " " : tLDVo.get("F0");
//					makeExcel.setValue(row, 20, custfacm, "R");
//					// 擔保品類別
//					String clType = tLDVo.get("F1") == null && !tLDVo.get("F1").isEmpty() ? " " : tLDVo.get("F1");
//					makeExcel.setValue(row, 21, clType, "R");
//					// 商品利率代碼
//					String prodNo = tLDVo.get("F2") == null && !tLDVo.get("F2").isEmpty() ? " " : tLDVo.get("F2");
//					makeExcel.setValue(row, 22, prodNo, "C");
//					// 初貸日期
//					String fDrawDownDate = tLDVo.get("F3") == null && !tLDVo.get("F3").isEmpty() ? " "
//							: tLDVo.get("F3");
//					makeExcel.setValue(row, 23, fDrawDownDate, "C");
//
//					row++;
//				}
//
//			}

		}

	}

	/**
	 * exportSheet4 放款餘額明細表
	 */
	private void exportSheet4(TitaVo titaVo, List<Map<String, String>> listL9731, int form) throws LogicException {
		this.info("L9731Report exportSheet4");

		if (listL9731 == null || listL9731.isEmpty()) {

			makeExcel.setValue(2, 1, "本日無資料", "L");

		} else {

			int row = 2;

			for (Map<String, String> tLDVo : listL9731) {
				makeExcel.setValue(row, 1, Integer.valueOf(tLDVo.get("F0").toString() + tLDVo.get("F1").toString()),
						"###0", "R");
				for (int i = 0; i < tLDVo.size(); i++) {

					String fieldValue = tLDVo.get("F" + i);
					// 從B欄開始
					int col = i + 2;

					switch (i) {
					case 0:// 戶號
					case 1:// 額度
					case 2:// 序號
						makeExcel.setValue(row, col, fieldValue, "#0", "R");
						break;

					case 3:// 利變
					case 6:// 科目
						makeExcel.setValue(row, col, fieldValue, "C");
						break;
					case 4:// ID
					case 5:// 戶名
						makeExcel.setValue(row, col, fieldValue, "L");
						break;

					case 7:// 撥款日
					case 8:// 到期日
					case 11:// 繳息迄日
					case 12:// 轉催收日期
//						if (fieldValue != null && !fieldValue.isEmpty() && !fieldValue.equals("0")) {
						makeExcel.setValue(row, col, showBcDate(fieldValue, 0), "C");
//						}
						break;
					case 9:// 利率
						BigDecimal rate = getBigDecimal(fieldValue);
						makeExcel.setValue(row, col, rate, "0.0000", "R");
						break;
					case 10:// 繳息周期
					case 13:// 資金用讀別
						makeExcel.setValue(row, col, fieldValue, "C");
						break;
					case 14:// 核貸金額
					case 15:// 撥款金額
					case 16:// 放款餘額
					case 28:// 無擔保金額
						BigDecimal amt = getBigDecimal(fieldValue);
						makeExcel.setValue(row, col, amt, "#,##0", "R");
						break;
					case 17:// 擔保品地區別
						makeExcel.setValue(row, col, fieldValue, "R");
					case 18:// 擔保品地區別地址
						makeExcel.setValue(row, col, fieldValue, "L");
						break;
					case 19:// 商品利率代碼
					case 20:// 初貸日
					case 21:// 政策性_專案貸款
					case 22:// 擔保品類別
					case 23:// 案件隸屬單位
					case 24:// 企金別
					case 25:// 建築貸款
					case 26:// 資產分類
					case 27:// 資產分類2
					case 29:// 無擔保資產分類
						makeExcel.setValue(row, col, fieldValue, "C");
						break;

					default:
//						makeExcel.setValue(row, col, fieldValue, "L");
						break;
					}
				} // for

				row++;
			} // for

			// 放款餘額總計的 excel formula
//			for (int i = 1; i <= listL9731.size(); i++) {
//				makeExcel.formulaCalculate(i + 1, 1);
//			}
			makeExcel.formulaCaculate(1, 18);
		}

	}

}
