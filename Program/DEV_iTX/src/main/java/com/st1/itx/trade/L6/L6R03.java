package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R03")
@Scope("prototype")
/**
 * 
 * 取得縣市別/行政區/地段/路名的下拉選單資料
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L6R03 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R03.class);

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdAreaService sCdAreaService;
	@Autowired
	public CdLandSectionService sCdLandSectionService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R03 ");
		this.totaVo.init(titaVo);

		// FunCd = 1 , 取縣市別
		// = 2 , 取行政區 (需傳入縣市別代碼)
		// = 3 , 取地段 (需傳入縣市別代碼 & 行政區代碼)
		// = 4 , 取路名 (需傳入縣市別代碼 & 行政區代碼)

		/* Tita 取值 */
		/* Tita-參數1:FunCd */
		int funcd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		/* Tita-參數2:CityCode */
		String cityCode = titaVo.getParam("RimCityCode");
		/* Tita-參數3:AreaCode */
		String areaCode = titaVo.getParam("RimAreaCode");

		/* ShowLog */
		this.info("L6R03 Tita Funcd = " + funcd);
		this.info("L6R03 Tita CityCode = " + cityCode);
		this.info("L6R03 Tita AreaCode = " + areaCode);

		/* 宣告下拉選單字串 */
		String comboBoxString = "";

		/* FunCd 判斷 , 並組成下拉選單字串 格式為 Code:Item; */
		switch (funcd) {
		case 1:
			/* FunCd = 1 , 取縣市別 */
			Slice<CdCity> slCdCity = sCdCityService.findAll(0, Integer.MAX_VALUE, titaVo);
			List<CdCity> lCdCity = slCdCity == null ? null : new ArrayList<CdCity>(slCdCity.getContent());
			if (lCdCity == null || lCdCity.size() == 0) {
				throw new LogicException("E6015", "地區別代碼檔");
			}

			/* 依縣市別代碼排序 */
			lCdCity.sort((c1, c2) -> {
				if (c2 == null) {
					return 0;
				}
				return c1.getCityCode().compareTo(c2.getCityCode());
			});

			for (int i = 0; i < lCdCity.size(); i++) {
				CdCity tCdCity = lCdCity.get(i);
				String resultCityCode = tCdCity.getCityCode().trim();
				String resultCityItem = tCdCity.getCityItem().trim();

				if (i < lCdCity.size() - 1) {
					comboBoxString += resultCityCode + ":" + resultCityItem + ";";
				} else {
					/* 最後一個不加分號 */
					comboBoxString += resultCityCode + ":" + resultCityItem;
				}
			}
			this.info("Funcd = 1 , 取縣市別,下拉選單字串 = " + comboBoxString);
			break;
		case 2:
			/* FunCd = 2 , 取行政區 (需傳入縣市別代碼) */
			if (cityCode.equals("00")) {
				throw new LogicException("E6102", "L6R03");
			}

			Slice<CdArea> slCdArea = sCdAreaService.cityCodeEq(cityCode, cityCode, 0, Integer.MAX_VALUE, titaVo);
			List<CdArea> lCdArea = slCdArea == null ? null : new ArrayList<CdArea>(slCdArea.getContent());
			if (lCdArea == null || lCdArea.size() == 0) {
				throw new LogicException("E6102", "L6R03");
			}

			/* 依行政區代碼排序 */
			lCdArea.sort((c1, c2) -> {
				if (c2 == null) {
					return 0;
				}
				if (c1.getCityCode().equals(c2.getCityCode())) {
					return c1.getAreaCode().compareTo(c2.getAreaCode());
				} else {
					return c1.getCityCode().compareTo(c2.getCityCode());
				}
			});

			for (int i = 0; i < lCdArea.size(); i++) {
				CdArea tCdArea = lCdArea.get(i);
				String resultAreaCode = tCdArea.getAreaCode();
				String resultAreaItem = tCdArea.getAreaItem();

				/* 最後一個不加分號 */
				if (i < lCdArea.size() - 1) {
					comboBoxString += resultAreaCode + ":" + resultAreaItem + ";";
				} else {
					comboBoxString += resultAreaCode + ":" + resultAreaItem;
				}
			}

			this.info("Funcd = 2 , 取行政區別,下拉選單字串 = " + comboBoxString);
			break;
		case 3:
			/* FunCd = 3 , 取地段 (需傳入縣市別代碼 & 行政區代碼) */
			if (cityCode.equals("00")) {
				throw new LogicException("E6102", "L6R03");
			}
			if (areaCode.equals("000")) {
				throw new LogicException("E6103", "L6R03");
			}

			Slice<CdLandSection> slCdLandSection = sCdLandSectionService.areaCodeEq(cityCode, areaCode, 0, Integer.MAX_VALUE, titaVo);
			List<CdLandSection> lCdLandSection = slCdLandSection == null ? null : new ArrayList<CdLandSection>(slCdLandSection.getContent());

			if (lCdLandSection == null || lCdLandSection.size() == 0) {
				throw new LogicException("E6104", "L6R03");
			}

			/* 依地段代碼排序 */
			lCdLandSection.sort((c1, c2) -> {
				if (c2 == null) {
					return 0;
				}
				if (c1.getCityCode().equals(c2.getCityCode())) {
					if (c1.getAreaCode().equals(c2.getAreaCode())) {
						return c1.getIrCode().compareTo(c2.getIrCode());
					} else {
						return c1.getAreaCode().compareTo(c2.getAreaCode());
					}
				} else {
					return c1.getCityCode().compareTo(c2.getCityCode());
				}
			});

			for (int i = 0; i < lCdLandSection.size(); i++) {
				CdLandSection tCdLandSection = lCdLandSection.get(i);
				String resultIrCode = tCdLandSection.getIrCode();
				String resultIrItem = tCdLandSection.getIrItem();

				/* 最後一個不加分號 */
				if (i < lCdLandSection.size() - 1) {
					comboBoxString += resultIrCode + ":" + resultIrItem + ";";
				} else {
					comboBoxString += resultIrCode + ":" + resultIrItem;
				}
			}
			this.info("Funcd = 3 , 取地段,下拉選單字串 = " + comboBoxString);
			break;
		case 4:
			/* FunCd = 4 , 取路名 (需傳入縣市別代碼 & 行政區代碼) */
			if (cityCode.equals("00")) {
				throw new LogicException("E6102", "L6R03");
			}
			if (areaCode.equals("000")) {
				throw new LogicException("E6103", "L6R03");
			}
			break;
		default:
			throw new LogicException("E6101", "FunCd 錯誤!");
		}

		/* 將下拉選單字串放入totaVo */
		this.totaVo.putParam("L6r03Result", comboBoxString);

		this.addList(this.totaVo);
		return this.sendList();
	}
}