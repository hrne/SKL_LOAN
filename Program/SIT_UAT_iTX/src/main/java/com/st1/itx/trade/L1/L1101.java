package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdIndustry;
import com.st1.itx.db.domain.CustCross;
import com.st1.itx.db.domain.CustCrossId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdIndustryService;
import com.st1.itx.db.service.CustCrossService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankRelationCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BankRelationVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunCd=9,1 CustId=X,10 CustName1=X,50 CustName2=X,50 Birthday=9,7 Sex=9,1
 * CustTypeCode=9,2 IndustryCode=9,6 NationalityCode=X,2 SpouseId=X,10
 * SpouseName=X,100 RegZip3=X,3 RegZip2=X,2 RegCityCode=X,2 RegAreaCode=X,3
 * RegIrCode=X,4 RegRoad=X,40 RegSection=X,5 RegAlley=X,5 RegLane=X,5 RegNum=X,5
 * RegNumDash=X,5 RegFloor=X,5 RegFloorDash=X,5 CurrZip3=X,3 CurrZip2=X,2
 * CurrCityCode=X,2 CurrAreaCode=X,3 CurrIrCode=X,4 CurrRoad=X,40
 * CurrSection=X,5 CurrAlley=X,5 CurrLane=X,5 CurrNum=X,5 CurrNumDash=X,5
 * CurrFloor=X,5 CurrFloorDash=X,5 IsLimit=X,1 IsRelated=X,1 IsLnrelNear=X,1
 * EntCode=X,1 EmpNo=X,6 EName=X,20 EduCode=X,1 OwnedHome=X,1 CurrCompName=X,60
 * CurrCompId=X,8 CurrCompTel=X,16 JobTitle=X,20 JobTenure=X,2
 * IncomeOfYearly=9,9 IncomeDataDate=X,6 PassportNo=X,20 AMLJobCode=X,3
 * AMLGroup=X,3 IndigenousName1=X,50 IndigenousName2=X,50
 */

