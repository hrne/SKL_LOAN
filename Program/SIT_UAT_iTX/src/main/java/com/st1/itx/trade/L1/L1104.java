package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustId=X,10 CustIdInd=X,1 CustIdAfter=X,10 CustNameInd=X,1 CustName1Aft=X,50
 * CustName2Aft=X,50 BirthdayInd=X,1 BirthdayBefore=9,7 CustTypeInd=X,1
 * CustTypeAft=9,2 IndustryInd=X,1 IndustryAft=9,6 CountryInd=X,1 CountryAft=X,2
 * SpouseIdInd=X,1 SpouseIdAft=X,10 SpouseNmInd=X,1 SpouseNmAft=X,14
 * RegZip3Ind=X,1 RegZip3Aft=9,3 RegZip2Ind=X,1 RegZip2Aft=9,2
 * RegCitycodeInd=X,1 RegCitycodeAft=X,10 RegAreacodeInd=X,1 RegAreacodeAft=X,2
 * RegIrcodeInd=X,1 RegIrcodeAft=X,4 RegRoadInd=X,1 RegRoadAft=X,40
 * RegSctionInd=X,1 RegSctionAft=X,5 RegAlleyInd=X,1 RegAlleyAft=X,5
 * RegLaneInd=X,1 RegLaneAft=X,5 RegNumInd=X,1 RegNumAft=X,5 RegNumdashsInd=X,1
 * RegNumdashsAft=X,5 RegFloorInd=X,1 RegFloorAft=X,5 RegFloordashInd=X,1
 * RegFloordashAft=X,5 CurrZip3Ind=X,1 CurrZip3Aft=X,3 CurrZip2Ind=X,1
 * CurrZip2Aft=X,2 CurrCitycodeInd=X,1 CurrCitycodeAft=X,10 CurrAreacodeInd=X,1
 * CurrAreacodeAft=X,2 CurrIrcodeInd=X,1 CurrIrcodeAft=X,4 CurrRoadInd=X,1
 * CurrRoadAft=X,40 CurrSectionInd=X,1 CurrSectionAft=X,5 CurrAlleyInd=X,1
 * CurrAlleyAft=X,5 CurrLaneInd=X,1 CurrLaneAft=X,5 CurrNumInd=X,1
 * CurrNumAft=X,5 CurrNumdashsInd=X,1 CurrNumdashsAft=X,5 CurrFloorInd=X,1
 * CurrFloorAft=X,5 CurrFloordashInd=X,1 CurrFloordashAft=X,5 EntcodeInd=X,1
 * EntcodeAft=9,1 EnameInd=X,1 EnameAft=X,20 IncomeofyearlyInd=X,1
 * IncomeofyearlyAft=9,9 IncomedatadateInd=X,1 IncomedatadateAft=X,6
 */

