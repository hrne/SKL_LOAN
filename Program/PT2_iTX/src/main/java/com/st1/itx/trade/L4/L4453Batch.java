package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.CustNoticeService;
import com.st1.itx.db.service.CustTelNoService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.MailVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4453Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4453Batch extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public BankDeductDtlService bankDeductDtlService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public CustNoticeCom custNoticeCom;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CustTelNoService custTelNoService;

	@Autowired
	public CustNoticeService custNoticeService;

	@Autowired
	public TxToDoDetailService txToDoDetailService;
	@Autowired
	MakeFile makeFileText;
	@Autowired
	MakeFile makeFileMail;

	@Autowired
	public FileCom fileCom;
	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public WebClient webClient;

	private int iEntryDate = 0;
	private int iRepayBank = 0;
	private String sEntryDate = "";
	private HashMap<Integer, String> custPhoneMap = new HashMap<>();
	private HashMap<Integer, String> custIdMap = new HashMap<>();
	private HashMap<Integer, String> insuMonthMap = new HashMap<>();
	private HashMap<Integer, BigDecimal> insuFeeMap = new HashMap<>();
	private HashMap<Integer, Integer> prevIntDateMap = new HashMap<>();
	private HashMap<Integer, Integer> custLoanFlagMap = new HashMap<>();
	private HashMap<Integer, Integer> custFireFlagMap = new HashMap<>();
	private int totalCnt = 0;
	private int deleteCnt = 0;
	private int wkCalDy = 0;
	private Boolean checkFlag = true;
	private String sendMsg = "";
	String noticePhoneNo = "";
	String noticeEmail = "";
//	寄送筆數
	private int commitCnt = 200;
	// 年月
	// 應繳日
	private int prevIntDate = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4453Batch ");
		this.totaVo.init(titaVo);
//		設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;
		wkCalDy = dateUtil.getNowIntegerForBC();
		txToDoCom.setTxBuffer(this.getTxBuffer());
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L4453").setRptItem("期款扣款通知").build();
		// 開啟報表
		makeFileText.open(titaVo, reportVo, "簡訊檔.txt");
		ReportVo mailReportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI() + 19110000).setBrno(titaVo.getBrno())
				.setRptCode("L4453").setRptItem("期款扣款通知").build();
		// 開啟報表
		makeFileMail.open(titaVo, mailReportVo, "email檔.txt");
		if (titaVo.isHcodeErase()) {
//			刪除TxToDoDetail
			dele("TEXT00", titaVo);
			dele("MAIL00", titaVo);
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6001", titaVo.getTlrNo(),
					"L4453 銀扣扣款前通知 訂正完畢，總筆數：" + totalCnt + ", 刪除筆數：" + deleteCnt, titaVo);
		} else {

			try {
				execute(titaVo);
			} catch (LogicException e) {
				checkFlag = false;
				sendMsg = e.getErrorMsg();
			}

			if (checkFlag) {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6001",
						titaVo.getTlrNo(),
						"L4453 銀扣扣款前通知 處理完畢，送出筆數：" + (custLoanFlagMap.size() + custFireFlagMap.size()), titaVo);
			} else {
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4453",
						titaVo.getTlrNo(), sendMsg, titaVo);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void execute(TitaVo titaVo) throws LogicException {

		Slice<BankDeductDtl> sBankDeductDtl = null;

		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		iRepayBank = parse.stringToInteger(titaVo.getParam("RepayBank"));

		sEntryDate = ("" + iEntryDate).substring(0, 4) + "/" + ("" + iEntryDate).substring(4, 6) + "/"
				+ ("" + iEntryDate).substring(6);

//		條件 : 入帳日 or 銀行代號
		List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();
		if (iRepayBank == 999 || iRepayBank == 998) {
			sBankDeductDtl = bankDeductDtlService.entryDateRng(iEntryDate, iEntryDate, this.index, this.limit);
		} else {
			sBankDeductDtl = bankDeductDtlService.repayBankEq(FormatUtil.pad9("" + iRepayBank, 3), iEntryDate,
					iEntryDate, this.index, this.limit);
		}

		lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

//		Step1. find Cust Phone Num. & CustId
		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			int n = 0;
			// 抓期款最大應繳日
			for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
				if (tBankDeductDtl.getIntStartDate() > 0 && tBankDeductDtl.getIntEndDate() > 0
						&& tBankDeductDtl.getPrevIntDate() > prevIntDate) {
					prevIntDate = tBankDeductDtl.getPrevIntDate();
				}
			}

			for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
				if (iRepayBank == 998 && "700".equals(tBankDeductDtl.getRepayBank())) {
					continue;
				}

				if (n % commitCnt == 0) {
					this.batchTransaction.commit();
				}
				n++;

				if (!"Y".equals(tBankDeductDtl.getMediaCode())) {
					this.info("tBankDeductDtl ... " + tBankDeductDtl.toString());
					this.info("MediaCode != Y, continue...");
					continue;
				}

				TempVo tempVo = new TempVo();
				tempVo = custNoticeCom.getCustNotice("L4453", tBankDeductDtl.getCustNo(), tBankDeductDtl.getFacmNo(),
						titaVo);

				CustMain tCustMain = new CustMain();
				tCustMain = custMainService.custNoFirst(tBankDeductDtl.getCustNo(), tBankDeductDtl.getCustNo());

