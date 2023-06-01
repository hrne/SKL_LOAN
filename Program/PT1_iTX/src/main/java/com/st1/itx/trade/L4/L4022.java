package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCityRate;
import com.st1.itx.db.service.CdCityRateService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4022") // 地區利率查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L4022 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCityRateService cdCityRateService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4022 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iWorkMonth = Integer.valueOf(titaVo.getParam("WorkMonth")) + 191100;
		int AcDate = titaVo.getEntDyI() + 19110000;
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 168 * 200 = 33,600
		int iWorkMonthS = 0;
		int iWorkMonthE = 0;
		if (iWorkMonth == 191100) {
			iWorkMonthS = 0;
			iWorkMonthE = 999999;
		} else {
			iWorkMonthS = iWorkMonth;
			iWorkMonthE = iWorkMonth;
		}

		Slice<CdCityRate> slCdCityRate;
		slCdCityRate = cdCityRateService.findEffectDateRange(iWorkMonthS, iWorkMonthE, index, limit, titaVo);

		if (slCdCityRate == null) {
			throw new LogicException(titaVo, "E0001", "地區利率檔"); // 查無資料
		}
		// 如有找到資料
		ArrayList<Integer> iWorkMonthList = new ArrayList<>();
		for (CdCityRate tCdCityRate : slCdCityRate.getContent()) {
			this.info(" tCdCityRate.getEffectYYMM() = " + tCdCityRate.getEffectYYMM());
			if (iWorkMonthList.contains(tCdCityRate.getEffectYYMM())) {
				continue;
			} else {
				/* 將每筆資料放入Tota的OcList */

				iWorkMonthList.add(0, tCdCityRate.getEffectYYMM());
			}
		}
		this.info(" iWorkMonthList .getEffectYYMM() before= " + iWorkMonthList);
		Collections.sort(iWorkMonthList,Collections.reverseOrder());
		this.info(" iWorkMonthList .getEffectYYMM() after = " + iWorkMonthList);
		for (int reWorkMonth : iWorkMonthList) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOWorkMonth", reWorkMonth - 191100);

			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdCityRate != null && slCdCityRate.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}