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
import com.st1.itx.db.service.springjpa.cm.LY005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("LY005Report")
@Scope("prototype")

public class LY005Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LY005Report.class);

	@Autowired
	public LY005ServiceImpl LY005ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
//		LY005ServiceImpl.getEntityManager(titaVo);
		try {

//			List<HashMap<String, String>> LY005List = LY005ServiceImpl.findAll();
//			if(LY005List.size() > 0){
			testExcel(titaVo);
//			  testExcel(titaVo, LY005List);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY005ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void testExcel(TitaVo titaVo, List<HashMap<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY005", "非RBC_表20_會計部年度檢查報表", "LY005-非RBC_表20_會計部年度檢查報表", "非RBC_表20_會計部年度檢查報表.xlsx", "107.12");
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
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY005", "非RBC_表20_會計部年度檢查報表", "LY005-非RBC_表20_會計部年度檢查報表", "非RBC_表20_會計部年度檢查報表.xlsx", "107.12");
		// makeExcel.setValue(5,1,"測試日期");
//		makeExcel.setSheet("201903-固定");

		makeExcel.setValue(3, 17, "50,000,000");
		makeExcel.setValue(5, 3, "E");
		makeExcel.setValue(5, 4, "2222222");
		makeExcel.setValue(5, 5, "測試者");
		makeExcel.setValue(5, 6, "A");
		makeExcel.setValue(5, 7, "C");
		makeExcel.setValue(5, 8, "台北市xxxxxxxxxxxxxxx99號99樓");
		makeExcel.setValue(5, 9, "20200101");
		makeExcel.setValue(5, 10, "5,000,000");
		makeExcel.setValue(5, 14, "5");
		makeExcel.setValue(5, 15, "B");
		makeExcel.setValue(13, 10, "5,000,000");
		makeExcel.setValue(14, 3, "107.12.31 淨值：5,000,000元【查核數】");
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
