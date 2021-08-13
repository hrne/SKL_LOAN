package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LQ005Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LQ005Report.class);

	@Autowired
	LQ005ServiceImpl LQ005ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
//		LQ005ServiceImpl.getEntityManager(titaVo);
		try {

//			List<HashMap<String, String>> LQ005List = LQ005ServiceImpl.findAll();
//			if(LQ005List.size() > 0){
			exportExcel(titaVo);
//			  testExcel(titaVo, LQ005List);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LQ005ServiceImpl.testExcel error = " + errors.toString());
		}
	}

//	private void testExcel(TitaVo titaVo, List<HashMap<String, String>> LDList) throws LogicException {
//		this.info("===========in testExcel");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ005", "住宅違約統計季報_服務課申報表",
//				"LQ005住宅違約統計季報_服務課申報表", "放款管理課_住宅違約統計季報_服務課申報表.xlsx", "填報");
		// makeExcel.setValue(5,1,"測試帳號");
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
//
//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ005", "表A18_會計部申報表", "LQ005表A18_會計部申報表",
				"表A18_會計部申報表.xlsx", "108.03.31");
		// makeExcel.setValue(5,1,"測試日期");

		makeExcel.setValue(8, 1, "0001");
		makeExcel.setValue(8, 2, "02");
		makeExcel.setValue(8, 3, "03456789");
		makeExcel.setValue(8, 4, "健康人壽");
		makeExcel.setValue(8, 5, "B");
		makeExcel.setValue(8, 6, "B123456123");
		makeExcel.setValue(8, 7, "張X風");
		makeExcel.setValue(8, 8, "放款 02");
		makeExcel.setValue(8, 9, "19,89");
		makeExcel.setValue(8, 10, "30,00");
		makeExcel.setValue(8, 11, "123456");
		makeExcel.setValue(8, 12, "30,00");

		makeExcel.setValue(12, 9, "19,89");
		makeExcel.setValue(12, 10, "30,00");
		makeExcel.setValue(12, 12, "30,00");

		makeExcel.setSheet("T044_20190329_金控法44條");
		makeExcel.setValue(3, 1, "新光人壽");
		makeExcel.setValue(3, 2, "B123456123");
		makeExcel.setValue(3, 3, "張X風");
		makeExcel.setValue(3, 4, "協理");
		makeExcel.setValue(3, 5, "33,333,333");
		makeExcel.setValue(3, 6, "10.000.000");
		makeExcel.setValue(3, 8, "123456");
		makeExcel.setValue(3, 10, "22,222,222");
		makeExcel.setValue(3, 13, "27,000,000");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
