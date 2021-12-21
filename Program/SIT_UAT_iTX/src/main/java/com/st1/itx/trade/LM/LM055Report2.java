package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM055ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM055Report2 extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM055Report2.class);

	@Autowired
	LM055ServiceImpl lM055ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> LM055List = new ArrayList<>();
		try {
			LM055List = lM055ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM055ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(titaVo, LM055List);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("LM055Report exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM055", "A042放款餘額彙總表", "LM055-A042放款餘額彙總表", "A042放款餘額彙總表_手搞.xlsx", "A042填報說明");
		if (LDList.size() == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
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

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

//	private void testExcel(TitaVo titaVo) throws LogicException {
//		this.info("===========in testExcel");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM055", "A042放款餘額彙總表", "LM055-A042放款餘額彙總表_手搞", "A042放款餘額彙總表_手搞.xlsx", "A042填報說明");
//
//		makeExcel.setSheet("A042放款餘額彙總表");
//
////		makeExcel.setValue(10,6, "56,555,646");
////		makeExcel.setValue(10,7, "20,254,556");
////		makeExcel.setValue(10,8, "44,444,444");
////		makeExcel.setValue(10,9, "35,365,555");
////		makeExcel.setValue(10,10, "9,999,999");
////		makeExcel.setValue(10,13, "45,545,555");
////		makeExcel.setValue(10,14, "12,235,554");
////		makeExcel.setValue(10,15, "4,996,665");
//
//		makeExcel.setSheet("LNM34AP");
//		makeExcel.setValue(1, 6, "3,333,333");
//		makeExcel.setValue(2, 1, "5316");
//		makeExcel.setValue(2, 2, "45678");
//		makeExcel.setValue(2, 4, "6");
//		makeExcel.setValue(2, 5, "1");
//		makeExcel.setValue(2, 6, "3,333,333");
//		makeExcel.setValue(2, 7, "0");
//		makeExcel.setValue(2, 8, "0");
//		makeExcel.setValue(2, 9, "25");
//		makeExcel.setValue(2, 10, "A6");
//		makeExcel.setValue(2, 13, "330");
//		makeExcel.setValue(2, 15, "C");
//
//		makeExcel.setSheet("分析數");
//		makeExcel.setValue(2, 1, "加總 -  246,972,826.0 ");
//		makeExcel.setValue(11, 1, "加總 -  246,972,826.0 ");
//
//		makeExcel.setValue(4, 2, "1,111,111");
//		makeExcel.setValue(4, 3, "2,222,222");
//		makeExcel.setValue(4, 4, "3,333,333");
//		makeExcel.setValue(4, 5, "4,444,444");
//		makeExcel.setValue(4, 6, "5,555,555");
//		makeExcel.setValue(4, 7, "6,666,666");
//		makeExcel.setValue(4, 8, "7,777,777");
//		makeExcel.setValue(4, 9, "8,888,888");
//		makeExcel.setValue(4, 16, "100,000");
//		makeExcel.setValue(4, 17, "200,000");
//		makeExcel.setValue(4, 18, "300,000");
//		makeExcel.setValue(4, 19, "400,000");
//		makeExcel.setValue(4, 20, "500,000");
//		makeExcel.setValue(4, 21, "600,000");
//		makeExcel.setValue(4, 22, "700,000");
//		makeExcel.setValue(4, 23, "800,000");
//		makeExcel.setValue(4, 25, "3,600,000");
//
//		makeExcel.setValue(8, 16, "100,000");
//		makeExcel.setValue(8, 17, "200,000");
//		makeExcel.setValue(8, 18, "300,000");
//		makeExcel.setValue(8, 19, "400,000");
//		makeExcel.setValue(8, 20, "500,000");
//		makeExcel.setValue(8, 21, "600,000");
//		makeExcel.setValue(8, 22, "700,000");
//		makeExcel.setValue(8, 23, "800,000");
//		makeExcel.setValue(8, 25, "3,600,000");
//
//		makeExcel.setValue(13, 2, "40,400,000");
//		makeExcel.setValue(13, 3, "44,500,000");
//		makeExcel.setValue(13, 4, "1");
//		makeExcel.setValue(13, 5, "47,000,000");
//
//		makeExcel.setValue(13, 16, "40,400,000");
//		makeExcel.setValue(13, 17, "44,500,000");
//		makeExcel.setValue(13, 18, "1");
//		makeExcel.setValue(13, 19, "47,000,000");
//		makeExcel.setValue(13, 21, "131,900,000");
//
//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}

}
