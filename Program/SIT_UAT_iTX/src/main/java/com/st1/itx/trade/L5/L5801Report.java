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

@Component("L5801Report")
@Scope("prototype")
public class L5801Report extends MakeReport {

	@Autowired
	L5801ServiceImpl l5801ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	ExcelFontStyleVo fontStyleVo;

	public void exec(int thisMonth, int lastMonth, TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listL5801 = null;

		try {
			listL5801 = l5801ServiceImpl.findAll(thisMonth, lastMonth, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L5801ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(thisMonth, listL5801, titaVo);
	}

	private void exportExcel(int thisMonth, List<Map<String, String>> listL5801, TitaVo titaVo) throws LogicException {
		this.info("L5801 exportExcel");

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5801", "補貼息申貸名冊工作檔", "L5801補貼息申貸名冊工作檔",
//				"L5801_底稿_催收款明細表.xlsx", "工作表1", this.showRocDate(thisMonth * 100 + 1, 5));

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5801", "補貼息申貸名冊工作檔", "L5801補貼息申貸名冊工作檔" + thisMonth, "補貼息申貸名冊工作檔" + thisMonth);

		fontStyleVo = new ExcelFontStyleVo();

		fontStyleVo.setFont((short) 1); // 字體 : 標楷體

		fontStyleVo.setSize((short) 12); // 字體大小 : 12
		
		makeExcel.setValue(1, 1, "戶號額度", fontStyleVo);
		makeExcel.setValue(1, 2, "商品代碼", fontStyleVo);
		makeExcel.setValue(1, 3, "專案融資種類", fontStyleVo);
		makeExcel.setValue(1, 4, "借款人戶名", fontStyleVo);
		makeExcel.setValue(1, 5, "借款人身份證字號", fontStyleVo);
		makeExcel.setValue(1, 6, "配偶戶名", fontStyleVo);
		makeExcel.setValue(1, 7, "配偶身份證字號", fontStyleVo);
		makeExcel.setValue(1, 8, "貸款期限", fontStyleVo);
		makeExcel.setValue(1, 9, "貸放利率", fontStyleVo);
		makeExcel.setValue(1, 10, "優惠貸款金額", fontStyleVo);
		makeExcel.setValue(1, 11, "撥款日期", fontStyleVo);
		makeExcel.setValue(1, 12, "屋址區域", fontStyleVo);
		makeExcel.setValue(1, 13, "售屋者戶名", fontStyleVo);
		makeExcel.setValue(1, 14, "售屋者身份證字號及營利事業編號", fontStyleVo);
		makeExcel.setValue(1, 15, "註記", fontStyleVo);

		
		makeExcel.setWidth(1, 16);
		makeExcel.setWidth(2, 12);
		makeExcel.setWidth(3, 17);
		makeExcel.setWidth(4, 14);
		makeExcel.setWidth(5, 23);
		makeExcel.setWidth(6, 12);
		makeExcel.setWidth(7, 19);
		makeExcel.setWidth(8, 12);
		makeExcel.setWidth(9, 12);
		makeExcel.setWidth(10, 17);
		makeExcel.setWidth(11, 12);
		makeExcel.setWidth(12, 12);
		makeExcel.setWidth(13, 14);
		makeExcel.setWidth(14, 42);
		makeExcel.setWidth(15, 9);
		
		int printRow = 2; // 從第二行開始印

		if (listL5801 == null || listL5801.isEmpty()) {

//			makeExcel.setValue(printRow, 1, "本月無資料", fontStyleVo);

		} else {

			for (Map<String, String> mapL5801 : listL5801) {

				// F0 戶號
				String custNo = FormatUtil.pad9(mapL5801.get("F0"), 7);
				// F1 額度
				String facmNo = FormatUtil.pad9(mapL5801.get("F1"), 3);

				makeExcel.setValue(printRow, 1, custNo + "-" + facmNo, fontStyleVo);

				// F2 商品代碼
				String ProdNo = mapL5801.get("F2");

				makeExcel.setValue(printRow, 2, ProdNo, fontStyleVo);

				// F3 專案融資種類
				String ProjectKind = mapL5801.get("F3");
				String Kind = "";
				switch (ProjectKind) {
				case "1":
					Kind = "一千二百億元青年優惠房屋貸款暨信用保證專案(不得轉貸、重購)";
					break;
				case "2":
					Kind = "四千億元優惠購屋專案貸款(不得轉貸、重購)";
					break;
				case "3":
					Kind = "續辦二千億元優惠購屋專案貸款(補貼 0.425%，不得轉貸、重購)";
					break;
				case "4":
					Kind = "續辦四千八百億元優惠購屋專案貸款(補貼 0.25%，不得轉貸、重購)";
					break;
				case "5":
					Kind = "續辦六千億元優惠購屋專案貸款(補貼 0.125%，不得轉貸、重購)";
					break;
				case "6":
					Kind = "增撥新台幣四千億元優惠購屋專案貸款(補貼 0.7%，不得重購)";
					break;
				}
				makeExcel.setValue(printRow, 3, Kind, fontStyleVo);

				// F4 借款人戶名
				String CustName = mapL5801.get("F4");

				makeExcel.setValue(printRow, 4, CustName, fontStyleVo);

				// F5 借款人身份證字號
				String CustId = mapL5801.get("F5");

				makeExcel.setValue(printRow, 5, CustId, fontStyleVo);

				// F6 配偶人戶名
				String FamilyName = mapL5801.get("F6");

				makeExcel.setValue(printRow, 6, FamilyName, fontStyleVo);

				// F7 配偶身份證字號
				String FamilyId = mapL5801.get("F7");

				makeExcel.setValue(printRow, 7, FamilyId, fontStyleVo);

				// F8 貸款期限
				String LoanTermYy = mapL5801.get("F8");

				makeExcel.setValue(printRow, 8, LoanTermYy, fontStyleVo);

				// F9 貸放利率
				String ApproveRate = mapL5801.get("F9");

				makeExcel.setValue(printRow, 9, ApproveRate, fontStyleVo);

				// F10 優惠貸款金額
				String DrawdownAmt = mapL5801.get("F10");

				makeExcel.setValue(printRow, 10, DrawdownAmt, fontStyleVo);

				// F11 撥款日期
				String FirstDrawdownDate = mapL5801.get("F11");
				FirstDrawdownDate = String.valueOf(Integer.valueOf(FirstDrawdownDate) - 19110000); // 轉民國年
				makeExcel.setValue(printRow, 11, FirstDrawdownDate, fontStyleVo);

				// F12 屋址區域
				String CityItem = mapL5801.get("F12");

				makeExcel.setValue(printRow, 12, CityItem, fontStyleVo);

				
				// F13 註記
				String SellerName = mapL5801.get("F13");

				makeExcel.setValue(printRow, 13, SellerName, fontStyleVo);
				// F14 註記
				String SellerId = mapL5801.get("F14");

				makeExcel.setValue(printRow, 14, SellerId, fontStyleVo);
				
				// F15 註記
				String Remark = mapL5801.get("F15");

				makeExcel.setValue(printRow, 15, Remark, fontStyleVo);
				printRow++;
			}
			// 畫框線
			makeExcel.setAddRengionBorder("A", 1, "O", printRow-1, 1);

		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

	}

}
