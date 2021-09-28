package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM057ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM057Report extends MakeReport {

	@Autowired
	LM057ServiceImpl lM057ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public String dateF = "";
	public int yearMon = 0;

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> findList = new ArrayList<>();
//		List<Map<String, String>> findList2 = new ArrayList<>();
//		List<Map<String, String>> findList3 = new ArrayList<>();

		this.info("LM057Report exec");

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日 0是月底
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		int iDay = iEntdy % 100;
		
		String date = iYear + "/" + iMonth + "/" + iDay;

		dateF = date;
		
		yearMon = Integer.valueOf(titaVo.get("ENTDY")) / 100;

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM057", "表14-5、14-6會計部申報表",
				"LM057-表14-5、14-6_會計部申報表", "LM057_底稿_表14-5、14-6_會計部申報表.xlsx", "14-5申報表");

		try {

			findList = lM057ServiceImpl.findAll(titaVo);
//			findList2 = lM057ServiceImpl.findAll(titaVo);
//			findList3 = lM057ServiceImpl.findAll(titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM057ServiceImpl.findAll error = " + errors.toString());

		}

//		reportData(findList, findList2);

		exportExcel14_5(findList);

		exportExcel14_6();

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

//	private void reportData(List<Map<String, String>> listData, List<Map<String, String>> listData2)
//			throws LogicException {
//		this.info("LM057report.reportData");
//
//		String sheetName = yearMon + "工作表";
//
//		makeExcel.setSheet("XXX工作表", sheetName);
//
//		int row = 1;
//
//
//		if (listData.size() > 0 && listData2.size() > 0) {
//		
//			for (Map<String, String> lM057Vo : listData) {
//				row++;
//				// A~C 921受災戶 (要vlookup的)
//				makeExcel.setValue(row, 1, lM057Vo.get("F??"));
//				makeExcel.setValue(row, 2, "C7", "C");
//				makeExcel.setValue(row, 3, lM057Vo.get("F??"));
//			}
//
//			row = 1;
//
//			for (Map<String, String> lM057Vo : listData2) {
//				row++;
//
//				// 戶號+額度
//				makeExcel.setValue(row, 4, lM057Vo.get("F??"));
//				// 帳冊別
//				makeExcel.setValue(row, 5, lM057Vo.get("F??"));
//				// 戶號
//				makeExcel.setValue(row, 6, lM057Vo.get("F??"));
//				// 額度
//				makeExcel.setValue(row, 7, lM057Vo.get("F??"));
//				// 序號
//				makeExcel.setValue(row, 8, lM057Vo.get("F??"));
//				// 姓名
//				makeExcel.setValue(row, 9, lM057Vo.get("F??"));
//				// 金額
//				makeExcel.setValue(row, 10, lM057Vo.get("F??"));
//				// 到期日
//				makeExcel.setValue(row, 11, lM057Vo.get("F??"));
//				// 利率代碼
//				makeExcel.setValue(row, 12, lM057Vo.get("F??"));
//				// 分類(需加判斷分類)
//				makeExcel.setValue(row, 13, lM057Vo.get("F??"));
//				// 跟921受災戶比
//				makeExcel.setValue(row, 14, "=VLOOKUP(F" + row + ",A:B,2,0)");
//
//		
//
//				// 法務進度
//				makeExcel.setValue(row, 15, lM057Vo.get("F??"));
//
//			}
//			
//			//重整公式
//			makeExcel.formulaCaculate(1, 8);
//			makeExcel.formulaCaculate(1, 9);
//
//		} else {
//
//			makeExcel.setValue(2, 1, "本日無資料");
//
//
//		}
//
//	}	

	private void exportExcel14_5(List<Map<String, String>> listData) throws LogicException {
		this.info("LM057report.report14_5");

		makeExcel.setSheet("14-5申報表");

		// D2 日期
		makeExcel.setValue(2, 4, dateF);

		int col = 0;
		int row = 0;
		BigDecimal amount = BigDecimal.ZERO;

		// 可能可去掉這層，
		for (Map<String, String> lM057Vo : listData) {

			amount = lM057Vo.get("F1") == null ? BigDecimal.ZERO : new BigDecimal(lM057Vo.get("F1"));

			switch (lM057Vo.get("F0")) {
			case "B1":
				// 甲類-放款本金超過清償期三個月而未獲清償，或雖未屆滿三個月，但以向主、從償務人訴追或楚芬擔保品者
				makeExcel.setValue(5, 4, amount, "#,##0");
				break;
			case "B3":
				// 甲類-放款本金未按期攤超過六個月
				makeExcel.setValue(7, 4, amount, "#,##0");
				break;
			case "C2":
				// 乙類-分期償還放款未按期攤超過三至六個月
				makeExcel.setValue(11, 4,  amount, "#,##0");
				break;
			case "C5":
				// 已確定分配之債權，惟尚未獲分款者
				makeExcel.setValue(14, 4, amount, "#,##0");
				break;
			case "TOTAL":
				// 放款總額(含轉列催收款)
				makeExcel.setValue(18, 4,  amount, "#,##0");
				break;
			default:
				break;
			}


		}

		// 重整公式
		makeExcel.formulaCaculate(9, 4);
		makeExcel.formulaCaculate(17, 4);
		makeExcel.formulaCaculate(19, 4);
		makeExcel.formulaCaculate(20, 4);
		makeExcel.formulaCaculate(21, 4);
		makeExcel.formulaCaculate(22, 4);
 
	}

	private void exportExcel14_6() throws LogicException {
		this.info("LM057report.report14_6");

		makeExcel.setSheet("表14-6");

		// 重整 D2~D24
		makeExcel.formulaCaculate(2, 4);

		for (int i = 5; i <= 24; i++) {

			makeExcel.formulaCaculate(i, 4);

		}

	}
}
