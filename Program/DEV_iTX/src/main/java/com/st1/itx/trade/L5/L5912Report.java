package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.springjpa.cm.L5912ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("L5912Report")
@Scope("prototype")

public class L5912Report extends MakeReport {

	@Autowired
	public PfBsDetailService iPfBsDetailService;

	@Autowired
	public CdEmpService iCdEmpService;

	@Autowired
	public CdCodeService iCdCodeService;
	
	@Autowired
	public CdWorkMonthService iCdWorkMonthService;
	
	@Autowired
	public L5912ServiceImpl iL5912ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public long exec(TitaVo titaVo) throws LogicException {

		this.info("L5912Report start success");

		String iDrawDateFm = titaVo.getParam("DrawDownDateFm");
		String iDrawDateTo = titaVo.getParam("DrawDownDateTo");
		String iCDate = titaVo.getCalDy();

		List<String> header = new ArrayList<>();
		List<String> reportHeader = new ArrayList<>();
		header.addAll(Arrays.asList("戶號", "額度", "撥款", "撥款金額", "撥款日", "到期日", "扣款銀行", "計件代碼", "房專員編", "房專姓名"));
		reportHeader.addAll(Arrays.asList("部室", "經辦區部", "姓名", "新貸件數", "新光銀行扣款件數", "占率"));
		//抓工作月
		CdWorkMonth iCdWorkMonthSt = new CdWorkMonth();
		CdWorkMonth iCdWorkMonthEd = new CdWorkMonth();
		iCdWorkMonthSt = iCdWorkMonthService.findDateFirst(Integer.valueOf(iDrawDateFm)+19110000, Integer.valueOf(iDrawDateFm)+19110000, titaVo);
		if (iCdWorkMonthSt == null) {
			throw new LogicException(titaVo, "E0001", "工作月起");
		}
		iCdWorkMonthEd = iCdWorkMonthService.findDateFirst(Integer.valueOf(iDrawDateTo)+19110000, Integer.valueOf(iDrawDateTo)+19110000, titaVo);
		if (iCdWorkMonthEd == null) {
			throw new LogicException(titaVo, "E0001", "工作月迄");
		}
		String iYearSt = StringUtils.leftPad(String.valueOf(iCdWorkMonthSt.getYear()-1911), 3,'0');
		String iMonthSt = StringUtils.leftPad(String.valueOf(iCdWorkMonthSt.getMonth()), 2,'0');
		String iYearEd = StringUtils.leftPad(String.valueOf(iCdWorkMonthEd.getYear()-1911), 3,'0');
		String iMonthEd = StringUtils.leftPad(String.valueOf(iCdWorkMonthEd.getMonth()), 2,'0');
		List<Map<String, String>> t5912SqlReturn = new ArrayList<Map<String, String>>();
		List<Map<String, String>> d5912SqlReturn = new ArrayList<Map<String, String>>();
		String fileName = iYearSt+iMonthSt+"到"+iYearEd+iMonthEd+"工作月新光銀扣款件排行-"+iCDate;
		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5912", fileName, fileName);
		
		try {
			t5912SqlReturn = iL5912ServiceImpl.findDetail(Integer.valueOf(iDrawDateFm)+19110000, Integer.valueOf(iDrawDateTo)+19110000,titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (t5912SqlReturn == null || t5912SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001","查無明細資料");
		}
		try {
			d5912SqlReturn = iL5912ServiceImpl.findData(Integer.valueOf(iDrawDateFm)+19110000, Integer.valueOf(iDrawDateTo)+19110000,titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5908 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (d5912SqlReturn == null || d5912SqlReturn.isEmpty()) {
			throw new LogicException(titaVo, "E0001","查無報表資料");
		}
		// 列數
		int row = 1;

		// 表頭列數
		int hcol = 0;
		
		//明細
		makeExcel.setSheet("L5912", "明細");
		for (String content : header) {
			makeExcel.setFontType(1);
			switch(hcol+1) {
			case 4:
				makeExcel.setColor("Blue");
				break;
			case 5:
				makeExcel.setBackGroundColor("Orange");
				makeExcel.setColor("White");
				break;
			case 7:
				makeExcel.setBackGroundColor("Green");
				makeExcel.setColor("White");
				break;
			case 8:
				makeExcel.setBackGroundColor("Orange");
				makeExcel.setColor("White");
				break;
			case 9:
				makeExcel.setBackGroundColor("Green");
				makeExcel.setColor("White");
				break;
			case 10:
				makeExcel.setBackGroundColor("Green");
				makeExcel.setColor("White");
				break;
			default:
				makeExcel.setBackGroundColor("Grey");
				break;
			}
			makeExcel.setValue(row, hcol + 1, content,"R");
			hcol++;
		}
		for (Map<String, String> s5912SqlReturn : t5912SqlReturn) {
			row++;
			for (int col = 0; col < s5912SqlReturn.size(); col++) {
				switch (col) {
				// 處裡左右靠
				case 0:
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5912SqlReturn.get("CustNo")));
					break;
				case 1:
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5912SqlReturn.get("FacmNo")));
					break;
				case 2:
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5912SqlReturn.get("BormNo")));
					break;
				case 3:
					makeExcel.setColor("Blue");
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5912SqlReturn.get("DrawdownAmt")),"#,##0");
					break;	
				case 4:
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5912SqlReturn.get("DrawdownDate"))-19110000);
					break;
				case 5:
					makeExcel.setValue(row, col + 1, Integer.valueOf(s5912SqlReturn.get("MaturityDate"))-19110000);
					break;
				case 6:
					makeExcel.setValue(row, col + 1, s5912SqlReturn.get("RepayBank"),"R");
					break;
				case 7:
					makeExcel.setValue(row, col + 1, s5912SqlReturn.get("PieceCode"),"R");
					break;
				case 8:
					makeExcel.setValue(row, col + 1, s5912SqlReturn.get("BsOfficer"));
					break;
				case 9:
					makeExcel.setValue(row, col + 1, s5912SqlReturn.get("Fullname"));
					break;

				}
			}
		}
		makeExcel.setWidth(4, 16);
		makeExcel.setWidth(7, 14);
		makeExcel.setWidth(8, 14);
		
		//報表
		// 列數
		row = 1;

		// 表頭列數
		hcol = 0;
		
		makeExcel.newSheet("報表");
		for (String content : reportHeader) {
			makeExcel.setValue(row, hcol + 1, content);
			hcol++;
		}
		for (Map<String, String> a5912SqlReturn : d5912SqlReturn) {
			row++;
			for (int col = 0; col < a5912SqlReturn.size(); col++) {
				BigDecimal iTotal = new BigDecimal("0");
				BigDecimal i103Total = new BigDecimal("0");
				BigDecimal iAvg = new BigDecimal("0");
				BigDecimal iComp = new BigDecimal("0");
				BigDecimal i100 = new BigDecimal("100");
				if (!a5912SqlReturn.get("F4").isEmpty() && !a5912SqlReturn.get("F4").equals("")) {
					iTotal = new BigDecimal(a5912SqlReturn.get("F4"));
				}
				
				if (!a5912SqlReturn.get("F9").isEmpty() && !a5912SqlReturn.get("F9").equals("")) {
					i103Total = new BigDecimal(a5912SqlReturn.get("F9"));
				}
				if 	(iTotal.compareTo(iComp)>0) {
					if 	(i103Total.compareTo(iComp)>0) {
						iAvg = i103Total.divide(iTotal,3,RoundingMode.HALF_UP);
						iAvg = iAvg.multiply(i100).setScale(1);
					}
				}
				
				switch (col) {
				// 處裡左右靠
				case 0:
					makeExcel.setValue(row, col + 1, a5912SqlReturn.get("F3"));
					break;
				case 1:
					makeExcel.setValue(row, col + 1, a5912SqlReturn.get("F2"));
					break;
				case 2:
					makeExcel.setValue(row, col + 1, a5912SqlReturn.get("F1"));
					break;
				case 3:
					makeExcel.setValue(row, col + 1, iTotal);
					break;	
				case 4:
					makeExcel.setValue(row, col + 1, i103Total);
					break;
				case 5:
					makeExcel.setValue(row, col + 1, String.valueOf(iAvg)+"%");
					break;
				}
			}
		}
		makeExcel.setWidth(1, 14);
		makeExcel.setWidth(2, 14);
		makeExcel.setWidth(3, 14);
		makeExcel.setWidth(4, 18);
		makeExcel.setWidth(5, 20);
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

		return sno;
	}

}
