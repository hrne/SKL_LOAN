package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(L5801Report3.class);

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

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5801", "補貼息核撥清單工作檔", "L5801補貼息核撥清單工作檔" + thisMonth, "補貼息核撥清單工作檔"+ thisMonth);
		
		fontStyleVo = new ExcelFontStyleVo();

		fontStyleVo.setFont((short) 1); // 字體 : 標楷體

		fontStyleVo.setSize((short) 12); // 字體大小 : 12

		makeExcel.setValue(1, 1, "專案融資種類", fontStyleVo);
		makeExcel.setValue(1, 2, "補貼息", fontStyleVo);
		makeExcel.setValue(1, 3, "上月貸款餘額", fontStyleVo);
		makeExcel.setValue(1, 4, "本月貸出數", fontStyleVo);
		makeExcel.setValue(1, 5, "本月收回數", fontStyleVo);
		makeExcel.setValue(1, 6, "屆期不再申撥補貼息", fontStyleVo);
		makeExcel.setValue(1, 7, "本月貸款餘額", fontStyleVo);
		
		int printRow = 2; // 從第二行開始印

		if (listL5801 == null || listL5801.isEmpty()) {

//			makeExcel.setValue(printRow, 1, "本月無資料", fontStyleVo);

		} else {


			for (Map<String, String> mapL5801 : listL5801) {


				// F0 專案融資種類
				String ProjectKind = mapL5801.get("F0");
				
				makeExcel.setValue(printRow, 1, ProjectKind, fontStyleVo);
				
				// F1 補貼息
				String Money = mapL5801.get("F1");

				makeExcel.setValue(printRow, 2, Money, fontStyleVo);

				// F2 上月貸款餘額
				String LastMonthBal = mapL5801.get("F2");

				makeExcel.setValue(printRow, 3, LastMonthBal, fontStyleVo);

				// F3 本月貸出數	
				String OpenAmount = mapL5801.get("F3");

				makeExcel.setValue(printRow, 3, OpenAmount, fontStyleVo);

				// F4 本月收回數
				String CloseAmount = mapL5801.get("F4");

				makeExcel.setValue(printRow, 4, CloseAmount, fontStyleVo);

				// F5 屆期不再申撥補貼息
				String MaturityAmount = mapL5801.get("F5");

				makeExcel.setValue(printRow, 5, MaturityAmount, fontStyleVo);

				// F6 本月貸款餘額
				String ThisMonthBal = mapL5801.get("F6");

				makeExcel.setValue(printRow, 6, ThisMonthBal, fontStyleVo);

				
			}
		// 畫框線
		makeExcel.setAddRengionBorder("A", 1, "P", printRow, 1);

	  }

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
