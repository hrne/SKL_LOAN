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
import com.st1.itx.db.service.springjpa.cm.L5903ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustNo=9,7<br>
 * ApplDateFrom=9,7<br>
 * ApplDateTo=9,7<br>
 * UsageCode=9,2<br>
 * ApplCode=9,2<br>
 * END=X,1<br>
 */

@Service("L5903")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5903 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5903.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5903ServiceImpl l5903ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5903 ");
		this.totaVo.init(titaVo);


//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;
		
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = l5903ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l4920ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultList != null && resultList.size() > 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
			
			for (Map<String, String> result : resultList) {
				
				int applDate = parse.stringToInteger(result.get("F6"));
				int returnDate = parse.stringToInteger(result.get("F7"));
				
				if(applDate > 19110000) {
					applDate = applDate - 19110000;
				}
				
				if(returnDate > 19110000) {
					returnDate = returnDate - 19110000;
				}

				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo", result.get("F0"));
				occursList.putParam("OOFacmNo", result.get("F1"));
				occursList.putParam("OOApplSeq", result.get("F2"));
				occursList.putParam("OOCustName", result.get("F3"));
				occursList.putParam("OOKeeperEmpNo", result.get("F4"));
				occursList.putParam("OOApplEmpNo", result.get("F5"));
				occursList.putParam("OOApplDate", applDate);
				occursList.putParam("OOReturnDate", returnDate);
				occursList.putParam("OOReturnEmpNo", result.get("F8"));
				occursList.putParam("OOUsageCode", result.get("F9"));
				occursList.putParam("OOCopyCode", result.get("F10"));
				occursList.putParam("OORemark", result.get("F11"));
				occursList.putParam("OOApplObj", result.get("F12"));

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
	
	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l5903ServiceImpl.getSize();
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