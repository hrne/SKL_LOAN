package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM051ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM051Report extends MakeReport {

	@Autowired
	LM051ServiceImpl lM051ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	// 起始列印行數
	int row = 2;

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM051", "放款資產分類案件明細表_內部控管",
				"LM051_放款資產分類案件明細表_內部控管", "LM051_底稿_放款資產分類案件明細表_內部控管.xlsx", "10804工作表");
		makeExcel.setSheet("10804工作表", titaVo.get("ENTDY").substring(1, 6) + "工作表");

		String mm = String.valueOf(Integer.valueOf(titaVo.get("ENTDY").substring(4, 6)));

		makeExcel.setValue(1, 2, "資產分類案件明細表" + titaVo.get("ENTDY").substring(1, 4) + "." + mm);

		List<Map<String, String>> lM051List = null;

		// 有四種不同條件，要select 4次
		for (int i = 1; i < 5; i++) {
			try {

				lM051List = lM051ServiceImpl.findAll(titaVo, i);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM051ServiceImpl.findAll error = " + errors.toString());
			}
			exportExcel(lM051List);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
	// F0+F1
	// F0 戶號：2
	// F1 額度：3
	// F2 利變；4
	// F3 戶名；5
	// F4 本金餘額；6
	// F5 科目；7
	// F6 逾期數；8
	// F7 地區別；9
	// F8 繳息日期；10
	// F9分類項目:11
	// F4 五類金額(用F16區分F4)=；12~17
	// F10 分類標準(文字)；18
	// F11 金額 19
	// F12 備註；20
	// F13 基本利率代碼(商品代號)；21

	private void exportExcel(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM051Report exportExcel");
		if (LDList.size() > 0) {

			for (Map<String, String> tLDVo : LDList) {
				row++;
				// F0+F1:1
				makeExcel.setValue(row, 1, tLDVo.get("F0") + tLDVo.get("F1"));
				// F0 戶號：2
				makeExcel.setValue(row, 2, Integer.valueOf(tLDVo.get("F0")));
				// F1 額度：3
				makeExcel.setValue(row, 3, Integer.valueOf(tLDVo.get("F1")), "C");
				// F2 利變；4
				makeExcel.setValue(row, 4,
						tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? ' ' : tLDVo.get("F2"), "C");
				// F3 戶名；5
				makeExcel.setValue(row, 5, tLDVo.get("F3"), "L");
				// F4 本金餘額；6
				if (tLDVo.get("F4").equals("")) {
					makeExcel.setValue(row, 6, 0);
				} else {
					makeExcel.setValue(row, 6, Float.valueOf(tLDVo.get("F4")), "#,##0");
				}

				// F5 科目；7
				makeExcel.setValue(row, 7, Integer.valueOf(tLDVo.get("F5")), "L");
				// F6 逾期數；8
//				putTerm(row, tLDVo);

				String ovduText = "";
				if (!tLDVo.get("F18").isEmpty()) {
					if (Integer.valueOf(tLDVo.get("F6")) == 99) {
						// 協or協*or催協
						ovduText = tLDVo.get("F18");
					} else {
						// *協-逾期數
						ovduText = tLDVo.get("F18") + tLDVo.get("F6");
					}
				} else {
					// 逾期數
					ovduText = tLDVo.get("F6");
				}

				makeExcel.setValue(row, 8, ovduText, "C");
				// F7 地區別；9
				makeExcel.setValue(row, 9,
						tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? 0 : Integer.valueOf(tLDVo.get("F7")),
						"C");
				// F8 繳息日期；10
				makeExcel.setValue(row, 10,
						tLDVo.get("F8") == null || tLDVo.get("F8").length() == 0 ? 0 : Integer.valueOf(tLDVo.get("F8")),
						"C");
				// F9分類項目:11
//				makeExcel.setValue(row, 11, tLDVo.get("F9"), "C");
				// F4 五類金額(用F16區分F4)=；12~17
				putAsset(row, tLDVo.get("F4"), tLDVo.get("F16"));
				// F10 分類標準(文字)；18
				makeExcel.setValue(row, 17, tLDVo.get("F10").isEmpty() ? "核貸估價" : tLDVo.get("F10"), "L");
				// F11 金額 19
				makeExcel.setValue(row, 18, Integer.valueOf(tLDVo.get("F11")), "#,##0");
				// F12 備註；20
				makeExcel.setValue(row, 19, tLDVo.get("F12"), "L");
				// F13 基本利率代碼(商品代號)；21
				makeExcel.setValue(row, 20, tLDVo.get("F13"), "C");
			}
		} else

		{
			makeExcel.setValue(3, 1, "本日無資料");

		}
	}

	/**
	 * 資產五分類 欄位位置
	 * 
	 * @param row         列數
	 * @param Map<String, String>
	 * 
	 */

	private void putAsset(int row, String prinBalance, String assetClass) throws LogicException {
		int col = 0;
//		String memo = "";
		switch (assetClass) {
		case "21":
			col = 11;

			break;
		case "22":
			col = 12;
			break;
		case "23":
			col = 13;
			break;
		case "3":
			col = 14;
			break;
		case "4":
			col = 15;
			break;
		case "5":
			col = 16;
			break;
		default:
			col = 0;
			break;
		}
		if (col > 0) {
			makeExcel.setValue(row, col, Float.valueOf(prinBalance), "#,##0");
		}
	}

	/**
	 * 逾期數
	 * 
	 * @param row 列數
	 * @param
	 */
	private void putTerm(int row, Map<String, String> tLDVo) throws LogicException {
		String tmp = "";
		if (tLDVo.get("F17").equals("2")) {
			tmp = "催";
			if (tLDVo.get("F14").equals("2")) {
				tmp = tmp + "協";
			}
		} else if (tLDVo.get("F14").equals("2")) {
			tmp = "協";
		} else {
			tmp = tLDVo.get("F6");
		}
		makeExcel.setValue(row, 8, tmp, "C");
	}

	private void memoText(int typeNum) {
		String one1 = "有足額擔保";
		String one2 = "有擔保";
		String one3 = "無擔保";

		String two1 = "--但債信不良(" + typeNum + ")";
		String two2 = "--拍定後不足額(" + typeNum + ")";
		String two3 = "--逾繳12月以上(" + typeNum + ")";
		String two4 = "--逾繳7-12月(" + typeNum + ")";
		String two5 = "--協議後正常還款(" + typeNum + ")";

		String three1 = "--逾期";
		String three2 = "--正常繳息";
		String three3 = "--逾期未滿30日";

	}

	private void regalText() {
		String one = "核貸估價";

		String two = "協議戶";

	}

}
