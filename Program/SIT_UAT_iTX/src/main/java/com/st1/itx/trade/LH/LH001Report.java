package com.st1.itx.trade.LH;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LH001ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LH001Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LH001Report.class);

	@Autowired
	LH001ServiceImpl LH001ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
//		LH001ServiceImpl.getEntityManager(titaVo);
		try {

//			List<HashMap<String, String>> LH001List = LH001ServiceImpl.findAll();
//			if(LH001List.size() > 0){
			testExcel(titaVo);
//			  testExcel(titaVo, LH001List);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LH001ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void testExcel(TitaVo titaVo, List<HashMap<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LH001", "住宅違約統計季報_服務課申報表", "LH001住宅違約統計季報_服務課申報表", "放款管理課_住宅違約統計季報_服務課申報表.xlsx", "填報");
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

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void testExcel(TitaVo titaVo) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LH001", "表A07_會計部申報表", "LH001表A07_會計部申報表", "表A07_會計部申報表.xlsx", "新表7(108.03.31");
		// makeExcel.setValue(5,1,"測試日期");

		makeExcel.setValue(5, 1, "孫X空");
		makeExcel.setValue(5, 2, "123456788");
		makeExcel.setValue(5, 5, "1");
		makeExcel.setValue(5, 6, "授信（不含短期票券之保證或背書）");
		makeExcel.setValue(5, 7, "12");
		makeExcel.setValue(5, 8, "有擔保金額：5,555,555");
		makeExcel.setValue(5, 9, "N");
		makeExcel.setValue(5, 10, "4,444,444");
		makeExcel.setValue(34, 10, "4,444,444");

		makeExcel.setValue(36, 1, "孫X空及其同一關係人");
		makeExcel.setValue(36, 2, "A123321225");
		makeExcel.setValue(36, 5, "1");
		makeExcel.setValue(36, 6, "授信（不含短期票券之保證或背書）");
		makeExcel.setValue(36, 7, "12");
		makeExcel.setValue(36, 8, "有擔保金額：99,999");
		makeExcel.setValue(36, 9, "N");
		makeExcel.setValue(36, 10, "59,999");
		makeExcel.setValue(39, 10, "59,999");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
