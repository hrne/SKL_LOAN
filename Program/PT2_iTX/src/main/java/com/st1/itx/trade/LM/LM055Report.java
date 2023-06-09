package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM055ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Service
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM055Report extends MakeReport {

	@Autowired
	LM055ServiceImpl lM055ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	/*
	 * 用LM051的表 去分別做擔保品類別與： 1.逾期數的餘額表 2.資產五分類的餘額表
	 * 
	 * 放款種類： A.銀行保證放款 B.動產擔保放款 C.不動產抵押放款 D.有價證券質押放款 E.壽險貸款 F墊繳保費 Z.其他。
	 */

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @throws LogicException
	 * 
	 */
	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {
		this.info("LM055Report exec");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		// LM054
		String txcd = "LM055";
		// 檔案名稱
		String rptItem = "A042放款餘額彙總表";
		// 輸出檔名
		String fileName = "LM055-A042放款餘額彙總表_工作表";
		// 底稿名稱
		String defaultName = "LM055_底稿_A042放款餘額彙總表.xlsx";
		// 底稿工作表名
		String defaultSheetName = "A042放款餘額彙總表";

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI()).setRptCode(txcd)
				.setRptItem(rptItem).build();

		makeExcel.open(titaVo, reportVo, fileName, defaultName, defaultSheetName);

		makeExcel.setValue(2, 3, yearMonth);

		try {

			fnAllList = lM055ServiceImpl.findAll(titaVo, yearMonth);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM055ServiceImpl.findAll error = " + errors.toString());

		}
		exportExcel(fnAllList);

		makeExcel.close();

	}

	private void exportExcel(List<Map<String, String>> listData) throws LogicException {

		int col = 0;
		int row = 0;

		String type = "";
		int kind = 0;
		BigDecimal amount = BigDecimal.ZERO;

		makeExcel.setValue(12, 2, "C", "C");
		
		for (Map<String, String> r : listData) {

			// 會null是因為MonthlyFacBal的AssetClass沒有更新到處於null狀態，需上傳L7205(五類資產分類上傳轉檔作業)
			if (r.get("F2") == null) {
				continue;
			}

			type = r.get("F0");
			kind = Integer.valueOf(r.get("F1"));
			amount = new BigDecimal(r.get("F2"));
			this.info("type=" + type);
			this.info("kind=" + kind);
			this.info("amount=" + amount);
			switch (type) {
			case "A":
				row = 8;
				break;
			case "B":
				row = 9;
				break;
			case "C":
				row = 10;
				break;
			case "D":
				row = 11;
				break;
			case "Z":
				row = 12;
				break;
			case "ZZ":
				row = 13;
				break;
			default:
				break;
			}

			col = kind + 5;

			makeExcel.setValue(row, col, amount, "#,##0");
		}

	}

}