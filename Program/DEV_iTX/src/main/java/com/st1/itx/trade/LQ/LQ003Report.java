package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ003ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LQ003Report extends MakeReport {


	@Autowired
	LQ003ServiceImpl LQ003ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {
 
	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 設定資料庫(必須的)
//		LQ003ServiceImpl.getEntityManager(titaVo);
		try {

			exportExcel(titaVo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LQ003ServiceImpl.testExcel error = " + errors.toString());
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		this.info("===========in exportExcel");
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日(int)
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthLastDate = Calendar.getInstance();
		// 設當年月底日
		calMonthLastDate.set(iYear, iMonth, 0);

		int monthLastDate = Integer.valueOf(dateFormat.format(calMonthLastDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < monthLastDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		// 年
		String rocYear = String.valueOf(iYear - 1911);
		// 月
		String rocMon = String.format("%02d", iMonth);
		
		// 季
		String Q = "";

		switch (rocMon) {
		case "01":
		case "02":
		case "03":
			Q = String.valueOf(Integer.valueOf(rocYear) - 1) + "Q4";
			break;
		case "04":
		case "05":
		case "06":
			Q = rocYear + "Q1";
			break;
		case "07":
		case "08":
		case "09":
			Q = rocYear + "Q2";
			break;
		case "10":
		case "11":
		case "12":
			Q = rocYear + "Q3";
			break;
		}

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ003", "住宅違約統計季報_服務課申報表", "LQ003住宅違約統計季報" + Q,
				"放款管理課_住宅違約統計季報_服務課申報表.xlsx", "填報");

		makeExcel.setValue(1, 9, Q, "C");

		// 設欄寬
		makeExcel.setWidth(3, 15);
		makeExcel.setWidth(4, 15);
		makeExcel.setWidth(5, 18);
		makeExcel.setWidth(6, 17);
		makeExcel.setWidth(7, 10);
		// 設列高
		makeExcel.setHeight(1, 50);

//		makeExcel.setSheet("N總額");

//		makeExcel.setValue(1, 1, "地區別名稱", "C");
//		makeExcel.setValue(1, 2, "地區別", "C");
//		makeExcel.setValue(1, 3, "總額", "C");
//		makeExcel.setValue(1, 4, "逾放", "C");
//		makeExcel.setValue(1, 5, "催收", "C");
//		makeExcel.setValue(1, 6, "件數", "C");
//		makeExcel.setValue(1, 7, "上季逾放", "C");

		List<Map<String, String>> findList = new ArrayList<>();

		List<Map<String, String>> findList2 = new ArrayList<>();

		try {
			// 當季
			findList = LQ003ServiceImpl.findAll(titaVo, 0);
			// 上季
			findList2 = LQ003ServiceImpl.findAll(titaVo, 1);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LQP003ServiceImpl.findDept error = " + errors.toString());
		}

		if (findList.size() > 0 && findList2.size() > 0) {
			int i = 2;

			for (Map<String, String> tLDVo : findList) {

				// 縣市名稱
				makeExcel.setValue(i, 1, tLDVo.get("F0"));

				// 縣市代號
				int f1 = tLDVo.get("F1") == null || tLDVo.get("F1").length() == 0 ? 0
						: Integer.parseInt(tLDVo.get("F1"));
				makeExcel.setValue(i, 2, f1, "C");

				// 季末貸款總額
				BigDecimal f2 = tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F2"));
				makeExcel.setValue(i, 3, f2, "#,##0", "C");

				// 季末逾放金額
				BigDecimal f3 = tLDVo.get("F3") == null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(i, 4, f3, "#,##0", "R");

				// 催收
				BigDecimal f4 = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(i, 6, f4, "#,##0", "R");

				// 逾放筆數
				BigDecimal f5 = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(i, 7, f5, "#,##0", "R");

//				// 上季末貸款總額
//				BigDecimal f6 = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
//						: new BigDecimal(tLDVo.get("F6"));
//				makeExcel.setValue(i, 7, f6, "#,##0", "R");

				i++;
			}

			i = 2;

			for (Map<String, String> tLDVo2 : findList2) {
				// 上季末貸款總額
				BigDecimal f9 = tLDVo2.get("F3") == null || tLDVo2.get("F3").length() == 0 ? BigDecimal.ZERO
						: new BigDecimal(tLDVo2.get("F3"));
				makeExcel.setValue(i, 9, f9, "#,##0", "R");

				i++;
			}

		} else {
			makeExcel.setValue(2, 2, "本日無資料");
		}

	}

}

//if (findList.size() > 0) {
//	int i = 2;
//	BigDecimal f2Total = BigDecimal.ZERO;
//	BigDecimal f3Total = BigDecimal.ZERO;
//	BigDecimal f5Total = BigDecimal.ZERO;
//	BigDecimal f6Total = BigDecimal.ZERO;
//	BigDecimal f7Total = BigDecimal.ZERO;
//	for (Map<String, String> tLDVo : findList) {
//		// 縣市代號
//		String f0 = tLDVo.get("F0") == null || tLDVo.get("F0").length() == 0 ? " " : tLDVo.get("F0");
//		// 縣市名稱
//		String f1 = tLDVo.get("F1");
//		// 季末貸款總額
//		BigDecimal f2 = tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? BigDecimal.ZERO
//				: new BigDecimal(tLDVo.get("F2"));
//		// 季末逾放金額
//		BigDecimal f3 = tLDVo.get("F3") == null || tLDVo.get("F3").length() == 0 ? BigDecimal.ZERO
//				: new BigDecimal(tLDVo.get("F3"));
//		// 本季末購置住宅貸款逾放金額變化(百萬元)
//		BigDecimal f4 = tLDVo.get("F4") == null || tLDVo.get("F4").length() == 0 ? BigDecimal.ZERO
//				: new BigDecimal(tLDVo.get("F4"));
//		// 本季末購置住宅貸款催收金額(百萬元)
//		BigDecimal f5 = tLDVo.get("F5") == null || tLDVo.get("F5").length() == 0 ? BigDecimal.ZERO
//				: new BigDecimal(tLDVo.get("F5"));
//		// 逾放筆數
//		BigDecimal f6 = tLDVo.get("F6") == null || tLDVo.get("F6").length() == 0 ? BigDecimal.ZERO
//				: new BigDecimal(tLDVo.get("F6"));
//
//		// 上季的 逾放金額?催收金額?
//		BigDecimal f7 = tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? BigDecimal.ZERO
//				: new BigDecimal(tLDVo.get("F7"));
//
//		makeExcel.setValue(i, 1, f0, "C");
//		makeExcel.setValue(i, 2, f1, "C");
//		makeExcel.setValue(i, 3, fm1.format(f2.doubleValue()), "R");
//		makeExcel.setValue(i, 4, fm1.format(f3.doubleValue()), "R");
//		makeExcel.setValue(i, 5, fm1.format(f4.intValue()), "R");
//		makeExcel.setValue(i, 6, fm2.format(f5.doubleValue()), "R");
//		makeExcel.setValue(i, 7, f6.intValue(), "R");
//		makeExcel.setValue(i, 8, "");
//		makeExcel.setValue(i, 9, fm2.format(f7.doubleValue()), "R");
//
//		f2Total = f2Total.add(f2);
//		f3Total = f3Total.add(f3);
//		f5Total = f5Total.add(f5);
//		f6Total = f6Total.add(f6);
//		f7Total = f7Total.add(f7);
//
//		i++;
//	}
//	makeExcel.setValue(i, 1, "合計", "C");
//	makeExcel.setValue(i, 3, f2Total, "R");
//	makeExcel.setValue(i, 4, f3Total, "R");
//	makeExcel.setValue(i, 6, fm2.format(f5Total), "R");
//	makeExcel.setValue(i, 7, f6Total, "R");
//
//	makeExcel.setValue(i, 9, fm2.format(f7Total), "R");
//
////	makeExcel.setAddRengionBorder("A", 2, "G", i + 1, 1);
//} else {
//	makeExcel.setValue(2, 2, "本日無資料");
//}
//// 設欄寬
//makeExcel.setWidth(3, 15);
//makeExcel.setWidth(4, 15);
//makeExcel.setWidth(5, 18);
//makeExcel.setWidth(6, 17);
//makeExcel.setWidth(7, 10);
//// 設列高
//makeExcel.setHeight(1, 50);
