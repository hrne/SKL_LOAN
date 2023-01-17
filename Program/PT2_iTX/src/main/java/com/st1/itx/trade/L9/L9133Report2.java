package com.st1.itx.trade.L9;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcAcctCheckDetail;
import com.st1.itx.db.service.AcAcctCheckDetailService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;

@Component("L9133Report2")
@Scope("prototype")
public class L9133Report2 extends CommBuffer {

	/* DB服務注入 */
	@Autowired
	AcAcctCheckDetailService sAcAcctCheckDetailService;

	@Autowired
	private MakeExcel makeExcel;

	private String reportCode = "L9133";
	private String reportItem = "會計與主檔餘額檢核明細表";

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9133Report2 exec ...");

		int reportDate = Integer.valueOf(titaVo.getParam("AcDate")) + 19110000;

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(reportCode).setRptItem(reportItem).build();

		makeExcel.open(titaVo, reportVo, reportItem, "L9133_底稿_會計與主檔餘額檢核明細表.xlsx", "明細");

		// 查會計業務檢核檔
		Slice<AcAcctCheckDetail> slAcAcctCheckDetail = sAcAcctCheckDetailService.findAcDate(reportDate, 0,
				Integer.MAX_VALUE, titaVo);
		List<AcAcctCheckDetail> lAcAcctCheckDetail = slAcAcctCheckDetail == null ? null
				: slAcAcctCheckDetail.getContent();

		if (lAcAcctCheckDetail == null || lAcAcctCheckDetail.size() == 0) {
			this.error("L9133Report2 lAcAcctCheckDetail IS NULL.");
		} else {
			int rowCursor = 2;
			for (AcAcctCheckDetail tAcAcctCheckDetail : lAcAcctCheckDetail) {
				// 區隔帳冊
				makeExcel.setValue(rowCursor, 1, tAcAcctCheckDetail.getAcSubBookCode());

				// 科目
				makeExcel.setValue(rowCursor, 2, tAcAcctCheckDetail.getAcctItem());

				// 戶號
				String custNo = FormatUtil.pad9(String.valueOf(tAcAcctCheckDetail.getCustNo()), 7);
				String facmNo = FormatUtil.pad9(String.valueOf(tAcAcctCheckDetail.getFacmNo()), 3);
				String bormNo = FormatUtil.pad9(String.valueOf(tAcAcctCheckDetail.getBormNo()), 3);
				makeExcel.setValue(rowCursor, 3, custNo + "-" + facmNo + "-" + bormNo);

				// 銷帳檔餘額
				makeExcel.setValue(rowCursor, 4, tAcAcctCheckDetail.getAcBal(), "#,##0");

				// 業務帳餘額
				makeExcel.setValue(rowCursor, 5, tAcAcctCheckDetail.getAcctMasterBal(), "#,##0");

				// 差額 (銷帳檔餘額-業務帳餘額)
				makeExcel.setValue(rowCursor, 6, tAcAcctCheckDetail.getDiffBal(), "#,##0");

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
