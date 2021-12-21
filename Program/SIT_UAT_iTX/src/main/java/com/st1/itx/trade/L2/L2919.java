package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.springjpa.cm.L2919ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustId=X,10<br>
 * BORROWER=X,1<br>
 * END=X,1<br>
 */

@Service("L2919")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2919 extends TradeBuffer {

	@Autowired
	public L2919ServiceImpl sL2919ServiceImpl;

	@Autowired
	public CdCityService cdCityService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2919 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 10; // 122 * 400 = 48800

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = sL2919ServiceImpl.FindAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("L2919ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L2919");

		}

		if (resultList != null && resultList.size() > 0) {

			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (Map<String, String> result : resultList) {
				this.info("L2919 result = " + result.toString());
				// new occurs
				OccursList occurslist = new OccursList();

				occurslist.putParam("OOClCode1", result.get("F0"));
				occurslist.putParam("OOClCode2", result.get("F1"));
				occurslist.putParam("OOClNo", result.get("F2"));
				occurslist.putParam("OOClTypeCode", result.get("F3"));
				occurslist.putParam("OOCustName", result.get("F6"));
				occurslist.putParam("OOCustNo", result.get("F7"));

				String cityCode = result.get("F4");
				this.info("縣市 = " + cityCode);

				CdCity tCdCity = new CdCity();

				if (!(cityCode.isEmpty() || cityCode == null)) {
					/* 取縣市名稱 */
					tCdCity = cdCityService.findById(cityCode, titaVo);
					if (tCdCity == null) {
						tCdCity = new CdCity();
					}
					this.info("戶籍縣市   " + tCdCity.getCityItem());
				}

				occurslist.putParam("OOCityItem", tCdCity.getCityItem());

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occurslist);

			} // for

		} // if

		this.addList(this.totaVo);
		return this.sendList();

	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = sL2919ServiceImpl.getSize();
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