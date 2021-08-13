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
import com.st1.itx.db.service.springjpa.cm.LM048ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM048Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM048Report.class);

	@Autowired
	LM048ServiceImpl lM048ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> LM048List = null;
		try {
			LM048List = lM048ServiceImpl.findAll(titaVo);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM048ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(titaVo, LM048List);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("LM048Report exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM048", "放款企業放款風險承擔限額控管表_限額控管", "LM048放款企業放款風險承擔限額控管表_限額控管", "放款企業放款風險承擔限額控管表_限額控管.xlsx", "明細總表108.04");
		String yymm = titaVo.get("ENTDY");
		makeExcel.setSheet("明細總表108.04", "明細總表" + yymm.substring(1, 4) + "." + yymm.substring(4, 6));
		makeExcel.setValue(2, 6, showYear(yymm));
		if (LDList.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		} else {
			int row = 5;
			String lastIndustryItem = "";
			int set = 1;//第一筆資料設定行業名
			int listSize = 0;
			BigDecimal oneLineAmtTotal = new BigDecimal("0");//單一行業核貸金額合計
			BigDecimal oneLoanBalanceTotal = new BigDecimal("0");//單一行業放款本金餘額合計
			BigDecimal LineAmtTotal = new BigDecimal("0");//核貸金額合計
			BigDecimal LoanBalanceTotal = new BigDecimal("0");//放款本金餘額合計
			for (Map<String, String> tLDVo : LDList) {
				listSize++;
				String ad = "";
				int col = 0;
				for (int i = 0; i < tLDVo.size(); i++) {

					ad = "F" + String.valueOf(col);
					col++;
					switch (col) {
					case 1:
						if(set == 1) {
							makeExcel.setValue(row, col + 1, tLDVo.get("F0"));
							set = 0;
						}else {
							if(! tLDVo.get("F0").equals(lastIndustryItem)) {//如果行業名與上一筆資料的不同的話先印單一行業小計
								makeExcel.setValue(row, 3, "單一行業小計");
								makeExcel.setValue(row, 5, oneLineAmtTotal, "#,##0");
								LineAmtTotal = LineAmtTotal.add(oneLineAmtTotal);
								makeExcel.setValue(row, 6, oneLoanBalanceTotal, "#,##0");
								LoanBalanceTotal = LoanBalanceTotal.add(oneLoanBalanceTotal);
								row++;
								oneLineAmtTotal = BigDecimal.ZERO;
								oneLoanBalanceTotal = BigDecimal.ZERO;
								makeExcel.setValue(row, col + 1, tLDVo.get("F0"));
							}else {
								
							}
						}
						lastIndustryItem = tLDVo.get("F0");
						break;
					case 4:
					case 5:
						if(col == 4) {
							oneLineAmtTotal = oneLineAmtTotal.add(tLDVo.get("F3") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3")));
						}
						if(col == 5) {
							oneLoanBalanceTotal = oneLoanBalanceTotal.add(tLDVo.get("F4") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F4")));
						}
						// 金額
						makeExcel.setValue(row, col+1, new BigDecimal(tLDVo.get(ad)), "#,##0");
						break;
					case 6:
						makeExcel.setValue(row, col+1, new BigDecimal(tLDVo.get(ad)), "0.0000");
						break;
					case 7:
					case 8:
						makeExcel.setValue(row, col + 1, showDate(tLDVo.get(ad)));
						break;
					default:
						makeExcel.setValue(row, col + 1, tLDVo.get(ad));
						break;
					}
				} // for
				if(listSize == LDList.size()) {
					row++;
					makeExcel.setValue(row, 3, "單一行業小計");
					makeExcel.setValue(row, 5, oneLineAmtTotal, "#,##0");
					LineAmtTotal = LineAmtTotal.add(oneLineAmtTotal);
					makeExcel.setValue(row, 6, oneLoanBalanceTotal, "#,##0");
					LoanBalanceTotal = LoanBalanceTotal.add(oneLoanBalanceTotal);
				}
				row++;
			} // for
			makeExcel.setValue(row, 2, "總合計");
			makeExcel.setValue(row, 5, LineAmtTotal, "#,##0");
			makeExcel.setValue(row, 6, LoanBalanceTotal, "#,##0");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
//		makeExcel.setValue(5,1,"測試帳號");
//		makeExcel.setValue(1,1,"測試2");
//		String inf = "經辦;房貸專員;戶名;戶號;額度;"  // 24
//				+ "撥款;撥款日;利率代碼;計件代碼;是否計件;"
//				+ "撥款金額;部室代號;區部代號;單位代號;部室;"
//				+ "區部;單位;員工代號;介紹人;處經理;"
//				+ "區經理;換算業績;業務報酬;業績金額";
//		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23";
//		
//		String inf1[] = inf.split(";");
//		String txt1[] = txt.split(";") ;
//		int i = 1;
//		this.info("-----------------" + LDList);
//		for (HashMap<String, String> tLDVo : LDList) {
//			for( int j = 1 ; j <= tLDVo.size(); j++) {
//				if( i == 1) {
//					makeExcel.setValue(i, j, inf1[j-1]);
//				} else {
//					if(tLDVo.get(txt1[j-1]) == null) {
//						makeExcel.setValue(i, j, "");
//					} else {
//						this.info("->" + tLDVo.get(txt1[j-1]));
//						makeExcel.setValue(i, j, tLDVo.get(txt1[j-1]));
//					}
//				} // else 
//			}		
//			i++;
//		}
	}
	public String showDate(String date) {
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		int Cyear = (Integer.parseInt(date) - 19110000) / 10000;
		return String.valueOf(Cyear) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
	}
	public String showYear(String date) {
		if (date == null || date.equals("") || date.equals("0")) {
			return "";
		}
		return date.substring(1, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6, 8) + "日";
	}
//	private void testExcel(TitaVo titaVo) throws LogicException {
//		this.info("===========in testExcel");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM048", "放款企業放款風險承擔限額控管表_限額控管", "LM048放款企業放款風險承擔限額控管表_限額控管", "放款企業放款風險承擔限額控管表_限額控管.xlsx", "明細總表108.04");
//		// makeExcel.setValue(5,1,"測試日期");
////		makeExcel.setSheet("201903-固定");
//
//		makeExcel.setValue(12, 3, "456941");
//		makeExcel.setValue(12, 4, "台xxx信託基金");
//		makeExcel.setValue(12, 5, "1,354,456");
//		makeExcel.setValue(12, 6, "500,024");
//		makeExcel.setValue(12, 7, "1.5200");
//		makeExcel.setValue(12, 8, "1091231");
//		makeExcel.setValue(12, 9, "1090331");
//
//		makeExcel.setValue(13, 5, "1,354,456");
//		makeExcel.setValue(13, 6, "500,024");
//		makeExcel.setValue(13, 12, "0.83%");
//		makeExcel.setValue(13, 13, "1.51%");
//
//		makeExcel.setValue(70, 5, "1,354,456");
//		makeExcel.setValue(70, 6, "500,024");
//		makeExcel.setValue(70, 12, "0.83%");
//		makeExcel.setValue(70, 13, "1.51%");
//		makeExcel.setValue(77, 5, "163,187,469");
//		makeExcel.setValue(78, 5, "60,243,975");
//
//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}

}
