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
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankDeductDtlId;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.PostAuthLog;
import com.st1.itx.db.service.AchDeductMediaService;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.PostAuthLogService;
import com.st1.itx.db.service.PostDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.data.CheckAmlVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * EntryDate=9,7<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * PrevIntDate=9,7<br>
 * RepayType=9,2<br>
 * RepayAmt=9,14.2<br>
 * END=X,1<br>
 */

@Service("L4451")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4451 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4451.class);

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
	public FacMainService facMainService;
	@Autowired
	public AchDeductMediaService achDeductMediaService;
	@Autowired
	public PostDeductMediaService postDeductMediaService;
	@Autowired
	public TxAmlCom txAmlCom;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public PostAuthLogService postAuthLogService;
	@Autowired
	public AuthLogCom authLogCom;
	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public BankAuthActService bankAuthActService;

	private TempVo tempVo = new TempVo();
	private BigDecimal unpaidAmt = BigDecimal.ZERO;
	private BigDecimal tempAmt = BigDecimal.ZERO;
	private BigDecimal repayAmt = BigDecimal.ZERO;
	private int postMediaSeq = 0;
	private int achMediaSeq1 = 0;
	private int achMediaSeq2 = 0;
	private BigDecimal limitAmt = BigDecimal.ZERO;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4451 ");
		this.totaVo.init(titaVo);

		unpaidAmt = parse.stringToBigDecimal(titaVo.getParam("UnpaidAmt"));
		tempAmt = parse.stringToBigDecimal(titaVo.getParam("TempAmt"));
		repayAmt = unpaidAmt.subtract(tempAmt);

		this.info("unpaidAmt = " + unpaidAmt);
		this.info("tempAmt = " + tempAmt);
		this.info("repayAmt = " + repayAmt);

		BankDeductDtl tBankDeductDtl = new BankDeductDtl();

		String iFunctionCode = titaVo.getParam("FunctionCode").trim();
		int rocAcDate = parse.stringToInteger(titaVo.getParam("AcDate"));

		this.info("iFunctionCode : " + iFunctionCode);
		this.info("rocAcDate : " + rocAcDate);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		// 主key = EntryDate,CustNo,FacmNo,PrevIntDate,RepayType
		BankDeductDtlId tBankDeductDtlId = new BankDeductDtlId();

		// 新增
		if (iFunctionCode.equals("1")) {

			FacMainId tFacMainId = new FacMainId();
			tFacMainId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tFacMainId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			FacMain tFacMain = facMainService.findById(tFacMainId);

			tempVo = authLogCom.exec(parse.stringToInteger(titaVo.getParam("CustNo")),
					parse.stringToInteger(titaVo.getParam("FacmNo")), titaVo);

			HashMap<tmpFacm, Integer> prevPayIntDateList = new HashMap<>();
			HashMap<tmpFacm, Integer> prevRepaidDateList = new HashMap<>();
			tmpFacm tmpBormList = new tmpFacm(parse.stringToInteger(titaVo.getParam("CustNo")),
					parse.stringToInteger(titaVo.getParam("FacmNo")));

			List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

			Slice<LoanBorMain> sLoanBorMain = null;

			sLoanBorMain = loanBorMainService.bormCustNoEq(parse.stringToInteger(titaVo.getParam("CustNo")),
					parse.stringToInteger(titaVo.getParam("FacmNo")), parse.stringToInteger(titaVo.getParam("FacmNo")),
					0, 999, this.index, this.limit);

			lLoanBorMain = sLoanBorMain == null ? null : sLoanBorMain.getContent();

			if (lLoanBorMain != null && lLoanBorMain.size() != 0) {
				for (LoanBorMain tLoanBorMain : lLoanBorMain) {
					if (!prevPayIntDateList.containsKey(tmpBormList)) {
						prevPayIntDateList.put(tmpBormList, tLoanBorMain.getPrevPayIntDate());
					} else {
						if (tLoanBorMain.getPrevPayIntDate() < prevPayIntDateList.get(tmpBormList)) {
							prevPayIntDateList.put(tmpBormList, tLoanBorMain.getPrevPayIntDate());
						}
					}
					if (!prevRepaidDateList.containsKey(tmpBormList)) {
						prevRepaidDateList.put(tmpBormList, tLoanBorMain.getPrevRepaidDate());
					} else {
						if (tLoanBorMain.getPrevRepaidDate() < prevRepaidDateList.get(tmpBormList)) {
							prevRepaidDateList.put(tmpBormList, tLoanBorMain.getPrevRepaidDate());
						}
					}
				}
			} else {
				throw new LogicException(titaVo, "E0001", "L4451 無此戶號額度於撥款檔");
			}

			tBankDeductDtlId.setEntryDate(parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000);
			tBankDeductDtlId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tBankDeductDtlId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tBankDeductDtlId.setBormNo(parse.stringToInteger(titaVo.getParam("BormNo")));
			tBankDeductDtlId.setPayIntDate(parse.stringToInteger(titaVo.getParam("PayIntDate")) + 19110000);
			tBankDeductDtlId.setRepayType(parse.stringToInteger(titaVo.getParam("RepayType")));

			tBankDeductDtl.setBankDeductDtlId(tBankDeductDtlId);

			tBankDeductDtl.setPrevIntDate(parse.stringToInteger(titaVo.getParam("PrevIntDate")));

			tBankDeductDtl.setUnpaidAmt(unpaidAmt);
			tBankDeductDtl.setTempAmt(tempAmt);
			tBankDeductDtl.setRepayAmt(repayAmt);

			if (tempVo != null && tFacMain != null) {
				tBankDeductDtl.setAcctCode(tFacMain.getAcctCode());
				tBankDeductDtl.setRepayBank(tempVo.getParam("RepayBank"));
				tBankDeductDtl.setRepayAcctNo(FormatUtil.pad9(tempVo.getParam("RepayAcctNo"), 14));
				tBankDeductDtl.setPostCode(tempVo.getParam("PostCode"));
				tBankDeductDtl.setRelationCode(tempVo.getParam("RelationCode"));
				tBankDeductDtl.setRelCustName(tempVo.getParam("RelationName"));
				tBankDeductDtl.setRelCustId(tempVo.getParam("RelationId"));

				PostAuthLog tPostAuthLog = postAuthLogService.pkFacmNoFirst("1", tFacMain.getCustNo(),
						tempVo.getParam("PostCode"), tempVo.getParam("RepayAcctNo"), "1", tFacMain.getFacmNo());
				if (tPostAuthLog != null) {
					if (tPostAuthLog.getRepayAcctSeq() != null) {
						tBankDeductDtl.setRepayAcctSeq(tPostAuthLog.getRepayAcctSeq());
					}
				}

				if ("700".equals(tempVo.getParam("RepayBank"))) {
					tBankDeductDtl.setMediaKind("3");

					setMediaSeq("3", titaVo);
					this.info("postMediaSeq : " + postMediaSeq);

					tBankDeductDtl.setMediaSeq(postMediaSeq);
				} else if ("103".equals(tempVo.getParam("RepayBank"))) {
					tBankDeductDtl.setMediaKind("1");

					setMediaSeq("1", titaVo);
					this.info("achMediaSeq1 : " + achMediaSeq1);

					tBankDeductDtl.setMediaSeq(achMediaSeq1);
				} else {
					tBankDeductDtl.setMediaKind("2");

					setMediaSeq("2", titaVo);
					this.info("achMediaSeq2 : " + achMediaSeq2);

					tBankDeductDtl.setMediaSeq(achMediaSeq2);
				}
			} else {
				throw new LogicException(titaVo, "E0001", "L4451 無此戶號額度於額度檔");
			}
			if (lLoanBorMain != null && lLoanBorMain.size() != 0) {
				tBankDeductDtl.setIntStartDate(prevPayIntDateList.get(tmpBormList));
				tBankDeductDtl.setIntEndDate(prevRepaidDateList.get(tmpBormList));
			}

//			tBankDeductDtl.setMediaDate(this.getTxBuffer().getTxCom().getTbsdyf());
			txAmlCom.setTxBuffer(this.txBuffer);
			CheckAmlVo tCheckAmlVo = txAmlCom.deduct(tBankDeductDtl, titaVo);

			String amlRsp = tCheckAmlVo.getConfirmStatus();
			tBankDeductDtl.setAmlRsp(amlRsp);

			String warningMsg = "";

			String failFlag = checkAcctAuth(titaVo);
			TempVo tTempVo = new TempVo();

//			Aml檢核未過
			if (!"0".equals(amlRsp)) {
				warningMsg += "Aml檢核：" + amlMSg(amlRsp, titaVo) + "。";
				tTempVo.putParam("Aml", amlRsp);
			}
//			帳號授權檢核未過
			if (!"0".equals(failFlag)) {
				warningMsg += "帳號授權檢核：" + authX(failFlag, titaVo) + "。";
				tTempVo.putParam("Auth", failFlag);
			}
//			繳款金額=0(抵繳)
			if (repayAmt.compareTo(BigDecimal.ZERO) == 0) {
				warningMsg += "繳款金額為零。";
				tTempVo.putParam("Deduct", 1);
			}
//			扣款金額超過帳號設定限額(限額為零不檢查)
			if (limitAmt.compareTo(BigDecimal.ZERO) > 0 && repayAmt.compareTo(limitAmt) > 0) {
				warningMsg += "超過帳戶限額。";
				tTempVo.putParam("Deduct", "超過帳戶限額");
			}
			
			tBankDeductDtl.setJsonFields(tTempVo.getJsonString());

			this.info("amlRsp = " + amlRsp);
			this.info("failFlag = " + failFlag);
			this.info("warningMsg = " + warningMsg);

			if (!"".equals(warningMsg)) {
				totaVo.putParam("OWarningMsg", warningMsg + "請確認。");
			} else {
				totaVo.putParam("OWarningMsg", warningMsg);
			}

			try {
				bankDeductDtlService.insert(tBankDeductDtl);
				this.info("L4451 insert Success");
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "L4451 BankDeductDtl insert " + e.getErrorMsg());
			}
		} else if (iFunctionCode.equals("2") && rocAcDate != 0) {

//			EntryDate	入帳日期
//			CustNo		戶號
//			FacmNo		額度
//			BormNo		撥款
//			RepayType	還款類別
//			PayIntDate	應繳日
			int entryDate = 0;
			int custNo = 0;
			int facmNo = 0;
			int bormNo = 0;
			int payIntDate = 0;
			int repayType = 0;

			if (titaVo.getParam("EntryDate") != null && parse.stringToInteger(titaVo.getParam("EntryDate")) != 0) {
				entryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
			}
			if (titaVo.getParam("CustNo") != null) {
				custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
			}
			if (titaVo.getParam("FacmNo") != null) {
				facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
			}
			if (titaVo.getParam("BormNo") != null) {
				bormNo = parse.stringToInteger(titaVo.getParam("BormNo"));
			}
			if (titaVo.getParam("PayIntDate") != null && parse.stringToInteger(titaVo.getParam("PayIntDate")) != 0) {
				payIntDate = parse.stringToInteger(titaVo.getParam("PayIntDate")) + 19110000;
			}
			if (titaVo.getParam("RepayType") != null) {
				repayType = parse.stringToInteger(titaVo.getParam("RepayType"));
			}

			tBankDeductDtlId.setEntryDate(entryDate);
			tBankDeductDtlId.setCustNo(custNo);
			tBankDeductDtlId.setFacmNo(facmNo);
			tBankDeductDtlId.setBormNo(bormNo);
			tBankDeductDtlId.setPayIntDate(payIntDate);
			tBankDeductDtlId.setRepayType(repayType);

			this.info("EntryDate ..." + entryDate);
			this.info("CustNo ..." + custNo);
			this.info("FacmNo ..." + facmNo);
			this.info("BormNo ..." + bormNo);
			this.info("PayIntDate ..." + payIntDate);
			this.info("RepayType ..." + repayType);

			BankDeductDtl t2BankDeductDtl = new BankDeductDtl();
			t2BankDeductDtl.setBankDeductDtlId(tBankDeductDtlId);
//			 抓出要修改的資料
			BankDeductDtl editBankDeductDtl = bankDeductDtlService.holdById(t2BankDeductDtl);

			if (editBankDeductDtl != null) {

				if (!"".equals(editBankDeductDtl.getMediaCode())) {
					throw new LogicException(titaVo, "E0015", "已產出媒體檔，修改前須先訂正");
				}

				editBankDeductDtl.setUnpaidAmt(unpaidAmt);
				editBankDeductDtl.setTempAmt(tempAmt);
				editBankDeductDtl.setRepayAmt(repayAmt);

				txAmlCom.setTxBuffer(this.txBuffer);
				CheckAmlVo tCheckAmlVo = txAmlCom.deduct(editBankDeductDtl, titaVo);

				String amlRsp = tCheckAmlVo.getConfirmStatus();
				editBankDeductDtl.setAmlRsp(amlRsp);
				String warningMsg = "";

				String failFlag = checkAcctAuth(titaVo);
				TempVo tTempVo = new TempVo();

//				Aml檢核未過
				if (!"0".equals(amlRsp)) {
					warningMsg += "Aml檢核：" + amlMSg(amlRsp, titaVo) + "。";
					tTempVo.putParam("Aml", amlRsp);
				}
//				帳號授權檢核未過
				if (!"0".equals(failFlag)) {
					warningMsg += "帳號授權檢核：" + authX(failFlag, titaVo) + "。";
					tTempVo.putParam("Auth", failFlag);
				}
//				繳款金額=0(抵繳)
				if (repayAmt.compareTo(BigDecimal.ZERO) == 0) {
					warningMsg += "繳款金額為零。";
					tTempVo.putParam("Deduct", "扣款金額為零");
				}
				
//				扣款金額超過帳號設定限額(限額為零不檢查)
				if (limitAmt.compareTo(BigDecimal.ZERO) > 0 && repayAmt.compareTo(limitAmt) > 0) {
					warningMsg += "超過帳戶限額。";
					tTempVo.putParam("Deduct", "超過帳戶限額");
				}
				
				editBankDeductDtl.setJsonFields(tTempVo.getJsonString());
				
				this.info("amlRsp = " + amlRsp);
				this.info("failFlag = " + failFlag);
				this.info("warningMsg = " + warningMsg);

				if (!"".equals(warningMsg)) {
					totaVo.putParam("OWarningMsg", warningMsg + "請確認。");
				} else {
					totaVo.putParam("OWarningMsg", warningMsg);
				}

				try {
//					 送出到DB
					bankDeductDtlService.update(editBankDeductDtl);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "L4451 editBankDeductDtl update " + e.getErrorMsg());
				}
			}
		} else if (iFunctionCode.equals("4") && rocAcDate != 0) {

			int entryDate = 0;
			int custNo = 0;
			int facmNo = 0;
			int bormNo = 0;
			int payIntDate = 0;
			int repayType = 0;

			if (titaVo.getParam("EntryDate") != null && parse.stringToInteger(titaVo.getParam("EntryDate")) != 0) {
				entryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
			}
			if (titaVo.getParam("CustNo") != null) {
				custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
			}
			if (titaVo.getParam("FacmNo") != null) {
				facmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
			}
			if (titaVo.getParam("BormNo") != null) {
				bormNo = parse.stringToInteger(titaVo.getParam("BormNo"));
			}
			if (titaVo.getParam("PayIntDate") != null && parse.stringToInteger(titaVo.getParam("PayIntDate")) != 0) {
				payIntDate = parse.stringToInteger(titaVo.getParam("PayIntDate")) + 19110000;
			}
			if (titaVo.getParam("RepayType") != null) {
				repayType = parse.stringToInteger(titaVo.getParam("RepayType"));
			}

			tBankDeductDtlId.setEntryDate(entryDate);
			tBankDeductDtlId.setCustNo(custNo);
			tBankDeductDtlId.setFacmNo(facmNo);
			tBankDeductDtlId.setBormNo(bormNo);
			tBankDeductDtlId.setPayIntDate(payIntDate);
			tBankDeductDtlId.setRepayType(repayType);

			this.info("EntryDate ..." + entryDate);
			this.info("CustNo ..." + custNo);
			this.info("FacmNo ..." + facmNo);
			this.info("BormNo ..." + bormNo);
			this.info("PayIntDate ..." + payIntDate);
			this.info("RepayType ..." + repayType);

			totaVo.putParam("OWarningMsg", "");
			BankDeductDtl t2BankDeductDtl = new BankDeductDtl();
			t2BankDeductDtl.setBankDeductDtlId(tBankDeductDtlId);
			this.info("t2BankDeductDtl ..." + t2BankDeductDtl.toString());

			// 抓出要刪除的資料
			BankDeductDtl deleBankDeductDtl = bankDeductDtlService.holdById(t2BankDeductDtl);

			if (deleBankDeductDtl != null) {
				if ("Y".equals(deleBankDeductDtl.getMediaCode())) {
					throw new LogicException(titaVo, "E0015", "已產出媒體檔，刪除前須先訂正");
				}

				try {
					bankDeductDtlService.delete(deleBankDeductDtl);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		} else {
			throw new LogicException(titaVo, "E0010", "FunctionCode 錯誤!!");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String amlMSg(String amlRsp, TitaVo titaVo) {
		String result = "";

		CdCode t1CdCode = cdCodeService.getItemFirst(4, "AmlCheckItem", amlRsp, titaVo);

		if (!"0".equals(amlRsp)) {
			result = t1CdCode.getItem();
		}

		return result;
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + facmNo;
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
			return true;
		}

		private L4451 getEnclosingInstance() {
			return L4451.this;
		}
	}

	private void setMediaSeq(String mediaKind, TitaVo titaVo) {
		BankDeductDtl tBankDeductDtl = new BankDeductDtl();

		tBankDeductDtl = bankDeductDtlService.findL4451First(this.getTxBuffer().getTxCom().getTbsdyf(),
				this.getTxBuffer().getTxCom().getTbsdyf(), mediaKind, titaVo);

		if (tBankDeductDtl != null) {
			switch (mediaKind) {
			case "1":
				achMediaSeq1 = tBankDeductDtl.getMediaSeq() + 1;
				break;
			case "2":
				achMediaSeq2 = tBankDeductDtl.getMediaSeq() + 1;
				break;
			case "3":
				postMediaSeq = tBankDeductDtl.getMediaSeq() + 1;
				break;
			}
		}
	}

	private String checkAcctAuth(TitaVo titaVo) throws LogicException {
		String failFlag = " ";
		limitAmt = BigDecimal.ZERO;
		BankAuthAct tBankAuthAct = new BankAuthAct();
		BankAuthActId tBankAuthActId = new BankAuthActId();

		tBankAuthActId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tBankAuthActId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));

		if (tempVo != null) {
			if ("700".equals(tempVo.getParam("RepayBank"))) {
				tBankAuthActId.setAuthType("01");
			} else {
				tBankAuthActId.setAuthType("00");
			}
		}

		tBankAuthAct = bankAuthActService.findById(tBankAuthActId, titaVo);

		if (tBankAuthAct != null) {
			failFlag = tBankAuthAct.getStatus();
			limitAmt = tBankAuthAct.getLimitAmt();
		}

		return failFlag;
	}
	
	private String authX(String auth, TitaVo titaVo) {
		String result = "";

		CdCode cdCode = cdCodeService.getItemFirst(4, "AuthStatusCode", auth, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}
}