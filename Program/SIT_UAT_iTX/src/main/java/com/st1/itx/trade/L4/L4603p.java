package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.mail.MailService;
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
	public InsuRenewService insuRenewService;
	
	@Autowired
	public CustNoticeCom custNoticeCom;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	public LoanBorMainService loanBorMainService;
	
	@Autowired
	public ClBuildingService clBuildingService;
	
	@Autowired
	public CustMainService custMainService;
	
	@Autowired
	L4603Report l4603report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public Parse parse;
	
	@Autowired
	public MakeFile makeFile;
	
	@Autowired
	public TxToDoCom txToDoCom;
	
	@Autowired
	public FacMainService facMainService;
	
	@Value("${iTXOutFolder}")
	private String outFolder = "";
	
	private int noticeFlag = 0;
	private int iEntryDate = 0;
	private int specificDd = 0;
	
	private String sEntryDate = "";
	private String noticePhoneNo = "";
	private String noticeEmail = "";
	private String noticeAddress = "";
	
	private ArrayList<String> dataListLatter = new ArrayList<String>();
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4603p ");
		this.totaVo.init(titaVo);

		this.info("L4603p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		int iInsuEndMonth = 0;
		l4603report.setParentTranCode(parentTranCode);

		iInsuEndMonth = parse.stringToInteger(titaVo.getParam("InsuEndMonth")) + 191100;
		Slice<InsuRenew> slInsuRenew = insuRenewService.selectC(iInsuEndMonth, 0, Integer.MAX_VALUE, titaVo);
		
			String subject = "火險及地震險保費-繳款通知單 ";
			for (InsuRenew t : slInsuRenew.getContent()) {
				
				checkC(t.getCustNo(), t.getFacmNo(), titaVo);
				dDateUtil.init();
				dDateUtil.setDate_1(iInsuEndMonth * 100 + 01);
				dDateUtil.setMons(0);
				dDateUtil.getCalenderDay();
				if (specificDd > dDateUtil.getDays()) {
					specificDd = dDateUtil.getDays();
				}
				iEntryDate = parse.stringToInteger("" + iInsuEndMonth + specificDd);

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
				
//				2.依通知方式寫入L6001處理事項清單，之後在由批次程式去執行BatxNoticeCom寫File
//				通知方式為書信者直接寫File
//			           預設書信
						if ("Y".equals(tempVo.getParam("isLetter"))) {
							setLetterFileVO(t, titaVo);
						}
						if ("Y".equals(tempVo.getParam("isMessage"))) {
							setTextFileVO(t, 0, titaVo);
						}
						if ("Y".equals(tempVo.getParam("isEmail"))) {
							setEMailFileVO(t, 0, titaVo);
							
							l4603report.exec(titaVo, t, this.getTxBuffer());
							
							String noticeEmail = tempVo.getParam("EmailAddress");		
							
//							mailService.setParams(tempVo.getParam("EmailAddress"), subject, bodyText);
							String bodyText = "親愛的客戶，繳款通知"+"\n"+"新光人壽關心您。";
							
							mailService.setParams("skcu31780001@skl.com.tw", subject, bodyText);
							mailService.setParams("", outFolder + "火險及地震險保費-繳款通知單.pdf");
							mailService.exec();
							
						}
				
			}

			if (dataListLatter.size() > 0)

			{
				makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
						titaVo.getTxCode() + "-火險通知作業", "LNM52P.txt", 2);

				for (String line : dataListLatter) {
					makeFile.put(line);
				}

				long sno = makeFile.close();

				this.info("sno : " + sno);
				makeFile.toFile(sno);


			}
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L4603火險通知作業已完成", titaVo);
			

		this.addList(this.totaVo);
		return this.sendList();
	}
	
	private void setLetterFileVO(InsuRenew tInsuRenew, TitaVo titaVo) throws LogicException {
		String dataLines = "";

		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);
		if (tCustMain != null) {
//			QC 495 中文欄位前+半形空格
			dataLines = " " + FormatUtil.padX(getZipCode(tCustMain), 9) + ", " + FormatUtil.padX(noticeAddress, 64)
					+ "," + FormatUtil.padX("", 42) + "," + FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + ", "
					+ FormatUtil.padX(tCustMain.getCustName().trim(), 12) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getCustNo(), 7) + "," + FormatUtil.padX("-", 1) + ","
					+ FormatUtil.pad9("" + tInsuRenew.getFacmNo(), 3) + ", "
					+ FormatUtil.padX(getRepayCode(tInsuRenew, titaVo), 10) + ", "
					+ FormatUtil.padX(tCustMain.getCustName().trim(), 42) + ", "
					+ FormatUtil.padX(getBdLocation(tInsuRenew, titaVo), 58) + ","
					+ FormatUtil.padX(tInsuRenew.getNowInsuNo(), 16) + ","
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
		CustMain tCustMain = new CustMain();
		tCustMain = custMainService.custNoFirst(tInsuRenew.getCustNo(), tInsuRenew.getCustNo(), titaVo);

		ArrayList<String> dataList = new ArrayList<String>();

		String insuAmt = toFullWidth("" + tInsuRenew.getTotInsuPrem());
		String insuMonth = toFullWidth(("" + tInsuRenew.getInsuYearMonth()).substring(4, 6));

		this.info("Text... insuAmt = " + insuAmt);
		this.info("Text... insuMonth = " + insuMonth);

		this.info("CustNotice is not null...");
		String dataLines = "<" + noticePhoneNo + ">";
		dataLines += "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticePhoneNo + "\",\"您好：提醒您" + insuMonth
				+ "月份，除期款外，另加收年度火險地震險費＄" + insuAmt + "，請留意帳戶餘額。新光人壽關心您。　　\",\""
				+ dateSlashFormat(this.getTxBuffer().getMgBizDate().getTbsDy()) + "\"";
		dataList.add(dataLines);

		this.info("Text... dataList = " + dataList);

		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(tInsuRenew.getCustNo());
		tTxToDoDetail.setFacmNo(tInsuRenew.getFacmNo());
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("<火險保費>" + tInsuRenew.getPrevInsuNo());
		tTxToDoDetail.setItemCode("TEXT00");
		tTxToDoDetail.setStatus(0);
		tTxToDoDetail.setProcessNote(dataLines);

		txToDoCom.addDetail(true, flag, tTxToDoDetail, titaVo);
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
		String dataLines = "<" + noticeEmail + ">";

		dataLines += "\"H1\",\"" + tCustMain.getCustId() + "\",\"" + noticeEmail + "\",\"親愛的客戶，繳款通知；新光人壽關心您。”,\""
				+ sEntryDate + "\"";
		dataList.add(dataLines);
		
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setCustNo(tInsuRenew.getCustNo());
		tTxToDoDetail.setFacmNo(tInsuRenew.getFacmNo());
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("<火險保費>" + tInsuRenew.getPrevInsuNo());
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
	
	private void checkC(int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		// 未撥款或已結案
		specificDd = 01;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(custNo, facmNo, facmNo, 0, 900, this.index,
				this.limit, titaVo);
		if (slLoanBorMain != null) {
			for (LoanBorMain tLoanBorMain : slLoanBorMain.getContent()) {
				if (tLoanBorMain.getLoanBal().compareTo(BigDecimal.ZERO) > 0) {
					specificDd = tLoanBorMain.getSpecificDd();
				}
			}
		}
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
}