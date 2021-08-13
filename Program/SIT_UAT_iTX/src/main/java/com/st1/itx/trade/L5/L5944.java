package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5944ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Component("L5944")
@Scope("prototype")

/**
 * 房貸專員業績統計作業－目標金額、累計目標金額查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5944 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5944.class);
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public L5944ServiceImpl iL5944ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		String iDeptCode = titaVo.getParam("DeptCode"); // 部室代號
		String iDistCode = titaVo.getParam("DistCode"); // 區部代號
		String iUnitCode = titaVo.getParam("UnitCode"); // 單位代號
		List<Map<String, String>> iL5944SqlReturn = new ArrayList<Map<String, String>>();
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		try {
			iL5944SqlReturn = iL5944ServiceImpl.FindData(iDeptCode, iDistCode, iUnitCode);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5944 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		this.info("db return = " + iL5944SqlReturn.toString());
		for (Map<String, String> r5944SqlReturn : iL5944SqlReturn) {
			OccursList occursList = new OccursList();
			for (int i = 0; i < r5944SqlReturn.size(); i++) {
				this.info("F" + i + "=" + r5944SqlReturn.get("F" + i));
			}
			if (r5944SqlReturn.get("F14").equals("0")) {
				occursList.putParam("OOWorkMonth", 0); // 統計同一年份為累計
			} else {
				occursList.putParam("OOWorkMonth", Integer.valueOf(r5944SqlReturn.get("F14")) - 191100); // 統計同一年份為累計
			}
			occursList.putParam("OOEmpNo", r5944SqlReturn.get("F6"));
			occursList.putParam("OOUnitCode", r5944SqlReturn.get("F0"));
			occursList.putParam("OODistCode", r5944SqlReturn.get("F2"));
			occursList.putParam("OODeptCode", r5944SqlReturn.get("F4"));
			occursList.putParam("OOUnitItem", r5944SqlReturn.get("F1"));
			occursList.putParam("OODistItem", r5944SqlReturn.get("F3"));
			occursList.putParam("OODeptItem", r5944SqlReturn.get("F5"));
			occursList.putParam("OODirectorCode", r5944SqlReturn.get("F8"));
			occursList.putParam("OOEmpName", r5944SqlReturn.get("F7"));
			occursList.putParam("OODepartOfficer", r5944SqlReturn.get("F9"));
			occursList.putParam("OOGoalCnt", r5944SqlReturn.get("F10"));
			occursList.putParam("OOGoalAmt", r5944SqlReturn.get("F12"));
			occursList.putParam("OOSumGoalCnt", r5944SqlReturn.get("F11"));
			occursList.putParam("OOSumGoalAmt", r5944SqlReturn.get("F13"));
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
