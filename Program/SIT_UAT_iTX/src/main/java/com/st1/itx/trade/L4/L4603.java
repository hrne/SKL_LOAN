package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4603")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4603 extends TradeBuffer {
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
	private int insuStartDate = 0;
	private int insuEndDate = 0;
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
		this.info("active L4603 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		totaVo.put("PdfSnoM", "");

		txToDoCom.setTxBuffer(this.getTxBuffer());
//		書信
//		String outputLatterFilePath = outFolder + "LNM52P.txt";

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		insuStartDate = parse.stringToInteger(iInsuEndMonth + "01");
		insuEndDate = parse.stringToInteger(iInsuEndMonth + "31");

		Slice<InsuRenew> sInsuRenew = null;

		List<InsuRenew> lInsuRenew = new ArrayList<InsuRenew>();
//		條件 : 畫面輸入火險年月整月份
		sInsuRenew = insuRenewService.selectC(iInsuEndMonth, this.index, this.limit, titaVo);

		lInsuRenew = sInsuRenew == null ? null : sInsuRenew.getContent();

		if (lInsuRenew != null && lInsuRenew.size() != 0) {
//		-1.將以入通知檔者改為未入，並將其銷帳

			if (titaVo.isHcodeErase()) {
				resetAcReceivable(lInsuRenew, titaVo);
				for (InsuRenew tInsuRenew : lInsuRenew) {
					TempVo tempVo = new TempVo();
					tempVo = custNoticeCom.getCustNotice("L4603", tInsuRenew.getCustNo(), tInsuRenew.getFacmNo(), titaVo);

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
						setLetterFileVO(tInsuRenew);
					} else if (!"".equals(noticePhoneNo)) {
						this.info("09-Text...");
//						1.delete
						setTextFileVO(tInsuRenew, 1, titaVo);
					} else if (!"".equals(noticeEmail)) {
						this.info("09-EMail...");
//						1.delete
						setEMailFileVO(tInsuRenew, 1, titaVo);
					}
				}
			} else {

				List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();

				for (InsuRenew tInsuRenew : lInsuRenew) {

					if ("Y".equals(tInsuRenew.getNotiTempFg())) {
						throw new LogicException("E0005", "已入通知，請先訂正此交易。");
					}

					AcReceivable acReceivable = new AcReceivable();
					acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
//				0.寫入AcReceivable (狀態為0.正常，且為2.續保)
					if (tInsuRenew.getStatusCode() == 0 && tInsuRenew.getRenewCode() == 2) {
						acReceivable.setAcctCode("TMI"); // 業務科目
						acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
						acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
						acReceivable.setFacmNo(tInsuRenew.getFacmNo());
						acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
						acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
						acReceivableList.add(acReceivable);

//				} else if (tInsuRenew.getStatusCode() == 1) {
//					acReceivable.setAcctCode("F09"); // 業務科目
					} else if (tInsuRenew.getRenewCode() == 2) {
						if (tInsuRenew.getStatusCode() == 1) {
							throw new LogicException("E0015", "已轉借支");
						} else {
							throw new LogicException("E0015", "已轉催呆");
						}
					}

//		1.找出客戶通知方式
					int Dd = findNextPayDate(tInsuRenew.getCustNo(), tInsuRenew.getFacmNo());
					iEntryDate = parse.stringToInteger("" + iInsuEndMonth + Dd);

					sEntryDate = ("" + iEntryDate).substring(0, 4) + "/" + ("" + iEntryDate).substring(4, 6) + "/" + ("" + iEntryDate).substring(6);

					this.info("iEntryDate : " + iEntryDate);

					TempVo tempVo = new TempVo();
					tempVo = custNoticeCom.getCustNotice("L4603", tInsuRenew.getCustNo(), tInsuRenew.getFacmNo(), titaVo);

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
						setLetterFileVO(tInsuRenew);
					} else if (!"".equals(noticePhoneNo)) {
						this.info("09-Text...");
//						0 新增
						setTextFileVO(tInsuRenew, 0, titaVo);
					} else if (!"".equals(noticeEmail)) {
						this.info("09-EMail...");
//						0 新增
						setEMailFileVO(tInsuRenew, 0, titaVo);
					}

//		3.L4603之output & 更新table ->NotiTempFg 入通知檔	Y:已入 N:未入		
					CustMain t2CustMain = new CustMain();
					t2CustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

					OccursList occursList = new OccursList();
					occursList.putParam("OOCustNo", tInsuRenew.getCustNo());
					occursList.putParam("OOFacmNo", tInsuRenew.getFacmNo());
					occursList.putParam("OOClCode1", tInsuRenew.getClCode1());
					occursList.putParam("OOClCode2", tInsuRenew.getClCode2());
					occursList.putParam("OOClNo", tInsuRenew.getClNo());
					if (t2CustMain != null) {
						occursList.putParam("OOCustName", t2CustMain.getCustName());
					} else {
						occursList.putParam("OOCustName", "");
					}
					occursList.putParam("OOInsuNo", tInsuRenew.getNowInsuNo());
					occursList.putParam("OOLableA", noticeFlag);

					this.totaVo.addOccursList(occursList);

					tInsuRenew = insuRenewService.holdById(tInsuRenew);
					tInsuRenew.setNotiTempFg("Y");
					try {
						insuRenewService.update(tInsuRenew);
					} catch (DBException e) {
						throw new LogicException("E0007", "InsuRenew update error");
					}
				}

				acReceivableCom.setTxBuffer(this.getTxBuffer());
				acReceivableCom.mnt(0, acReceivableList, titaVo); // 0-起帳 1-銷帳-刪除

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
//			try {
//				// 用共用工具寫入檔案
//				fileCom.outputTxt(dataListLatter, outputLatterFilePath);
//			} catch (IOException e) {
//				e.printStackTrace();
//				throw new LogicException("E0014", "Latter output error");
//			}
		} else

		{
			throw new LogicException("E0001", "查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setLetterFileVO(InsuRenew tInsuRenew) throws LogicException {
		String dataLines = "";

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());
		if (tCustMain != null) {
//			QC 495 中文欄位前+半形空格
			dataLines = " " + FormatUtil.padX(getZipCode(tCustMain), 9) + ", " + FormatUtil.padX(noticeAddress, 64) + "," + FormatUtil.padX("", 42) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + ", " + FormatUtil.padX(tCustMain.getCustName().trim(), 12) + "," + FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + ","
					+ FormatUtil.padX("-", 1) + "," + FormatUtil.pad9("" + tInsuRenew.getFacmNo(), 3) + ", " + FormatUtil.padX(getRepayCode(tInsuRenew), 10) + ", "
					+ FormatUtil.padX(tCustMain.getCustName().trim(), 42) + ", " + FormatUtil.padX(getBdLocation(tInsuRenew), 58) + "," + FormatUtil.padX(tInsuRenew.getNowInsuNo(), 16) + ","
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
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

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
		tTxToDoDetail.setDtlValue("<Text>-FireFee");
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
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo());

		ArrayList<String> dataList = new ArrayList<String>();
		String dataLines = "";

		dataLines = "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticeEmail + "\",\"親愛的客戶，繳款通知；新光人壽關心您。”,\"" + sEntryDate + "\"";
		dataList.add(dataLines);

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(tInsuRenew.getCustNo());
		tTxToDoDetail.setFacmNo(tInsuRenew.getFacmNo());
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("<EMail>");
		tTxToDoDetail.setItemCode("MAIL00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(dataLines);

		txToDoCom.addDetail(false, flag, tTxToDoDetail, titaVo);
	}

//	火險應繳日跟著期款->額度內>0、最小之應繳日
	private int findNextPayDate(int custNo, int facmNo) throws LogicException {
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
		tmpFacm tmp = new tmpFacm(custNo, facmNo);
		HashMap<tmpFacm, Integer> custDd = new HashMap<>();

		Slice<LoanBorMain> sLoanBorMain = null;

		sLoanBorMain = loanBorMainService.bormCustNoEq(custNo, facmNo, facmNo, 0, 999, this.index, this.limit);

		lLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();

		if (lLoanBorMain != null && lLoanBorMain.size() != 0) {
			for (LoanBorMain tLoanBorMain : lLoanBorMain) {
				if (tLoanBorMain.getSpecificDd() >= 1) {
					if (!custDd.containsKey(tmp)) {
						custDd.put(tmp, tLoanBorMain.getSpecificDd());
					} else {
						if (tLoanBorMain.getSpecificDd() < custDd.get(tmp)) {
							custDd.put(tmp, tLoanBorMain.getSpecificDd());
						}
					}
				}
			}
		}

		if (custDd.get(tmp) == null) {
			custDd.put(tmp, 0);
		}

		return custDd.get(tmp);
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

		public int getCustNo() {
			return custNo;
		}

		public void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		public int getFacmNo() {
			return facmNo;
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

	private String getRepayCode(InsuRenew tInsuRenew) {
		String sRepayCode = "";
		FacMain tFacMain = new FacMain();
		FacMainId tFacMainId = new FacMainId();
		tFacMainId.setCustNo(tInsuRenew.getCustNo());
		tFacMainId.setFacmNo(tInsuRenew.getFacmNo());
		tFacMain = facMainService.findById(tFacMainId);

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

	private String getBdLocation(InsuRenew tInsuRenew) {
		String address = "";
		ClBuildingId tClBuildingId = new ClBuildingId();
		tClBuildingId.setClCode1(tInsuRenew.getInsuRenewId().getClCode1());
		tClBuildingId.setClCode2(tInsuRenew.getInsuRenewId().getClCode2());
		tClBuildingId.setClNo(tInsuRenew.getInsuRenewId().getClNo());
		ClBuilding tClBuilding = new ClBuilding();
		tClBuilding = clBuildingService.findById(tClBuildingId);

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

//	將以入通知檔者將其銷帳，並改為未入
	private void resetAcReceivable(List<InsuRenew> lInsuRenew, TitaVo titaVo) throws LogicException {
		List<AcReceivable> acReceivableList = new ArrayList<AcReceivable>();

		for (InsuRenew tInsuRenew : lInsuRenew) {
			if ("Y".equals(tInsuRenew.getNotiTempFg())) {

				AcReceivable acReceivable = new AcReceivable();

				if (tInsuRenew.getStatusCode() == 0) {
					acReceivable.setReceivableFlag(3); // 銷帳科目記號 -> 2-核心出帳 3-未收費用 4-短繳期金 5-另收欠款
				}

				if (tInsuRenew.getStatusCode() == 0) {
					acReceivable.setAcctCode("TMI"); // 業務科目
				} else if (tInsuRenew.getStatusCode() == 1) {
					acReceivable.setAcctCode("F09"); // 業務科目
				} else {
					throw new LogicException("E0015", "已轉催呆");
				}

				acReceivable.setRvAmt(tInsuRenew.getTotInsuPrem()); // 記帳金額
				acReceivable.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
				acReceivable.setFacmNo(tInsuRenew.getFacmNo());
				acReceivable.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
				acReceivable.setOpenAcDate(tInsuRenew.getInsuStartDate());
				acReceivableList.add(acReceivable);

				tInsuRenew = insuRenewService.holdById(tInsuRenew);
				tInsuRenew.setNotiTempFg("N");
				try {
					insuRenewService.update(tInsuRenew);
				} catch (DBException e) {
					throw new LogicException("E0007", "InsuRenew update error");
				}

			}
		}
		acReceivableCom.setTxBuffer(this.getTxBuffer());
		acReceivableCom.mnt(2, acReceivableList, titaVo); // 0-起帳 1-銷帳2-刪除

	}
}