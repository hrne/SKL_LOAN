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
import com.st1.itx.db.domain.CdCashFlow;
import com.st1.itx.db.domain.LoanBorMain;
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

	private int commitCnt = 20;

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

		BigDecimal loanBal = BigDecimal.ZERO; // 放款餘額(還款前、只放第一期)
		BigDecimal principal = BigDecimal.ZERO; // 本金
		BigDecimal interest = BigDecimal.ZERO; // 利息
		int cntTrans = 0;

		ArrayList<BaTxVo> lBaTxVo = new ArrayList<BaTxVo>();
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.nextPayIntDateRange(0, entryDate + 19110000, 0, this.index, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();

		this.batchTransaction.commit();
		if (lLoanBorMain != null)
			for (LoanBorMain bo : lLoanBorMain) {
				{
					if (cntTrans > this.commitCnt) {
						cntTrans = 0;
						this.batchTransaction.commit();
					}
					cntTrans++;
					// baTxCom.cashFlow 現金流量預估
					try {
						lBaTxVo = baTxCom.cashFlow(entryDate, bo.getCustNo(), bo.getFacmNo(), bo.getBormNo(), titaVo);
					} catch (LogicException e) {
						this.info("ErrorMsg :" + e.getErrorMsg(titaVo) + " " + bo.getCustNo() + "-" + bo.getFacmNo() + "-" + bo.getBormNo());
					}
					if (lBaTxVo != null) {
						loanBal = BigDecimal.ZERO;
						principal = BigDecimal.ZERO;
						interest = BigDecimal.ZERO;
						for (BaTxVo baTxVo : lBaTxVo) {
							if (baTxVo.getPayIntDate() >= beginDate && baTxVo.getPayIntDate() <= entryDate) {
								loanBal = loanBal.add(baTxVo.getLoanBal());
								principal = principal.add(baTxVo.getPrincipal());
								interest = interest.add(baTxVo.getInterest());
							}
						}
					}
					interestIncome = interestIncome.add(interest);
					if (loanBal.equals(principal))
						duePaymentAmt = duePaymentAmt.add(principal);
					else
						principalAmortizeAmt = principalAmortizeAmt.add(principal);
				}
			}

		int yearMonth = (entryDate / 100) + 191100;

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