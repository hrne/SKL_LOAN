package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcLoanInt;
import com.st1.itx.db.domain.AcLoanIntCashFlow;
import com.st1.itx.db.domain.AcLoanIntCashFlowId;
import com.st1.itx.db.domain.AcLoanIntId;
import com.st1.itx.db.domain.CdCashFlow;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.AcLoanIntCashFlowService;
import com.st1.itx.db.service.AcLoanIntService;
import com.st1.itx.db.service.CdCashFlowService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("BS060")
@Scope("prototype")
/**
 * 現金流量預估資料檔維護<br>
 * 執行時機：日始作業(BS001)，月底前五個營業日執行 1.預估日期=下個月月底日 2.找撥款正常戶，進行計息
 * 3.更新現金流量預估資料檔，利息收入、本金攤還金額、到期清償金額
 * 
 * @author w.y.Lai
 * @version 1.0.0
 */
public class BS060 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BaTxCom baTxCom;

	@Autowired
	public CdCashFlowService cdCashFlowService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	private AcLoanIntCashFlowService acLoanIntCashFlowService;

	private int commitCnt = 20;

	private int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS060 ");

		baTxCom.setTxBuffer(this.getTxBuffer());

		// bizDate 月底前五個營業日
		dateUtil.init();
		int bizDate = dateUtil.getbussDate(this.txBuffer.getMgBizDate().getMfbsDyf(), -5);
		this.info("月底前五個營業日=" + bizDate);
		// entryDate 預估日期=下個月月底日
		int entryDate = (this.txBuffer.getMgBizDate().getTbsDy() / 100) * 100 + 01;

		dateUtil.init();
		dateUtil.setDate_1(entryDate);
		dateUtil.setMons(2);
		entryDate = dateUtil.getCalenderDay();

		dateUtil.init();
		dateUtil.setDate_1(entryDate);
		dateUtil.setDays(-1);
		entryDate = dateUtil.getCalenderDay();
		int beginDate = (entryDate / 100) * 100 + 01;
		this.info("預估日期=" + beginDate + "~" + entryDate);
		BigDecimal interestIncome = BigDecimal.ZERO; // 利息收入
		BigDecimal principalAmortizeAmt = BigDecimal.ZERO; // 本金攤還金額
		BigDecimal duePaymentAmt = BigDecimal.ZERO; // 到期清償金額
		this.index = 0;
		this.limit = Integer.MAX_VALUE;

		int cntTrans = 0;
		int yearMonth = (entryDate / 100) + 191100;

		Slice<AcLoanIntCashFlow> slAcLoanIntCashFlow = acLoanIntCashFlowService.findYearMonthEq(yearMonth, this.index,
				Integer.MAX_VALUE, titaVo);
		List<AcLoanIntCashFlow> lAcLoanIntCashFlow = slAcLoanIntCashFlow == null ? null
				: slAcLoanIntCashFlow.getContent();
		if (lAcLoanIntCashFlow != null) {
			try {
				acLoanIntCashFlowService.deleteAll(lAcLoanIntCashFlow, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0008", "AcLoanInt delete " + e.getErrorMsg());
			}
		}

		ArrayList<BaTxVo> lBaTxVo = new ArrayList<BaTxVo>();
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.nextPayIntDateRange(0, entryDate + 19110000, 0,
				this.index, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		lAcLoanIntCashFlow = new ArrayList<AcLoanIntCashFlow>();

		this.batchTransaction.commit();
		if (lLoanBorMain != null) {
			for (LoanBorMain ln : lLoanBorMain) {
				if (cntTrans > this.commitCnt) {
					cntTrans = 0;
					if (lAcLoanIntCashFlow.size() > 0) {
						try {
							acLoanIntCashFlowService.insertAll(lAcLoanIntCashFlow, titaVo);
						} catch (DBException e) {
							e.printStackTrace();
							throw new LogicException(titaVo, "E0005", "AcLoanIntCashFlow insert " + e.getErrorMsg());
						}
					}
					this.batchTransaction.commit();
					lAcLoanIntCashFlow = new ArrayList<AcLoanIntCashFlow>();
				}
				cntTrans++;
				// baTxCom.cashFlow 現金流量預估
				lBaTxVo = new ArrayList<BaTxVo>();
				try {
					lBaTxVo = baTxCom.cashFlow(entryDate, ln.getCustNo(), ln.getFacmNo(), ln.getBormNo(), titaVo);
				} catch (LogicException e) {
					this.info("ErrorMsg :" + e.getErrorMsg(titaVo) + " " + ln.getCustNo() + "-" + ln.getFacmNo() + "-"
							+ ln.getBormNo());
				}
				if (lBaTxVo != null) {
					int termNo = 0;
					for (BaTxVo ba : lBaTxVo) {
						AcLoanIntCashFlow tAc = new AcLoanIntCashFlow();
						AcLoanIntCashFlowId tAcId = new AcLoanIntCashFlowId();
						tAcId.setYearMonth(yearMonth); // 提息年月
						tAcId.setCustNo(ba.getCustNo()); // 借款人戶號
						tAcId.setFacmNo(ba.getFacmNo()); // 額度編號
						tAcId.setBormNo(ba.getBormNo()); // 撥款序號
						termNo++;
						// tAcId.setTermNo(ba.getPaidTerms()); // 期數編號
						tAcId.setTermNo(termNo); // 期數編號
						tAc.setAcLoanIntCashFlowId(tAcId);
						tAc.setIntStartDate(ba.getIntStartDate()); // 計息起日
						tAc.setIntEndDate(ba.getIntEndDate()); // 計息止日
						tAc.setAmount(ba.getAmount()); // 計息本金
						tAc.setIntRate(ba.getIntRate()); // 計息利率
						tAc.setPrincipal(ba.getPrincipal()); // 回收本金
						tAc.setInterest(ba.getInterest()); // 利息
						tAc.setDelayInt(ba.getDelayInt()); // 延滯息
						tAc.setBreachAmt(ba.getBreachAmt()); // 違約金
						tAc.setAmortizedCode(ln.getAmortizedCode());
						tAc.setIntCalcCode(ln.getIntCalcCode());
						tAc.setAcctCode(ba.getAcctCode()); //
						tAc.setPayIntDate(ba.getPayIntDate()); // 應繳息日
						tAc.setLoanBal(ba.getLoanBal()); // 放款餘額
						lAcLoanIntCashFlow.add(tAc);
						cnt++;
						if (ba.getPayIntDate() >= beginDate && ba.getPayIntDate() <= entryDate) {
							interestIncome = interestIncome.add(ba.getInterest());
							if ("3".equals(ln.getAmortizedCode()) || "4".equals(ln.getAmortizedCode()))
								principalAmortizeAmt = principalAmortizeAmt.add(ba.getPrincipal());
							else
								duePaymentAmt = duePaymentAmt.add(ba.getPrincipal());
						}
					}
				}
			}
		}

		if (lAcLoanIntCashFlow.size() > 0) {
			try {
				acLoanIntCashFlowService.insertAll(lAcLoanIntCashFlow, titaVo);
			} catch (DBException e) {
				e.printStackTrace();
				throw new LogicException(titaVo, "E0005", "AcLoanIntCashFlow insert " + e.getErrorMsg());
			}
		}
		this.info("AcLoanIntCashFlow cnt=" + cnt);
		CdCashFlow tCdCashFlow = cdCashFlowService.holdById(yearMonth, titaVo);
		if (tCdCashFlow == null) {
			tCdCashFlow = new CdCashFlow();
			tCdCashFlow.setDataYearMonth(yearMonth);
			tCdCashFlow.setInterestIncome(interestIncome); // 利息收入
			tCdCashFlow.setPrincipalAmortizeAmt(principalAmortizeAmt); // 本金攤還金額
			tCdCashFlow.setDuePaymentAmt(duePaymentAmt); // 到期清償金額
			try {
				cdCashFlowService.insert(tCdCashFlow, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "CdCashFlow insert " + e.getErrorMsg());
			}
		} else {
			tCdCashFlow.setInterestIncome(interestIncome); // 利息收入
			tCdCashFlow.setPrincipalAmortizeAmt(principalAmortizeAmt); // 本金攤還金額
			tCdCashFlow.setDuePaymentAmt(duePaymentAmt); // 到期清償金額
			try {
				cdCashFlowService.update(tCdCashFlow, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "CdCashFlow update " + e.getErrorMsg());
			}
		}
		// end
		this.batchTransaction.commit();
		return null;

	}
}