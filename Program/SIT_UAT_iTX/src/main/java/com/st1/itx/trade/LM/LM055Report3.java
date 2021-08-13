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

public class LM055Report3 extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM055Report3.class);

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
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM055", "A042放款餘額彙總表", "LM055-A042放款餘額彙總表_工作表", "A042放款餘額彙總表_工作表.xlsx", "LNM34AP");
		if(LDList.size() == 0) {
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
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM055", "A042放款餘額彙總表", "LM055-A042放款餘額彙總表_工作表", "A042放款餘額彙總表_工作表.xlsx", "LNM34AP");
//
//		makeExcel.setValue(1, 14, "2,000,000");
//
//		makeExcel.setValue(2, 1, "5316");
//		makeExcel.setValue(2, 2, "321");
//		makeExcel.setValue(2, 4, "6");
//		makeExcel.setValue(2, 5, "33123");
//		makeExcel.setValue(2, 6, "1");
//		makeExcel.setValue(2, 7, "10603031");
//		makeExcel.setValue(2, 8, "1");
//		makeExcel.setValue(2, 9, "20121212");
//		makeExcel.setValue(2, 10, "20121230");
//		makeExcel.setValue(2, 11, "20421212");
//		makeExcel.setValue(2, 12, "20421212");
//		makeExcel.setValue(2, 13, "12,000,000");
//		makeExcel.setValue(2, 14, "2,000,000");
//		makeExcel.setValue(2, 15, "1,212");
//		makeExcel.setValue(2, 16, "-");
//		makeExcel.setValue(2, 17, "0.0163");
//		makeExcel.setValue(2, 18, "0");
//		makeExcel.setValue(2, 19, "0");
//		makeExcel.setValue(2, 20, "6000");
//		makeExcel.setValue(2, 21, "25");
//		makeExcel.setValue(2, 22, "301");
//		makeExcel.setValue(2, 23, "A6");
//		makeExcel.setValue(2, 24, "2");
//		makeExcel.setValue(2, 25, "1");
//		makeExcel.setValue(2, 26, "B");
//		makeExcel.setValue(2, 29, "330");
//		makeExcel.setValue(2, 31, "C");
//		makeExcel.setValue(2, 32, "44444");
//
//		makeExcel.setSheet("資產分類_09");
//
//		makeExcel.setValue(3, 1, "54632");
//		makeExcel.setValue(3, 2, "3211");
//		makeExcel.setValue(3, 3, "5");
//		makeExcel.setValue(3, 6, "544,444");
//		makeExcel.setValue(3, 7, "330");
//		makeExcel.setValue(3, 8, "1");
//		makeExcel.setValue(3, 9, "80");
//		makeExcel.setValue(3, 10, "20191212");
//		makeExcel.setValue(3, 10, "2");
//		makeExcel.setValue(6, 5, "1");
//		makeExcel.setValue(6, 6, "544,444");
//		makeExcel.setValue(7, 6, "544,444");
//		makeExcel.setValue(8, 6, "144,444");
//		makeExcel.setValue(9, 6, "400,000");
//		makeExcel.setValue(10, 6, "544,444");
//
//		makeExcel.setSheet("會計科目(短、中、長期)折溢價攤銷數(總表)");
//		makeExcel.setValue(6, 3, "999,999");
//		makeExcel.setValue(7, 3, "69,999,999");
//		makeExcel.setValue(9, 3, "70,999,998");
//		makeExcel.setValue(12, 3, "9,999");
//		makeExcel.setValue(14, 3, "9,999");
//		makeExcel.setValue(16, 3, "71,009,997");
//
//		makeExcel.setSheet("la$w30p_09");
//		makeExcel.setValue(2, 1, "45678");
//		makeExcel.setValue(2, 2, "555");
//		makeExcel.setValue(2, 3, "1");
//		makeExcel.setValue(2, 4, "1");
//		makeExcel.setValue(2, 8, "330");
//		makeExcel.setValue(2, 10, "C");
//
//		makeExcel.setValue(3, 1, "11234");
//		makeExcel.setValue(3, 2, "666");
//		makeExcel.setValue(3, 3, "6");
//		makeExcel.setValue(3, 4, "6");
//		makeExcel.setValue(3, 8, "330");
//		makeExcel.setValue(3, 10, "Z");
//
//		makeExcel.setSheet("D9210081_09");
//
//		makeExcel.setValue(1, 8, "1,111,111");
//		makeExcel.setValue(1, 6, "5,555");
//		makeExcel.setValue(2, 1, "33,444,555");
//		makeExcel.setValue(2, 2, "2");
//		makeExcel.setValue(2, 4, "1122334");
//		makeExcel.setValue(2, 5, "2");
//		makeExcel.setValue(2, 7, "20200202");
//		makeExcel.setValue(2, 8, "1,111,111");
//
//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}

}
