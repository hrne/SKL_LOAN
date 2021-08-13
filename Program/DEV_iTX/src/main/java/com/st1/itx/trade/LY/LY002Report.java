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
import com.st1.itx.db.service.springjpa.cm.LY002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("LY002Report")
@Scope("prototype")

public class LY002Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LY002Report.class);

	@Autowired
	public LY002ServiceImpl LY002ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
//		String iPage = titaVo.getParam("Page").trim();
		// 設定資料庫(必須的)
//		LY002ServiceImpl.getEntityManager(titaVo);
		try {

//			List<HashMap<String, String>> LY002List = LY002ServiceImpl.findAll();
//			if(LY002List.size() > 0){
			testExcel(titaVo);
//			  testExcel(titaVo, LY002List);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY002ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void testExcel(TitaVo titaVo, List<HashMap<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY002", "非RBC_表14-1_會計部年度檢查報表", "LY002-非RBC_表14-1_會計部年度檢查報表", "非RBC_表14-1_會計部年度檢查報表.xlsx", "表14-1");
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
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY002", "非RBC_表14-1_會計部年度檢查報表", "LY002-非RBC_表14-1_會計部年度檢查報表", "非RBC_表14-1_會計部年度檢查報表.xlsx", "表14-1");
		// makeExcel.setValue(5,1,"測試日期");
//		makeExcel.setSheet("201903-固定");

		makeExcel.setValue(7, 3, "2XXXXX22");
		makeExcel.setValue(7, 4, "拉X建設股份有限公司");
		makeExcel.setValue(7, 5, "C");
		makeExcel.setValue(7, 6, "E");
		makeExcel.setValue(7, 7, "C");
		makeExcel.setValue(7, 8, "B2");
		makeExcel.setValue(7, 9, "20200701");
		makeExcel.setValue(7, 10, "20300701");
		makeExcel.setValue(7, 11, "1,6666");
		makeExcel.setValue(7, 12, "E");
		makeExcel.setValue(7, 13, "20290501");
		makeExcel.setValue(7, 14, "2XXXXX22");
		makeExcel.setValue(7, 15, "拉X建設股份有限公司");
		makeExcel.setValue(7, 16, "1");
		makeExcel.setValue(7, 17, "7,777,777,777");
		makeExcel.setValue(7, 18, "4,000,000,000");
		makeExcel.setValue(7, 19, "NTD");
		makeExcel.setValue(7, 20, "3,333,333,333");
		makeExcel.setValue(7, 21, "0.16%");
		makeExcel.setValue(7, 22, "4.44%");
		makeExcel.setValue(7, 23, "-");
		makeExcel.setValue(7, 33, "V");
		makeExcel.setValue(7, 37, "666666");

		makeExcel.setValue(16, 17, "7,777,777,777");
		makeExcel.setValue(16, 18, "4,000,000,000");
		makeExcel.setValue(16, 20, "3,333,333,333");
		makeExcel.setValue(16, 21, "0.16%");
		makeExcel.setValue(16, 22, "4.44%");

		makeExcel.setValue(20, 17, "7,777,777,777");
		makeExcel.setValue(20, 18, "4,000,000,000");
		makeExcel.setValue(20, 20, "3,333,333,333");
		makeExcel.setValue(20, 21, "0.16%");
		makeExcel.setValue(20, 22, "4.44%");
		makeExcel.setValue(21, 17, "7,777,777,777");
		makeExcel.setValue(21, 18, "4,000,000,000");
		makeExcel.setValue(21, 20, "55,555,555");
		makeExcel.setValue(21, 22, "2,255,555,555");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
