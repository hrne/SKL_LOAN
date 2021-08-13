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
import com.st1.itx.db.service.springjpa.cm.LM007ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM007Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM007Report.class);

	@Autowired
	LM007ServiceImpl lM007ServiceImpl;

	// 報表種類中文
	String rptTypeItem;
	
	int noData = 0;
	// 合計總合
	BigDecimal total1 = new BigDecimal("0");
	BigDecimal total2 = new BigDecimal("0");
	BigDecimal total3 = new BigDecimal("0");
	BigDecimal total4 = new BigDecimal("0");
	BigDecimal total5 = new BigDecimal("0");
	BigDecimal total6 = new BigDecimal("0");
	BigDecimal total7 = new BigDecimal("0");
	BigDecimal total8 = new BigDecimal("0");
	BigDecimal total9 = new BigDecimal("0");
	BigDecimal total10 = new BigDecimal("0");
	BigDecimal[][] total = new BigDecimal[12][11];

	
	@Override
	public void printHeader() {

		this.print(-1, 143, "機密等級：密");
		this.print(-2, 1, "　程式ID：" + this.getParentTranCode());
		this.print(-2, 85, "新光人壽保險股份有限公司", "C");
		this.print(-2, 143, "日　期：" + this.showBcDate(Integer.parseInt(dDateUtil.getNowStringBc()), 1));
		this.print(-3, 1, "　報　表：" + this.getRptCode());
		this.print(-3, 85, "利息收入成長表－實收基礎 -- " + rptTypeItem, "C");
		this.print(-3, 143, "時　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 143, "頁　數：" + this.getNowPage());
		this.print(-5, 85, getshowRocDate(this.getReportDate()).substring(0, 10), "C");
		this.print(-5, 143, "單　位：仟元");
		this.print(-7, 2, "　　　　　　　 短期擔保放款　　　　　　　　中期擔保放款　　　　　　　　長期擔保放款　　　　　30年房貸");
		this.print(-8, 2,
				"　　　　------------------------　  ------------------------　  ------------------------  　----------");
		this.print(-9, 2, "月份　　　　 企金　　　　 房貸　　　　　 企金　　　　 房貸　　　　　 企金　　　　 房貸　　　　　 房貸　　　　 車貸　　　　 轉催收　　　　　 合計　　　　　各月累計");
		this.print(-10, 0,
				" ---------------------------------------------------------------------------------------------------------------------------------------------------------------------");

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
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM007", "放款利息收入成長表", "", "A4", "L");

		List<Map<String, String>> lLM007 = null;

		int iTbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		this.info("LM007Report exec iTbsdyf = " + iTbsdyf);

		boolean isFirstPage = true;
		for (Map<String, String> subBookVo : listSubBookCodes) {

			rptTypeItem = subBookVo.get("F1");

			String acSubBookCode = subBookVo.get("F0");
			try {
				lLM007 = lM007ServiceImpl.findAll(iTbsdyf, acSubBookCode, titaVo);
			} catch (Exception e) {
				this.info("lM007ServiceImpl.findAll error = " + e.toString());
			}
			if(lLM007.size() == 0) {
				noData++;
			}
			if(noData == 2) {
				this.print(-5, 3, "本日無資料");
			}
			if (!isFirstPage) {
				// 非第一頁時先換頁
				this.newPage();
			} else {
				isFirstPage = false;
			}
			
			exportPdf(lLM007);
		}

		long sno = this.close();
		this.toPdf(sno);
	}
		

	
	private void exportPdf(List<Map<String, String>> lLM007) {
		for(int n = 0 ; n < 12 ; n++){
			for(int m = 0 ; m < 11 ; m++) {
				total[n][m] = BigDecimal.ZERO;
			}
		}
		//最下面的合計
		BigDecimal sum = new BigDecimal("0");//短企
		BigDecimal sum1 = new BigDecimal("0");//短房
		BigDecimal sum2 = new BigDecimal("0");//中企
		BigDecimal sum3 = new BigDecimal("0");//中房
		BigDecimal sum4 = new BigDecimal("0");//長企
		BigDecimal sum5 = new BigDecimal("0");//長房
		BigDecimal sum6 = new BigDecimal("0");//30房
		BigDecimal sum7 = new BigDecimal("0");//車貸
		BigDecimal sum8 = new BigDecimal("0");//轉催收
		BigDecimal sum9 = new BigDecimal("0");//各月累計
		BigDecimal thousand = new BigDecimal("1000");// 千
		sum = BigDecimal.ZERO;
		sum1 = BigDecimal.ZERO;
		sum2 = BigDecimal.ZERO;
		sum3 = BigDecimal.ZERO;
		sum4 = BigDecimal.ZERO;
		sum5 = BigDecimal.ZERO;
		sum6 = BigDecimal.ZERO;
		sum7 = BigDecimal.ZERO;
		sum8 = BigDecimal.ZERO;
		sum9 = BigDecimal.ZERO;
		for (Map<String, String> tLM007 : lLM007) {		
			int row = Integer.parseInt(tLM007.get("F0").substring(4, 6));
			total1 = tLM007.get("F1") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F1"));
			
			total[row - 1][1] = total[row - 1][1].add(formatDivide(total1, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total1, thousand, 0));
			sum = sum.add(formatDivide(total1, thousand, 0));
			total2 = tLM007.get("F2") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F2"));
			
			total[row - 1][2] = total[row - 1][2].add(formatDivide(total2, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total2, thousand, 0));
			sum1 = sum1.add(formatDivide(total2, thousand, 0));
			total3 = tLM007.get("F3") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F3"));
			
			total[row - 1][3] = total[row - 1][3].add(formatDivide(total3, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total3, thousand, 0));
			sum2 = sum2.add(formatDivide(total3, thousand, 0));
			total4 = tLM007.get("F4") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F4"));
			
			total[row - 1][4] = total[row - 1][4].add(formatDivide(total4, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total4, thousand, 0));
			sum3 = sum3.add(formatDivide(total4, thousand, 0));
			total5 = tLM007.get("F5") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F5"));
			
			total[row - 1][5] = total[row - 1][5].add(formatDivide(total5, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total5, thousand, 0));
			sum4 = sum4.add(formatDivide(total5, thousand, 0));
			total6 = tLM007.get("F6") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F6"));
			
			total[row - 1][6] = total[row - 1][6].add(formatDivide(total6, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total6, thousand, 0));
			sum5 = sum5.add(formatDivide(total6, thousand, 0));
			total7 = tLM007.get("F7") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F7"));
			
			total[row - 1][7] = total[row - 1][7].add(formatDivide(total7, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total7, thousand, 0));
			sum6 = sum6.add(formatDivide(total7, thousand, 0));
			total8 = tLM007.get("F8") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F8"));
			
			total[row - 1][8] = total[row - 1][8].add(formatDivide(total8, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total8, thousand, 0));
			sum7 = sum7.add(formatDivide(total8, thousand, 0));
			total9 = tLM007.get("F9") == null ? BigDecimal.ZERO
					: new BigDecimal(tLM007.get("F9"));
			
			total[row - 1][9] = total[row - 1][9].add(formatDivide(total9, thousand, 0));
			total[row - 1][10] = total[row - 1][10].add(formatDivide(total9, thousand, 0));
			sum8 = sum8.add(formatDivide(total9, thousand, 0));
//			total10 = tLM007.get("F10") == null ? BigDecimal.ZERO
//					: new BigDecimal(tLM007.get("F10"));
//			
//			total[row - 1][10] = total[row - 1][10].add(formatDivide(total10, thousand, 0));
		} // for
		for(int i = 0 ; i < (this.txBuffer.getTxCom().getTbsdyf() % 10000 / 100) ; i++) {
			if(i == 0) {
				this.print(1, 8, String.valueOf(i + 1) + " 月", "R");
			}else {
				this.print(2, 8, String.valueOf(i + 1) + " 月", "R");
			}
			
			//輸出金額
			this.print(0, 19, this.formatAmt(total[i][1], 0), "R");
			this.print(0, 32, this.formatAmt(total[i][2], 0), "R");
			this.print(0, 47, this.formatAmt(total[i][3], 0), "R");
			this.print(0, 60, this.formatAmt(total[i][4], 0), "R");
			this.print(0, 75, this.formatAmt(total[i][5], 0), "R");
			this.print(0, 88, this.formatAmt(total[i][6], 0), "R");
			this.print(0, 103, this.formatAmt(total[i][7], 0), "R");
			this.print(0, 116, this.formatAmt(total[i][8], 0), "R");
			this.print(0, 131, this.formatAmt(total[i][9], 0), "R");
			this.print(0, 146, this.formatAmt(total[i][10], 0), "R");
			sum9 = sum9.add(total[i][10]);
			this.print(0, 162, this.formatAmt(sum9, 0), "R");
		}
		
		
		
		
		this.print(1, 0,
				" ---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		this.print(1, 1, " 合計");
		this.print(0, 19, this.formatAmt(sum, 0), "R");
		this.print(0, 32, this.formatAmt(sum1, 0), "R");
		this.print(0, 47, this.formatAmt(sum2, 0), "R");
		this.print(0, 60, this.formatAmt(sum3, 0), "R");
		this.print(0, 75, this.formatAmt(sum4, 0), "R");
		this.print(0, 88, this.formatAmt(sum5, 0), "R");
		this.print(0, 103, this.formatAmt(sum6, 0), "R");
		this.print(0, 116, this.formatAmt(sum7, 0), "R");
		this.print(0, 131, this.formatAmt(sum8, 0), "R");
		this.print(0, 146, this.formatAmt(sum9, 0), "R");
		this.print(1, 0,
				" ---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		sum = BigDecimal.ZERO;
		sum1 = BigDecimal.ZERO;
		sum2 = BigDecimal.ZERO;
		sum3 = BigDecimal.ZERO;
		sum4 = BigDecimal.ZERO;
		sum5 = BigDecimal.ZERO;
		sum6 = BigDecimal.ZERO;
		sum7 = BigDecimal.ZERO;
		sum8 = BigDecimal.ZERO;
		sum9 = BigDecimal.ZERO;

	}
	// i = computeDivide四捨五入的位數，j = formatAmt四捨五入的位數
	private BigDecimal formatDivide(BigDecimal dividend, BigDecimal divisor, int i) {
		BigDecimal count = this.computeDivide(dividend, divisor, i);
		return count;
	}
}
