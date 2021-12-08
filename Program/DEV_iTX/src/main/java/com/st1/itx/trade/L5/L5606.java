package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.util.data.DataLog;

@Component("L5606")
@Scope("prototype")

/**
 * 提醒資料登錄
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5606 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public CdCityService iCdCityService;
	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		this.info("L5606 start");
		String iCityCode = titaVo.getParam("CityCode");
		String iAccCollPsn = titaVo.getParam("AccCollPsn");
		String iAccTelArea = titaVo.getParam("AccTelArea");
		String iAccTelNo = titaVo.getParam("AccTelNo");
		String iAccTelExt = titaVo.getParam("AccTelExt");
		String iLegalPsn = titaVo.getParam("LegalPsn");
		String iLegalArea = titaVo.getParam("LegalArea");
		String iLegalNo = titaVo.getParam("LegalNo");
		String iLegalExt = titaVo.getParam("LegalExt");
		
		CdCity iCdCity = new CdCity();
		CdCity uCdCity = new CdCity();
		iCdCity = iCdCityService.holdById(iCityCode, titaVo);
		CdCity beforeCdCity = (CdCity) iDataLog.clone(iCdCity);
		iCdCity.setAccCollPsn(iAccCollPsn);
		iCdCity.setAccTelArea(iAccTelArea);
		iCdCity.setAccTelExt(iAccTelExt);
		iCdCity.setAccTelNo(iAccTelNo);
		iCdCity.setLegalPsn(iLegalPsn);
		iCdCity.setLegalArea(iLegalArea);
		iCdCity.setLegalNo(iLegalNo);
		iCdCity.setLegalExt(iLegalExt);
		try {
			uCdCity = iCdCityService.update2(iCdCity, titaVo);
			iDataLog.setEnv(titaVo, beforeCdCity, uCdCity);
			iDataLog.exec("修改催收人員法務人員資料");
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}

}
