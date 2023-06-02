package com.st1.itx.trade.L9;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9133ServiceImpl;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;

@Component("L9133Report2")
@Scope("prototype")
public class L9133Report2 extends CommBuffer {

	@Autowired
	private L9133ServiceImpl l9133ServiceImpl;

	@Autowired
	private MakeExcel makeExcel;

	private String reportCode = "L9133";
	private String reportItem = "L9133-會計與主檔餘額檢核明細表";

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9133Report2 exec ...");

		int reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(reportCode).setRptItem(reportItem).build();

		makeExcel.open(titaVo, reportVo, reportItem, "L9133_底稿_會計與主檔餘額檢核明細表.xlsx", "明細");

		// 查差異明細
		List<Map<String, String>> resultList = l9133ServiceImpl.findDiffDetail(reportDate, titaVo);

		if (resultList == null || resultList.isEmpty()) {
			//this.error("L9133Report2 lAcAcctCheckDetail IS NULL.");
		} else {
			int rowCursor = 2;
			for (Map<String, String> result : resultList) {
				// 區隔帳冊
				makeExcel.setValue(rowCursor, 1, result.get("AcSubBookCode"));

				// 科目
				makeExcel.setValue(rowCursor, 2, result.get("AcctItem"));

				// 戶號
				String custNo = FormatUtil.pad9(String.valueOf(result.get("CustNo")), 7);
				String facmNo = FormatUtil.pad9(String.valueOf(result.get("FacmNo")), 3);
				String bormNo = FormatUtil.pad9(String.valueOf(result.get("BormNo")), 3);
				makeExcel.setValue(rowCursor, 3, custNo + "-" + facmNo + "-" + bormNo);

				// 銷帳檔餘額
				makeExcel.setValue(rowCursor, 4, result.get("AcBal"), "#,##0");

				// 業務帳餘額
				makeExcel.setValue(rowCursor, 5, result.get("AcctMasterBal"), "#,##0");

				// 差額 (銷帳檔餘額-業務帳餘額)
				makeExcel.setValue(rowCursor, 6, result.get("DiffBal"), "#,##0");

				rowCursor++;
			}
		}
		long excelNo = makeExcel.close();

		makeExcel.toExcel(excelNo);
	}

	@Override
	public void exec() throws LogicException {
	}
}
