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

	@Autowired
	public L4601Report l4601Report;

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

		if (titaVo.getParam("FunCd").equals("1")) {

//			??????
//			String filePath1 = "D:\\temp\\test\\??????\\Test\\Return\\1)R-10904LNM01P.txt";
			String filePath1 = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
					+ File.separatorChar + titaVo.getParam("FILENA").trim();

			ArrayList<String> dataLineList = new ArrayList<>();

//		if (titaVo.getParam("FILENA").trim().indexOf("LNM01P") < 0) {
//			throw new LogicException("E0014", "????????????????????? : " + titaVo.getParam("FILENA").trim());
//		}

//			 ????????????????????????UTF-8 || big5
			try {
				dataLineList = fileCom.intputTxt(filePath1, "big5");
			} catch (IOException e) {
				throw new LogicException("E0014", "L4601(" + filePath1 + ") : " + e.getMessage());
			}

//			 ?????????????????????????????????????????????
			insuRenewFileVo.setValueFromFile(dataLineList);

			ArrayList<OccursList> uploadFile = insuRenewFileVo.getOccursList();

			if (uploadFile != null && uploadFile.size() != 0) {

				deleInsuRenewMediaTemp(titaVo);

				for (OccursList t : uploadFile) {
					if (iInsuEndMonth != parse.stringToInteger(t.get("FireInsuMonth"))) {
						throw new LogicException("E0014", "?????????????????????????????????");
					}
					checkResultA = "";
					checkResultB = "";
					checkResultC = "";

//		1.?????????????????????????????????(????????????)
//			1.?????????=0) 
//			2.?????????(????????????????????????????????????
					InsuRenew tInsuRenew = insuRenewService.prevInsuNoFirst(
							parse.stringToInteger(t.get("CustNo").trim()),
							parse.stringToInteger(t.get("FacmNo").trim()), t.get("InsuNo").trim(), titaVo);
//				??????????????????
					if (tInsuRenew == null) {
//					????????????????????????
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
//					a.??????
					if ("".equals(checkResultA)) {

						resetAcReceivable(2, tInsuRenew, titaVo); // 2-????????????
						tInsuRenew.setFireInsuCovrg(parse.stringToBigDecimal(t.get("NewFireInsuAmt").trim()));
						tInsuRenew.setEthqInsuCovrg(parse.stringToBigDecimal(t.get("NewEqInsuAmt").trim()));
						tInsuRenew.setFireInsuPrem(parse.stringToBigDecimal(t.get("NewFireInsuFee").trim()));
						tInsuRenew.setEthqInsuPrem(parse.stringToBigDecimal(t.get("NewEqInsuFee").trim()));
						tInsuRenew
								.setInsuStartDate(parse.stringToInteger(t.get("NewInsuStartDate").replaceAll("/", "")));
						tInsuRenew.setInsuEndDate(parse.stringToInteger(t.get("NewInsuEndDate").replaceAll("/", "")));
						tInsuRenew.setTotInsuPrem(parse.stringToBigDecimal(t.get("NewTotalFee").trim()));
						try {
							insuRenewService.update(tInsuRenew);
						} catch (DBException e) {
							throw new LogicException("E0007", "L4601 InsuRenew update " + e.getErrorMsg());
						}
						resetAcReceivable(0, tInsuRenew, titaVo); // 0-??????
						succesCnt = succesCnt + 1;
						checkReportB(tInsuRenew, titaVo);
						checkReportC(tInsuRenew, titaVo);
					} else {
						errorACnt = errorACnt + 1;
					}

					InsuRenewMediaTemp tInsuRenewMediaTemp = new InsuRenewMediaTemp();

					tInsuRenewMediaTemp = this.getInsuRenewMediaTemp(t, tInsuRenewMediaTemp, titaVo);

					this.info("tInsuRenewMediaTemp = " + tInsuRenewMediaTemp);
					// ????????????
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

			checkMsg = "???????????????????????????????????????????????????" + succesCnt + "???????????????????????????" + errorACnt + "??????";

			checkMsg += "???????????????????????????" + errorBCnt + "??????";

			checkMsg += "?????????????????????" + errorCCnt + "??????";

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4601",
					"2" + titaVo.getParam("InsuEndMonth"), checkMsg, titaVo);

			return null;

		} else {
			// ?????????????????????
			l4601Report.exec(titaVo);

			String sendMsg = "L4601-???????????????";

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L4601", sendMsg, titaVo);

			this.addList(this.totaVo);
			return this.sendList();
		}

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

		// ?????????
		if (tInsuRenew.getAcDate() > 0) {
			return;
		}

		AcReceivable acReceivable = new AcReceivable();
		acReceivable.setReceivableFlag(3); // ?????????????????? -> 2-???????????? 3-???????????? 4-???????????? 5-????????????
		acReceivable.setAcctCode("TMI"); // ????????????
		acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // ????????????
		acReceivable.setCustNo(tInsuRenew.getCustNo());// ??????+??????
		acReceivable.setFacmNo(tInsuRenew.getFacmNo());
		acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // ????????????
		acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
		TempVo tTempVo = new TempVo();
		tTempVo.clear();
		tTempVo.putParam("InsuYearMonth", tInsuRenew.getInsuYearMonth());
		acReceivable.setJsonFields(tTempVo.getJsonString());
		acReceivableList.add(acReceivable);
		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(flag, acReceivableList, titaVo); // 0-?????? 1-?????? 2-????????????
	}

	public InsuRenewMediaTemp getInsuRenewMediaTemp(OccursList occursList, InsuRenewMediaTemp t, TitaVo titaVo)
			throws LogicException {

//	FireInsuMonth		??????????????????	X	6	
		t.setFireInsuMonth(occursList.get("FireInsuMonth").trim());
//	ReturnCode	?????????
		t.setReturnCode(occursList.get("ReturnCode").trim());
//	InsuCampCode	??????????????????
		t.setInsuCampCode(occursList.get("InsuCampCode").trim());
//	InsuCustId	?????????????????????
		t.setInsuCustId(occursList.get("InsuCustId").trim());
//	InsuCustName	???????????????
		t.setInsuCustName(occursList.get("InsuCustName").trim());
//	LoanCustId	?????????????????????
		t.setLoanCustId(occursList.get("LoanCustId").trim());
//	LoanCustName	???????????????
		t.setLoanCustName(occursList.get("LoanCustName").trim());
//	PostalCode	????????????
		t.setPostalCode(occursList.get("PostalCode").trim());
//	Address	????????????
		t.setAddress(occursList.get("Address").trim());
//	BuildingSquare	???????????????
		t.setBuildingSquare(occursList.get("BuildingSquare").trim());
//	BuildingCode	??????????????????
		t.setBuildingCode(occursList.get("BuildingCode").trim());
//	BuildingYears	????????????
		t.setBuildingYears(occursList.get("BuildingYears").trim());
//	BuildingFloors	?????????
		t.setBuildingFloors(occursList.get("BuildingFloors").trim());
//	RoofCode	??????????????????
		t.setRoofCode(occursList.get("RoofCode").trim());
//	BusinessUnit	???????????????
		t.setBusinessUnit(occursList.get("BusinessUnit").trim());
//	ClCode1	????????????
		t.setClCode1(occursList.get("ClCode1").trim());
//	ClCode2	????????????
		t.setClCode2(occursList.get("ClCode2").trim());
//	ClNo	????????????
		t.setClNo(occursList.get("ClNo").trim());
//	Seq	??????
		t.setSeq(occursList.get("Seq").trim());
//  InsuNo	????????????	
		t.setInsuNo(occursList.get("InsuNo").trim());
//	InsuStartDate	????????????
		t.setInsuStartDate(occursList.get("InsuStartDate").trim());
//	InsuEndDate	????????????
		t.setInsuEndDate(occursList.get("InsuEndDate").trim());
//	FireInsuAmt	????????????
		t.setFireInsuAmt(occursList.get("FireInsuAmt").trim());
//	FireInsuFee	????????????
		t.setFireInsuFee(occursList.get("FireInsuFee").trim());
//	EqInsuAmt	???????????????
		t.setEqInsuAmt(occursList.get("EqInsuAmt").trim());
//	EqInsuFee	???????????????
		t.setEqInsuFee(occursList.get("EqInsuFee").trim());
//	CustNo	???????????????
		t.setCustNo(occursList.get("CustNo").trim());
//	FacmNo	????????????
		t.setFacmNo(occursList.get("FacmNo").trim());
//	SendDate	????????????
		t.setSendDate(occursList.get("SendDate").trim());
//	NewInusNo	????????????(???)
		t.setNewInusNo(occursList.get("NewInusNo").trim());
//	NewInsuStartDate	????????????(???)
		t.setNewInsuStartDate(occursList.get("NewInsuStartDate").trim());
//	NewInsuEndDate	????????????(???)
		t.setNewInsuEndDate(occursList.get("NewInsuEndDate").trim());
//	NewFireInsuAmt	????????????(???)
		t.setNewFireInsuAmt(occursList.get("NewFireInsuAmt").trim());
//	NewFireInsuFee	????????????(???)
		t.setNewFireInsuFee(occursList.get("NewFireInsuFee").trim());
//	NewEqInsuAmt	???????????????(???)
		t.setNewEqInsuAmt(occursList.get("NewEqInsuAmt").trim());
//	NewEqInsuFee	???????????????(???)
		t.setNewEqInsuFee(occursList.get("NewEqInsuFee").trim());
//	NewTotalFee	?????????(???)
		t.setNewTotalFee(occursList.get("NewTotalFee").trim());
//	Remark1	?????????
		t.setRemark1(occursList.get("Remark1").trim());
//	MailingAddress	????????????
		t.setMailingAddress(occursList.get("MailingAddress").trim());
//	Remark2	?????????
		t.setRemark2(occursList.get("Remark2").trim());
//	SklSalesName	???????????????????????????
		t.setSklSalesName(occursList.get("SklSalesName").trim());
//	SklUnitCode	????????????????????????
		t.setSklUnitCode(occursList.get("SklUnitCode").trim());
//	SklUnitName	????????????????????????
		t.setSklUnitName(occursList.get("SklUnitName").trim());
//	SklSalesCode	???????????????????????????
		t.setSklSalesCode(occursList.get("SklSalesCode").trim());
//	RenewTrlCode	????????????????????????
		t.setRenewTrlCode(occursList.get("RenewTrlCode").trim());
//	RenewUnit	??????????????????
		t.setRenewUnit(occursList.get("RenewUnit").trim());
		return t;
	}

	private void deleInsuRenewMediaTemp(TitaVo titaVo) throws LogicException {
		Slice<InsuRenewMediaTemp> slInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuMonthRg("" + iInsuEndMonth,
				"" + iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);

		if (slInsuRenewMediaTemp != null) {

			List<InsuRenewMediaTemp> lInsuRenewMediaTemp = new ArrayList<InsuRenewMediaTemp>();
			lInsuRenewMediaTemp = slInsuRenewMediaTemp.getContent();
			this.info("lInsuRenewMediaTemp = " + lInsuRenewMediaTemp);
			try {
				insuRenewMediaTempService.deleteAll(lInsuRenewMediaTemp, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "InsuRenew insert error : " + e.getErrorMsg());
			}
		}
	}

	private void checkReportA(OccursList t, InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		this.info("ReportA Start...");
//  	?????????=0
		if (parse.stringToBigDecimal(t.get("NewTotalFee")).compareTo(BigDecimal.ZERO) == 0) {
			if ("".equals(checkResultA)) {
				checkResultA = checkResultA + "11";
			} else {
				checkResultA = checkResultA + ",11";
			}
			return;
		}
//		?????????,?????????????????????????????? 
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

//		???????????????
		if ("Y".equals(tInsuRenew.getNotiTempFg())) {
			if ("".equals(checkResultA)) {
				checkResultA += "13";
			} else {
				checkResultA += ",13";
			}
			return;
		}

//		???????????????0.??????
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
//		Slice<InsuRenew> sl2InsuRenew = insuRenewService.findL4601B(calYear(iInsuEndMonth, -1), t.getClCode1(),
//				t.getClCode2(), t.getClNo(), 0, 1, titaVo);
//		InsuRenew t2InsuRenew = null;
//		if (sl2InsuRenew != null) {
//			t2InsuRenew = sl2InsuRenew.getContent().get(0);
//		}

//		??? ???????????????????????? ????????? ?????????????????????????????????
//		InsuOrignal t2InsuOrignal = insuOrignalService.clNoFirst(t.getClCode1(), t.getClCode2(), t.getClNo(), titaVo);

//		if (t2InsuRenew != null) {
//			if (t2InsuRenew.getInsuEndDate() != t.getInsuStartDate()) {
//				if ("".equals(checkResultB)) {
//					checkResultB += "21";
//				} else {
//					checkResultB += ",21";
//				}
//			}
//		} else if (t2InsuOrignal != null) {
//			if (t2InsuOrignal.getInsuEndDate() != t.getInsuStartDate()) {
//				if ("".equals(checkResultB)) {
//					checkResultB += "22";
//				} else {
//					checkResultB += ",22";
//				}
//			}
//		}

//		????????????
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
	 * ???????????????????????????
	 * 
	 * @param t      InsuRenew
	 * @param titaVo ..
	 * @throws LogicException ..
	 */
	private void checkReportC(InsuRenew t, TitaVo titaVo) throws LogicException {
		// ?????????????????????
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
		// ?????????
		if (isClose) {
			if ("".equals(checkResultC)) {
				checkResultC += "31";
			} else {
				checkResultC += ",31";
			}
		}
		// ?????????
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

//	private int calYear(int today, int year) throws LogicException {
//		int resultMonth = 0;
//	10801
//	201901
//	1080101
//	trans today = Bc format
//		if (today < 100000) {
//			today = parse.stringToInteger((today + 191100) + "01");
//		} else if (today < 1000000) {
//			today = parse.stringToInteger(today + "01");
//		} else if (today < 10000000) {
//			today = today + 19110000;
//		}
//
//		dateUtil.init();
//		dateUtil.setDate_1(today);
//		dateUtil.setYears(year);
//		today = dateUtil.getCalenderDay();
//
//		resultMonth = parse.stringToInteger((today + "").substring(0, 6));
//
//		return resultMonth;
//	}

}