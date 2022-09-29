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
import com.st1.itx.db.service.springjpa.cm.L5905ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.format.FormatUtil;

/**
 * Tita InqFg=9,1 YearMonth=9,5 ConditionCode=9,2 YearMonthS=9,5 YearMonthE=9,5
 * ReChkMonth=9,2 CustNo=9,7 END=X,1
 */

@Service("L5905") // 覆審案件明細檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Fegie
 * @version 1.0.0
 */

public class L5905 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	L5905ServiceImpl l5905Servicelmpl;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5905 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int wkFYearMonth = 0;
		int wkFReChkYearMonth = 0;
		int iTraceMonth = 0;
		int itempdate = 0;
		String sConditionCode = "";
		int iConditionCode = 0;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit =  100; // 361 * 100 = 34,900

		// 查詢覆審案件明細檔
		List<Map<String, String>> resultList = null;
		try {
			resultList = l5905Servicelmpl.queryresult(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l5905Servicelmpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (this.index == 0 && (resultList == null || resultList.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "覆審案件明細檔"); // 查無資料
		}

		// 如有找到資料
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {

				OccursList occursList = new OccursList();

				occursList.putParam("OOCustName", result.get("CustName"));

				wkFYearMonth = parse.stringToInteger(result.get("YearMonth"));
				if (wkFYearMonth == 0) {
					occursList.putParam("OOYearMonth", 0);
				} else {
					wkFYearMonth = wkFYearMonth - 191100;
					occursList.putParam("OOYearMonth", wkFYearMonth);
				}
				wkFReChkYearMonth = parse.stringToInteger(result.get("ReChkYearMonth"));
				if (!(wkFReChkYearMonth == 0)) {
					wkFReChkYearMonth = wkFReChkYearMonth - 191100;
				}
				occursList.putParam("OOReChkYearMonth", wkFReChkYearMonth);
				
				iConditionCode = parse.stringToInteger(result.get("ConditionCode"));
				if (iConditionCode < 10) {
					sConditionCode = "0" + result.get("ConditionCode");
				}else {
					sConditionCode = result.get("ConditionCode");
				}
				occursList.putParam("OOConditionCode", sConditionCode);
				occursList.putParam("OOCustNo", parse.stringToInteger(result.get("CustNo")));
				occursList.putParam("OOFacmNo", parse.stringToInteger(result.get("FacmNo")));
				occursList.putParam("OOReCheckCode", parse.stringToInteger(result.get("CheckCode")));
				occursList.putParam("OOFollowMark", parse.stringToInteger(result.get("FollowMark")));

				itempdate = parse.stringToInteger(result.get("DrawdownDate")) ;
				if (itempdate == 0) {
					occursList.putParam("OODrawdownDate", 0);
				} else {
					occursList.putParam("OODrawdownDate", itempdate - 19110000);
				}

				occursList.putParam("OOLoanBal", parse.stringToInteger(result.get("LoanBal")));
				occursList.putParam("OOEvaluation", parse.stringToInteger(result.get("Evaluation")));
				occursList.putParam("OOCustTypeItem", result.get("CustTypeItem)"));
				occursList.putParam("OOUsageItem", result.get("UsageItem)"));
				occursList.putParam("OOCityItem", result.get("CityItem)"));
				occursList.putParam("OOReChkUnit", result.get("ReChkUnit)"));
				occursList.putParam("OORemark", result.get("Remark)"));

				iTraceMonth = parse.stringToInteger(result.get("TraceMonth"));
				if (iTraceMonth == 0) {
					occursList.putParam("OOTraceYearMonth", 0);
				} else {
					occursList.putParam("OOTraceYearMonth", iTraceMonth - 191100);
				}
				occursList.putParam("OOSpecifyFg", parse.stringToInteger(result.get("SpecifyFg")));
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */

			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l5905Servicelmpl.getSize();
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
