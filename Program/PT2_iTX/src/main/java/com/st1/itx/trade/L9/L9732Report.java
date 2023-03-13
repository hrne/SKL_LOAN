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
import com.st1.itx.util.common.data.ReportVo;

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

		exportExcel(titaVo, lL9732);
		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> list) throws LogicException {

		this.info(TXCD + "Report exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = TXCD;
		String fileItem = TXName;
		String fileName = TXCD + "_" + TXName;
		String defaultExcel = TXCD + "_底稿_" + TXName + ".xlsx";
		String defaultSheet = "工作表1";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
		makeExcel.setSheet(defaultSheet,"質押股票明細表");


		String iDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE")));
		String mmdd = showRocDate(iDAY, 1).substring(4);
		this.info("iDAY = " + iDAY);
		this.info("mmdd = " + mmdd);

		if (list != null && !list.isEmpty()) {
			if (list.size() > 1) {
				makeExcel.setShiftRow(row + 1, list.size() - 1);
			}

			for (Map<String, String> tLDVo : list) {
				// 除萬共用
				BigDecimal gal = new BigDecimal("10000");

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
				BigDecimal ratio = getBigDecimal(tLDVo.get("F5"));
				makeExcel.setValue(row, 6, ratio, "R");

				// 設 定 股 數 ( 股 )
				BigDecimal shareMat = getBigDecimal(tLDVo.get("F6"));
				makeExcel.setValue(row, 7, shareMat, "#,##0", "R");
				//
				makeExcel.setValue(row, 8, "集：");
				//
				makeExcel.setValue(row, 9, shareMat, "#,##0", "R");

				// 目前餘額(萬) 除萬 4捨5入 要用bigdecimal
				BigDecimal balance = getBigDecimal(tLDVo.get("F7"));
				balance = balance.divide(gal, 0, 1);
				makeExcel.setValue(row, 10, balance, "#,##0", "R");

				// 擔保品總市價 除萬 4捨5入 小數點後一位
				BigDecimal collateral = getBigDecimal(tLDVo.get("F8"));
				collateral = collateral.divide(gal, 1, 1);
				makeExcel.setValue(row, 11, collateral, "#,##0.0", "R");

				// 實貸成數 百分比
				BigDecimal actual = getBigDecimal(tLDVo.get("F9"));
				makeExcel.setValue(row, 12, actual, "R");
				
				// 每股貸放 小數點後兩位
				BigDecimal share = getBigDecimal(tLDVo.get("F10"));
				makeExcel.setValue(row, 13, share, "#,##0.00", "R");

				// 調整後鑑定單價 小數點後兩位
				BigDecimal identi = getBigDecimal(tLDVo.get("F11"));
				makeExcel.setValue(row, 14, identi, "#,##0.00", "R");

				// 擔保品維持率 百分比
				BigDecimal guarantee = getBigDecimal(tLDVo.get("F12"));
				makeExcel.setValue(row, 15, guarantee, "R");

				// 追繳價格 小數點後兩位
				BigDecimal recover = getBigDecimal(tLDVo.get("F13"));
				makeExcel.setValue(row, 16, recover, "#,##0.00", "R");

				// 收盤價 小數點後兩位
				BigDecimal close = getBigDecimal(tLDVo.get("F14"));
				makeExcel.setValue(row, 17, close, "#,##0.00", "R");

				// 全戶維持率 百分比
				BigDecimal household = getBigDecimal(tLDVo.get("F15"));
				makeExcel.setValue(row, 18, household, "#,##0.00", "R");

				// 收盤價是否低於追繳價
				makeExcel.setValue(row, 19, tLDVo.get("F16"));
				row++;
			}
		}

		makeExcel.setValue(1, 10, showRocDate(iDAY, 1));
		// makeExcel.setValue(2, 1, "借款戶號","C");
		// makeExcel.setValue(2, 2, "借款戶名","C");
		// makeExcel.setValue(2, 3, "擔保物號","C");
		// makeExcel.setValue(2, 4, "擔保物名稱","C");
		// makeExcel.setValue(2, 5, "擔保物提供人","C");
		// makeExcel.setValue(2, 6, "核貸成數","C");
		// makeExcel.setValue(2, 7, "設 定 股 數 (股)","C");
		// makeExcel.setValue(2, 10, "目前餘額(萬)","C");
		// makeExcel.setValue(2, 11, "擔保品總市價(萬)","C");
		// makeExcel.setValue(2, 12, "實貸成數","C");
		// makeExcel.setValue(2, 13, "每股貸放(元)","C");
		// makeExcel.setValue(2, 14, "調整後鑑定單價","C");
		// makeExcel.setValue(2, 15, "擔保品維持率","C");
		// makeExcel.setValue(2, 16, "追繳價格","C");
		makeExcel.setValue(2, 17, "(" + mmdd + ")" + "收盤價", "C");
		// makeExcel.setValue(2, 18, "全戶維持率","C");
		// makeExcel.setValue(2, 19, "收盤價是否低於追繳價","C");

		makeExcel.formulaCalculate(row + 6, 1);
		makeExcel.close();
	}
}
