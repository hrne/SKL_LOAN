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
import com.st1.itx.util.common.TxToDoCom;
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
	public FileCom fileCom;
	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public WebClient webClient;

	private int iEntryDate = 0;
	private String iRepayBank = "";
	private String sEntryDate = "";
	private HashMap<tmpFacm, String> custPhone = new HashMap<>();
	private HashMap<tmpFacm, String> custId = new HashMap<>();
	private HashMap<tmpFacm, String> repayBank = new HashMap<>();
	private HashMap<tmpFacm, Integer> insuMonth = new HashMap<>();
	private HashMap<tmpFacm, BigDecimal> insuFee = new HashMap<>();
	private HashMap<Integer, Integer> custLoanFlag = new HashMap<>();
	private HashMap<Integer, Integer> custFireFlag = new HashMap<>();

	private Boolean checkFlag = true;
	private String sendMsg = "";
	private String noticePhoneNo = "";
	private String noticeEmail = "";
	private String itemCodeText = "";
	private String itemCodeMail = "";

//	寄送筆數
	private int commitCnt = 200;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4453Batch ");
		this.totaVo.init(titaVo);
//		設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;
		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		iRepayBank = titaVo.getParam("RepayBank");

		txToDoCom.setTxBuffer(this.getTxBuffer());

		try {
			execute(titaVo);
		} catch (LogicException e) {
			checkFlag = false;
			sendMsg = e.getErrorMsg();
		}

		if (checkFlag) {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L6001", titaVo.getTlrNo(), "L4453 銀扣扣款前通知 處理完畢，送出筆數：" + (custLoanFlag.size() + custFireFlag.size()), titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4453", titaVo.getTlrNo(), sendMsg, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void execute(TitaVo titaVo) throws LogicException {
//		刪除TxToDoDetail
		deleteTxtoDo("TEXT00", titaVo);
		deleteTxtoDo("MAIL00", titaVo);

		sEntryDate = ("" + iEntryDate).substring(0, 4) + "/" + ("" + iEntryDate).substring(4, 6) + "/" + ("" + iEntryDate).substring(6);

//		條件 : 入帳日 or 銀行代號
		Slice<BankDeductDtl> slBankDeductDtl = null;
		switch (iRepayBank) {
		case "999":
			slBankDeductDtl = bankDeductDtlService.entryDateRng(iEntryDate, iEntryDate, this.index, this.limit, titaVo);
			break;
		case "998":
			slBankDeductDtl = bankDeductDtlService.repayBankNotEq("700", iEntryDate, iEntryDate, this.index, this.limit, titaVo);
			break;
		default:
			slBankDeductDtl = bankDeductDtlService.repayBankEq(iRepayBank, iEntryDate, iEntryDate, this.index, this.limit, titaVo);
			break;
		}

//		Step1. find Cust Phone Num. & CustId
		if (slBankDeductDtl != null) {
			int n = 0;

			for (BankDeductDtl tBankDeductDtl : slBankDeductDtl.getContent()) {

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
				tempVo = custNoticeCom.getCustNotice("L4453", tBankDeductDtl.getCustNo(), tBankDeductDtl.getFacmNo(), titaVo);

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
				tmpFacm tmp = new tmpFacm(tBankDeductDtl.getCustNo(), tBankDeductDtl.getFacmNo(), tBankDeductDtl.getRepayType());

				int insuMon = 0;
				if (tBankDeductDtl.getRepayType() == 5) {
					if (tBankDeductDtl.getPayIntDate() > 0) {
						insuMon = parse.stringToInteger(FormatUtil.pad9("" + tBankDeductDtl.getPayIntDate(), 7).substring(0, 5));
					}
					insuMonth.put(tmp, insuMon);
					insuFee.put(tmp, tBankDeductDtl.getRepayAmt());
				}
				custId.put(tmp, tCustMain.getCustId());
				custPhone.put(tmp, noticePhoneNo);
				repayBank.put(tmp, tBankDeductDtl.getRepayBank());
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
		String dataLines = "<" + noticePhoneNo + ">";
		if (tmp.getRepayType() == 1 || tmp.getRepayType() == 3) {
			this.info("RepayType() == 1 3...");
			if (!custLoanFlag.containsKey(tmp.getCustNo())) {
				dataLines += "\"H1\",\"" + custId.get(tmp) + "\",\"" + custPhone.get(tmp) + "\",\"親愛的客戶，繳款通知；新光人壽關心您。”,\"" + sEntryDate + "\"";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款通知>" + repayBank.get(tmp));
				tTxToDoDetail.setItemCode(itemCodeText);
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				txToDoCom.setTxBuffer(this.getTxBuffer());
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custLoanFlag.put(tmp.getCustNo(), 1);
			} else {
				this.info("CustNo : " + tmp.getCustNo() + " is continue ...");
			}
		} else if (tmp.getRepayType() == 5) {
			this.info("RepayType() == 5...");
			if (!custFireFlag.containsKey(tmp.getCustNo())) {
//				轉全形
				String sInsuAmt = toFullWidth("" + insuFee.get(tmp));
				String sInsuMonth = FormatUtil.pad9(toFullWidth("" + insuMonth.get(tmp)), 5).substring(3, 5);

				dataLines += "\"H1\",\"" + custId.get(tmp) + "\",\"" + custPhone.get(tmp) + "\",\"您好：提醒您" + sInsuMonth + "月份，除期款外，另加收年度火險地震險費＄" + sInsuAmt + "，請留意帳戶餘額。新光人壽關心您。　　\",\""
						+ dateSlashFormat(this.getTxBuffer().getMgBizDate().getTbsDy()) + "\"";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險扣款通知>" + repayBank.get(tmp));
				tTxToDoDetail.setItemCode(itemCodeText);
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				txToDoCom.setTxBuffer(this.getTxBuffer());
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custFireFlag.put(tmp.getCustNo(), 1);
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
		String dataLines = "<" + noticeEmail + ">";
		if (tmp.getRepayType() == 1 || tmp.getRepayType() == 3) {
			this.info("RepayType() == 1 3...");
			if (!custLoanFlag.containsKey(tmp.getCustNo())) {
				dataLines += "親愛的客戶，繳款通知；新光人壽關心您。";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款通知>" + repayBank.get(tmp));
				tTxToDoDetail.setItemCode(itemCodeMail);
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custLoanFlag.put(tmp.getCustNo(), 1);
			} else {
				this.info("CustNo : " + tmp.getCustNo() + " is continue ...");
			}
		} else if (tmp.getRepayType() == 5) {
			this.info("RepayType() == 5...");
			if (!custFireFlag.containsKey(tmp.getCustNo())) {
//				轉全形
				String sInsuAmt = toFullWidth("" + insuFee.get(tmp));
				String sInsuMonth = FormatUtil.pad9(toFullWidth("" + insuMonth.get(tmp)), 5).substring(3, 5);

				dataLines += "您好：提醒您" + sInsuMonth + "月份，除期款外，另加收年度火險地震險費＄" + sInsuAmt + "，請留意帳戶餘額。新光人壽關心您。";
				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險扣款通知>" + repayBank.get(tmp));
				tTxToDoDetail.setItemCode("itemCodeMail");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(dataLines);

				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo);

//				目前為同一戶號僅寄一封簡訊，後續要改依狀況寄送需改key
				custFireFlag.put(tmp.getCustNo(), 1);
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

//	刪除TxToDoDetail
	private void deleteTxtoDo(String itemCode, TitaVo titaVo) {
//		刪除未處理且為今天的
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, this.getTxBuffer().getTxCom().getTbsdyf(), this.getTxBuffer().getTxCom().getTbsdyf(), this.index,
				this.limit, titaVo);
		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		if (slTxToDoDetail != null) {
			for (TxToDoDetail tTxToDoDetail : slTxToDoDetail.getContent()) {
				if (tTxToDoDetail.getDtlValue().length() >= 9) {
					if (tTxToDoDetail.getDtlValue().substring(0, 8).equals("<期款扣款通知>") || tTxToDoDetail.getDtlValue().substring(0, 8).equals("<火險扣款通知>")) {
						switch (iRepayBank) {
						case "999": // ALL
							lTxToDoDetail.add(tTxToDoDetail);
							break;
						case "998": // ACH
							if (!tTxToDoDetail.getDtlValue().substring(8, 3).equals("700")) {
								lTxToDoDetail.add(tTxToDoDetail);
							}
							break;
						default: // Bank
							if (!tTxToDoDetail.getDtlValue().substring(8, 3).equals(iRepayBank)) {
								lTxToDoDetail.add(tTxToDoDetail);
							}
							break;
						}
					}
				}
			}
		}
		if (lTxToDoDetail.size() > 0) {
			try {
				this.info("DeleteAll...");
				txToDoCom.addByDetailList(false, 1, lTxToDoDetail, titaVo);
			} catch (LogicException e) {
				this.info("DeleteAll Error : " + e.getErrorMsg());
			}
		}
	}
}