@Service("L1104")
@Scope("prototype")
public class L1104 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1104.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1104 ");
		this.totaVo.init(titaVo);

		String custid = titaVo.getParam("CustId").trim();
		CustMain tCustMain = custMainService.custIdFirst(custid);

		// 例外處理 若查無資料請至L1102新增
		if (tCustMain == null) {
			throw new LogicException("E1003", "客戶資料主檔");
		}

		// 鎖定這筆
		custMainService.holdById(tCustMain);

		// 經辦登錄時更新
		if (titaVo.isActfgEntry()) {

			// 變更前
			CustMain beforeCustMain = (CustMain) dataLog.clone(tCustMain);

			// 若該欄位有被修改,更新該欄位資料

			// 如果要修改統編
			if (titaVo.getParam("CustIdInd").trim().equals("X")) {
				// 修改後的統編
				String new_custid = titaVo.getParam("CustIdAfter").trim();

				// 先檢查新統編是否已存在
				CustMain lCustMain2 = custMainService.custIdFirst(new_custid);

				// 新統編不存在,可修改
				if (lCustMain2 == null) {
					// 更換資料
					tCustMain.setCustId(new_custid);
				} else {
					// 例外處理 新統編已存在
					throw new LogicException("E1005", "客戶資料主檔");
				}

			}
			// 正常交易或修正交易
			if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {

				if (titaVo.isHcodeNormal() && tCustMain.getActFg() == 1) {
					throw new LogicException(titaVo, "E0021", " "); // 該筆資料待放行中
				}

				tCustMain.setActFg(1);

				// 訂正交易
			} else {

				tCustMain.setActFg(0);
			}

			try {
				// 更新資料
				tCustMain = custMainService.update2(tCustMain,titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2)
					throw new LogicException("E0007", "客戶資料主檔");

			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeCustMain, tCustMain);
			dataLog.exec();
		}

		// 放行一般
		if (titaVo.isActfgSuprele() && titaVo.isHcodeNormal()) {
			this.info("放行一般");
			this.info("tCustMain =" + tCustMain);
			if (tCustMain.getActFg() != 1) {
				throw new LogicException(titaVo, "E0017", " "); // 該筆交易狀態非待放行，不可做交易放行
			}

			// 單位別
			if (titaVo.getParam("BranchNoInd").equals("X")) {
				tCustMain.setBranchNo(titaVo.getParam("BranchNo"));
			}

			// 公司名稱
			if (titaVo.getParam("CustNameInd").equals("X")) {
				// if (!titaVo.getParam("CUSTNM_IND").isEmpty()) {
				tCustMain.setCustName(titaVo.getParam("CustNameAft"));
			}

			// 設立日期
			if (titaVo.getParam("BirthdayInd").equals("X")) {
				tCustMain.setBirthday(parse.stringToInteger(titaVo.getParam("BirthdayAft")));
			}

			// 客戶別
			if (titaVo.getParam("CustTypeInd").equals("X")) {
				tCustMain.setCustTypeCode(titaVo.getParam("CustTypeAft"));
			}

			// 行業別
			if (titaVo.getParam("IndustryInd").equals("X")) {
				tCustMain.setIndustryCode(titaVo.getParam("IndustryAft"));
			}

			// 國籍
			if (titaVo.getParam("CountryInd").equals("X")) {
				tCustMain.setNationalityCode(titaVo.getParam("CountryAft"));
			}

			// 負責人身分證
			if (titaVo.getParam("SpouseIdInd").equals("X")) {
				tCustMain.setSpouseId(titaVo.getParam("SpouseIdAft"));
			}

			// 負責人姓名
			if (titaVo.getParam("SpouseNmInd").equals("X")) {
				tCustMain.setSpouseName(titaVo.getParam("SpouseNmAft"));
			}

			// 戶籍-郵遞區號前三碼
			if (titaVo.getParam("RegZip3Ind").equals("X")) {
				tCustMain.setRegZip3(titaVo.getParam("RegZip3Aft"));
			}

			// 戶籍-郵遞區號後兩碼
			if (titaVo.getParam("RegZip2Ind").equals("X")) {
				tCustMain.setRegZip2(titaVo.getParam("RegZip2Aft"));
			}

			// 戶籍-縣市代碼
			if (titaVo.getParam("RegCitycodeInd").equals("X")) {
				tCustMain.setRegCityCode(titaVo.getParam("RegCitycodeAft"));
			}

			// 戶籍-鄉鎮市區代碼
			if (titaVo.getParam("RegAreacodeInd").equals("X")) {
				tCustMain.setRegAreaCode(titaVo.getParam("RegAreacodeAft"));
			}

			// 戶籍-路名
			if (titaVo.getParam("RegRoadInd").equals("X")) {
				tCustMain.setRegRoad(titaVo.getParam("RegRoadAft"));
			}

			// 戶籍-段
			if (titaVo.getParam("RegSectionInd").equals("X")) {
				tCustMain.setRegSection(titaVo.getParam("RegSectionAft"));
			}

			// 戶籍-巷
			if (titaVo.getParam("RegAlleyInd").equals("X")) {
				tCustMain.setRegAlley(titaVo.getParam("RegAlleyAft"));
			}

			// 戶籍-弄
			if (titaVo.getParam("RegLaneInd").equals("X")) {
				tCustMain.setRegLane(titaVo.getParam("RegLaneAft"));
			}

			// 戶籍-號
			if (titaVo.getParam("RegNumInd").equals("X")) {
				tCustMain.setRegNum(titaVo.getParam("RegNumAft"));
			}

			// 戶籍-號之
			if (titaVo.getParam("RegNumdashsInd").equals("X")) {
				tCustMain.setRegNumDash(titaVo.getParam("RegNumdashsAft"));
			}

			// 戶籍-樓
			if (titaVo.getParam("RegFloorInd").equals("X")) {
				tCustMain.setRegFloor(titaVo.getParam("RegFloorAft"));
			}

			// 戶籍-樓之
			if (titaVo.getParam("RegFloordashInd").equals("X")) {
				tCustMain.setRegFloorDash(titaVo.getParam("RegFloordashAft"));
			}

			// 通訊-郵遞區號前三碼
			if (titaVo.getParam("CurrZip3Ind").equals("X")) {
				tCustMain.setCurrZip3(titaVo.getParam("CurrZip3Aft"));
			}

			// 通訊-郵遞區號後兩碼
			if (titaVo.getParam("CurrZip2Ind").equals("X")) {
				tCustMain.setCurrZip2(titaVo.getParam("CurrZip2Aft"));
			}

			// 通訊-縣市代碼
			if (titaVo.getParam("CurrCitycodeInd").equals("X")) {
				tCustMain.setCurrCityCode(titaVo.getParam("CurrCitycodeAft"));
			}

			// 通訊-鄉鎮市區代碼
			if (titaVo.getParam("CurrAreacodeInd").equals("X")) {
				tCustMain.setCurrAreaCode(titaVo.getParam("CurrAreacodeAft"));
			}

			// 通訊-路名
			if (titaVo.getParam("CurrRoadInd").equals("X")) {
				tCustMain.setCurrRoad(titaVo.getParam("CurrRoadAft"));
			}

			// 通訊-段
			if (titaVo.getParam("CurrSectionInd").equals("X")) {
				tCustMain.setCurrSection(titaVo.getParam("CurrSectionAft"));
			}

			// 通訊-巷
			if (titaVo.getParam("CurrAlleyInd").equals("X")) {
				tCustMain.setCurrAlley(titaVo.getParam("CurrAlleyAft"));
			}

			// 通訊-弄
			if (titaVo.getParam("CurrLaneInd").equals("X")) {
				tCustMain.setCurrLane(titaVo.getParam("CurrLaneAft"));
			}

			// 通訊-號
			if (titaVo.getParam("CurrNumInd").equals("X")) {
				tCustMain.setCurrNum(titaVo.getParam("CurrNumAft"));
			}

			// 通訊-號之
			if (titaVo.getParam("CurrNumdashsInd").equals("X")) {
				tCustMain.setCurrNumDash(titaVo.getParam("CurrNumdashsAft"));
			}

			// 通訊-樓
			if (titaVo.getParam("CurrFloorInd").equals("X")) {
				tCustMain.setCurrFloor(titaVo.getParam("CurrFloorAft"));
			}

			// 通訊-樓之
			if (titaVo.getParam("CurrFloordashInd").equals("X")) {
				tCustMain.setCurrFloorDash(titaVo.getParam("CurrFloordashAft"));
			}

			// 電子信箱
			if (titaVo.getParam("EmailInd").equals("X")) {
				tCustMain.setEmail(titaVo.getParam("EmailAft"));
			}

			// 企金別
			if (titaVo.getParam("EntcodeInd").equals("X")) {
				tCustMain.setEntCode(titaVo.getParam("EntcodeAft"));
			}

			// 英文姓名
			if (titaVo.getParam("EnameInd").equals("X")) {
				tCustMain.setEName(titaVo.getParam("EnameAft"));
			}

			// 年收入
			if (titaVo.getParam("IncomeofyearlyInd").equals("X")) {
				tCustMain.setIncomeOfYearly(parse.stringToInteger(titaVo.getParam("IncomeofyearlyAft")));
			}

			// 年收入資料年月
			if (titaVo.getParam("IncomedatadateInd").equals("X")) {
				if (titaVo.getParam("IncomedatadateAft").equals("")) {
					tCustMain.setIncomeDataDate("");
				} else {
					tCustMain.setIncomeDataDate(
							"" + (parse.stringToInteger(titaVo.getParam("IncomedatadateAft")) + 191100));
				}

			}

			tCustMain.setActFg(2);
			try {
				tCustMain = custMainService.update(tCustMain,titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 放行訂正
		if (titaVo.isActfgSuprele() && titaVo.isHcodeErase()) {
			this.info("放行訂正");
			if (tCustMain.getActFg() != 2) {
				throw new LogicException(titaVo, "E0018", " "); // 該筆交易狀態非已放行，不可做訂正已放行交易
			}
			// 公司名稱
			if (titaVo.getParam("CustNameInd").equals("X")) {
				// if (!titaVo.getParam("CUSTNM_IND").isEmpty()) {
				tCustMain.setCustName(titaVo.getParam("CustNameBefore"));
			}

			// 設立日期
			if (titaVo.getParam("BirthdayInd").equals("X")) {
				tCustMain.setBirthday(parse.stringToInteger(titaVo.getParam("BirthdayBefore")));
			}

			// 客戶別
			if (titaVo.getParam("CustTypeInd").equals("X")) {
				tCustMain.setCustTypeCode(titaVo.getParam("CustTypeBefore"));
			}

			// 行業別
			if (titaVo.getParam("IndustryInd").equals("X")) {
				tCustMain.setIndustryCode(titaVo.getParam("IndustryBefore"));
			}

			// 國籍
			if (titaVo.getParam("CountryInd").equals("X")) {
				tCustMain.setNationalityCode(titaVo.getParam("CountryBefore"));
			}

			// 負責人身分證
			if (titaVo.getParam("SpouseIdInd").equals("X")) {
				tCustMain.setSpouseId(titaVo.getParam("SpouseIdBefore"));
			}

			// 負責人姓名
			if (titaVo.getParam("SpouseNmInd").equals("X")) {
				tCustMain.setSpouseName(titaVo.getParam("SpouseNmBefore"));
			}

			// 戶籍-郵遞區號前三碼
			if (titaVo.getParam("RegZip3Ind").equals("X")) {
				tCustMain.setRegZip3(titaVo.getParam("RegZip3Before"));
			}

			// 戶籍-郵遞區號後兩碼
			if (titaVo.getParam("RegZip2Ind").equals("X")) {
				tCustMain.setRegZip2(titaVo.getParam("RegZip2Before"));
			}

			// 戶籍-縣市代碼
			if (titaVo.getParam("RegCitycodeInd").equals("X")) {
				tCustMain.setRegCityCode(titaVo.getParam("RegCitycodeBefore"));
			}

			// 戶籍-鄉鎮市區代碼
			if (titaVo.getParam("RegAreacodeInd").equals("X")) {
				tCustMain.setRegAreaCode(titaVo.getParam("RegAreacodeBefore"));
			}

			// 戶籍-路名
			if (titaVo.getParam("RegRoadInd").equals("X")) {
				tCustMain.setRegRoad(titaVo.getParam("RegRoadBefore"));
			}

			// 戶籍-段
			if (titaVo.getParam("RegSectionInd").equals("X")) {
				tCustMain.setRegSection(titaVo.getParam("RegSectionBefore"));
			}

			// 戶籍-巷
			if (titaVo.getParam("RegAlleyInd").equals("X")) {
				tCustMain.setRegAlley(titaVo.getParam("RegAlleyBefore"));
			}

			// 戶籍-弄
			if (titaVo.getParam("RegLaneInd").equals("X")) {
				tCustMain.setRegLane(titaVo.getParam("RegLaneBefore"));
			}

			// 戶籍-號
			if (titaVo.getParam("RegNumInd").equals("X")) {
				tCustMain.setRegNum(titaVo.getParam("RegNumBefore"));
			}

			// 戶籍-號之
			if (titaVo.getParam("RegNumdashsInd").equals("X")) {
				tCustMain.setRegNumDash(titaVo.getParam("RegNumdashsBefore"));
			}

			// 戶籍-樓
			if (titaVo.getParam("RegFloorInd").equals("X")) {
				tCustMain.setRegFloor(titaVo.getParam("RegFloorBefore"));
			}

			// 戶籍-樓之
			if (titaVo.getParam("RegFloordashInd").equals("X")) {
				tCustMain.setRegFloorDash(titaVo.getParam("RegFloordashBefore"));
			}

			// 通訊-郵遞區號前三碼
			if (titaVo.getParam("CurrZip3Ind").equals("X")) {
				tCustMain.setCurrZip3(titaVo.getParam("CurrZip3Before"));
			}

			// 通訊-郵遞區號後兩碼
			if (titaVo.getParam("CurrZip2Ind").equals("X")) {
				tCustMain.setCurrZip2(titaVo.getParam("CurrZip2Before"));
			}

			// 通訊-縣市代碼
			if (titaVo.getParam("CurrCitycodeInd").equals("X")) {
				tCustMain.setCurrCityCode(titaVo.getParam("CurrCitycodeBefore"));
			}

			// 通訊-鄉鎮市區代碼
			if (titaVo.getParam("CurrAreacodeInd").equals("X")) {
				tCustMain.setCurrAreaCode(titaVo.getParam("CurrAreacodeBefore"));
			}

			// 通訊-路名
			if (titaVo.getParam("CurrRoadInd").equals("X")) {
				tCustMain.setCurrRoad(titaVo.getParam("CurrRoadBefore"));
			}

			// 通訊-段
			if (titaVo.getParam("CurrSectionInd").equals("X")) {
				tCustMain.setCurrSection(titaVo.getParam("CurrSectionBefore"));
			}

			// 通訊-巷
			if (titaVo.getParam("CurrAlleyInd").equals("X")) {
				tCustMain.setCurrAlley(titaVo.getParam("CurrAlleyBefore"));
			}

			// 通訊-弄
			if (titaVo.getParam("CurrLaneInd").equals("X")) {
				tCustMain.setCurrLane(titaVo.getParam("CurrLaneBefore"));
			}

			// 通訊-號
			if (titaVo.getParam("CurrNumInd").equals("X")) {
				tCustMain.setCurrNum(titaVo.getParam("CurrNumBefore"));
			}

			// 通訊-號之
			if (titaVo.getParam("CurrNumdashsInd").equals("X")) {
				tCustMain.setCurrNumDash(titaVo.getParam("CurrNumdashsBefore"));
			}

			// 通訊-樓
			if (titaVo.getParam("CurrFloorInd").equals("X")) {
				tCustMain.setCurrFloor(titaVo.getParam("CurrFloorBefore"));
			}

			// 通訊-樓之
			if (titaVo.getParam("CurrFloordashInd").equals("X")) {
				tCustMain.setCurrFloorDash(titaVo.getParam("CurrFloordashBefore"));
			}

			// 電子信箱
			if (titaVo.getParam("EmailInd").equals("X")) {
				tCustMain.setEmail(titaVo.getParam("EmailBefore"));
			}

			// 企金別
			if (titaVo.getParam("EntcodeInd").equals("X")) {
				tCustMain.setEntCode(titaVo.getParam("EntcodeBefore"));
			}

			// 英文姓名
			if (titaVo.getParam("EnameInd").equals("X")) {
				tCustMain.setEName(titaVo.getParam("EnameBefore"));
			}

			// 年收入
			if (titaVo.getParam("IncomeofyearlyInd").equals("X")) {
				tCustMain.setIncomeOfYearly(parse.stringToInteger(titaVo.getParam("IncomeofyearlyBefore")));
			}

			// 年收入資料年月
			if (titaVo.getParam("IncomedatadateInd").equals("X")) {
				if (titaVo.getParam("IncomedatadateBefore").equals("")) {
					tCustMain.setIncomeDataDate("");
				} else {
					tCustMain.setIncomeDataDate(
							"" + (parse.stringToInteger(titaVo.getParam("IncomedatadateBefore")) + 191100));
				}

			}

			tCustMain.setActFg(1);
			try {
				tCustMain = custMainService.update(tCustMain,titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}