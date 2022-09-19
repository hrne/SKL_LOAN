package com.st1.itx.trade.L8;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L8205ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L8205Report2 extends MakeReport {

	@Autowired
	L8205ServiceImpl l8205ServiceImpl;

	/* 轉換工具 */
	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	private List<Map<String, String>> listL8205 = null;

	private String stEntryDate;
	private String edEntryDate;

//	自訂表頭
	@Override
	public void printHeader() {
		this.print(-4, 3, "程式ID：" + this.getParentTranCode());
		this.print(-4, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		this.print(-5, 3, "報  表：" + this.getRptCode());
		this.print(-5, this.getMidXAxis(), "疑似洗錢樣態1、2合理性報表", "C");
		this.print(-4, 80, "報表等級：機密");
		String bcDate = dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6, 8)
				+ "/" + dDateUtil.getNowStringBc().substring(2, 4);
		this.print(-5, 80, "日　　期：" + bcDate);
		this.print(-6, 80, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-7, 80, "頁　　數：　	　" + this.getNowPage());
		this.print(-7, this.getMidXAxis(), stEntryDate + "－" + edEntryDate, "C");
		this.print(-9, 3, "樣態 入帳日 　　戶號　　戶名　　　　　累積金額　　　　經辦　　　合理性　　　　同意日");
		this.print(-10, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
	}

	// 自訂表尾
	@Override
	public void printFooter() {
		print(-66, this.getMidXAxis(), "　經辦:　　　　　　　　　　　　　　經理:　　　　　　　　　　　　　　協理:　　　　　　　　　　　　　　", "C");
	}

	public boolean exec(TitaVo titaVo) throws LogicException {
		try {
			listL8205 = l8205ServiceImpl.L8205Rpt2(titaVo);

		} catch (Exception e) {
			this.info("l8205ServiceImpl.L8205Rpt2 error = " + e.toString());
		}

		makeReport(titaVo);

		makeExcel(titaVo);

		return (listL8205 != null && !listL8205.isEmpty());
	}

	public void makeReport(TitaVo titaVo) throws LogicException {

		// 入帳日區間 Min
		stEntryDate = titaVo.getParam("DateStart");
		stEntryDate = stEntryDate.substring(0, 3) + "/" + stEntryDate.substring(3, 5) + "/"
				+ stEntryDate.substring(5, 7);

		// 入帳日區間 Max
		edEntryDate = titaVo.getParam("DateEnd");
		edEntryDate = edEntryDate.substring(0, 3) + "/" + edEntryDate.substring(3, 5) + "/"
				+ edEntryDate.substring(5, 7);
		// 筆數計算
		int icount = 0;

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getKinbr()).setRptDate(titaVo.getEntDyI())
				.setSecurity("機密").setRptCode("L8205").setRptItem("疑似洗錢樣態1、2合理性報表").setPageOrientation("P")
				.setUseDefault(true).build();

		this.open(titaVo, reportVo, "A4直式底稿.pdf");

		this.setBeginRow(11);
		this.setMaxRows(50);

		if (listL8205 != null && listL8205.size() > 0) {
			DecimalFormat df1 = new DecimalFormat("#,##0");

			for (Map<String, String> tL8205Vo : listL8205) {
				// 樣態
				print(1, 4, tL8205Vo.get("F0"));

				// 入帳日
				print(0, 6, tL8205Vo.get("F1") == "0" || tL8205Vo.get("F1") == null || tL8205Vo.get("F1").length() == 0
						|| tL8205Vo.get("F1").equals(" ") ? " " : showDate(tL8205Vo.get("F1"), 1));

				// 戶號
				print(0, 17, padStart(tL8205Vo.get("F2"), 7, "0"));

				// 戶名
				String custname = tL8205Vo.get("F3");
				if (custname.length() > 8) {
					custname = custname.substring(0, 8);
				}
				print(0, 25, custname);

				// 累積金額
				BigDecimal f4 = tL8205Vo.get("F4") == "0" || tL8205Vo.get("F4") == null
						|| tL8205Vo.get("F4").length() == 0 || tL8205Vo.get("F4").equals(" ") ? BigDecimal.ZERO
								: new BigDecimal(tL8205Vo.get("F4"));

				print(0, 50, f4.equals(BigDecimal.ZERO) ? " " : df1.format(f4), "R");

				// 經辦
				print(0, 52, tL8205Vo.get("F5"));

				// 合理性
				print(0, 64, tL8205Vo.get("F6"));

				// 同意日
				print(0, 74, tL8205Vo.get("F7") == "0" || tL8205Vo.get("F7") == null || tL8205Vo.get("F7").length() == 0
						|| tL8205Vo.get("F7").equals(" ") ? " " : showDate(tL8205Vo.get("F7"), 1));

				// 經辦說明
				String EmpNoDesc = tL8205Vo.get("F8");
				String EmpNoDesc1 = "";// 總長100個字,接1-45個
				String EmpNoDesc2 = "";// 接46-90個
				String EmpNoDesc3 = "";// 剩餘10個
				int ilength = 0;
				if (!EmpNoDesc.isEmpty()) {
					EmpNoDesc = EmpNoDesc.replace("$n", "");
					ilength = EmpNoDesc.length();
				}
				if (ilength > 45) {
					if (ilength > 90) {
						EmpNoDesc1 = EmpNoDesc.substring(0, 45);
						EmpNoDesc2 = EmpNoDesc.substring(45, 90);
						EmpNoDesc3 = EmpNoDesc.substring(90, ilength);
					} else {
						EmpNoDesc1 = EmpNoDesc.substring(0, 45);
						EmpNoDesc2 = EmpNoDesc.substring(45, ilength);
					}
				} else {
					EmpNoDesc1 = EmpNoDesc.substring(0, ilength);
				}

				if (!EmpNoDesc1.isEmpty()) {
					print(1, 4, "經辦說明:" + EmpNoDesc1);
				}
				if (!EmpNoDesc2.isEmpty()) {
					print(1, 4, "　　　　 " + EmpNoDesc2);
				}
				if (!EmpNoDesc3.isEmpty()) {
					print(1, 4, "　　　　 " + EmpNoDesc3);
				}

				print(1, 4, "");

				// 主管覆核
				String check = tL8205Vo.get("F9");
				if (("Y").equals(check)) {
					check = "同意";
				}
				if (("N").equals(check)) {
					check = "退回";
				}
				print(1, 4, "主管覆核: " + check);
				print(1, 4, "");

				icount++;
			}
			if (icount > 0) {
				print(1, 50, "【合　計：　" + icount + "　筆】", "C");
			}

		} else {
			this.print(1, 3, "本日無資料");
		}

		this.print(-64, this.getMidXAxis(), "===== 報　表　結　束 =====", "C");

		long sno = this.close();
		this.toPdf(sno);
	}

	public void makeExcel(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L8205", "疑似洗錢樣態1、2合理性報表",
				"L8205" + "_" + "疑似洗錢樣態1、2合理性報表");
		printExcelHeader();

		int rowCursor = 2;

		if (listL8205 != null && listL8205.size() > 0) {

			for (Map<String, String> tL8205Vo : listL8205) {

				makeExcel.setValue(rowCursor, 1, tL8205Vo.get("F0"));

				makeExcel
						.setValue(rowCursor, 2,
								tL8205Vo.get("F1") == "0" || tL8205Vo.get("F1") == null
										|| tL8205Vo.get("F1").length() == 0 || tL8205Vo.get("F1").equals(" ") ? " "
												: showDate(tL8205Vo.get("F1"), 1));

				makeExcel.setValue(rowCursor, 3, padStart(tL8205Vo.get("F2"), 7, "0"));

				makeExcel.setValue(rowCursor, 4, tL8205Vo.get("F3"));

				BigDecimal Amt = parse.stringToBigDecimal(tL8205Vo.get("F4"));
				makeExcel.setValue(rowCursor, 5, Amt, "#,##0");

				makeExcel.setValue(rowCursor, 6, tL8205Vo.get("F5"));

				makeExcel.setValue(rowCursor, 7, tL8205Vo.get("F6"));

				makeExcel
						.setValue(rowCursor, 8,
								tL8205Vo.get("F7") == "0" || tL8205Vo.get("F7") == null
										|| tL8205Vo.get("F7").length() == 0 || tL8205Vo.get("F7").equals(" ") ? " "
												: showDate(tL8205Vo.get("F7"), 1));

				// 經辦說明
				String EmpNoDesc = tL8205Vo.get("F8");
				if (!EmpNoDesc.isEmpty()) {
					EmpNoDesc = EmpNoDesc.replace("$n", "");
				}
				makeExcel.setValue(rowCursor, 9, EmpNoDesc);

				String check = tL8205Vo.get("F9");
				if (("Y").equals(check)) {
					check = "同意";
				}
				if (("N").equals(check)) {
					check = "退回";
				}
				makeExcel.setValue(rowCursor, 10, check);

				rowCursor++;
			}

		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

	}

	private String padStart(String temp, int len, String tran) {
		if (temp.length() < len) {
			for (int i = temp.length(); i < len; i++) {
				temp = tran + temp;
			}
		}
		return temp;
	}

	private String showDate(String date, int iType) {
//		this.info("MakeReport.toPdf showRocDate1 = " + date);
		if (date == null || date.equals("") || date.equals("0") || date.equals(" ")) {
			return " ";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);
//		this.info("MakeReport.toPdf showRocDate2 = " + rocdatex);

		if (rocdatex.length() == 7) {
			return rocdatex.substring(0, 3) + "/" + rocdatex.substring(3, 5) + "/" + rocdatex.substring(5, 7);
		} else {
			return rocdatex.substring(0, 2) + "/" + rocdatex.substring(2, 4) + "/" + rocdatex.substring(4, 6);

		}

	}

	private void printExcelHeader() throws LogicException {
		makeExcel.setValue(1, 1, "樣態");

		makeExcel.setValue(1, 2, "入帳日");
		makeExcel.setWidth(2, 14);

		makeExcel.setValue(1, 3, "戶號");
		makeExcel.setWidth(3, 16);

		makeExcel.setValue(1, 4, "戶名");
		makeExcel.setWidth(4, 20);

		makeExcel.setValue(1, 5, "累積金額");
		makeExcel.setWidth(5, 20);

		makeExcel.setValue(1, 6, "經辦");
		makeExcel.setWidth(6, 20);

		makeExcel.setValue(1, 7, "合理性");

		makeExcel.setValue(1, 8, "同意日期");
		makeExcel.setWidth(8, 14);

		makeExcel.setValue(1, 9, "經辦說明");
		makeExcel.setWidth(9, 30);

		makeExcel.setValue(1, 10, "主管覆核");
		makeExcel.setWidth(10, 20);

	}
}
