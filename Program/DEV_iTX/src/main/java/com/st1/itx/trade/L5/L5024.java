package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;	
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5024ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Component("L5024")
@Scope("prototype")

/**
 * 房貸專員業績統計作業－目標金額、累計目標金額查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5024 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5024.class);
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public L5024ServiceImpl iL5024ServiceImpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iDeptCode = titaVo.getParam("DeptCode"); //部室代號
		String iDistCode = titaVo.getParam("DistCode"); //區部代號
		String iUnitCode = titaVo.getParam("UnitCode"); //單位代號
		List<Map<String, String>> iL5024SqlReturn = new ArrayList<Map<String,String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;
		
		try {
			iL5024SqlReturn = iL5024ServiceImpl.FindData(this.index,this.limit,iDeptCode,iDistCode,iUnitCode,titaVo);
		}catch (Exception e) {
			//E5004 讀取DB語法發生問題
			this.info("L5024 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		this.info("db return = "+iL5024SqlReturn.toString());
		if(iL5024SqlReturn.size()==0) {
			throw new LogicException(titaVo, "E0001","業績目標檔查無資料");
		}
		
		if(iL5024SqlReturn!=null && iL5024SqlReturn.size()>=this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			//this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		
		for (Map<String, String> r5024SqlReturn:iL5024SqlReturn) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOEmpNo", r5024SqlReturn.get("F6"));
				occursList.putParam("OOUnitCode", r5024SqlReturn.get("F0"));
				occursList.putParam("OODistCode", r5024SqlReturn.get("F2"));
				occursList.putParam("OODeptCode", r5024SqlReturn.get("F4"));
				occursList.putParam("OOUnitItem", r5024SqlReturn.get("F1"));
				occursList.putParam("OODistItem", r5024SqlReturn.get("F3"));
				occursList.putParam("OODeptItem", r5024SqlReturn.get("F5"));
				occursList.putParam("OODirectorCode", r5024SqlReturn.get("F8"));
				occursList.putParam("OOEmpName", r5024SqlReturn.get("F7"));
				occursList.putParam("OODepartOfficer", r5024SqlReturn.get("F9")); 	
				occursList.putParam("OOGoalCnt", r5024SqlReturn.get("F10"));
				occursList.putParam("OOGoalAmt", r5024SqlReturn.get("F12"));
				occursList.putParam("OOSumGoalCnt", r5024SqlReturn.get("F11"));
				occursList.putParam("OOSumGoalAmt", r5024SqlReturn.get("F13"));
				this.totaVo.addOccursList(occursList);
			}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}
