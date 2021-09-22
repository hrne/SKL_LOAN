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
import com.st1.itx.db.domain.CdIndustry;
import com.st1.itx.db.domain.CustCross;
import com.st1.itx.db.domain.CustCrossId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.CustTelNo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
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
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
 * FunCd=9,1 CustId=X,10 CustName1=X,50 CustName2=X,50 Birthday=9,7
 * CustTypeCode=9,2 IndustryCode=9,6 NationalityCode=X,2 SpouseId=X,10
 * SpouseName=X,100 RegZip3=X,3 RegZip2=X,2 RegCityCode=X,2 RegAreaCode=X,3
 * RegIrCode=X,4 RegRoad=X,40 RegSection=X,5 RegAlley=X,5 RegLane=X,5 RegNum=X,5
 * RegNumDash=X,5 RegFloor=X,5 RegFloorDash=X,5 CurrZip3=X,3 CurrZip2=X,2
 * CurrCityCode=X,2 CurrAreaCode=X,3 CurrIrCode=X,4 CurrRoad=X,40
 * CurrSection=X,5 CurrAlley=X,5 CurrLane=X,5 CurrNum=X,5 CurrNumDash=X,5
 * CurrFloor=X,5 CurrFloorDash=X,5 EntCode=X,1 EName=X,20
 */

