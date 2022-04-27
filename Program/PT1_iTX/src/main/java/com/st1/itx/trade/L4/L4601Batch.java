package com.st1.itx.trade.L4;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4601Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4601Batch extends TradeBuffer {

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public InsuRenewFileVo insuRenewFileVo;

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public InsuRenewMediaTempService insuRenewMediaTempService;

	@Autowired
	public AcReceivableCom acReceivableCom;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public TotaVo totaC;

	@Autowired
	public WebClient webClient;

	private String checkMsg = "";
	private int errorACnt = 0;
	private int errorBCnt = 0;
	private int errorCCnt = 0;

	private int succesCnt = 0;

	private int iInsuEndMonth = 0;
	private String checkResultA = "";
	private String checkResultB = "";
	private String checkResultC = "";

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4601Batch ");
		this.totaVo.init(titaVo);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

//			吃檔
//			String filePath1 = "D:\\temp\\test\\火險\\Test\\Return\\1)R-10904LNM01P.txt";
		String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		ArrayList<String> dataLineList = new ArrayList<>();

//		if (titaVo.getParam("FILENA").trim().indexOf("LNM01P") < 0) {
//			throw new LogicException("E0014", "檔案錯誤，檔名 : " + titaVo.getParam("FILENA").trim());
//		}

//			 編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filePath1, "big5");
		} catch (IOException e) {
			throw new LogicException("E0014", "L4601(" + filePath1 + ") : " + e.getMessage());
		}

