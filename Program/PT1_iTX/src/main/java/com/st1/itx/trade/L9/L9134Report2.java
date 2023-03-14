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
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component("L9134Report2")
@Scope("prototype")
public class L9134Report2 extends MakeReport {

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
//	private String brno = "";
	private String reportCode = "L9134";
	private String reportItem = "暫收款傳票金額表(明細)";

	private String headerStartDate = "";
	private String headerEndDate = "";

	@Autowired
	public MakeExcel makeExcel;

	public void exec(int startDate, int endDate, TitaVo titaVo) throws LogicException {
		this.info("L9134Report exec ...");

		headerStartDate = this.showRocDate(startDate, 1);
		headerEndDate = this.showRocDate(endDate, 1);
		this.reportDate = endDate;

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(titaVo.getBrno()).setRptCode(reportCode)
				.setRptItem(reportItem).build();

		makeExcel.open(titaVo, reportVo, reportItem);

		makeExcel.setMergedRegionValue(1, 1, 1, 5, "會計日期： " + headerStartDate + " － " + headerEndDate);

		makeExcel.setValue(2, 1, "會計日期", "C");
		makeExcel.setValue(2, 2, "會計科目", "C");
		makeExcel.setValue(2, 3, "子目", "C");
		makeExcel.setValue(2, 4, "細目", "C");
		makeExcel.setValue(2, 5, "科目名稱", "C");
		makeExcel.setValue(2, 6, "傳票借方金額", "C");
		makeExcel.setValue(2, 7, "傳票貸方金額", "C");
		makeExcel.setValue(2, 8, "傳票號碼", "C");
		makeExcel.setValue(2, 9, "交易序號", "C");
		makeExcel.setValue(2, 10, "戶號", "C");
		// 2022-10-14新增額度欄位
		makeExcel.setValue(2, 11, "額度", "C");
		makeExcel.setValue(2, 12, "戶名", "C");

		List<Map<String, String>> list = l9134ServiceImpl.doDetailQuery(startDate, endDate, titaVo);

		if (list == null || list.isEmpty()) {
			// 本日無資料
			makeExcel.setValue(3, 1, "本日無資料");
		} else {
			int row = 2;
			/*
			 * 20222010000 暫收及待結轉帳項－火險保費 
			 * 20222020000 暫收及待結轉帳項－擔保放款 
			 * 20222180000 暫收及待結轉帳項－債權協商
			 * 20222180100 暫收及待結轉帳項－更生統一收付 
			 * 20222180200 暫收及待結轉帳項－前置調解
			 */
			for (Map<String, String> r : list) {
				row++;

				makeExcel.setValue(row, 1, r.get("AcDate"));
				makeExcel.setValue(row, 2, r.get("AcNoCode"));
				makeExcel.setValue(row, 3, r.get("AcSubCode"));
				makeExcel.setValue(row, 4, r.get("AcDtlCode"));
				makeExcel.setValue(row, 5, r.get("AcNoItem"));
				makeExcel.setValue(row, 6, r.get("DrAmt").isEmpty() ? BigDecimal.ZERO : new BigDecimal(r.get("DrAmt")),
						"#,##0", "R");
				makeExcel.setValue(row, 7, r.get("CrAmt").isEmpty() ? BigDecimal.ZERO : new BigDecimal(r.get("CrAmt")),
						"#,##0", "R");
				makeExcel.setValue(row, 8, r.get("SlipNo"), "R");
				makeExcel.setValue(row, 9, r.get("TitaTxtNo"), "R");
				makeExcel.setValue(row, 10, r.get("CustNo"));
				makeExcel.setValue(row, 11, r.get("FacmNo"));
				makeExcel.setValue(row, 12, r.get("CustName"));

			}

		}

		makeExcel.close();

	}

}
