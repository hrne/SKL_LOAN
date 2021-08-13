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
import com.st1.itx.db.service.springjpa.cm.LY004ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("LY004Report")
@Scope("prototype")

public class LY004Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LY004Report.class);

	@Autowired
	public LY004ServiceImpl LY004ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
//		LY004ServiceImpl.getEntityManager(titaVo);
		try {

//			List<HashMap<String, String>> LY004List = LY004ServiceImpl.findAll();
//			if(LY004List.size() > 0){
			testExcel(titaVo);
//			  testExcel(titaVo, LY004List);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY004ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void testExcel(TitaVo titaVo, List<HashMap<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY004", "非RBC_表14-4_會計部年度檢查報表", "LY004-非RBC_表14-4_會計部年度檢查報表", "非RBC_表14-4_會計部年度檢查報表.xlsx", "表14-4");
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
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY004", "非RBC_表14-4_會計部年度檢查報表", "LY004-非RBC_表14-4_會計部年度檢查報表", "非RBC_表14-4_會計部年度檢查報表.xlsx", "表14-4");
		// makeExcel.setValue(5,1,"測試日期");
//		makeExcel.setSheet("201903-固定");

		makeExcel.setValue(6, 2, "5,000,000,000");
		makeExcel.setValue(6, 3, "20,000,000");
		makeExcel.setValue(6, 4, "44,000,000,000%");
		makeExcel.setValue(6, 5, "-4,000,000,000");
		makeExcel.setValue(6, 6, "10%");
		makeExcel.setValue(6, 7, "1,500,000,000");
		makeExcel.setValue(6, 8, "-22,000,000");
		makeExcel.setValue(6, 9, "17,000,000");
		makeExcel.setValue(6, 10, "500,000");
		makeExcel.setValue(6, 13, "1,522,000,000");
		makeExcel.setValue(6, 14, "21,500,000");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
