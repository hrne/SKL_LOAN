package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimL6R12CityCode=X,1
 */
@Service("L6R12") // 尋找地區別代碼檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R12 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R12.class);

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R12 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimCityCode = titaVo.getParam("RimL6R12CityCode");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R12"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R12"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdCity(new CdCity());

		// 查詢地區別代碼檔
		CdCity tCdCity = sCdCityService.findById(iRimCityCode, titaVo);

		/* 如有找到資料 */
		if (tCdCity != null) {
			if (iRimTxCode.equals("L6704") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", iRimCityCode); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdCity(tCdCity);
			}
		} else {
			if (iRimTxCode.equals("L6704") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "地區別代碼檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 地區別代碼檔
	private void moveTotaCdCity(CdCity mCdCity) throws LogicException {

		this.totaVo.putParam("L6R12CityCode", mCdCity.getCityCode());
		this.totaVo.putParam("L6R12CityItem", mCdCity.getCityItem());
//		this.totaVo.putParam("L6R12AS400CityCode", mCdCity.getAS400CityCode());
		this.totaVo.putParam("L6R12UnitCode", mCdCity.getUnitCode());
		this.totaVo.putParam("L6R12AccCollPsn", mCdCity.getAccCollPsn());
		this.totaVo.putParam("L6R12LegalPsn", mCdCity.getLegalPsn());
//		this.totaVo.putParam("L6R12IntRateCeiling", mCdCity.getIntRateCeiling());
//		this.totaVo.putParam("L6R12IntRateFloor", mCdCity.getIntRateFloor());

	}

}