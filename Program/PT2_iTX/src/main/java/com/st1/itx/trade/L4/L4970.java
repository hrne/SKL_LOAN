package com.st1.itx.trade.L4;

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
import com.st1.itx.db.service.springjpa.cm.L4970ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4970")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L4970 extends TradeBuffer {

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	/* DB服務注入 */
	@Autowired
	public L4970ServiceImpl l4970ServiceImpl;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4970 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200;
		
		int startDate = parse.stringToInteger(titaVo.get("StartDate"))+19110000;
		
		int endDate = parse.stringToInteger(titaVo.get("EndDate"))+19110000;
		
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		try {
			// *** 折返控制相關 ***
			resultList = l4970ServiceImpl.findAll(startDate,endDate,titaVo);
		} catch (Exception e) {
			this.error("l4970ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultList != null && resultList.size() > 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */

			for (Map<String, String> result : resultList) {

				OccursList occursList = new OccursList();

				int insuMonth = parse.stringToInteger(result.get("InsuYearMonth"));
				int insuStartDate = parse.stringToInteger(result.get("InsuStartDate"));
				int insuEndDate = parse.stringToInteger(result.get("InsuEndDate"));

				occursList.putParam("OOInsuYearMonth", insuMonth);
				occursList.putParam("OOPrevInsuNo", result.get("PrevInsuNo"));
				occursList.putParam("OONowInsuNo", result.get("NowInsuNo"));
				occursList.putParam("OOInsuStartDate", insuStartDate);
				occursList.putParam("OOInsuEndDate", insuEndDate);
				occursList.putParam("OOCustNo", result.get("CustNo")+"-"+result.get("FacmNo"));
				occursList.putParam("OOCustName", result.get("CustName"));
				occursList.putParam("OOTotInsuPrem", result.get("TotInsuPrem"));
				occursList.putParam("OORepayCode", result.get("RepayCode"));
				occursList.putParam("OOStatusCode", result.get("StatusCode"));
				occursList.putParam("OOTavBal", result.get("TavBal"));

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		}
//		else {
//			throw new LogicException("E0001","查無資料");
//		}

		this.addList(this.totaVo);

		return this.sendList();

	}
}