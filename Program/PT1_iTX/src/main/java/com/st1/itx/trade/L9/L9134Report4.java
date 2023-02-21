package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.service.springjpa.cm.L9134ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component("L9134Report4")
@Scope("prototype")
public class L9134Report4 extends MakeExcel {

	@Autowired
	L9134ServiceImpl l9134ServiceImpl;
	@Autowired
	private TxBizDateService iTxBizDateService;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;
	
	public void exec(TitaVo titaVo) throws LogicException {
		final String REPORT_CODE = "L9134";
		final String REPORT_ITEM = "暫收款對帳-日調結表";
		final String FILE_NAME = "暫收款對帳-日調結表";
		final String DEFAULT_EXCEL = "暫收款對帳-日調結表.xlsx";

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getKinbr()).setRptDate(titaVo.getEntDyI())
				.setRptCode(REPORT_CODE).setRptItem(REPORT_ITEM).build();
		/**
		 * 開啟excel製檔<br>
		 * 指定底稿
		 * 
		 * @param titaVo       titaVo
		 * @param reportVo     reportVo
		 * @param fileName     檔案實際輸出名稱
		 * @param defaultExcel 底稿檔案名稱
		 * @param defaultSheet 底稿頁籤名稱
		 * @throws LogicException
		 */
		int idate = parse.stringToInteger(titaVo.getParam("EndDate").trim());
		this.info("idate  = " + idate);

		makeExcel.open(titaVo, reportVo, FILE_NAME, DEFAULT_EXCEL, "工作表1");
		List<Map<String, String>> findList = new ArrayList<>();
		
		TxBizDate tTxBizDate = new TxBizDate();
		tTxBizDate = iTxBizDateService.findById("ONLINE", titaVo);
		int iLmnDy    = tTxBizDate.getLmnDy();
		int iTmnDy    = tTxBizDate.getTmnDy();
		this.info("iLmnDy    = "+ tTxBizDate.getLmnDy());
		this.info("iTmnDy    = "+ tTxBizDate.getTbsDy());
		
		try {
			findList = l9134ServiceImpl.doQueryL9134_4_1(titaVo, idate , iLmnDy, iTmnDy);
			
			this.info("findList    = " + findList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l9134ServiceImpl.doQueryL9134_4 error = " + errors.toString());
		}
		int row=5;
		int size = findList.size();
		this.info("Size = " + findList.size());
		makeExcel.setShiftRow(row, size-1);
			for (Map<String, String> r : findList) {
				BigDecimal iTdBal = parse.stringToBigDecimal(r.get("TdBal"));
				BigDecimal iDifTdBal =parse.stringToBigDecimal(r.get("DifTdBal"));
				BigDecimal didTdBal = parse.stringToBigDecimal(r.get("didTdBal"));
				BigDecimal didDifTdBal = parse.stringToBigDecimal(r.get("didDifTdBal"));
				BigDecimal drAmt = parse.stringToBigDecimal(r.get("DrAmt"));
				BigDecimal crAmt = parse.stringToBigDecimal(r.get("CrAmt"));
//				int AcDate = parse.stringToInteger(r.get("AcDate"));

				makeExcel.setValue(row, 1, iTdBal , "#,##0");
				makeExcel.setValue(row, 2, iDifTdBal, "#,##0");
				makeExcel.setValue(row, 3, didTdBal, "#,##0");
				makeExcel.setValue(row, 4, didDifTdBal, "#,##0");
				makeExcel.setValue(row, 5, iDifTdBal.add(didDifTdBal), "#,##0");
				makeExcel.setValue(row, 6, r.get("AcDate"),"R");	
				makeExcel.setValue(row, 12, drAmt , "#,##0");	
				makeExcel.setValue(row, 13, crAmt , "#,##0");	
				BigDecimal ix = crAmt.subtract(drAmt);
				BigDecimal ii = iDifTdBal.add(didDifTdBal);
				BigDecimal iy = ix.subtract(ii);
				BigDecimal io = BigDecimal.ZERO;
				if(iy.compareTo(io)==0) {
					makeExcel.setValue(row, 14, "V");
				}else {
					makeExcel.setValue(row, 14, "X");
				}
				
				
				row++;

		}
		makeExcel.close();
	}

}
