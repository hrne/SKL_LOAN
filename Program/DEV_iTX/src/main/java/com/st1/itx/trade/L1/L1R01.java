package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.parse.Parse;

/*
 * RimCustId=X,10
 * RimCustNo=9,7
 * */

@Service("L1R01")
@Scope("prototype")
public class L1R01 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L1R01.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	/* DB服務注入 */
	@Autowired
	public CustTelNoService sCustTelNoService;

	@Autowired
	public Parse parse;

	@Autowired
	SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R01 ");
		this.totaVo.init(titaVo);

		// RimCustId=X,10
		String iCustId = titaVo.get("RimCustId").trim();

		String funcd = titaVo.get("RimFunCd");

		// RimCustNo=9,7
		int iCustNo = parse.stringToInteger(titaVo.get("RimCustNo").trim());

		CustMain tCustMain = new CustMain();

		/* DB服務 */
		if (!iCustId.isEmpty()) {
			tCustMain = custMainService.custIdFirst(iCustId, titaVo);
		} else if (iCustNo > 0) {
			tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		} else {
			// 統編、戶號需擇一輸入
			throw new LogicException("E0010", "客戶主檔");
		}

		// 邏輯錯誤處理
		if (funcd.equals("1") && tCustMain != null) {
			// 若為新增，但資料已存在，拋錯
			throw new LogicException("E0002", "客戶主檔");
		} else if (tCustMain == null) {
			switch (funcd) {
			case "1":
				// 若為新增且資料不存在，存空值到totaVo
				tCustMain = new CustMain();
				break;
			case "2":
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "客戶主檔");
			case "3":
				// 若為拷貝，但資料不存在，拋錯
				throw new LogicException("E0002", "客戶主檔");
			case "4":
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "客戶主檔");
			case "5":
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "客戶主檔");
			default:
				// funch不在以上範圍，拋錯
				throw new LogicException("E0010", "客戶主檔");
			}
		}

		/* 存入Tota */
		/* key 名稱需與L1R01.tom相同 */
		this.totaVo.putParam("L1r01CustId", tCustMain.getCustId());
		this.totaVo.putParam("L1r01CustNo", tCustMain.getCustNo());
		this.totaVo.putParam("L1r01BranchNo", tCustMain.getBranchNo());
		this.totaVo.putParam("L1r01CustName", tCustMain.getCustName());
		this.totaVo.putParam("L1r01Birthday", tCustMain.getBirthday());
		this.totaVo.putParam("L1r01Sex", tCustMain.getSex());
		this.totaVo.putParam("L1r01CustTypeCode", tCustMain.getCustTypeCode());
		this.totaVo.putParam("L1r01IndustryCode", tCustMain.getIndustryCode());
		this.totaVo.putParam("L1r01NationalityCode", tCustMain.getNationalityCode());
		this.totaVo.putParam("L1r01SpouseId", tCustMain.getSpouseId());
		this.totaVo.putParam("L1r01SpouseName", tCustMain.getSpouseName());
		this.totaVo.putParam("L1r01RegZip3", tCustMain.getRegZip3());
		this.totaVo.putParam("L1r01RegZip2", tCustMain.getRegZip2());
		this.totaVo.putParam("L1r01RegCityCode", tCustMain.getRegCityCode());
		this.totaVo.putParam("L1r01RegAreaCode", tCustMain.getRegAreaCode());
		this.totaVo.putParam("L1r01RegRoad", tCustMain.getRegRoad());
		this.totaVo.putParam("L1r01RegSection", tCustMain.getRegSection());
		this.totaVo.putParam("L1r01RegAlley", tCustMain.getRegAlley());
		this.totaVo.putParam("L1r01RegLane", tCustMain.getRegLane());
		this.totaVo.putParam("L1r01RegNum", tCustMain.getRegNum());
		this.totaVo.putParam("L1r01RegNumDash", tCustMain.getRegNumDash());
		this.totaVo.putParam("L1r01RegFloor", tCustMain.getRegFloor());
		this.totaVo.putParam("L1r01RegFloorDash", tCustMain.getRegFloorDash());
		this.totaVo.putParam("L1r01CurrZip3", tCustMain.getCurrZip3());
		this.totaVo.putParam("L1r01CurrZip2", tCustMain.getCurrZip2());
		this.totaVo.putParam("L1r01CurrCityCode", tCustMain.getCurrCityCode());
		this.totaVo.putParam("L1r01CurrAreaCode", tCustMain.getCurrAreaCode());
		this.totaVo.putParam("L1r01CurrRoad", tCustMain.getCurrRoad());
		this.totaVo.putParam("L1r01CurrSection", tCustMain.getCurrSection());
		this.totaVo.putParam("L1r01CurrAlley", tCustMain.getCurrAlley());
		this.totaVo.putParam("L1r01CurrLane", tCustMain.getCurrLane());
		this.totaVo.putParam("L1r01CurrNum", tCustMain.getCurrNum());
		this.totaVo.putParam("L1r01CurrNumDash", tCustMain.getCurrNumDash());
		this.totaVo.putParam("L1r01CurrFloor", tCustMain.getCurrFloor());
		this.totaVo.putParam("L1r01CurrFloorDash", tCustMain.getCurrFloorDash());
		this.totaVo.putParam("L1r01EMail", tCustMain.getEmail());
//		this.totaVo.putParam("L1r01IsLimit", tCustMain.getIsLimit());
//		this.totaVo.putParam("L1r01IsRelated", tCustMain.getIsRelated());
//		this.totaVo.putParam("L1r01IsLnrelNear", tCustMain.getIsLnrelNear());
		this.totaVo.putParam("L1r01EntCode", tCustMain.getEntCode());
		this.totaVo.putParam("L1r01EmpNo", tCustMain.getEmpNo());
		this.totaVo.putParam("L1r01EName", tCustMain.getEName());
		this.totaVo.putParam("L1r01EduCode", tCustMain.getEduCode());
		this.totaVo.putParam("L1r01OwnedHome", tCustMain.getOwnedHome());
		this.totaVo.putParam("L1r01CurrCompName", tCustMain.getCurrCompName());
		this.totaVo.putParam("L1r01CurrCompId", tCustMain.getCurrCompId());
		this.totaVo.putParam("L1r01CurrCompTel", tCustMain.getCurrCompTel());
		this.totaVo.putParam("L1r01JobTitle", tCustMain.getJobTitle());
		this.totaVo.putParam("L1r01JobTenure", tCustMain.getJobTenure());
		this.totaVo.putParam("L1r01IncomeOfYearly", tCustMain.getIncomeOfYearly());

		if (tCustMain.getIncomeDataDate() == null || "".equals(tCustMain.getIncomeDataDate())
				|| "0".equals(tCustMain.getIncomeDataDate())) {
			this.totaVo.putParam("L1r01IncomeDataDate", "");
		} else {
			this.totaVo.putParam("L1r01IncomeDataDate",
					(parse.stringToInteger(tCustMain.getIncomeDataDate()) - 191100));
		}
		this.totaVo.putParam("L1r01PassportNo", tCustMain.getPassportNo());
		this.totaVo.putParam("L1r01AMLJobCode", tCustMain.getAMLJobCode());
		this.totaVo.putParam("L1r01AMLGroup", tCustMain.getAMLGroup());
		this.totaVo.putParam("L1r01IndigenousName", tCustMain.getIndigenousName());
		this.totaVo.putParam("L1r01Introducer", tCustMain.getIntroducer());

		this.addList(this.totaVo);
		return this.sendList();
	}
}