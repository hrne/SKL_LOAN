package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM025ServiceImpl;

import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("lM025Report")
@Scope("prototype")

public class LM025Report extends MakeReport {

	@Autowired
	LM025ServiceImpl lM025ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM025", "減損系統有效利率資料查核", "LM025減損系統有效利率資料查核", "LM025_底稿_減損系統有效利率資料查核.xls", "201903-固定");

		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) + 19110000) / 100);

		makeExcel.setSheet("201903-固定", entdy + "-固定");
		exportExcel(titaVo, 0);

		makeExcel.setSheet("201903-浮動", entdy + "-浮動");
		exportExcel(titaVo, 1);

		long sno = makeExcel.close();
		//makeExcel.toExcel(sno);
	}

	/**
	 * 執行EXCEL
	 * 
	 * @param titaVo
	 * @param caseType 0=固定,1=浮動
	 */
	private void exportExcel(TitaVo titaVo, int caseType) throws LogicException {
		this.info("===========in exportExcel");

		List<Map<String, String>> lm025List = null;

		try {

			lm025List = lM025ServiceImpl.findAll(titaVo, caseType);

		} catch (Exception e) {

			this.error("lM025ServiceImpl.findAll error = " + e.getMessage());

		}

		int row = 1;
		String ad = "";
		if (lm025List != null && lm025List.size() != 0) {
			for (Map<String, String> tLDVo : lm025List) {
				row++;

				for (int i = 0; i < tLDVo.size(); i++) {
					ad = "F" + String.valueOf(i);
					switch (i) {
					case 0:
						makeExcel.setValue(row, 1, tLDVo.get(ad), "R");
						break;
					case 1:
						makeExcel.setValue(row, 2, tLDVo.get(ad), "L");
						break;
					case 2:
						makeExcel.setValue(row, 3, tLDVo.get(ad), "R");
						break;
					case 3:
						makeExcel.setValue(row, 4, tLDVo.get(ad) == null ? "" : new BigDecimal(tLDVo.get(ad)), "R");

						break;
					case 4:
						makeExcel.setValue(row, 5, tLDVo.get(ad) == null ? "" : new BigDecimal(tLDVo.get(ad)), "#,##0", "R");
						break;
					default:

						break;
					}
				}

			}
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

	}

//	public String padString(String result, int pad) {// 前面補0，ex:001 -> 0000001
//		String s = "";
//		if (result.length() >= pad) {
//			return result;
//		} else {
//			int count = pad - result.length();
//			for (int i = 0; i < count; i++) {
//				s += "0";
//			}
//			s += result;
//			return s;
//		}
//	}

//	public void DoExcel(List<Map<String, String>> Data, String yyyymm, int CaseType) throws LogicException {
//		String strRateCode = "";
//		// CaseType 1:固定 2:機動
//		switch (CaseType) {
//		case 1:
//			strRateCode = "固定";
//			break;
//		case 2:
//			strRateCode = "浮動";
//			break;
//		}
//		int assignRow = 2;
//
//		String newSheetName = yyyymm + "-" + strRateCode;
//
//		if (Data != null && Data.size() != 0) {
//			for (int j = 0; j < Data.size(); j++) {
//				int StartRow = assignRow + j;
//				String rate = Data.get(j).get("F0").trim();// 利率
//				String count = Data.get(j).get("F1").trim();// 筆數
//				String sum = Data.get(j).get("F2").trim();// 金額合計
//				if (rate != null && rate.length() != 0) {
//
//				} else {
//					rate = "0";
//				}
//				if (count != null && count.length() != 0) {
//
//				} else {
//					count = "0";
//				}
//				if (sum != null && sum.length() != 0) {
//
//				} else {
//					sum = "0";
//				}
//				makeExcel.setValue(row, col, new BigDecimal(tLDVo.get(ad).toString()), "###0.00");
//				makeExcel.setValue(StartRow, 1, new BigDecimal(rate), "###0.00");
//				makeExcel.setValue(StartRow, 2, new BigDecimal(count), "#,##0");
//				makeExcel.setValue(StartRow, 3, new BigDecimal(sum), "#,##0");
//			}
//
//		}
//	}
//
//	String NumToCh[] = { "○", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
//
//	public String NumToChYYYY(String yyyy) {
//		String Ans = "";
//		if (yyyy != null) {
//			int yyyyL = yyyy.length();
//			if (yyyyL != 0) {
//				if (yyyyL == 4) {
//					yyyy = String.valueOf(Integer.parseInt(yyyy) - 1911);
//					yyyyL = yyyy.length();
//				}
//				for (int i = 0; i < yyyyL; i++) {
//					Ans += NumToCh[Integer.parseInt(yyyy.substring(i, i + 1))];
//				}
//			}
//		}
//		return Ans;
//	}
//
//	public String NumToChMM(String mm) {
//		int mmInt = Integer.parseInt(mm);
//		String Ans = "";
//		if (mmInt <= 13) {
//			Ans = NumToCh[mmInt] + "月" + MonthToDate(mm) + "日";
//		} else {
//			Ans = (mmInt - 1) + "月" + MonthToDate(mm) + "日";
//		}
//
//		return Ans;
//	}
//
//	public String MonthToDate(String mm) {
//		String Date = "";
//		int mmInt = Integer.parseInt(mm);
//		if (mmInt == 1 || mmInt == 3 || mmInt == 5 || mmInt == 7 || mmInt == 8 || mmInt == 10 || mmInt == 12) {
//			// 1 3 5 7 9 11
//			Date = "三十一";
//		} else {
//			if (mmInt == 2) {
//
//			} else {
//				Date = "三十";
//			}
//		}
//		return Date;
//	}
}
