package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L2023ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2023")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L2023 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public L2023ServiceImpl sL2023ServiceImpl;

	/* 轉換工具 */
	@Autowired
	public Parse parse;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2023 ");
		this.totaVo.init(titaVo);


		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 50; // 9 * 15 * 376 = 50760 1次最多9筆occurs

		
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = sL2023ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("L2023ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L2023");

		}
		List<LinkedHashMap<String, String>> chkOccursList = null;
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				if(parse.stringToInteger(result.get("CreditSysNo")) == 0) {
					occursList.putParam("oCreditSysNo", "");
				} else {
					occursList.putParam("oCreditSysNo", result.get("CreditSysNo"));
				}
				occursList.putParam("oApplNo", result.get("ApplNo"));
				occursList.putParam("oCustNo", result.get("CustNo"));
				occursList.putParam("oFacmNo", result.get("FacmNo"));
				occursList.putParam("oCustName", result.get("CustName"));
				occursList.putParam("oUKey", result.get("UKey"));
				occursList.putParam("oId", result.get("Id"));
				occursList.putParam("oName", result.get("Name"));
				occursList.putParam("oType", result.get("Type"));
				occursList.putParam("oRelation", result.get("Relation"));
				occursList.putParam("oClCode1", result.get("ClCode1"));
				occursList.putParam("oClCode2", result.get("ClCode2"));
				occursList.putParam("oClNo", result.get("ClNo"));
				occursList.putParam("oModify", result.get("Modify"));

				this.totaVo.addOccursList(occursList);
				
			}
			
			chkOccursList = this.totaVo.getOccursList();

			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		}
		
		
		
		if (chkOccursList == null && titaVo.getReturnIndex() == 0) {
			throw new LogicException("E2003", ""); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = sL2023ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}
}