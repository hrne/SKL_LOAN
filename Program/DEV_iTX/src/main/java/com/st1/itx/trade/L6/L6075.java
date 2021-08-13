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
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita CityCode=X,2 AreaCode=X,3 END=X,1
 */

@Service("L6075") // 地區別與鄉鎮區對照檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6075 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6075.class);

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6075 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iCityCode = titaVo.getParam("CityCode");
		String iAreaCode = titaVo.getParam("AreaCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 48 * 200 = 9600

		// 查詢地區別與鄉鎮區對照檔
		Slice<CdArea> slCdArea;
		if (iCityCode.isEmpty() || iCityCode.equals("00")) {
			slCdArea = sCdAreaService.findAll(this.index, this.limit, titaVo);
		} else {
			if (iAreaCode.isEmpty() || iAreaCode.equals("00")) {
				slCdArea = sCdAreaService.areaCodeRange(iCityCode, iCityCode, "00", "ZZ", this.index, this.limit, titaVo);
			} else {
				slCdArea = sCdAreaService.areaCodeRange(iCityCode, iCityCode, iAreaCode, iAreaCode, this.index, this.limit, titaVo);
			}
		}
		List<CdArea> lCdArea = slCdArea == null ? null : slCdArea.getContent();

		if (lCdArea == null || lCdArea.size() == 0) {
			throw new LogicException(titaVo, "E0001", "地區別與鄉鎮區對照檔"); // 查無資料
		}
		// 如有找到資料
		for (CdArea tCdArea : lCdArea) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCityCode", tCdArea.getCityCode());
			occursList.putParam("OOAreaCode", tCdArea.getAreaCode());
			occursList.putParam("OOAreaItem", tCdArea.getAreaItem());

			// 查詢地區別代碼檔
			CdCity tCdCity = new CdCity();
			tCdCity = sCdCityService.findById(tCdArea.getCityCode(), titaVo);
			/* 必須先建立地區別代碼檔資料 */
			if (tCdCity != null) {
				occursList.putParam("OOCityItem", tCdCity.getCityItem());
			} else {
				occursList.putParam("OOCityItem", "");
			}

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdArea != null && slCdArea.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}