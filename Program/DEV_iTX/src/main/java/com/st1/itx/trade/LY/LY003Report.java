package com.st1.itx.trade.LY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LY003ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("LY003Report")
@Scope("prototype")

public class LY003Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LY003Report.class);

	@Autowired
	public LY003ServiceImpl LY003ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
//		LY003ServiceImpl.getEntityManager(titaVo);
		try {

//			List<HashMap<String, String>> LY003List = LY003ServiceImpl.findAll();
//			if(LY003List.size() > 0){
			testExcel(titaVo);
//			  testExcel(titaVo, LY003List);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY003ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void testExcel(TitaVo titaVo, List<HashMap<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY003", "非RBC_表14-2_會計部年度檢查報表", "LY003-非RBC_表14-2_會計部年度檢查報表", "非RBC_表14-2_會計部年度檢查報表.xlsx", "表14-2");
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

	private void testExcel(TitaVo titaVo) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY003", "非RBC_表14-2_會計部年度檢查報表", "LY003-非RBC_表14-2_會計部年度檢查報表", "非RBC_表14-2_會計部年度檢查報表.xlsx", "表14-2");
		// makeExcel.setValue(5,1,"測試日期");
//		makeExcel.setSheet("201903-固定");

		makeExcel.setValue(8, 4, "66,666,666,666");
		makeExcel.setValue(8, 5, "11,111,111,111");
		makeExcel.setValue(8, 6, "16.66%");
		makeExcel.setValue(8, 8, "2,222,222");
		makeExcel.setValue(8, 9, "0.55%");
		makeExcel.setValue(8, 10, "19.9%");

		makeExcel.setValue(10, 4, "666,666,666");
		makeExcel.setValue(10, 5, "111,111,111");
		makeExcel.setValue(10, 6, "16,66%");
		makeExcel.setValue(10, 8, "2,222,222");
		makeExcel.setValue(10, 9, "0.55%");
		makeExcel.setValue(10, 10, "19.9%");

		makeExcel.setValue(13, 4, "67,333,332,332");
		makeExcel.setValue(13, 5, "11,222,222,222");
		makeExcel.setValue(13, 6, "16,66%");
		makeExcel.setValue(13, 8, "4,444,444");
		makeExcel.setValue(13, 9, "1.10%");
		makeExcel.setValue(13, 10, "39.8%");

		makeExcel.setValue(67, 4, "67,333,332,332");
		makeExcel.setValue(67, 5, "11,222,222,222");
		makeExcel.setValue(67, 6, "16,66%");
		makeExcel.setValue(67, 8, "4,444,444");
		makeExcel.setValue(67, 9, "1.10%");
		makeExcel.setValue(67, 10, "39.8%");

		makeExcel.setValue(83, 5, "11,222,222,222");
		makeExcel.setValue(83, 10, "11,222,222,222");
		makeExcel.setValue(83, 11, "11,222,222,222");
		makeExcel.setValue(88, 5, "11,222,222,222");
		makeExcel.setValue(88, 10, "11,222,222,222");
		makeExcel.setValue(88, 11, "11,222,222,222");
		makeExcel.setValue(89, 10, "11,222,222,222");
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
