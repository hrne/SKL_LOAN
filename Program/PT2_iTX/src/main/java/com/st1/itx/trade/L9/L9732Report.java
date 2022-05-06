package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9732ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class L9732Report extends MakeReport {

	@Autowired
	L9732ServiceImpl l9732ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String TXCD = "L9732";
	String TXName = "質押股票明細表";

	// pivot position for data inputs
	int pivotRow = 2; // 1-based
	int pivotCol = 1; // 1-based

	// 從哪裡開始
	int row = 3;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(TXCD + "Report exec start ...");

		List<Map<String, String>> lL9732 = null;

		try {

			lL9732 = l9732ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(TXCD + "ServiceImpl.findAll error = " + errors.toString());
		}

		// makeExcel.toExcel(sno);
		exportExcel(titaVo, lL9732);
		makeExcel.close();
		return true;
	}

	@SuppressWarnings("unused")
	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

//		String SheetName = titaVo.getParam("inputYear") + titaVo.getParam("inputMonth") + "工作表";
		String tmpValue = "";

		this.info(TXCD + "Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName, TXCD + "_" + TXName,
				TXCD + "_底稿_" + TXName + ".xlsx", "工作表1", "L9732_底稿_質押股票明細表");

		String iDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE")));
		String rDAY = showRocDate(iDAY, 1).substring(4);
		this.info("ACCTDATE   = " + iDAY);
		this.info("ACCTDATE4   = " + rDAY);
		int lSize = lList.size();
		int x = 1;
		
		
		makeExcel.setShiftRow(row+1, lSize-1);
		
		
		for (Map<String, String> tLDVo : lList) {
			// 除萬共用
			BigDecimal gal = new BigDecimal("10000");
//			if (x != lSize) {
//				makeExcel.setShiftRow(row + 1, 1);
//			}
			// 戶號
			makeExcel.setValue(row, 1, tLDVo.get("F0"));

			// 戶名
			makeExcel.setValue(row, 2, tLDVo.get("F17"));

			// 擔保物號
			makeExcel.setValue(row, 3, tLDVo.get("F18"));

			// 擔保物名稱
			makeExcel.setValue(row, 4, tLDVo.get("F19"));

			// 擔保物提供人
			makeExcel.setValue(row, 5, tLDVo.get("F20"));
			// 核貸成數 百分比
			BigDecimal rAtio = getBigDecimal(tLDVo.get("F5"));
			// makeExcel.setValue(row, 4, rAtio,"#,##0.00%","R");
			makeExcel.setValue(row, 6, rAtio, "R");

			// 設 定 股 數 ( 股 )
			BigDecimal sHareMat = getBigDecimal(tLDVo.get("F6"));
			makeExcel.setValue(row, 7, sHareMat, "#,##0", "R");
			//
			makeExcel.setValue(row, 8, "集：");
			//
			makeExcel.setValue(row, 9, sHareMat, "#,##0", "R");

			// 目前餘額(萬) 除萬 4捨5入 要用bigdecimal
			BigDecimal bAlance = getBigDecimal(tLDVo.get("F7"));
			bAlance = bAlance.divide(gal, 0, 1);
			makeExcel.setValue(row, 10, bAlance, "#,##0", "R");

			// 擔保品總市價 除萬 4捨5入 小數點後一位
			BigDecimal cOllateral = getBigDecimal(tLDVo.get("F8"));
			cOllateral = cOllateral.divide(gal, 1, 1);
			makeExcel.setValue(row, 11, cOllateral, "#,##0.0", "R");

			// 實貸成數 百分比
			BigDecimal aCtual = getBigDecimal(tLDVo.get("F9"));
			// makeExcel.setValue(row, 10, aCtual,"#,##0.00%","R");
			makeExcel.setValue(row, 12, aCtual, "R");
			// 每股貸放 小數點後兩位
			BigDecimal sHare = getBigDecimal(tLDVo.get("F10"));
			makeExcel.setValue(row, 13, sHare, "#,##0.00", "R");

			// 調整後鑑定單價 小數點後兩位
			BigDecimal iDenti = getBigDecimal(tLDVo.get("F11"));
			makeExcel.setValue(row, 14, iDenti, "#,##0.00", "R");

			// 擔保品維持率 百分比
			BigDecimal gUarantee = getBigDecimal(tLDVo.get("F12"));
			// makeExcel.setValue(row, 13, gUarantee,"#,##0.00%","R");
			makeExcel.setValue(row, 15, gUarantee, "R");

			// 追繳價格 小數點後兩位
			BigDecimal rEcover = getBigDecimal(tLDVo.get("F13"));
			makeExcel.setValue(row, 16, rEcover, "#,##0.00", "R");

			// 收盤價 小數點後兩位
			BigDecimal cLose = getBigDecimal(tLDVo.get("F14"));
			makeExcel.setValue(row, 17, cLose, "#,##0.00", "R");

			// 全戶維持率 百分比
			BigDecimal hOusehold = getBigDecimal(tLDVo.get("F15"));
			// makeExcel.setValue(row, 16, hOusehold,"#,##0.00%","R");
			makeExcel.setValue(row, 18, hOusehold, "#,##0.00", "R");

			// 收盤價是否低於追繳價
			makeExcel.setValue(row, 19, tLDVo.get("F16"));
			row++;
			x++;
		}

		makeExcel.setValue(1, 10, showRocDate(iDAY, 1));
//		makeExcel.setValue(2, 1, "借款戶號","C");
//		makeExcel.setValue(2, 2, "借款戶名","C");
//		makeExcel.setValue(2, 3, "擔保物號","C");
//		makeExcel.setValue(2, 4, "擔保物名稱","C");
//		makeExcel.setValue(2, 5, "擔保物提供人","C");
//		makeExcel.setValue(2, 6, "核貸成數","C");
//		makeExcel.setValue(2, 7, "設 定 股 數 (股)","C");
//		makeExcel.setValue(2, 10, "目前餘額(萬)","C");
//		makeExcel.setValue(2, 11, "擔保品總市價(萬)","C");
//		makeExcel.setValue(2, 12, "實貸成數","C");
//		makeExcel.setValue(2, 13, "每股貸放(元)","C");
//		makeExcel.setValue(2, 14, "調整後鑑定單價","C");
//		makeExcel.setValue(2, 15, "擔保品維持率","C");
//		makeExcel.setValue(2, 16, "追繳價格","C");
		makeExcel.setValue(2, 17, "(" + rDAY + ")" + "收盤價", "C");
//		makeExcel.setValue(2, 18, "全戶維持率","C");
//		makeExcel.setValue(2, 19, "收盤價是否低於追繳價","C");

		makeExcel.formulaCalculate(row + 6, 1);
	}
}
