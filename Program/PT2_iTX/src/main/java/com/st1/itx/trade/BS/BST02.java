package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcLoanInt;
import com.st1.itx.db.domain.AcLoanIntId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("BST02")
@Scope("prototype")
/**
 * 月底提存計息測試<br>
 * 
 * @author LAI
 * @version 1.0.0
 */
public class BST02 extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	private LoanBorMainService loanBorMainService;

	@Autowired
	private BaTxCom baTxCom;

	@Autowired
	LoanCom loanCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BST02 ......");
		this.totaVo.init(titaVo);
		int iCustNo = parse.stringToInteger(titaVo.getParam("Parm"));

		int yearMonth = this.getTxBuffer().getMgBizDate().getTbsDyf() / 100; // 提息年月
		dateUtil.init();
		dateUtil.setDate_1(this.getTxBuffer().getMgBizDate().getTbsDy());
		dateUtil.setMons(1);

		int intEndDate = (dateUtil.getCalenderDay() / 100) * 100 + 01; // 計算止日 =>下個月1日
		int iEntryDate = this.getTxBuffer().getMgBizDate().getTmnDy(); // 入帳日 ==> 月底日曆日

		procAcLoanint(iCustNo, yearMonth, iEntryDate, intEndDate, titaVo);
		
		return this.sendList();
	}



	private void procAcLoanint(int custNo, int yearMonth, int iEntryDate, int intEndDate, TitaVo titaVo) throws LogicException {
		this.info("BST02 procAcLoanint, yearMonth=" + yearMonth + ", intDate=" + intEndDate);
		int last1MonthDate = 0;
		int last3MonthDate = 0;
		int last6MonthDate = 0;

		dateUtil.init();
		dateUtil.setDate_1(intEndDate);
		dateUtil.setMons(-1);
		last1MonthDate = dateUtil.getCalenderDay();
		this.info("last1MonthDate = " + last1MonthDate);

		dateUtil.init();
		dateUtil.setDate_1(intEndDate);
		dateUtil.setMons(-3);
		last3MonthDate = dateUtil.getCalenderDay();
		this.info("last3MonthDate = " + last3MonthDate);

		dateUtil.init();
		dateUtil.setDate_1(intEndDate);
		dateUtil.setMons(-6);
		last6MonthDate = dateUtil.getCalenderDay();
		this.info("last6MonthDate = " + last6MonthDate);

		baTxCom.setTxBuffer(this.getTxBuffer());

		ArrayList<BaTxVo> lBaTxVo = new ArrayList<BaTxVo>();
		int termNo = 0;
		String acBookCode = null;
		String acSubBookCode = null;
		// find all loanBorMain status = 0 //
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(custNo, 0, 999, 0, 990, this.index,
				Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain != null) {
			for (LoanBorMain ln : lLoanBorMain) {
				if (ln.getStatus() > 0) {
					continue;
				}
				if (ln.getDrawdownDate() >= intEndDate) {
					this.info("DrawdownDate() >= intEndDate " + ln.toString());
					continue;
				}
				try {
					lBaTxVo = baTxCom.acLoanInt(iEntryDate, intEndDate, ln.getCustNo(), ln.getFacmNo(), ln.getBormNo(),
							titaVo); // 提存
				} catch (LogicException e) {
					this.error("ErrorMsg :" + e.getErrorMsg(titaVo) + " " + ln.getCustNo() + "-" + ln.getFacmNo() + "-"
							+ ln.getBormNo());
					continue;
				}
				//
				// 以上次應繳日與月底日的月差判斷 0.一個月以下 1.一~三個月 2.三~六個月3.六個月以上
				// 2022/1 月底提存 , last1MonthDate=2022/01/01 , last3MonthDate=2022/11/01,
				// last6MonthDate = 2022/08/01
				// 上次應繳日 >= 2022/01/01 => 0.一個月以下 , 2022/01 ...
				// 上次應繳日 >= 2022/11/01 => 1.一~三個月 , 2022/11, 2021/12
				// 上次應繳日 >= 2022/08/01 => 2.三~六個月 , 2021/08, 2021/09, 2021/10
				// 上次應繳日 < 2022/08/01 => 3.六個月以上 , 2021/07 ...

				int aging = 0;
				if (ln.getPrevPayIntDate() >= last1MonthDate) {
					aging = 0;
				} else if (ln.getPrevPayIntDate() >= last3MonthDate) {
					aging = 1;
				} else if (ln.getPrevPayIntDate() >= last6MonthDate) {
					aging = 2;
				} else {
					aging = 3;
				}

				termNo = 0; // 期數編號需重算，因月底最後一段期數與前一段相同
				if (lBaTxVo != null) {
					for (BaTxVo ba : lBaTxVo) {
						AcLoanInt tAcLoanInt = new AcLoanInt();
						AcLoanIntId tAcLoanIntId = new AcLoanIntId();
						tAcLoanIntId.setYearMonth(yearMonth); // 提息年月
						tAcLoanIntId.setCustNo(ba.getCustNo()); // 借款人戶號
						tAcLoanIntId.setFacmNo(ba.getFacmNo()); // 額度編號
						tAcLoanIntId.setBormNo(ba.getBormNo()); // 撥款序號
						termNo++;
						// tAcLoanIntId.setTermNo(ba.getPaidTerms()); // 期數編號
						tAcLoanIntId.setTermNo(termNo); // 期數編號
						tAcLoanInt.setAcLoanIntId(tAcLoanIntId);
						tAcLoanInt.setIntStartDate(ba.getIntStartDate()); // 計息起日
						tAcLoanInt.setIntEndDate(ba.getIntEndDate()); // 計息止日
						tAcLoanInt.setAmount(ba.getAmount()); // 計息本金
						tAcLoanInt.setIntRate(ba.getIntRate()); // 計息利率
						tAcLoanInt.setPrincipal(ba.getPrincipal()); // 回收本金
						tAcLoanInt.setInterest(ba.getInterest()); // 利息
						tAcLoanInt.setDelayInt(ba.getDelayInt()); // 延滯息
						tAcLoanInt.setBreachAmt(ba.getBreachAmt()); // 違約金
						tAcLoanInt.setRateIncr(ba.getRateIncr()); // 加碼利率
						tAcLoanInt.setIndividualIncr(ba.getIndividualIncr()); // 個別加碼利率
						if ("3".equals(ba.getAcctCode().substring(0, 1))) {
							tAcLoanInt.setAcctCode(loanCom.setIntAcctCode(ba.getAcctCode())); //
						} else {
							tAcLoanInt.setAcctCode(ba.getAcctCode()); //
						}
						tAcLoanInt.setPayIntDate(ba.getPayIntDate()); // 應繳息日
						tAcLoanInt.setLoanBal(ba.getLoanBal()); // 放款餘額
						tAcLoanInt.setAging(aging); // 帳齡
						tAcLoanInt.setAcBookCode(acBookCode); // 帳冊別
						tAcLoanInt.setAcSubBookCode(acSubBookCode); // 區隔帳冊
						tAcLoanInt.setBranchNo(ln.getBranchNo());
						this.info("tAcLoanInt=" + tAcLoanInt.toString());
					}
				}
			}
		}
	}
}