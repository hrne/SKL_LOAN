package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCityRate;
import com.st1.itx.db.domain.CdCityRateId;
import com.st1.itx.db.service.CdCityRateService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R20")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L4R20 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;

	@Autowired
	public CdCityRateService cdCityRateService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R20 ");
		this.totaVo.init(titaVo);

		this.info("L4R20 Run");

		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		int iWorkMonth = parse.stringToInteger(titaVo.getParam("RimWorkMonth"));

//		 查詢地區別代碼檔
		this.index = 0;
		this.limit = Integer.MAX_VALUE;
		Slice<CdCity> sCdCity = cdCityService.findAll(this.index, this.limit, titaVo);
		Slice<CdCityRate> sCdCityRate = cdCityRateService.findEffectDateEq(iWorkMonth + 191100, this.index, this.limit,
				titaVo);

		if (iFunCd == 1 || iFunCd == 3) {
			if (sCdCityRate != null) {
				throw new LogicException(titaVo, "E0002", "年月 = " + iWorkMonth); // 新增資料已存在
			}
		} else {
			if (sCdCityRate == null || sCdCityRate.getContent().size() == 0) {
				throw new LogicException(titaVo, "E0001", "地區別代碼檔"); // 查無資料
			}
		}

		int totalRow = 30;
		int lCdCityL = sCdCity.getContent().size();
		int row = 1;

		if (lCdCityL > totalRow) {
			this.info("L4R20  需增加L4322 Grid長度 ..." + lCdCityL);
		}

		for (CdCity t : sCdCity.getContent()) {

			CdCityRate tCdCityRate = cdCityRateService.findById(new CdCityRateId(iWorkMonth + 191100, t.getCityCode()),
					titaVo);

			BigDecimal intRateIncr = BigDecimal.ZERO;
			BigDecimal intRateCeiling = BigDecimal.ZERO;
			BigDecimal intRateFloor = BigDecimal.ZERO;
			if (tCdCityRate != null) {
				intRateIncr = tCdCityRate.getIntRateIncr();
				intRateCeiling = tCdCityRate.getIntRateCeiling();
				intRateFloor = tCdCityRate.getIntRateFloor();
			}
			totaVo.putParam("L4r20CityCode" + row, t.getCityCode());
			totaVo.putParam("L4r20CityItem" + row, t.getCityItem());
			totaVo.putParam("L4r20IntRateIncr" + row, intRateIncr);
			totaVo.putParam("L4r20IntRateCeiling" + row, intRateCeiling);
			totaVo.putParam("L4r20IntRateFloor" + row, intRateFloor);
			row++;
		}

		for (int i = row; i <= 30; i++) {
			totaVo.putParam("L4r20CityCode" + row, "");
			totaVo.putParam("L4r20CityItem" + row, "");
			totaVo.putParam("L4r20IntRateIncr" + row, 0);
			totaVo.putParam("L4r20IntRateCeiling" + row, 0);
			totaVo.putParam("L4r20IntRateFloor" + row, 0);
			row++;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}