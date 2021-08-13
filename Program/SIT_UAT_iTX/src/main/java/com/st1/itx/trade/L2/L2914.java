package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.domain.ClOther;
import com.st1.itx.db.domain.ClOtherId;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.db.service.ClOtherService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2914")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2914 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2914.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public CdCityService cdCityService;

	/* DB服務注入 */
	@Autowired
	public ClOtherService sClOtherService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2914 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));

		// new PK
		ClMainId ClMainId = new ClMainId();
		ClOtherId ClOtherId = new ClOtherId();

		// new TABLE
		ClMain tClMain = new ClMain();
		ClOther tClOther = new ClOther();

		// 組PK
		// ClMain
		ClMainId.setClCode1(iClCode1);
		ClMainId.setClCode2(iClCode2);
		ClMainId.setClNo(iClNo);

		// ClMovables
		ClOtherId.setClCode1(iClCode1);
		ClOtherId.setClCode2(iClCode2);
		ClOtherId.setClNo(iClNo);

		int RPTFG = 0;
		// 地區別中文
		CdCity tCdCity = new CdCity();

		// 測試該擔保品編號在擔保品動產檔是否有資料
		tClOther = sClOtherService.findById(ClOtherId, titaVo);
		tClMain = sClMainService.findById(ClMainId, titaVo);

		// 無資料 拋錯
		if (tClMain == null) {
			throw new LogicException("E0001", "擔保品主檔"); // 查無資料
		}
		if (tClOther == null) {
			throw new LogicException("E0001", "擔保品其他檔"); // 查無資料
		}
		if(tClMain.getCityCode()!=null) {
			tCdCity = cdCityService.findById(tClMain.getCityCode(), titaVo);
		}
		if (tCdCity == null) {
			tCdCity = new CdCity();
		}


		if (tClMain.getClTypeCode().equals("101")) {
			RPTFG = 3;
		} else if (tClMain.getClTypeCode().equals("102")) {
			RPTFG = 3;
		} else if (tClMain.getClTypeCode().equals("103")) {
			RPTFG = 3;
		} else if (tClMain.getClTypeCode().equals("104")) {
			RPTFG = 2;
		} else if (tClMain.getClTypeCode().equals("110")) {
			RPTFG = 3;
		} else if (tClMain.getClTypeCode().equals("130")) {
			RPTFG = 3;
		} else if (tClMain.getClTypeCode().equals("160")) {
			RPTFG = 2;
		} else if (tClMain.getClTypeCode().equals("180")) {
			RPTFG = 2;
		} else if (tClMain.getClTypeCode().equals("190")) {
			RPTFG = 2;
		} else if (tClMain.getClTypeCode().equals("1D0")) {
			RPTFG = 2;
		} else if (tClMain.getClTypeCode().equals("1E1")) {
			RPTFG = 3;
		} else if (tClMain.getClTypeCode().equals("1X0")) {
			RPTFG = 2;
		} else if (tClMain.getClTypeCode().equals("998")) {
			RPTFG = 1;
		} else if (tClMain.getClTypeCode().equals("999")) {
			RPTFG = 1;
		} else {
			RPTFG = 0;
		}

		this.totaVo.putParam("OCityCode", tClMain.getCityCode());
		this.totaVo.putParam("OCityCodeX", tCdCity.getCityItem());
		this.totaVo.putParam("OClTypeCode", tClMain.getClTypeCode());
		this.totaVo.putParam("OEvaDate", tClMain.getEvaDate());
		this.totaVo.putParam("OPledgeAmt", tClMain.getEvaAmt());
		this.totaVo.putParam("OPledgeStartDate", tClOther.getPledgeStartDate());
		this.totaVo.putParam("OPledgeEndDate", tClOther.getPledgeEndDate());
		this.totaVo.putParam("OPledgeBankCode", tClOther.getPledgeBankCode());
		this.totaVo.putParam("OPledgeNO", tClOther.getPledgeNO());
		this.totaVo.putParam("OOwnerId", tClOther.getOwnerId());
		this.totaVo.putParam("OOwnerName", tClOther.getOwnerName());
		this.totaVo.putParam("OIssuingId", tClOther.getIssuingId());
		this.totaVo.putParam("OIssuingCounty", tClOther.getIssuingCounty());
		this.totaVo.putParam("ODocNo", tClOther.getDocNo());
		this.totaVo.putParam("OLoanToValue", tClOther.getLoanToValue());

		this.totaVo.putParam("OSecuritiesType", tClOther.getSecuritiesType());
		this.totaVo.putParam("OListed", tClOther.getListed());
		this.totaVo.putParam("OOfferingDate", tClOther.getOfferingDate());
		this.totaVo.putParam("OExpirationDate", tClOther.getExpirationDate());
		this.totaVo.putParam("OTargetIssuer", tClOther.getTargetIssuer());
		this.totaVo.putParam("OSubTargetIssuer", tClOther.getSubTargetIssuer());
		this.totaVo.putParam("OCreditDate", tClOther.getCreditDate());
		this.totaVo.putParam("OCredit", tClOther.getCredit());
		this.totaVo.putParam("OExternalCredit", tClOther.getExternalCredit());
		this.totaVo.putParam("OIndex", tClOther.getIndex());
		this.totaVo.putParam("OTradingMethod", tClOther.getTradingMethod());
		this.totaVo.putParam("OCompensation", tClOther.getCompensation());
		this.totaVo.putParam("OInvestment", tClOther.getInvestment());
		this.totaVo.putParam("OPublicValue", tClOther.getPublicValue());

		this.totaVo.putParam("OSettingStat", tClOther.getSettingStat());
		this.totaVo.putParam("OClStat", tClOther.getClStat());
		this.totaVo.putParam("OSettingDate", tClOther.getSettingDate());
		this.totaVo.putParam("OSettingAmt", tClOther.getSettingAmt());
		this.totaVo.putParam("OSynd", tClMain.getSynd());
		this.totaVo.putParam("OSyndCode", tClMain.getSyndCode());
		this.totaVo.putParam("ODispPrice", tClMain.getDispPrice());
		this.totaVo.putParam("ODispDate", tClMain.getDispDate());
		this.totaVo.putParam("OClStatus", tClMain.getClStatus());
		this.totaVo.putParam("ORPTFG", RPTFG);
		

		this.addList(this.totaVo);
		return this.sendList();
	}
}