package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.CdCityRate;
import com.st1.itx.db.domain.CdCityRateId;
import com.st1.itx.db.service.CdCityRateService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4322")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L4322 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdCityRateService cdCityRateService;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4322 ");
		this.totaVo.init(titaVo);

		int iWorkMonth = parse.stringToInteger(titaVo.getParam("WorkMonth"));
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunctionCode"));

		int totalRow = 30;
		this.info("L4322 Run");

		for (int i = 0; i < totalRow; i++) {
			int thisRow = i + 1;
			this.info("thisRow ..." + thisRow);

			String iCityCode = titaVo.getParam("CityCode" + thisRow).trim();
			String iIntRateIncr = titaVo.getParam("IntRateIncr" + thisRow).trim();
			String iIntRateCeiling = titaVo.getParam("IntRateCeiling" + thisRow).trim();
			String iIntRateFloor = titaVo.getParam("IntRateFloor" + thisRow).trim();

			if (iCityCode == null || iCityCode.length() == 0) {
				this.info("iCityCode ... '" + iCityCode + "' break ...");
				break;
			}
			if (iFunCd == 1 || iFunCd == 3) { // 新增 複製
				CdCityRate tCdCityRate = cdCityRateService.findById(new CdCityRateId(iWorkMonth + 191100, iCityCode),
						titaVo);

				if (tCdCityRate == null) {
					tCdCityRate = new CdCityRate();
					CdCityRateId cdCityRateId = new CdCityRateId();
					cdCityRateId.setCityCode(iCityCode);
					cdCityRateId.setEffectYYMM(iWorkMonth + 191100);
					tCdCityRate.setCdCityRateId(cdCityRateId);
					tCdCityRate.setCityCode(iCityCode);
					tCdCityRate.setEffectYYMM(iWorkMonth + 191100);
					tCdCityRate.setIntRateIncr(parse.stringToBigDecimal(iIntRateIncr));
					tCdCityRate.setIntRateCeiling(parse.stringToBigDecimal(iIntRateCeiling));
					tCdCityRate.setIntRateFloor(parse.stringToBigDecimal(iIntRateFloor));

					try {
						cdCityRateService.insert(tCdCityRate);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				} else {
					throw new LogicException(titaVo, "E0002", "年月 = " + iWorkMonth + " " + iCityCode); // 新增資料已存在
				}
			}
			if (iFunCd == 2) { // 修改
				CdCityRate tCdCityRate = cdCityRateService.holdById(new CdCityRateId(iWorkMonth + 191100, iCityCode),
						titaVo);

				if (tCdCityRate == null) {

					tCdCityRate = new CdCityRate();
					CdCityRateId cdCityRateId = new CdCityRateId();
					cdCityRateId.setCityCode(iCityCode);
					cdCityRateId.setEffectYYMM(iWorkMonth + 191100);
					tCdCityRate.setCdCityRateId(cdCityRateId);
					tCdCityRate.setCityCode(iCityCode);
					tCdCityRate.setEffectYYMM(iWorkMonth + 191100);
					tCdCityRate.setIntRateIncr(parse.stringToBigDecimal(iIntRateIncr));
					tCdCityRate.setIntRateCeiling(parse.stringToBigDecimal(iIntRateCeiling));
					tCdCityRate.setIntRateFloor(parse.stringToBigDecimal(iIntRateFloor));

					try {
						cdCityRateService.insert(tCdCityRate);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				} else {
					CdCityRate tCdCityRate2 = (CdCityRate) dataLog.clone(tCdCityRate);
					tCdCityRate.setIntRateIncr(parse.stringToBigDecimal(iIntRateIncr));
					tCdCityRate.setIntRateCeiling(parse.stringToBigDecimal(iIntRateCeiling));
					tCdCityRate.setIntRateFloor(parse.stringToBigDecimal(iIntRateFloor));

					if (tCdCityRate2.getIntRateCeiling().compareTo(tCdCityRate.getIntRateCeiling()) == 0
							&& tCdCityRate2.getIntRateIncr().compareTo(tCdCityRate.getIntRateIncr()) == 0
							&& tCdCityRate2.getIntRateFloor().compareTo(tCdCityRate.getIntRateFloor()) == 0) {
						this.info("tCdCity2.getIntRateCeiling() ... '" + tCdCityRate2.getIntRateCeiling());
						this.info("tCdCity.getIntRateCeiling() ... '" + tCdCityRate.getIntRateCeiling());
						this.info("tCdCity2.getIntRateIncr() ... '" + tCdCityRate2.getIntRateIncr());
						this.info("tCdCity.getIntRateIncr() ... '" + tCdCityRate.getIntRateIncr());
						this.info("tCdCity2.getIntRateFloor() ... '" + tCdCityRate2.getIntRateCeiling());
						this.info("tCdCity.getIntRateFloor() ... '" + tCdCityRate.getIntRateCeiling());
						this.info("continue ...");
						continue;
					}
					CdCity tCdCity = cdCityService.findById(iCityCode, titaVo);
					String cityItem = "";
					if (tCdCity != null) {
						cityItem = tCdCity.getCityItem();
					}

					try {
						tCdCityRate = cdCityRateService.update2(tCdCityRate);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
					}

					dataLog.setEnv(titaVo, tCdCityRate2, tCdCityRate);
					dataLog.exec("修改 " + tCdCityRate.getCityCode() + "-" + cityItem);
				}
			}

			if (iFunCd == 4) { // 刪除
				CdCityRate tCdCityRate = cdCityRateService.holdById(new CdCityRateId(iWorkMonth + 191100, iCityCode),
						titaVo);

				if (tCdCityRate != null) {

					CdCityRate tCdCityRate2 = (CdCityRate) dataLog.clone(tCdCityRate);

					CdCity tCdCity = cdCityService.findById(iCityCode, titaVo);
					String cityItem = "";
					if (tCdCity != null) {
						cityItem = tCdCity.getCityItem();
					}
					try {
						cdCityRateService.delete(tCdCityRate, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
					}

					dataLog.setEnv(titaVo, tCdCityRate2, tCdCityRate);
					dataLog.exec("刪除 " + iCityCode + "-" + cityItem + " 年月" + iWorkMonth);
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}