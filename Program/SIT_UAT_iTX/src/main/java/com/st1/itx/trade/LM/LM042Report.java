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
import com.st1.itx.db.service.springjpa.cm.LM042ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM042Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM042Report.class);

	@Autowired
	LM042ServiceImpl lM042ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM042", "RBC表_會計部", "LM042-RBC表_會計部_共三份", "LM042RBC表_會計部_共三份.xlsx", "10804RBC");
		String entdy = titaVo.get("ENTDY");
		int ym = Integer.parseInt(entdy) / 100;
//		try {
//
//			
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.info("LM042ServiceImpl.testExcel error = " + errors.toString());
//		}
		List<Map<String, String>> LM042List = null;
		exportExcel(titaVo, LM042List);
		makeExcel.setSheet("10804RBC", String.valueOf(ym) + "RBC");

		makeExcel.setSheet("明細表");
		List<Map<String, String>> LM042List1 = null;
		exportExcel1(titaVo, LM042List1);

		makeExcel.setSheet("RBC工作表");
		List<Map<String, String>> LM042List2 = null;
		exportExcel2(titaVo, LM042List2);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");

		if (LDList == null) {
			makeExcel.setValue(6, 3, "本日無資料");
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
		if (LDList == null) {
			makeExcel.setValue(5, 3, "本日無資料");
		}
	}

	private void exportExcel2(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if (LDList == null) {
			makeExcel.setValue(6, 3, "本日無資料");
		}
	}
//	private void testExcel(TitaVo titaVo) throws LogicException {
//		this.info("===========in testExcel");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM042", "RBC表_會計部", "LM042-RBC表_會計部_共三份", "RBC表_會計部_共三份.xlsx", "10804RBC");
//
//		String day = String.valueOf(titaVo.getEntDyI());
//		this.info("===========day=" + day);
//		makeExcel.setValue(2, 1, day.substring(0, 3) + "/" + day.substring(3, 5) + "/" + day.substring(5, 7));
//
//		makeExcel.setValue(7, 3, "0.0066");
//		makeExcel.setValue(7, 4, "40,000,000,000");
//		makeExcel.setValue(7, 5, "4,000,000,000");
//
//		makeExcel.setValue(11, 3, "0.0066");
//		makeExcel.setValue(11, 4, "40,000,000,000");
//		makeExcel.setValue(11, 5, "4,000,000,000");
//
//		makeExcel.setValue(13, 3, "0.0066");
//		makeExcel.setValue(13, 4, "40,000,000,000");
//		makeExcel.setValue(13, 5, "4,000,000,000");
//
//		makeExcel.setValue(15, 3, "0.0066");
//		makeExcel.setValue(15, 4, "40,000,000,000");
//		makeExcel.setValue(15, 5, "4,000,000,000");
//
//		makeExcel.setValue(16, 3, "0.0132");
//		makeExcel.setValue(16, 4, "80,000,000,000");
//		makeExcel.setValue(16, 5, "8,000,000,000");
//
//		makeExcel.setValue(17, 4, "200,000,000");
//		makeExcel.setValue(17, 5, "100,000,000");
//
//		makeExcel.setValue(25, 4, "80,000,000,000");
//		makeExcel.setValue(25, 5, "8,000,000,000");
//
//		makeExcel.setSheet("明細表");
//		makeExcel.setValue(2, 2, day.substring(0, 3) + "/" + day.substring(3, 5) + "/" + day.substring(5, 7));
//
//		makeExcel.setValue(5, 3, "44,444,444,321");
//		makeExcel.setValue(6, 3, "0");
//		makeExcel.setValue(7, 3, "0");
//		makeExcel.setValue(8, 3, "0");
//		makeExcel.setValue(9, 3, "44,444,444,321");
//		makeExcel.setValue(11, 3, "0");
//		makeExcel.setValue(12, 3, "10,000,000,000");
//		makeExcel.setValue(13, 3, "10,000,000,000");
//		makeExcel.setValue(14, 3, "54,444,444,321");
//		makeExcel.setValue(27, 3, "54,444,444,321");
//		makeExcel.setValue(27, 5, "54,444,444,321");
//		makeExcel.setValue(28, 5, "1,524,017,534");
//
//		makeExcel.setSheet("RBC工作表");
//		makeExcel.setValue(2, 1, day.substring(0, 3) + "/" + day.substring(3, 5) + "/" + day.substring(5, 7));
//
//		makeExcel.setValue(6, 3, "40,000,000,000");
//		makeExcel.setValue(7, 3, "10,000,000,000");
//		makeExcel.setValue(8, 3, "20,000,000,000");
//		makeExcel.setValue(9, 3, "10,000,000,000");
//		makeExcel.setValue(22, 3, "40,000,000,000");
//		makeExcel.setValue(27, 3, "40,000,000,000");
//
//		makeExcel.setValue(6, 5, "40,000,000,000");
//		makeExcel.setValue(7, 5, "10,000,000,000");
//		makeExcel.setValue(8, 5, "20,000,000,000");
//		makeExcel.setValue(9, 5, "10,000,000,000");
//		makeExcel.setValue(22, 5, "40,000,000,000");
//		makeExcel.setValue(27, 5, "40,000,000,000");
//
//		makeExcel.setValue(6, 6, "50,000,000,000");
//		makeExcel.setValue(6, 7, "51,000,000,000");
//
//		makeExcel.setValue(7, 6, "50,000,000,000");
//		makeExcel.setValue(7, 7, "51,000,000,000");
//		makeExcel.setValue(22, 6, "50,000,000,000");
//		makeExcel.setValue(22, 7, "51,000,000,000");
//		makeExcel.setValue(27, 6, "50,000,000,000");
//		makeExcel.setValue(27, 7, "51,000,000,000");
//
//		makeExcel.setValue(41, 3, "40,000,000,000");
//		makeExcel.setValue(41, 5, "40,000,000,000");
//		makeExcel.setValue(41, 6, "50,000,000,000");
//		makeExcel.setValue(41, 7, "51,000,000,000");
//		makeExcel.setValue(42, 5, "40,000,000,000");
//		makeExcel.setValue(42, 6, "50,000,000,000");
//		makeExcel.setValue(42, 7, "51,000,000,000");
//		makeExcel.setValue(44, 5, "40,000,000,000");
//
//		makeExcel.setValue(6, 8, "2.91%");
//		makeExcel.setValue(6, 9, "1,000,000,000");
//		makeExcel.setValue(6, 10, "2.84%");
//		makeExcel.setValue(6, 11, "1,000,000,000");
//
//		makeExcel.setValue(41, 8, "2.91%");
//		makeExcel.setValue(41, 9, "1,000,000,000");
//		makeExcel.setValue(41, 10, "2.84%");
//		makeExcel.setValue(41, 11, "1,000,000,000");
//
//		makeExcel.setValue(4, 13, "4,000,000,000");
//		makeExcel.setValue(6, 13, "3,333,333,333");
//
//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}

}
