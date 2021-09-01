package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustId=X,10 CustIdInd=X,1 CustIdAft=X,10 CustNameInd=X,1 CustName1Aft=X,50
 * CustName2Aft=X,50 BirthdayInd=X,1 BirthdayAft=9,7 GenderInd=X,1 GenderAft=9,1
 * CustTypeInd=X,1 CustTypeAft=9,2 IndustryInd=X,1 IndustryAft=9,6
 * CountryInd=X,1 CountryAft=X,2 SpouseIdInd=X,1 SpouseIdAft=X,10
 * SpouseNmInd=X,1 SpouseNmAft=X,100 RegZip3Ind=X,1 RegZip3Aft=9,3
 * RegZip2Ind=X,1 RegZip2Aft=9,2 RegCityCodeInd=X,1 RegCityCodeAft=X,2
 * RegAreaCodeInd=X,1 RegAreaCodeAft=X,3 RegIrcodeInd=X,1 RegIrcodeAft=X,4
 * RegRoadInd=X,1 RegRoadAft=X,40 RegSctionInd=X,1 RegSctionAft=X,5
 * RegAlleyInd=X,1 RegAlleyAft=X,5 RegLaneInd=X,1 RegLaneAft=X,5 RegNumInd=X,1
 * RegNumAft=X,5 RegNumDashInd=X,1 RegNumDashAft=X,5 RegFloorInd=X,1
 * RegFloorAft=X,5 RegFloorDashInd=X,1 RegFloorDashAft=X,5 CurrZip3Ind=X,1
 * CurrZip3Aft=X,3 CurrZip2Ind=X,1 CurrZip2Aft=X,2 CurrCityCodeInd=X,1
 * CurrCityCodeAft=X,2 CurrAreaCodeInd=X,1 CurrAreaCodeAft=X,3 CurrIrcodeInd=X,1
 * CurrIrcodeAft=X,4 CurrRoadInd=X,1 CurrRoadAft=X,40 CurrSectionInd=X,1
 * CurrSectionAft=X,5 CurrAlleyInd=X,1 CurrAlleyAft=X,5 CurrLaneInd=X,1
 * CurrLaneAft=X,5 CurrNumInd=X,1 CurrNumAft=X,5 CurrNumDashInd=X,1
 * CurrNumDashAft=X,5 CurrFloorInd=X,1 CurrFloorAft=X,5 CurrFloorDashInd=X,1
 * CurrFloorDashAft=X,5 IslimitInd=X,1 IslimitAft=X,1 IsrelatedInd=X,1
 * IsrelatedAft=X,1 IsrelnearInd=X,1 IsrelnearAft=X,1 EntcodeInd=X,1
 * EntcodeAft=9,1 EmpnoInd=X,1 EmpnoAft=X,6 EnameInd=X,1 EnameAft=X,20
 * EducodeInd=X,1 EducodeAft=X,1 OwnedhomeInd=X,1 OwnedhomeAft=X,1
 * CurrcompnameInd=X,1 CurrcompnameAft=X,60 CurrcompidInd=X,1 CurrcompidAft=X,8
 * CurrcomptelInd=X,1 CurrcomptelAft=X,16 JobtitleInd=X,1 JobtitleAft=X,20
 * JobTenureInd=X,1 JobTenureAft=X,2 IncomeofyearlyInd=X,1 IncomeofyearlyAft=9,9
 * IncomedatadateInd=X,1 IncomedatadateAft=X,6 PassportnoInd=X,1
 * PassportnoAft=X,20 AmljobcodeInd=X,1 AmljobcodeAft=X,3 AmlgroupInd=X,1
 * AmlgroupAft=X,3 IndigenousnameInd=X,1 IndigenousnameAft=X,100
 */

