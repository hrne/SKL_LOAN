package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L4R20")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L4R20 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4R20.class);
	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R20 ");
		this.totaVo.init(titaVo);

		this.info("L4R20 Run");

//		 查詢地區別代碼檔
		this.index = 0;
		this.limit = Integer.MAX_VALUE;

		Slice<CdCity> sCdCity = cdCityService.findAll(this.index, this.limit, titaVo);
		List<CdCity> lCdCity = sCdCity == null ? null : sCdCity.getContent();

		if (lCdCity == null || lCdCity.size() == 0) {
			throw new LogicException(titaVo, "E0001", "地區別代碼檔"); // 查無資料
		}

//		 如有找到資料
		int totalRow = 30;
		int lCdCityL = lCdCity.size();
		int row = 1;

		if (lCdCityL > totalRow) {
			this.info("L4R20  需增加L4322 Grid長度 ..." + lCdCityL);
		}

		for (CdCity tCdCity : lCdCity) {
			totaVo.putParam("L4r20CityCode" + row, tCdCity.getCityCode());
			totaVo.putParam("L4r20CityItem" + row, tCdCity.getCityItem());
			totaVo.putParam("L4r20IntRateIncr" + row, tCdCity.getIntRateIncr());
			totaVo.putParam("L4r20IntRateCeiling" + row, tCdCity.getIntRateCeiling());
			totaVo.putParam("L4r20IntRateFloor" + row, tCdCity.getIntRateFloor());
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