@Service("L1102")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1102 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public CustTelNoService sCustTelNoService;
	@Autowired
	public LoanBorMainService sLoanBorMainService;
	@Autowired
	public CdIndustryService sCdIndustryService;
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdAreaService cdAreaService;
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
	private String wkIsLimit = "N";
	private String wkIsRelated = "N";
	private String wkIsLnrelNear = "N";
	private String wkIsDataDate = "";
	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1102 ");
		this.totaVo.init(titaVo);

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		int iChkFg = 0;
		// 取統編先確定是否已在客戶資料主檔測試
		String CustId = titaVo.get("CustId").trim();

		tCustMain = custMainService.custIdFirst(CustId);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 300; // 300 * 185 = 55500
		// 功能 1:新增 4:刪除
		String funcd = titaVo.getParam("FunCd");

		// Eloan 新增，已存在 => 修改
		if (funcd.equals("1") && this.isEloan && tCustMain != null) {
			funcd = "2";
		}

		// 錯誤處理
		if (funcd.equals("1") && tCustMain != null) { // 新增資料已存在
			// 2021.8.29 by eric
			if (tCustMain.getDataStatus() == 1) {
				funcd = "2";
			} else {
				throw new LogicException("E0002", "客戶資料主檔");
			}

		}

		// 新增
		if (funcd.equals("1")) {

			if (tCustMain != null) {
				throw new LogicException("E0002", "客戶資料主檔");
			}

			tCustMain = new CustMain();

			// 產生一組新的識別碼
			tCustMain.setCustUKey(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
			tCustMain.setCustId(CustId);

			setCstMain(titaVo);

			/* 存入DB */

			try {
				custMainService.insert(tCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "客戶資料主檔");
			}
			// 如果是查詢先主管刷卡

			// by eric 2021.7.31
			setCustCross(titaVo, tCustMain);
		} else if (funcd.equals("2")) {
			// 變更前
			CustMain beforeCustMain = (CustMain) iDataLog.clone(tCustMain);
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

			// by eric 2021.7.31
			setCustCross(titaVo, tCustMain);

		} else if ("5".equals(funcd)) {
			if (titaVo.getEmpNos().trim().isEmpty()) {
				this.info("主管 = " + titaVo.getEmpNos().trim());

				iChkFg = 0;
				iChkFg = inqLoanBorMain(tCustMain.getCustNo(), iChkFg, titaVo);
				if (iChkFg != 0)
					iSendRsp.addvReason(this.txBuffer, titaVo, "0004", "已結清滿5年");
			}
			BankRelationVo vo = bankRelationCom.getBankRelation(CustId, titaVo);

			if ("Y".equals(vo.getIsLimit())) {
				wkIsLimit = "Y"; // 是否為授信限制對象
			}
			if ("Y".equals(vo.getIsRelated())) {
				wkIsRelated = "Y"; // 是否為利害關係人
			}
			if ("Y".equals(vo.getIsLnrelNear())) {
				wkIsLnrelNear = "Y"; // 是否為準利害關係人
			}
			wkIsDataDate = vo.getDataDate();
		}

//			刪除功能
//		刪除功能暫時先拔掉 資料刪除影響很多db
//		else if ("4".equals(funcd)) {
//			tCustMain = custMainService.holdById(tCustMain);
//			try {
//				custMainService.delete(tCustMain);
//			} catch (DBException e) {
//				throw new LogicException("E0008", "客戶資料主檔");
//			}
//		}

		this.info("tCustMain = " + tCustMain);
		// 用客戶識別碼取電話資料
		Slice<CustTelNo> slCustTelNo = sCustTelNoService.findCustUKey(tCustMain.getCustUKey(), 0, Integer.MAX_VALUE,
				titaVo);
		List<CustTelNo> lCustTelNo = slCustTelNo == null ? null : slCustTelNo.getContent();

		// 查詢行業別代號資料檔
		CdIndustry tCdIndustry = sCdIndustryService.findById(tCustMain.getIndustryCode(), titaVo);
		if (tCdIndustry == null) {
			tCdIndustry = new CdIndustry();
		}

		// 通訊地址
		String WkCurrAddres = custNoticeCom.getCurrAddress(tCustMain,titaVo);
		this.info("CurrAddres" + WkCurrAddres);
		// 戶籍地址
		String WkRegAddres = custNoticeCom.getRegAddress(tCustMain,titaVo);
		this.info("CurrAddres" + WkRegAddres);

		this.totaVo.putParam("OCustId", tCustMain.getCustId());
		this.totaVo.putParam("OCustNo", tCustMain.getCustNo());
		this.totaVo.putParam("OTypeCode", tCustMain.getTypeCode());
		this.totaVo.putParam("OCustName", tCustMain.getCustName().replace("$n", "\n"));
		this.totaVo.putParam("OBirthday", tCustMain.getBirthday());
		this.totaVo.putParam("OCustTypeCode", tCustMain.getCustTypeCode());
		this.totaVo.putParam("OIndustryCode", tCustMain.getIndustryCode());
		this.totaVo.putParam("OIndustryCodeX", tCdIndustry.getIndustryItem());
		this.totaVo.putParam("ONationalityCode", tCustMain.getNationalityCode());
		this.totaVo.putParam("OBussNationalityCode", tCustMain.getBussNationalityCode());
		this.totaVo.putParam("OSpouseId", tCustMain.getSpouseId());
		this.totaVo.putParam("OSpouseName", tCustMain.getSpouseName().replace("$n", "\n"));
		this.totaVo.putParam("ORegZip3", tCustMain.getRegZip3());
		this.totaVo.putParam("ORegZip2", tCustMain.getRegZip2());
		this.totaVo.putParam("ORegaddress", WkRegAddres);
		this.totaVo.putParam("OCurrZip3", tCustMain.getCurrZip3());
		this.totaVo.putParam("OCurrZip2", tCustMain.getCurrZip2());
		this.totaVo.putParam("OCurraddress", WkCurrAddres);
		this.totaVo.putParam("OEmail", tCustMain.getEmail());
		this.totaVo.putParam("OIsLimit", wkIsLimit);
		this.totaVo.putParam("OIsRelated", wkIsRelated);
		this.totaVo.putParam("OIsLnrelNear", wkIsLnrelNear);
		this.totaVo.putParam("OIsDataDate", wkIsDataDate);
		this.totaVo.putParam("OEntCode", tCustMain.getEntCode());
		this.totaVo.putParam("OEName", tCustMain.getEName());
		this.totaVo.putParam("OIncomeOfYearly", tCustMain.getIncomeOfYearly());
		if (tCustMain.getIncomeDataDate().equals("") || tCustMain.getIncomeDataDate().equals("0")) {
			this.totaVo.putParam("OIncomeDataDate", "");
		} else {
			this.totaVo.putParam("OIncomeDataDate", iParse.stringToInteger(tCustMain.getIncomeDataDate()) - 191100);
		}
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

		if (lCustTelNo == null || lCustTelNo.size() == 0) {
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
//			occursList.putParam("OOMobile", tCustTelNo.getMobile().trim());
			occursList.putParam("OORelationCode", tCustTelNo.getRelationCode());
			occursList.putParam("OOLiaisonName", tCustTelNo.getLiaisonName());
			occursList.putParam("OOEnable", tCustTelNo.getEnable());
			occursList.putParam("OORmk", tCustTelNo.getRmk());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setCstMain(TitaVo titaVo) throws LogicException {

		tCustMain.setCustName(titaVo.getParam("CustName"));
		tCustMain.setBirthday(iParse.stringToInteger(titaVo.getParam("Birthday")));
		tCustMain.setCustTypeCode(titaVo.getParam("CustTypeCode"));
		tCustMain.setIndustryCode(titaVo.getParam("IndustryCode"));
		tCustMain.setNationalityCode(titaVo.getParam("NationalityCode"));
		tCustMain.setBussNationalityCode(titaVo.getParam("BussNationalityCode"));
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
		tCustMain.setEntCode(titaVo.getParam("EntCode"));
		tCustMain.setEName(titaVo.getParam("EName"));
		tCustMain.setIncomeOfYearly(iParse.stringToInteger(titaVo.getParam("IncomeOfYearly")));

		if (titaVo.getParam("IncomeDataDate").equals("")) {
			tCustMain.setIncomeDataDate("");
		} else {
			tCustMain.setIncomeDataDate("" + (iParse.stringToInteger(titaVo.getParam("IncomeDataDate")) + 191100));
		}

		// 分行別預設0000 --2021.8.6 Fegie
		tCustMain.setBranchNo("0000");

		tCustMain.setIsSuspected(titaVo.getParam("IsSuspected"));
		tCustMain.setIsSuspectedCheck(titaVo.getParam("IsSuspectedCheck"));
		tCustMain.setIsSuspectedCheckType(titaVo.getParam("IsSuspectedCheckType"));
		
		tCustMain.setDataStatus(0);
	}

	// 查詢放款主檔
	private int inqLoanBorMain(int cCustNo, int cChkFg, TitaVo titaVo) throws LogicException {

		Slice<LoanBorMain> slLoanBorMain = null;
		slLoanBorMain = sLoanBorMainService.bormCustNoEq(cCustNo, 0, 999, 0, 999, 0, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			cChkFg = 0;
			return cChkFg;
		}

		for (LoanBorMain tLoanBorMain : lLoanBorMain) {

			// 0:正常戶 2:催收戶 4:逾期戶 6:呆帳戶 7:部分轉呆戶 => 不需授權
			if (tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 2 || tLoanBorMain.getStatus() == 4
					|| tLoanBorMain.getStatus() == 6 || tLoanBorMain.getStatus() == 7) {
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

}