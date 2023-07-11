package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxHoliday;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.service.TxHolidayService;
import com.st1.itx.db.service.springjpa.cm.L9134ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component("L9134Report4")
@Scope("prototype")
public class L9134Report4 extends MakeReport {

	@Autowired
	L9134ServiceImpl l9134ServiceImpl;
	@Autowired
	private TxBizDateService iTxBizDateService;

	@Autowired
	private TxHolidayService iTxHoliday;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;
	
	BigDecimal iDifTdBalSum=BigDecimal.ZERO;
	BigDecimal ETalSum=BigDecimal.ZERO;
	BigDecimal didCDrAmtSum=BigDecimal.ZERO;
	BigDecimal JTalSum=BigDecimal.ZERO;
	BigDecimal didDifTdBalSum=BigDecimal.ZERO;
	BigDecimal iTemTalSum=BigDecimal.ZERO;

	public void exec(int endDate, TitaVo titaVo) throws LogicException {
		String reportCode = "L9134";
		String reportItem = "暫收款對帳-日調結表";
		String fileName = "暫收款對帳-日調結表";
		String defaultFile = "暫收款對帳-日調結表.xlsx";

		ReportVo reportVo = ReportVo.builder().setRptDate(endDate).setBrno(titaVo.getBrno()).setRptCode(reportCode)
				.setRptItem(reportItem).build();

		makeExcel.open(titaVo, reportVo, fileName, defaultFile, "工作表1");

		List<Map<String, String>> findList = new ArrayList<>();

		TxBizDate tTxBizDate = new TxBizDate();

		tTxBizDate = iTxBizDateService.findById("ONLINE", titaVo);

		ExcelFontStyleVo efvo = new ExcelFontStyleVo();
		
	
		efvo.setAlign("R");
		
		// 上個月底日(民國年)
		int ixLmnDy = tTxBizDate.getLmnDy();
		// 本月底日(民國年)
		int ixTmnDy = tTxBizDate.getTmnDy();
		if(endDate > 19110000) {
			ixTmnDy = endDate - 19110000;//畫面輸入值
		}

		this.info("iLmnDy    = " + tTxBizDate.getLmnDy());
		this.info("iTmnDy    = " + tTxBizDate.getTmnDy());

		try {
			findList = l9134ServiceImpl.doQueryL9134_4(titaVo, ixLmnDy, ixTmnDy);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l9134ServiceImpl.doQueryL9134_4 error = " + errors.toString());
		}
		int row = 5;

		makeExcel.setShiftRow(row+1, findList.size() - 1);

		if (findList.size() == 0 || findList.isEmpty()) {

			makeExcel.setValue(5, 1, "本日無資料");

		} else {

			for (Map<String, String> r : findList) {
				this.info("AcDate     = " + r.get("AcDate"));
				int iYear = ((parse.stringToInteger(r.get("AcDate").substring(0, 2))) * 10000)+ 19110000;
				int iMon = parse.stringToInteger(r.get("AcDate").substring(4, 5)) * 100;
				int iDay = parse.stringToInteger(r.get("AcDate").substring(7, 8));
				this.info("iYear   = " + iYear);
				this.info("iMon    = " + iMon);
				this.info("iDay    = " + iDay);
				int nowday = iYear + iMon + iDay;
				Slice<TxHoliday> nowDay = iTxHoliday.findHoliday("TW", nowday, nowday, ixTmnDy, row, titaVo);
				if (nowDay == null) {
					
					BigDecimal iTdBal = parse.stringToBigDecimal(r.get("TdBal"));
					BigDecimal iDifTdBal = parse.stringToBigDecimal(r.get("DifTdBal"));
					BigDecimal didTdBal = parse.stringToBigDecimal(r.get("didTdBal"));
					BigDecimal didDifTdBal = parse.stringToBigDecimal(r.get("didDifTdBal"));
					BigDecimal drAmt = parse.stringToBigDecimal(r.get("DrAmt"));
					BigDecimal crAmt = parse.stringToBigDecimal(r.get("CrAmt"));
					BigDecimal iTemTal = parse.stringToBigDecimal(r.get("TemTal"));
					BigDecimal didCDrAmt = parse.stringToBigDecimal(r.get("didCDrAmt"));
//				int AcDate = parse.stringToInteger(r.get("AcDate"));
					BigDecimal ETal = iDifTdBal.add(didDifTdBal);
					BigDecimal JTal = didCDrAmt.add(iTemTal);
					
					
					
					if(iTdBal.equals(BigDecimal.ZERO)&&iDifTdBal.equals(BigDecimal.ZERO)
							&& didTdBal.equals(BigDecimal.ZERO)&& didDifTdBal.equals(BigDecimal.ZERO)) {
						
						makeExcel.setValue(row, 1, "");
						makeExcel.setValue(row, 2, "");
						makeExcel.setValue(row, 3, "");
						makeExcel.setValue(row, 4, "");
						makeExcel.setValue(row, 5, "");
						makeExcel.setValue(row, 6, r.get("AcDate"), "R");
						makeExcel.setValue(row, 7, "");
						makeExcel.setValue(row, 8, "");
						makeExcel.setValue(row, 10, "");
						makeExcel.setValue(row, 11, "");
						makeExcel.setValue(row, 12, "");
						makeExcel.setValue(row, 13, "");
						
					}else {
						
						makeExcel.setValue(row, 1, iTdBal, "#,##0", efvo);
						makeExcel.setValue(row, 2, iDifTdBal, "#,##0", efvo);
						makeExcel.setValue(row, 3, didTdBal, "#,##0", efvo);
						makeExcel.setValue(row, 4, didDifTdBal, "#,##0", efvo);
						makeExcel.setValue(row, 5, ETal, "#,##0", efvo);
						makeExcel.setValue(row, 6, r.get("AcDate"), "C");
						makeExcel.setValue(row, 7, didCDrAmt, efvo);
						makeExcel.setValue(row, 8, iTemTal, "#,##0", efvo);
						makeExcel.setValue(row, 10, JTal, "#,##0", efvo);
						makeExcel.setValue(row, 11, ETal.subtract(JTal), "#,##0", efvo);
						makeExcel.setValue(row, 12, drAmt, "#,##0", efvo);
						makeExcel.setValue(row, 13, crAmt, "#,##0", efvo);
						if(row>5) {
							iDifTdBalSum=iDifTdBalSum.add(iDifTdBal);
							ETalSum=ETalSum.add(ETal);
							didCDrAmtSum=didCDrAmtSum.add(didCDrAmt);
							JTalSum=JTalSum.add(JTal);
							didDifTdBalSum=didDifTdBalSum.add(didDifTdBal);
							iTemTalSum=iTemTalSum.add(iTemTal);
						}
						
					}
					
					
					BigDecimal ix = crAmt.subtract(drAmt);
					BigDecimal ii = iDifTdBal.add(didDifTdBal);
					BigDecimal iy = ix.subtract(ii);
					BigDecimal io = BigDecimal.ZERO;
					if (iy.compareTo(io) == 0) {
						makeExcel.setValue(row, 14, "V");
					} else {
						makeExcel.setValue(row, 14, "X");
					}
				} 
				row++;
			}
			makeExcel.setValue(row, 2, iDifTdBalSum, "#,##0",efvo);
			makeExcel.setValue(row, 4, didDifTdBalSum, "#,##0",efvo);
			makeExcel.setValue(row, 5, ETalSum, "#,##0",efvo);
			makeExcel.setValue(row, 7, didCDrAmtSum,efvo);
			makeExcel.setValue(row, 8, iTemTalSum,efvo);
			makeExcel.setValue(row, 10, JTalSum, "#,##0",efvo);
		}
		makeExcel.close();
	}

}
