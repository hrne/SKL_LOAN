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
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM052ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Service
@Scope("prototype")

public class LM052Report extends MakeReport {

	@Autowired
	LM052ServiceImpl lM052ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		// 取得會計日(同頁面上會計日)
		// 年月日
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		// 月
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		Calendar calendar = Calendar.getInstance();

		// 設當年月底日
		// calendar.set(iYear, iMonth, 0);
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth - 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		// 星期 X (排除六日用) 代號 0~6對應 日到六
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		// 月底日有卡在六日，需往前推日期
		int diff = -(day == 1 ? 2 : (day == 6 ? 1 : 0));
		calendar.add(Calendar.DATE, diff);

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// 當前日期
		int nowDate = Integer.valueOf(iEntdy);
		// 以當前月份取得月底日期 並格式化處理
		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		// 確認是否為1月
		boolean isMonthZero = iMonth - 1 == 0;

		// 當前日期 比 當月底日期 前面 就取上個月底日
		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		calendar.set(iYear, iMonth - 1, 0);

		int lyymm = (Integer.valueOf(dateFormat.format(calendar.getTime())) - 19110000) / 100;

		this.info("LM052Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM052", "放款資產分類-會計部備呆計提", "LM052_放款資產分類-會計部備呆計提",
				"LM052_底稿_放款資產分類-會計部備呆計提.xlsx", "備呆總表");

		String formTitle = "";

		formTitle = (iYear - 1911) + "年 " + String.format("%02d", iMonth) + "    放款資產品質分類";
		makeExcel.setValue(1, 1, formTitle);

		formTitle = lyymm + "\n" + "放款總額";
		makeExcel.setValue(15, 3, formTitle, "C");

		List<Map<String, String>> lM052List = null;

		for (int formNum = 1; formNum <= 4; formNum++) {

			try {

				lM052List = lM052ServiceImpl.findAll(titaVo, formNum);

			} catch (Exception e) {

				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM052ServiceImpl.findAll error = " + errors.toString());

			}

			exportExcel(lM052List, formNum);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	/*
	 * 應收利息提列2%：用MonthlyFacBal的應收利息加總、前者*0.02
	 * 
	 * 五類資產評估合計：M13+M14 1-5類總額(含應收息)提列1%：F13+L14 *0.01 無擔保案件總金額：F11
	 * 
	 * 法定備抵損失提撥(含應收息1%)：特定和非特定資產的提存金額 (G28+L28)+ 應收利息提列2%(L14)
	 * 
	 * 
	 * 問題： B30的 第三項 最後面金額 本月預期損失金額 30.89百萬元 (從IFRS9) 第四項 的4月份 十計提列 638.283百萬元
	 * 
	 * 
	 */
	private void exportExcel(List<Map<String, String>> LDList, int formNum) throws LogicException {

		BigDecimal amt = BigDecimal.ZERO;

		if (LDList.size() > 0) {

			int row = 0;
			int col = 0;

			for (Map<String, String> tLDVo : LDList) {

				switch (formNum) {
				case 1:

					row = "11".equals(tLDVo.get("F0")) ? 4
							: "12".equals(tLDVo.get("F0")) ? 5
									: "21".equals(tLDVo.get("F0")) ? 6
											: "22".equals(tLDVo.get("F0")) ? 7
													: "23".equals(tLDVo.get("F0")) ? 8
															: "3".equals(tLDVo.get("F0")) ? 9
																	: "4".equals(tLDVo.get("F0")) ? 10
																			: "5".equals(tLDVo.get("F0")) ? 11
																					: "6".equals(tLDVo.get("F0")) ? 12
																							: 14;

					col = "00A".equals(tLDVo.get("F1")) ? 3
							: "201".equals(tLDVo.get("F1")) ? 4
									: "6".equals(tLDVo.get("F0")) && "999".equals(tLDVo.get("F1")) ? 6 : 12;

					amt = tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

					break;
				case 2:

					row = "1".equals(tLDVo.get("F0")) ? 17
							: "21".equals(tLDVo.get("F0")) ? 18
									: "22".equals(tLDVo.get("F0")) ? 19
											: "23".equals(tLDVo.get("F0")) ? 20
													: "3".equals(tLDVo.get("F0")) ? 21
															: "4".equals(tLDVo.get("F0")) ? 22
																	: "5".equals(tLDVo.get("F0")) ? 23 : 24;

					col = 3;

					amt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));

					break;
				case 3:

					row = "1".equals(tLDVo.get("F0")) ? 16 : "2".equals(tLDVo.get("F0")) ? 17 : 18;

					col = "310".equals(tLDVo.get("F1")) ? 9 : "320".equals(tLDVo.get("F1")) ? 8 : 7;

					amt = tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

					break;

				case 4:
					row = 27;

					col = "S1".equals(tLDVo.get("F0")) ? 7
							: "S2".equals(tLDVo.get("F0")) ? 8
									: "NS1".equals(tLDVo.get("F0")) ? 9 : "NS2".equals(tLDVo.get("F0")) ? 11 : 12;

					amt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));

					break;

				case 5:
					row = 27;

					col = 15;

					amt = tLDVo.get("F0").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F0"));

					break;
				}

				makeExcel.setValue(row, col, amt, "#,##0");

			}

			// C13 D13 E13
			makeExcel.formulaCaculate(13, 3);
			makeExcel.formulaCaculate(13, 4);
			makeExcel.formulaCaculate(13, 5);

			// F4~F11
			for (int r = 4; r <= 11; r++) {
				makeExcel.formulaCaculate(r, 6);
			}

			// F13
			makeExcel.formulaCaculate(13, 6);

			// J4~M13 M14
			for (int r = 4; r <= 13; r++) {
				for (int c = 10; c <= 13; c++) {
					makeExcel.formulaCaculate(r, c);
				}
			}

			makeExcel.formulaCaculate(14, 13);

			// C25 D17~D25
			makeExcel.formulaCaculate(25, 3);
			for (int r = 17; r <= 25; r++) {
				makeExcel.formulaCaculate(r, 4);
			}

			// G19 H19 I19 G20
			makeExcel.formulaCaculate(19, 7);
			makeExcel.formulaCaculate(19, 8);
			makeExcel.formulaCaculate(19, 9);
			makeExcel.formulaCaculate(20, 7);

			// M16~18
			makeExcel.formulaCaculate(16, 13);
			makeExcel.formulaCaculate(17, 13);
			makeExcel.formulaCaculate(18, 13);

			// G28 L28 M24
			makeExcel.formulaCaculate(28, 7);
			makeExcel.formulaCaculate(28, 12);
			makeExcel.formulaCaculate(24, 13);

			// B30
			makeExcel.formulaCaculate(30, 2);

			// set height row 1,4~13,23,24
			makeExcel.setHeight(1, 40);

			for (int r = 4; r <= 13; r++) {
				makeExcel.setHeight(r, 40);
			}

			makeExcel.setHeight(22, 20);
			makeExcel.setHeight(23, 40);

		}

	}

}