/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
@Service("L1101")
@Scope("prototype")
public class L1101 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public CdIndustryService sCdIndustryService;
	@Autowired
	public CustTelNoService sCustTelNoService;
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdAreaService cdAreaService;
	@Autowired
	public LoanBorMainService sLoanBorMainService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public CustNoticeCom custNoticeCom;
	@Autowired
	public BankRelationCom bankRelationCom;
	@Autowired
	public CustCrossService iCustCrossService;
	@Autowired
	public CdCodeService iCdCodeService;

	@Autowired
	public Parse iParse;

	@Autowired
	SendRsp iSendRsp;

	@Autowired
	public DataLog iDataLog;

	private CustMain tCustMain;
	private boolean isEloan = false;
	private String wkIsLimit = "N";
	private String wkIsRelated = "N";
	private String wkIsLnrelNear = "N";
	CustMain beforeCustMain = new CustMain();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1101 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 300; // 300 * 185 = 55500

		int iChkFg = 0;

		// 功能 1:新增 4:刪除
		String funcd = titaVo.getParam("FunCd");

		// 統編
		String iCustId = titaVo.get("CustId");

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}
		// IncomeDataDate 年收入資料年月 X 5 YYYMM ex.10912

		// 先用統編查是否已在客戶資料主檔
		tCustMain = custMainService.custIdFirst(iCustId, titaVo);

		// Eloan 新增，已存在 => 修改
		if (funcd.equals("1") && this.isEloan && tCustMain != null) {
			funcd = "2";
		}
		// 錯誤處理
		if (funcd.equals("1") && tCustMain != null) { // 新增資料已存在
			throw new LogicException("E0002", "客戶資料主檔");
		} else if (funcd.equals("4") && tCustMain == null) { // 刪除資料不存在
			throw new LogicException("E0004", "客戶資料主檔");
		} else if ("5".equals(funcd) && tCustMain == null) {
			throw new LogicException("E0001", "客戶資料主檔"); // 查詢無資料
		}
		switch (funcd) {
		case "1": // 新增
			tCustMain = new CustMain();
			// 搬值
			setCstMain(titaVo);
			// 產生一組新的識別碼
			tCustMain.setCustUKey(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
			tCustMain.setCustId(iCustId);
			/* 存入DB */
			try {
				custMainService.insert(tCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "客戶資料主檔");
			}
//by eric 2021.7.31
			setCustCross(titaVo, tCustMain);
			break;
		case "2": // 修改
			// 變更前
			beforeCustMain = (CustMain) iDataLog.clone(tCustMain);
			// 搬值

			setCstMain(titaVo);
			try {
				tCustMain = custMainService.update2(tCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "客戶主檔" + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
			// 紀錄變更前變更後
			iDataLog.setEnv(titaVo, beforeCustMain, tCustMain);
			iDataLog.exec();
			break;
		case "4": // 刪除
//		刪除功能暫時先拔掉 資料刪除影響很多db
//		else if ("4".equals(funcd)) {
//			tCustMain = custMainService.holdById(tCustMain);
//			try {
//				custMainService.delete(tCustMain);
//			} catch (DBException e) {
//				throw new LogicException("E0008", "客戶資料主檔");
//			}
//		}
			break;
		case "5": // 查詢
			// 主管刷卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				this.info("主管 = " + titaVo.getEmpNos().trim());
				iChkFg = 0;
				iChkFg = inqLoanBorMain(tCustMain.getCustNo(), iChkFg, titaVo);
				if (iChkFg != 0) {
					iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "已結清滿5年");
				}
			}

			BankRelationVo vo = bankRelationCom.getBankRelation(iCustId, titaVo);

			if ("Y".equals(vo.getIsLimit())) {
				wkIsLimit = "Y"; // 是否為授信限制對象
			}
			if ("Y".equals(vo.getIsRelated())) {
				wkIsRelated = "Y"; // 是否為利害關係人
			}
			if ("Y".equals(vo.getIsLnrelNear())) {
				wkIsLnrelNear = "Y"; // 是否為準利害關係人
			}

			break;
		}

		if (!this.isEloan || "ELTEST".equals(titaVo.getTlrNo())) {
			setTota(titaVo);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setTota(TitaVo titaVo) throws LogicException {
		this.info("tCustMain = " + tCustMain);
		// 用客戶識別碼取電話資料
		Slice<CustTelNo> slCustTelNo = sCustTelNoService.findCustUKey(tCustMain.getCustUKey(), this.index, this.limit, titaVo);
		List<CustTelNo> lCustTelNo = slCustTelNo == null ? null : slCustTelNo.getContent();

		// 查詢行業別代號資料檔
		CdIndustry tCdIndustry = sCdIndustryService.findById(tCustMain.getIndustryCode(), titaVo);
		if (tCdIndustry == null) {
			tCdIndustry = new CdIndustry();
		}
		// 通訊地址
		String WkCurrAddres = custNoticeCom.getCurrAddress(tCustMain);
		this.info("CurrAddres" + WkCurrAddres);
		// 戶籍地址
		String WkRegAddres = custNoticeCom.getRegAddress(tCustMain);
		this.info("CurrAddres" + WkRegAddres);

		this.totaVo.putParam("OCustId", tCustMain.getCustId());
		this.totaVo.putParam("OCustNo", tCustMain.getCustNo());

		this.totaVo.putParam("OCustName", tCustMain.getCustName().replace("$n", "\n"));
		this.totaVo.putParam("OBirthday", tCustMain.getBirthday());
		this.totaVo.putParam("OSex", tCustMain.getSex());
		this.totaVo.putParam("OCustTypeCode", tCustMain.getCustTypeCode());
		this.totaVo.putParam("OIndustryCode", tCustMain.getIndustryCode());
		this.totaVo.putParam("OIndustryCodeX", tCdIndustry.getIndustryItem());
		this.totaVo.putParam("ONationalityCode", tCustMain.getNationalityCode());
		this.totaVo.putParam("OSpouseId", tCustMain.getSpouseId());
		this.totaVo.putParam("OSpouseName", tCustMain.getSpouseName().replace("$n", "\n"));
		this.totaVo.putParam("ORegZip3", tCustMain.getRegZip3());
		this.totaVo.putParam("ORegZip2", tCustMain.getRegZip2());
		this.totaVo.putParam("ORegaddress", WkRegAddres);
		this.totaVo.putParam("OCurrZip3", tCustMain.getCurrZip3());
		this.totaVo.putParam("OCurrZip2", tCustMain.getCurrZip2());
		this.totaVo.putParam("OCurraddress", WkCurrAddres);
		this.totaVo.putParam("OEMail", tCustMain.getEmail());
		this.totaVo.putParam("OIsLimit", wkIsLimit);
		this.totaVo.putParam("OIsRelated", wkIsRelated);
		this.totaVo.putParam("OIsLnrelNear", wkIsLnrelNear);
		this.totaVo.putParam("OEntCode", tCustMain.getEntCode());
		this.totaVo.putParam("OEmpNo", tCustMain.getEmpNo());
		this.totaVo.putParam("OEName", tCustMain.getEName());
		this.totaVo.putParam("OEduCode", tCustMain.getEduCode());
		this.totaVo.putParam("OOwnedHome", tCustMain.getOwnedHome());
		this.totaVo.putParam("OCurrCompName", tCustMain.getCurrCompName());
		this.totaVo.putParam("OCurrCompId", tCustMain.getCurrCompId());
		this.totaVo.putParam("OCurrCompTel", tCustMain.getCurrCompTel());
		this.totaVo.putParam("OJobTitle", tCustMain.getJobTitle());
		this.totaVo.putParam("OJobTenure", tCustMain.getJobTenure());
		this.totaVo.putParam("OIncomeOfYearly", tCustMain.getIncomeOfYearly());
		if (tCustMain.getIncomeDataDate().equals("") || tCustMain.getIncomeDataDate().equals("0")) {
			this.totaVo.putParam("OIncomeDataDate", "");
		} else {
			if (iParse.stringToInteger(tCustMain.getIncomeDataDate()) > 191100) {
				this.totaVo.putParam("OIncomeDataDate", iParse.stringToInteger(tCustMain.getIncomeDataDate()) - 191100);
			} else {
				this.totaVo.putParam("OIncomeDataDate", iParse.stringToInteger(tCustMain.getIncomeDataDate()));
			}
		}

		this.totaVo.putParam("OPassportNo", tCustMain.getPassportNo());
		this.totaVo.putParam("OAMLJobCode", tCustMain.getAMLJobCode());
		this.totaVo.putParam("OAMLGroup", tCustMain.getAMLGroup());
		this.totaVo.putParam("OIndigenousName", tCustMain.getIndigenousName());

		// 取介紹人名稱
		String wkIntroducerX = "";
		if (tCustMain.getIntroducer() != null) {
			CdEmp tCdEmp = cdEmpService.findById(tCustMain.getIntroducer(), titaVo);
			if (tCdEmp != null) {
				wkIntroducerX = tCdEmp.getFullname();
			}
		}

		this.totaVo.putParam("OIntroducerX", wkIntroducerX);
		this.totaVo.putParam("OCustCross", "Y");

//		交互運用
		Slice<CdCode> iCdCode = iCdCodeService.getCodeList(1, "SubCompanyCode", this.index, this.limit, titaVo);
		if (iCdCode == null) {
			throw new LogicException(titaVo, "E0001", "共用代碼檔查無子公司選項"); // 查無資料
		}
		int i = 1;
		for (CdCode xCdCode : iCdCode) {
			if (xCdCode.getEnable().equals("N")) {
				continue;
			}
			CustCross iCustCross = new CustCross();
			CustCrossId iCustCrossId = new CustCrossId();
			iCustCrossId.setCustUKey(tCustMain.getCustUKey());
			iCustCrossId.setSubCompanyCode(xCdCode.getCode());
			iCustCross = iCustCrossService.findById(iCustCrossId, titaVo);
			if (iCustCross == null) {
				this.totaVo.putParam("OSubCompanyCode" + i, xCdCode.getCode());
				this.totaVo.putParam("OCrossUse" + i, "N");
			} else {
				this.totaVo.putParam("OSubCompanyCode" + i, xCdCode.getCode());
				this.totaVo.putParam("OCrossUse" + i, iCustCross.getCrossUse());
			}
			i++;
		}
		while (true) {
			if (i > 20) {
				break;
			}
			this.totaVo.putParam("OSubCompanyCode" + i, "");
			this.totaVo.putParam("OCrossUse" + i, "");
			i++;
		}

//      客戶電話
		if (lCustTelNo == null || lCustTelNo.isEmpty()) {
			this.info("無電話資料");
			lCustTelNo = new ArrayList<CustTelNo>();
			CustTelNo tmpCustTelNo = new CustTelNo();
			lCustTelNo.add(tmpCustTelNo);
		}
		this.info("listCustTelNo = " + lCustTelNo);
		for (CustTelNo tCustTelNo : lCustTelNo) {
			OccursList occursList = new OccursList();
			this.info("tCustTelNo = " + tCustTelNo);

			occursList.putParam("OOTelTypeCode", tCustTelNo.getTelTypeCode());
			occursList.putParam("OOTelArea", tCustTelNo.getTelArea().trim());
			occursList.putParam("OOTelNo", tCustTelNo.getTelNo().trim());
			occursList.putParam("OOTelExt", tCustTelNo.getTelExt().trim());
			occursList.putParam("OORelationCode", tCustTelNo.getRelationCode());
			occursList.putParam("OOLiaisonName", tCustTelNo.getLiaisonName());
			occursList.putParam("OOEnable", tCustTelNo.getEnable());
			occursList.putParam("OORmk", tCustTelNo.getRmk());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}
	}

	private void setCstMain(TitaVo titaVo) throws LogicException {

		tCustMain.setCustName(titaVo.getParam("CustName"));
		tCustMain.setBirthday(iParse.stringToInteger(titaVo.getParam("Birthday")));
		tCustMain.setSex(titaVo.getParam("Sex"));
		tCustMain.setCustTypeCode(titaVo.getParam("CustTypeCode"));
		tCustMain.setIndustryCode(titaVo.getParam("IndustryCode"));
		tCustMain.setNationalityCode(titaVo.getParam("NationalityCode"));
		tCustMain.setSpouseId(titaVo.getParam("SpouseId"));
		tCustMain.setSpouseName(titaVo.getParam("SpouseName"));
		tCustMain.setRegZip3(titaVo.getParam("RegZip3"));
		tCustMain.setRegZip2(titaVo.getParam("RegZip2"));
		tCustMain.setRegCityCode(titaVo.getParam("RegCityCode"));
		tCustMain.setRegAreaCode(titaVo.getParam("RegAreaCode"));
		tCustMain.setRegRoad(titaVo.getParam("RegRoad"));
		tCustMain.setRegSection(titaVo.getParam("RegSection"));
		tCustMain.setRegAlley(titaVo.getParam("RegAlley"));
		tCustMain.setRegLane(titaVo.getParam("RegLane"));
		tCustMain.setRegNum(titaVo.getParam("RegNum"));
		tCustMain.setRegNumDash(titaVo.getParam("RegNumDash"));
		tCustMain.setRegFloor(titaVo.getParam("RegFloor"));
		tCustMain.setRegFloorDash(titaVo.getParam("RegFloorDash"));
		tCustMain.setCurrZip3(titaVo.getParam("CurrZip3"));
		tCustMain.setCurrZip2(titaVo.getParam("CurrZip2"));
		tCustMain.setCurrCityCode(titaVo.getParam("CurrCityCode"));
		tCustMain.setCurrAreaCode(titaVo.getParam("CurrAreaCode"));
		tCustMain.setCurrRoad(titaVo.getParam("CurrRoad"));
		tCustMain.setCurrSection(titaVo.getParam("CurrSection"));
		tCustMain.setCurrAlley(titaVo.getParam("CurrAlley"));
		tCustMain.setCurrLane(titaVo.getParam("CurrLane"));
		tCustMain.setCurrNum(titaVo.getParam("CurrNum"));
		tCustMain.setCurrNumDash(titaVo.getParam("CurrNumDash"));
		tCustMain.setCurrFloor(titaVo.getParam("CurrFloor"));
		tCustMain.setCurrFloorDash(titaVo.getParam("CurrFloorDash"));
		tCustMain.setEmail(titaVo.getParam("Email"));
//		tCustMain.setIsLimit(titaVo.getParam("IsLimit"));
//		tCustMain.setIsRelated(titaVo.getParam("IsRelated"));
//		tCustMain.setIsLnrelNear(titaVo.getParam("IsLnrelNear"));
		tCustMain.setEntCode(titaVo.getParam("EntCode"));
		tCustMain.setEmpNo(titaVo.getParam("EmpNo"));
		tCustMain.setEName(titaVo.getParam("EName"));
		tCustMain.setEduCode(titaVo.getParam("EduCode"));
		tCustMain.setOwnedHome(titaVo.getParam("OwnedHome"));
		tCustMain.setCurrCompName(titaVo.getParam("CurrCompName"));
		tCustMain.setCurrCompId(titaVo.getParam("CurrCompId"));
		tCustMain.setCurrCompTel(titaVo.getParam("CurrCompTel"));
		tCustMain.setJobTitle(titaVo.getParam("JobTitle"));
		tCustMain.setJobTenure(titaVo.getParam("JobTenure"));
		tCustMain.setIncomeOfYearly(iParse.stringToInteger(titaVo.getParam("IncomeOfYearly")));
		if (titaVo.getParam("IncomeDataDate").equals("")) {
			tCustMain.setIncomeDataDate("");
		} else if (iParse.stringToInteger(titaVo.getParam("IncomeDataDate")) > 191100) {
			tCustMain.setIncomeDataDate("" + iParse.stringToInteger(titaVo.getParam("IncomeDataDate")));
		} else {
			tCustMain.setIncomeDataDate("" + (iParse.stringToInteger(titaVo.getParam("IncomeDataDate")) + 191100));
		}

		tCustMain.setPassportNo(titaVo.getParam("PassportNo"));
		tCustMain.setAMLJobCode(titaVo.getParam("AMLJobCode"));
		tCustMain.setAMLGroup(titaVo.getParam("AMLGroup"));
		tCustMain.setIndigenousName(titaVo.getParam("IndigenousName"));
		if (beforeCustMain == null & (beforeCustMain.getIntroducer() == null || "".equals(beforeCustMain.getIntroducer()))) {
			tCustMain.setIntroducer("");
		} else {
			tCustMain.setIntroducer(beforeCustMain.getIntroducer());
		}
//		Introducer
		// 分行別預設0000 --2021.8.6 Fegie
		tCustMain.setBranchNo("0000");
	}

	// by eric 2021.7.31
	private void setCustCross(TitaVo titaVo, CustMain custMain) throws LogicException {
		String SubCompanyFg = titaVo.getParam("SubCompanyFg");
		this.info("active SubCompanyFg=" + SubCompanyFg + "/" + custMain.getCustUKey());
		if ("Y".equals(SubCompanyFg)) {
			for (int i = 1; i <= 20; i++) {
				String iSubCompanyCode = titaVo.get("SubCompanyCode" + i);
				this.info("active iSubCompanyCode=" + i + "/" + iSubCompanyCode);
				if (iSubCompanyCode == null || "".equals(iSubCompanyCode.trim())) {
					break;
				}
				String iCrossUse = titaVo.get("CrossUse" + i);
				if ("".equals(iCrossUse)) {
					iCrossUse = "N";
				}
				CustCrossId fCustCrossId = new CustCrossId(); // findbyId
				fCustCrossId.setCustUKey(custMain.getCustUKey());
				fCustCrossId.setSubCompanyCode(iSubCompanyCode);
				CustCross fCustCross = iCustCrossService.holdById(fCustCrossId, titaVo);
				if (fCustCross == null) {
					// insert
					fCustCross = new CustCross(); // init
					fCustCross.setCustCrossId(fCustCrossId);
					fCustCross.setCrossUse(iCrossUse);
					try {
						iCustCrossService.insert(fCustCross, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0005", "新增時發生錯誤");
					}
				} else {
					// update
					CustCross beforeCustCross = (CustCross) iDataLog.clone(fCustCross);
					fCustCross.setCrossUse(iCrossUse);
					try {
						fCustCross = iCustCrossService.update2(fCustCross, titaVo);
					} catch (DBException e) {
						throw new LogicException("E0007", "更新時發生錯誤");
					}

					// 紀錄變更前變更後
					iDataLog.setEnv(titaVo, beforeCustCross, fCustCross);
					iDataLog.exec();
				}
			}

		}
	}

	// 查詢放款主檔
	private int inqLoanBorMain(int cCustNo, int cChkFg, TitaVo titaVo) throws LogicException {

		Slice<LoanBorMain> slLoanBorMain = null;
		slLoanBorMain = sLoanBorMainService.bormCustNoEq(cCustNo, 0, 999, 0, 999, 0, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

		if (lLoanBorMain == null || lLoanBorMain.isEmpty()) {
			cChkFg = 0;
			return cChkFg;
		}

		for (LoanBorMain tLoanBorMain : lLoanBorMain) {

			// 0:正常戶 2:催收戶 4:逾期戶 6:呆帳戶 7:部分轉呆戶 => 不需授權
			if (tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 2 || tLoanBorMain.getStatus() == 4 || tLoanBorMain.getStatus() == 6 || tLoanBorMain.getStatus() == 7) {
				cChkFg = 0;
				return cChkFg;
			}

			// 3:結案戶 5:催收結案戶 8:債權轉讓戶 9:呆帳結案戶 => 滿5年需授權
			this.info("tLoanBorMain.getAcDate() = " + tLoanBorMain.getAcDate());
			this.info("this.txBuffer.getTxCom().getTbsdy() = " + this.txBuffer.getTxCom().getTbsdy());
			if (tLoanBorMain.getAcDate() + 50000 <= this.txBuffer.getTxCom().getTbsdy()) {
				cChkFg = 1;
				this.info("  > 5 year = " + cChkFg);
			} else {
				cChkFg = 0;
				this.info("  < 5 year = " + cChkFg);
				return cChkFg;
			}
		}

		return cChkFg;

	}

}