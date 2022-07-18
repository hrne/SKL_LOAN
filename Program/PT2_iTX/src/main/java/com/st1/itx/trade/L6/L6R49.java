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
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdLand;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R49")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6R49 extends TradeBuffer {

	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdLandService cdLandService;
	@Autowired
	Parse parse;

	/* 宣告下拉選單字串 */
	String comboBoxString = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R48");
		this.totaVo.init(titaVo);

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		String iCityCode = titaVo.getParam("RimCityCode");

		List<CdLand> lCdLand = new ArrayList<CdLand>();
		List<CdCity> lCdCity = new ArrayList<CdCity>();
		Slice<CdLand> slCdLand;
		Slice<CdCity> slCdCity;

		switch (iFunCd) {
		case 1: // 縣市別選單
			slCdCity = cdCityService.findAll(0, Integer.MAX_VALUE);
			lCdCity = slCdCity == null ? null : new ArrayList<CdCity>(slCdCity.getContent());
			if (lCdCity != null) {
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
			}

			break;
		case 2: // 地政選單
			if (iCityCode.isEmpty()) {
				slCdLand = cdLandService.findAll(0, Integer.MAX_VALUE, titaVo);
			} else {
				slCdLand = cdLandService.findCityCodeEq(iCityCode, 0, Integer.MAX_VALUE, titaVo);
			}
			lCdLand = slCdLand == null ? null : new ArrayList<CdLand>(slCdLand.getContent());
			if (lCdLand != null) {
				for (int i = 0; i < lCdLand.size(); i++) {
					CdLand tCdLand = lCdLand.get(i);
					String resultLandCode = tCdLand.getLandOfficeCode().trim();
					String resultLandItem = tCdLand.getLandOfficeItem().trim();

					if (i < lCdLand.size() - 1) {
						comboBoxString += resultLandCode + ":" + resultLandItem + ";";
					} else {
						/* 最後一個不加分號 */
						comboBoxString += resultLandCode + ":" + resultLandItem;
					}
				}
			}
			break;
		default:
			throw new LogicException("E6101", "FunCd 錯誤!");
		}

		/* 將下拉選單字串放入totaVo */
		this.totaVo.putParam("L6r49Result", comboBoxString);

		this.info("end ... ");
		this.addList(this.totaVo);
		return this.sendList();
	}

}