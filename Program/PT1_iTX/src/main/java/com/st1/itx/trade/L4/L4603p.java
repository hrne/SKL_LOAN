package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * L4603p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L4603p")
@Scope("prototype")
public class L4603p extends TradeBuffer {

	@Autowired
	private InsuRenewService insuRenewService;

	@Autowired
	private CustNoticeCom custNoticeCom;

	@Autowired
	private LoanBorMainService loanBorMainService;

	@Autowired
	private ClBuildingService clBuildingService;

	@Autowired
	private CustMainService custMainService;

	@Autowired
	private L4603Report l4603report;

	@Autowired
	private L4603Report2 l4603report2;

	@Autowired
	private WebClient webClient;

	@Autowired
	private DateUtil dDateUtil;

	@Autowired
	private Parse parse;

	@Autowired
	private MakeFile makeFile;

	@Autowired
	private TxToDoCom txToDoCom;

	@Autowired
	private AcReceivableCom acReceivableCom;

	@Autowired
	private FacMainService facMainService;

	@Autowired
	private TxToDoDetailService txToDoDetailService;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	private int noticeFlag = 0;
	private int iEntryDate = 0;
	private int specificDd = 0;

	private String sEntryDate = "";
	private String noticePhoneNo = "";
	private String noticeEmail = "";
	private String noticeAddress = "";
	private String checkResultC = "";

	private ArrayList<String> dataListLatter = new ArrayList<String>();
	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4603p ");
		this.totaVo.init(titaVo);

		this.info("L4603p titaVo.getTxcd() = " + titaVo.getTxcd());

		String parentTranCode = titaVo.getTxcd();

		int iInsuEndMonth = 0;
		l4603report.setParentTranCode(parentTranCode);
		l4603report2.setParentTranCode(parentTranCode);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		Slice<InsuRenew> slInsuRenew = insuRenewService.selectC(iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);
		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();

		if (slInsuRenew != null) {
			for (InsuRenew t : slInsuRenew.getContent()) {
//		 ?????????
				if (t.getRenewCode() == 2 && t.getStatusCode() == 0) {
					lInsuRenew.add(t);
				} // if
			} // for
		} // if

//		if (lInsuRenew.size() == 0) {
//			throw new LogicException(titaVo, "E0001", "");// ????????????
//		}

		// ????????????
		if (titaVo.isHcodeErase()) {
			txToDoCom.setTxBuffer(this.getTxBuffer());
			deleteTxToDo("TEXT00", titaVo);
			deleteTxToDo("MAIL00", titaVo);
		}

