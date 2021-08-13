package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 */

@Service("L4322")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L4322 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4322.class);
	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;
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

			CdCity tCdCity = cdCityService.holdById(iCityCode);
			
			if (tCdCity == null) {
				throw new LogicException(titaVo, "E0003", iCityCode); // 修改資料不存在
			} else {
				CdCity tCdCity2 = (CdCity) dataLog.clone(tCdCity);
				tCdCity.setIntRateIncr(parse.stringToBigDecimal(iIntRateIncr));
				tCdCity.setIntRateCeiling(parse.stringToBigDecimal(iIntRateCeiling));
				tCdCity.setIntRateFloor(parse.stringToBigDecimal(iIntRateFloor));

				if (tCdCity2.getIntRateCeiling().compareTo(tCdCity.getIntRateCeiling()) == 0
						&& tCdCity2.getIntRateIncr().compareTo(tCdCity.getIntRateIncr()) == 0
						&& tCdCity2.getIntRateFloor().compareTo(tCdCity.getIntRateFloor()) == 0) {
					this.info("tCdCity2.getIntRateCeiling() ... '" + tCdCity2.getIntRateCeiling());
					this.info("tCdCity.getIntRateCeiling() ... '" + tCdCity.getIntRateCeiling());
					this.info("tCdCity2.getIntRateIncr() ... '" + tCdCity2.getIntRateIncr());
					this.info("tCdCity.getIntRateIncr() ... '" + tCdCity.getIntRateIncr());
					this.info("tCdCity2.getIntRateFloor() ... '" + tCdCity2.getIntRateCeiling());
					this.info("tCdCity.getIntRateFloor() ... '" + tCdCity.getIntRateCeiling());
					this.info("continue ...");
					continue;
				}

				tCdCity.setLastUpdate(
						parse.IntegerToSqlDateO(dateUtil.getNowIntegerForBC(), dateUtil.getNowIntegerTime()));
				tCdCity.setLastUpdateEmpNo(titaVo.getTlrNo());
				
				try {
					tCdCity = cdCityService.update2(tCdCity);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
				}

				dataLog.setEnv(titaVo, tCdCity2, tCdCity);
				dataLog.exec();
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}