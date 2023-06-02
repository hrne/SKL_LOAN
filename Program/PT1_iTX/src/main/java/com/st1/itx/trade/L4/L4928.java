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
import com.st1.itx.db.service.springjpa.cm.L4928ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4928") // 同額度不同繳息迄日查詢
@Scope("prototype")
/**
 *
 *
 * @author Linda
 * @version 1.0.0
 */

public class L4928 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	Parse parse;

	@Autowired
	L4928ServiceImpl l4928Servicelmpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4928 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = 0;

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; //

		List<Map<String, String>> resultList = null;
		try {
			resultList = l4928Servicelmpl.queryresult(this.index, this.limit, titaVo);

		} catch (Exception e) {
			this.error("l4928Servicelmpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (this.index == 0 && (resultList == null || resultList.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "撥款主檔"); // 查無資料
		}

		// 如有找到資料
		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				OccursList occursList = new OccursList();
				int prevPayIntDate = parse.stringToInteger(result.get("PrevPayIntDate"));
				if (prevPayIntDate > 19110000) {
					prevPayIntDate = prevPayIntDate - 19110000;
				}
				occursList.putParam("OOCustNo", result.get("CustNo")); // 戶號
				occursList.putParam("OOFacmNo", result.get("FacmNo")); // 額度
				occursList.putParam("OOBormNo", result.get("BormNo")); // 撥款
				occursList.putParam("OOPrevPayIntDate", prevPayIntDate); // 繳息迄日

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
		int cnt = l4928Servicelmpl.getSize();
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
