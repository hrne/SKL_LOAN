package com.st1.itx.trade.LP;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LP001ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("LP001Report2")
@Scope("prototype")

public class LP001Report2 extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LP001Report2.class);

	@Autowired
	public LP001ServiceImpl LP001ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
//		LP001ServiceImpl.getEntityManager(titaVo);
		try {

//			List<Map<String, String>> LP001List = LP001ServiceImpl.findAll();
//			if(LP001List.size() > 0){
			testExcel(titaVo);
//			  testExcel(titaVo, LP001List);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP001ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void testExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP001", "工作月區域中心業績累計", "LP001工作月區域中心業績累計", "區域中心業績累計.xls", "10805");
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
//		for (Map<String, String> tLDVo : LDList) {
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

	private void testExcel(TitaVo titaVo) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP001", "工作月區域中心業績累計", "LP001工作月區域中心業績累計", "區域中心業績累計.xls", "10805");
		makeExcel.setValue(5, 3, "36");
		makeExcel.setValue(5, 4, "123,947,891");
		makeExcel.setValue(5, 5, "42");
		makeExcel.setValue(5, 6, "113,800,222");
		makeExcel.setValue(5, 7, "41");
		makeExcel.setValue(5, 8, "113,064,062");
		makeExcel.setValue(5, 9, "42");
		makeExcel.setValue(5, 10, "104,329,317");
		makeExcel.setValue(5, 11, "29");
		makeExcel.setValue(5, 12, "95,634,846");

		makeExcel.setValue(6, 3, "16");
		makeExcel.setValue(6, 4, "46,134,385");
		makeExcel.setValue(6, 5, "22");
		makeExcel.setValue(6, 6, "59,689,081");
		makeExcel.setValue(6, 7, "17");
		makeExcel.setValue(6, 8, "57,496,534");
		makeExcel.setValue(6, 9, "19");
		makeExcel.setValue(6, 10, "58,908,955");
		makeExcel.setValue(6, 11, "23");
		makeExcel.setValue(6, 12, "43,382,644");

		makeExcel.setValue(7, 3, "18");
		makeExcel.setValue(7, 4, "53,498,541");
		makeExcel.setValue(7, 5, "21");
		makeExcel.setValue(7, 6, "36,750,000");
		makeExcel.setValue(7, 7, "28");
		makeExcel.setValue(7, 8, "46,490,000");
		makeExcel.setValue(7, 9, "24");
		makeExcel.setValue(7, 10, "54,230,000");
		makeExcel.setValue(7, 11, "23");
		makeExcel.setValue(7, 12, "55,770,000");
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
