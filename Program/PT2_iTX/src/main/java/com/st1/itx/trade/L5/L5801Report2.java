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
import com.st1.itx.util.format.FormatUtil;

@Component("L5801Report2")
@Scope("prototype")
public class L5801Report2 extends MakeReport {

	@Autowired
	L5801ServiceImpl l5801ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	ExcelFontStyleVo fontStyleVo;

	public void exec(int thisMonth, int lastMonth, TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listL5801 = null;

		try {
			listL5801 = l5801ServiceImpl.findAll2(thisMonth, lastMonth, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L5801ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(thisMonth, listL5801, titaVo);
	}

	private void exportExcel(int thisMonth, List<Map<String, String>> listL5801, TitaVo titaVo) throws LogicException {
		this.info("L5801 exportExcel");

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5801", "補貼息結清名冊終止名冊工作檔", "L5801補貼息結清名冊終止名冊工作檔",
//				"L5801_底稿_催收款明細表.xlsx", "工作表1", this.showRocDate(thisMonth * 100 + 1, 5));

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5801", "補貼息結清名冊終止名冊工作檔", "L5801補貼息結清名冊終止名冊工作檔" + thisMonth, "補貼息結清名冊終止名冊工作檔" + thisMonth);

		fontStyleVo = new ExcelFontStyleVo();

		fontStyleVo.setFont((short) 1); // 字體 : 標楷體

		fontStyleVo.setSize((short) 12); // 字體大小 : 12

		
		makeExcel.setValue(1, 1, "種類", fontStyleVo);
		makeExcel.setValue(1, 2, "戶號額度", fontStyleVo);
		makeExcel.setValue(1, 3, "商品代碼", fontStyleVo);
		makeExcel.setValue(1, 4, "專案融資種類", fontStyleVo);
		makeExcel.setValue(1, 5, "借款人戶名", fontStyleVo);
		makeExcel.setValue(1, 6, "借款人身份證字號", fontStyleVo);
		makeExcel.setValue(1, 7, "原核撥優惠貸款金額", fontStyleVo);
		makeExcel.setValue(1, 8, "銷戶/終止補貼日期", fontStyleVo);
		makeExcel.setValue(1, 9, "註記", fontStyleVo);
		makeExcel.setValue(1, 10, "上個月貸款餘額", fontStyleVo);

		makeExcel.setWidth(1, 12);
		makeExcel.setWidth(2, 16);
		makeExcel.setWidth(3, 12);
		makeExcel.setWidth(4, 17);
		makeExcel.setWidth(5, 14);
		makeExcel.setWidth(6, 23);
		makeExcel.setWidth(7, 25);
		makeExcel.setWidth(8, 24);
		makeExcel.setWidth(9, 7);
		makeExcel.setWidth(10, 25);

		
		int printRow = 2; // 從第三行開始印

		if (listL5801 == null || listL5801.isEmpty()) {

//			makeExcel.setValue(printRow, 1, "本月無資料", fontStyleVo);

		} else {

			for (Map<String, String> mapL5801 : listL5801) {

				// F0 種類
				makeExcel.setValue(printRow, 1, mapL5801.get("F0"));
				// F1 戶號
				String custNo = FormatUtil.pad9(mapL5801.get("F1"), 7);
				// F2 額度
				String facmNo = FormatUtil.pad9(mapL5801.get("F2"), 3);

				makeExcel.setValue(printRow, 2, custNo + "-" + facmNo, fontStyleVo);

				// F3 商品代碼
				String ProdNo = mapL5801.get("F3");

				makeExcel.setValue(printRow, 3, ProdNo, fontStyleVo);

				// F4 專案融資種類
				String ProjectKind = mapL5801.get("F4");
				String Kind = "";
				String Rate = mapL5801.get("F11");//補貼利率
				switch (ProjectKind) {
				case "1":
					Kind = "一千二百億元青年優惠房屋貸款暨信用保證專案(不得轉貸、重購)";
					break;
				case "2":
					Kind = "四千億元優惠購屋專案貸款(不得轉貸、重購)";
					break;
				case "3":
					Kind = "續辦二千億元優惠購屋專案貸款(補貼 " + Rate + "%，不得轉貸、重購)";
					break;
				case "4":
					Kind = "續辦四千八百億元優惠購屋專案貸款(補貼 " + Rate + "%，不得轉貸、重購)";
					break;
				case "5":
					Kind = "續辦六千億元優惠購屋專案貸款(補貼 " + Rate + "%，不得轉貸、重購)";
					break;
				case "6":
					Kind = "增撥新台幣四千億元優惠購屋專案貸款(補貼 " + Rate + "%，不得重購)";
					break;
				}
				
				makeExcel.setValue(printRow, 4, Kind, fontStyleVo);

				// F5 借款人戶名
				String CustName = mapL5801.get("F5");

				makeExcel.setValue(printRow, 5, CustName, fontStyleVo);

				// F6 借款人身份證字號	
				String CustId = mapL5801.get("F6");

				makeExcel.setValue(printRow, 6, CustId, fontStyleVo);

				// F7 原核撥優惠貸款金額
				String DrawdownAmt = mapL5801.get("F7");

				makeExcel.setValue(printRow, 7, getBigDecimal(DrawdownAmt), fontStyleVo);

				// F8 銷戶/終止補貼日期
				String CloseDate = mapL5801.get("F8");
				CloseDate = String.valueOf(Integer.valueOf(CloseDate) - 19110000); // 轉民國年
				makeExcel.setValue(printRow, 8, CloseDate, fontStyleVo);

				// F9 註記
				String Remark = mapL5801.get("F9");

				makeExcel.setValue(printRow, 9, Remark, fontStyleVo);

				// F10 上個月貸款餘額
				String LastmonthBal = mapL5801.get("F10");

				makeExcel.setValue(printRow, 10, getBigDecimal(LastmonthBal), fontStyleVo);

				
				printRow++;
			}
			// 畫框線
			makeExcel.setAddRengionBorder("A", 1, "I", printRow-1, 1);

		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
