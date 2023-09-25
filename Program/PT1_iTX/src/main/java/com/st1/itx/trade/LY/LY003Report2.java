package com.st1.itx.trade.LY;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM055AssetLoss;
import com.st1.itx.db.service.MonthlyLM055AssetLossService;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class LY003Report2 extends MakeReport {

	@Autowired
	public MonthlyLM055AssetLossService sLM055AssetLossService;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		this.info("LY003.exportExcel active");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LY003";
		String fileItem = "A142放款餘額彙總表";
		String fileName = "LY003-A142放款餘額彙總表";
		String defaultExcel = "LY003_底稿_A142放款餘額彙總表.xlsx";
		String defaultSheet = "A142放款餘額彙總表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		int rocYear = Integer.valueOf(titaVo.getParam("RocYear"));
		int rocMonth = 12;

		makeExcel.setValue(2, 3, (rocYear + 1911) * 100 + rocMonth);

		boolean isNotEmpty = true;

		int endOfYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;

		Slice<MonthlyLM055AssetLoss> slMonthlyLM055AssetLoss = sLM055AssetLossService.findYearMonthAll(endOfYearMonth,
				0, Integer.MAX_VALUE, titaVo);
		if (slMonthlyLM055AssetLoss == null) {
			throw new LogicException(titaVo, "E0015", "需先執行 L7205-五類資產分類上傳轉檔作業 "); // 檢查錯誤
		}
		reportExcelA142(slMonthlyLM055AssetLoss.getContent());

		makeExcel.close();

		return isNotEmpty;

	}

	private void reportExcelA142(List<MonthlyLM055AssetLoss> listData) throws LogicException {

		this.info("reportExcelA142 ");

		makeExcel.setSheet("A142放款餘額彙總表");

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

	}

}
