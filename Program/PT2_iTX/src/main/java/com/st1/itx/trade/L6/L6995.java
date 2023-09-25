package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.CdStock;
import com.st1.itx.db.service.CdStockService;

/**
 * Tita<br>
 */

@Service("L6995") // 各類代碼檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */

public class L6995 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdStockService sCdStockService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6995 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iStockCode = titaVo.getParam("StockCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; // 217 * 200 = 43400

		if (!iStockCode.equals("")) {
			CdStock lCdStock2 = sCdStockService.findById(iStockCode, titaVo);
			if(lCdStock2 == null) {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}
			OccursList occursList = new OccursList();
			occursList.putParam("OOStockCode", lCdStock2.getStockCode());
			occursList.putParam("OOStockItem", lCdStock2.getStockItem());
			occursList.putParam("OOStockCompanyName", lCdStock2.getStockCompanyName());
			occursList.putParam("OOCurrency", lCdStock2.getCurrency());
			occursList.putParam("OOYdClosePrice", lCdStock2.getYdClosePrice());
			occursList.putParam("OOMonthlyAvg", lCdStock2.getMonthlyAvg());
			occursList.putParam("OOThreeMonthAvg", lCdStock2.getThreeMonthAvg());
			occursList.putParam("OOStockType", lCdStock2.getStockType());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		} else {
			List<CdStock> lCdStock = new ArrayList<CdStock>();
			Slice<CdStock> L6995DateList = null;

			try {

				L6995DateList = sCdStockService.findAll(this.index, this.limit, titaVo);

			} catch (Exception e) {
				throw new LogicException(titaVo, "E0001", "SQL ERROR");
			}

			lCdStock = L6995DateList == null ? null : L6995DateList.getContent();
			this.info("count   = " + L6995DateList.getContent());

			for (CdStock t : lCdStock) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOStockCode", t.getStockCode());
				occursList.putParam("OOStockItem", t.getStockItem());
				occursList.putParam("OOStockCompanyName", t.getStockCompanyName());
				occursList.putParam("OOCurrency", t.getCurrency());
				occursList.putParam("OOYdClosePrice", t.getYdClosePrice());
				occursList.putParam("OOMonthlyAvg", t.getMonthlyAvg());
				occursList.putParam("OOThreeMonthAvg", t.getThreeMonthAvg());
				occursList.putParam("OOStockType", t.getStockType());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

			if (lCdStock != null && lCdStock.size() >= this.limit) {
				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				titaVo.setReturnIndex(this.setIndexNext());
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}