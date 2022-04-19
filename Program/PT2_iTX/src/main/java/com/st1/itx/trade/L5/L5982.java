package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.db.service.springjpa.cm.L5982ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * END=X,1<br>
 */

@Service("L5982") // 國稅局申報檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author ZhiCheng
 * @version 1.0.0
 */

public class L5982 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5982.class);

	/* DB服務注入 */
	@Autowired
	public YearlyHouseLoanIntService sYearlyHouseLoanIntService;

	@Autowired
	Parse parse;

	@Autowired
	L5982ServiceImpl l5982ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L5982 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 85 * 100=

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = l5982ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l5982ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultList != null && resultList.size() > 0) {

			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();

				occursList.putParam("OOYearMonth", Integer.parseInt(result.get("F0")) - 191100);
				occursList.putParam("OOCustNo", result.get("F1"));
				occursList.putParam("OOFacmNo", result.get("F2"));
				occursList.putParam("OOUsageCode", result.get("F3"));
				occursList.putParam("OOAcctCode", result.get("F4"));
				occursList.putParam("OORepayCode", result.get("F5"));
				occursList.putParam("OOLoanAmt", result.get("F6"));
				occursList.putParam("OOLoanBal", result.get("F7"));

				int iFirstDrawdownDate = 0;
				if (Integer.parseInt(result.get("F8")) != 0) {
					iFirstDrawdownDate = Integer.parseInt(result.get("F8")) - 19110000;
				}
				occursList.putParam("OOFirstDrawdownDate", iFirstDrawdownDate);

				int iMaturityDate = 0;
				if (Integer.parseInt(result.get("F9")) != 0) {
					iMaturityDate = Integer.parseInt(result.get("F9")) - 19110000;
				}
				occursList.putParam("OOMaturityDate", iMaturityDate);
				occursList.putParam("OOYearlyInt", result.get("F10"));

				int iHouseBuyDate = 0;
				if (Integer.parseInt(result.get("F11")) != 0) {
					iHouseBuyDate = Integer.parseInt(result.get("F11")) - 19110000;
				}
				occursList.putParam("OOHouseBuyDate", iHouseBuyDate);
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resultList != null && resultList.size() >= this.limit) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		} else {
			throw new LogicException(titaVo, "E0001", "每年房屋擔保借款繳息工作檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

//	private Boolean hasNext() {
//		Boolean result = true;
//
//		int times = this.index + 1;
//		int cnt = l5982ServiceImpl.getSize();
//		int size = times * this.limit;
//
//		this.info("index ..." + this.index);
//		this.info("times ..." + times);
//		this.info("cnt ..." + cnt);
//		this.info("size ..." + size);
//
//		if (size == cnt) {
//			result = false;
//		}
//		this.info("result ..." + result);
//
//		return result;
//	}

}