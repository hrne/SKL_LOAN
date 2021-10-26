package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
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
	BigDecimal[] totalCounters = new BigDecimal[12];

	// 各欄位的輸出座標
	int[] outputPos = new int[] { 8, 19, 32, 47, 60, 75, 88, 103, 116, 131, 146, 162 };
	
	// 計算千元單位用
    private static final BigDecimal thousand = new BigDecimal(1000);

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

			//
			if (lLM007 != null && !lLM007.isEmpty()) {
				hasOutputtedAnything = true;
				
				if (!isFirstPage) {
					// 非第一頁時先換頁
					this.newPage();
				} else {
					isFirstPage = false;
				}

				exportPdf(lLM007);
			}
		}

		if (!hasOutputtedAnything) {
			this.print(-5, 3, "本日無資料");
		}

		long sno = this.close();
		this.toPdf(sno);
	}

	private void exportPdf(List<Map<String, String>> lLM007) throws LogicException {

		// 為 totalCounters 初始化
		Arrays.fill(totalCounters, BigDecimal.ZERO);

		// 每行做輸出, 同時記錄 11 項合計 (F0為月份, 因此 totalCounters[0] 實際不使用, 只用後面11個)
		// totalCounters[11] 為跨月合計
		
		for (Map<String, String> tLDVo : lLM007) {

			this.print(1, 0, "");

			// F0 月份
			this.print(0, outputPos[0], tLDVo.get("F0") + " 月", "R");
			
			String valueStr;
			BigDecimal valueBd;

			for (int i = 1; i <= 10; i++) {
				valueStr = tLDVo.get("F" + i);
				valueBd = getBigDecimal(valueStr);
				
				this.print(0, outputPos[i], formatAmt(computeDivide(valueBd, thousand, 0), 0), "R");
				totalCounters[i] = totalCounters[i].add(valueBd);
				
				// 特殊處理: F10(月合計)時, 也處理跨月合計
				if (i == 10)
				{
					totalCounters[11] =  totalCounters[11].add(valueBd);
					this.print(0, outputPos[11], formatAmt(computeDivide(totalCounters[11], thousand, 0), 0), "R");
				}
			}
			
			this.print(1, 0, "");
		}

		this.print(1, 0,
				" ---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		this.print(1, 1, " 合計");

		// 11是跨月合計 參考樣張不出
		for (int i = 1; i <= 10; i++) {
			this.print(0, outputPos[i], formatAmt(computeDivide(totalCounters[i], thousand, 0), 0), "R");
		}

		this.print(1, 0,
				" ---------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		
		// 輸出位置:
		// 8, 19, 32, 47, 60, 75, 88, 103, 116, 131, 146, 162
		// 全部都是R

	}
}
