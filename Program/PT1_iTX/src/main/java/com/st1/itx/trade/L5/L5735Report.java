package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L5735ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

/**
 * L5735 建商餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L5735Report extends MakeReport {

	@Autowired
	L5735ServiceImpl l5735ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	public void exec(String subTxCD, String txName, TitaVo titaVo) throws LogicException {
		this.info("exec ... ");

		// 取得輸入值
		int inputDrawdownDate = parse.stringToInteger(titaVo.getParam("DrawdownDate"));

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
//		L5735A-建商餘額明細
//		L5735B-首購餘額明細
//		L5735D-工業區土地抵押餘額明細
//		L5735E-正常戶餘額明細
//		L5735G-住宅貸款餘額明細
//		L5735I-補助貸款餘額明細
//		L5735J-政府優惠貸款餘額明細
//		L5735K-保險業投資不動產及放款情形
		switch (subTxCD) {
		case "L5735A":
			resultList = l5735ServiceImpl.getConstructionCompanyLoanData(inputDrawdownDate, titaVo);
			break;
		case "L5735D":
			resultList = l5735ServiceImpl.getNormalCustomerLoanDataL5735D(inputDrawdownDate, titaVo);
			break;
		case "L5735K":// 此表格式與其他不同，需另外寫
			resultList = l5735ServiceImpl.getL5735K(inputDrawdownDate, titaVo);
			exportL5735K(resultList, inputDrawdownDate, subTxCD, txName, titaVo);
			return;
		default:
			resultList = l5735ServiceImpl.getNormalCustomerLoanData(inputDrawdownDate, subTxCD, titaVo);
			break;
		}

		// open excel
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = titaVo.getTxcd();
		String fileItem = txName;
		String fileName = subTxCD + "_" + txName;
		String defaultExcel = subTxCD + "_底稿_公會報送-" + txName + ".xlsx";
		this.info("defaultExcel = " + defaultExcel);
		String defaultSheet = "yyymmdd";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		makeExcel.setSheet(defaultSheet, titaVo.getParam("DrawdownDate"));

		int row = 2;

		int tmpCol = 0;

		// L5735D-工業區土地抵押餘額明細 會多一欄擔保品代碼2，需移動欄位
		if ("L5735D".equals(subTxCD)) {
			tmpCol = 1;
		}

		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(row, 1, "本日無資料", "R");

		} else {

			for (Map<String, String> result : resultList) {

				makeExcel.setValue(row, 1, result.get("CustName"), "L");
				makeExcel.setValue(row, 2, parse.stringToInteger(result.get("CustNo")));
				makeExcel.setValue(row, 3, parse.stringToInteger(result.get("FacmNo")));
				makeExcel.setValue(row, 4, parse.stringToInteger(result.get("BormNo")));
				makeExcel.setValue(row, 5, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###");
				makeExcel.setValue(row, 6, parse.stringToBigDecimal(result.get("LineAmt")), "#,###");
				makeExcel.setValue(row, 7, this.showBcDate(result.get("PrevPayIntDate"), 0), "C");
				makeExcel.setValue(row, 8, parse.stringToInteger(result.get("ClCode1")));

				// L5735D-工業區土地抵押餘額明細 會多一欄擔保品代碼2，需移動欄位
				if ("L5735D".equals(subTxCD)) {
					makeExcel.setValue(row, 9, parse.stringToInteger(result.get("ClCode2")));
				}

				makeExcel.setValue(row, 9 + tmpCol, parse.stringToInteger(result.get("UsageCode")));
				makeExcel.setValue(row, 10 + tmpCol, parse.stringToInteger(result.get("AcctCode")));
				makeExcel.setValue(row, 11 + tmpCol, result.get("ProdNo"), "L");
				makeExcel.setValue(row, 12 + tmpCol, parse.stringToInteger(result.get("Status")));
				makeExcel.setValue(row, 13 + tmpCol, this.showBcDate(result.get("DrawdownDate"), 0), "C");
				makeExcel.setValue(row, 14 + tmpCol, parse.stringToBigDecimal(result.get("EvaNetWorth")), "#,##0");
				makeExcel.setValue(row, 15 + tmpCol, parse.stringToBigDecimal(result.get("LoanRatio")), "0%");

				row++;
			}
		}

		makeExcel.close();

	}

	private void exportL5735K(List<Map<String, String>> resultList, int inputDrawdownDate, String subTxCD,
			String txName, TitaVo titaVo) throws LogicException {

		// open excel
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = titaVo.getTxcd();
		String fileItem = txName;
		String fileName = subTxCD + "_" + txName;
		String defaultExcel = subTxCD + "_底稿_公會報送-" + txName + ".xlsx";
		this.info("defaultExcel = " + defaultExcel);
		String defaultSheet = "不動產擔保放款";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		String rocYM = this.showRocDate(inputDrawdownDate, 5);

		makeExcel.setValue(1, 2, txName + "(" + rocYM + "底)");

		if (resultList == null || resultList.isEmpty() || resultList.size() == 2) {
			makeExcel.setValue(1, 1, "本日無資料", "R");

		} else {
			int col = 0;

			String availableFundsAcDate = "";
			for (Map<String, String> result : resultList) {
				String clcode1 = result.get("ClCode1");
				String usageCode = result.get("UsageCode");

				// 用途別02 購置不動產
				if ("02".equals(usageCode)) {
					col = 5;

					// 土地建物
					if ("1".equals(clcode1)) {
						makeExcel.setValue(8, col, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###", "R");
						makeExcel.setValue(9, col, parse.stringToBigDecimal(result.get("LineAmt")), "#,###", "R");
						makeExcel.setValue(12, col, parse.stringToBigDecimal(result.get("AllNetWorth")), "#,###", "R");
					}

					// 土地
					if ("2".equals(clcode1)) {
						makeExcel.setValue(10, col, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###", "R");
						makeExcel.setValue(11, col, parse.stringToBigDecimal(result.get("LineAmt")), "#,###", "R");
						makeExcel.setValue(13, col, parse.stringToBigDecimal(result.get("AllNetWorth")), "#,###", "R");
					}

				}

				// 用途別01 週轉金
				if ("01".equals(usageCode)) {
					col = 9;

					// 土地建物
					if ("1".equals(clcode1)) {
						makeExcel.setValue(8, col, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###", "R");
						makeExcel.setValue(9, col, parse.stringToBigDecimal(result.get("LineAmt")), "#,###", "R");
						makeExcel.setValue(12, col, parse.stringToBigDecimal(result.get("AllNetWorth")), "#,###", "R");
					}

					// 土地
					if ("2".equals(clcode1)) {
						makeExcel.setValue(10, col, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###", "R");
						makeExcel.setValue(11, col, parse.stringToBigDecimal(result.get("LineAmt")), "#,###", "R");
						makeExcel.setValue(13, col, parse.stringToBigDecimal(result.get("AllNetWorth")), "#,###", "R");
					}

				}

				// 用途別 其它 (UsageCode=99 ClCode1=99)
				if ("99".equals(usageCode) && "99".equals(clcode1)) {
					col = 12;

					makeExcel.setValue(8, col, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###", "R");
					makeExcel.setValue(9, col, parse.stringToBigDecimal(result.get("LineAmt")), "#,###", "R");
					makeExcel.setValue(12, col, parse.stringToBigDecimal(result.get("AllNetWorth")), "#,###", "R");

				}

				// 用途別全部：押品為非土地及土地建物(UsageCode=1 ClCode1=3)
				if ("01".equals(usageCode) && "3".equals(clcode1)) {
					col = 16;

					makeExcel.setValue(8, col, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###", "R");
					makeExcel.setValue(9, col, parse.stringToBigDecimal(result.get("LineAmt")), "#,###", "R");

				}

				// 建商餘額貸款(P欄)(UsageCode=12 ClCode1=12)
				if ("12".equals(usageCode) && "12".equals(clcode1)) {
					col = 16;
					makeExcel.setValue(5, col, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###", "R");

				}

				// 可運用資金(P欄)(UsageCode=11 ClCode1=11)
				if ("11".equals(usageCode) && "11".equals(clcode1)) {
					col = 16;
					makeExcel.setValue(4, col, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###", "R");

					// 可運用資金的會計日期 用 CustNoCount欄位取代
					availableFundsAcDate = result.get("CustNoCount");

				}

			} // for

			for (int c = 2; c <= 14; c++) {
				makeExcel.formulaCaculate(6, c);
			}

			availableFundsAcDate = this.showRocDate(availableFundsAcDate, 0);

			String memo = "備註：1.放款於建商之金額已包含於住宅抵押貸款大類及以不動產為擔保品之其他擔保放款大類。\r\n";
			memo += "      2.可運用資金總額係" + availableFundsAcDate + "會計師核閱數。";
			makeExcel.setValue(7, 1, memo);

		} // if

		makeExcel.close();
	}

}
