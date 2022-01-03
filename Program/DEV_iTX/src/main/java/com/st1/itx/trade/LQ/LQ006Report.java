package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ006ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LQ006Report extends MakeReport {

	@Autowired
	public LQ006ServiceImpl lQ006ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> lQ006List = null;
		List<Map<String, String>> lQ006List_Total = null;
		
		try {
			lQ006List = lQ006ServiceImpl.findAll(titaVo);
			lQ006List_Total = lQ006ServiceImpl.findTotal(titaVo);

			exportExcel(titaVo, lQ006List, lQ006List_Total);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lQ006ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList, List<Map<String, String>> LDListTotal) throws LogicException {
		this.info("exportExcel---------");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ006", "已逾期未減損-帳齡分析", "LQ006已逾期未減損-帳齡分析", "LQ006已逾期未減損-帳齡分析.xlsx", "已逾期未減損-帳齡分析");

		// 起始列
		int row = 2;

		// 有無資料
		if (LDList != null && !LDList.isEmpty()) {
			for (Map<String, String> tLDVo : LDList) {

				// 帳號
				makeExcel.setValue(row, 1, tLDVo.get("F0").toString());
				// 本金餘額
				makeExcel.setValue(row, 2, getBigDecimal(tLDVo.get("F1")), "#,##0", "R");
				// 應收利息
				// format: show - if 0
				makeExcel.setValue(row, 3, getBigDecimal(tLDVo.get("F2")), "_-* #,##0_-;-* #,##0_-;_-* \"-\"??_-;_-@_-", "R");
				// 法拍及火險費用
				// format: show - if 0
				makeExcel.setValue(row, 4, getBigDecimal(tLDVo.get("F3")), "_-* #,##0_-;-* #,##0_-;_-* \"-\"??_-;_-@_-", "R");
				// 三項總計
				makeExcel.setValue(row, 5, getBigDecimal(tLDVo.get("F4")), "#,##0", "R");
				// 逾期繳款天數
				makeExcel.setValue(row, 6, getBigDecimal(tLDVo.get("F5")), "##0", "R");

				// 換列
				row++;

			}
		
		makeExcel.formulaCaculate(1, 5);
			
		// 換去做 Sheet1
		if (LDListTotal != null && !LDListTotal.isEmpty())
		{
			makeExcel.setSheet("Sheet1");
			
			row = 4; // 開始行, 1-based
			
			for (Map<String, String> tLDVo : LDListTotal)
			{
				// 日期
				makeExcel.setValue(row, 1, tLDVo.get("F0"), "L");
				// 31-60 天金額
				makeExcel.setValue(row, 2, getBigDecimal(tLDVo.get("F1")), "#,##0", "R");
				// 61-90 天金額
				makeExcel.setValue(row, 3, getBigDecimal(tLDVo.get("F2")), "#,##0", "R");
				// 合計
				makeExcel.setValue(row, 4, getBigDecimal(tLDVo.get("F3")), "#,##0", "R");
				
				row++;
			}
		}
		
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}

		long sno = makeExcel.close();
		// makeExcel.toExcel(sno);
	}

}
