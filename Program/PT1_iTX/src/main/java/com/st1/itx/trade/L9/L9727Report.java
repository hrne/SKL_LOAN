package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9727ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class L9727Report extends MakeReport {

	@Autowired
	L9727ServiceImpl l9727ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "L9727";
	String txname = "金檢Q53資料";
	String sheetName = "yyy.mm.dd";

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9727Report exec start ...");

		int inputDrawdownDateStart = Integer.parseInt(titaVo.getParam("inputDrawdownDateStart")) + 19110000;
		int inputDrawdownDateEnd = Integer.parseInt(titaVo.getParam("inputDrawdownDateEnd")) + 19110000;

		List<Map<String, String>> listL9727 = null;

		try {
			listL9727 = l9727ServiceImpl.findAll(inputDrawdownDateStart, inputDrawdownDateEnd, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9726ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, listL9727);

		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> list) throws LogicException {

		this.info("L9727Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txcd, txname, txcd + "_" + txname, txcd + "_底稿_" + txname + ".xlsx", sheetName);

		// 更新SheetName
		makeExcel.setSheet(sheetName, this.showRocDate(titaVo.getEntDyI(), 6));

		int rowCursor = 2;

		if (list != null && !list.isEmpty()) {

			for (Map<String, String> m : list) {

				// 專員部門
				makeExcel.setValue(rowCursor, 1, m.get("F0"));
				// 專員區部
				makeExcel.setValue(rowCursor, 2, m.get("F1"));
				// 專員單位
				makeExcel.setValue(rowCursor, 3, m.get("F2"));
				// 專員員編
				makeExcel.setValue(rowCursor, 4, m.get("F3"));
				// 專員姓名
				makeExcel.setValue(rowCursor, 5, m.get("F4"));
				// 戶號
				makeExcel.setValue(rowCursor, 6, m.get("F5"));
				// 額度
				makeExcel.setValue(rowCursor, 7, m.get("F6"));
				// 撥款
				makeExcel.setValue(rowCursor, 8, m.get("F7"));
				// 戶名
				makeExcel.setValue(rowCursor, 9, m.get("F8"));
				// 客戶ID
				makeExcel.setValue(rowCursor, 10, m.get("F9"));
				// 核准額度
				makeExcel.setValue(rowCursor, 11, getBigDecimal(m.get("F10")), "#,##0");
				// 餘額
				makeExcel.setValue(rowCursor, 12, getBigDecimal(m.get("F11")), "#,##0");
				// 撥款日期
				makeExcel.setValue(rowCursor, 13, showRocDate(m.get("F12"), 1));
				// 資金用途別
				makeExcel.setValue(rowCursor, 14, m.get("F13"));
				// 利害關係人
				makeExcel.setValue(rowCursor, 15, m.get("F14"));
				// 評估淨值
				makeExcel.setValue(rowCursor, 16, getBigDecimal(m.get("F15")), "#,##0");
				// 風險等級
				makeExcel.setValue(rowCursor, 17, m.get("F16"));
				// 上次繳息日
				makeExcel.setValue(rowCursor, 18, showRocDate(m.get("F17"), 1));
				// 利率
				makeExcel.setValue(rowCursor, 19, getBigDecimal(m.get("F18")), "#,##0.0000");
				// 核准主管(員編)
				makeExcel.setValue(rowCursor, 20, m.get("F19"));
				// 核准主管(姓名)
				makeExcel.setValue(rowCursor, 21, m.get("F20"));
				// 寬限期(年)
				makeExcel.setValue(rowCursor, 22, m.get("F21"));
				// 展期/借新還舊
				makeExcel.setValue(rowCursor, 23, m.get("F22"));
				// 代償碼
				makeExcel.setValue(rowCursor, 24, m.get("F23"));
				// 擔保品代號1
				makeExcel.setValue(rowCursor, 25, m.get("F24"));
				// 擔保品代號2
				makeExcel.setValue(rowCursor, 26, m.get("F25"));
				// 擔保品號碼
				makeExcel.setValue(rowCursor, 27, m.get("F26"));
				// 寬限到期日
				makeExcel.setValue(rowCursor, 28, showRocDate(m.get("F27"), 1));
				// 寬限區分
				makeExcel.setValue(rowCursor, 29, m.get("F28"));
				// 更新會計日期
				makeExcel.setValue(rowCursor, 30, showRocDate(m.get("F29"), 1));
				// 更新交易代號
				makeExcel.setValue(rowCursor, 31, m.get("F30"));

				rowCursor++;
			}
		} else {
			makeExcel.setValue(rowCursor, 1, "本日無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
