package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9134ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component("L9134Report")
@Scope("prototype")
public class L9134Report extends MakeReport {

	@Autowired
	L9134ServiceImpl l9134ServiceImpl;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L9134";
	private String reportItem = "暫收款傳票金額表(累計)";
	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "L";

	// 製表日期
	private String nowDate;
	// 製表時間
	private String nowTime;

	private String headerStartDate = "";
	private String headerEndDate = "";

	private BigDecimal drSum = BigDecimal.ZERO;
	private BigDecimal crSum = BigDecimal.ZERO;
	private BigDecimal diffSum = BigDecimal.ZERO;

	private BigDecimal drTotal = BigDecimal.ZERO;
	private BigDecimal crTotal = BigDecimal.ZERO;
	private BigDecimal diffTotal = BigDecimal.ZERO;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L9134Report.printHeader");

		this.print(-1, 1, "程式ID：" + this.getParentTranCode());
		this.print(-1, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		this.print(-1, 145, "機密等級：" + this.security);
		this.print(-2, 1, "報　表：" + this.reportCode);
		this.print(-2, this.getMidXAxis(), this.reportItem, "C");
		this.print(-2, 145, "日　　期：" + showBcDate(this.nowDate, 1));
		this.print(-3, 145, "時　　間：" + showTime(this.nowTime));
		this.print(-4, 1, "會計日期：　" + headerStartDate + "　－　" + headerEndDate);
		this.print(-4, 145, "頁　　次：" + this.getNowPage());
		this.print(-4, this.getMidXAxis(), showRocDate(this.reportDate), "C");
		// 明細表頭
		// -------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		// ----------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		this.print(-5, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　（貸方金額－借方金額）");
		this.print(-6, 1, "　　　會計日期　　會計科目　　　　　　　　　　　　　　　　　　借方金額　　　　　貸方金額　　　　　　　當日小計");
		this.print(-7, 1, "　　　－－－－－　－－－－－－－－－－－－－－－－－－－　－－－－－－－－　－－－－－－－－　－－－－－－－－－－－");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(8);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(40);

	}

	@Override
	public void printContinueNext() {
		this.print(1, this.getMidXAxis(), "=====　續　　下　　頁　=====", "C");
	}

	public void exec(int startDate, int endDate, TitaVo titaVo) throws LogicException {
		this.info("L9134Report exec ...");

		headerStartDate = this.showRocDate(startDate, 1);
		headerEndDate = this.showRocDate(endDate, 1);

		this.setFont(1, 12);

		this.reportDate = endDate;

		this.brno = titaVo.getBrno();

		this.nowDate = dDateUtil.getNowStringRoc();
		this.nowTime = dDateUtil.getNowStringTime();

//		this.open(titaVo, reportDate, brno, reportCode, reportItem, security, pageSize, pageOrientation);
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(reportCode)
				.setRptItem(reportItem).setRptSize(pageSize).setPageOrientation(pageOrientation)
				.build();

		open(titaVo, reportVo);

		this.setCharSpaces(0);

		List<Map<String, String>> list = l9134ServiceImpl.doQuery(startDate, endDate, titaVo);

		if (list == null || list.isEmpty()) {
			// 本日無資料
			print(1, 1, "本日無資料");
		} else {
			int lastAcDate = 0;
			/*
			 * 20222010000 暫收及待結轉帳項－火險保費 
			 * 20222020000 暫收及待結轉帳項－擔保放款 
			 * 20222180000 暫收及待結轉帳項－債權協商
			 * 20222180100 暫收及待結轉帳項－更生統一收付 
			 * 20222180200 暫收及待結轉帳項－前置調解
			 */
			for (Map<String, String> map : list) {
				int acDate = Integer.parseInt(map.get("AcDate"));
				if (lastAcDate != 0 && lastAcDate != acDate) {
					// 印合計
					printSum(lastAcDate);
				}
				// 新的一行
				print(1, 0, "");
				print(0, 8, showRocDate(acDate, 1)); // 會計日期
				
				String AcNameCode = "";
				if(map.get("AcNoCode").equals("20222010000")) {
					AcNameCode = map.get("AcNoCode")+"－火險保費 ";
				}
				if(map.get("AcNoCode").equals("20222020000")) {
					AcNameCode = map.get("AcNoCode")+"－擔保放款  ";
				}
				if(map.get("AcNoCode").equals("20222180000")) {
					AcNameCode = map.get("AcNoCode")+"－債權協商  ";
				}
				if(map.get("AcNoCode").equals("20222180100")) {
					AcNameCode = map.get("AcNoCode")+"－更生統一收付   ";
				}
				if(map.get("AcNoCode").equals("20222180200")) {
					AcNameCode = map.get("AcNoCode")+"－前置調解  ";
				}
				
				print(0, 19, AcNameCode); // 會計科目	
				print(0, 74, this.formatAmt(map.get("DrAmt"), 0), "R"); // 借方金額
				print(0, 92, this.formatAmt(map.get("CrAmt"), 0), "R"); // 貸方金額
				print(0, 116, this.formatAmt(map.get("DiffAmt"), 0), "R"); // 當日小計

				// 計入合計
				drSum = drSum.add(getBigDecimal(map.get("DrAmt")));
				crSum = crSum.add(getBigDecimal(map.get("CrAmt")));
				diffSum = diffSum.add(getBigDecimal(map.get("DiffAmt")));

				// 一行結束
				lastAcDate = acDate;
			}
			// 印合計
			printSum(lastAcDate);

			// 印總計
			// 新的一行
			print(1, 19, "總計");
			print(0, 74, this.formatAmt(drTotal, 0), "R"); // 借方金額
			print(0, 92, this.formatAmt(crTotal, 0), "R"); // 貸方金額
			print(0, 116, this.formatAmt(diffTotal, 0), "R"); // 當日小計
		}

		// 此表不用簽核但需要印報表結束
		this.print(1, this.getMidXAxis(), "=====　報　表　結　束　=====", "C");

		this.close();
		this.info("L9134Report finished.");
	}

	private void printSum(int acDate) {
		// 新的一行
		print(1, 2, "合計");
		print(0, 8, showRocDate(acDate, 1)); // 會計日期
		print(0, 74, this.formatAmt(drSum, 0), "R"); // 借方金額
		print(0, 92, this.formatAmt(crSum, 0), "R"); // 貸方金額
		print(0, 116, this.formatAmt(diffSum, 0), "R"); // 當日小計

		// 計入總計
		drTotal = drTotal.add(drSum);
		crTotal = crTotal.add(crSum);
		diffTotal = diffTotal.add(diffSum);

		// 清零
		drSum = BigDecimal.ZERO;
		crSum = BigDecimal.ZERO;
		diffSum = BigDecimal.ZERO;
	}
}
