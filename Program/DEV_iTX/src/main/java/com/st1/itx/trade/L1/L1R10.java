package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdLandSectionService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L1R10")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R10 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L1R10.class);
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
		this.info("active L1R10 ");
		this.totaVo.init(titaVo);

		int Zip3 = 0;
		int Zip2 = 0;
		String CityCode = "";
		String AreaCode = "";

		CdArea tCdArea = new CdArea();

//		取得輸入資料
		int iZip3 = parse.stringToInteger(titaVo.getParam("RimZip3"));
		int iZip2 = parse.stringToInteger(titaVo.getParam("RimZip2"));
		String iCityCode = titaVo.getParam("RimCityCode");
		String iAreaCode = titaVo.getParam("RimAreaCode");

		this.info("RimZip3	=" + iZip3);
		this.info("RimZip2	=" + iZip2);
		this.info("CityCode	=" + iCityCode);
		this.info("AreaCode	=" + iAreaCode);
		// 輸入縣市別鄉鎮區代碼時
		if (!iCityCode.isEmpty() && !iAreaCode.isEmpty()) {
			this.info("輸入縣市別鄉鎮區代碼");
			tCdArea = sCdAreaService.findById(new CdAreaId(iCityCode, iAreaCode), titaVo);

			if (tCdArea == null) {
				tCdArea = new CdArea();

			}
			Zip3 = parse.stringToInteger(tCdArea.getZip3());

		} else if (iZip3 > 0) {
			this.info("輸入郵遞區號代碼");
			tCdArea = sCdAreaService.Zip3First(parse.IntegerToString(iZip3, 3), titaVo);
			if (tCdArea == null) {
				tCdArea = new CdArea();
			}
			CityCode = tCdArea.getCityCode();
			AreaCode = tCdArea.getAreaCode();
		}

		this.totaVo.putParam("L1r10Zip3", Zip3);
		this.totaVo.putParam("L1r10CityCode", CityCode);
		this.totaVo.putParam("L1r10AreaCode", AreaCode);

		this.addList(this.totaVo);
		return this.sendList();
	}
}