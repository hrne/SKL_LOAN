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

@Component("L9134Report3")
@Scope("prototype")
public class L9134Report3 extends MakeReport {

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
	private String reportCode = "L9134";
	private String reportItem = "放款暫收款對帳明細表";


	@Autowired
	public MakeExcel makeExcel;

	public void exec(int endDate, TitaVo titaVo) throws LogicException {
		this.info("L9134Report exec ...");

		this.reportDate = endDate;

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(titaVo.getBrno()).setRptCode(reportCode)
				.setRptItem(reportItem).setSecurity(this.getSecurity()).build();

		makeExcel.open(titaVo, reportVo, reportItem);


		makeExcel.setValue(1, 1, "暫收款名稱", "C");
		makeExcel.setValue(1, 2, "戶號", "C");
		makeExcel.setValue(1, 3, "額度", "C");
		makeExcel.setValue(1, 4, "起帳日期", "C");
		makeExcel.setValue(1, 5, "最後交易日", "C");
		makeExcel.setValue(1, 6, "暫收款餘額", "C");


		List<Map<String, String>> list = l9134ServiceImpl.doQueryL9134_3(titaVo);

		if (list == null || list.isEmpty()) {
			// 本日無資料
			makeExcel.setValue(2, 1, "本日無資料");
		} else {
			int row = 1;
			/*
			 * 20222010000 暫收及待結轉帳項－火險保費 
			 * 20222020000 暫收及待結轉帳項－擔保放款 
			 * 20222180000 暫收及待結轉帳項－債權協商
			 * 20222180100 暫收及待結轉帳項－更生統一收付 
			 * 20222180200 暫收及待結轉帳項－前置調解
			 */
			for (Map<String, String> r : list) {
				row++;

				makeExcel.setValue(row, 1, r.get("AcNoItem"));
				makeExcel.setValue(row, 2, r.get("CustNo"));
				makeExcel.setValue(row, 3, r.get("FacmNo"));
				makeExcel.setValue(row, 4, r.get("OpenAcDate"));
				makeExcel.setValue(row, 5, r.get("LastTxDate"));
				makeExcel.setValue(row, 6, r.get("RvBal").isEmpty() ? BigDecimal.ZERO : new BigDecimal(r.get("RvBal")),
						"#,##0", "R");

			}

		}

		makeExcel.close();

	}

}
