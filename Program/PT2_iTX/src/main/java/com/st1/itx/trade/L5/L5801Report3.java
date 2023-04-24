package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L5801ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ExcelFontStyleVo;

@Component("L5801Report3")
@Scope("prototype")
public class L5801Report3 extends MakeReport {

	@Autowired
	L5801ServiceImpl l5801ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	ExcelFontStyleVo fontStyleVo;
	public void exec(int thisMonth, int lastMonth, TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listL5801 = null;

		try {
			listL5801 = l5801ServiceImpl.findAll3(thisMonth, lastMonth, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L5801ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(thisMonth, listL5801, titaVo);
	}

	private void exportExcel(int thisMonth, List<Map<String, String>> listL5801, TitaVo titaVo) throws LogicException {
		this.info("L5801 exportExcel");

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5801", "補貼息核撥清單工作檔", "L5801補貼息核撥清單工作檔",
//				"L5801_底稿_催收款明細表.xlsx", "工作表1", this.showRocDate(thisMonth * 100 + 1, 5));

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5801", "補貼息核撥清單工作檔", "L5801補貼息核撥清單工作檔" + thisMonth, "補貼息核撥清單工作檔" + thisMonth);

		fontStyleVo = new ExcelFontStyleVo();

		fontStyleVo.setFont((short) 1); // 字體 : 標楷體

		fontStyleVo.setSize((short) 12); // 字體大小 : 12

		
		thisMonth = thisMonth -191100;
		String YM = String.valueOf(thisMonth);
		
		if(YM.length() == 5) {
			makeExcel.setValue(1, 1, "申報年月:" + YM.substring(0,3) + "年" + YM.substring(3,5) + "月", fontStyleVo);
		} else {
			makeExcel.setValue(1, 1, "申報年月:" + YM.substring(0,2) + "年" + YM.substring(2,4) + "月", fontStyleVo);
		}
		// 補貼息欄位移到最後
		// 個餘額新增戶號欄位
		makeExcel.setMergedRegion(2,3,1,1);
		makeExcel.setMergedRegion(2,2,2,3);
		makeExcel.setMergedRegion(2,2,4,5);
		makeExcel.setMergedRegion(2,2,6,7);
		makeExcel.setMergedRegion(2,2,8,9);
		makeExcel.setMergedRegion(2,2,10,11);
		makeExcel.setMergedRegion(2,3,12,12);
		
		fontStyleVo = new ExcelFontStyleVo();

		fontStyleVo.setFont((short) 1); // 字體 : 標楷體

		fontStyleVo.setSize((short) 12); // 字體大小 : 12
		
		fontStyleVo.setAlign("C");
		
		makeExcel.setValue(2, 1, "專案融資種類", fontStyleVo);
		makeExcel.setValue(2, 2, "上月貸款餘額", fontStyleVo);
		makeExcel.setValue(2, 4, "本月貸出數", fontStyleVo);
		makeExcel.setValue(2, 6, "本月收回數", fontStyleVo);
		makeExcel.setValue(2, 8, "屆期不再申撥補貼息", fontStyleVo);
		makeExcel.setValue(2, 10, "本月貸款餘額", fontStyleVo);
		makeExcel.setValue(2, 12, "補貼息", fontStyleVo);
		
		makeExcel.setValue(3, 2, "戶數", fontStyleVo);
		makeExcel.setValue(3, 3, "金額", fontStyleVo);
		makeExcel.setValue(3, 4, "戶數", fontStyleVo);
		makeExcel.setValue(3, 5, "金額", fontStyleVo);
		makeExcel.setValue(3, 6, "戶數", fontStyleVo);
		makeExcel.setValue(3, 7, "金額", fontStyleVo);
		makeExcel.setValue(3, 8, "戶數", fontStyleVo);
		makeExcel.setValue(3, 9, "金額", fontStyleVo);
		makeExcel.setValue(3, 10, "戶數", fontStyleVo);
		makeExcel.setValue(3, 11, "金額", fontStyleVo);
		
		makeExcel.setWidth(1, 24);
		makeExcel.setWidth(2, 10);
		makeExcel.setWidth(3, 12);
		makeExcel.setWidth(4, 10);
		makeExcel.setWidth(5, 12);
		makeExcel.setWidth(6, 10);
		makeExcel.setWidth(7, 12);
		makeExcel.setWidth(8, 10);
		makeExcel.setWidth(9, 12);
		makeExcel.setWidth(10, 10);
		makeExcel.setWidth(11, 12);
		makeExcel.setWidth(12, 12);
		int printRow = 4; // 從第二行開始印

		fontStyleVo = new ExcelFontStyleVo();

		fontStyleVo.setFont((short) 1); // 字體 : 標楷體

		fontStyleVo.setSize((short) 12); // 字體大小 : 12
		
		fontStyleVo.setAlign("L");
		
		if (listL5801 == null || listL5801.isEmpty()) {

//			makeExcel.setValue(printRow, 1, "本月無資料", fontStyleVo);

		} else {

			for (Map<String, String> mapL5801 : listL5801) {

				// F0 專案融資種類
				String ProjectKind = mapL5801.get("F0");
				String Kind = "";
				String Rate = mapL5801.get("F12");//補貼利率
				switch (ProjectKind) {
				case "1":
					Kind = "一千二百億元青年優惠房屋貸款暨信用保證專案(不得轉貸、重購)";
					break;
				case "2":
					Kind = "四千億元優惠購屋專案貸款(不得轉貸、重購)";
					break;
				case "3":
//					Kind = "續辦二千億元優惠購屋專案貸款(補貼 0.425%，不得轉貸、重購)";
					Kind = "續辦二千億元優惠購屋專案貸款(補貼 " + Rate + "%，不得轉貸、重購)";
					break;
				case "4":
//					Kind = "續辦四千八百億元優惠購屋專案貸款(補貼 0.25%，不得轉貸、重購)";
					Kind = "續辦四千八百億元優惠購屋專案貸款(補貼 " + Rate + "%，不得轉貸、重購)";
					break;
				case "5":
//					Kind = "續辦六千億元優惠購屋專案貸款(補貼 0.125%，不得轉貸、重購)";
					Kind = "續辦六千億元優惠購屋專案貸款(補貼 " + Rate + "%，不得轉貸、重購)";
					break;
				case "6":
//					Kind = "增撥新台幣四千億元優惠購屋專案貸款(補貼 0.7%，不得重購)";
					Kind = "增撥新台幣四千億元優惠購屋專案貸款(補貼 " + Rate + "%，不得重購)";
					break;
				}
				makeExcel.setValue(printRow, 1, Kind, fontStyleVo);

				// F1 上月貸款餘額(次數)
				String LastMonthCnt = mapL5801.get("F1");
				makeExcel.setValue(printRow, 2, getBigDecimal(LastMonthCnt), fontStyleVo);
				
				// F2 上月貸款餘額
				String LastMonthBal = mapL5801.get("F2");
				makeExcel.setValue(printRow, 3, getBigDecimal(LastMonthBal), fontStyleVo);

				// F3 本月貸出數(次數)
				String ThisMonthOpenCnt = mapL5801.get("F3");
				makeExcel.setValue(printRow, 4, getBigDecimal(ThisMonthOpenCnt), fontStyleVo);
				
				// F4 本月貸出數
				String OpenAmount = mapL5801.get("F4");
				makeExcel.setValue(printRow, 5, getBigDecimal(OpenAmount), fontStyleVo);

				// F5 本月收回數(次數)
				String ThisMonthCloseCnt = mapL5801.get("F5");
				makeExcel.setValue(printRow, 6, getBigDecimal(ThisMonthCloseCnt), fontStyleVo);

				// F6 本月收回數
				String CloseAmount = mapL5801.get("F6");
				makeExcel.setValue(printRow, 7, getBigDecimal(CloseAmount), fontStyleVo);

				// F7 屆期不再申撥補貼息(次數)
				String MaturityCnt = mapL5801.get("F7");
				makeExcel.setValue(printRow, 8, getBigDecimal(MaturityCnt), fontStyleVo);
				
				// F8 屆期不再申撥補貼息
				String MaturityAmount = mapL5801.get("F8");
				makeExcel.setValue(printRow, 9, getBigDecimal(MaturityAmount), fontStyleVo);

				// F9 本月貸款餘額(次數)
				String ThisMonthCnt = mapL5801.get("F9");
				makeExcel.setValue(printRow, 10, getBigDecimal(ThisMonthCnt), fontStyleVo);
				
				// F10 本月貸款餘額
				String ThisMonthBal = mapL5801.get("F10");
				makeExcel.setValue(printRow, 11, getBigDecimal(ThisMonthBal), fontStyleVo);
				
				// F1 補貼息
				String Money = mapL5801.get("F11");
				makeExcel.setValue(printRow, 12, getBigDecimal(Money), fontStyleVo);
				
				printRow++;
			}
			// 畫框線
			makeExcel.setAddRengionBorder("A", 1, "L", printRow-1, 1);

		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
