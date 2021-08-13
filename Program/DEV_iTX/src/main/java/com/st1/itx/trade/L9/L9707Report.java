package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9707ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9707Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L9707Report.class);

	@Autowired
	L9707ServiceImpl l9707ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	BaTxCom dBaTxCom;

	@Autowired
	Parse parse;

	public void exec(TitaVo titaVo, TxBuffer txbuffer) throws LogicException {
		String ACCTDATE_ST = titaVo.getParam("ACCTDATE_ST");
		String ACCTDATE_ED = titaVo.getParam("ACCTDATE_ED");
		try {

			List<Map<String, String>> L9707List = l9707ServiceImpl.findAll(titaVo, ACCTDATE_ST, ACCTDATE_ED);
			
			testExcel(titaVo, L9707List, txbuffer);
			
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9707ServiceImpl.LoanBorTx error = " + errors.toString());
		}
	}

	private void testExcel(TitaVo titaVo, List<Map<String, String>> LDList, TxBuffer txbuffer) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L9707", "新增逾放案件明細", "L9707-新增逾放案件明細",
				"新增逾放案件明細.xlsx", "工作表1");
		// F0 申請日期
		// F1 准駁日期
		// F2 借款人戶號
		// F3 額度編號
		// F4 核准額度
		// F5 累計撥款金額
		// F6 已動用額度餘額
		// F7 入帳日期(要除100)

		// (F7-F8 > 90 = 逾期天數)

		// F8 應繳日期
		// F9 應繳息日
		// F10 UnpaidAmt
		// F11 企金別
		// F12 計件代碼
		// F13 T1

		/*
		 * 1. T1 = 1 要CALL BaTxCom.settingUnPaid 計算應繳金額=
		 * 
		 * payIntDate 應繳息日、應繳日 <= tbsdy && BaTxVo.dataKind = 2.本金利息 { Sum by 應繳息日
		 * Principal // 本金 Interest // 利息 DelayInt // 延滯息 BreachAmt // 違約金
		 */


		if (LDList != null && LDList.size() != 0) {
			int row = 3;
			BigDecimal totalDataCount = BigDecimal.ZERO;
			BigDecimal totalLoanAmt = BigDecimal.ZERO;
			for (Map<String, String> tLDVo : LDList) {
				String ad = "";
				int col = 0;
				for (int i = 0; i < tLDVo.size(); i++) {
					ad = "F" + String.valueOf(col);
					col++;
					switch(i) {
					case 2:
						//資料筆數
						totalDataCount = totalDataCount.add(new BigDecimal(1));
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? "0" : tLDVo.get(ad), "R");
						break;
					case 5:
					        //貸出金額
						totalLoanAmt = totalLoanAmt.add(new BigDecimal(tLDVo.get(ad) == "" ? "0" : tLDVo.get(ad)));
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? "0" : tLDVo.get(ad), "R");
						break;
					case 11:
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)), "R");
						break;
					case 14:
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? "0" : tLDVo.get(ad), "L");
						break;
					default:
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? "0" : tLDVo.get(ad), "R");
						break;
					}
				}
				row++;
			}

			// output totals
			makeExcel.setValue(1, 3, formatAmt(totalDataCount, 0), "R");
			makeExcel.setValue(1, 6, formatAmt(totalLoanAmt, 1), "R");
	

		} else {
			makeExcel.setValue(3, 1, "無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	public int calculateTimeDifferenceBySimpleDateFormat(String date1, String date2) throws ParseException {

		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

		/* 天數差 */

		Date fromDate1 = simpleFormat
				.parse(date1.substring(0, 4) + "-" + date1.substring(4, 6) + "-" + date1.substring(6, 8) + " 00:00");

		Date toDate1 = simpleFormat
				.parse(date2.substring(0, 4) + "-" + date2.substring(4, 6) + "-" + date2.substring(6, 8) + " 00:00");

		long from1 = fromDate1.getTime();

		long to1 = toDate1.getTime();

		int days = (int) ((to1 - from1) / (1000 * 60 * 60 * 24));

		return days;
	}
}
