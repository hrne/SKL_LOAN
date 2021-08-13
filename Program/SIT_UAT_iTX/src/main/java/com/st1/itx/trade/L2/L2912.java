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
import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.service.CustMainService;
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

@Service("L2912")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2912 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2912.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClMovablesService sClMovablesService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2912 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		String custId = "";
		String custName = "";

		// new PK
		ClMainId ClMainId = new ClMainId();
		ClMovablesId ClMovablesId = new ClMovablesId();

		// new TABLE
		ClMain tClMain = new ClMain();
		ClMovables tClMovables = new ClMovables();

		// 組PK
		// ClMain
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		// ClMovables
		ClMovablesId.setClCode1(iClCode1);
		ClMovablesId.setClCode2(iClCode2);
		ClMovablesId.setClNo(iClNo);

		// 測試該擔保品編號在擔保品動產檔是否有資料
		tClMovables = sClMovablesService.findById(ClMovablesId, titaVo);
		tClMain = sClMainService.findById(ClMainId, titaVo);

		// 無資料 拋錯
		if (tClMovables == null) {
			throw new LogicException("E0001", "擔保品動產檔"); // 查無資料
		}

		// 無資料 拋錯
		if (tClMain == null) {
			throw new LogicException("E0001", "擔保品主檔"); // 查無資料
		}

		String custUKey = tClMain.getCustUKey();

		CustMain tCustMain = sCustMainService.findById(custUKey, titaVo);

		// 無資料 拋錯
		if (tCustMain != null) {
			custId = tCustMain.getCustId().trim();
			custName = tCustMain.getCustName().trim();
		}

		// 地區別中文
		CdCity tCdCity = new CdCity();
		List<CdCity> lCdCity = new ArrayList<CdCity>();
		/* 取縣市名稱 */
		if (tClMain.getCityCode() != null) {
			tCdCity = cdCityService.findById(tClMain.getCityCode(), titaVo);
		}
		if (tCdCity == null) {
			tCdCity = new CdCity();
		}

		this.totaVo.putParam("OCustId", custId);
		this.totaVo.putParam("OCustName", custName);
		this.totaVo.putParam("OCityCode", tClMain.getCityCode());
		this.totaVo.putParam("OCityCodeX", tCdCity.getCityItem());
		this.totaVo.putParam("OClTypeCode", tClMain.getClTypeCode());
		this.totaVo.putParam("OOwnerId", tClMovables.getOwnerId());
		this.totaVo.putParam("OOwnerName", tClMovables.getOwnerName());
		this.totaVo.putParam("OEvaDate", tClMain.getEvaDate());
		this.totaVo.putParam("OEvaAmt", tClMain.getEvaAmt());
		this.totaVo.putParam("OServiceLife", tClMovables.getServiceLife());
		this.totaVo.putParam("OProductSpec", tClMovables.getProductSpec());
		this.totaVo.putParam("OProductType", tClMovables.getProductType());
		this.totaVo.putParam("OProductBrand", tClMovables.getProductBrand());
		this.totaVo.putParam("OProductCC", tClMovables.getProductCC());
		this.totaVo.putParam("OProductColor", tClMovables.getProductColor());
		this.totaVo.putParam("OEngineSN", tClMovables.getEngineSN());
		this.totaVo.putParam("OLicenseNo", tClMovables.getLicenseNo());
		this.totaVo.putParam("OLicenseTypeCode", tClMovables.getLicenseTypeCode());
		this.totaVo.putParam("OLicenseUsageCode", tClMovables.getLicenseUsageCode());
		this.totaVo.putParam("OLiceneIssueDate", tClMovables.getLiceneIssueDate());
		if (tClMovables.getMfgYearMonth() > 0) {
			if (tClMovables.getMfgYearMonth() > 191100) {
				this.totaVo.putParam("OMfgYearMonth", tClMovables.getMfgYearMonth() - 191100);
			} else {
				this.totaVo.putParam("OMfgYearMonth", tClMovables.getMfgYearMonth());
			}
		} else {
			this.totaVo.putParam("OMfgYearMonth", tClMovables.getMfgYearMonth());
		}
		this.totaVo.putParam("OVehicleTypeCode", tClMovables.getVehicleTypeCode());
		this.totaVo.putParam("OVehicleStyleCode", tClMovables.getVehicleStyleCode());
		this.totaVo.putParam("OVehicleOfficeCode", tClMovables.getVehicleOfficeCode());
		this.totaVo.putParam("OCurrency", tClMovables.getCurrency());
		this.totaVo.putParam("OExchangeRate", tClMovables.getExchangeRate());
		this.totaVo.putParam("OInsurance", tClMovables.getInsurance());
		this.totaVo.putParam("OLoanToValue", tClMovables.getLoanToValue());
		this.totaVo.putParam("OSalvageValue", tClMovables.getScrapValue());
		this.totaVo.putParam("OMtgCode", tClMovables.getMtgCode());
		this.totaVo.putParam("OMtgCheck", tClMovables.getMtgCheck());
		this.totaVo.putParam("OMtgLoan", tClMovables.getMtgLoan());
		this.totaVo.putParam("OMtgPledge", tClMovables.getMtgPledge());
		this.totaVo.putParam("OSynd", tClMain.getSynd());
		this.totaVo.putParam("OSyndCode", tClMain.getSyndCode());
		this.totaVo.putParam("ODispPrice", tClMain.getDispPrice());
		this.totaVo.putParam("ODispDate", tClMain.getDispDate());
		this.totaVo.putParam("OSettingStat", tClMovables.getSettingStat());
		this.totaVo.putParam("OClStat", tClMovables.getClStat());
		this.totaVo.putParam("OSettingAmt", tClMovables.getSettingAmt());
		this.totaVo.putParam("OReceiptNo", tClMovables.getReceiptNo());
		this.totaVo.putParam("OMtgNo", tClMovables.getMtgNo());
		this.totaVo.putParam("OReceivedDate", tClMovables.getReceivedDate());
		this.totaVo.putParam("OMortgageIssueStartDate", tClMovables.getMortgageIssueStartDate());
		this.totaVo.putParam("OMortgageIssueEndDate", tClMovables.getMortgageIssueEndDate());
		this.totaVo.putParam("OClStatus", tClMain.getClStatus());
		this.totaVo.putParam("ORemark", tClMovables.getRemark());

		this.addList(this.totaVo);
		return this.sendList();
	}
}