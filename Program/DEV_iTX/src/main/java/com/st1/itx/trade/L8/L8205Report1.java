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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L8205Report1 extends MakeReport {

	@Autowired
	public L8205ServiceImpl l8205ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;
	
	private List<Map<String, String>> L8205List = null;
	
//	自訂表頭
	@Override
	public void printHeader() {

		this.info("MakeReport.printHeader");

		printHeaderL();

		// 明細起始列(自訂亦必須)
		this.setBeginRow(11);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(58);
	}

	public void printHeaderL() {
		this.print(-3, 5, "程式ID：" + this.getParentTranCode());
		this.print(-3, 50, "新光人壽保險股份有限公司", "C");
		this.print(-4, 5, "報  表：" + this.getRptCode());
		this.print(-4, 40, "疑似洗錢樣態3合理性報表");
		this.print(-3, 80, "報表等級：機密" );
		String bcDate = dDateUtil.getNowStringBc().substring(4, 6) + "/" + dDateUtil.getNowStringBc().substring(6, 8) + "/" + dDateUtil.getNowStringBc().substring(2, 4);
		this.print(-4, 80, "日　　期：" + bcDate);
		this.print(-5, 80, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":" + dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));
		this.print(-6, 80, "頁　　數：　	　" + this.getNowPage());
	}

	// 自訂表尾
	@Override
	public void printFooter() {
		print(-68, 1, "　　協理:　　　　　　　　　　　　　　　　　　經理:　　　　　　　　　　　　　　　　　　經辦:", "P");
			
		}
			
	public boolean exec(TitaVo titaVo) throws LogicException {

		try {
			L8205List = l8205ServiceImpl.L8205Rpt1(titaVo);
			
		} catch (Exception e) {
			this.info("l8205ServiceImpl.L8205Rpt1 error = " + e.toString());
		}

		makeReport(titaVo);

		makeExcel(titaVo);

		if (L8205List != null && L8205List.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public void makeReport(TitaVo titaVo) throws LogicException{
		
		// 入帳日區間 Min
		String stEntryDate = titaVo.getParam("DateStart");
		stEntryDate = stEntryDate.substring(0, 3)+"/"+stEntryDate.substring(3, 5)+"/"+stEntryDate.substring(5, 7);
			
		// 入帳日區間  Max
		String edEntryDate = titaVo.getParam("DateEnd");
		edEntryDate = edEntryDate.substring(0, 3)+"/"+edEntryDate.substring(3, 5)+"/"+edEntryDate.substring(5, 7);
				
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L8205", "疑似洗錢樣態3合理性報表", "", "A4", "P");
				
		if (L8205List != null && L8205List.size() > 0) {
			DecimalFormat df1 = new DecimalFormat("#,##0");

			this.print(-7, 40, stEntryDate + "－" + edEntryDate);
			this.print(-9, 3, "樣態 入帳日 　戶號　　戶名　　　　　累積金額　　　　總筆數　　經辦　　　合理性　　　　異動日期");
			this.print(-10, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

			for (Map<String, String> tL8205Vo : L8205List) {
				// 樣態
				print(1, 4, tL8205Vo.get("F0"));

				// 入帳日
				print(0, 6, tL8205Vo.get("F1") == "0" || tL8205Vo.get("F1") == null || tL8205Vo.get("F1").length() == 0 || tL8205Vo.get("F1").equals(" ") ? " " : showDate(tL8205Vo.get("F1"), 1));

				// 戶號
				print(0, 16, padStart(tL8205Vo.get("F2"), 7, "0"));

				//戶名
				print(0, 24, tL8205Vo.get("F3"));
								
				// 累積金額
				BigDecimal f4 = tL8205Vo.get("F4") == "0" || tL8205Vo.get("F4") == null || tL8205Vo.get("F4").length() == 0 || tL8205Vo.get("F4").equals(" ") ? BigDecimal.ZERO
						: new BigDecimal(tL8205Vo.get("F4"));

				print(0, 48, f4.equals(BigDecimal.ZERO) ? " " : df1.format(f4), "R");

				//總筆數
				print(0, 55, tL8205Vo.get("F5"),"R");
				
				//經辦
				print(0, 57, tL8205Vo.get("F6"));
				
				//合理性
				print(0, 72, tL8205Vo.get("F7"));
				
				//異動日
				print(0, 81, tL8205Vo.get("F8") == "0" || tL8205Vo.get("F8") == null || tL8205Vo.get("F8").length() == 0 || tL8205Vo.get("F8").equals(" ") ? " " : showDate(tL8205Vo.get("F8"), 1));
	
				

				//經辦說明
				String EmpNoDesc = tL8205Vo.get("F9");
				if(!EmpNoDesc.isEmpty()) {
					EmpNoDesc = EmpNoDesc.replace("$n", "");
				}
				print(1, 4, "經辦說明:"+EmpNoDesc);
				print(1, 4,"");
				//主管覆核
				String check = tL8205Vo.get("F10");
				if(("Y").equals(check)) {
					check = "同意";
				}
				print(1, 4, "主管覆核: "+check);
				print(1, 4, "");
				
				// 檢查列數
				checkRow(stEntryDate, edEntryDate);
			}

		} else {
			this.print(1, 3, "本日無資料");
			this.print(-7, 40, stEntryDate + "－" + edEntryDate);
			this.print(-9, 3, "樣態 入帳日 　戶號　　戶名　　　　　累積金額　　　　總筆數　　經辦　　　合理性　　　　異動日期");
			this.print(-10, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");

		}

		this.print(-64, 50, "===== 報　表　結　束 =====", "C");
		long sno = this.close();
		this.toPdf(sno);
		
	}
	
	public void makeExcel(TitaVo titaVo) throws LogicException{
		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L8205", "疑似洗錢樣態3合理性報表", "L8205" + "_" + "疑似洗錢樣態3合理性報表");
		printExcelHeader();
		
		int rowCursor = 2;
		
		if (L8205List != null && L8205List.size() > 0) {
			
			for (Map<String, String> tL8205Vo : L8205List) {
				
				makeExcel.setValue(rowCursor, 1, tL8205Vo.get("F0"));
				
				makeExcel.setValue(rowCursor, 2, tL8205Vo.get("F1") == "0" || tL8205Vo.get("F1") == null || tL8205Vo.get("F1").length() == 0 || tL8205Vo.get("F1").equals(" ") ? " " : showDate(tL8205Vo.get("F1"),1));
				
				makeExcel.setValue(rowCursor, 3, padStart(tL8205Vo.get("F2"), 7, "0"));
				
				makeExcel.setValue(rowCursor, 4, tL8205Vo.get("F3"));
				
				BigDecimal Amt = parse.stringToBigDecimal(tL8205Vo.get("F4"));
				makeExcel.setValue(rowCursor, 5, Amt, "#,##0");
				
				makeExcel.setValue(rowCursor, 6, tL8205Vo.get("F5"));
				
				makeExcel.setValue(rowCursor, 7, tL8205Vo.get("F6"));
				
				makeExcel.setValue(rowCursor, 8, tL8205Vo.get("F7"));
				
				makeExcel.setValue(rowCursor, 9, tL8205Vo.get("F8") == "0" || tL8205Vo.get("F8") == null || tL8205Vo.get("F8").length() == 0 || tL8205Vo.get("F8").equals(" ") ? " " : showDate(tL8205Vo.get("F8"), 1));
				
				//經辦說明
				String EmpNoDesc = tL8205Vo.get("F9");
				if(!EmpNoDesc.isEmpty()) {
					EmpNoDesc = EmpNoDesc.replace("$n", "");
				}
				makeExcel.setValue(rowCursor, 10, EmpNoDesc);
				
				String check = tL8205Vo.get("F10");
				if(("Y").equals(check)) {
					check = "同意";
				}
				makeExcel.setValue(rowCursor, 11, check);
				
				
				
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

	/**
	 * 檢查列數
	 * 
	 * @param stEntryDate 日期 區間-起
	 * @param edEntryDate 日期 區間-止
	 */
	private void checkRow(String stEntryDate, String edEntryDate) {
		if (this.NowRow >= 60) {

			newPage();
			this.print(-7, 40, stEntryDate + "－" + edEntryDate);
			this.print(-9, 3, "樣態 入帳日 　戶號　　戶名　　　　　累積金額　　　　總筆數　　經辦　　　合理性　　　　異動日期");
			this.print(-10, 3, "－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
}

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
		
		makeExcel.setValue(1, 6, "總筆數");
		
		makeExcel.setValue(1, 7, "經辦");
		makeExcel.setWidth(7, 20);
		
		makeExcel.setValue(1, 8, "合理性");
		
		makeExcel.setValue(1, 9, "異動日期");
		makeExcel.setWidth(9, 14);
		
		makeExcel.setValue(1, 10, "經辦說明");
		makeExcel.setWidth(10, 30);
		
		makeExcel.setValue(1, 11, "主管覆核");
		makeExcel.setWidth(11, 20);
		
		
	}
}
