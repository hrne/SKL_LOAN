package com.st1.itx.trade.LP;

import java.io.PrintWriter;
import java.io.StringWriter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LP005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LP005Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LP005Report.class);

	@Autowired
	LP005ServiceImpl LP005ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}
 
	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
//		LP005ServiceImpl.getEntityManager(titaVo);
		try {

//			List<HashMap<String, String>> LP005List = LP005ServiceImpl.findAll();
//			if(LP005List.size() > 0){
			exportExcel(titaVo);
//			  testExcel(titaVo, LP005List);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LP005ServiceImpl.testExcel error = " + errors.toString());
		}
	}

//	private void testExcel(TitaVo titaVo, List<HashMap<String, String>> LDList) throws LogicException {
//		this.info("===========in testExcel");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP005", "房貸協辦人員考核核算底稿", "LP005協辦考核核算底稿",
//				"協辦考核核算底稿.xlsx", "1月件數");
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

//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP005", "房貸協辦人員考核核算底稿", "LP005協辦考核核算底稿",
				"協辦考核核算底稿.xlsx", "1月件數");
		// makeExcel.setValue(5,1,"測試日期");
//		makeExcel.setSheet("201903-固定");
		makeExcel.setValue(2, 1, "5");
		makeExcel.setValue(2, 2, "1445678");
		makeExcel.setValue(2, 3, "陳水牛");
		makeExcel.setValue(2, 4, "1000");
		makeExcel.setValue(2, 5, "A0B000");
		makeExcel.setValue(2, 6, "呂蓮花");
		makeExcel.setValue(2, 7, "業務推展部");
		makeExcel.setValue(2, 8, "彰化區部");
		makeExcel.setValue(2, 9, "溪湖通－收");
		makeExcel.setValue(2, 11, "呂蓮花");

		makeExcel.setSheet("1月金額");
		makeExcel.setValue(2, 1, "1445678");
		makeExcel.setValue(2, 2, "5");
		makeExcel.setValue(2, 3, "500000");
		makeExcel.setValue(2, 4, "1");
		makeExcel.setValue(2, 5, "A0B000");
		makeExcel.setValue(2, 6, "呂蓮花");
		makeExcel.setValue(2, 7, "業務推展部");
		makeExcel.setValue(2, 8, "彰化區部");
		makeExcel.setValue(2, 9, "溪湖通－收");

		makeExcel.setSheet("2月件數");
		makeExcel.setValue(2, 1, "5");
		makeExcel.setValue(2, 2, "1445678");
		makeExcel.setValue(2, 3, "陳水牛");
		makeExcel.setValue(2, 4, "1000");
		makeExcel.setValue(2, 5, "A0B000");
		makeExcel.setValue(2, 6, "呂蓮花");
		makeExcel.setValue(2, 7, "業務推展部");
		makeExcel.setValue(2, 8, "彰化區部");
		makeExcel.setValue(2, 9, "溪湖通－收");
		makeExcel.setValue(2, 11, "呂蓮花");

		makeExcel.setSheet("2月金額");
		makeExcel.setValue(2, 1, "1445678");
		makeExcel.setValue(2, 2, "5");
		makeExcel.setValue(2, 3, "500000");
		makeExcel.setValue(2, 4, "1");
		makeExcel.setValue(2, 5, "A0B000");
		makeExcel.setValue(2, 6, "呂蓮花");
		makeExcel.setValue(2, 7, "業務推展部");
		makeExcel.setValue(2, 8, "彰化區部");
		makeExcel.setValue(2, 9, "溪湖通－收");

		makeExcel.setSheet("3月件數");
		makeExcel.setValue(2, 1, "5");
		makeExcel.setValue(2, 2, "1445678");
		makeExcel.setValue(2, 3, "陳水牛");
		makeExcel.setValue(2, 4, "1000");
		makeExcel.setValue(2, 5, "A0B000");
		makeExcel.setValue(2, 6, "呂蓮花");
		makeExcel.setValue(2, 7, "業務推展部");
		makeExcel.setValue(2, 8, "彰化區部");
		makeExcel.setValue(2, 9, "溪湖通－收");
		makeExcel.setValue(2, 11, "呂蓮花");

		makeExcel.setSheet("3月金額");
		makeExcel.setValue(2, 1, "1445678");
		makeExcel.setValue(2, 2, "5");
		makeExcel.setValue(2, 3, "500000");
		makeExcel.setValue(2, 4, "1");
		makeExcel.setValue(2, 5, "A0B000");
		makeExcel.setValue(2, 6, "呂蓮花");
		makeExcel.setValue(2, 7, "業務推展部");
		makeExcel.setValue(2, 8, "彰化區部");
		makeExcel.setValue(2, 9, "溪湖通－收");

		makeExcel.setSheet("108Q1");
		makeExcel.setValue(3, 2, "業務推展部");
		makeExcel.setValue(3, 3, "彰化區部");
		makeExcel.setValue(3, 4, "秀水收");
		makeExcel.setValue(3, 5, "呂蓮花");
		makeExcel.setValue(3, 6, "AFD123");
		makeExcel.setValue(3, 7, "初級");
		makeExcel.setValue(3, 8, "初級");

		makeExcel.setSheet("營管");
		makeExcel.setValue(4, 1, "彰化區部");
		makeExcel.setValue(4, 2, "秀水收");
		makeExcel.setValue(4, 3, "呂蓮花");
		makeExcel.setValue(4, 4, "AFD123");

		makeExcel.setSheet("營推");
		makeExcel.setValue(4, 1, "彰化區部");
		makeExcel.setValue(4, 2, "秀水收");
		makeExcel.setValue(4, 3, "呂蓮花");
		makeExcel.setValue(4, 4, "AFD123");

		makeExcel.setSheet("業推");
		makeExcel.setValue(4, 1, "彰化區部");
		makeExcel.setValue(4, 2, "清水收");
		makeExcel.setValue(4, 3, "呂蓮花");
		makeExcel.setValue(4, 4, "AFD123");

		makeExcel.setSheet("業開");
		makeExcel.setValue(4, 1, "彰化區部");
		makeExcel.setValue(4, 2, "大發收");
		makeExcel.setValue(4, 3, "呂蓮花");
		makeExcel.setValue(4, 4, "AFD123");

		makeExcel.setSheet("108Q2稿");
		makeExcel.setValue(3, 2, "營管部");
		makeExcel.setValue(3, 3, "大新收");
		makeExcel.setValue(3, 4, "士林區部");
		makeExcel.setValue(3, 5, "呂蓮花");
		makeExcel.setValue(3, 6, "AFD123");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
