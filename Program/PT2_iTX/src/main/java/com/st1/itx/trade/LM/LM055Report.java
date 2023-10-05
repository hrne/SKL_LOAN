package com.st1.itx.trade.LM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM055AssetLoss;
import com.st1.itx.db.service.MonthlyLM055AssetLossService;
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
	public MonthlyLM055AssetLossService sLM055AssetLossService;

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
		Slice<MonthlyLM055AssetLoss> slMonthlyLM055AssetLoss = sLM055AssetLossService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (slMonthlyLM055AssetLoss == null) {
			throw new LogicException(titaVo, "E0015", "需先執行 L7205-五類資產分類上傳轉檔作業 "); // 檢查錯誤
		}
		exportExcel(slMonthlyLM055AssetLoss.getContent());

		makeExcel.close();

	}

	private void exportExcel(List<MonthlyLM055AssetLoss> listData) throws LogicException {

		int row = 7;

		for (MonthlyLM055AssetLoss r : listData) {
			row++;
			makeExcel.setValue(row, 6, r.getOverdueAmount(), "#,##0", "R");// 逾期放款
			makeExcel.setValue(row, 7, r.getObserveAmount(), "#,##0", "R");// 未列入逾期應予評估放款
			makeExcel.setValue(row, 8, r.getNormalAmount(), "#,##0", "R");// 正常放款
			makeExcel.setValue(row, 9, r.getLoanAmountClass2(), "#,##0", "R");// 放款金額備呆金額五分類2
			makeExcel.setValue(row, 10, r.getLoanAmountClass3(), "#,##0", "R");// 放款金額備呆金額五分類3
			makeExcel.setValue(row, 11, r.getLoanAmountClass4(), "#,##0", "R");// 放款金額備呆金額五分類4
			makeExcel.setValue(row, 12, r.getLoanAmountClass5(), "#,##0", "R");// 放款金額備呆金額五分類5
			makeExcel.setValue(row, 13, r.getReserveLossAmt1(), "#,##0", "R");// 備呆金額五分類1
			makeExcel.setValue(row, 14, r.getReserveLossAmt2(), "#,##0", "R");// 備呆金額五分類2
			makeExcel.setValue(row, 15, r.getReserveLossAmt3(), "#,##0", "R");// 備呆金額五分類3
			makeExcel.setValue(row, 16, r.getReserveLossAmt4(), "#,##0", "R");// 備呆金額五分類4
			makeExcel.setValue(row, 17, r.getReserveLossAmt5(), "#,##0", "R");// 備呆金額五分類5
			makeExcel.setValue(row, 18, r.getIFRS9AdjustAmt(), "#,##0", "R");// IFRS9增提金額(含應收利息)
		}

		// 重整合計公式F15~R15
		makeExcel.formulaRangeCalculate(15, 15, 6, 18);

	}

}