package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
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
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdAreaService;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4603Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4603Batch extends TradeBuffer {
	@Autowired
	public InsuRenewService insuRenewService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public Parse parse;

	@Autowired
	public CustNoticeService custNoticeService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CdAreaService cdAreaService;

	@Autowired
	public CdCityService cdCityService;

	@Autowired
	public CustTelNoService custTelNoService;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public ClBuildingService clBuildingService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public AcReceivableCom acReceivableCom;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public CustNoticeCom custNoticeCom;

	@Autowired
	public MakeFile makeFile;

	private int iInsuEndMonth = 0;
	private int noticeFlag = 0;
	private String noticePhoneNo = "";
	private String noticeEmail = "";
	private String noticeAddress = "";
	private int iEntryDate = 0;
	private String sEntryDate = "";
	private ArrayList<String> dataListLatter = new ArrayList<String>();

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4603Batch ");
		this.totaVo.init(titaVo);

		totaVo.put("PdfSnoM", "");

		txToDoCom.setTxBuffer(this.getTxBuffer());
//		書信
//		String outputLatterFilePath = outFolder + "LNM52P.txt";

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;

		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();
//		條件 : 畫面輸入火險年月整月份
		Slice<InsuRenew> slInsuRenew = insuRenewService.selectC(iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);
		if (slInsuRenew != null) {
			for (InsuRenew t : slInsuRenew.getContent()) {
//			 續保件
				if (t.getRenewCode() == 2 && t.getStatusCode() == 0) {
					lInsuRenew.add(t);
				}
			}
		}

		if (lInsuRenew.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");// 查無資料

		}
		// 訂正交易
		if (titaVo.isHcodeErase()) {
			deleteTxToDo("TEXT00", titaVo);
			deleteTxToDo("Mail00", titaVo);
			updateErase(lInsuRenew, titaVo);
		}

		// 正常交易
		if (titaVo.isHcodeNormal()) {
			updateNormal(lInsuRenew, titaVo);
			for (InsuRenew t : slInsuRenew.getContent()) {

//		1.找出客戶通知方式
				int Dd = findNextPayDate(t.getCustNo(), t.getFacmNo(), titaVo);
				iEntryDate = parse.stringToInteger("" + iInsuEndMonth + Dd);

				sEntryDate = ("" + iEntryDate).substring(0, 4) + "/" + ("" + iEntryDate).substring(4, 6) + "/" + ("" + iEntryDate).substring(6);

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

//		2.依通知方式寫入L6001處理事項清單，之後在由批次程式去執行BatxNoticeCom寫File
//			通知方式為書信者直接寫File
//		           預設書信
				if (!"".equals(noticeAddress)) {
					this.info("09-Letter...");
					setLetterFileVO(t, titaVo);
				} else if (!"".equals(noticePhoneNo)) {
					this.info("09-Text...");
//						0 新增
					setTextFileVO(t, 0, titaVo);
				} else if (!"".equals(noticeEmail)) {
					this.info("09-EMail...");
//						0 新增
					setEMailFileVO(t, 0, titaVo);
				}

//		3.L4603之output & 更新table ->NotiTempFg 入通知檔	Y:已入 N:未入		
				CustMain t2CustMain = new CustMain();
				t2CustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);

				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo", t.getCustNo());
				occursList.putParam("OOFacmNo", t.getFacmNo());
				occursList.putParam("OOClCode1", t.getClCode1());
				occursList.putParam("OOClCode2", t.getClCode2());
				occursList.putParam("OOClNo", t.getClNo());
				if (t2CustMain != null) {
					occursList.putParam("OOCustName", t2CustMain.getCustName());
				} else {
					occursList.putParam("OOCustName", "");
				}
				occursList.putParam("OOInsuNo", t.getNowInsuNo());
				occursList.putParam("OOLableA", noticeFlag);

				this.totaVo.addOccursList(occursList);
			}

			if (dataListLatter.size() > 0) {
				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), titaVo.getTxCode() + "-火險通知作業", "LNM52P.txt", 2);

				for (String line : dataListLatter) {
					makeFile.put(line);
				}

				long sno = makeFile.close();

				this.info("sno : " + sno);
				makeFile.toFile(sno);

				totaVo.put("PdfSnoM", "" + sno);

			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 刪除處理清單
	private void deleteTxToDo(String itemCode, TitaVo titaVo) throws LogicException {
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.findTxNoEq(itemCode, titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(), titaVo.getOrgTlr(),
				parse.stringToInteger(titaVo.getOrgTno()), 0, Integer.MAX_VALUE, titaVo);
		if (slTxToDoDetail != null) {
			txToDoCom.delByDetailList(slTxToDoDetail.getContent(), titaVo);
		}
	}

	private void setLetterFileVO(InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		String dataLines = "";

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);
		if (tCustMain != null) {
//			QC 495 中文欄位前+半形空格
			dataLines = " " + FormatUtil.padX(getZipCode(tCustMain), 9) + ", " + FormatUtil.padX(noticeAddress, 64) + "," + FormatUtil.padX("", 42) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + ", " + FormatUtil.padX(tCustMain.getCustName().trim(), 12) + "," + FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + ","
					+ FormatUtil.padX("-", 1) + "," + FormatUtil.pad9("" + tInsuRenew.getFacmNo(), 3) + ", " + FormatUtil.padX(getRepayCode(tInsuRenew, titaVo), 10) + ", "
					+ FormatUtil.padX(tCustMain.getCustName().trim(), 42) + ", " + FormatUtil.padX(getBdLocation(tInsuRenew, titaVo), 58) + "," + FormatUtil.padX(tInsuRenew.getNowInsuNo(), 16) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getInsuStartDate(), 8) + "," + FormatUtil.pad9("" + tInsuRenew.getInsuStartDate(), 8) + "," + FormatUtil.padX("-", 1) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getInsuEndDate(), 8) + "," + FormatUtil.pad9("" + tInsuRenew.getFireInsuPrem(), 6) + "," + FormatUtil.pad9("" + tInsuRenew.getFireInsuCovrg(), 11)
					+ "," + FormatUtil.pad9("" + tInsuRenew.getEthqInsuPrem(), 6) + "," + FormatUtil.pad9("" + tInsuRenew.getEthqInsuCovrg(), 7) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getTotInsuPrem(), 6) + "," + FormatUtil.pad9("" + (iEntryDate - 19110000), 8) + "," + "9510200" + FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7)
					+ " ";
			dataListLatter.add(dataLines);
		}
	}

	private void setTextFileVO(InsuRenew tInsuRenew, int flag, TitaVo titaVo) throws LogicException {
		if (flag == 1) {
			this.info("Delete Text...");
		} else {
			this.info("set Text...");
		}
		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);

		ArrayList<String> dataList = new ArrayList<String>();
		String dataLines = "";

		String insuAmt = toFullWidth("" + tInsuRenew.getTotInsuPrem());
		String insuMonth = toFullWidth(("" + tInsuRenew.getInsuYearMonth()).substring(4, 6));

		this.info("Text... insuAmt = " + insuAmt);
		this.info("Text... insuMonth = " + insuMonth);

		this.info("CustNotice is not null...");
		dataLines = "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticePhoneNo + "\",\"您好：提醒您" + insuMonth + "月份，除期款外，另加收年度火險地震險費＄" + insuAmt + "，請留意帳戶餘額。新光人壽關心您。　　\",\""
				+ dateSlashFormat(this.getTxBuffer().getMgBizDate().getTbsDy()) + "\"";
		dataList.add(dataLines);

		this.info("Text... dataList = " + dataList);

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(tInsuRenew.getCustNo());
		tTxToDoDetail.setFacmNo(tInsuRenew.getFacmNo());
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("<FireFee>" + tInsuRenew.getPrevInsuNo());
		tTxToDoDetail.setItemCode("TEXT00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(dataLines);

		txToDoCom.addDetail(false, flag, tTxToDoDetail, titaVo);
	}

	private void setEMailFileVO(InsuRenew tInsuRenew, int flag, TitaVo titaVo) throws LogicException {
		if (flag == 1) {
			this.info("Delete EMail...");
		} else {
			this.info("set EMail...");
		}
		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);

		ArrayList<String> dataList = new ArrayList<String>();
		String dataLines = "";

		dataLines = "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticeEmail + "\",\"親愛的客戶，繳款通知；新光人壽關心您。”,\"" + sEntryDate + "\"";
		dataList.add(dataLines);

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(tInsuRenew.getCustNo());
		tTxToDoDetail.setFacmNo(tInsuRenew.getFacmNo());
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("<FireFee>" + tInsuRenew.getPrevInsuNo());
		tTxToDoDetail.setItemCode("MAIL00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(dataLines);

		txToDoCom.addDetail(false, flag, tTxToDoDetail, titaVo);
	}

