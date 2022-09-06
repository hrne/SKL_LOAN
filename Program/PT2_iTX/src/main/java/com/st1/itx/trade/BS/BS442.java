package com.st1.itx.trade.BS;

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

@Service("BS442")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class BS442 extends TradeBuffer {

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
	private int iRepayBank = 0;
	private HashMap<tmpFacm, String> custPhone = new HashMap<>();
	private HashMap<tmpFacm, String> custId = new HashMap<>();
	private HashMap<tmpFacm, Integer> insuMonth = new HashMap<>();
	private HashMap<tmpFacm, BigDecimal> insuFee = new HashMap<>();
	private HashMap<Integer, Integer> custLoanFlag = new HashMap<>();
	private HashMap<Integer, Integer> custFireFlag = new HashMap<>();

	private Boolean checkFlag = true;
	private String sendMsg = "";
	String noticePhoneNo = "";
	String noticeEmail = "";

//	寄送筆數
	private int commitCnt = 200;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS442 ");
		this.totaVo.init(titaVo);
//		設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

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
		dele("TEXT00", "<期款扣款通知>", titaVo);
		dele("TEXT00", "<火險扣款通知>", titaVo);
		dele("MAIL00", "<期款扣款通知>", titaVo);
		dele("MAIL00", "<火險扣款通知>", titaVo);

		Slice<BankDeductDtl> sBankDeductDtl = null;

		iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		iRepayBank = parse.stringToInteger(titaVo.getParam("RepayBank"));

//		條件 : 入帳日 or 銀行代號
		List<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();
		if (iRepayBank == 999) {
			sBankDeductDtl = bankDeductDtlService.entryDateRng(iEntryDate, iEntryDate, this.index, this.limit);
		} else {
			sBankDeductDtl = bankDeductDtlService.repayBankEq(FormatUtil.pad9("" + iRepayBank, 3), iEntryDate, iEntryDate, this.index, this.limit);
		}

		lBankDeductDtl = sBankDeductDtl == null ? null : sBankDeductDtl.getContent();

//		Step1. find Cust Phone Num. & CustId
		if (lBankDeductDtl != null && lBankDeductDtl.size() != 0) {
			int n = 0;

			for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {

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
		if (tmp.getRepayType() == 1 || tmp.getRepayType() == 3) {
			this.info("RepayType() == 1 3...");
			if (!custLoanFlag.containsKey(tmp.getCustNo())) {

				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<期款扣款通知>");
				tTxToDoDetail.setItemCode("TEXT00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(txToDoCom.getProcessNoteForText(custPhone.get(tmp), "親愛的客戶，繳款通知；新光人壽關心您。", iEntryDate));

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

				// Step3. send L6001
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setCustNo(tmp.getCustNo());
				tTxToDoDetail.setFacmNo(tmp.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue("<火險扣款通知>");
				tTxToDoDetail.setItemCode("TEXT00");
				tTxToDoDetail.setStatus(0);
				tTxToDoDetail.setProcessNote(txToDoCom.getProcessNoteForText(custPhone.get(tmp), "您好：提醒您" + sInsuMonth + "月份，除期款外，另加收年度火險地震險費＄" + sInsuAmt + "，請留意帳戶餘額。新光人壽關心您。　　", iEntryDate));

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
				tTxToDoDetail.setDtlValue("<期款扣款通知>");
				tTxToDoDetail.setItemCode("MAIL00");
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
				tTxToDoDetail.setDtlValue("<火險扣款通知>");
				tTxToDoDetail.setItemCode("MAIL00");
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

		private BS442 getEnclosingInstance() {
			return BS442.this;
		}

		@Override
		public String toString() {
			return "tmpFacm [custNo=" + custNo + ", facmNo=" + facmNo + ", repayType=" + repayType + "]";
		}
	}

//	刪除TxToDoDetail 同L4454 須同步更改
	private void dele(String itemCode, String dtlValue, TitaVo titaVo) {
//		刪除未處理且為今天的
		Slice<TxToDoDetail> slTxToDoDetail = txToDoDetailService.itemCodeRange(itemCode, dtlValue, 0, 0, this.getTxBuffer().getTxCom().getTbsdyf(), this.getTxBuffer().getTxCom().getTbsdyf(),
				this.index, this.limit, titaVo);
		if (slTxToDoDetail != null) {
			try {
				this.info("DeleteAll...");
				txToDoCom.addByDetailList(false, 1, slTxToDoDetail.getContent(), titaVo);
			} catch (LogicException e) {
				this.info("DeleteAll Error : " + e.getErrorMsg());
			}
		}
	}
}