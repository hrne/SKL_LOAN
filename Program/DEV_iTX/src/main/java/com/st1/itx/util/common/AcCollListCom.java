package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.service.CollListService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 法催紀錄清單檔更新<BR>
 * 1.run ： 還款即時更新 call by AcEntetCom<BR>
 * 1.1 資負明細科目（放款、催收款項)出帳時，更新餘額等資訊<BR>
 * 1.2 讀取交易後的放款主檔及催收呆帳檔資訊<BR>
 * 
 * @author st1
 *
 */
@Component("acCollListCom")
@Scope("prototype")
public class AcCollListCom extends TradeBuffer {
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public LoanOverdueService loanOverdueService;

	@Autowired
	public CollListService collListService;

	@Autowired
	public FacStatusCom facStatusCom;

	@Autowired
	public LoanCom loanCom;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	/*-----------  更新法催紀錄清單檔 -------------- */
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("AcCollListCom ... ");

		int bizTbsdy = this.txBuffer.getTxBizDate().getTbsDy();
		int custNo = 0;
		int facmNo = 0;
		for (AcDetail ac : this.txBuffer.getAcDetailList()) {
			// 資負明細科目（放款、催收款項) --> 法催紀錄清單檔
			if (ac.getAcctFlag() == 1) {
				if (ac.getCustNo() != custNo && ac.getFacmNo() != facmNo) {
					custNo = ac.getCustNo();
					facmNo = ac.getFacmNo();
					updCollList(bizTbsdy, custNo, facmNo, titaVo);
				}
			}
		}

		return null;
	}

	/* 更新法催紀錄清單檔 */
	private void updCollList(int bizTbsdy, int custNo, int facmNo, TitaVo titaVo) throws LogicException {
		CollList tCollList = collListService.holdById(new CollListId(custNo, facmNo), titaVo);
		if (tCollList != null) {
			tCollList.setPrevIntDate(0); // 繳息迄日
			tCollList.setNextIntDate(0); // 應繳息日
			tCollList.setPrinBalance(BigDecimal.ZERO); // 本金餘額 催收餘額
			tCollList.setBadDebtBal(BigDecimal.ZERO); // 呆帳餘額
			tCollList.setOvduTerm(0); // 逾期期數
			tCollList.setOvduDays(0); // 逾期天數
			tCollList.setStatus(0); // 戶況
			tCollList.setAcctCode(tCollList.getFacAcctCode()); // 帳務科目
			updByLoanBorMain(bizTbsdy, tCollList, titaVo);
			if (tCollList.getStatus() == 2 || tCollList.getStatus() == 6 || tCollList.getStatus() == 7)
				updByLoanOverdue(bizTbsdy, tCollList, titaVo);
			try {
				collListService.update(tCollList, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "AcCollListCom 法催紀錄清單檔檔更新 " + e.getErrorMsg());
			}

		}
	}

	private void updByLoanBorMain(int bizTbsdy, CollList tCollList, TitaVo titaVo) throws LogicException {
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(tCollList.getCustNo(), tCollList.getFacmNo(),
				tCollList.getFacmNo(), 1, 900, this.index, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain != null) {
			// 戶況
			int status = facStatusCom.settingStatus(lLoanBorMain, bizTbsdy);
			int nextIntDate = 0;
			int prevIntDate = 0;
			int ovduTerm = 0;
			int ovduDays = 0;
			int specificDd = 0;
			BigDecimal prinBalance = BigDecimal.ZERO;
			// 最小的應繳息日、繳息迄日
			for (LoanBorMain ln : lLoanBorMain) {
				if (ln.getStatus() == 0 || ln.getStatus() == 2 || ln.getStatus() == 7) {
					prinBalance = prinBalance.add(ln.getLoanBal()); // 本金餘額
					if (ln.getNextPayIntDate() < nextIntDate || nextIntDate == 0) {
						nextIntDate = ln.getNextPayIntDate();
						prevIntDate = ln.getPrevPayIntDate();
						specificDd = ln.getSpecificDd();
					}
				}
			}
			// 逾期期數、逾期天數
			if (nextIntDate > 0 && nextIntDate < bizTbsdy) {
				dDateUtil.init();
				dDateUtil.setDate_1(nextIntDate);
				dDateUtil.setDate_2(bizTbsdy);
				dDateUtil.dateDiff();
				ovduDays = dDateUtil.getDays();
				ovduTerm = loanCom.getOvduTerms(nextIntDate, bizTbsdy, specificDd);
			}
			tCollList.setStatus(status); // 戶況
			tCollList.setNextIntDate(nextIntDate); // 應繳息日
			tCollList.setPrevIntDate(prevIntDate); // 繳息迄日
			tCollList.setOvduTerm(ovduTerm); // 逾期期數
			tCollList.setOvduDays(ovduDays); // 逾期天數
			tCollList.setPrinBalance(prinBalance); // 本金餘額
		}
	}

	private void updByLoanOverdue(int bizTbsdy, CollList tCollList, TitaVo titaVo) throws LogicException {
		Integer statuss[] = { 1, 2, 3 }; // 1: 催收 2. 部分轉呆 3: 呆帳 4: 催收回復 5.催收收回
		List<Integer> lStatus = Arrays.asList(statuss);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(tCollList.getCustNo(),
				tCollList.getFacmNo(), tCollList.getFacmNo(), 1, 999, 1, 999, lStatus, this.index, Integer.MAX_VALUE,
				titaVo);
		List<LoanOverdue> lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();
		if (lLoanOverdue != null) {
			for (LoanOverdue tLoanOverdue : lLoanOverdue) {
				tCollList.setPrinBalance(tCollList.getPrinBalance().add(tLoanOverdue.getOvduBal())); // 催收餘額
				tCollList.setBadDebtBal(tCollList.getBadDebtBal().add(tLoanOverdue.getBadDebtBal())); // 呆帳餘額
				tCollList.setAcctCode(tLoanOverdue.getAcctCode()); // 帳務科目
			}
		}
	}
}
