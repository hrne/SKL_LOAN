package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 CityCode=X,1 CityItem=X,20 UnitCode=X,6 AccCollPsn=X,6
 * LegalPsn=X,6 IntRateCeiling=m,2.4 IntRateFloor=m,2.4 END=X,1
 */

@Service("L6704")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6704 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCityService sCdCityService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6704 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iCityCode = titaVo.getParam("CityCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6704"); // 功能選擇錯誤
		}

		// 更新地區別代碼檔
		CdCity tCdCity = new CdCity();
		switch (iFuncCode) {
		case 1: // 新增
			moveCdCity(tCdCity, iFuncCode, iCityCode, titaVo);
			try {
				this.info("1");
				sCdCityService.insert(tCdCity, titaVo);
				this.info("2");
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", iCityCode); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			tCdCity = sCdCityService.holdById(iCityCode);
			if (tCdCity == null) {
				throw new LogicException(titaVo, "E0003", iCityCode); // 修改資料不存在
			}
			CdCity tCdCity2 = (CdCity) dataLog.clone(tCdCity); ////
			try {
				moveCdCity(tCdCity, iFuncCode, iCityCode, titaVo);
				tCdCity = sCdCityService.update2(tCdCity, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCity2, tCdCity); ////
			dataLog.exec("修改地區別資料"); ////
			break;

		case 4: // 刪除
			tCdCity = sCdCityService.holdById(iCityCode);
			if (tCdCity != null) {
				try {
					sCdCityService.delete(tCdCity);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0004", iCityCode); // 刪除資料不存在
			}
			dataLog.setEnv(titaVo, tCdCity, tCdCity); ////
			dataLog.exec("刪除地區別資料"); ////
			break;
		}
		this.info("3");
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveCdCity(CdCity mCdCity, int mFuncCode, String mCityCode, TitaVo titaVo) throws LogicException {

		mCdCity.setCityCode(titaVo.getParam("CityCode"));
		mCdCity.setCityItem(titaVo.getParam("CityItem"));
		mCdCity.setUnitCode(titaVo.getParam("UnitCode"));
//		mCdCity.setAccCollPsn(titaVo.getParam("AccCollPsn"));
//		mCdCity.setLegalPsn(titaVo.getParam("LegalPsn"));
//		mCdCity.setIntRateCeiling(this.parse.stringToBigDecimal(titaVo.getParam("IntRateCeiling")));
//		mCdCity.setIntRateFloor(this.parse.stringToBigDecimal(titaVo.getParam("IntRateFloor")));

		if (mFuncCode != 2) {
			mCdCity.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mCdCity.setCreateEmpNo(titaVo.getTlrNo());
		}
		mCdCity.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mCdCity.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}
