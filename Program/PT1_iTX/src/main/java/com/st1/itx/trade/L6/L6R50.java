package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLand;
import com.st1.itx.db.domain.CdLandId;
import com.st1.itx.db.service.CdLandService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R50")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L6R50 extends TradeBuffer {

	@Autowired
	public CdLandService cdLandService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R50 ");
		this.totaVo.init(titaVo);
		initset();

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		String iCityCode = titaVo.getParam("RimCityCode");
		String iLandOfficeCode = titaVo.getParam("RimLandOfficeCode");

		CdLand tCdLand = cdLandService.findById(new CdLandId(iCityCode, iLandOfficeCode), titaVo);
		if (tCdLand == null) {
			switch (iFunCd) {
			case 1:
				break;
			case 2:
			case 4:
			case 5:
				throw new LogicException(titaVo, "E2003",
						"不存在縣市地政檔" + "縣市別 =" + iCityCode + "  地政所代號= " + iLandOfficeCode); // 查無資料
			}
		} else {
			if (iFunCd == 1) {
				throw new LogicException(titaVo, "E0002",
						"地政收件字檔 " + "縣市別 =" + iCityCode + " 地政所代號 =" + iLandOfficeCode); // 新增資料已存在
			}

			this.totaVo.putParam("L6r50CityCode", tCdLand.getCityCode());
			this.totaVo.putParam("L6r50LandOfficeCode", tCdLand.getLandOfficeCode());
			this.totaVo.putParam("L6r50LandOfficeItem", tCdLand.getLandOfficeItem());

		}

		this.info("end ... ");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void initset() throws LogicException {
		this.info("initset ... ");

		this.totaVo.putParam("L6r50CityCode", " ");
		this.totaVo.putParam("L6r50LandOfficeCode", " ");
		this.totaVo.putParam("L6r50LandOfficeItem", " ");
	}
}