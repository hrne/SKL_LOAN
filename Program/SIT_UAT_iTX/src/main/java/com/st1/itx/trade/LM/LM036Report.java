package com.st1.itx.trade.LM;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM036ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM036Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM036Report.class);

	@Autowired
	LM036ServiceImpl lM036ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM036", "第一類各項統計表_5份", "LM036第一類各項統計表_5份", "LM036第一類各項統計表_5份.xlsx", "Portfolio");
//		try {
//
//			LM036List = lM036ServiceImpl.findAll(titaVo);
//			
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.info("LM036ServiceImpl.testExcel error = " + errors.toString());
//		}
		List<Map<String, String>> LM036List = null;
		exportExcel(titaVo, LM036List);
		
		makeExcel.setSheet("Bad Rate-房貸(by件數)");
		List<Map<String, String>> LM036List1 = null;
		exportExcel1(titaVo, LM036List1);
		
		makeExcel.setSheet("Bad Rate-房貸(by金額)");
		List<Map<String, String>> LM036List2 = null;
		exportExcel2(titaVo, LM036List2);
		
		makeExcel.setSheet("Deliquency ");
		List<Map<String, String>> LM036List3 = null;
		exportExcel3(titaVo, LM036List3);
		
		makeExcel.setSheet("Collection");
		List<Map<String, String>> LM036List4 = null;
		exportExcel4(titaVo, LM036List4);
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if(LDList == null) {
			makeExcel.setValue(3, 3, "本日無資料");
		}
		String entdy = titaVo.get("ENTDY");
		int yy = Integer.parseInt(entdy) / 10000 + 1911;
		int mm = Integer.parseInt(entdy) % 10000 / 100;
		int dd[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		makeExcel.setValue(2, 15, yy + "/" + mm + "/" + dd[mm-1]);
		if(mm == 1) {
			makeExcel.setValue(2, 16, month[11] + " to " + month[0]);
		} else {
			makeExcel.setValue(2, 16, month[mm - 2] + " to " + month[mm - 1]);
		}
		int i = 1;
		int col = 15;
		while(i < 13) {
			if(mm - i == 0) {
				yy -= 1;
				mm += 12;
			}
			makeExcel.setValue(2, col - i, yy + "/" + (mm - i) + "/" + dd[mm - i - 1]);
			i++;
		}
		
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
	private void exportExcel1(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if(LDList == null) {
			makeExcel.setValue(4, 2, "本日無資料");
		}
		String entdy = titaVo.get("ENTDY");
		int ym = Integer.parseInt(entdy) / 100;
		for(int i = 0 ; i < 37 ; i++) {
			makeExcel.setValue(2, 40 - i, ym);
			makeExcel.setValue(43 - i, 1, ym);
			if((ym - 1) % 100 == 0) {
				ym -= 89;
			} else {
				ym -= 1;
			}
		}
		for(int i = 0 ; i < 3 ; i++) {
			makeExcel.setValue(6 - i, 1, ym);
			if((ym - 1) % 100 == 0) {
				ym -= 89;
			} else {
				ym -= 1;
			}
		}
	}
	private void exportExcel2(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if(LDList == null) {
			makeExcel.setValue(4, 2, "本日無資料");
		}
		String entdy = titaVo.get("ENTDY");
		int ym = Integer.parseInt(entdy) / 100;
		for(int i = 0 ; i < 37 ; i++) {
			makeExcel.setValue(2, 40 - i, ym);
			makeExcel.setValue(43 - i, 1, ym);
			if((ym - 1) % 100 == 0) {
				ym -= 89;
			} else {
				ym -= 1;
			}
		}
		for(int i = 0 ; i < 3 ; i++) {
			makeExcel.setValue(6 - i, 1, ym);
			if((ym - 1) % 100 == 0) {
				ym -= 89;
			} else {
				ym -= 1;
			}
		}
	}
	private void exportExcel3(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if(LDList == null) {
			makeExcel.setValue(3, 2, "本日無資料");
		}
		String entdy = titaVo.get("ENTDY");
		int yy = Integer.parseInt(entdy) / 10000 + 1911;
		int mm = Integer.parseInt(entdy) % 10000 / 100;
		int dd[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		makeExcel.setValue(2, 29, yy + "/" + mm + "/" + dd[mm-1]);
		int i = 1;
		int col = 29;
		while(i < 28) {
			if(mm - i == 0) {
				yy -= 1;
				mm += 12;
			}
			makeExcel.setValue(2, col - i, yy + "/" + (mm - i) + "/" + dd[mm - i - 1]);
			i++;
		}
	}
	private void exportExcel4(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if(LDList == null) {
			makeExcel.setValue(4, 2, "本日無資料");
		}
	}
//	private void testExcel(TitaVo titaVo) throws LogicException {
//		this.info("===========in testExcel");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM036", "第一類各項統計表", "LM036-Dashboard_第一類各項統計表_5份", "Dashboard_第一類各項統計表_5份.xlsx", "Portfolio");
//
//		makeExcel.setValue(3, 3, "55,555");
//		makeExcel.setValue(3, 4, "55,555");
//		makeExcel.setValue(4, 3, "44,444");
//		makeExcel.setValue(4, 4, "44,444");
//		makeExcel.setValue(5, 3, "33,333");
//		makeExcel.setValue(5, 4, "33,333");
//		makeExcel.setValue(6, 3, "2,222");
//		makeExcel.setValue(6, 4, "2,222");
//		makeExcel.setValue(7, 3, "9");
//		makeExcel.setValue(7, 4, "9");
//		makeExcel.setValue(16, 3, "81%");
//		makeExcel.setValue(16, 4, "81%");
//
//		makeExcel.setValue(31, 3, "1.66%");
//		makeExcel.setValue(31, 4, "1.33%");
//		makeExcel.setValue(32, 3, "1.08%");
//		makeExcel.setValue(32, 4, "1.12%");
//		makeExcel.setValue(33, 3, "1.13%");
//		makeExcel.setValue(33, 4, "1.21%");
//
//		makeExcel.setSheet("Bad Rate-房貸(by件數)");
//		makeExcel.setValue(4, 2, "100");
//		makeExcel.setValue(5, 2, "100");
//		makeExcel.setValue(6, 42, "200");
//		makeExcel.setValue(44, 2, "100");
//		makeExcel.setValue(44, 42, "200");
//		makeExcel.setValue(46, 3, "1.66%");
//
//		makeExcel.setSheet("Bad Rate-房貸(by金額)");
//		makeExcel.setValue(4, 2, "33,333,333,333");
//		makeExcel.setValue(5, 2, "33,333,333,333");
//		makeExcel.setValue(6, 42, "66,666,666,666");
//		makeExcel.setValue(44, 2, "66,666,666,666");
//		makeExcel.setValue(44, 42, "66,666,666,666");
//		makeExcel.setValue(49, 4, "0.02%");
//
//		makeExcel.setSheet("Deliquency ");
//		makeExcel.setValue(4, 2, "5,555,555");
//		makeExcel.setValue(4, 3, "5,555,555");
//		makeExcel.setValue(5, 2, "44,444");
//		makeExcel.setValue(5, 3, "44,444");
//		makeExcel.setValue(6, 2, "33,333");
//		makeExcel.setValue(6, 3, "33,333");
//		makeExcel.setValue(7, 2, "22,222,222");
//		makeExcel.setValue(7, 3, "22,222,222");
//
//		makeExcel.setSheet("Collection");
//		makeExcel.setValue(4, 2, "1.22%");
//		makeExcel.setValue(4, 3, "2.13%");
//		makeExcel.setValue(4, 26, "1.675%");
//
//		makeExcel.setValue(6, 2, "2.22%");
//		makeExcel.setValue(6, 3, "0.89%");
//		makeExcel.setValue(6, 26, "1.555%");
//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}

}
