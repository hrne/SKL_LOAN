package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM014ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM014Report extends MakeReport {

	// 表別
	String rptType = "";

	// 表的資料群組別
	String rptDataType = "";

	// 合計資料容器
	List<Map<String, String>> listLM014Total = null;

	// 合計-本月利息收入
	BigDecimal thisMonthIntAmtTotal = BigDecimal.ZERO;
	// 合計-本月月底餘額
	BigDecimal thisMonthLoanBalTotal = BigDecimal.ZERO;
	// 合計-本月加權
	BigDecimal thisMonthWeightedTotal = BigDecimal.ZERO;
	// 合計-加權平均利率
	BigDecimal thisMonthWeightedAvgRateTotal;
	// 合計-累計利息收入
	BigDecimal thisYearIntAmtTotal = BigDecimal.ZERO;
	// 合計-每月平均放款餘額
	BigDecimal thisYearLoanBalTotal = BigDecimal.ZERO;
	// 合計-每月加權
	BigDecimal thisYearWeightedTotal = BigDecimal.ZERO;
	// 合計-加權平均利率(年度)
	BigDecimal thisYearWeightedAvgRateTotal;

	// 小計-本月利息收入
	BigDecimal thisMonthIntAmtSum = BigDecimal.ZERO;
	// 小計-佔率(本月利息收入)
	BigDecimal thisMonthIntAmtPercentSum = BigDecimal.ZERO;
	// 小計-本月月底餘額
	BigDecimal thisMonthLoanBalSum = BigDecimal.ZERO;
	// 小計-佔率(本月月底餘額)
	BigDecimal thisMonthLoanBalPercentSum = BigDecimal.ZERO;
	// 小計-本月加權
	BigDecimal thisMonthWeightedSum = BigDecimal.ZERO;
	// 小計-加權平均利率
	BigDecimal thisMonthWeightedAvgRateSum;
	// 小計-累計利息收入
	BigDecimal thisYearIntAmtSum = BigDecimal.ZERO;
	// 小計-佔率(累計利息收入)
	BigDecimal thisYearIntAmtPercentSum = BigDecimal.ZERO;
	// 小計-每月平均放款餘額
	BigDecimal thisYearLoanBalSum = BigDecimal.ZERO;
	// 小計-佔率(每月平均放款餘額)
	BigDecimal thisYearLoanBalPercentSum = BigDecimal.ZERO;
	// 小計-每月加權
	BigDecimal thisYearWeightedSum = BigDecimal.ZERO;
	// 小計-加權平均利率(年度)
	BigDecimal thisYearWeightedAvgRateSum;

	private final BigDecimal oneHundred = new BigDecimal("100");

	@Autowired
	LM014ServiceImpl lM014ServiceImpl;

	@Override
	public void printHeader() {

		// 設定字體大小
		this.setFontSize(8);

		this.print(-1, 146, "機密等級：密");

		this.print(-2, 3, "程式ＩＤ：" + this.getParentTranCode());
		this.print(-2, 86, "新光人壽保險股份有限公司", "C");
		this.print(-2, 146, "日　　期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));

		this.print(-3, 3, "報　　表：" + this.getRptCode());
		this.print(-3, 86, "平均利率月報表《" + rptType + "》", "C");
		this.print(-3, 146, "時　　間：" + hour + ":" + min + ":" + sec);

		this.print(-4, 146, "頁　　數：" + this.getNowPage());

		this.print(-5, 86, getshowRocDate(this.getReportDate()).substring(0, 7), "C");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);
	}

	String year;
	String month;
	String day;

	String hour;
	String min;
	String sec;

	private static final Logger logger = LoggerFactory.getLogger(LM014Report.class);

	public Boolean exec(TitaVo titaVo) throws LogicException {

		getNowDateTime();
		
		int showType = Integer.parseInt(titaVo.getParam("inputType"));
		
		String[] typeText = {"全部","科目別","種類別","關係人","種類別+關係人"};
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM014", "平均利率月報表_"+typeText[showType], "密", "A4", "L");

		// 表1~表4:總表
		rptType = "總表";

		listLM014Total = null;

		// 取總表的合計金額
		try {
			listLM014Total = lM014ServiceImpl.getTotalAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
		

		// 計算合計金額
		computeTotal();

		switch (showType)
		{
		case 1:
		doReport1(titaVo); // 表1:總表-放款科目別
		break;
		case 2:
		doReport2(titaVo); // 表2:總表-放款種類別
		break;
		case 3:
		doReport3(titaVo); // 表3:總表-關係人別
		break;
		case 4:
		doReport4(titaVo); // 表4:總表-放款種類別&關係人
		break;
		default:
		doReport1(titaVo);
		doReport2(titaVo);
		doReport3(titaVo);
		doReport4(titaVo);
		break;
		}

		// 表5~表8:企金
		rptType = "企金";

		listLM014Total = null;

		// 取企金的合計金額
		try {
			listLM014Total = lM014ServiceImpl.getTotalEnt(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		// 計算合計金額
		computeTotal();

		switch (showType)
		{
		case 1:
		doReport5(titaVo); // 表5:企金-放款科目別
		break;
		case 2:
		doReport6(titaVo); // 表6:企金-放款種類別
		break;
		case 3:
		doReport7(titaVo); // 表7:企金-關係人別
		break;
		case 4:
		doReport8(titaVo); // 表8:企金-放款種類別&關係人
		break;
		default:
		doReport5(titaVo);
		doReport6(titaVo);
		doReport7(titaVo);
		doReport8(titaVo);
		break;
		}

		// 表9~表12:房貸
		rptType = "房貸";

		listLM014Total = null;

		// 取房貸的合計金額
		try {
			listLM014Total = lM014ServiceImpl.getTotalHouse(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		// 計算合計金額
		computeTotal();

		switch (showType)
		{
		case 1:
		doReport9(titaVo); // 表9:房貸-放款科目別
		break;
		case 2:
		doReport10(titaVo); // 表10:房貸-放款種類別
		break;
		case 3:
		doReport11(titaVo); // 表11:房貸-關係人別
		break;
		case 4:
		doReport12(titaVo); // 表12:房貸-放款種類別&關係人
		break;
		default:
		doReport9(titaVo);
		doReport10(titaVo);
		doReport11(titaVo);
		doReport12(titaVo);
		break;
		}

		// 表13~表16:企金通路
		rptType = "企金通路";

		listLM014Total = null;

		// 取企金通路的合計金額
		try {
			listLM014Total = lM014ServiceImpl.getTotalEntChannel(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		// 計算合計金額
		computeTotal();

		switch (showType)
		{
		case 1:
		doReport13(titaVo); // 表13:企金通路-放款科目別
		break;
		case 2:
		doReport14(titaVo); // 表14:企金通路-放款種類別
		break;
		case 3:
		doReport15(titaVo); // 表15:企金通路-關係人別
		break;
		case 4:
		doReport16(titaVo); // 表16:企金通路-放款種類別&關係人
		break;
		default:
		doReport13(titaVo);
		doReport14(titaVo);
		doReport15(titaVo);
		doReport16(titaVo);
		break;
		}

		// 表17~表18:非企金通路
		rptType = "非企金通路";

		listLM014Total = null;

		// 取非企金通路的合計金額
		try {
			listLM014Total = lM014ServiceImpl.getTotalNotEntChannel(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		// 計算合計金額
		computeTotal();

		switch (showType)
		{
		case 1:
		doReport17(titaVo); // 表17:非企金通路-放款科目別
		break;
		case 2:
		doReport18(titaVo); // 表18:非企金通路-放款種類別
		break;
		case 3:
		doReport19(titaVo); // 表19:非企金通路-關係人別
		break;
		case 4:
		doReport20(titaVo); // 表20:非企金通路-放款種類別&關係人
		break;
		default:
		doReport17(titaVo);
		doReport18(titaVo);
		doReport19(titaVo);
		doReport20(titaVo);
		break;
		}
		long sno = this.close();
		this.toPdf(sno);
		
		return true;
	}

	/**
	 * 總表-放款科目別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport1(TitaVo titaVo) {

		// 表1:總表-放款科目別
		rptDataType = "放款科目別";

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail1(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeA(listLM014);

	}

	/**
	 * 總表-放款種類別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport2(TitaVo titaVo) {

		// 表2:總表-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail2(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeA(listLM014);

	}

	/**
	 * 總表-關係人別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport3(TitaVo titaVo) {

		// 表3:總表-關係人別
		rptDataType = "關係人別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail3(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 總表-放款種類別&關係人<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport4(TitaVo titaVo) {

		// 表4:總表-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail4(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 企金-放款科目別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport5(TitaVo titaVo) {

		// 表5:企金-放款科目別
		rptDataType = "放款科目別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail5(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 企金-放款種類別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport6(TitaVo titaVo) {

		// 表6:企金-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail6(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 企金-關係人別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport7(TitaVo titaVo) {

		// 表7:企金-關係人別
		rptDataType = "關係人別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail7(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 企金-放款種類別&關係人<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport8(TitaVo titaVo) {

		// 表8:企金-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail8(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 房貸-放款科目別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport9(TitaVo titaVo) {

		// 表9:房貸-放款科目別
		rptDataType = "放款科目別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail9(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeA(listLM014);

	}

	/**
	 * 房貸-放款種類別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport10(TitaVo titaVo) {

		// 表10:房貸-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail10(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 房貸-關係人別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport11(TitaVo titaVo) {

		// 表11:房貸-關係人別
		rptDataType = "關係人別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail11(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 房貸-放款種類別&關係人<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport12(TitaVo titaVo) {

		// 表12:房貸-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail12(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 企金通路-放款科目別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport13(TitaVo titaVo) {

		// 表13:企金通路-放款科目別
		rptDataType = "放款科目別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail13(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeA(listLM014);

	}

	/**
	 * 企金通路-放款種類別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport14(TitaVo titaVo) {

		// 表14:企金通路-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail14(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeA(listLM014);

	}

	/**
	 * 企金通路-關係人別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport15(TitaVo titaVo) {

		// 表15:企金通路-關係人別
		rptDataType = "關係人別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail15(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 企金通路-放款種類別&關係人<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport16(TitaVo titaVo) {

		// 表16:企金通路-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail16(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 非企金通路-放款科目別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport17(TitaVo titaVo) {

		// 表17:非企金通路-放款科目別
		rptDataType = "放款科目別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail17(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeA(listLM014);

	}

	/**
	 * 非企金通路-放款種類別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport18(TitaVo titaVo) {

		// 表18:非企金通路-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail18(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeA(listLM014);

	}

	/**
	 * 非企金通路-關係人別<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport19(TitaVo titaVo) {

		// 表19:非企金通路-關係人別
		rptDataType = "關係人別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail19(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	/**
	 * 非企金通路-放款種類別&關係人<BR>
	 * 
	 * @param titaVo titaVo
	 */
	private void doReport20(TitaVo titaVo) {

		// 表20:非企金通路-放款種類別
		rptDataType = "放款種類別";

		this.newPage();

		List<Map<String, String>> listLM014 = null;

		try {
			listLM014 = lM014ServiceImpl.getDetail20(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}

		exportReportTypeB(listLM014);

	}

	private void exportReportTypeA(List<Map<String, String>> listLM014) {

		if (listLM014 != null && listLM014.size() != 0) {

			// 更新"累計利息收入"的合計數字
			computeIntTotal(listLM014);
			
			this.print(2, 1,
					"┌──────────────┬─────────────────────────────────────┬─────────────────────────────────────┐");
			this.print(1, 1,
					"│　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　本　　月　　份　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　年　初　到　本　月　份　　　　　　　　　　　　│");
			this.print(1, 1,
					"│　　　　　　　　　　　　　　├────────┬────┬──────────┬────┬───────┼────────┬────┬──────────┬────┬───────┤");
			this.print(0, 7, rptDataType);
			this.print(1, 1,
					"│　　　　　　　　　　　　　　│　本月利息收入　│　佔率　│　　本月月底餘額　　│　佔率　│　平均利率％　│　累計利息收入　│　佔率　│　每月平均放款餘額　│　佔率　│　平均利率％　│");

			String lastGroupType = listLM014.get(0).get("F0");
			String lastGroupTypeName = listLM014.get(0).get("F1");
			String lastSecGroupType = listLM014.get(0).get("F2");
			String lastSecGroupTypeName = listLM014.get(0).get("F3");

			this.print(1, 1,
					"├──────────────┼────────┼────┼──────────┼────┼───────┼────────┼────┼──────────┼────┼───────┤");
			this.print(1, 1,
					"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
			this.print(0, 4, lastGroupTypeName);
			this.print(0, 18, lastSecGroupTypeName);

			for (int i = 0; i < listLM014.size(); i++) {

				Map<String, String> tLM014 = listLM014.get(i);

				// F0 = 群組類別(不同表時群組條件不同)
				String groupType = tLM014.get("F0");

				// F1 = 群組類別中文名稱
				String groupTypeName = tLM014.get("F1");

				// F2 = 次要群組類別
				String secGroupType = tLM014.get("F2");

				// F3 = 次要群組類別中文
				String secGroupTypeName = tLM014.get("F3");

				// F4 = 本月利息收入
				BigDecimal thisMonthIntAmt = SafelyBigDecimal(tLM014.get("F4"));

				// 佔率(本月利息收入)
				BigDecimal thisMonthIntAmtPercent = computeDivide(thisMonthIntAmt, thisMonthIntAmtTotal, 3)
						.multiply(oneHundred);

				// F5 = 本月放款餘額
				BigDecimal thisMonthLoanBal = SafelyBigDecimal(tLM014.get("F5"));

				// 佔率(本月放款餘額)
				BigDecimal thisMonthLoanBalPercent = computeDivide(thisMonthLoanBal, thisMonthLoanBalTotal, 3)
						.multiply(oneHundred);

				// F6 = 本月加權
				BigDecimal thisMonthWeighted = SafelyBigDecimal(tLM014.get("F6"));

				// 加權平均利率
				BigDecimal thisMonthWeightedAvgRate = computeDivide(thisMonthWeighted, thisMonthLoanBal, 3);

				// 2021-03-09 原程式是由本月月底餘額 * 加權平均利率 / 100 /12
//				BigDecimal thisMonthIntAmt = computeDivide(thisMonthLoanBal.multiply(thisMonthWeightedAvgRate),
//						new BigDecimal("1200"), 0);

				// 佔率(本月利息收入)
				// 2021-03-09 原程式是由本月月底餘額 * 加權平均利率 / 100 /12
//				BigDecimal thisMonthIntAmtPercent = computeDivide(thisMonthIntAmt, thisMonthIntAmtTotal, 3)
//						.multiply(oneHundred);

				// F7 = 累計利息收入
				BigDecimal thisYearIntAmt = SafelyBigDecimal(tLM014.get("F7"));

				// 佔率(累計利息收入)
				BigDecimal thisYearIntAmtPercent = computeDivide(thisYearIntAmt, thisYearIntAmtTotal, 3)
						.multiply(oneHundred);

				// F8 = 每月平均放款餘額
				BigDecimal thisYearLoanBal = SafelyBigDecimal(tLM014.get("F8"));

				// 佔率(每月平均放款餘額)
				BigDecimal thisYearLoanBalPercent = computeDivide(thisYearLoanBal, thisYearLoanBalTotal, 3)
						.multiply(oneHundred);

				// F9 = 每月平均放款餘額加權
				BigDecimal thisYearWeighted = SafelyBigDecimal(tLM014.get("F9"));

				// 加權平均利率(年度)
				BigDecimal thisYearWeightedAvgRate = computeDivide(thisYearWeighted, thisYearLoanBal, 3);

				// 2021-03-09 原程式是由每月平均放款餘額 * 加權平均利率(年度) / 100 /12
//				BigDecimal thisYearIntAmt = computeDivide(thisYearLoanBal.multiply(thisYearWeightedAvgRate),
//						new BigDecimal("1200"), 0);

				// 佔率(本月利息收入)
				// 2021-03-09 原程式是由每月平均放款餘額 * 加權平均利率(年度) / 100 /12
//				BigDecimal thisYearIntAmtPercent = computeDivide(thisYearIntAmt, thisYearIntAmtTotal, 3)
//						.multiply(oneHundred);

				if (!lastGroupType.equals(groupType)) { // 群組類別與上一筆不相同

					// 印小計
					printSum();

					// 換行並列印 群組類別中文名稱 及 次要群組類別中文名稱
					this.print(1, 1,
							"├──────────────┼────────┼────┼──────────┼────┼───────┼────────┼────┼──────────┼────┼───────┤");
					this.print(1, 1,
							"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
					this.print(0, 4, groupTypeName);
					this.print(0, 18, secGroupTypeName);

				} else if (!lastSecGroupType.equals(secGroupType)) { // 群組類別與上一筆相同 但次要群組類別與上一筆不相同

					// 換行並列印 次要群組類別中文名稱
					this.print(1, 1,
							"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
					this.print(0, 18, secGroupTypeName);

				}

				// 列印本行資料
				this.print(0, 44, formatAmt(thisMonthIntAmt, 0), "R");
				this.print(0, 52, formatAmt(thisMonthIntAmtPercent, 1), "R");
				this.print(0, 72, formatAmt(thisMonthLoanBal, 0), "R");
				this.print(0, 81, formatAmt(thisMonthLoanBalPercent, 1), "R");
				this.print(0, 95, formatAmt(thisMonthWeightedAvgRate, 3), "R");
				this.print(0, 112, formatAmt(thisYearIntAmt, 0), "R");
				this.print(0, 121, formatAmt(thisYearIntAmtPercent, 1), "R");
				this.print(0, 141, formatAmt(thisYearLoanBal, 0), "R");
				this.print(0, 150, formatAmt(thisYearLoanBalPercent, 1), "R");
				this.print(0, 164, formatAmt(thisYearWeightedAvgRate, 3), "R");

				// 將本行資料加入小計
				thisMonthIntAmtSum = thisMonthIntAmtSum.add(thisMonthIntAmt);
				thisMonthLoanBalSum = thisMonthLoanBalSum.add(thisMonthLoanBal);
				thisMonthWeightedSum = thisMonthWeightedSum.add(thisMonthWeighted);
				thisYearIntAmtSum = thisYearIntAmtSum.add(thisYearIntAmt);
				thisYearLoanBalSum = thisYearLoanBalSum.add(thisYearLoanBal);
				thisYearWeightedSum = thisYearWeightedSum.add(thisYearWeighted);

				// 更新群組類別及次要群組類別，供下一筆比較
				lastSecGroupType = secGroupType;
				lastGroupType = groupType;

			} // for

			// 印小計
			printSum();

			// 印合計
			printTotal();

		} else {
			noData();
		}

	}

	private void exportReportTypeB(List<Map<String, String>> listLM014) {

		if (listLM014 != null && listLM014.size() != 0) {

			// 更新"累計利息收入"的合計數字
			computeIntTotal(listLM014);

			this.print(2, 1,
					"┌──────────────┬─────────────────────────────────────┬─────────────────────────────────────┐");
			this.print(1, 1,
					"│　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　本　　月　　份　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　年　初　到　本　月　份　　　　　　　　　　　　│");
			this.print(1, 1,
					"│　　　　　　　　　　　　　　├────────┬────┬──────────┬────┬───────┼────────┬────┬──────────┬────┬───────┤");
			this.print(0, 7, rptDataType);
			this.print(1, 1,
					"│　　　　　　　　　　　　　　│　本月利息收入　│　佔率　│　　本月月底餘額　　│　佔率　│　平均利率％　│　累計利息收入　│　佔率　│　每月平均放款餘額　│　佔率　│　平均利率％　│");

			String lastGroupType = listLM014.get(0).get("F0");
			String lastGroupTypeName = listLM014.get(0).get("F1");

			this.print(1, 1,
					"├──────────────┼────────┼────┼──────────┼────┼───────┼────────┼────┼──────────┼────┼───────┤");
			this.print(1, 1,
					"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
			this.print(0, 4, lastGroupTypeName);

			for (int i = 0; i < listLM014.size(); i++) {

				Map<String, String> tLM014 = listLM014.get(i);

				// F0 = 群組類別(不同表時群組條件不同)
				String groupType = tLM014.get("F0");

				// F1 = 群組類別中文名稱
				String groupTypeName = tLM014.get("F1");

				// F2 = 本月利息收入
				BigDecimal thisMonthIntAmt = SafelyBigDecimal(tLM014.get("F2"));

				// 佔率(本月利息收入)
				BigDecimal thisMonthIntAmtPercent = computeDivide(thisMonthIntAmt, thisMonthIntAmtTotal, 3)
						.multiply(oneHundred);

				// F3 = 本月放款餘額
				BigDecimal thisMonthLoanBal = SafelyBigDecimal(tLM014.get("F3"));

				// 佔率(本月放款餘額)
				BigDecimal thisMonthLoanBalPercent = computeDivide(thisMonthLoanBal, thisMonthLoanBalTotal, 3)
						.multiply(oneHundred);

				// F4 = 本月加權
				BigDecimal thisMonthWeighted = SafelyBigDecimal(tLM014.get("F4"));

				// 加權平均利率
				BigDecimal thisMonthWeightedAvgRate = computeDivide(thisMonthWeighted, thisMonthLoanBal, 3);

				// 2021-03-09 原程式是由本月月底餘額 * 加權平均利率 / 100 /12
//				BigDecimal thisMonthIntAmt = computeDivide(thisMonthLoanBal.multiply(thisMonthWeightedAvgRate),
//						new BigDecimal("1200"), 0);

				// 佔率(本月利息收入)
				// 2021-03-09 原程式是由本月月底餘額 * 加權平均利率 / 100 /12
//				BigDecimal thisMonthIntAmtPercent = computeDivide(thisMonthIntAmt, thisMonthIntAmtTotal, 3)
//						.multiply(oneHundred);

				// F5 = 累計利息收入
				// 2021-03-09 原程式是由每月平均放款餘額 * 加權平均利率(年度) / 100 /12
				BigDecimal thisYearIntAmt = SafelyBigDecimal(tLM014.get("F5"));

				// 佔率(累計利息收入)
				// 2021-03-09 原程式是由每月平均放款餘額 * 加權平均利率(年度) / 100 /12
				BigDecimal thisYearIntAmtPercent = computeDivide(thisYearIntAmt, thisYearIntAmtTotal, 3)
						.multiply(oneHundred);

				// F6 = 每月平均放款餘額
				BigDecimal thisYearLoanBal = SafelyBigDecimal(tLM014.get("F6"));

				// 佔率(每月平均放款餘額)
				BigDecimal thisYearLoanBalPercent = computeDivide(thisYearLoanBal, thisYearLoanBalTotal, 3)
						.multiply(oneHundred);

				// F7 = 每月平均放款餘額加權
				BigDecimal thisYearWeighted = SafelyBigDecimal(tLM014.get("F7"));

				// 加權平均利率(年度)
				BigDecimal thisYearWeightedAvgRate = computeDivide(thisYearWeighted, thisYearLoanBal, 3);

				// 2021-03-09 原程式是由每月平均放款餘額 * 加權平均利率(年度) / 100 /12
//				BigDecimal thisYearIntAmt = computeDivide(thisYearLoanBal.multiply(thisYearWeightedAvgRate),
//						new BigDecimal("1200"), 0);

				// 佔率(本月利息收入)
				// 2021-03-09 原程式是由每月平均放款餘額 * 加權平均利率(年度) / 100 /12
//				BigDecimal thisYearIntAmtPercent = computeDivide(thisYearIntAmt, thisYearIntAmtTotal, 3)
//						.multiply(oneHundred);

				if (!lastGroupType.equals(groupType)) { // 群組類別與上一筆不相同

					// 換行並列印 群組類別中文名稱
					this.print(1, 1,
							"├──────────────┼────────┼────┼──────────┼────┼───────┼────────┼────┼──────────┼────┼───────┤");
					this.print(1, 1,
							"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
					this.print(0, 4, groupTypeName);

				}

				// 列印本行資料
				this.print(0, 44, formatAmt(thisMonthIntAmt, 0), "R");
				this.print(0, 52, formatAmt(thisMonthIntAmtPercent, 1), "R");
				this.print(0, 72, formatAmt(thisMonthLoanBal, 0), "R");
				this.print(0, 81, formatAmt(thisMonthLoanBalPercent, 1), "R");
				this.print(0, 95, formatAmt(thisMonthWeightedAvgRate, 3), "R");
				this.print(0, 112, formatAmt(thisYearIntAmt, 0), "R");
				this.print(0, 121, formatAmt(thisYearIntAmtPercent, 1), "R");
				this.print(0, 141, formatAmt(thisYearLoanBal, 0), "R");
				this.print(0, 150, formatAmt(thisYearLoanBalPercent, 1), "R");
				this.print(0, 164, formatAmt(thisYearWeightedAvgRate, 3), "R");

				// 更新群組類別，供下一筆比較
				lastGroupType = groupType;

			} // for

			// 印合計
			printTotal();

		} else {
			noData();
		}

	}

	/**
	 * 印小計
	 */
	private void printSum() {

		// 計算:小計-佔率(本月利息收入)
		thisMonthIntAmtPercentSum = computeDivide(thisMonthIntAmtSum, thisMonthIntAmtTotal, 3).multiply(oneHundred);
		// 計算:小計-佔率(本月月底餘額)
		thisMonthLoanBalPercentSum = computeDivide(thisMonthLoanBalSum, thisMonthLoanBalTotal, 3).multiply(oneHundred);
		// 計算:小計-加權平均利率
		thisMonthWeightedAvgRateSum = computeDivide(thisMonthWeightedSum, thisMonthLoanBalSum, 3);
		// 計算:小計-佔率(累計利息收入)
		thisYearIntAmtPercentSum = computeDivide(thisYearIntAmtSum, thisYearIntAmtTotal, 3).multiply(oneHundred);
		// 計算:小計-佔率(每月平均放款餘額)
		thisYearLoanBalPercentSum = computeDivide(thisYearLoanBalSum, thisYearLoanBalTotal, 3).multiply(oneHundred);
		// 計算:小計-加權平均利率(年度)
		thisYearWeightedAvgRateSum = computeDivide(thisYearWeightedSum, thisYearLoanBalSum, 3);

		// 列印群組類別小計
		this.print(1, 1,
				"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
		this.print(0, 18, "小　　計");
		this.print(0, 44, formatAmt(thisMonthIntAmtSum, 0), "R");
		this.print(0, 52, formatAmt(thisMonthIntAmtPercentSum, 1), "R");
		this.print(0, 72, formatAmt(thisMonthLoanBalSum, 0), "R");
		this.print(0, 81, formatAmt(thisMonthLoanBalPercentSum, 1), "R");
		this.print(0, 95, formatAmt(thisMonthWeightedAvgRateSum, 3), "R");
		this.print(0, 112, formatAmt(thisYearIntAmtSum, 0), "R");
		this.print(0, 121, formatAmt(thisYearIntAmtPercentSum, 1), "R");
		this.print(0, 141, formatAmt(thisYearLoanBalSum, 0), "R");
		this.print(0, 150, formatAmt(thisYearLoanBalPercentSum, 1), "R");
		this.print(0, 164, formatAmt(thisYearWeightedAvgRateSum, 3), "R");

		cleanSum();
	}

	/**
	 * 將小計歸零
	 */
	private void cleanSum() {
		thisMonthIntAmtSum = BigDecimal.ZERO;
		thisMonthIntAmtPercentSum = BigDecimal.ZERO;
		thisMonthLoanBalSum = BigDecimal.ZERO;
		thisMonthLoanBalPercentSum = BigDecimal.ZERO;
		thisMonthWeightedSum = BigDecimal.ZERO;
		thisYearIntAmtSum = BigDecimal.ZERO;
		thisYearIntAmtPercentSum = BigDecimal.ZERO;
		thisYearLoanBalSum = BigDecimal.ZERO;
		thisYearLoanBalPercentSum = BigDecimal.ZERO;
		thisYearWeightedSum = BigDecimal.ZERO;
	}

	private BigDecimal SafelyBigDecimal(String s) {
		try {
			return new BigDecimal(s);
		} catch (Exception e)
		{
			this.warn("LM014Report trying to turn "+ s +" into BigDecimal, failed.");
			this.warn("Reason: " + e);
			return BigDecimal.ZERO;
		}
	}
	
	private void computeTotal() {

		cleanTotal();

		if (listLM014Total != null && listLM014Total.size() != 0) {

			Map<String, String> tLM014Total = listLM014Total.get(0);

			// F0 = 合計-本月利息收入
			// 2021-03-09 原程式是由本月月底餘額 * 加權平均利率 / 100 /12
//			thisMonthIntAmtTotal = tLM014Total.get("F0") == null ? BigDecimal.ZERO
//					: new BigDecimal(tLM014Total.get("F0"));

			// F1 = 合計-本月月底餘額
			this.info("LM014 tLM014Total F1 is... " + tLM014Total.get("F1"));
			thisMonthLoanBalTotal = SafelyBigDecimal(tLM014Total.get("F1"));

			// F2 = 合計-本月加權
			thisMonthWeightedTotal = SafelyBigDecimal(tLM014Total.get("F2"));

			// 合計-加權平均利率
			thisMonthWeightedAvgRateTotal = computeDivide(thisMonthWeightedTotal, thisMonthLoanBalTotal, 3);

			// 2021-03-09 原程式是由本月月底餘額 * 加權平均利率 / 100 /12
			thisMonthIntAmtTotal = computeDivide(thisMonthLoanBalTotal.multiply(thisMonthWeightedAvgRateTotal),
					new BigDecimal("1200"), 0);

			// F3 = 合計-累計利息收入
			// 改用累加
//			thisYearIntAmtTotal = tLM014Total.get("F3") == null ? BigDecimal.ZERO
//					: new BigDecimal(tLM014Total.get("F3"));
			// F4 = 合計-每月平均放款餘額
			thisYearLoanBalTotal = SafelyBigDecimal(tLM014Total.get("F4"));
			
			// F5 = 合計-每月加權
			thisYearWeightedTotal = SafelyBigDecimal(tLM014Total.get("F5"));

			// 合計-加權平均利率(年度)
			thisYearWeightedAvgRateTotal = computeDivide(thisYearWeightedTotal, thisYearLoanBalTotal, 3);

		}
	}

	/**
	 * 合計-累計利息收入
	 * 
	 * @param listLM014 listLM014
	 */
	private void computeIntTotal(List<Map<String, String>> listLM014) {

		thisYearIntAmtTotal = BigDecimal.ZERO;

		for (Map<String, String> tLM014 : listLM014) {

			String value = tLM014.get("F7");

			BigDecimal intAmt = SafelyBigDecimal(value);

			thisYearIntAmtTotal = thisYearIntAmtTotal.add(intAmt);
		}

	}

	/**
	 * 印合計
	 */
	private void printTotal() {

		this.print(1, 1,
				"├──────────────┼────────┼────┼──────────┼────┼───────┼────────┼────┼──────────┼────┼───────┤");
		this.print(1, 1,
				"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
		this.print(0, 5, "　　合　　　　　　計");

		this.print(0, 44, formatAmt(thisMonthIntAmtTotal, 0), "R");
		this.print(0, 52, "***", "R");
		this.print(0, 72, formatAmt(thisMonthLoanBalTotal, 0), "R");
		this.print(0, 81, "***", "R");
		this.print(0, 95, formatAmt(thisMonthWeightedAvgRateTotal, 3), "R");
		this.print(0, 112, formatAmt(thisYearIntAmtTotal, 0), "R");
		this.print(0, 121, "***", "R");
		this.print(0, 141, formatAmt(thisYearLoanBalTotal, 0), "R");
		this.print(0, 150, "***", "R");
		this.print(0, 164, formatAmt(thisYearWeightedAvgRateTotal, 3), "R");

		this.print(1, 1,
				"└──────────────┴────────┴────┴──────────┴────┴───────┴────────┴────┴──────────┴────┴───────┘");

	}

	/**
	 * 將合計歸零
	 */
	private void cleanTotal() {

		thisMonthIntAmtTotal = BigDecimal.ZERO;
		thisMonthLoanBalTotal = BigDecimal.ZERO;
		thisMonthWeightedTotal = BigDecimal.ZERO;
		thisYearIntAmtTotal = BigDecimal.ZERO;
		thisYearLoanBalTotal = BigDecimal.ZERO;
		thisYearWeightedTotal = BigDecimal.ZERO;
	}

	private void getNowDateTime() {
		String date = dDateUtil.getNowStringBc();
		year = date.substring(2, 4);
		month = date.substring(4, 6);
		day = date.substring(6, 8);

		String time = dDateUtil.getNowStringTime();
		hour = time.substring(0, 2);
		min = time.substring(2, 4);
		sec = time.substring(4, 6);
	}

	private void noData() {
		this.print(1, 1, "本日無資料");
		this.print(2, 1,
				"┌──────────────┬─────────────────────────────────────┬─────────────────────────────────────┐");
		this.print(1, 1,
				"│　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　本　　月　　份　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　年　初　到　本　月　份　　　　　　　　　　　　│");
		this.print(1, 1,
				"│　　　　　　　　　　　　　　├────────┬────┬──────────┬────┬───────┼────────┬────┬──────────┬────┬───────┤");
		this.print(0, 7, rptDataType);
		this.print(1, 1,
				"│　　　　　　　　　　　　　　│　本月利息收入　│　佔率　│　　本月月底餘額　　│　佔率　│　平均利率％　│　累計利息收入　│　佔率　│　每月平均放款餘額　│　佔率　│　平均利率％　│");
		this.print(1, 1,
				"├──────────────┼────────┼────┼──────────┼────┼───────┼────────┼────┼──────────┼────┼───────┤");
		this.print(1, 1,
				"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
		this.print(1, 1,
				"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
		this.print(1, 1,
				"├──────────────┼────────┼────┼──────────┼────┼───────┼────────┼────┼──────────┼────┼───────┤");
		this.print(1, 1,
				"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
		this.print(0, 18, "小　　計");
		this.print(1, 1,
				"├──────────────┼────────┼────┼──────────┼────┼───────┼────────┼────┼──────────┼────┼───────┤");
		this.print(1, 1,
				"│　　　　　　　　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│　　　　　　　　│　　　　│　　　　　　　　　　│　　　　│　　　　　　　│");
		this.print(0, 5, "　　合　　　　　　計");
		this.print(1, 1,
				"└──────────────┴────────┴────┴──────────┴────┴───────┴────────┴────┴──────────┴────┴───────┘");
	}
}
