package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM007ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM007Report extends MakeReport {

	@Autowired
	LM007ServiceImpl lM007ServiceImpl;

	@Autowired
	Parse parse;

	// 報表種類中文
	String rptTypeItem;

	// 用於確認此次是否有資料輸出
	Boolean hasOutputtedAnything = false;

	// 合計總合
	BigDecimal[] sumCounters = new BigDecimal[15];
	
	// 合計的合計（for 全部類輸出）
	ArrayList<BigDecimal[]> totalTotal = new ArrayList<BigDecimal[]>();

	// 各欄位的輸出座標
	private static final int[] outputPos = new int[] { 8, 15, 25, 39, 49, 59, 73, 83, 93, 107, 120, 131, 140, 150, 162 };
	
	// 千元單位
	final static BigDecimal thousand = new BigDecimal("1000");

	@Override
	public void printHeader() {


		this.print(-1, 148, "機密等級：密");
		this.print(-2, 1, "　程式ID：" + this.getParentTranCode());
		this.print(-2, 85, "新光人壽保險股份有限公司", "C");
		this.print(-2, 148, "日　期：" + this.showBcDate(dDateUtil.getNowStringBc(), 1));
		this.print(-3, 1, "　報　表：" + this.getRptCode());
		this.print(-3, 85, "利息收入成長表－實收基礎 -- " + rptTypeItem, "C");
		this.print(-3, 148, "時　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 148, "頁　數：" + this.getNowPage());
		this.print(-5, 85, getshowRocDate(this.getReportDate()).substring(0, 10), "C");
		this.print(-5, 148, "單　位：仟元");
		this.print(-7, 2, "　　　　　　　　　 短期擔保放款　　　　　　　　 　　　  中期擔保放款　　　　　　　　 　　   長期擔保放款　　　　　　　　　　　30年房貸");
		this.print(-8, 2, "　　　　	　----------------------------　　　----------------------------　　　----------------------------    　　----------");
		this.print(-9, 2, "月份　　　 企金　　企金自然人　　　　房貸　　　　企金　　企金自然人　　　　房貸　　　　企金　　企金自然人　　　　房貸  　　　　 房貸 　　　車貸　　轉催收 　　　合計 　　 各月累計");
		this.print(-10, 0, " ---------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		this.setBeginRow(11);
		this.setMaxRows(35);
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.setCharSpaces(0);

		List<Map<String, String>> listSubBookCodes = null;

		// 設定每頁報表種類
		try {
			listSubBookCodes = lM007ServiceImpl.findAll_SubBookCodes(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM007ServiceImpl.findAll error = " + e.getMessage());
		}
		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI())
				.setRptCode("LM007").setRptItem("放款利息收入成長表").setRptSize("A4")
				.setSecurity("").setPageOrientation("L").build();

		
		this.open(titaVo, reportVo);
		
		// 設定字體大小
		this.setFontSize(8);
		
	
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM007", "放款利息收入成長表", "", "A4", "L");

		List<Map<String, String>> lLM007 = null;

		int iTbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		this.info("LM007Report exec iTbsdyf = " + iTbsdyf);

		boolean isFirstPage = true;
		
		// 初始化 totalTotal
		for (int i = 0; i <= 15; i++) // 0為dummy 1到12對應月份
		{
			BigDecimal[] bMonth = new BigDecimal[15];
			Arrays.fill(bMonth, BigDecimal.ZERO);
			totalTotal.add(bMonth); // 0為dummy 1到11放各項金額
		}
		for (Map<String, String> subBookVo : listSubBookCodes) {

			rptTypeItem = subBookVo.get("F1");

			String acSubBookCode = subBookVo.get("F0");
			try {
				lLM007 = lM007ServiceImpl.findAll(iTbsdyf, acSubBookCode, titaVo);
			} catch (Exception e) {
				this.info("lM007ServiceImpl.findAll error = " + e.toString());
			}

			//
			if (lLM007 != null && !lLM007.isEmpty()) {
				hasOutputtedAnything = true;

				if (!isFirstPage) {
					// 非第一頁時先換頁
					this.newPage();
				} else {
					isFirstPage = false;
				}
				
				exportPdf(lLM007, "全部".equals(rptTypeItem));
			}
		}

		if (!hasOutputtedAnything) {
			this.print(-5, 3, "本日無資料");
		}

		this.close();
		// this.toPdf(sno);
	}

	private void exportPdf(List<Map<String, String>> lLM007, Boolean isTotal) throws LogicException {
		
		// isTotal 表示此次輸出是否為【全部】類
		
		// 為模擬原報表的四捨五入設計
		// 【全部】類是用前面已輸出的結果 (totalTotal) 去加總輸出，而非實際數目 (query from serviceimpl) 化為千元後四捨五入
		// 此外無論是哪一種類，合計、小計都是用該頁已輸出結果加總輸出

		// 為 sumCounters 初始化
		Arrays.fill(sumCounters, BigDecimal.ZERO);

		// 每行做輸出, 同時記錄 11 項合計 (F0為月份, 因此 sumCounters[0] 實際不使用, 只用後面11個)

		for (Map<String, String> tLDVo : lLM007) {

			this.print(1, 0, "");
			
			int month = parse.stringToInteger(tLDVo.get("F0"));
			
			// totalTotal 用於【全部】類輸出
			// 是一個 ArrayList, 包含 13 個 BigDecimal[12], index 0 是dummy, index 1 ~ 12 代表一到十二月
			// BigDecimal[12] 代表【各類每月的細項與小計】的加總, 0 是dummy, 1~9 對應 F1~F9, 10 是每月小計, 11 是跨月小計
			BigDecimal[] totalTotalMonth = totalTotal.get(month);

			// F0 月份
			this.print(0, outputPos[0], tLDVo.get("F0") + " 月", "R");

			BigDecimal monthlyTotal = BigDecimal.ZERO; // 每月合計

			for (int i = 1; i <= 12; i++) {
				String valueStr = tLDVo.get("F" + i);
				BigDecimal valueBd = isTotal ? totalTotalMonth[i] : getBigDecimal(valueStr); // 出【全部】類時，取 totalTotal，否則取 Query
				valueBd = isTotal ? valueBd : computeDivide(valueBd, thousand, 0);
				
				// 這邊先 computeDivide 再加總進小計與合計
				// 是為了模擬原報表計算小計與合計時，是用四捨五入過後的千元單位數字去加總
				// 而非實際數字合計再除以千元並四捨五入
				// 所以假如有三個月，細項分別為 600, 601, 602 合計 1803
				// 報表上會顯示 3 (1+1+1) 而非 2 (1.803 四捨五入)
				
				this.print(0, outputPos[i], formatAmt(valueBd, 0), "R");
				sumCounters[i] = sumCounters[i].add(valueBd); // 該頁合計用
				monthlyTotal = monthlyTotal.add(valueBd); // 每月小計
				
				totalTotalMonth[i] = totalTotalMonth[i].add(valueBd); // 【全部】頁合計用

				// 特殊處理: F12(每月合計前的最後一項細項)時, 處理月小計與跨月小計
				if (i == 12) {
					sumCounters[13] = sumCounters[13].add(monthlyTotal); // 在每頁最底下合計時使用
					this.print(0, outputPos[13], formatAmt(monthlyTotal, 0), "R"); // 每月小計
					sumCounters[14] = sumCounters[14].add(monthlyTotal); // 跨月小計
					this.print(0, outputPos[14], formatAmt(sumCounters[14], 0), "R");
					
					totalTotalMonth[13] = totalTotalMonth[13].add(monthlyTotal);
					totalTotalMonth[14] = totalTotalMonth[14].add(sumCounters[14]);
				}
			}

			this.print(1, 0, "");
		}

		this.print(1, 0, " ---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		this.print(1, 1, " 合計");

		// 11是跨月合計 參考樣張不出
		for (int i = 1; i <= 13; i++) {
			this.print(0, outputPos[i], formatAmt(sumCounters[i], 0), "R");
		}

		this.print(1, 0, " ---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
}