//				int sendCode = parse.stringToInteger(tempVo.getParam("NoticeFlag"));
				noticePhoneNo = tempVo.getParam("MessagePhoneNo");
				noticeEmail = tempVo.getParam("EmailAddress");

				this.info("CustNo ... " + tBankDeductDtl.getCustNo());
				this.info("FacmNo ... " + tBankDeductDtl.getFacmNo());
				this.info("phoneNo ... " + noticePhoneNo);
				this.info("emailAd ... " + noticeEmail);
//				寄送簡訊/電郵，若第一順位為書信，則找第二順位
//				若為皆1則傳簡訊
//				若為皆0則傳簡訊
				tmpFacm tmp = new tmpFacm(tBankDeductDtl.getCustNo(), tBankDeductDtl.getFacmNo(),
						tBankDeductDtl.getRepayType());
				if (tBankDeductDtl.getRepayType() <= 3) {
					prevIntDateMap.put(tBankDeductDtl.getCustNo(), tBankDeductDtl.getPrevIntDate());
				}
				if (tBankDeductDtl.getRepayType() == 5) {
					if (prevIntDate > 0) {
						insuMonthMap.put(tBankDeductDtl.getCustNo(),
								parse.IntegerToString(prevIntDate, 5).substring(0, 5));
					} else {
						TempVo dTempVo = new TempVo();
						dTempVo = dTempVo.getVo(tBankDeductDtl.getJsonFields());
						if (dTempVo.get("InsuDate") != null) {
							insuMonthMap.put(tBankDeductDtl.getCustNo(), dTempVo.get("InsuDate").substring(0, 5));
						} else {
							insuMonthMap.put(tBankDeductDtl.getCustNo(), "00000");
						}
					}
					insuFeeMap.put(tBankDeductDtl.getCustNo(), tBankDeductDtl.getRepayAmt());
				}
				custIdMap.put(tBankDeductDtl.getCustNo(), tCustMain.getCustId());
				custPhoneMap.put(tBankDeductDtl.getCustNo(), noticePhoneNo);

				if ("Y".equals(tempVo.getParam("isMessage"))) {
					setText(tmp, titaVo);
				}
				if ("Y".equals(tempVo.getParam("isEmail"))) {
					setMail(tmp, titaVo);
				}
			}
		} else {
			throw new LogicException("E0001", "查無資料");
		}
	}

	private void setText(tmpFacm tmp, TitaVo titaVo) throws LogicException {
//		RepayType = 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他
		this.info("setText...");
		String dataLines = "";
		if (tmp.getRepayType() == 1 || tmp.getRepayType() == 3) {
			this.info("RepayType() == 1 3...");
			if (!custLoanFlagMap.containsKey(tmp.getCustNo())) {

				int dd = prevIntDateMap.get(tmp.getCustNo()) % 100;
				dataLines += "\"H1\",\"" + "\",\"" + custPhoneMap.get(tmp.getCustNo()) + "\",\"親愛的客戶，您好：提醒您房貸將於"
						+ parse.IntegerToString(dd, 2) + "日扣款，敬請留意帳戶餘額以利扣款。新光人壽關心您。”,\"" + wkCalDy + "\"";

				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款通知>");
				tTxToDoDetail.setItemCode("TEXT00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				makeFileText.put(parse.IntegerToString(tmp.getCustNo(), 7) + "-"
						+ parse.IntegerToString(tmp.getFacmNo(), 3) + dataLines);
				txToDoCom.setTxBuffer(this.getTxBuffer());
				txToDoCom.addDetail(true, 9, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custLoanFlagMap.put(tmp.getCustNo(), 1);
			} else {
				this.info("CustNo : " + tmp.getCustNo() + " is continue ...");
			}
		} else if (tmp.getRepayType() == 5) {
			this.info("RepayType() == 5...");
			if (!custFireFlagMap.containsKey(tmp.getCustNo())) {
//				轉全形
				String sInsuAmt = toFullWidth("" + insuFeeMap.get(tmp.getCustNo()));
				String sInsuMonth = FormatUtil.pad9(toFullWidth("" + insuMonthMap.get(tmp.getCustNo())), 5).substring(3,
						5);

				dataLines += "\"H1\",\"" + "\",\"" + custPhoneMap.get(tmp.getCustNo()) + "\",\"您好：提醒您" + sInsuMonth
						+ "月份，除期款外，另加收年度火險地震險費＄" + sInsuAmt + "，請留意帳戶餘額。新光人壽關心您。　　\",\"" + wkCalDy + "\"";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險扣款通知>");
				tTxToDoDetail.setItemCode("TEXT00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				makeFileText.put(parse.IntegerToString(tmp.getCustNo(), 7) + "-"
						+ parse.IntegerToString(tmp.getFacmNo(), 3) + dataLines);
				txToDoCom.setTxBuffer(this.getTxBuffer());
				txToDoCom.addDetail(true, 9, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custFireFlagMap.put(tmp.getCustNo(), 1);
			} else {
				this.info("CustNo : " + tmp.getCustNo() + " is continue ...");
			}
		} else {
			this.info("RepayType() != 1 or 5...");
		}
	}

	private void setMail(tmpFacm tmp, TitaVo titaVo) throws LogicException {
//		RepayType = 1.期款 2.部分償還 3.結案 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他
		this.info("setMail...");

		if (tmp.getRepayType() == 1 || tmp.getRepayType() == 3) {
			this.info("RepayType() == 1 3...");
			if (!custLoanFlagMap.containsKey(tmp.getCustNo())) {
				MailVo mailVo = new MailVo();
				String processNote = mailVo.generateProcessNotes(noticeEmail, "期款扣款通知", "親愛的客戶，繳款通知；新光人壽關心您。", 0);

				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款通知>");
				tTxToDoDetail.setItemCode("MAIL00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(processNote);
				makeFileMail.put(parse.IntegerToString(tmp.getCustNo(), 7) + parse.IntegerToString(tmp.getFacmNo(), 3)
						+ processNote);
				txToDoCom.addDetail(true, 9, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custLoanFlagMap.put(tmp.getCustNo(), 1);
			} else {
				this.info("CustNo : " + tmp.getCustNo() + " is continue ...");
			}
		} else if (tmp.getRepayType() == 5) {
			this.info("RepayType() == 5...");
			if (!custFireFlagMap.containsKey(tmp.getCustNo())) {
//				轉全形
				String sInsuAmt = toFullWidth("" + insuFeeMap.get(tmp.getCustNo()));
				String sInsuMonth = FormatUtil.pad9(toFullWidth("" + insuMonthMap.get(tmp.getCustNo())), 5).substring(3,
						5);
				MailVo mailVo = new MailVo();
				String processNote = mailVo.generateProcessNotes(noticeEmail, "火險扣款通知",
						"您好：提醒您" + sInsuMonth + "月份，除期款外，另加收年度火險地震險費＄" + sInsuAmt + "，請留意帳戶餘額。新光人壽關心您。", 0);

				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險扣款通知>");
				tTxToDoDetail.setItemCode("MAIL00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(processNote);

				makeFileMail.put(parse.IntegerToString(tmp.getCustNo(), 7) + "-"
						+ parse.IntegerToString(tmp.getFacmNo(), 3) + processNote);
				txToDoCom.addDetail(true, 9, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custFireFlagMap.put(tmp.getCustNo(), 1);
			} else {
				this.info("CustNo : " + tmp.getCustNo() + " is continue ...");
			}
		}
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
		private int repayType = 0;

		public tmpFacm(int custNo, int facmNo, int repayType) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
			this.setRepayType(repayType);
		}

		private int getCustNo() {
			return custNo;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		private int getFacmNo() {
			return facmNo;
		}

		private void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		private int getRepayType() {
			return repayType;
		}

		private void setRepayType(int repayType) {
			this.repayType = repayType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
			result = prime * result + repayType;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			if (repayType != other.repayType)
				return false;
			return true;
		}

		private L4453Batch getEnclosingInstance() {
			return L4453Batch.this;
		}

		@Override
		public String toString() {
			return "tmpFacm [custNo=" + custNo + ", facmNo=" + facmNo + ", repayType=" + repayType + "]";
		}
	}

//	刪除TxToDoDetail 同L4454 須同步更改
	private void dele(String itemCode, TitaVo titaVo) throws LogicException {
//		刪除未處理
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.findTxNoEq(itemCode, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), parse.stringToInteger(titaVo.getOrgTno()), 0, Integer.MAX_VALUE,
				titaVo);
		if (slTxToDoDetail != null) {
			for (TxToDoDetail tTxToDoDetail : slTxToDoDetail.getContent()) {
				totalCnt++;
				if (tTxToDoDetail.getStatus() == 0) {
					deleteCnt++;
					try {
						this.info("DeleteAll...");
						txToDoCom.addDetail(true, 1, tTxToDoDetail, titaVo);
					} catch (LogicException e) {
						this.info("DeleteAll Error : " + e.getErrorMsg());
					}
				}
			}
		}
	}

}