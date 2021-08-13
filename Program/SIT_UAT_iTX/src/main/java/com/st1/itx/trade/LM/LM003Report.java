package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM003ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM003Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM003Report.class);

	@Autowired
	LM003ServiceImpl lM003ServiceImpl;

	String dataYear = "";
	String dataMonth = "";

	// 四捨五入
	RoundingMode roundingModeLM003 = RoundingMode.HALF_UP;
	
	BigDecimal sumF2 = BigDecimal.ZERO;
	BigDecimal sumF3 = BigDecimal.ZERO;
	BigDecimal sumF4 = BigDecimal.ZERO;
	BigDecimal sumF5 = BigDecimal.ZERO;
	BigDecimal sumF6 = BigDecimal.ZERO;
	BigDecimal sumF7 = BigDecimal.ZERO;
	BigDecimal sumF8 = BigDecimal.ZERO;
	BigDecimal sumF9 = BigDecimal.ZERO;
	
	private void resetSum() {
		
		sumF2 = BigDecimal.ZERO;
		sumF3 = BigDecimal.ZERO;
		sumF4 = BigDecimal.ZERO;
		sumF5 = BigDecimal.ZERO;
		sumF6 = BigDecimal.ZERO;
		sumF7 = BigDecimal.ZERO;
		sumF8 = BigDecimal.ZERO;
		sumF9 = BigDecimal.ZERO;
	
	}

	@Override
	public void printHeader() {

		this.setFontSize(16);
		this.print(-2, 52, dataYear + "年" + dataMonth + "月個人房貸戶 - 撥款/還款金額比較月報表", "C");
		this.setFontSize(7);
		this.print(-4, 243, "機密等級：密", "R");
		this.setFontSize(8);
		this.print(-5, 166, "單位：億元", "R");
		this.setFontSize(6);

		this.setBeginRow(7);
		this.setMaxRows(100);
	}

	public Boolean exec(TitaVo titaVo) throws LogicException {
		this.setCharSpaces(0);
		int iEntdy = Integer.parseInt(titaVo.getParam("ENTDY"));

		String.valueOf(iEntdy);

		dataYear = titaVo.getParam("inputDateEnd").substring(0,3);
		dataMonth = titaVo.getParam("inputDateEnd").substring(3);

		/*
		 * 抓SQL資料
		 */
		List<Map<String, String>> listLM003 = null;

		try {
			listLM003 = lM003ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.error("lM003ServiceImpl.findAll error = " + e.getMessage());
		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM003",
				dataYear + "年" + dataMonth + "月個人房貸戶 - 撥款／還款金額比較月報表", "", "A4", "L");

		print(1, 1, "");
		print(1, 42, "╔════════╦═══════╦═══════════════════════════════════════════════════════════════════╦═══════╦══════╗");
		print(1, 42, "║　　　　 　　 　║　　　　　　　║　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　║　　　　　　　║　　　　　　║");

		print(0, 136, "還款(b)");
		print(1, 42, "║　　　　　　　  ║　　　　　　　╠════════════════════════════╦════════════════════════════╦═════════╣　　　　　　  ║　　　　　　║");
		print(0, 218, "淨增減");
		print(0, 234, "月底");
		print(1, 42, "║　　　　　　　  ║ 　　　　　 　║　　　　　　　　    　　　　　　　　　　　　　　　　　  ║　　　　　　　　　　　　　　　　　　　　　　　　　　    ║　　 　　　　     ║　　　　　　　║　　　　　　║");
		print(0, 50, "期間");
		print(0, 66, "撥款(a) ");
		print(0, 101, "結清");
		print(0, 160 , "非結清");
		print(1, 42, "║　　　　　　　　║　　　　　　　╠════════╦═════╦══════╦══════╬═══════╦═════╦═══════╦══════╣　　　　　　　　　║　　　　　　　║　　　　　　║");
		print(0, 202, "合計");
		print(1, 42, "║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");

		print(0, 81, "利率高轉貸");
		print(0, 99, "買賣");
		print(0, 109, "自行還款等");
		print(0, 126, "小計");

		print(0, 140, "部份還款");
		print(0, 153, "本金攤提");

		print(0, 168, "轉催收");
		print(0, 184, "小計");

		print(0, 217, "(c=a-b)");

		print(0, 233, "餘額");

		Map<String, String> lastTLDVo = null;
		
		if (listLM003 != null && listLM003.size() != 0) {
			
		String lastOutputYear = "";
		String thisOutputYear = "";
		Boolean firstFlag = true;
		Boolean firstOfThisYearFlag = false;
		BigDecimal dataMonthsCounter = BigDecimal.ZERO;
		
		BigDecimal yearlyLoanTotal = BigDecimal.ZERO;

		BigDecimal yearlyReturnTotalA = BigDecimal.ZERO;
		BigDecimal yearlyReturnTotalB = BigDecimal.ZERO;
		BigDecimal yearlyReturnTotal = BigDecimal.ZERO;
		
		dataMonthsCounter = BigDecimal.ZERO;
		
		
		for (Map<String, String> tLDVo : listLM003)
		{
			
			if (firstFlag)
			{
				lastOutputYear = tLDVo.get("F0");
				firstFlag = false;
				firstOfThisYearFlag = true;
			}
			
			thisOutputYear = tLDVo.get("F0");
			
			if (!firstFlag && !thisOutputYear.equals(lastOutputYear))
			{
				print(1, 42, "╠════════╬═══════╬════════╬═════╬══════╬══════╬═══════╬═════╬═══════╬══════╬═════════╬═══════╬══════╣");
				print(1, 42, "║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");
				print(0, 59, (Integer.parseInt(lastOutputYear)-1911) + "年合計", "R");
				print(0, 75, formatAmt(getBillionAmt(sumF2), 2), "R");
				
				yearlyLoanTotal = yearlyLoanTotal.add(sumF2);
				
				print(0, 93, formatAmt(getBillionAmt(sumF3), 2), "R");
				print(0, 106, formatAmt(getBillionAmt(sumF4), 2), "R");
				print(0, 118, formatAmt(getBillionAmt(sumF5), 2), "R");
				
				yearlyReturnTotalA = yearlyReturnTotalA.add(sumF3.add(sumF4).add(sumF5));
				yearlyReturnTotalB = yearlyReturnTotalB.add(sumF6.add(sumF7).add(sumF8));
				yearlyReturnTotal = yearlyReturnTotalA.add(yearlyReturnTotalB);
				
				print(0, 133, formatAmt(getBillionAmt(yearlyReturnTotalA), 2), "R");
				print(0, 148, formatAmt(getBillionAmt(sumF6), 2), "R");
				print(0, 160, formatAmt(getBillionAmt(sumF7), 2), "R");
				print(0, 178, formatAmt(getBillionAmt(sumF8), 2), "R");
				print(0, 190, formatAmt(getBillionAmt(yearlyReturnTotalB), 2), "R");
				print(0, 210, formatAmt(getBillionAmt(yearlyReturnTotal), 2), "R");
				print(0, 225, formatAmt(getBillionAmt(yearlyLoanTotal.subtract(yearlyReturnTotal)), 2), "R");
		
				print(1, 42, "╠════════╬═══════╬════════╬═════╬══════╬══════╬═══════╬═════╬═══════╬══════╬═════════╬═══════╬══════╣");
				print(1, 42, "║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");
				print(0, 59, (Integer.parseInt(lastOutputYear)-1911) + "年月平均", "R");
				print(0, 75, formatAmt(getAverage(sumF2, dataMonthsCounter), 2), "R");
				print(0, 93, formatAmt(getAverage(sumF3, dataMonthsCounter), 2), "R");
				print(0, 106, formatAmt(getAverage(sumF4, dataMonthsCounter), 2), "R");
				print(0, 118, formatAmt(getAverage(sumF5, dataMonthsCounter), 2), "R");
				print(0, 133, formatAmt(getAverage(yearlyReturnTotalA, dataMonthsCounter), 2), "R");
				print(0, 148, formatAmt(getAverage(sumF6, dataMonthsCounter), 2), "R");
				print(0, 160, formatAmt(getAverage(sumF7, dataMonthsCounter), 2), "R");
				print(0, 178, formatAmt(getAverage(sumF8, dataMonthsCounter), 2), "R");
				print(0, 190, formatAmt(getAverage(yearlyReturnTotalB, dataMonthsCounter), 2), "R");
				print(0, 210, formatAmt(getAverage(yearlyReturnTotal, dataMonthsCounter), 2), "R");
				print(0, 225, formatAmt(getAverage(yearlyLoanTotal.subtract(yearlyReturnTotal), dataMonthsCounter), 2), "R");
				
				resetSum();
				
				yearlyLoanTotal = BigDecimal.ZERO;
				
				yearlyReturnTotalA = BigDecimal.ZERO;
				yearlyReturnTotalB = BigDecimal.ZERO;
				yearlyReturnTotal = BigDecimal.ZERO;
				
				dataMonthsCounter = BigDecimal.ZERO;
				
				firstOfThisYearFlag = true;
			}
			
			// 非今年的明細只出平均
			if (thisOutputYear.equals(Integer.toString(Integer.valueOf(titaVo.getParam("inputDateEnd").substring(0,3)) + 1911)))
			{
			print(1, 42, "╠════════╬═══════╬════════╬═════╬══════╬══════╬═══════╬═════╬═══════╬══════╬═════════╬═══════╬══════╣");
			print(1, 42, "║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");
			
			if (thisOutputYear.equals(lastOutputYear) && !firstOfThisYearFlag)
			{
				print(0, 59, "　　 " + tLDVo.get("F1") + "月", "R");
			}
			else
			{
				print(0, 59, Integer.parseInt(tLDVo.get("F0"))-1911 + "年" + tLDVo.get("F1") + "月", "R");
				firstOfThisYearFlag = false;
			}


				BigDecimal f2 = getBillionAmt(tLDVo.get("F2"));
				BigDecimal f3 = getBillionAmt(tLDVo.get("F3"));
				BigDecimal f4 = getBillionAmt(tLDVo.get("F4"));
				BigDecimal f5 = getBillionAmt(tLDVo.get("F5"));
				BigDecimal f6 = getBillionAmt(tLDVo.get("F6"));
				BigDecimal f7 = getBillionAmt(tLDVo.get("F7"));
				BigDecimal f8 = getBillionAmt(tLDVo.get("F8"));
				BigDecimal f9 = getBillionAmt(tLDVo.get("F9"));
				
				print(0, 75, formatAmt(f2, 2), "R");
				print(0, 93, formatAmt(f3, 2), "R");
				print(0, 106, formatAmt(f4, 2), "R");
				print(0, 118, formatAmt(f5, 2), "R");
				print(0, 133, formatAmt(f3.add(f4).add(f5), 2), "R");
				print(0, 148, formatAmt(f6, 2), "R");
				print(0, 160, formatAmt(f7, 2), "R");
				print(0, 178, formatAmt(f8, 2), "R");
				print(0, 190, formatAmt(f6.add(f7).add(f8), 2), "R");
				print(0, 210, formatAmt(f3.add(f4).add(f5).add(f6).add(f7).add(f8), 2), "R");
				print(0, 225, formatAmt(f2.subtract(f3.add(f4).add(f5).add(f6).add(f7).add(f8)), 2), "R");
				print(0, 242, formatAmt(f9, 2), "R");
				
			}

			    lastOutputYear = tLDVo.get("F0");
			    
				doSummary(tLDVo);
				dataMonthsCounter = dataMonthsCounter.add(new BigDecimal(1));
				
				lastTLDVo = tLDVo;
				
			}
		
			// 最後一筆資料的小計
			print(1, 42, "╠════════╬═══════╬════════╬═════╬══════╬══════╬═══════╬═════╬═══════╬══════╬═════════╬═══════╬══════╣");
			print(1, 42, "║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");
			print(0, 59, (Integer.parseInt(lastOutputYear)-1911) + "年合計", "R");
			print(0, 75, formatAmt(getBillionAmt(sumF2), 2), "R");
			
			yearlyLoanTotal = yearlyLoanTotal.add(sumF2);
			
			print(0, 93, formatAmt(getBillionAmt(sumF3), 2), "R");
			print(0, 106, formatAmt(getBillionAmt(sumF4), 2), "R");
			print(0, 118, formatAmt(getBillionAmt(sumF5), 2), "R");
			
			yearlyReturnTotalA = yearlyReturnTotalA.add(sumF3.add(sumF4).add(sumF5));
			yearlyReturnTotalB = yearlyReturnTotalB.add(sumF6.add(sumF7).add(sumF8));
			yearlyReturnTotal = yearlyReturnTotalA.add(yearlyReturnTotalB);
			
			print(0, 133, formatAmt(getBillionAmt(yearlyReturnTotalA), 2), "R");
			print(0, 148, formatAmt(getBillionAmt(sumF6), 2), "R");
			print(0, 160, formatAmt(getBillionAmt(sumF7), 2), "R");
			print(0, 178, formatAmt(getBillionAmt(sumF8), 2), "R");
			print(0, 190, formatAmt(getBillionAmt(yearlyReturnTotalB), 2), "R");
			print(0, 210, formatAmt(getBillionAmt(yearlyReturnTotal), 2), "R");
			print(0, 225, formatAmt(getBillionAmt(yearlyLoanTotal.subtract(yearlyReturnTotal)), 2), "R");
	
			print(1, 42, "╠════════╬═══════╬════════╬═════╬══════╬══════╬═══════╬═════╬═══════╬══════╬═════════╬═══════╬══════╣");
			print(1, 42, "║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");
			print(0, 59, (Integer.parseInt(lastOutputYear)-1911) + "年月平均", "R");
			print(0, 75, formatAmt(getAverage(sumF2, dataMonthsCounter), 2), "R");
			print(0, 93, formatAmt(getAverage(sumF3, dataMonthsCounter), 2), "R");
			print(0, 106, formatAmt(getAverage(sumF4, dataMonthsCounter), 2), "R");
			print(0, 118, formatAmt(getAverage(sumF5, dataMonthsCounter), 2), "R");
			print(0, 133, formatAmt(getAverage(yearlyReturnTotalA, dataMonthsCounter), 2), "R");
			print(0, 148, formatAmt(getAverage(sumF6, dataMonthsCounter), 2), "R");
			print(0, 160, formatAmt(getAverage(sumF7, dataMonthsCounter), 2), "R");
			print(0, 178, formatAmt(getAverage(sumF8, dataMonthsCounter), 2), "R");
			print(0, 190, formatAmt(getAverage(yearlyReturnTotalB, dataMonthsCounter), 2), "R");
			print(0, 210, formatAmt(getAverage(yearlyReturnTotal, dataMonthsCounter), 2), "R");
			print(0, 225, formatAmt(getAverage(yearlyLoanTotal.subtract(yearlyReturnTotal), dataMonthsCounter), 2), "R");

		}
		else if (listLM003 == null || listLM003.size() == 0) 
		{
			print(0, 70, "本日無資料", "R");
		}

		print(1, 42, "╚════════╩═══════╩════════╩═════╩══════╩══════╩═══════╩═════╩═══════╩══════╩═════════╩═══════╩══════╝");
		
		print(1, 42, "╔════════════════╦════════╦═════╦══════╦══════╦═══════╦═════╦═══════╦══════╦═════════╦═══════╦══════╗");
		print(1, 42, "║　　　　當月還款分布%　　　 　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");
		
		BigDecimal f3 = getBillionAmt(lastTLDVo.get("F3"));
		BigDecimal f4 = getBillionAmt(lastTLDVo.get("F4"));
		BigDecimal f5 = getBillionAmt(lastTLDVo.get("F5"));
		BigDecimal f6 = getBillionAmt(lastTLDVo.get("F6"));
		BigDecimal f7 = getBillionAmt(lastTLDVo.get("F7"));
		BigDecimal f8 = getBillionAmt(lastTLDVo.get("F8"));
		BigDecimal f9 = getBillionAmt(lastTLDVo.get("F9"));
		BigDecimal f10 = getBillionAmt(lastTLDVo.get("F10"));
		BigDecimal f11 = getBillionAmt(lastTLDVo.get("F11"));
		BigDecimal f12 = getBillionAmt(lastTLDVo.get("F12"));
		
		BigDecimal total = f3.add(f4).add(f5).add(f6).add(f7).add(f8);
		
		print(0, 93, formatAmt(f3.divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)), 2) + "%", "R");
		print(0, 106, formatAmt(f4.divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)), 2) + "%", "R");
		print(0, 118, formatAmt(f5.divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)), 2) + "%", "R");
		print(0, 133, formatAmt(f3.add(f4).add(f5).divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)), 2) + "%", "R");
		print(0, 148, formatAmt(f6.divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)), 2) + "%", "R");
		print(0, 160, formatAmt(f7.divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)), 2) + "%", "R");
		print(0, 178, "---", "R");
		print(0, 190, formatAmt(f6.add(f7).add(f8).divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)), 2) + "%", "R");
		print(0, 210, formatAmt(total.divide(total, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)), 2) + "%", "R");
		
		print(1, 42, "╚════════════════╩════════╩═════╩══════╩══════╩═══════╩═════╩═══════╩══════╩═════════╩═══════╩══════╝");
		print(1, 1, "");
		print(1, 42, " ●"+(Integer.parseInt(lastTLDVo.get("F0"))-1911)+"年"+lastTLDVo.get("F1")+"月底貸款總餘額："+formatAmt(f10,2)+"億元 ●企金："+formatAmt(f11,2)+"億元 ●房貸："+formatAmt(f9,2)+"億元");				
		print(1, 42, " ●依報表：LN6361編制：撥款金額含催收回復，還款金額含轉催收。");
		print(1, 42, " ●自行還款含內部代償、借新還舊、大額還款（1月~"+lastTLDVo.get("F1")+"月累積數　）。");
		
		BigDecimal monthlyReturnOutput = total.subtract(f8).subtract(f12);
		print(1, 42, " ●月實際還款數："+formatAmt(total,2)+"（帳載）　（內部轉帳）-"+formatAmt(f8,2)+"（轉催收）-"+formatAmt(f12,2)+"（企金件以自然人申貸還款）＝"+ formatAmt(monthlyReturnOutput,2) +"億");
		long sno = this.close();
		this.toPdf(sno);
		
		return true;
	}

	/**
	 * 先取平均後再取到億元
	 * 
	 * @param inputAmt
	 * @param dataMonths
	 * @return
	 */
	private BigDecimal getAverage(BigDecimal inputAmt, BigDecimal dataMonths) {
		BigDecimal result = BigDecimal.ZERO;

		if (inputAmt == null || dataMonths.compareTo(BigDecimal.ZERO) <= 0) {
			return result;
		}

		result = inputAmt.divide(dataMonths, 2, roundingModeLM003);

		return getBillionAmt(result);
	}

	/**
	 * 取到億元,四捨五入到小數點後第二位
	 * 
	 * @param inputBigDeicmal 從DB撈到的數值
	 * @return 處理後的數值
	 */
	private BigDecimal getBillionAmt(BigDecimal inputBigDeicmal) {
		return getBillionAmt(inputBigDeicmal.toString());
	}

	/**
	 * 取到億元,四捨五入到小數點後第二位
	 * 
	 * @param inputAmt 從DB撈到的數值
	 * @return 處理後的數值
	 */
	private BigDecimal getBillionAmt(String inputAmt) {
		BigDecimal result = BigDecimal.ZERO;

		if (inputAmt == null || inputAmt.isEmpty()) {
			return result;
		}

		BigDecimal billion = new BigDecimal("100000000");

		result = new BigDecimal(inputAmt).divide(billion, 2, roundingModeLM003);

		return result;
	}

	private void doSummary(Map<String, String> map) {

		BigDecimal f2 = getAmt(map.get("F2"));
		BigDecimal f3 = getAmt(map.get("F3"));
		BigDecimal f4 = getAmt(map.get("F4"));
		BigDecimal f5 = getAmt(map.get("F5"));
		BigDecimal f6 = getAmt(map.get("F6"));
		BigDecimal f7 = getAmt(map.get("F7"));
		BigDecimal f8 = getAmt(map.get("F8"));
		BigDecimal f9 = getAmt(map.get("F9"));

		sumF2 = sumF2.add(f2);
		sumF3 = sumF3.add(f3);
		sumF4 = sumF4.add(f4);
		sumF5 = sumF5.add(f5);
		sumF6 = sumF6.add(f6);
		sumF7 = sumF7.add(f7);
		sumF8 = sumF8.add(f8);
		sumF9 = sumF9.add(f9);
	}

	private BigDecimal getAmt(String inputAmt) {
		BigDecimal result = BigDecimal.ZERO;

		if (inputAmt == null || inputAmt.isEmpty()) {
			return result;
		}

		result = new BigDecimal(inputAmt);

		return result;
	}
}
