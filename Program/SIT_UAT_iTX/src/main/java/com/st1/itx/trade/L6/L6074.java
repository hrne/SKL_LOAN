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
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita CityCode=X,1 END=X,1
 */

@Service("L6074") // 地區別代碼檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6074 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6074 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iCityCode = titaVo.getParam("CityCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 51 * 200 = 10,200

		// 查詢地區別代碼檔
		Slice<CdCity> slCdCity;

		if (iCityCode.isEmpty() || iCityCode.equals("00")) {
			slCdCity = sCdCityService.findAll(this.index, this.limit, titaVo);
			this.info("iCityCode isEmpty=" + iCityCode);
		} else {
			slCdCity = sCdCityService.findCityCode(iCityCode, iCityCode, this.index, this.limit, titaVo);
		}
		List<CdCity> lCdCity = slCdCity == null ? null : slCdCity.getContent();

		if (lCdCity == null || lCdCity.size() == 0) {
			throw new LogicException(titaVo, "E0001", "地區別代碼檔"); // 查無資料
		}
		// 如有找到資料
		for (CdCity tCdCity : lCdCity) {
			CdEmp sCdEmp = new CdEmp();
			CdEmp iCdEmp = new CdEmp();
			OccursList occursList = new OccursList();
			occursList.putParam("OOCityCode", tCdCity.getCityCode());
			occursList.putParam("OOCityItem", tCdCity.getCityItem());
			occursList.putParam("OOUnitCode", tCdCity.getUnitCode());
			occursList.putParam("OOAccCollPsn", tCdCity.getAccCollPsn());
			occursList.putParam("OOLegalPsn", tCdCity.getLegalPsn());
//			occursList.putParam("OOIntRateCeiling", tCdCity.getIntRateCeiling());
//			occursList.putParam("OOIntRateFloor", tCdCity.getIntRateFloor());
			sCdEmp = sCdEmpService.findById(tCdCity.getAccCollPsn(), titaVo);
			iCdEmp = sCdEmpService.findById(tCdCity.getLegalPsn(), titaVo);
			if (sCdEmp == null) {
				occursList.putParam("OOAccCollPsnX", "");
			} else {
				occursList.putParam("OOAccCollPsnX", sCdEmp.getFullname());
			}
			if (iCdEmp == null) {
				occursList.putParam("OOLegalPsnX", "");
			} else {
				occursList.putParam("OOLegalPsnX", iCdEmp.getFullname());
			}
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdCity != null && slCdCity.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}