@Service("L1103")
@Scope("prototype")
public class L1103 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public Parse iParse;

	@Autowired
	public DataLog iDataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1103 ");
		this.totaVo.init(titaVo);

		titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		String custid = titaVo.get("CustId");
		CustMain tCustMain = iCustMainService.custIdFirst(custid);

		// 例外處理 若查無資料請至L1101新增
		if (tCustMain == null) {
			throw new LogicException("E1003", "客戶資料主檔");
		}
		// 鎖定這筆
		tCustMain = iCustMainService.holdById(tCustMain);

		// 經辦登錄時更新
		if (titaVo.isActfgEntry()) {

			// 若該欄位有被修改,更新該欄位資料

			// 如果要修改統編
			if (titaVo.getParam("CustIdInd").trim().equals("X")) {
				// 修改後的統編
				String new_custid = titaVo.getParam("CustIdAft");

				// 先檢查新統編是否已存在
				CustMain tCustMain2 = iCustMainService.custIdFirst(new_custid);

				// 新統編不存在,可修改
				if (tCustMain2 == null) {
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
				// 維護中櫃員代號
//				tCustMain.setMainTenanceTlrNo(titaVo.getTlrNo());

				// 訂正交易
			} else {

				tCustMain.setActFg(0);
				// 維護中櫃員代號
//				tCustMain.setMainTenanceTlrNo("");
			}
			try {
				// 更新資料
				tCustMain = iCustMainService.update2(tCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "客戶資料主檔");

			}

		}
		// 放行一般
		if (titaVo.isActfgSuprele() && titaVo.isHcodeNormal()) {
			this.info("放行一般");
			this.info("tCustMain =" + tCustMain);

			if (tCustMain.getActFg() != 1) {
				throw new LogicException(titaVo, "E0017", " "); // 該筆交易狀態非待放行，不可做交易放行
			}
			// 變更前
			CustMain BefCustMain = (CustMain) iDataLog.clone(tCustMain);
			// 單位別
			if (titaVo.getParam("BranchNoInd").equals("X")) {
				tCustMain.setBranchNo(titaVo.getParam("BranchNo"));
			}

			// 建檔客戶別
			if (titaVo.getParam("TypeCodeInd").equals("X")) {
				tCustMain.setTypeCode(iParse.stringToInteger(titaVo.getParam("TypeCodeAft")));
			}

			// 戶名
			if (titaVo.getParam("CustNameInd").equals("X")) {
				tCustMain.setCustName(titaVo.getParam("CustNameAft"));
			}

			// 出生年月日
			if (titaVo.getParam("BirthdayInd").equals("X")) {
				tCustMain.setBirthday(iParse.stringToInteger(titaVo.getParam("BirthdayAft")));
			}

			// 性別
			if (titaVo.getParam("GenderInd").equals("X")) {
				tCustMain.setSex(titaVo.getParam("GenderAft"));
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

			// 配偶身分證字號
			if (titaVo.getParam("SpouseIdInd").equals("X")) {
				tCustMain.setSpouseId(titaVo.getParam("SpouseIdAft"));
			}

			// 配偶姓名
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
			if (titaVo.getParam("RegCityCodeInd").equals("X")) {
				tCustMain.setRegCityCode(titaVo.getParam("RegCityCodeAft"));
			}

			// 戶籍-鄉鎮市區代碼
			if (titaVo.getParam("RegAreaCodeInd").equals("X")) {
				tCustMain.setRegAreaCode(titaVo.getParam("RegAreaCodeAft"));
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
			if (titaVo.getParam("RegNumDashInd").equals("X")) {
				tCustMain.setRegNumDash(titaVo.getParam("RegNumDashAft"));
			}

			// 戶籍-樓
			if (titaVo.getParam("RegFloorInd").equals("X")) {
				tCustMain.setRegFloor(titaVo.getParam("RegFloorAft"));
			}

			// 戶籍-樓之
			if (titaVo.getParam("RegFloorDashInd").equals("X")) {
				tCustMain.setRegFloorDash(titaVo.getParam("RegFloorDashAft"));
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
			if (titaVo.getParam("CurrCityCodeInd").equals("X")) {
				tCustMain.setCurrCityCode(titaVo.getParam("CurrCityCodeAft"));
			}

			// 通訊-鄉鎮市區代碼
			if (titaVo.getParam("CurrAreaCodeInd").equals("X")) {
				tCustMain.setCurrAreaCode(titaVo.getParam("CurrAreaCodeAft"));
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
			if (titaVo.getParam("CurrNumDashInd").equals("X")) {
				tCustMain.setCurrNumDash(titaVo.getParam("CurrNumDashAft"));
			}

			// 通訊-樓
			if (titaVo.getParam("CurrFloorInd").equals("X")) {
				tCustMain.setCurrFloor(titaVo.getParam("CurrFloorAft"));
			}

			// 通訊-樓之
			if (titaVo.getParam("CurrFloorDashInd").equals("X")) {
				tCustMain.setCurrFloorDash(titaVo.getParam("CurrFloorDashAft"));
			}

			// 電子信箱
			if (titaVo.getParam("EmailInd").equals("X")) {
				tCustMain.setEmail(titaVo.getParam("EmailAft"));
			}

			// 企金別
			if (titaVo.getParam("EntcodeInd").equals("X")) {
				tCustMain.setEntCode(titaVo.getParam("EntcodeAft"));
			}

			// 員工代號
			if (titaVo.getParam("EmpnoInd").equals("X")) {
				tCustMain.setEmpNo(titaVo.getParam("EmpnoAft"));
			}

			// 英文姓名
			if (titaVo.getParam("EnameInd").equals("X")) {
				tCustMain.setEName(titaVo.getParam("EnameAft"));
			}

			// 教育程度代號
			if (titaVo.getParam("EducodeInd").equals("X")) {
				tCustMain.setEduCode(titaVo.getParam("EducodeAft"));
			}

			// 自有住宅有無
			if (titaVo.getParam("OwnedhomeInd").equals("X")) {
				tCustMain.setOwnedHome(titaVo.getParam("OwnedhomeAft"));
			}

			// 任職機構名稱
			if (titaVo.getParam("CurrcompnameInd").equals("X")) {
				tCustMain.setCurrCompName(titaVo.getParam("CurrcompnameAft"));
			}

			// 任職機構統編
			if (titaVo.getParam("CurrcompidInd").equals("X")) {
				tCustMain.setCurrCompId(titaVo.getParam("CurrcompidAft"));
			}

			// 任職機構電話
			if (titaVo.getParam("CurrcomptelInd").equals("X")) {
				tCustMain.setCurrCompTel(titaVo.getParam("CurrcomptelAft"));
			}

			// 職位名稱
			if (titaVo.getParam("JobtitleInd").equals("X")) {
				tCustMain.setJobTitle(titaVo.getParam("JobtitleAft"));
			}

			// 服務年資
			if (titaVo.getParam("JobTenureInd").equals("X")) {
				tCustMain.setJobTenure(titaVo.getParam("JobTenureAft"));
			}

			// 年收入
			if (titaVo.getParam("IncomeofyearlyInd").equals("X")) {
				tCustMain.setIncomeOfYearly(iParse.stringToInteger(titaVo.getParam("IncomeofyearlyAft")));
			}

			// 年收入資料年月
			if (titaVo.getParam("IncomedatadateInd").equals("X")) {
				if (titaVo.getParam("IncomedatadateAft").equals("")) {
					tCustMain.setIncomeDataDate("");
				} else {
					tCustMain.setIncomeDataDate(
							"" + (iParse.stringToInteger(titaVo.getParam("IncomedatadateAft")) + 191100));
				}

			}

			// 護照號碼
			if (titaVo.getParam("PassportnoInd").equals("X")) {
				tCustMain.setPassportNo(titaVo.getParam("PassportnoAft"));
			}

			// AML職業別
			if (titaVo.getParam("AmljobcodeInd").equals("X")) {
				tCustMain.setAMLJobCode(titaVo.getParam("AmljobcodeAft"));
			}

			// AML組織
			if (titaVo.getParam("AmlgroupInd").equals("X")) {
				tCustMain.setAMLGroup(titaVo.getParam("AmlgroupAft"));
			}

			// 原住民姓名
			if (titaVo.getParam("IndigenousnameInd").equals("X")) {
				tCustMain.setIndigenousName(titaVo.getParam("IndigenousnameAft"));
			}
			tCustMain.setActFg(2);
			// 維護中櫃員代號 放行清空
//			tCustMain.setMainTenanceTlrNo("");

			try {
				tCustMain = iCustMainService.update2(tCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
			}

			// 紀錄變更前變更後
			iDataLog.setEnv(titaVo, BefCustMain, tCustMain);
			iDataLog.exec();
		}

		// 放行訂正
		if (titaVo.isActfgSuprele() && titaVo.isHcodeErase()) {
			this.info("放行訂正");
			if (tCustMain.getActFg() != 2) {
				throw new LogicException(titaVo, "E0018", " "); // 該筆交易狀態非已放行，不可做訂正已放行交易
			}

			// 變更前
			CustMain BefCustMain = (CustMain) iDataLog.clone(tCustMain);

			// 建檔客戶別
			if (titaVo.getParam("TypeCodeInd").equals("X")) {
				tCustMain.setTypeCode(iParse.stringToInteger(titaVo.getParam("TypeCodeBef")));
			}
			
			// 戶名
			if (titaVo.getParam("CustNameInd").equals("X")) {
				tCustMain.setCustName(titaVo.getParam("CustNameBef"));
			}

			// 出生年月日
			if (titaVo.getParam("BirthdayInd").equals("X")) {
				tCustMain.setBirthday(iParse.stringToInteger(titaVo.getParam("BirthdayBef")));
			}

			// 性別
			if (titaVo.getParam("GenderInd").equals("X")) {
				tCustMain.setSex(titaVo.getParam("GenderBef"));
			}

			// 客戶別
			if (titaVo.getParam("CustTypeInd").equals("X")) {
				tCustMain.setCustTypeCode(titaVo.getParam("CustTypeBef"));
			}

			// 行業別
			if (titaVo.getParam("IndustryInd").equals("X")) {
				tCustMain.setIndustryCode(titaVo.getParam("IndustryBef"));
			}

			// 國籍
			if (titaVo.getParam("CountryInd").equals("X")) {
				tCustMain.setNationalityCode(titaVo.getParam("CountryBef"));
			}

			// 配偶身分證字號
			if (titaVo.getParam("SpouseIdInd").equals("X")) {
				tCustMain.setSpouseId(titaVo.getParam("SpouseIdBef"));
			}

			// 配偶姓名
			if (titaVo.getParam("SpouseNmInd").equals("X")) {
				tCustMain.setSpouseName(titaVo.getParam("SpouseNmBef"));
			}

			// 戶籍-郵遞區號前三碼
			if (titaVo.getParam("RegZip3Ind").equals("X")) {
				tCustMain.setRegZip3(titaVo.getParam("RegZip3Bef"));
			}

			// 戶籍-郵遞區號後兩碼
			if (titaVo.getParam("RegZip2Ind").equals("X")) {
				tCustMain.setRegZip2(titaVo.getParam("RegZip2Bef"));
			}

			// 戶籍-縣市代碼
			if (titaVo.getParam("RegCityCodeInd").equals("X")) {
				tCustMain.setRegCityCode(titaVo.getParam("RegCityCodeBef"));
			}

			// 戶籍-鄉鎮市區代碼
			if (titaVo.getParam("RegAreaCodeInd").equals("X")) {
				tCustMain.setRegAreaCode(titaVo.getParam("RegAreaCodeBef"));
			}

			// 戶籍-路名
			if (titaVo.getParam("RegRoadInd").equals("X")) {
				tCustMain.setRegRoad(titaVo.getParam("RegRoadBef"));
			}

			// 戶籍-段
			if (titaVo.getParam("RegSectionInd").equals("X")) {
				tCustMain.setRegSection(titaVo.getParam("RegSectionBef"));
			}

			// 戶籍-巷
			if (titaVo.getParam("RegAlleyInd").equals("X")) {
				tCustMain.setRegAlley(titaVo.getParam("RegAlleyBef"));
			}

			// 戶籍-弄
			if (titaVo.getParam("RegLaneInd").equals("X")) {
				tCustMain.setRegLane(titaVo.getParam("RegLaneBef"));
			}

			// 戶籍-號
			if (titaVo.getParam("RegNumInd").equals("X")) {
				tCustMain.setRegNum(titaVo.getParam("RegNumBef"));
			}

			// 戶籍-號之
			if (titaVo.getParam("RegNumDashInd").equals("X")) {
				tCustMain.setRegNumDash(titaVo.getParam("RegNumDashBef"));
			}

			// 戶籍-樓
			if (titaVo.getParam("RegFloorInd").equals("X")) {
				tCustMain.setRegFloor(titaVo.getParam("RegFloorBef"));
			}

			// 戶籍-樓之
			if (titaVo.getParam("RegFloorDashInd").equals("X")) {
				tCustMain.setRegFloorDash(titaVo.getParam("RegFloorDashBef"));
			}

			// 通訊-郵遞區號前三碼
			if (titaVo.getParam("CurrZip3Ind").equals("X")) {
				tCustMain.setCurrZip3(titaVo.getParam("CurrZip3Bef"));
			}

			// 通訊-郵遞區號後兩碼
			if (titaVo.getParam("CurrZip2Ind").equals("X")) {
				tCustMain.setCurrZip2(titaVo.getParam("CurrZip2Bef"));
			}

			// 通訊-縣市代碼
			if (titaVo.getParam("CurrCityCodeInd").equals("X")) {
				tCustMain.setCurrCityCode(titaVo.getParam("CurrCityCodeBef"));
			}

			// 通訊-鄉鎮市區代碼
			if (titaVo.getParam("CurrAreaCodeInd").equals("X")) {
				tCustMain.setCurrAreaCode(titaVo.getParam("CurrAreaCodeBef"));
			}

			// 通訊-路名
			if (titaVo.getParam("CurrRoadInd").equals("X")) {
				tCustMain.setCurrRoad(titaVo.getParam("CurrRoadBef"));
			}

			// 通訊-段
			if (titaVo.getParam("CurrSectionInd").equals("X")) {
				tCustMain.setCurrSection(titaVo.getParam("CurrSectionBef"));
			}

			// 通訊-巷
			if (titaVo.getParam("CurrAlleyInd").equals("X")) {
				tCustMain.setCurrAlley(titaVo.getParam("CurrAlleyBef"));
			}

			// 通訊-弄
			if (titaVo.getParam("CurrLaneInd").equals("X")) {
				tCustMain.setCurrLane(titaVo.getParam("CurrLaneBef"));
			}

			// 通訊-號
			if (titaVo.getParam("CurrNumInd").equals("X")) {
				tCustMain.setCurrNum(titaVo.getParam("CurrNumBef"));
			}

			// 通訊-號之
			if (titaVo.getParam("CurrNumDashInd").equals("X")) {
				tCustMain.setCurrNumDash(titaVo.getParam("CurrNumDashBef"));
			}

			// 通訊-樓
			if (titaVo.getParam("CurrFloorInd").equals("X")) {
				tCustMain.setCurrFloor(titaVo.getParam("CurrFloorBef"));
			}

			// 通訊-樓之
			if (titaVo.getParam("CurrFloorDashInd").equals("X")) {
				tCustMain.setCurrFloorDash(titaVo.getParam("CurrFloorDashBef"));
			}

			// 電子信箱
			if (titaVo.getParam("EmailInd").equals("X")) {
				tCustMain.setEmail(titaVo.getParam("EmailBef"));
			}

			// 企金別
			if (titaVo.getParam("EntcodeInd").equals("X")) {
				tCustMain.setEntCode(titaVo.getParam("EntcodeBef"));
			}

			// 員工代號
			if (titaVo.getParam("EmpnoInd").equals("X")) {
				tCustMain.setEmpNo(titaVo.getParam("EmpnoBef"));
			}

			// 英文姓名
			if (titaVo.getParam("EnameInd").equals("X")) {
				tCustMain.setEName(titaVo.getParam("EnameBef"));
			}

			// 教育程度代號
			if (titaVo.getParam("EducodeInd").equals("X")) {
				tCustMain.setEduCode(titaVo.getParam("EducodeBef"));
			}

			// 自有住宅有無
			if (titaVo.getParam("OwnedhomeInd").equals("X")) {
				tCustMain.setOwnedHome(titaVo.getParam("OwnedhomeBef"));
			}

			// 任職機構名稱
			if (titaVo.getParam("CurrcompnameInd").equals("X")) {
				tCustMain.setCurrCompName(titaVo.getParam("CurrcompnameBef"));
			}

			// 任職機構統編
			if (titaVo.getParam("CurrcompidInd").equals("X")) {
				tCustMain.setCurrCompId(titaVo.getParam("CurrcompidBef"));
			}

			// 任職機構電話
			if (titaVo.getParam("CurrcomptelInd").equals("X")) {
				tCustMain.setCurrCompTel(titaVo.getParam("CurrcomptelBef"));
			}

			// 職位名稱
			if (titaVo.getParam("JobtitleInd").equals("X")) {
				tCustMain.setJobTitle(titaVo.getParam("JobtitleBef"));
			}

			// 服務年資
			if (titaVo.getParam("JobTenureInd").equals("X")) {
				tCustMain.setJobTenure(titaVo.getParam("JobTenureBef"));
			}

			// 年收入
			if (titaVo.getParam("IncomeofyearlyInd").equals("X")) {
				tCustMain.setIncomeOfYearly(iParse.stringToInteger(titaVo.getParam("IncomeofyearlyBef")));
			}

			// 年收入資料年月
			if (titaVo.getParam("IncomedatadateInd").equals("X")) {
				if (titaVo.getParam("IncomedatadateBef").equals("")) {
					tCustMain.setIncomeDataDate("");
				} else {
					tCustMain.setIncomeDataDate(
							"" + (iParse.stringToInteger(titaVo.getParam("IncomedatadateBef")) + 191100));
				}

			}

			// 護照號碼
			if (titaVo.getParam("PassportnoInd").equals("X")) {
				tCustMain.setPassportNo(titaVo.getParam("PassportnoBef"));
			}

			// AML職業別
			if (titaVo.getParam("AmljobcodeInd").equals("X")) {
				tCustMain.setAMLJobCode(titaVo.getParam("AmljobcodeBef"));
			}

			// AML組織
			if (titaVo.getParam("AmlgroupInd").equals("X")) {
				tCustMain.setAMLGroup(titaVo.getParam("AmlgroupBef"));
			}

			// 原住民姓名
			if (titaVo.getParam("IndigenousnameInd").equals("X")) {
				tCustMain.setIndigenousName(titaVo.getParam("IndigenousnameBef"));
			}
			tCustMain.setActFg(1);
			// 維護中櫃員代號
//			tCustMain.setMainTenanceTlrNo(titaVo.getTlrNo());
			try {
				tCustMain = iCustMainService.update2(tCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
			// 紀錄變更前變更後
			iDataLog.setEnv(titaVo, BefCustMain, tCustMain);
			iDataLog.exec();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}