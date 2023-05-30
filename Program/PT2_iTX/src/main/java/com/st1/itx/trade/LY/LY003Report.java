package com.st1.itx.trade.LY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LY003ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class LY003Report extends MakeReport {

	@Autowired
	public LY003ServiceImpl lY003ServiceImpl;

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
		String fileItem = "非RBC_表14-2_會計部年度檢查報表";
		String fileName = "LY003-非RBC_表14-2_會計部年度檢查報表";
		String defaultExcel = "LY003_底稿_非RBC_表14-2_會計部年度檢查報表.xlsx";
		String defaultSheet = "表14-2";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		int rocYear = Integer.valueOf(titaVo.getParam("RocYear"));
		int rocMonth = 12;

		makeExcel.setValue(1, 1, "新光人壽保險股份有限公司 " + rocYear + "年度(" + rocMonth + ")報表");

		List<Map<String, String>> lY003List = null;

		boolean isNotEmpty = true;

		int endOfYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;

		try {
			int recordNo = 0;

			for (int f = 1; f <= 3; f++) {

				lY003List = lY003ServiceImpl.findAll(titaVo, f, endOfYearMonth);

				if (lY003List.size() == 0) {

					recordNo++;

					isNotEmpty = recordNo == 3 ? false : true;
				}

				exportExcel(lY003List, f);

			}

			lY003List = lY003ServiceImpl.findAll2(titaVo, endOfYearMonth);
			reportExcel14_2(lY003List);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY003ServiceImpl.findAll error = " + errors.toString());
		}

		makeExcel.close();

		return isNotEmpty;

	}

	private void exportExcel(List<Map<String, String>> LDList, int formNum) throws LogicException {

		int row = 0;

		// 估計總值為人工
//		BigDecimal evaAmt = BigDecimal.ZERO;
		BigDecimal lineAmt = BigDecimal.ZERO;
		BigDecimal loanAmt = BigDecimal.ZERO;

		for (Map<String, String> tLDVo : LDList) {

			switch (formNum) {
			case 1:
				row = "A".equals(tLDVo.get("TYPE")) ? 6
						: "B".equals(tLDVo.get("TYPE")) ? 7
								: "C".equals(tLDVo.get("TYPE")) ? 8 : "D".equals(tLDVo.get("TYPE")) ? 9 : 10;
				break;
			case 2:
				row = "A".equals(tLDVo.get("TYPE")) ? 14
						: "B".equals(tLDVo.get("TYPE")) ? 15 : "C".equals(tLDVo.get("TYPE")) ? 16 : 17;
				break;
			case 3:
				row = "A".equals(tLDVo.get("TYPE")) ? 19
						: "B".equals(tLDVo.get("TYPE")) ? 20 : "C".equals(tLDVo.get("TYPE")) ? 21 : 22;
				break;

			default:
				break;
			}

//			evaAmt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
			lineAmt = tLDVo.get("LineAmt").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("LineAmt"));
			loanAmt = tLDVo.get("LoanBalance").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("LoanBalance"));

//			makeExcel.setValue(row, 4, evaAmt, "#,##0");
			makeExcel.setValue(row, 5, lineAmt, "#,##0");
			makeExcel.setValue(row, 8, loanAmt, "#,##0");
		}

		// 重整公式 上方表格
		for (int x = 4; x <= 10; x++) {
			makeExcel.formulaCalculate(13, x);
			makeExcel.formulaCalculate(18, x);
			makeExcel.formulaCalculate(23, x);
			makeExcel.formulaCalculate(25, x);
			makeExcel.formulaCalculate(31, x);
			makeExcel.formulaCalculate(37, x);
			makeExcel.formulaCalculate(43, x);
			makeExcel.formulaCalculate(49, x);
			makeExcel.formulaCalculate(55, x);
			makeExcel.formulaCalculate(67, x);
			makeExcel.formulaCalculate(68, x);
			makeExcel.formulaCalculate(69, x);
		}
		// 重整公式 上方表格
		for (int y = 6; y <= 69; y++) {
			makeExcel.formulaCalculate(y, 6);
			makeExcel.formulaCalculate(y, 9);
			makeExcel.formulaCalculate(y, 10);
		}
		// 重整公式 上方表格
		for (int y = 70; y <= 71; y++) {
//			makeExcel.formulaCalculate(y, 4);
		}

		// 重整公式 下方表格

		for (int y = 3; y <= 9; y++) {
			makeExcel.formulaCalculate(y, 83);
			makeExcel.formulaCalculate(y, 84);
			makeExcel.formulaCalculate(y, 85);
			makeExcel.formulaCalculate(y, 86);
			makeExcel.formulaCalculate(y, 87);
		}

		for (int x = 3; x <= 10; x++) {
			makeExcel.formulaCalculate(88, x);
		}
		for (int y = 83; y <= 88; y++) {
			makeExcel.formulaCalculate(y, 10);
		}

		// 暫缺 列68 列69 的值 (從LM054 55找)
		// 缺壽險貸款
		// 擔保品 壽險貸款
		// 缺業主權益 資金總額 上年度業主權益

	}

	/**
	 * 報表輸出 表14-2
	 * 
	 * @param listData
	 */
	private void reportExcel14_2(List<Map<String, String>> listData) throws LogicException {

		this.info("LY003report.reportExcel14_2");

		makeExcel.setSheet("表14-2");

		int row = 0;
		int col = 0;
		BigDecimal tempAmt = BigDecimal.ZERO;

		for (Map<String, String> lY003Vo : listData) {

			// 金額
			tempAmt = "0".equals(lY003Vo.get("F1")) ? BigDecimal.ZERO : new BigDecimal(lY003Vo.get("F1"));

			// H37 放款總計
			// D40 甲類逾期放款金額
			// D41 乙類逾期放款金額
			// D44 逾期放款比率%
			if ("B".equals(lY003Vo.get("F0"))) {

				row = 70;
				col = 4;

			} else if ("C".equals(lY003Vo.get("F0"))) {

				row = 71;
				col = 4;

			} else if ("COLLECTION".equals(lY003Vo.get("F0"))) {
//				return;
				row = 68;
				col = 8;

			} else if ("TOTAL".equals(lY003Vo.get("F0"))) {
				return;
//				row = 37;
//				col = 8;

			}

			makeExcel.setValue(row, col, tempAmt, "#,##0", "R");

		}
		// 重整
		// 甲類逾期放款比率%(含壽險保單質押放款)
		makeExcel.formulaCaculate(72, 4);
		// 乙類逾期放款比率%(含壽險保單質押放款)
		makeExcel.formulaCaculate(73, 4);
		// 逾期放款比率%(含壽險保單質押放款)
		makeExcel.formulaCaculate(74, 4);
		// 逾期放款比率%(不含壽險保單質押放款)
		makeExcel.formulaCaculate(75, 4);
		// 逾期放款比率%(不含壽險保單質押放款)
		makeExcel.formulaCaculate(76, 4);
		// 逾期放款比率%(不含壽險保單質押放款)
		makeExcel.formulaCaculate(77, 4);
	}

}
