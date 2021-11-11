package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita CityCode=X,1 END=X,1
 */

@Service("L5062") // 地區別代碼檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L5062 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdEmpService sCdEmpService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5062 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iCityCode = titaVo.getParam("CityCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 51 * 200 = 10,200
		Slice<CdCity> iCdCity = null;
		
		if (iCityCode.equals("00") || iCityCode.trim().isEmpty()) {
			iCdCity = sCdCityService.findAll(0, Integer.MAX_VALUE, titaVo);
		}else {
			iCdCity = sCdCityService.findCityCode(iCityCode, iCityCode, this.index, this.limit, titaVo);
		}
		
		if (iCdCity == null) {
			throw new LogicException(titaVo, "E0001", "地區別代碼檔"); // 查無資料
		} else {
			for (CdCity rCdCity : iCdCity) {
				OccursList occursList = new OccursList();
				CdEmp iCdEmp = new CdEmp();
				String iEmpNo = "";
				String iAccTelArea = "";
				String iAccTelNo = "";
				String iAccTelExt = "";
				String iAccCollPsnX = "";

				occursList.putParam("OOCityCode", rCdCity.getCityCode());
				occursList.putParam("OOCityItem", rCdCity.getCityItem());
				occursList.putParam("OOUnitCode", rCdCity.getUnitCode());

				iEmpNo = rCdCity.getAccCollPsn();// 催收人員
				if (!iEmpNo.trim().isEmpty() || !iEmpNo.equals("")) {
					iAccTelArea = rCdCity.getAccTelArea();
					iAccTelNo = rCdCity.getAccTelNo();
					iAccTelExt = rCdCity.getAccTelExt();
					iCdEmp = sCdEmpService.findById(iEmpNo, titaVo);
					if (iCdEmp != null) {
						iAccCollPsnX = iCdEmp.getFullname();
					}
				}
				occursList.putParam("OOAccCollPsn", iEmpNo);
				occursList.putParam("OOAccTelArea", iAccTelArea);
				occursList.putParam("OOAccTelNo", iAccTelNo);
				occursList.putParam("OOAccTelExt", iAccTelExt);
				occursList.putParam("OOAccCollPsnX", iAccCollPsnX);

				iEmpNo = rCdCity.getLegalPsn();// 法務人員
				String iLegalArea = "";
				String iLegalNo = "";
				String iLegalExt = "";
				String iLegalPsnX = "";
				if (!iEmpNo.trim().isEmpty() || !iEmpNo.equals("")) {
					iLegalArea = rCdCity.getLegalArea();
					iLegalNo = rCdCity.getLegalNo();
					iLegalExt = rCdCity.getLegalExt();
					iCdEmp = sCdEmpService.findById(iEmpNo, titaVo);
					if (iCdEmp != null) {
						iLegalPsnX = iCdEmp.getFullname();
					}
				}
				occursList.putParam("OOLegalPsn", iEmpNo);
				occursList.putParam("OOLegalArea", iLegalArea);
				occursList.putParam("OOLegalNo", iLegalNo);
				occursList.putParam("OOLegalExt", iLegalExt);
				occursList.putParam("OOLegalPsnX", iLegalPsnX);

				this.totaVo.addOccursList(occursList);
			}
		}		

		this.addList(this.totaVo);
		return this.sendList();
	}
}