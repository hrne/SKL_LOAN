package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3250 還款來源冲正
 * a.使用時機:將暫收款金額轉回還款來源
 * b.本交易不可訂正
 */

/*
 * Tita
 * TimCustNo=9,7 戶號
 * FacmNo=9,3  額度編號 
 * EntryDate 入帳日期
 * AcDate 會計日期
 * AcDate 經辦
 * TxtNo 交易序號
  * 沖正交易序號 ，會計日(7)+單位別(4)+經辦(6)+交易序號(8)
 */

/**
 * L3250 回收登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3250")
@Scope("prototype")
public class L3250 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public InsuRenewService insuRenewService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public CdAcCodeService cdAcCodeService;
	@Autowired
	public ForeclosureFeeService foreclosureFeeService;
	@Autowired
	public AcDetailService acDetailService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcRepayCom acRepayCom;
	@Autowired
	AcReceivableCom acReceivableCom;
	@Autowired
	BaTxCom baTxCom;
	@Autowired
	public SendRsp sendRsp;

	private TitaVo titaVo;
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private BigDecimal iTempAmt = BigDecimal.ZERO;
	private String iRvNo;
	private String iAcctCode;
	private BigDecimal wkTxAmt = BigDecimal.ZERO;
	private BigDecimal wkTempAmt = BigDecimal.ZERO;
	private int wkRepayCode = 0;
	private String wkReconCode = "";

	// work area
	private AcDetail acDetail;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private TempVo tTempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3250 ");

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		loanCom.setTxBuffer(this.txBuffer);
		baTxCom.setTxBuffer(this.txBuffer);
		acRepayCom.setTxBuffer(this.getTxBuffer());

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iTempAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTempAmt"));
		iRvNo = titaVo.getParam("RvNo");
		iAcctCode = titaVo.getParam("AcctCode");
		// Check Input
		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0005", ""); // 訂正需主管核可
		}

		checkInputRoutine();

		// 檢查到同戶帳務交易需由最近一筆交易開始訂正
		loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);

		// 沖正處理
		repayEraseRoutine();

		if (wkRepayCode == 0) {
			if (wkTxAmt.compareTo(BigDecimal.ZERO) > 0) {
				wkRepayCode = 9; // 其他
			} else {
				wkRepayCode = 90; // 暫收抵繳
			}
		}

		titaVo.putParam("RpCode1", wkRepayCode);
		titaVo.putParam("RpAmt1", wkTxAmt);
		titaVo.putParam("RpCustNo1", iCustNo);
		titaVo.putParam("RpFacmNo1", 0);
		titaVo.putParam("RpAcctCode1", wkReconCode);
		// 101.匯款轉帳 P03
		// 102.銀行扣款 C01 暫收款－非核心資金運用 核心銷帳碼 0010060yyymmdd (銀扣 ACH), 郵局 P01
		Slice<AcDetail> slAcList = acDetailService.acdtlRelTxseqEq(titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin() + titaVo.getOrgTlr() + titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo); // findByTxseq
		if (slAcList != null) {
			for (AcDetail ac : slAcList.getContent()) {
				if ("D".equals(ac.getDbCr())) {
					if ("P03".equals(ac.getAcctCode()) || "C01".equals(ac.getAcctCode())
							|| "P01".equals(ac.getAcctCode())) {
						String sumNo = "";
						if ("P03".equals(ac.getAcctCode())) {
							sumNo = "101";
						} else {
							sumNo = "102";
						}
						String rvNo = "";
						if ("C01".equals(ac.getAcctCode())) {
							rvNo = "0010060" + titaVo.getOrgEntdyI();
						}
						acDetail = new AcDetail();
						acDetail.setDbCr("D");
						acDetail.setAcctCode(ac.getAcctCode());
						acDetail.setSumNo(sumNo);
						acDetail.setTxAmt(ac.getTxAmt());
						acDetail.setCustNo(iCustNo);
						acDetail.setFacmNo(0);
						lAcDetail.add(acDetail);
						acDetail.setRvNo(rvNo);
						lAcDetail.add(acDetail);
						acDetail = new AcDetail();
						acDetail.setDbCr("C");
						acDetail.setAcctCode(ac.getAcctCode());
						acDetail.setSumNo(sumNo);
						acDetail.setTxAmt(ac.getTxAmt());
						acDetail.setCustNo(iCustNo);
						acDetail.setFacmNo(0);
						lAcDetail.add(acDetail);
						acDetail.setRvNo(rvNo);
						lAcDetail.add(acDetail);
					}
				}
			}
		}

		
		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, 0, 0, 99, BigDecimal.ZERO, titaVo); // 99-費用全部

		// 暫收款金額 (暫收借)
		acRepayCom.settleTempAmt(this.baTxList, this.lAcDetail, titaVo);

		// 暫收款登錄
		if (wkTxAmt.compareTo(BigDecimal.ZERO) > 0) {
			lAcDetail.add(acDetail);
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("THC");
			acDetail.setSumNo("099");
			acDetail.setTxAmt(wkTxAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(0);
			acDetail.setRvNo( "" + titaVo.getOrgEntdyI());
			lAcDetail.add(acDetail);
		}

		// 暫收款銷帳(費用科目)
		if (!iAcctCode.isEmpty()) {
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode(iAcctCode);
			acDetail.setTxAmt(iTempAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setRvNo(iRvNo);
			switch (iAcctCode) {
			case "TMI": // TMI 暫收款－火險保費
			case "F25": // F25 催收款項－火險費用
				checkInsuRenew(acDetail, titaVo);
				break;
			case "F07": // F07 暫付法務費
			case "F24": // F24 催收款項－法務費用
				acDetail.setReceivableFlag(2);
				checkForeclosureFee(acDetail, titaVo);
				break;
			case "10": // 10.沖帳管費/手續費
			case "12": // 12.聯貸件
			case "27": // 27.聯貸管理費
			case "29": // 29.貸後契變手續費
			case "30": // 30.沖呆帳戶法務費墊付
				acDetail.setReceivableFlag(3);
				lAcDetail.add(acDetail);
				break;
			default:
				lAcDetail.add(acDetail);
				// F08 收回呆帳及過期帳
				if ("F08".equals(iAcctCode)) {
					UpdLoanOverDueEraseRoutine();
				}
			}
		}

		// 累溢收入帳(暫收貸)
		acRepayCom.settleOverflow(lAcDetail, titaVo);

		// 產生會計分錄
		this.txBuffer.setAcDetailList(lAcDetail);
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkInputRoutine() throws LogicException {
		if (titaVo.isHcodeErase()) {
			throw new LogicException(titaVo, "E0010", "本交易不可訂正"); // 功能選擇錯誤
		}
	}

	// 沖正處理
	private void repayEraseRoutine() throws LogicException {
		this.info("calcRepayEraseRoutine ...");
		// 查詢放款交易內容檔
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorTx == null) {
			throw new LogicException(titaVo, "E0001", "放款交易內容檔"); // 查詢資料不存在
		}
		for (LoanBorTx tx : slLoanBorTx.getContent()) {
			if (!tx.getTitaHCode().equals("0")) {
				throw new LogicException(titaVo, "E0010", "非正常交易（轉換前資料）"); // 功能選擇錯誤
			}
//			if (!tx.getCreateEmpNo().equals("999999")) {
//				throw new LogicException(titaVo, "E0010", "非轉換資料不可執行L3240回收冲正（轉換前資料）"); // 功能選擇錯誤
//			}
			wkRepayCode = tx.getRepayCode();
			tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(tx.getOtherFields());
			wkReconCode = tTempVo.getParam("ReconCode");
			// 註記交易內容檔
			loanCom.setFacmBorTxHcode(tx.getCustNo(), tx.getFacmNo(), tx.getBorxNo(), titaVo);
			if (tx.getTxAmt().compareTo(BigDecimal.ZERO) > 0) {
				wkTxAmt = wkTxAmt.add(tx.getTxAmt());
			}
		}
	}

	// 火險費用
	private void checkInsuRenew(AcDetail ac, TitaVo titaVo) throws LogicException {
		Slice<InsuRenew> slInsuRenew = insuRenewService.findCustEq(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		if (slInsuRenew != null) {
			for (InsuRenew tInsuRenew : slInsuRenew.getContent()) {
				this.info("InsuRenew=".toString());
				if (tInsuRenew.getAcDate() == titaVo.getOrgEntdyI()
						&& tInsuRenew.getTitaTxtNo().equals(titaVo.getOrgTno())) {
					this.info("this.InsuRenew=".toString());
					ac.setTxAmt(tInsuRenew.getTotInsuPrem());
					wkTempAmt = wkTempAmt.add(tInsuRenew.getTotInsuPrem());
					switch (tInsuRenew.getStatusCode()) {
					case 0:
						ac.setReceivableFlag(3);
						ac.setAcctCode("TMI"); // TMI 火險保費
						break;
					case 1:
						ac.setReceivableFlag(2);
						ac.setAcctCode("F09"); // F09 暫付款－火險保費
						break;
					case 2:
						ac.setReceivableFlag(2);
						ac.setAcctCode("F24"); // F24 催收款項－火險保費
						break;
					default:
						break;
					}
					ac.setCustNo(tInsuRenew.getCustNo());// 戶號+額度
					ac.setFacmNo(tInsuRenew.getFacmNo());
					ac.setRvNo(tInsuRenew.getPrevInsuNo()); // 銷帳編號
					TempVo tTempVo = new TempVo();
					tTempVo.clear();
					tTempVo.putParam("OpenAcDate", tInsuRenew.getInsuStartDate());
					tTempVo.putParam("InsuYearMonth", tInsuRenew.getInsuYearMonth());
					ac.setJsonFields(tTempVo.getJsonString());
					lAcDetail.add(acDetail);
				}
			}
		}
		if (wkTempAmt.compareTo(iTempAmt) != 0) {
			throw new LogicException(titaVo, "E0015", "火險費用不符" + wkTempAmt); // 檢查錯誤
		}
	}

	// 法拍費用
	private void checkForeclosureFee(AcDetail ac, TitaVo titaVo) throws LogicException {
		Slice<ForeclosureFee> slForeclosureFee = foreclosureFeeService.custNoEq(iCustNo, this.index, this.limit,
				titaVo);
		if (slForeclosureFee == null) {
			throw new LogicException(titaVo, "E2003", "法拍費用檔查無資料"); // 查無資料
		}
		int closeDate = 0;
		// 找金額相同(負)
		for (ForeclosureFee tForeclosureFee : slForeclosureFee.getContent()) {
			if (tForeclosureFee.getOpenAcDate() == titaVo.getOrgEntdyI()
					&& tForeclosureFee.getFee().compareTo(BigDecimal.ZERO.subtract(iTempAmt)) == 0) {
				closeDate = tForeclosureFee.getCloseDate();
				break;
			}
		}
		// 找銷號日期相同，金額正值
		if (closeDate > 0) {
			for (ForeclosureFee tForeclosureFee : slForeclosureFee.getContent()) {
				if (tForeclosureFee.getCloseDate() == closeDate
						&& tForeclosureFee.getFee().compareTo(BigDecimal.ZERO) > 0) {
					ac.setTxAmt(tForeclosureFee.getFee()); // 記帳金額
					wkTempAmt = wkTempAmt.add(tForeclosureFee.getFee());
					ac.setCustNo(tForeclosureFee.getCustNo());
					ac.setFacmNo(tForeclosureFee.getFacmNo());
					// 紀錄號碼 7 int轉string左補0
					ac.setRvNo(parse.IntegerToString(tForeclosureFee.getRecordNo(), 7)); // 銷帳編號
					lAcDetail.add(acDetail);
				}
			}
		}
		if (wkTempAmt.compareTo(iTempAmt) != 0) {
			throw new LogicException(titaVo, "E0015", "法拍費用不符" + wkTempAmt); // 檢查錯誤
		}
	}

	private void UpdLoanOverDueEraseRoutine() throws LogicException {
		this.info("UpdLoanOverDueEraseRoutine ...");

		BigDecimal wkTempBal = iTempAmt;
		BigDecimal wkRepayAmt = BigDecimal.ZERO;
		BigDecimal wkTotalRepayAmt = BigDecimal.ZERO;

		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(2);
		lStatus.add(3);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, iFacmNo, iFacmNo, 1, 900, 1, 999,
				lStatus, 0, Integer.MAX_VALUE);
		List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();
		lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();
		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		for (LoanOverdue od : lLoanOverdue) {
			if (od.getBadDebtBal().compareTo(od.getBadDebtAmt()) >= 0) {
				continue;
			}
			if (wkTempBal.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
			wkRepayAmt = od.getBadDebtAmt().subtract(od.getBadDebtAmt());
			wkTotalRepayAmt = wkTotalRepayAmt.add(wkRepayAmt);
			if (wkTempBal.compareTo(wkRepayAmt) >= 0) {
				wkTempBal = wkTempBal.subtract(wkRepayAmt);
			} else {
				wkRepayAmt = wkTempBal;
				wkTempBal = BigDecimal.ZERO;
			}
			od.setBadDebtBal(od.getBadDebtBal().add(wkRepayAmt));
			try {
				loanOverdueService.update(od);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "催收呆帳檔 戶號 = " + od.getCustNo() + " 額度編號 = " + od.getFacmNo()
						+ " 撥款序號 = " + od.getBormNo() + " 催收序號 = " + od.getOvduNo()); // 更新資料時，發生錯誤
			}
		}
		if (wkTempBal.compareTo(BigDecimal.ZERO) > 0) {
			throw new LogicException(titaVo, "E0019", "該戶呆帳收回金額 = " + wkTotalRepayAmt); // 輸入資料錯誤
		}
	}

}