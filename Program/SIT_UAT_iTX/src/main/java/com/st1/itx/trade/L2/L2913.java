package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCity;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClStockService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * END=X,1<br>
 */

@Service("L2913")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2913 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2913.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;

	/* DB服務注入 */
	@Autowired
	public ClStockService sClStockService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2913 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		// new PK
		ClMainId ClMainId = new ClMainId();
		ClStockId ClStockId = new ClStockId();

		// new TABLE
		ClMain tClMain = new ClMain();
		ClStock tClStock = new ClStock();
		CdCity tCdCity = new CdCity();

		// new arraylist
		List<CdCity> lCdCity = new ArrayList<CdCity>();

		// 組PK
		// ClMain
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		// ClStock
		ClStockId.setClCode1(iClCode1);
		ClStockId.setClCode2(iClCode2);
		ClStockId.setClNo(iClNo);
		

		// 測試該擔保品編號在擔保品股票檔是否有資料
		tClStock = sClStockService.findById(ClStockId, titaVo);
		tClMain = sClMainService.findById(ClMainId, titaVo);

		// 無資料 拋錯
		if (tClMain == null) {
			throw new LogicException("E0001", "擔保品主檔");
		}
		if (tClStock == null) {
			throw new LogicException("E0001", "擔保品股票檔");
		}

		/* 取縣市名稱 */
		if(tClMain.getCityCode()!=null) {
			tCdCity = cdCityService.findById(tClMain.getCityCode(), titaVo);
		}
		if (tCdCity == null) {
			tCdCity = new CdCity();
		}


		this.totaVo.putParam("OCityCode", tClMain.getCityCode());
		this.totaVo.putParam("OCityCodeX", tCdCity.getCityItem());
		this.totaVo.putParam("OClTypeCode", tClMain.getClTypeCode());
		this.totaVo.putParam("OStockCode", tClStock.getStockCode());
		this.totaVo.putParam("OListingType", tClStock.getListingType());
		this.totaVo.putParam("OStockType", tClStock.getStockType());
		this.totaVo.putParam("OCompanyId", tClStock.getCompanyId());
		this.totaVo.putParam("ODataYear", tClStock.getDataYear());
		this.totaVo.putParam("OIssuedShares", tClStock.getIssuedShares());
		this.totaVo.putParam("ONetWorth", tClStock.getNetWorth());
		this.totaVo.putParam("OEvaStandard", tClStock.getEvaStandard());
		this.totaVo.putParam("OParValue", tClStock.getParValue());
		this.totaVo.putParam("OMonthlyAvg", tClStock.getMonthlyAvg());
		this.totaVo.putParam("OYdClosingPrice", tClStock.getYdClosingPrice());
		this.totaVo.putParam("OThreeMonthAvg", tClStock.getThreeMonthAvg());
		this.totaVo.putParam("OEvaUnitPrice", tClStock.getEvaUnitPrice());
		this.totaVo.putParam("OOwnerId", tClStock.getOwnerId());
		this.totaVo.putParam("OOwnerName", tClStock.getOwnerName());
		this.totaVo.putParam("OInsiderJobTitle", tClStock.getInsiderJobTitle());
		this.totaVo.putParam("OInsiderPosition", tClStock.getInsiderPosition());
		this.totaVo.putParam("OLegalPersonId", tClStock.getLegalPersonId());
		this.totaVo.putParam("OLoanToValue", tClStock.getLoanToValue());
		this.totaVo.putParam("OClMtr", tClStock.getClMtr());
		this.totaVo.putParam("ONoticeMtr", tClStock.getNoticeMtr());
		this.totaVo.putParam("OImplementMtr", tClStock.getImplementMtr());
		this.totaVo.putParam("OPledgeNo", tClStock.getPledgeNo());
		this.totaVo.putParam("OComputeMTR", tClStock.getComputeMTR());
		this.totaVo.putParam("OSettingStat", tClStock.getSettingStat());
		this.totaVo.putParam("OClStat", tClStock.getClStat());
		this.totaVo.putParam("OSettingDate", tClStock.getSettingDate());
		this.totaVo.putParam("OSettingBalance", tClStock.getSettingBalance());
		this.totaVo.putParam("OEvaDate", tClMain.getEvaDate());
		this.totaVo.putParam("OEvaAmt", tClMain.getEvaAmt());
		this.totaVo.putParam("OMtgDate", tClStock.getMtgDate());
		this.totaVo.putParam("OCustodyNo", tClStock.getCustodyNo());
		this.totaVo.putParam("OSynd", tClMain.getSynd());
		this.totaVo.putParam("OSyndCode", tClMain.getSyndCode());
		this.totaVo.putParam("ODispPrice", tClMain.getDispPrice());
		this.totaVo.putParam("ODispDate", tClMain.getDispDate());
		this.totaVo.putParam("OClStatus", tClMain.getClStatus());

		this.addList(this.totaVo);
		return this.sendList();
	}
}