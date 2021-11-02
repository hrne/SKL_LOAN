package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R44") // 尋找各類代碼檔資料與縣市與鄉鎮區對照檔
@Scope("prototype")
/**
 *
 *
 * @author Fegie
 * @version 1.0.0
 */
public class L5R44 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCityService iCdCityService;
	@Autowired
	public CdEmpService iCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R44 ");
		this.totaVo.init(titaVo);

		String iCityCode = titaVo.getParam("RimCityCode");
		String iEmpNo = "";
		CdCity iCdCity = new CdCity();
		iCdCity = iCdCityService.findById(iCityCode, titaVo);
		if (iCdCity == null) {
			throw new LogicException(titaVo, "E0001", "無此縣市代碼");
		}
		totaVo.putParam("L5R44CityCodeX", iCdCity.getCityItem());
		iEmpNo = iCdCity.getAccCollPsn();
		totaVo.putParam("L5R44AccCollPsn", iCdCity.getAccCollPsn());
		if (iEmpNo.trim().isEmpty()) {
			totaVo.putParam("L5R44AccCollPsnX", "");
		}else {
			CdEmp iCdEmp = iCdEmpService.findById(iEmpNo, titaVo);
			if (iCdEmp == null) {
				totaVo.putParam("L5R44AccCollPsnX", "");
			}else {
				totaVo.putParam("L5R44AccCollPsnX", iCdEmp.getFullname());
			}
		}
		
		totaVo.putParam("L5R44LegalPsn", iCdCity.getLegalPsn());
		iEmpNo = iCdCity.getLegalPsn();
		if (iEmpNo.trim().isEmpty()) {
			totaVo.putParam("L5R44LegalPsnX", "");
		}else {
			CdEmp iCdEmp = iCdEmpService.findById(iEmpNo, titaVo);
			if (iCdEmp == null) {
				totaVo.putParam("L5R44LegalPsnX", "");
			}else {
				totaVo.putParam("L5R44LegalPsnX", iCdEmp.getFullname());
			}
		}
		totaVo.putParam("L5R44AccTelArea", iCdCity.getAccTelArea());
		totaVo.putParam("L5R44AccTelNo", iCdCity.getAccTelNo());
		totaVo.putParam("L5R44AccTelExt", iCdCity.getAccTelExt());
		totaVo.putParam("L5R44LegalArea", iCdCity.getLegalArea());
		totaVo.putParam("L5R44LegalNo", iCdCity.getLegalNo());
		totaVo.putParam("L5R44LegalExt", iCdCity.getLegalExt());

		this.addList(this.totaVo);
		return this.sendList();
	}

}