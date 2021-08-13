package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Component("L5021")
@Scope("prototype")

/**
 * 房貸專員業績明細資料查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5021 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5021.class);
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public PfBsOfficerService iPfBsOfficerService;
	
	@Autowired
	public PfBsDetailService iPfBsDetailService;
	
	//資料來源: 輸入欄位=年月份撈PfBsOfficerService的GoalAmt(目標金額)
	//					=工作月撈PfBsDetailService的PerfAmt(業績金額)
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iYyy = titaVo.getParam("Yyy");
		String iMm = titaVo.getParam("Mm");
		String xEmpNo = titaVo.getParam("EmpNo");
		int iYyyMm = Integer.valueOf(iYyy+iMm)+191100;
		int iYyy01 = Integer.valueOf(iYyy+"01")+191100;
		int iYyy13 = Integer.valueOf(iYyy+"13")+191100;
		this.info("起"+iYyy01);
		this.info("尾"+iYyy13);
		Slice<PfBsOfficer> iPfBsOfficer = null;
	
		if (iMm.equals("00")) {
			//查全年度
			if (xEmpNo.equals("")) {
				iPfBsOfficer = iPfBsOfficerService.findBetween(iYyy13,iYyy01, this.index, this.limit, titaVo);
			}else {
				iPfBsOfficer = iPfBsOfficerService.findByEmpNoAndRange(xEmpNo, iYyy13, iYyy01, this.index, this.limit, titaVo);
			}
		}else {
			//查單月
			if (xEmpNo.equals("")) {
				iPfBsOfficer = iPfBsOfficerService.findByMonth(iYyyMm, this.index, this.limit, titaVo);
			}else {
				iPfBsOfficer = iPfBsOfficerService.findByEmpNoAndRange(xEmpNo, iYyyMm, iYyyMm, this.index, this.limit, titaVo);
			}
			
		}
		
		if (iPfBsOfficer == null) {
			if(!iMm.equals("00")) {
				throw new LogicException(titaVo, "E0001",iYyy+"年"+iMm+"月無明細");
			}else {
				throw new LogicException(titaVo, "E0001",iYyy+"年全年無明細");
			}	
		}
		
		int divideMonth = 0;
		BigDecimal sumBsOfficerGoalAmt = new BigDecimal("0");
			
		for (PfBsOfficer xPfBsOfficer:iPfBsOfficer) {
			String iEmpNo = xPfBsOfficer.getEmpNo();
			int iWorkMonth = xPfBsOfficer.getWorkMonth();
			Slice<PfBsDetail> iPfBsDetail = null;
			iPfBsDetail = iPfBsDetailService.findBsOfficerOneMonth(iEmpNo, iWorkMonth, this.index, this.limit, titaVo);
			BigDecimal sumPerfAmt = new BigDecimal("0");
			OccursList occursList = new OccursList();
			if (divideMonth == 0) {
				divideMonth = Integer.valueOf(xPfBsOfficer.getWorkMonth())-191100;
				this.info("第一次進入="+iPfBsOfficer.getContent().size());
			}
			
			if(divideMonth != Integer.valueOf(xPfBsOfficer.getWorkMonth())-191100) {
				//若工作月與occurs裡的工作月不一致時，重製並回傳總和金額
				occursList.putParam("OODividFlag", 1);
				occursList.putParam("OOYyyMm", divideMonth);
				occursList.putParam("OOEmpNo", "");
				occursList.putParam("OOTellerNm", "目標總合");
				occursList.putParam("OOGoalAmt", sumBsOfficerGoalAmt);
				occursList.putParam("OOPerfAmt", 0);
				this.totaVo.addOccursList(occursList);
				occursList = new OccursList();
				//總和歸零為新工作月的業績金額
				sumBsOfficerGoalAmt = xPfBsOfficer.getGoalAmt();
				divideMonth = Integer.valueOf(xPfBsOfficer.getWorkMonth())-191100;
				
			}else {
				sumBsOfficerGoalAmt = sumBsOfficerGoalAmt.add(xPfBsOfficer.getGoalAmt());
			}
			occursList.putParam("OODividFlag", 0);
			occursList.putParam("OOYyyMm", Integer.valueOf(xPfBsOfficer.getWorkMonth())-191100);
			occursList.putParam("OOEmpNo", xPfBsOfficer.getEmpNo());
			occursList.putParam("OOTellerNm", xPfBsOfficer.getFullname());
			occursList.putParam("OOGoalAmt", xPfBsOfficer.getGoalAmt());
			if (iPfBsDetail == null) {
				occursList.putParam("OOPerfAmt", 0);
			}else {
				for (PfBsDetail xPfBsDetail:iPfBsDetail) {
					sumPerfAmt = sumPerfAmt.add(xPfBsDetail.getPerfAmt());
				}
				occursList.putParam("OOPerfAmt", sumPerfAmt);
			}
			
			this.totaVo.addOccursList(occursList);
		}
		OccursList occursList = new OccursList();
		occursList.putParam("OODividFlag", 1);
		occursList.putParam("OOYyyMm", divideMonth);
		occursList.putParam("OOEmpNo", "");
		occursList.putParam("OOTellerNm", "目標總合");
		occursList.putParam("OOGoalAmt", sumBsOfficerGoalAmt);
		occursList.putParam("OOPerfAmt", 0);
		this.totaVo.addOccursList(occursList);
		
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}
