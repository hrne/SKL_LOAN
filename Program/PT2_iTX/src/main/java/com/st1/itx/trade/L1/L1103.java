package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

@Service("L1103")
@Scope("prototype")
public class L1103 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	/* DB服務注入 */
	@Autowired
	public CustTelNoService sCustTelNoService;

	@Autowired
	public TxDataLogService txDataLogService;

	@Autowired
	public Parse iParse;

	@Autowired
	public DataLog iDataLog;
	
	@Autowired
	public SendRsp sendRsp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1103 ");
		this.totaVo.init(titaVo);

		titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		String iCustUKey = titaVo.get("CustUKey");

		String custid = titaVo.get("CustId");

		CustMain tCustMain = new CustMain();

		if (iCustUKey != null && !iCustUKey.isEmpty()) {
			tCustMain = iCustMainService.findById(iCustUKey, titaVo);
		} else {
			tCustMain = iCustMainService.custIdFirst(custid, titaVo);
		}

		// 例外處理 若查無資料請至L1101新增
		if (tCustMain == null) {
			throw new LogicException("E1003", "客戶資料主檔");
		}

		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0101", "");
		}
		
		// 鎖定這筆
		tCustMain = iCustMainService.holdById(tCustMain);

		mntCust(titaVo,tCustMain);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void mntCust(TitaVo titaVo, CustMain tCustMain) throws LogicException {
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

		// 出生地國籍
		if (titaVo.getParam("CountryInd").equals("X")) {
			tCustMain.setNationalityCode(titaVo.getParam("CountryAft"));
		}

		// 居住地國籍
		if (titaVo.getParam("BussNationalityCodeInd").equals("X")) {
			tCustMain.setBussNationalityCode(titaVo.getParam("BussNationalityCodeAft"));
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

		// 介紹人
		if (titaVo.getParam("IntroducerInd").equals("X")) {
			tCustMain.setIntroducer(titaVo.getParam("IntroducerAft"));
		}

		// 房貸專員
		if (titaVo.getParam("BusinessOfficerInd").equals("X")) {
			tCustMain.setBusinessOfficer(titaVo.getParam("BusinessOfficerAft"));
		}

		// 站別
		if (titaVo.getParam("StationInd").equals("X")) {
			tCustMain.setStation(titaVo.getParam("StationAft"));
		}

		tCustMain.setActFg(2);
		// 維護中櫃員代號 放行清空
//		tCustMain.setMainTenanceTlrNo("");

		try {
			tCustMain = iCustMainService.update2(tCustMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 更新資料時，發生錯誤
		}

		// 紀錄變更前變更後
		iDataLog.setEnv(titaVo, BefCustMain, tCustMain);
		iDataLog.exec("修改顧客資料", "CustUKey:" + tCustMain.getCustUKey());
		
		// 若修改戶名,則同步維護電話檔(只維護與顧客關係=00本人之資料)
		if (titaVo.getParam("CustNameInd").equals("X")) {
			String custUKey = tCustMain.getCustUKey().trim();
			List<CustTelNo> lCustTelNo = new ArrayList<CustTelNo>();
			Slice<CustTelNo> slCustTelNo = sCustTelNoService.findCustUKey(custUKey, this.index, this.limit, titaVo);
			lCustTelNo = slCustTelNo == null ? null : slCustTelNo.getContent();
			/* 如有找到資料 */
			if (lCustTelNo != null && lCustTelNo.size() > 0) {
				for (CustTelNo tCustTelNo : lCustTelNo) {
					if (tCustTelNo.getRelationCode().trim().equals("00")) {// 本人-更新聯絡人姓名
						// 鎖定這筆
						tCustTelNo = sCustTelNoService.holdById(tCustTelNo);
						// 變更前
						CustTelNo BefCustTelNo = (CustTelNo) iDataLog.clone(tCustTelNo);
						tCustTelNo.setLiaisonName(titaVo.getParam("CustNameAft"));
						try {
							tCustTelNo = sCustTelNoService.update2(tCustTelNo, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", "客戶聯絡電話檔" + e.getErrorMsg()); // 更新資料時，發生錯誤
						}
						// 紀錄變更前變更後
						iDataLog.setEnv(titaVo, BefCustTelNo, tCustTelNo);
						iDataLog.exec("修改戶名同步維護客戶聯絡電話檔聯絡人本人姓名", "統一編號:" + titaVo.get("CustId"));
					}
				}
			}
		}

		
	}
}