//			 使用資料容器內定義的方法切資料
		insuRenewFileVo.setValueFromFile(dataLineList);

		ArrayList<OccursList> uploadFile = insuRenewFileVo.getOccursList();

		if (uploadFile != null && uploadFile.size() != 0) {

			deleInsuRenewMediaTemp(titaVo);

			for (OccursList t : uploadFile) {
				if (iInsuEndMonth != parse.stringToInteger(t.get("FireInsuMonth"))) {
					throw new LogicException("E0014", "輸入火險年月與檔案不同");
				}
				checkResultA = "";
				checkResultB = "";
				checkResultC = "";

//		1.火險詢價上傳檔轉檔作業(檢核清單)
//			1.總保費=0) 
//			2.無資料(無此戶號額度、擔保品號碼
				InsuRenew tInsuRenew = insuRenewService.prevInsuNoFirst(parse.stringToInteger(t.get("CustNo").trim()),
						parse.stringToInteger(t.get("FacmNo").trim()), t.get("InsuNo").trim(), titaVo);
//				無此保單號碼
				if (tInsuRenew == null) {
//					檢查無此戶號額度
					Slice<InsuRenew> slInsuRenew = insuRenewService.findL4601A(
							parse.stringToInteger(t.get("FireInsuMonth")),
							parse.stringToInteger(t.get("CustNo").trim()),
							parse.stringToInteger(t.get("FacmNo").trim()), 0, 1, titaVo);
					if (slInsuRenew == null) {
						checkResultA += "12";
					} else {
						checkResultA += "10";
					}
				} else {
					checkReportA(t, tInsuRenew, titaVo);
				}
//					a.檢核
				if ("".equals(checkResultA)) {

					resetAcReceivable(2, tInsuRenew, titaVo); // 2-起帳刪除
					tInsuRenew.setFireInsuCovrg(parse.stringToBigDecimal(t.get("NewFireInsuAmt").trim()));
					tInsuRenew.setEthqInsuCovrg(parse.stringToBigDecimal(t.get("NewEqInsuAmt").trim()));
					tInsuRenew.setFireInsuPrem(parse.stringToBigDecimal(t.get("NewFireInsuFee").trim()));
					tInsuRenew.setEthqInsuPrem(parse.stringToBigDecimal(t.get("NewEqInsuFee").trim()));
					tInsuRenew.setInsuStartDate(parse.stringToInteger(t.get("NewInsuStartDate").replaceAll("/", "")));
					tInsuRenew.setInsuEndDate(parse.stringToInteger(t.get("NewInsuEndDate").replaceAll("/", "")));
					tInsuRenew.setTotInsuPrem(parse.stringToBigDecimal(t.get("NewTotalFee").trim()));
					try {
						insuRenewService.update(tInsuRenew);
					} catch (DBException e) {
						throw new LogicException("E0007", "L4601 InsuRenew update " + e.getErrorMsg());
					}
					resetAcReceivable(0, tInsuRenew, titaVo); // 0-起帳
					succesCnt = succesCnt + 1;
					checkReportB(tInsuRenew, titaVo);
					checkReportC(tInsuRenew, titaVo);
				} else {
					errorACnt = errorACnt + 1;
				}

				InsuRenewMediaTemp tInsuRenewMediaTemp = new InsuRenewMediaTemp();

				tInsuRenewMediaTemp = this.getInsuRenewMediaTemp(t, tInsuRenewMediaTemp, titaVo);
				// 檢核結果
				tInsuRenewMediaTemp.setCheckResultA(checkResultA);
				tInsuRenewMediaTemp.setCheckResultB(checkResultB);
				tInsuRenewMediaTemp.setCheckResultC(checkResultC);

				try {
					insuRenewMediaTempService.insert(tInsuRenewMediaTemp);
				} catch (DBException e) {
					throw new LogicException("E0005", "L4601 InsuRenewMediaTemp insert " + e.getErrorMsg());
				}
			}
		}

		checkMsg = "請點擊訊息列印報表，檢核正常筆數：" + succesCnt + "筆，檢核錯誤筆數：" + errorACnt + "筆。";

		checkMsg += "火險詢價重複投保：" + errorBCnt + "筆。";

		checkMsg += "續保資料錯誤：" + errorCCnt + "筆。";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4601",
				"2" + titaVo.getParam("InsuEndMonth"), checkMsg, titaVo);

		return null;
	}

	private void resetAcReceivable(int flag, InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		this.info("resetAcReceivable.." + flag + ", " + tInsuRenew.toString());
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();

		if (tInsuRenew.getRenewCode() != 2) {
			this.info("skip AcReceivable RenewCode" + tInsuRenew.getRenewCode());
			return;
		}

		if (tInsuRenew.getStatusCode() > 0) {
			this.info("skip AcReceivable StatusCode= " + tInsuRenew.getStatusCode());
			return;
		}
		if (tInsuRenew.getTotInsuPrem().compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		
		// 已入帳
		if(tInsuRenew.getAcDate() > 0) {
			return;
		}
		
		AcReceivable acReceivable = new AcReceivable();
		acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
		acReceivable.setAcctCode("TMI"); // 業務科目
		acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
		acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
		acReceivable.setFacmNo(tInsuRenew.getFacmNo());
		acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
		acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
		TempVo tTempVo = new TempVo();
		tTempVo.clear();
		tTempVo.putParam("InsuYearMonth", tInsuRenew.getInsuYearMonth());
		acReceivable.setJsonFields(tTempVo.getJsonString());
		acReceivableList.add(acReceivable);
		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(flag, acReceivableList, titaVo); // 0-起帳 1-銷帳 2-起帳刪除
	}

	public InsuRenewMediaTemp getInsuRenewMediaTemp(OccursList occursList, InsuRenewMediaTemp t, TitaVo titaVo)
			throws LogicException {

//	FireInsuMonth		火險到期年月	X	6	
		t.setFireInsuMonth(occursList.get("FireInsuMonth").trim());
//	ReturnCode	回傳碼
		t.setReturnCode(occursList.get("ReturnCode").trim());
//	InsuCampCode	保險公司代碼
		t.setInsuCampCode(occursList.get("InsuCampCode").trim());
//	InsuCustId	提供人統一編號
		t.setInsuCustId(occursList.get("InsuCustId").trim());
//	InsuCustName	提供人姓名
		t.setInsuCustName(occursList.get("InsuCustName").trim());
//	LoanCustId	借款人統一編號
		t.setLoanCustId(occursList.get("LoanCustId").trim());
//	LoanCustName	借款人姓名
		t.setLoanCustName(occursList.get("LoanCustName").trim());
//	PostalCode	郵遞區號
		t.setPostalCode(occursList.get("PostalCode").trim());
//	Address	門牌號碼
		t.setAddress(occursList.get("Address").trim());
//	BuildingSquare	主建物坪數
		t.setBuildingSquare(occursList.get("BuildingSquare").trim());
//	BuildingCode	建物結構代碼
		t.setBuildingCode(occursList.get("BuildingCode").trim());
//	BuildingYears	建造年份
		t.setBuildingYears(occursList.get("BuildingYears").trim());
//	BuildingFloors	樓層數
		t.setBuildingFloors(occursList.get("BuildingFloors").trim());
//	RoofCode	屋頂結構代碼
		t.setRoofCode(occursList.get("RoofCode").trim());
//	BusinessUnit	營業單位別
		t.setBusinessUnit(occursList.get("BusinessUnit").trim());
//	ClCode1	押品別１
		t.setClCode1(occursList.get("ClCode1").trim());
//	ClCode2	押品別２
		t.setClCode2(occursList.get("ClCode2").trim());
//	ClNo	押品號碼
		t.setClNo(occursList.get("ClNo").trim());
//	Seq	序號
		t.setSeq(occursList.get("Seq").trim());
//  InsuNo	保單號碼	
		t.setInsuNo(occursList.get("InsuNo").trim());
//	InsuStartDate	保險起日
		t.setInsuStartDate(occursList.get("InsuStartDate").trim());
//	InsuEndDate	保險迄日
		t.setInsuEndDate(occursList.get("InsuEndDate").trim());
//	FireInsuAmt	火險保額
		t.setFireInsuAmt(occursList.get("FireInsuAmt").trim());
//	FireInsuFee	火險保費
		t.setFireInsuFee(occursList.get("FireInsuFee").trim());
//	EqInsuAmt	地震險保額
		t.setEqInsuAmt(occursList.get("EqInsuAmt").trim());
//	EqInsuFee	地震險保費
		t.setEqInsuFee(occursList.get("EqInsuFee").trim());
//	CustNo	借款人戶號
		t.setCustNo(occursList.get("CustNo").trim());
//	FacmNo	額度編號
		t.setFacmNo(occursList.get("FacmNo").trim());
//	SendDate	傳檔日期
		t.setSendDate(occursList.get("SendDate").trim());
//	NewInusNo	保單號碼(新)
		t.setNewInusNo(occursList.get("NewInusNo").trim());
//	NewInsuStartDate	保險起日(新)
		t.setNewInsuStartDate(occursList.get("NewInsuStartDate").trim());
//	NewInsuEndDate	保險迄日(新)
		t.setNewInsuEndDate(occursList.get("NewInsuEndDate").trim());
//	NewFireInsuAmt	火險保額(新)
		t.setNewFireInsuAmt(occursList.get("NewFireInsuAmt").trim());
//	NewFireInsuFee	火險保費(新)
		t.setNewFireInsuFee(occursList.get("NewFireInsuFee").trim());
//	NewEqInsuAmt	地震險保額(新)
		t.setNewEqInsuAmt(occursList.get("NewEqInsuAmt").trim());
//	NewEqInsuFee	地震險保費(新)
		t.setNewEqInsuFee(occursList.get("NewEqInsuFee").trim());
//	NewTotalFee	總保費(新)
		t.setNewTotalFee(occursList.get("NewTotalFee").trim());
//	Remark1	備註一
		t.setRemark1(occursList.get("Remark1").trim());
//	MailingAddress	通訊地址
		t.setMailingAddress(occursList.get("MailingAddress").trim());
//	Remark2	備註二
		t.setRemark2(occursList.get("Remark2").trim());
//	SklSalesName	新光人壽業務員名稱
		t.setSklSalesName(occursList.get("SklSalesName").trim());
//	SklUnitCode	新光人壽單位代號
		t.setSklUnitCode(occursList.get("SklUnitCode").trim());
//	SklUnitName	新光人壽單位中文
		t.setSklUnitName(occursList.get("SklUnitName").trim());
//	SklSalesCode	新光人壽業務員代號
		t.setSklSalesCode(occursList.get("SklSalesCode").trim());
//	RenewTrlCode	新產續保經辦代號
		t.setRenewTrlCode(occursList.get("RenewTrlCode").trim());
//	RenewUnit	新產續保單位
		t.setRenewUnit(occursList.get("RenewUnit").trim());
		return t;
	}

	private void deleInsuRenewMediaTemp(TitaVo titaVo) throws LogicException {
		Slice<InsuRenewMediaTemp> slInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuMonthRg("" + iInsuEndMonth,
				"" + iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);

		if (slInsuRenewMediaTemp != null) {
			try {
				insuRenewMediaTempService.deleteAll(slInsuRenewMediaTemp.getContent(), titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "InsuRenew insert error : " + e.getErrorMsg());
			}
		}
	}

	private void checkReportA(OccursList t, InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		this.info("ReportA Start...");
//  	總金額=0
		if (parse.stringToBigDecimal(t.get("NewTotalFee")).compareTo(BigDecimal.ZERO) == 0) {
			if ("".equals(checkResultA)) {
				checkResultA = checkResultA + "11";
			} else {
				checkResultA = checkResultA + ",11";
			}
			return;
		}
//		已入帳,總保費與入帳金額不符 
		if (tInsuRenew.getAcDate() > 0
				&& parse.stringToBigDecimal(t.get("NewTotalFee")).compareTo(tInsuRenew.getTotInsuPrem()) != 0) {
			if ("".equals(checkResultA)) {
				checkResultA = checkResultA + "15";
			} else {
				checkResultA = checkResultA + ",15";
			}
			return;
		}
//
//		if ("Y".equals(tInsuRenew.getNotiTempFg())) {
//			if ("".equals(checkResultA)) {
//				checkResultA += "13";
//			} else {
//				checkResultA += ",13";
//			}
//			return;
//		}

//		已入通知檔
		if ("Y".equals(tInsuRenew.getNotiTempFg())) {
			if ("".equals(checkResultA)) {
				checkResultA += "13";
			} else {
				checkResultA += ",13";
			}
			return;
		}

//		處理代碼非0.正常
		if (tInsuRenew.getStatusCode() > 0) {
			if ("".equals(checkResultA)) {
				checkResultA += "14";
			} else {
				checkResultA += ",14";
			}
			return;
		}

	}

	private void checkReportB(InsuRenew t, TitaVo titaVo) throws LogicException {
		this.info("ReportB Start...");
		Slice<InsuRenew> sl2InsuRenew = insuRenewService.findL4601B(calYear(iInsuEndMonth, -1), t.getClCode1(),
				t.getClCode2(), t.getClNo(), 0, 1, titaVo);
		InsuRenew t2InsuRenew = null;
		if (sl2InsuRenew != null) {
			t2InsuRenew = sl2InsuRenew.getContent().get(0);
		}

//		或 新保檔之保險迄日 不等於 新寫入續保檔之保險起日
		InsuOrignal t2InsuOrignal = insuOrignalService.clNoFirst(t.getClCode1(), t.getClCode2(), t.getClNo(), titaVo);

		if (t2InsuRenew != null) {
			if (t2InsuRenew.getInsuEndDate() != t.getInsuStartDate()) {
				if ("".equals(checkResultB)) {
					checkResultB += "21";
				} else {
					checkResultB += ",21";
				}
			}
		} else if (t2InsuOrignal != null) {
			if (t2InsuOrignal.getInsuEndDate() != t.getInsuStartDate()) {
				if ("".equals(checkResultB)) {
					checkResultB += "22";
				} else {
					checkResultB += ",22";
				}
			}
		}

//		重複投保
		Slice<InsuRenew> sl3InsuRenew = insuRenewService.findL4601B(iInsuEndMonth, t.getClCode1(), t.getClCode2(),
				t.getClNo(), 0, 2, titaVo);
		if (sl3InsuRenew != null && sl3InsuRenew.getContent().size() >= 2) {
			if ("".equals(checkResultB)) {
				checkResultB += "23";
			} else {
				checkResultB += ",23";
			}
		}

		if (!"".equals(checkResultB)) {
			errorBCnt = errorBCnt + 1;
		}

	}

	/**
	 * 檢核未撥款或已結案
	 * 
	 * @param t      InsuRenew
	 * @param titaVo ..
	 * @throws LogicException ..
	 */
	private void checkReportC(InsuRenew t, TitaVo titaVo) throws LogicException {
		// 未撥款或已結案
		boolean isClose = true;
		boolean isUnLoan = true;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(t.getCustNo(), t.getFacmNo(), t.getFacmNo(),
				0, 900, 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorMain != null) {
			isUnLoan = false;
			for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
				if (tLoanBorMain.getLoanBal().compareTo(BigDecimal.ZERO) > 0) {
					isClose = false;
				}
			}
		}
		// 已結案
		if (isClose) {
			if ("".equals(checkResultC)) {
				checkResultC += "31";
			} else {
				checkResultC += ",31";
			}
		}
		// 未撥款
		if (isUnLoan) {
			if ("".equals(checkResultC)) {
				checkResultC += "32";
			} else {
				checkResultC += ",32";
			}
		}

		if (!"".equals(checkResultC)) {
			errorCCnt = errorCCnt + 1;
		}

	}

	private int calYear(int today, int year) throws LogicException {
		int resultMonth = 0;
//	10801
//	201901
//	1080101
//	trans today = Bc format
		if (today < 100000) {
			today = parse.stringToInteger((today + 191100) + "01");
		} else if (today < 1000000) {
			today = parse.stringToInteger(today + "01");
		} else if (today < 10000000) {
			today = today + 19110000;
		}

		dateUtil.init();
		dateUtil.setDate_1(today);
		dateUtil.setYears(year);
		today = dateUtil.getCalenderDay();

		resultMonth = parse.stringToInteger((today + "").substring(0, 6));

		return resultMonth;
	}

}