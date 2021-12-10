package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L4603Report1
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L4603Report extends MakeReport {

	@Autowired
	DateUtil dateUtil;

	@Autowired
	BaTxCom baTxCom;

	@Autowired
	Parse parse;

	@Autowired
	public CustMainService custMainService;
	
	@Autowired
	public ClBuildingService clBuildingService;
	
	@Autowired
	public LoanBorMainService loanBorMainService;
	
	private int specificDd = 0;
	
	@Override
	public void printHeader() {

		this.info("L4603Report1.printHeader");

		this.setFontSize(15);

		printHeaderP();
		// 明細起始列(自訂亦必須)
		this.setBeginRow(2);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(60);

	}

	public void printHeaderP() {
	
	}
	
	public void exec(TitaVo titaVo, InsuRenew tInsuRenew, TxBuffer txbuffer) throws LogicException {
		this.info("L4603Report exec");

		int iEntryDate = 0;
		String sEntryDate = "";
		int iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		
		CustMain tCustMain = new CustMain();
		DecimalFormat df1 = new DecimalFormat("#,##0");

		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);
		
		checkC(tInsuRenew.getCustNo(), tInsuRenew.getFacmNo(), titaVo);
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L4603", "火險及地震險保費_繳款通知單", "火險及地震險保費_繳款通知單", "A4", "P");
		
		this.print(1, 38,"火險及地震險保費 繳款通知單","C");
		
		this.setFontSize(11);
		this.print(5, 8,"親愛的房貸客戶您好：");
		this.print(1, 12,"承蒙您於本公司辦理房屋抵押貸款，謹致謝忱！");
		this.print(1, 12,"您的火險及地震險保單即將到期（內容請參照保費明細表），本公司將於每月房貸期款併同收取");
		this.print(1, 8,"火險及地震險保費，並向新光產物保險（股）公司辦理續保手續。");
		this.print(1, 8,"一、採銀行（郵局）自動扣繳期款者，敬請貴貸戶留意增加火險及地震險保費金額。");
		this.print(1, 8,"二、自行轉帳匯款者，敬請留意增加火險及地震險保費金額（匯款方式如下說明）。");
		this.print(1, 8,"謹先奉達，並頌安祺！");
		this.print(1, 8,"備註：1.依據財政部91.01.25台財保字第0910750050號函辦理，自91.04.01起推行｢新住宅火險");
		this.print(1, 15,"及地震險｣方案，住宅火險自動涵蓋地震險，並一律為一年期。");
		this.print(1, 13,"2.依據台端所簽具火險及地震險續保切結書之內文，保險費與貸款期款一併繳交，如保險費");
		this.print(1, 16,"有未全額繳交情形，本公司得自所繳交期款中優先扣取。");
		
		this.setFontSize(13);
		this.print(1, 50,"新光人壽保險股份有限公司 敬啟");
		
		this.setFontSize(15);
		this.print(4, 38,"火險及地震險保費明細表","C");
		
		
		this.setFontSize(12);
		this.print(9, 9,"貸款戶號："  + tInsuRenew.getCustNo());
		
		this.print(0, 65,"銀行扣款");
		
		this.print(2, 9,"客戶名稱：" + tCustMain.getCustName());
		this.print(2, 9,"擔保品地址：" + getBdLocation(tInsuRenew, titaVo));
		this.print(2, 9,"屆期保單號碼：" + tInsuRenew.getNowInsuNo());
		
		String InsuStartDate = "";
		String InsuEndDate = "";
		
		if(String.valueOf(tInsuRenew.getInsuStartDate()).length() == 7) {
			InsuStartDate = String.valueOf(tInsuRenew.getInsuStartDate()).substring(0,3) + "/" +
		    String.valueOf(tInsuRenew.getInsuStartDate()).substring(3,5) + "/" +
		    String.valueOf(tInsuRenew.getInsuStartDate()).substring(5,7);
		} else {
			InsuStartDate = String.valueOf(tInsuRenew.getInsuStartDate()).substring(0,2) + "/" +
		 	String.valueOf(tInsuRenew.getInsuStartDate()).substring(2,4) + "/" +
			String.valueOf(tInsuRenew.getInsuStartDate()).substring(4,6);
		}
		
		if(String.valueOf(tInsuRenew.getInsuEndDate()).length() == 7) {
			InsuEndDate = String.valueOf(tInsuRenew.getInsuEndDate()).substring(0,3) + "/" +
		    String.valueOf(tInsuRenew.getInsuEndDate()).substring(3,5) + "/" +
		    String.valueOf(tInsuRenew.getInsuEndDate()).substring(5,7);
		} else {
			InsuEndDate = String.valueOf(tInsuRenew.getInsuEndDate()).substring(0,2) + "/" +
		 	String.valueOf(tInsuRenew.getInsuEndDate()).substring(2,4) + "/" +
			String.valueOf(tInsuRenew.getInsuEndDate()).substring(4,6);
		}
		this.print(2, 9,"保單到期日：" + InsuStartDate);
		this.print(2, 9,"續保期間：" + InsuStartDate +  " - "  + InsuEndDate);
		
		this.print(0, 40,"火險保費：");
		this.print(0, 60, df1.format(tInsuRenew.getFireInsuPrem()), "R");
		
		this.print(2, 9,"火險保額：");
		this.print(0, 34,df1.format(tInsuRenew.getFireInsuCovrg()), "R");
		
		this.print(0, 40,"地震險保費：");
		this.print(0, 60, df1.format(tInsuRenew.getEthqInsuPrem()), "R");
		
		this.print(2, 9,"地震險保額：");
		this.print(0, 34,df1.format(tInsuRenew.getEthqInsuCovrg()), "R");
		
		this.print(0, 40,"總保費：");
		this.print(0, 60, df1.format(tInsuRenew.getTotInsuPrem()), "R");
		
		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		dateUtil.init();
		dateUtil.setDate_1(iInsuEndMonth * 100 + 01);
		dateUtil.setMons(0);
		dateUtil.getCalenderDay();
		if (specificDd > dateUtil.getDays()) {
			specificDd = dateUtil.getDays();
		}
		
		iEntryDate = parse.stringToInteger("" + iInsuEndMonth + specificDd) - 19110000;
		
		if(String.valueOf(iEntryDate).length() == 7) {
			sEntryDate = String.valueOf(iEntryDate).substring(0,3) + "/" +
		    String.valueOf(iEntryDate).substring(3,5) + "/" +
		    String.valueOf(iEntryDate).substring(5,7);
		} else {
			sEntryDate = String.valueOf(iEntryDate).substring(0,2) + "/" +
		 	String.valueOf(iEntryDate).substring(2,4) + "/" +
			String.valueOf(iEntryDate).substring(4,6);
		}
		
		this.print(0, 61, "（應繳日：" + sEntryDate + "）");
		this.print(2, 9,"匯款帳號：" + "9510200" + FormatUtil.pad9("" + tInsuRenew.getCustNo(),7));
		long sno = this.close();
		this.toPdf(sno,"火險及地震險保費-繳款通知單");
		
	}
	
	private String getBdLocation(InsuRenew tInsuRenew, TitaVo titaVo) {
		String address = "";
		ClBuildingId tClBuildingId = new ClBuildingId();
		tClBuildingId.setClCode1(tInsuRenew.getInsuRenewId().getClCode1());
		tClBuildingId.setClCode2(tInsuRenew.getInsuRenewId().getClCode2());
		tClBuildingId.setClNo(tInsuRenew.getInsuRenewId().getClNo());
		ClBuilding tClBuilding = new ClBuilding();
		tClBuilding = clBuildingService.findById(tClBuildingId, titaVo);

		if (tClBuilding != null) {
			address = tClBuilding.getBdLocation();
		}
		return address;
	}
	
//	火險應繳日跟著期款->額度內>0、最小之應繳日
	private void checkC(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		// 未撥款或已結案
		specificDd = 01;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(custNo, facmNo, facmNo, 0, 900, this.index,
				this.limit, titaVo);
		if (slLoanBorMain != null) {
			for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
				if (tLoanBorMain.getLoanBal().compareTo(BigDecimal.ZERO) > 0) {
					specificDd = tLoanBorMain.getSpecificDd();
				}
			}
		}
		
	}
}
