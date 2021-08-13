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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
/* DB服務 */
import com.st1.itx.db.service.springjpa.cm.L4042ServiceImpl;
/* DB容器 */

/**
 * Tita<br>
 * SearchFlag=9,1<br>
 * DateFrom=9,7<br>
 * DateTo=9,7<br>
 * CustNo=9,7<br>
 * RepayAcct=9,14<br>
 * RepayBank=9,3<br>
 * END=X,1<br>
 */

@Service("L4042")
@Scope("prototype")
public class L4042 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4042.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L4042ServiceImpl l4042ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4042 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l4042ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (resultList != null && resultList.size() != 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				int authCreateDate = parse.stringToInteger(result.get("F8"));
				int propDate = parse.stringToInteger(result.get("F9"));
				int retrDate = parse.stringToInteger(result.get("F10"));
				int stampFinishDate = parse.stringToInteger(result.get("F16"));
				int deleteDate = parse.stringToInteger(result.get("F17"));

				if (authCreateDate > 19110000) {
					authCreateDate = authCreateDate - 19110000;
				}
				if (propDate > 19110000) {
					propDate = propDate - 19110000;
				}
				if (retrDate > 19110000) {
					retrDate = retrDate - 19110000;
				}
				if (stampFinishDate > 19110000) {
					stampFinishDate = stampFinishDate - 19110000;
				}
				if (deleteDate > 19110000) {
					deleteDate = deleteDate - 19110000;
				}
				occursList.putParam("OOCustNo", result.get("F0"));
				occursList.putParam("OOFacmNo", result.get("F1"));
				occursList.putParam("OOAuthType", result.get("F2"));
				occursList.putParam("OORepayBank", result.get("F3"));
				occursList.putParam("OORepayAcct", result.get("F4"));
				occursList.putParam("OOStatus", result.get("F5"));
				occursList.putParam("OOLimitAmt", result.get("F6"));
				occursList.putParam("OOCreateFlag", result.get("F7"));
				occursList.putParam("OOAuthCreateDate", authCreateDate);
				occursList.putParam("OOPropDate", propDate);
				occursList.putParam("OORetrDate", retrDate);
				occursList.putParam("OOAuthStatus", result.get("F11"));
				occursList.putParam("OOMediaCode", result.get("F12"));
				occursList.putParam("OOAmlRsp", result.get("F13"));
				occursList.putParam("OORepayBankLog", result.get("F14"));
				occursList.putParam("OORepayAcctLog", result.get("F15"));
				occursList.putParam("OOStampFinishDate", stampFinishDate);
				occursList.putParam("OODeleteDate", deleteDate);
//				暫無用處
				occursList.putParam("OOButtenFlagA", result.get("F18"));

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
		int cnt = l4042ServiceImpl.getSize();
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