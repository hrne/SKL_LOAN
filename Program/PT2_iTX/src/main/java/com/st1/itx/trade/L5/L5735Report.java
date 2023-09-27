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

		case "L5735K":
			resultList = null;

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

}
