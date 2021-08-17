package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;
import com.st1.itx.db.service.ClMovablesService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R24")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R24 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ClMovablesService sClMovablesService;
	
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R24 ");
		this.totaVo.init(titaVo);

		// tita
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		;
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));

		// new table ClMain
		ClMovables tClMovables = new ClMovables();
		// new pk
		ClMovablesId ClMovablesId = new ClMovablesId();

		// 塞pk
		ClMovablesId.setClCode1(iClCode1);
		ClMovablesId.setClCode2(iClCode2);
		ClMovablesId.setClNo(iClNo);

		tClMovables = sClMovablesService.findById(ClMovablesId, titaVo);
		if (tClMovables == null) {

			switch (iFunCd) {
			case 1:
				// 若為新增且資料不存在，存空值到totaVo
				tClMovables = new ClMovables();
				break;
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "擔保品動產檔");
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "擔保品動產檔");
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "擔保品動產檔");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "擔保品動產檔");
			}

		} else {
			if (iFunCd == 1) {
				// 若為新增，但資料已存在，拋錯
				throw new LogicException("E0002", "擔保品動產檔");
			}

		}
		this.totaVo.putParam("L2r24ClCode1", tClMovables.getClCode1());
		this.totaVo.putParam("L2r24ClCode2", tClMovables.getClCode2());
		this.totaVo.putParam("L2r24ClNo", tClMovables.getClNo());
//		this.totaVo.putParam("L2r24OwnerId", tClMovables.getOwnerId());
//		this.totaVo.putParam("L2r24OwnerName", tClMovables.getOwnerName());
		CustMain custMain = sCustMainService.findById(tClMovables.getOwnerCustUKey(), titaVo);
		if (custMain != null) {
			this.totaVo.putParam("L2r24OwnerId", custMain.getCustId());
			this.totaVo.putParam("L2r24OwnerName", custMain.getCustName());
		} else {
			this.totaVo.putParam("L2r24OwnerId", "");
			this.totaVo.putParam("L2r24OwnerName", "");
		}
		
		this.totaVo.putParam("L2r24ServiceLife", tClMovables.getServiceLife());
		this.totaVo.putParam("L2r24ProductSpec", tClMovables.getProductSpec());
		this.totaVo.putParam("L2r24ProductType", tClMovables.getProductType());
		this.totaVo.putParam("L2r24ProductBrand", tClMovables.getProductBrand());
		this.totaVo.putParam("L2r24ProductCC", tClMovables.getProductCC());
		this.totaVo.putParam("L2r24ProductColor", tClMovables.getProductColor());
		this.totaVo.putParam("L2r24EngineSN", tClMovables.getEngineSN());
		this.totaVo.putParam("L2r24LicenseNo", tClMovables.getLicenseNo());
		this.totaVo.putParam("L2r24LicenseTypeCode", tClMovables.getLicenseTypeCode());
		this.totaVo.putParam("L2r24LicenseUsageCode", tClMovables.getLicenseUsageCode());
		this.totaVo.putParam("L2r24LiceneIssueDate", tClMovables.getLiceneIssueDate());

		if (tClMovables.getMfgYearMonth() > 0) {
			if (tClMovables.getMfgYearMonth() > 191100) {
				this.totaVo.putParam("L2r24MfgYearMonth", tClMovables.getMfgYearMonth() - 191100);
			} else {
				this.totaVo.putParam("L2r24MfgYearMonth", tClMovables.getMfgYearMonth());
			}
		} else {
			this.totaVo.putParam("L2r24MfgYearMonth", "");
		}

		this.totaVo.putParam("L2r24VehicleTypeCode", tClMovables.getVehicleTypeCode());
		this.totaVo.putParam("L2r24VehicleStyleCode", tClMovables.getVehicleStyleCode());
		this.totaVo.putParam("L2r24VehicleOfficeCode", tClMovables.getVehicleOfficeCode());
		this.totaVo.putParam("L2r24Currency", tClMovables.getCurrency());
		this.totaVo.putParam("L2r24ExchangeRate", tClMovables.getExchangeRate());
		this.totaVo.putParam("L2r24Insurance", tClMovables.getInsurance());
		this.totaVo.putParam("L2r24LoanToValue", tClMovables.getLoanToValue());
		this.totaVo.putParam("L2r24ScrapValue", tClMovables.getScrapValue());
		this.totaVo.putParam("L2r24MtgCode", tClMovables.getMtgCode());
		this.totaVo.putParam("L2r24MtgCheck", tClMovables.getMtgCheck());
		this.totaVo.putParam("L2r24MtgLoan", tClMovables.getMtgLoan());
		this.totaVo.putParam("L2r24MtgPledge", tClMovables.getMtgPledge());
		this.totaVo.putParam("L2r24ClStat", tClMovables.getClStat());
		this.totaVo.putParam("L2r24SettingStat", tClMovables.getSettingStat());
		this.totaVo.putParam("L2r24SettingAmt", tClMovables.getSettingAmt());
		this.totaVo.putParam("L2r24ReceiptNo", tClMovables.getReceiptNo());
		this.totaVo.putParam("L2r24MtgNo", tClMovables.getMtgNo());
		this.totaVo.putParam("L2r24ReceivedDate", tClMovables.getReceivedDate());
		this.totaVo.putParam("L2r24MortgageIssueStartDate", tClMovables.getMortgageIssueStartDate());
		this.totaVo.putParam("L2r24MortgageIssueEndDate", tClMovables.getMortgageIssueEndDate());
		this.totaVo.putParam("L2r24Remark", tClMovables.getRemark());

		this.addList(this.totaVo);
		return this.sendList();
	}
}