package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.GraceConditionService;
import com.st1.itx.db.service.springjpa.cm.L5906ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5906")
@Scope("prototype")
/**
 * 寬限條件控管繳息查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5906 extends TradeBuffer {

	@Autowired
	public GraceConditionService iGraceConditionService;

	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	L5906ServiceImpl l5906ServiceImpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5906 ");
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		this.totaVo.init(titaVo);
		
		List<Map<String, String>> resultList = null;
		
		try {
			resultList = l5906ServiceImpl.findAll(this.index,this.limit,titaVo);
		} catch (Exception e) {
			throw new LogicException("E0013", e.getMessage());
		}
		
		
//		if (iGraceCondition != null && iGraceCondition.hasNext()) {
//			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
//			titaVo.setReturnIndex(this.setIndexNext());
//			// this.totaVo.setMsgEndToAuto();// 自動折返
//			this.totaVo.setMsgEndToEnter();// 手動折返
//		}
		if (resultList == null || resultList.size()==0) {
			throw new LogicException(titaVo, "E0001", "");
		} else {
			
			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
			
			
			for (Map<String, String> result : resultList) {
				
				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo", result.get("F0"));
				occursList.putParam("OOCustName", result.get("F1"));
				occursList.putParam("OOFacmNo", result.get("F2"));
				occursList.putParam("OOActUse", result.get("F6"));
				
				occursList.putParam("OOPrevPayIntDate", showDate(result.get("F3"),1));
			
				occursList.putParam("OOGraceDate", showDate(result.get("F4"),1));
				
				occursList.putParam("OOLoanBal", result.get("F5"));
				
				
				this.totaVo.addOccursList(occursList);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
	private String showDate(String date, int iType) {
		this.info("date=="+date);
		if (date == null || date.equals("") || date.equals("0") || date.equals(" ")) {
			return " ";
		}
		int rocdate = Integer.valueOf(date);
		if (rocdate > 19110000) {
			rocdate -= 19110000;
		}
		String rocdatex = String.valueOf(rocdate);

		return rocdatex;
	}
	
	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l5906ServiceImpl.getSize();
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