//	火險應繳日跟著期款->額度內>0、最小之應繳日
	private int findNextPayDate(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		int specificDd = 0;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(custNo, facmNo, facmNo, 0, 900, this.index, this.limit, titaVo);
		if (slLoanBorMain != null) {
			for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
				if (tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 2 || tLoanBorMain.getStatus() == 7) {
					specificDd = tLoanBorMain.getSpecificDd();

				}
			}
		}
		return specificDd;
	}

	private String toFullWidth(String Pwd) {
		String outStr = "";
		char[] chars = Pwd.toCharArray();
		int tranTemp = 0;

		for (int i = 0; i < chars.length; i++) {
			tranTemp = (int) chars[i];
			if (tranTemp != 45) // ASCII碼:45 是減號 -
				tranTemp += 65248; // 此數字是 Unicode編碼轉為十進位 和 ASCII碼的 差
			outStr += (char) tranTemp;
		}
		return outStr;
	}

//	暫時紀錄戶號額度
	private class tmpFacm {

		private int custNo = 0;
		private int facmNo = 0;

		public tmpFacm(int custNo, int facmNo) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}
	}

	private String getZipCode(CustMain tCustMain) {
		String zipCode = "";

		if (tCustMain != null) {
			if (tCustMain.getRegZip3() != null && tCustMain.getRegZip3().length() >= 3) {
				zipCode = tCustMain.getRegZip3().substring(0, 1) + " " + tCustMain.getRegZip3().substring(1, 2) + " " + tCustMain.getRegZip3().substring(2, 3);
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
				sRepayCode = "匯款轉帳";
				break;
			case 2:
				sRepayCode = "銀行扣款";
				break;
			case 3:
				sRepayCode = "員工扣薪";
				break;
			case 4:
				sRepayCode = "支票";
				break;
			case 5:
				sRepayCode = "特約金";
				break;
			case 6:
				sRepayCode = "人事特約金";
				break;
			case 7:
				sRepayCode = "定存特約";
				break;
			case 8:
				sRepayCode = "劃撥存款";
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

	private String dateSlashFormat(int today) {
		String slashedDate = "";
		String acToday = "";
		if (today >= 1 && today < 19110000) {
			acToday = FormatUtil.pad9("" + (today + 19110000), 8);
		} else if (today >= 19110000) {
			acToday = FormatUtil.pad9("" + today, 8);
		}
		slashedDate = acToday.substring(0, 4) + "/" + acToday.substring(4, 6) + "/" + acToday.substring(6, 8);

		return slashedDate;
	}

	// 入銷帳檔
	private void updateNormal(List<InsuRenew> lInsuRenew, TitaVo titaVo) throws LogicException {
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();

		for (InsuRenew tInsuRenew : lInsuRenew) {
			if ("Y".equals(tInsuRenew.getNotiTempFg())) {
				throw new LogicException("E0005", "已入通知，請先訂正此交易。");
			}
			AcReceivable acReceivable = new AcReceivable();
			acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
			if (tInsuRenew.getStatusCode() == 0) {
				acReceivable.setAcctCode("TMI"); // 業務科目
				acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
				acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
				acReceivable.setFacmNo(tInsuRenew.getFacmNo());
				acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
				acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
				acReceivableList.add(acReceivable);
				tInsuRenew = insuRenewService.holdById(tInsuRenew, titaVo);
				tInsuRenew.setNotiTempFg("Y");
				try {
					insuRenewService.update(tInsuRenew, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "InsuRenew update error");
				}
			}
		}

		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳-刪除
	}

//	將以入通知檔者將其銷帳，並改為未入
	private void updateErase(List<InsuRenew> lInsuRenew, TitaVo titaVo) throws LogicException {
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();
		for (InsuRenew tInsuRenew : lInsuRenew) {
			if ("Y".equals(tInsuRenew.getNotiTempFg())) {
				AcReceivable acReceivable = new AcReceivable();
				if (tInsuRenew.getStatusCode() == 0) {
					acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				}
				acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
				acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
				acReceivable.setFacmNo(tInsuRenew.getFacmNo());
				acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
				acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
				acReceivableList.add(acReceivable);
				tInsuRenew = insuRenewService.holdById(tInsuRenew, titaVo);
				tInsuRenew.setNotiTempFg(""); // 待通知
				try {
					insuRenewService.update(tInsuRenew, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "InsuRenew update error");
				}
			}
		}
		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(2, acReceivableList, titaVo); // 0-起帳 1-銷帳2-刪除

	}
}