		// ????????????
		if (titaVo.isHcodeNormal()) {

			this.info("slInsuRenew ---------->" + slInsuRenew.getContent().size());

			List<OccursList> reportlist = new ArrayList<>();
			List<OccursList> reportlist1 = new ArrayList<>();

			for (InsuRenew t : lInsuRenew) {
//				if ("Y".equals(t.getNotiTempFg())) {
//					throw new LogicException("E0005", "???????????????????????????????????????");
//				}

//	1.????????????????????????
				CustMain t2CustMain = new CustMain();
				String custName = "";
				t2CustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
				if (t2CustMain != null) {
					custName = t2CustMain.getCustName();
				}
				checkC(t.getCustNo(), t.getFacmNo(), titaVo);
				if (!"".equals(checkResultC)) {
					// ???????????? ??????????????? ?????? ?????? ?????? ??????????????? ??????????????? ???????????? ???????????? ??????????????? ??????????????? ????????? ????????????
					OccursList occursListReport = new OccursList();
					occursListReport.putParam("ReportCClCode1", t.getClCode1());
					occursListReport.putParam("ReportCClCode2", t.getClCode2());
					occursListReport.putParam("ReportCClNo", t.getClNo());
					occursListReport.putParam("ReportCPrevInsuNo", t.getPrevInsuNo());
					occursListReport.putParam("ReportCCustNo", t.getCustNo());
					occursListReport.putParam("ReportCFacmNo", t.getFacmNo());
					occursListReport.putParam("ReportCCustName", custName);
					occursListReport.putParam("ReportCNewInsuStartDate", t.getInsuStartDate());
					occursListReport.putParam("ReportCNewInsuEndDate", t.getInsuEndDate());
					occursListReport.putParam("ReportCFireAmt", t.getFireInsuCovrg());
					occursListReport.putParam("ReportCFireFee", t.getFireInsuPrem());
					occursListReport.putParam("ReportCEthqAmt", t.getEthqInsuCovrg());
					occursListReport.putParam("ReportCEthqFee", t.getEthqInsuPrem());
					occursListReport.putParam("ReportCTotlFee", t.getTotInsuPrem());
					if ("31".equals(checkResultC)) {
						occursListReport.putParam("ReportCErrMsg", "??????????????????");
					}
					if ("32".equals(checkResultC)) {
						occursListReport.putParam("ReportCErrMsg", "??????????????????");
					}
					reportlist.add(occursListReport);
//				totaC.addOccursList(occursListReport);
				} else {
//
					OccursList occursList = new OccursList();
					occursList.putParam("OOCustNo", t.getCustNo());
					occursList.putParam("OOFacmNo", t.getFacmNo());
					occursList.putParam("OOClCode1", t.getClCode1());
					occursList.putParam("OOClCode2", t.getClCode2());
					occursList.putParam("OOClNo", t.getClNo());
					occursList.putParam("OOCustName", custName);
					occursList.putParam("OOInsuNo", t.getPrevInsuNo());
					occursList.putParam("OOLableA", noticeFlag);
					reportlist1.add(occursList);
//				this.totaVo.addOccursList(occursList);
				} // else
//			
			} // for

			if (lAcReceivable.size() > 0) {
				acReceivableCom.setTxBuffer(this.getTxBuffer());
				acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-?????? 1-??????-??????
			}

			if (reportlist.size() > 0) {
				l4603report.exec(titaVo, reportlist, 1);
			}

			if (reportlist1.size() > 0) {
				l4603report.exec1(titaVo, reportlist1, 2);
			}

//			String subject = "????????????????????????-??????????????? ";
			for (InsuRenew t : lInsuRenew) {

				checkC(t.getCustNo(), t.getFacmNo(), titaVo);
				dDateUtil.init();
				dDateUtil.setDate_1(iInsuEndMonth * 100 + 01);
				dDateUtil.setMons(0);
				dDateUtil.getCalenderDay();
				if (specificDd > dDateUtil.getDays()) {
					specificDd = dDateUtil.getDays();
				}

				String sInsuEndMonth = Integer.toString(iInsuEndMonth);
				String sspecificDd = FormatUtil.pad9(Integer.toString(specificDd), 2);

				iEntryDate = parse.stringToInteger(sInsuEndMonth + sspecificDd);

				sEntryDate = ("" + iEntryDate).substring(0, 4) + "/" + ("" + iEntryDate).substring(4, 6) + "/"
						+ ("" + iEntryDate).substring(6);

				this.info("iEntryDate : " + iEntryDate);

				TempVo tempVo = new TempVo();
				tempVo = custNoticeCom.getCustNotice("L4603", t.getCustNo(), t.getFacmNo(), titaVo);

				noticeFlag = parse.stringToInteger(tempVo.getParam("NoticeFlag"));
				noticePhoneNo = tempVo.getParam("MessagePhoneNo");
				noticeEmail = tempVo.getParam("EmailAddress");
				noticeAddress = tempVo.getParam("LetterAddress");

				this.info("noticeFlag : " + noticeFlag);
				this.info("noticePhoneNo : " + noticePhoneNo);
				this.info("noticeEmail : " + noticeEmail);
				this.info("noticeAddress : " + noticeAddress);

//				2.?????????????????????L6001??????????????????????????????????????????????????????BatxNoticeCom???File
//				?????????????????????????????????File
//			           ????????????
				if ("Y".equals(tempVo.getParam("isLetter"))) {
					setLetterFileVO(t, titaVo);
				}
				if ("Y".equals(tempVo.getParam("isMessage"))) {
					setTextFileVO(t, 0, titaVo);
				}
				if ("Y".equals(tempVo.getParam("isEmail"))) {

					// ????????????
					noticeEmail = tempVo.getParam("EmailAddress");

					long sno = l4603report2.exec(titaVo, t, this.getTxBuffer());

					// 2022-01-14 ????????????
					setEMailFileVO(t, 0, titaVo, sno);

//					String bodyText = "??????????????????????????????" + "\n" + "????????????????????????";
//					mailService.setParams(tempVo.getParam("EmailAddress"), subject, bodyText);

//					mailService.setParams("skcu31780001@skl.com.tw", subject, bodyText);
//					mailService.setParams("", outFolder + "????????????????????????-???????????????.pdf");
//					mailService.exec();

				}

			}

			if (dataListLatter.size() > 0)

			{
				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), "??????????????????_LNM52P",
						"LNM52P.txt", 2);

				for (String line : dataListLatter) {
					makeFile.put(line);
				}

				long sno = makeFile.close();

