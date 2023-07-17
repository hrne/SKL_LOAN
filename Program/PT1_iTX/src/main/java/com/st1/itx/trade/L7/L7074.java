package com.st1.itx.trade.L7;

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
import com.st1.itx.db.service.springjpa.cm.L7074ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L7074")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L7074 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public L7074ServiceImpl iL7074ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7074 ");
		this.totaVo.init(titaVo);

//		/*
//		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
//		 */
//		this.index = titaVo.getReturnIndex();
//
//		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
//		this.limit = 100; // 57 * 500 = 28500

		// tita
		// 會計日期
		int iAccDateStart = parse.stringToInteger(titaVo.getParam("AccDateStart")) + 19110000;
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		
		try {
			
			// *** 折返控制相關 ***
			resultList = iL7074ServiceImpl.findAcDate(iAccDateStart , titaVo);
		} catch (Exception e) {
			this.error("L7074ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}
		if (resultList != null && resultList.size() > 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */

			for (Map<String, String> result : resultList) {
			
			OccursList occurslist = new OccursList();
			
			occurslist.putParam("OOGroupId",      result.get("GroupId") );
			occurslist.putParam("OOTotalLines",   result.get("TotalLines") );
			occurslist.putParam("OOCurrencyCode", result.get("CurrencyCode") );
			occurslist.putParam("OOTotalAmount",  result.get("TotalAmount") );
			occurslist.putParam("OOSendStatus",   result.get("SendStatus") );
			occurslist.putParam("OOSendStatusX",  result.get("SendStatusX") );


			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}
		}else {
			throw new LogicException("E0001", "查無資料");
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}