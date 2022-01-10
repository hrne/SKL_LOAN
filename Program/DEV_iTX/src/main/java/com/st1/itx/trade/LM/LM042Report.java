package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM042ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM042Report extends MakeReport {

	@Autowired
	LM042ServiceImpl lM042ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
	public int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
	public int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM042Report.exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM042", "RBC表_會計部", "LM042-RBC表_會計部",
				"LM042_底稿_RBC表_會計部_共三份.xlsx", "統計數");

		Object test = makeExcel.getValue(7, 3);
		this.info("LM042 get value" + test.toString());
		exportExcelFindStatistics(titaVo);

		exportExcel(titaVo);

		exportExcel2(titaVo);

		exportExcel3(titaVo);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	BigDecimal cY1Amt = BigDecimal.ZERO;
	BigDecimal cN1Amt = BigDecimal.ZERO;
	BigDecimal cN2Amt = BigDecimal.ZERO;
	BigDecimal cN3Amt = BigDecimal.ZERO;
	BigDecimal cN5Amt = BigDecimal.ZERO;
	BigDecimal dN1Amt = BigDecimal.ZERO;
	BigDecimal zY1Amt = BigDecimal.ZERO;
	BigDecimal zN1Amt = BigDecimal.ZERO;
	BigDecimal zN2Amt = BigDecimal.ZERO;
	BigDecimal zN3Amt = BigDecimal.ZERO;
	BigDecimal zN5Amt = BigDecimal.ZERO;

	private void exportExcelFindStatistics(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> statisticsList1 = null;
		List<Map<String, String>> statisticsList2 = null;
		List<Map<String, String>> statisticsList3 = null;

		try {

			statisticsList1 = lM042ServiceImpl.findStatistics1(titaVo);
			statisticsList2 = lM042ServiceImpl.findStatistics2(titaVo);
			statisticsList3 = lM042ServiceImpl.findStatistics3(titaVo);

		} catch (Exception e) {

			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM042ServiceImpl.exportExcel error = " + errors.toString());

		}

		if (statisticsList1.size() == 0 && statisticsList2.size() == 0 && statisticsList3.size() == 0) {
			return;
		}

		if (statisticsList1.size() > 0 && statisticsList2.size() > 0 && statisticsList2.size() > 0) {

			int row = 0;
			int col = 0;

			for (Map<String, String> lm42Vo1 : statisticsList1) {
				int assetClass = Integer.valueOf(lm42Vo1.get("F1"));
				String kind = lm42Vo1.get("F2");
				String rptId = lm42Vo1.get("F3");
				BigDecimal amt = getBigDecimal(lm42Vo1.get("F4"));

				if("C".equals(kind) && "Y".equals(rptId) && assetClass==1) {
					row = 21;
					cY1Amt = cY1Amt.add(amt);
				}

				if("C".equals(kind) && "N".equals(rptId) && assetClass==1) {
					row = 22;
					cN1Amt = cN1Amt.add(amt);
				}
				if("C".equals(kind) && "N".equals(rptId) && assetClass==2) {
					row = 22;
					cN2Amt = cN2Amt.add(amt);
				}
				if("C".equals(kind) && "N".equals(rptId) && assetClass==3) {
					row = 22;
					cN3Amt = cN3Amt.add(amt);
				}
				if("C".equals(kind) && "N".equals(rptId) && assetClass==5) {
					row = 22;
					cN5Amt = cN5Amt.add(amt);
				}
				
				if("D".equals(kind) && "N".equals(rptId) && assetClass==1) {
					row = 23;
					dN1Amt = dN1Amt.add(amt);
				}		
				
				if("Z".equals(kind) && "Y".equals(rptId) && assetClass==1) {
					row = 24;
					zY1Amt = zY1Amt.add(amt);
				}

				if("Z".equals(kind) && "N".equals(rptId)&& assetClass==1) {
					row = 25;
					zN1Amt = zN1Amt.add(amt);
				}
				if("Z".equals(kind) && "N".equals(rptId)&& assetClass==2) {
					row = 25;
					zN2Amt = zN2Amt.add(amt);
				}
				if("Z".equals(kind) && "N".equals(rptId)&& assetClass==3) {
					row = 25;
					zN3Amt = zN3Amt.add(amt);
				}
				if("Z".equals(kind) && "N".equals(rptId)&& assetClass==5) {
					row = 25;
					zN5Amt = zN5Amt.add(amt);
				}
				makeExcel.setValue(row, col, amt, "#,##0");

			}

			for (int i = 3; i <= 6; i++) {
				makeExcel.formulaCaculate(27, i);
			}

			for (int i = 21; i <= 27; i++) {
				makeExcel.formulaCaculate(i, 8);
			}

			for (Map<String, String> lm42Vo2 : statisticsList2) {
				String item = lm42Vo2.get("F0");
				BigDecimal amt = getBigDecimal(lm42Vo2.get("F1"));

				// 各金額項目
				// 折溢價及催收費用
				if ("DisPreRemFees".equals(item)) {
					row = 12;
				}
				// 應收利息
				if ("IntRecv".equals(item)) {
					row = 13;
				}
				// 專案貸款
				if ("ProLoan".equals(item)) {
					row = 14;
				}
				// 利關人_職員數
				if ("Stakeholder".equals(item)) {
					row = 16;
					cY1Amt = cY1Amt.add(amt);
				}

				col = 3;

				makeExcel.setValue(row, col, amt, "#,##0");

				for (int x = 3; x <= 16; x++) {
					for (int y = 5; y <= 16; y++) {
						makeExcel.formulaCaculate(y, x);
					}
				}

			}
			
			// P欄位 (購置住宅+修繕貸款)
			for (Map<String, String> lm42Vo3 : statisticsList3) {

				String type = lm42Vo3.get("F0");
				BigDecimal amt = getBigDecimal(lm42Vo3.get("F2"));

				// 擔保品類別
				switch (type) {
				case "C":
					row = 6;
					break;
				case "Z":
					row = 9;
					break;
				}

				col = 11;

				makeExcel.setValue(row, col, amt, "#,##0");

			}

		}

	}

	public void lossAmt() {

	}

	public void exportExcelDataDetail(BigDecimal lossAmt) {

		try {

			makeExcel.setSheet("明細表");

			for (int i = 5; i <= 27; i++) {
				makeExcel.formulaCaculate(i, 3);
			}

			makeExcel.setValue(28, 1, "註：各類放款總餘額（含催收款）已扣除備抵呆帳（" + lossAmt + "）。");

		} catch (LogicException e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM042ServiceImpl.exportExcel error = " + errors.toString());

		}
	}

	/*
	 * YYYMMRBC
	 * 
	 */
	// 只需要抓去年底(累積的)金額 跟 上個月的金額 其餘的公式固定不變，重整即可
	// 風險係數來源：資產風險之非關係人風險計算表???
	// A26 備註的金額：RBC工作表的 毛額A放款合計(F41)扣掉淨額放款合計(E41) 或
	// 備抵損失的餘額合計(10623、10624100、10624200)
	private void exportExcel(TitaVo titaVo) throws LogicException {

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

		// 當年上個月底
		calMonthLastDate.set(iYear, iMonth - 1, 0);
		int monthPreDate = Integer.valueOf(dateFormat.format(calMonthLastDate.getTime()));

		// 去年底日
		calMonthLastDate.set(iYear - 1, 12, 0);
		int lastYearEnd = Integer.valueOf(dateFormat.format(calMonthLastDate.getTime()));

		String rocYymm = (iYear - 1911) + String.format("%02d", iMonth);

		String fmThisMonthTitle = ((monthLastDate / 10000) - 1911) + "/" + ((monthLastDate / 100) % 100) + "/"
				+ (monthLastDate % 10000);

		String fmThisMonth = ((monthLastDate / 10000) - 1911) + "." + ((monthLastDate / 100) % 100) + "."
				+ (monthLastDate % 10000);

		String fmtLastMonth = ((monthPreDate / 10000) - 1911) + "." + ((monthPreDate / 100) % 100) + "."
				+ (monthPreDate % 10000);

		String fmLastYear = ((lastYearEnd / 10000) - 1911) + "." + ((lastYearEnd / 100) % 100) + "."
				+ (lastYearEnd % 10000);

		makeExcel.setSheet("10804RBC", rocYymm);

		List<Map<String, String>> lm42LastYearMonthEndList = null;
		List<Map<String, String>> lm42LastMonthEndList = null;
		try {

			// 確認各工作表是否要分開select00
			// 去年底
//			lm42LastYearMonthEndList = lM042ServiceImpl.findAll(titaVo, "LastYearMonthEnd");
			// 上個月
//			lm422LastMonthEndList = lM042ServiceImpl.findAll(titaVo, "LastMonthEnd");

		} catch (Exception e) {

			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM042ServiceImpl.exportExcel error = " + errors.toString());

		}

		// 日期
		makeExcel.setValue(1, 2, fmThisMonthTitle);

		if (lm42LastYearMonthEndList.size() > 0 && lm42LastYearMonthEndList != null && lm42LastMonthEndList.size() > 0
				&& lm42LastMonthEndList != null) {

			int col = 4;
			int row = 7;

			int amt = 0;
			int risk = 0;

			// 去年底 日期
			makeExcel.setValue(4, col, fmLastYear);

			for (Map<String, String> lm42tiVo : lm42LastYearMonthEndList) {

				row++;

				amt = lm42tiVo.get("F??") == null ? 0 : Integer.valueOf(lm42tiVo.get("F??"));
				risk = lm42tiVo.get("F??") == null ? 0 : Integer.valueOf(lm42tiVo.get("F??"));

				// 金額
				makeExcel.setValue(row, col, new BigDecimal(amt), "#,##0");
				// 風險量
				makeExcel.setValue(row, col + 1, new BigDecimal(risk), "#,##0");

				// 會有間格，到時候看select方式 再做判斷
				// 還有分一搬放款 專案放款

			}

			col = 6;
			row = 7;

			amt = 0;
			risk = 0;

			// 上個月底 日期
			makeExcel.setValue(4, col, fmtLastMonth);

			for (Map<String, String> lm42tiVo : lm42LastYearMonthEndList) {

				row++;

				amt = lm42tiVo.get("F??") == null ? 0 : Integer.valueOf(lm42tiVo.get("F??"));
				risk = lm42tiVo.get("F??") == null ? 0 : Integer.valueOf(lm42tiVo.get("F??"));

				// 金額
				makeExcel.setValue(row, col, new BigDecimal(amt), "#,##0");
				// 風險量
				makeExcel.setValue(row, col + 1, new BigDecimal(risk), "#,##0");

				// 會有間格，到時候看select方式 再做判斷
				// 還有分一搬放款 專案放款

			}

			// H6~K25 重整
			makeExcel.setValue(4, col, fmThisMonth);

			for (int i = 8; i <= 11; i++) {
				for (int j = 6; j <= 25; j++) {

					makeExcel.formulaCaculate(j, i);

				}
			}

			// 風險係數 來源要找

			// 備註1
			makeExcel.setValue(26, 1, "註1：各類放款總餘額(含催收款)已扣除備抵呆帳(" + 0 + ")。");

		} else {

			makeExcel.setValue(4, 6, "本日無資料");

		}

	}

	/**
	 * RBC工作表(共14欄)
	 * 
	 * 毛額A 非授信-不動產擔保放款 第二類 (F8)= 超過三期以上
	 * 房貸(RBC-C6)(放款管理課-逾期及風險相關報表-催收月報表-XXX年度-11005月報的工作報05月的C10)要找來源 等於 總計減首購340+專案
	 * 
	 * 毛額A 非授信-不動產擔保放款 第三類 (F9)= 催收款
	 * 房貸(RBC-C3)(放款管理課-逾期及風險相關報表-催收月報表-XXX年度-11005月報的工作報05月的E10)要找來源 等於 總計減首購340+專案
	 * 毛額A 非授信-動產擔保放款 (F10)= (科子目計算表格的所有動產合計)
	 *
	 * 毛額A 非授信-有價證券質押放款 (F14)= (科子目計算表格的有價證券合計)
	 * 
	 * 毛額A 授信-二 非具控制與從屬關係(F25)= 1~7+15日薪合計 或 利害關係人+15日薪合計 (科子目計算表格-110.05-B26)
	 * (放款管理課-關係人作業-放款關係人報表-月初報表-保險法第3條利害關係人放款餘額的工作表110.05的 D31 X 1000)要找來源 減
	 * 關係人政府優惠專案(科子目計算表格-110.05-B27)
	 * 
	 * 毛額A 專案放款 政策性之專案運用...(F28)= 專案和公共投資(放款管理課-關係人作業-XXX年度-專案放款的工作表109預警的F40)
	 * 要找一下來源
	 * 
	 * 毛額A 專案放款-非授信-不動產擔保放款 第二類 (F32)= 超過三期以上 首次購物(340)+專案 (RBC的E6)
	 * 
	 * 毛額A 專案放款-非授信-不動產擔保放款 第三類 (F33)= 催收款 首次購物(340)+專案 (RBC的E3)
	 * 
	 * 毛額A 專案放款-授信限制對象(F37)= 關係人政府優惠專案(科子目計算表格-110.05-B27)
	 * 
	 * 
	 * 淨額 放款合計(E41)= F41 減 I41
	 * 
	 * 毛額A 放款合計(F41)= 10603 擔保放款(科子目計算表格-科子目預2-K37) 加 10604000
	 * 催收款項-放款部(科子目計算表格-科子目預2-K38) 加 10604010 催收款項-法務費用(科子目計算表格-科子目預2-K39) 加
	 * 10604020 催收款項-火險費用(科子目計算表格-科子目預2-K40) 加 10604040 催收款項-溢折價(科子目計算表格-科子目預2-K41)
	 * 
	 * 
	 * A表 非授信-不動產擔保放款 第三類 (I9)= 10624 備抵損失-催收款項-擔保放款 (科子目計算表格-科子目預2-J23) 減 催收款
	 * 首次購物(340)+專案 (RBC的E3) 減 車貸備呆(這個月沒有)
	 * 
	 * 毛額B 授信-二 非具控制與從屬關係(G25)= 利害關係人合計(科子目計算表格-110.05-B20)
	 * (放款管理課-關係人作業-放款關係人報表-月初報表-保險法第3條利害關係人放款餘額的工作表110.05的 D29 X 1000)要找來源 減
	 * 關係人政府優惠專案(科子目計算表格-110.05-B27)
	 * 
	 * 
	 * A表 放款合計(I41)= 10623 備抵損失-擔保放款(科子目計算表格-科子目預2-K46) 加 10624100
	 * 備抵損失-催收款項-放款部(科子目計算表格-科子目預2-K47) 加 10624200 備抵損失-催收款樣-營業稅提撥科子目計算表格-科子目預2-K48)
	 */
	private void exportExcel2(TitaVo titaVo) throws LogicException {

		makeExcel.setSheet("RBC工作表");

		// 日期
		makeExcel.formulaCaculate(2, 2);

		List<Map<String, String>> lm42List = null;

		try {

			// 確認各工作表是否要分開select00
//			lm42List = lM042ServiceImpl.findAll(titaVo);

		} catch (Exception e) {

			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM042ServiceImpl.exportExcel error = " + errors.toString());

		}

		if (lm42List.size() > 0 && lm42List != null) {

			// lm42List.get(0).get("FXx")

			// 淨額-放款合計(E41)
			makeExcel.setValue(41, 5, "");

			// 毛額A-不動產-第2類(F8)
			makeExcel.setValue(6, 8, "");
			// 毛額A-不動產-第3類(F9)
			makeExcel.setValue(9, 6, "");
			// 毛額A-動產擔保放款(F10)
			makeExcel.setValue(10, 6, "");
			// 毛額A-有價證券質押放款(F14)
			makeExcel.setValue(14, 6, "");
			// 毛額A-非具控制與從屬關係(F25)
			makeExcel.setValue(25, 6, "");
			// 毛額A-政策性之專案運用及公共投資(F28)
			makeExcel.setValue(28, 6, "");
			// 毛額A-專案放款-不動產-第2類(F32)
			makeExcel.setValue(32, 6, "");
			// 毛額A-專案放款-不動產-第3類(F33)
			makeExcel.setValue(33, 6, "");
			// 毛額A-專案放款-授信限制對象(F37)
			makeExcel.setValue(37, 6, "");
			// 毛額A-放款合計(F41)
			makeExcel.setValue(41, 6, "");

			// 毛額B-利害關係人-非子/母公司(G25)
			makeExcel.setValue(25, 7, "");

			// A表-不動產-第3類(J9)
			makeExcel.setValue(9, 9, "");
			// A表-不動產-第3類(J41)
			makeExcel.setValue(41, 9, "");

			// E6~K41 重整
			for (int i = 5; i <= 11; i++) {
				for (int j = 6; j <= 41; j++) {

					makeExcel.formulaCaculate(j, i);

				}
			}

		} else {
			makeExcel.setValue(5, 6, "本日無資料");
		}

	}

	/**
	 * 明細表
	 */
	private void exportExcel3(TitaVo titaVo) throws LogicException {

		makeExcel.setSheet("明細表");

		// 日期
		makeExcel.formulaCaculate(2, 2);

		// 標題 B2
		makeExcel.formulaCaculate(2, 2);

		// 各項金額 C5~C27
		for (int i = 5; i <= 27; i++) {
			makeExcel.formulaCaculate(i, 2);
		}

		// 備註 A28
		makeExcel.formulaCaculate(1, 28);

	}

	public void updData() {

	}

}
