package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM030ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM030Report extends MakeReport {

	@Autowired
	LM030ServiceImpl lM030ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public BaTxCom baTxCom;

	public Boolean exec(TitaVo titaVo, TxBuffer txBuffer) throws LogicException {

		baTxCom.setTxBuffer(txBuffer);

		List<Map<String, String>> listLM030 = null;

		try {
			listLM030 = lM030ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, listLM030);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM030ServiceImpl.testExcel error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM030) throws LogicException {
		this.info("LM030Report exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM030";
		String fileItem = "轉催收案件明細";
		String fileName = "LM030轉催收案件明細";
		String defaultExcel = "LM030_底稿_轉催收案件明細_核定總表.xlsx";
		String defaultSheet = "YYYMM";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		int yy = Integer.valueOf(titaVo.getParam("YearMonth")) / 100;
		int mm = Integer.valueOf(titaVo.getParam("YearMonth")) % 100;

		int nextYY = 0;
		int nextMM = 0;

		if (mm == 12) {
			mm = 1;
			yy = yy + 1;

		} else {
			mm = mm + 1;
		}

		if (mm == 12) {
			nextMM = 2;
			nextYY = yy + 1;
		} else if (mm == 11) {
			nextMM = 1;
			nextYY = yy + 1;
		} else {
			nextMM = mm + 1;
			nextYY = yy;
		}

		int nextDueDate = nextYY * 10000 + nextMM * 100 + 1;

		this.info("nextDueDate = " + nextDueDate);

		int rocYM = yy * 100 + mm;

		makeExcel.setSheet("YYYMM", rocYM + "");
		makeExcel.setValue(1, 1, yy + "年 " + mm + " 月轉催收明細表");

		int row = 3;

		if (listLM030 == null || listLM030.isEmpty()) {
			makeExcel.setValue(3, 1, "本日無資料");
		} else {

			int custNoCnt = 0;
			int tmpCustNo = 0;
			for (Map<String, String> r : listLM030) {

				int custNo = parse.stringToInteger(r.get("CustNo"));
				int facmNo = parse.stringToInteger(r.get("FacmNo"));
				// 一樣表示是同一擔保品戶號
				if (tmpCustNo == custNo) {
					custNoCnt++;
				}

				if (tmpCustNo != custNo) {
					tmpCustNo = custNo;
					custNoCnt = 1;
				}

				BigDecimal ovduIntAmt = BigDecimal.ZERO; // 轉催收利息

				try {
					baTxCom.settingUnPaid(nextDueDate, custNo, facmNo, 0, 3, BigDecimal.ZERO, titaVo); // 3-結案
				} catch (LogicException e) {
					this.info("ErrorMsg :" + e.getErrorMsg(titaVo) + " " + custNo + "-" + facmNo);
				}
				// 轉催收利息 = 短繳利息 + 利息
				ovduIntAmt = baTxCom.getInterest().add(baTxCom.getShortfallInterest());

				// 地區別
				makeExcel.setValue(row, 1, r.get("CityItem"), "C");
				// 催收人員
				makeExcel.setValue(row, 2, r.get("AccCollPsn"), "C");
				// 戶號
				makeExcel.setValue(row, 3, parse.stringToInteger(r.get("CustNo")), "C");
				// 額度
				makeExcel.setValue(row, 4, parse.stringToInteger(r.get("FacmNo")), "C");
				// 戶名
				makeExcel.setValue(row, 5, r.get("CustName"), "C");
				// 初貸日
				makeExcel.setValue(row, 6, parse.stringToInteger(r.get("DrawdownDate")), "R");
				// 本金餘額
				makeExcel.setValue(row, 7, getBigDecimal(r.get("LoanBal")), "#,##0", "R");
				// 利息
				makeExcel.setValue(row, 8, ovduIntAmt, "#,##0", "R");
				// 利率
				makeExcel.setValue(row, 9, getBigDecimal(r.get("StoreRate")), "0.0000", "R");
				// 到期日期
				makeExcel.setValue(row, 10, parse.stringToInteger(r.get("MaturityDate")), "R");
				// 繳息迄日
				makeExcel.setValue(row, 11, parse.stringToInteger(r.get("PrevPayIntDate")), "R");
				// 最近應繳日
				makeExcel.setValue(row, 12, baTxCom.getRepayIntDate(), "R");
				// 預計轉催日
				makeExcel.setValue(row, 13, parse.stringToInteger(r.get("NextOvduDate")), "R");

				// 備註
				if (custNoCnt > 1) {
					makeExcel.setValue(row, 14, "同一擔保品", "C");
				}

				row++;
			} // for
		}

		row += 10;

		// block 1
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 1, "放款管理課", "C");
		// block 2
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 6, "放款服務課", "C");
		// block 3
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 10, "部室主管", "C");

		makeExcel.close();
	}

}
