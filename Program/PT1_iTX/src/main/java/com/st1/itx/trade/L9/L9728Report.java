package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9728ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9728Report extends MakeReport {

	@Autowired
	L9728ServiceImpl L9728ServiceImpl;

	@Autowired
	Parse parse;
	
	@Autowired
	DateUtil dateUtil;

	String txcd = "L9728";
	String txname = "申請不列印書面通知書控管報表";
	
	int custNoStart;
	int custNoEnd;
	int findDateStart;
	int findDateEnd;
	
	boolean useDate;
	
	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9728Report exec start ...");

		List<Map<String, String>> listL9728 = null;
		
		custNoStart = parse.stringToInteger(titaVo.get("CustNoStart"));
		custNoEnd = parse.stringToInteger(titaVo.get("CustNoEnd"));
		findDateStart = parse.stringToInteger(titaVo.get("FindDateStart"));		
		findDateEnd = parse.stringToInteger(titaVo.get("FindDateEnd"));
		
		useDate = findDateStart > 0 && findDateEnd >= findDateStart;
				
		try {
			listL9728 = L9728ServiceImpl.findAll(custNoStart, custNoEnd, useDate ? findDateStart + 19110000 : 19110101, useDate ? findDateEnd + 19110000 : 99991231, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9728ServiceImpl.findAll error = " + errors.toString());
		}
		
		return exportPdf(titaVo, listL9728);
	}
	
	@Override
	public void printHeader() {
		
		this.setCharSpaces(0);
		this.setFontSize(12);

		this.print(-1, 1, "  程式ID：" + this.getParentTranCode());
		this.print(-1, this.getMidXAxis(), "新光人壽保險股份有限公司", "C");
		this.print(-1, 123, "日  期：" + this.showBcDate(dateUtil.getNowStringBc(), 1));
		this.print(-2, 1, "  報  表：" + this.getRptCode());
		this.print(-2, this.getMidXAxis(), "申請不列印書面通知書控管報表", "C");
		this.print(-2, 123, "時  間：" + dateUtil.getNowStringTime().substring(0, 2) + ":" + dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
		this.print(-3, 123, "頁  數：" + this.getNowPage());
		this.print(-4, 1, String.format("  戶號... %07d - %07d ", custNoStart, custNoEnd));
		
		if (useDate)
			this.print(-5, 1, String.format("  期間... %s - %s ", this.showRocDate(findDateStart, 1), this.showRocDate(findDateEnd, 1)));
		/**
		 * ------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
		 * ---------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
		 */
		this.print(-7, 1,"       戶號    額度 戶名                                新增者       新增時間              最後修改者   最後修改時間");
		this.print(-8, 1,"  ========================================================================================================================================");
		
		this.setBeginRow(9);

		this.setMaxRows(29);
	}
	
	@Override
	public void printFooter() {
		this.print(1, 1,"  ========================================================================================================================================");
	}

	private Boolean exportPdf(TitaVo titaVo, List<Map<String, String>> list) throws LogicException {
		
		Boolean isSuccess = true;

		this.info("L9728Report exportPdf");

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txcd, txname, "", "A4", "L");
	
		if (list == null || list.isEmpty())
		{
			// 本日無資料
			this.print(1, 1, "  本日無資料!");
			isSuccess = false;
		} else {
			for (Map<String, String> l9728Vo : list)
			{
				this.print(1, 8, FormatUtil.pad9(l9728Vo.get("CustNo"), 7));
				this.print(0, 16, l9728Vo.get("FacmNo"));
				this.print(0, 21, l9728Vo.get("CustName"));
				this.print(0, 57, l9728Vo.get("CreateName"));
				this.print(0, 70, parse.stringToStringDateTime(l9728Vo.get("CreateDate")));
				this.print(0, 92, l9728Vo.get("UpdateName"));
				this.print(0, 105, parse.stringToStringDateTime(l9728Vo.get("UpdateDate")));
			}
		}
		
		// close as pdf
		// long sno = 
		this.close();
		// this.toPdf(sno);
		
		return isSuccess;
	}
}
