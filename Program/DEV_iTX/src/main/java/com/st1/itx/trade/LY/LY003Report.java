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

@Component("LY003Report")
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

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY003", "非RBC_表14-2_會計部年度檢查報表",
				"LY003-非RBC_表14-2_會計部年度檢查報表", "LY003_底稿_非RBC_表14-2_會計部年度檢查報表.xlsx", "表14-2");

		int rocYear = Integer.valueOf(titaVo.getParam("RocYear"));
		int rocMonth = 12;

		makeExcel.setValue(1, 1, "新光人壽保險股份有限公司 " + rocYear + "年度(" + rocMonth + ")報表");

		List<Map<String, String>> lY003List = null;

		boolean isNotEmpty = true;

		int endOfYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;

		try {
			int recordNo = 0;
			
			for (int f = 1; f <= 4; f++) {

				lY003List = lY003ServiceImpl.findAll(titaVo, f, endOfYearMonth);
			
				if (lY003List.size() == 0) {

					recordNo++;

					isNotEmpty = recordNo == 4 ? false : true;
				}
				
				exportExcel(lY003List, f);

			}
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

		// 最下方表格欄列用
		int bCol = 0;
		int bRow = 0;

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
			case 4:
				row = "A".equals(tLDVo.get("TYPE")) ? 25
						: "B".equals(tLDVo.get("TYPE")) ? 31
								: "C".equals(tLDVo.get("TYPE")) ? 37 : "D".equals(tLDVo.get("TYPE")) ? 43 : 49;

				// 最下方表格用
				bCol = "A".equals(tLDVo.get("TYPE")) ? 3
						: "B".equals(tLDVo.get("TYPE")) ? 4
								: "C".equals(tLDVo.get("TYPE")) ? 5 : "D".equals(tLDVo.get("TYPE")) ? 6 : 7;
				break;
			default:
				break;
			}

//			evaAmt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
			lineAmt = tLDVo.get("LineAmt").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("LineAmt"));
			loanAmt = tLDVo.get("LoanBalance").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("LoanBalance"));

			if (formNum == 4) {
				int assetClass = tLDVo.get("AssetClass").isEmpty() ? 0 : Integer.valueOf(tLDVo.get("AssetClass"));
				switch (assetClass) {
				case 1:
					row = row + 1;

					bRow = 83;// 最下方表格用
					break;
				case 2:
					row = row + 2;
					bRow = 84;// 最下方表格用
					break;
				case 3:
					row = row + 3;
					bRow = 85;// 最下方表格用
					break;
				case 4:
					row = row + 4;
					bRow = 86;// 最下方表格用
					break;
				case 5:
					row = row + 5;
					bRow = 87;// 最下方表格用
					break;
				default:
					break;
				}
				// 最下方表格
				makeExcel.setValue(bRow, bCol, loanAmt, "#,##0");

			}
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

}
