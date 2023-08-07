package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM002ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM002Report extends MakeReport {

	@Autowired
	LM002ServiceImpl lM002ServiceImpl;

	@Autowired
	Parse parse;

	@Override
	public void printHeader() {
		int leftPos = 1;
//		int centerPos = 70;
		int rightPos = 120;

		this.setFont(2, 16);
		this.print(-2, 45, "房 貸 專 案 放 款", "C");

		this.setFont(2, 10);
		this.print(-2, 1, "　 程式 ID：" + this.getParentTranCode());
		this.print(-3, leftPos, "　 報　 表：" + this.getRptCode());
		this.print(-2, rightPos, "機密等級：" + this.getSecurity());
		this.print(-3, rightPos, "日　期：" + dDateUtil.getNowStringBc().substring(4, 6) + "/"
				+ dDateUtil.getNowStringBc().substring(6, 8) + "/" + dDateUtil.getNowStringBc().substring(2, 4));
		this.print(-4, rightPos, "單　位：元");

		this.setFont(2, 8);
		this.setBeginRow(6);
		this.setMaxRows(60);
	}

	@Override
	public void printTitle() {
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LM002Report exec ...");
		exportExcel(titaVo);

	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> fnAllList = new ArrayList<>();

		// 民國年
		int rocYY = parse.stringToInteger(titaVo.get("ENTDY").substring(0, 4));

		this.info("reportVo open");

		String brno = titaVo.getBrno();
		int rptDate = titaVo.getEntDyI();
		String rptCode = "LM002";
		String rptName = "專案放款";
		;
		String rptSize = "A4";
		String security = "";
		String pageOrien = "L";

		ReportVo reportVo = ReportVo.builder().setBrno(brno).setRptDate(rptDate).setRptCode(rptCode).setRptItem(rptName)
				.setRptSize(rptSize).setSecurity(security).setPageOrientation(pageOrien).build();

		this.open(titaVo, reportVo);

		try {
			fnAllList = lM002ServiceImpl.doQuery(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM002ServiceImpl.findAll error = " + errors.toString());
		}

		// 畫框(三個區塊)
		fw(1, rocYY - 2);
		fw(1, rocYY - 1);
		fw(1, rocYY);

		// 儲存合計
		int[] thisTotalLoan = new int[12];
		int[] lastLoan = new int[12];
		int[] lastTwoLoan = new int[12];

		if (fnAllList.size() > 0) {

			// 欄位名稱 Year、DataType、Month、LoanSum
			for (Map<String, String> r : fnAllList) {

				int year = parse.stringToInteger(r.get("Year"));
				int month = parse.stringToInteger(r.get("Month"));
				int dataType = parse.stringToInteger(r.get("DataType"));
				int loanBal = parse.stringToInteger(r.get("LoanSum"));
				int block = 0;
				// 前年
				if ((rocYY + 1911 - 2) == year) {
					block = 0;
					lastTwoLoan[month - 1] = lastTwoLoan[month - 1] + loanBal;
				}

				// 去年
				if ((rocYY + 1911 - 1) == year) {
					block = 1;
					lastLoan[month - 1] = lastLoan[month - 1] + loanBal;
				}

				// 今年
				if ((rocYY + 1911) == year) {
					block = 2;
					thisTotalLoan[month - 1] = thisTotalLoan[month - 1] + loanBal;

				}

				setVal(block, month, dataType, formatAmt(loanBal + "", 0));

			}
	
			// 合計
			for (int i = 0; i < 12; i++) {

				setVal(0, i + 1, 5, formatAmt(lastTwoLoan[i] + "", 0));
				setVal(1, i + 1, 5, formatAmt(lastLoan[i] + "", 0));
				setVal(2, i + 1, 5, formatAmt(thisTotalLoan[i] + "", 0));

			}

		} else{
			
			setVal(1, 1, 1, "本日無資料");
		}

		this.setFont(2, 8);

		this.close();
	}

	// 畫區塊(框)
	private void fw(int row, int rocYear) throws LogicException {

		print(row, 0, "┌────────┬───────────────────────────────────────────────────────────────────────────────────┐");
		print(1, 0, "│　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　│");
		print(0, 87, rocYear + "年度", "C");
		print(1, 0, "├────────┼──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┤");
		print(1, 0, "│　　　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│");

		for (int i = 0; i < 3; i++) {

			print(0, 24 + (i * 25), (i * 2 + 1) + "月", "C");
			print(0, 36 + (i * 25), (i * 2 + 2) + "月", "C");

			print(0, 24 + ((i + 3) * 25), (i * 2 + 7) + "月", "C");
			print(0, 37 + ((i + 3) * 25), (i * 2 + 8) + "月", "C");

		}

		print(1, 0, "├────────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┤");
		print(1, 0, "│　　　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│");
		print(0, 9, "921", "C");

		print(1, 0, "├────────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┤");
		print(1, 0, "│　　　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│");
		print(0, 9, "政府優惠", "C");
		print(1, 0, "├────────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┤");
		print(1, 0, "│　　　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│");
		print(0, 9, "首購", "C");
		print(1, 0, "├────────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┤");
		print(1, 0, "│　　　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│");
		print(0, 9, "專案放款-催收款項", "C");
		print(1, 0, "├────────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┼──────┤");
		print(1, 0, "│　　　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│　　　　　　│");
		print(0, 9, "合　　計", "C");
		print(1, 0, "└────────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┘");

	}

	/**
	 * 列印數值
	 * 
	 * @param block    區塊(1~3)
	 * @param month    月份
	 * @param dataType 放款類型
	 * @param val      輸出值
	 */
	private void setVal(int block, int month, int dataType, String val) {

		// 字體大小一定要6，因間距已算好，若要調整大小以下需全部重新調整
		this.setFont(2, 6);

		// 總共有三區塊，區分年度
		int blockRow = block * 150;

		// 每個區塊 X和Y軸起始
		// 初始基數79，63是已算好的間格，x參數的範圍為月份1~12(Month)
		int x = 79 + (63 * month);

		// 初始基數90，20是算好的間隔y參數為是哪種類型放款 (DataType)
		int y = 90 + (20 * dataType);

		this.printXY(x, y + blockRow, val, "R");

	}
}