				this.info("sno : " + sno);
				makeFile.toFile(sno);

			}
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO")+"L4603", "L4603???????????????????????????", titaVo);

		} // if

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setLetterFileVO(InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		String dataLines = "";

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);
		if (tCustMain != null) {
			
			// ??????????????????????????????
			if (!custNoticeCom.checkIsLetterSendable(null, tCustMain.getCustNo(), tInsuRenew.getFacmNo(), "L4603", titaVo))
				return;
			
//			QC 495 ???????????????+????????????
			dataLines = " " + FormatUtil.padX(getZipCode(tCustMain), 9) + ", " + FormatUtil.padX(noticeAddress, 64)
					+ "," + FormatUtil.padX("", 42) + "," + FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + ", "
					+ FormatUtil.padX(tCustMain.getCustName().trim(), 12) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + "," + FormatUtil.padX("-", 1) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getFacmNo(), 3) + ", "
					+ FormatUtil.padX(getRepayCode(tInsuRenew, titaVo), 10) + ", "
					+ FormatUtil.padX(tCustMain.getCustName().trim(), 42) + ", "
					+ FormatUtil.padX(getBdLocation(tInsuRenew, titaVo), 58) + ","
					+ FormatUtil.padX(tInsuRenew.getOrigInsuNo(), 16) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getInsuStartDate(), 8) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getInsuStartDate(), 8) + "," + FormatUtil.padX("-", 1) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getInsuEndDate(), 8) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getFireInsuPrem(), 6) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getFireInsuCovrg(), 11) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getEthqInsuPrem(), 6) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getEthqInsuCovrg(), 7) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getTotInsuPrem(), 6) + ","
					+ FormatUtil.pad9("" + (iEntryDate - 19110000), 8) + "," + "9510200"
					+ FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + " ";
			dataListLatter.add(dataLines);
		}
	}

	private void setTextFileVO(InsuRenew tInsuRenew, int flag, TitaVo titaVo) throws LogicException {
		if (flag == 1) {
			this.info("Delete Text...");
		} else {
			this.info("set Text...");
		}
		custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);

		String insuAmt = toFullWidth("" + tInsuRenew.getTotInsuPrem());
		String insuMonth = toFullWidth(("" + tInsuRenew.getInsuYearMonth()).substring(4, 6));

		this.info("Text... insuAmt = " + insuAmt);
		this.info("Text... insuMonth = " + insuMonth);

		this.info("CustNotice is not null...");

		txToDoCom.setTxBuffer(this.getTxBuffer());

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(tInsuRenew.getCustNo());
		tTxToDoDetail.setFacmNo(tInsuRenew.getFacmNo());
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("<????????????>" + tInsuRenew.getPrevInsuNo());
		tTxToDoDetail.setItemCode("TEXT00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(txToDoCom.getProcessNoteForText(noticePhoneNo, "??????????????????" + insuMonth
				+ "????????????????????????????????????????????????????????????" + insuAmt + "?????????????????????????????????????????????????????????", this.getTxBuffer().getMgBizDate().getTbsDy()));

		txToDoCom.addDetail(true, flag, tTxToDoDetail, titaVo);
	}

	private void setEMailFileVO(InsuRenew tInsuRenew, int flag, TitaVo titaVo, long sno) throws LogicException {
		if (flag == 1) {
			this.info("Delete EMail...");
		} else {
			this.info("set EMail...");
		}
		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);

		ArrayList<String> dataList = new ArrayList<String>();
		String dataLines = "<" + noticeEmail + ">";

		// 2022-01-14 ????????????
		// L4711 ?????????????????????????????????
		// L4711>String[] processNotes = tTxToDoDetail.getProcessNote().split(",");	
		// L4711>String email = processNotes[2];
		// L4711>long pdfno = Long.parseLong(processNotes[3]);
		// ??????????????????????????????,???????????????L4711
		dataLines += "\"H1\",\"" + tCustMain.getCustId() + "\"," + noticeEmail + "," + sno
				+ ",\"????????????????????????????????????????????????????????????,\"" + sEntryDate + "\"";
		dataList.add(dataLines);

		txToDoCom.setTxBuffer(this.getTxBuffer());

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(tInsuRenew.getCustNo());
		tTxToDoDetail.setFacmNo(tInsuRenew.getFacmNo());
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("<????????????>" + tInsuRenew.getPrevInsuNo());
		tTxToDoDetail.setItemCode("MAIL00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(dataLines);

		txToDoCom.addDetail(true, flag, tTxToDoDetail, titaVo);

	}

	private String getZipCode(CustMain tCustMain) {
		String zipCode = "";

		if (tCustMain != null) {
			if (tCustMain.getRegZip3() != null && tCustMain.getRegZip3().length() >= 3) {
				zipCode = tCustMain.getRegZip3().substring(0, 1) + " " + tCustMain.getRegZip3().substring(1, 2) + " "
						+ tCustMain.getRegZip3().substring(2, 3);
			}
			if (tCustMain.getRegZip2() != null && tCustMain.getRegZip2().length() >= 2) {
				zipCode += tCustMain.getRegZip2().substring(0, 1) + " " + tCustMain.getRegZip2().substring(1, 2);
			}
		}
		return zipCode;
	}

	private String getRepayCode(InsuRenew tInsuRenew, TitaVo titaVo) {
		String sRepayCode = "";
		FacMain tFacMain = new FacMain();
		FacMainId tFacMainId = new FacMainId();
		tFacMainId.setCustNo(tInsuRenew.getCustNo());
		tFacMainId.setFacmNo(tInsuRenew.getFacmNo());
		tFacMain = facMainService.findById(tFacMainId, titaVo);

		if (tFacMain != null) {
			switch (tFacMain.getRepayCode()) {
			case 1:
				sRepayCode = "????????????";
				break;
			case 2:
				sRepayCode = "????????????";
				break;
			case 3:
				sRepayCode = "????????????";
				break;
			case 4:
				sRepayCode = "??????";
				break;
			case 5:
				sRepayCode = "?????????";
				break;
			case 6:
				sRepayCode = "???????????????";
				break;
			case 7:
				sRepayCode = "????????????";
				break;
			case 8:
				sRepayCode = "????????????";
				break;
			default:
				sRepayCode = "";
				break;
			}
		}

		return sRepayCode;
	}

	private String getBdLocation(InsuRenew tInsuRenew, TitaVo titaVo) {
		String address = "";
		ClBuildingId tClBuildingId = new ClBuildingId();
		tClBuildingId.setClCode1(tInsuRenew.getInsuRenewId().getClCode1());
		tClBuildingId.setClCode2(tInsuRenew.getInsuRenewId().getClCode2());
		tClBuildingId.setClNo(tInsuRenew.getInsuRenewId().getClNo());
		ClBuilding tClBuilding = new ClBuilding();
		tClBuilding = clBuildingService.findById(tClBuildingId, titaVo);

		if (tClBuilding != null) {
			address = tClBuilding.getBdLocation();
		}
		return address;
	}

	private String toFullWidth(String Pwd) {
		String outStr = "";
		char[] chars = Pwd.toCharArray();
		int tranTemp = 0;

		for (int i = 0; i < chars.length; i++) {
			tranTemp = (int) chars[i];
			if (tranTemp != 45) // ASCII???:45 ????????? -
				tranTemp += 65248; // ???????????? Unicode????????????????????? ??? ASCII?????? ???
			outStr += (char) tranTemp;
		}
		return outStr;
	}

	// ??????????????????
	private void deleteTxToDo(String itemCode, TitaVo titaVo) throws LogicException {
		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.detailStatusRange(itemCode, 0, 0, 0, Integer.MAX_VALUE,
				titaVo);
		if (slTxToDoDetail != null) {
			for (TxToDoDetail t : slTxToDoDetail.getContent()) {
				if (t.getDtlValue().length() >= 6 && t.getDtlValue().substring(0, 6).equals("<????????????>")) {
					lTxToDoDetail.add(t);
				}
			}
			if (lTxToDoDetail.size() > 0) {
				txToDoCom.delByDetailList(lTxToDoDetail, titaVo);
			}
		}
	}

//	???????????????????????????->?????????>0?????????????????????
	private void checkC(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		// ?????????????????????
		specificDd = 01;
		boolean isClose = true;
		boolean isUnLoan = true;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(custNo, facmNo, facmNo, 0, 900, this.index,
				this.limit, titaVo);
		if (slLoanBorMain != null) {
			isUnLoan = false;
			for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
				if (tLoanBorMain.getLoanBal().compareTo(BigDecimal.ZERO) > 0) {
					specificDd = tLoanBorMain.getSpecificDd();
					isClose = false;
				}
			}
		}

		checkResultC = "";
		// ?????????
		if (isClose) {
			checkResultC = "31";
		}
		// ?????????
		if (isUnLoan) {
			checkResultC = "32";
		}

//		// ?????????
//		if (isClose) {
//			if ("".equals(checkResultC)) {
//				checkResultC += "31";
//			} else {
//				checkResultC += ",31";
//			}
//		}
//		// ?????????
//		if (isUnLoan) {
//			if ("".equals(checkResultC)) {
//				checkResultC += "32";
//			} else {
//				checkResultC = "32";
//			}
//		}

//		if (!"".equals(checkResultC)) {
//			errorCCnt = errorCCnt + 1;
//		}
	}

}