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
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

@Service("L1104")
@Scope("prototype")
public class L1104 extends TradeBuffer {

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
		this.info("active L1104 ");
		this.totaVo.init(titaVo);

		String custid = titaVo.getParam("CustId").trim();
		CustMain tCustMain = iCustMainService.custIdFirst(custid);

		// 例外處理 若查無資料請至L1102新增
		if (tCustMain == null) {
			throw new LogicException("E1003", "客戶資料主檔");
		}
		
		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0101", "");
		}
		
		// 鎖定這筆
		iCustMainService.holdById(tCustMain);

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

		// 公司名稱
		if (titaVo.getParam("CustNameInd").equals("X")) {
			// if (!titaVo.getParam("CUSTNM_IND").isEmpty()) {
			tCustMain.setCustName(titaVo.getParam("CustNameAft"));
		}

		// 設立日期
		if (titaVo.getParam("BirthdayInd").equals("X")) {
			tCustMain.setBirthday(iParse.stringToInteger(titaVo.getParam("BirthdayAft")));
		}

		// 建檔客戶別
		if (titaVo.getParam("TypeCodeInd").equals("X")) {
			tCustMain.setTypeCode(iParse.stringToInteger(titaVo.getParam("TypeCodeAft")));
		}

		// 客戶別
		if (titaVo.getParam("CustTypeInd").equals("X")) {
			tCustMain.setCustTypeCode(titaVo.getParam("CustTypeAft"));
		}

		// 行業別
		if (titaVo.getParam("IndustryInd").equals("X")) {
			tCustMain.setIndustryCode(titaVo.getParam("IndustryAft"));
		}

		// 公司註冊地
		if (titaVo.getParam("NationalityCodeInd").equals("X")) {
			tCustMain.setNationalityCode(titaVo.getParam("NationalityCodeAft"));
		}

		// 營業地國籍
		if (titaVo.getParam("BussNationalityCodeInd").equals("X")) {
			tCustMain.setBussNationalityCode(titaVo.getParam("BussNationalityCodeAft"));
		}

		// 負責人身分證
		if (titaVo.getParam("SpouseIdInd").equals("X")) {
			tCustMain.setSpouseId(titaVo.getParam("SpouseIdAft"));
		}

		// 負責人姓名
		if (titaVo.getParam("SpouseNameInd").equals("X")) {
			tCustMain.setSpouseName(titaVo.getParam("SpouseNameAft"));
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

		// 英文姓名
		if (titaVo.getParam("EnameInd").equals("X")) {
			tCustMain.setEName(titaVo.getParam("EnameAft"));
		}

		// 年收入
		if (titaVo.getParam("IncomeofyearlyInd").equals("X")) {
			tCustMain.setIncomeOfYearly(iParse.stringToInteger(titaVo.getParam("IncomeofyearlyAft")));
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
		
		
		// 年收入資料年月
		if (titaVo.getParam("IncomeDataDateInd").equals("X")) {
			if (titaVo.getParam("IncomeDataDateAft").equals("")) {
				tCustMain.setIncomeDataDate("");
			} else {
				tCustMain.setIncomeDataDate(
						"" + (iParse.stringToInteger(titaVo.getParam("IncomeDataDateAft")) + 191100));
			}

		}

		tCustMain.setActFg(2);
		try {
			tCustMain = iCustMainService.update2(tCustMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
		String iResFg = titaVo.getParam("Reason");
//		HELP(1:客戶申請;2:資料修正)
		String iRes = "";
		if(iResFg.equals("1")) {
			iRes="客戶申請";
		}
		if(iResFg.equals("2")) {
			iRes="資料修正";
		}
		
		// 紀錄變更前變更後
		iDataLog.setEnv(titaVo, BefCustMain, tCustMain);
		iDataLog.exec("修改顧客資料/"
		   +BefCustMain.getCustName() +"  "
		   +"原因註記/"+iRes
//		   +BefCustMain.getEName()
//		   +BefCustMain.getBirthday()+BefCustMain.getSex()
//		   +BefCustMain.getCustTypeCode()+BefCustMain.getEmpNo()
//		   +BefCustMain.getEntCode()+BefCustMain.getNationalityCode()
//		   +BefCustMain.getBussNationalityCode()+BefCustMain.getSpouseId()
//		   //地址
//		   +BefCustMain.getSpouseName()+BefCustMain.getRegCityCode()
//		   +BefCustMain.getRegAreaCode()+BefCustMain.getRegRoad()
//		   +BefCustMain.getRegSection()+BefCustMain.getRegAlley()
//		   +BefCustMain.getRegLane()+BefCustMain.getRegNum()
//		   +BefCustMain.getRegNumDash()+BefCustMain.getRegFloor()
//		   +BefCustMain.getRegFloorDash()+BefCustMain.getRegZip3()
//		   +BefCustMain.getRegZip2()
//		   +BefCustMain.getCurrCityCode()+BefCustMain.getCurrAreaCode()
//		   +BefCustMain.getCurrRoad()+BefCustMain.getCurrSection()
//		   +BefCustMain.getCurrAlley()+BefCustMain.getCurrLane()
//		   +BefCustMain.getCurrNum()+BefCustMain.getCurrNumDash()
//		   +BefCustMain.getCurrFloor() +BefCustMain.getCurrFloorDash()
//		   +BefCustMain.getCurrZip3()+BefCustMain.getCurrZip2()
//		   +BefCustMain.getEmail()+BefCustMain.getEduCode()
//		   +BefCustMain.getOwnedHome()+BefCustMain.getCurrCompName()
//		   +BefCustMain.getCurrCompId()+BefCustMain.getCurrCompTel()
//		   +BefCustMain.getJobTitle()+BefCustMain.getJobTenure()
//		   +BefCustMain.getIncomeOfYearly()+BefCustMain.getIncomeDataDate()
//		   +BefCustMain.getPassportNo()+BefCustMain.getAMLJobCode()
//		   +BefCustMain.getAMLGroup()+BefCustMain.getIndigenousName()
//		   +BefCustMain.getIntroducer()+BefCustMain.getBusinessOfficer()
//		   +BefCustMain.getStation()
		   , "CustUKey:" + tCustMain.getCustUKey());

		// 若修改公司名稱,則同步維護電話檔(只維護與顧客關係=00本人之資料)
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
						iDataLog.exec("修改公司名稱同步維護客戶聯絡電話檔聯絡人本人姓名"+BefCustTelNo.getLiaisonName(), "統一編號:" + titaVo.get("CustId"));
					}
				}
			}
		}
		
	}
	
	//原二段式交易
	public ArrayList<TotaVo> run2(TitaVo titaVo) throws LogicException {
		this.info("active L1104 ");
		this.totaVo.init(titaVo);

		String custid = titaVo.getParam("CustId").trim();
		CustMain tCustMain = iCustMainService.custIdFirst(custid);

		// 例外處理 若查無資料請至L1102新增
		if (tCustMain == null) {
			throw new LogicException("E1003", "客戶資料主檔");
		}
		
		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0101", "身份證號／統一編號變更");
		}

		// 鎖定這筆
		iCustMainService.holdById(tCustMain);

		// 經辦登錄時更新
		if (titaVo.isActfgEntry()) {

			// 若該欄位有被修改,更新該欄位資料

			// 如果要修改統編
			if (titaVo.getParam("CustIdInd").trim().equals("X")) {
				// 修改後的統編
				String new_custid = titaVo.getParam("CustIdAfter").trim();

				// 先檢查新統編是否已存在
				CustMain lCustMain2 = iCustMainService.custIdFirst(new_custid);

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
				tCustMain = iCustMainService.update2(tCustMain, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2)
					throw new LogicException("E0007", "客戶資料主檔");

			}

		}

		// 放行一般
		if (titaVo.isActfgSuprele() && titaVo.isHcodeNormal()) {
			if (tCustMain.getActFg() != 1) {
				throw new LogicException(titaVo, "E0017", " "); // 該筆交易狀態非待放行，不可做交易放行
			}

			mntCust(titaVo,tCustMain);

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
				tCustMain.setCustName(titaVo.getParam("CustNameBef"));
			}

			// 設立日期
			if (titaVo.getParam("BirthdayInd").equals("X")) {
				tCustMain.setBirthday(iParse.stringToInteger(titaVo.getParam("BirthdayBef")));
			}

			// 客戶別
			if (titaVo.getParam("CustTypeInd").equals("X")) {
				tCustMain.setCustTypeCode(titaVo.getParam("CustTypeBef"));
			}

			// 行業別
			if (titaVo.getParam("IndustryInd").equals("X")) {
				tCustMain.setIndustryCode(titaVo.getParam("IndustryBef"));
			}

			// 公司註冊地
			if (titaVo.getParam("NationalityCodeInd").equals("X")) {
				tCustMain.setNationalityCode(titaVo.getParam("NationalityCodeBef"));
			}

			// 營業地國籍
			if (titaVo.getParam("BussNationalityCodeInd").equals("X")) {
				tCustMain.setBussNationalityCode(titaVo.getParam("BussNationalityCodeBef"));
			}

			// 負責人身分證
			if (titaVo.getParam("SpouseIdInd").equals("X")) {
				tCustMain.setSpouseId(titaVo.getParam("SpouseIdBef"));
			}

			// 負責人姓名
			if (titaVo.getParam("SpouseNameInd").equals("X")) {
				tCustMain.setSpouseName(titaVo.getParam("SpouseNameBef"));
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

			// 英文姓名
			if (titaVo.getParam("EnameInd").equals("X")) {
				tCustMain.setEName(titaVo.getParam("EnameBef"));
			}

			// 年收入
			if (titaVo.getParam("IncomeofyearlyInd").equals("X")) {
				tCustMain.setIncomeOfYearly(iParse.stringToInteger(titaVo.getParam("IncomeofyearlyBef")));
			}

			// 年收入資料年月
			if (titaVo.getParam("IncomeDataDateInd").equals("X")) {
				if (titaVo.getParam("IncomeDataDateBef").equals("")) {
					tCustMain.setIncomeDataDate("");
				} else {
					tCustMain.setIncomeDataDate(
							"" + (iParse.stringToInteger(titaVo.getParam("IncomeDataDateBef")) + 191100));
				}

			}

			tCustMain.setActFg(1);
			try {
				tCustMain = iCustMainService.update(tCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
			}

//刪除變更記錄

			iDataLog.delete(titaVo);

			// 同步維護電話檔(只維護與顧客關係=00本人之資料)
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
							tCustTelNo.setLiaisonName(titaVo.getParam("CustNameBef"));
							try {
								tCustTelNo = sCustTelNoService.update2(tCustTelNo, titaVo);
							} catch (DBException e) {
								throw new LogicException(titaVo, "E0007", "客戶聯絡電話檔" + e.getErrorMsg()); // 更新資料時，發生錯誤
							}
							iDataLog.delete(titaVo);
						}
					}
				}
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}