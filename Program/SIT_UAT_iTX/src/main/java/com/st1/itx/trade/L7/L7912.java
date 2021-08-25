package com.st1.itx.trade.L7;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L7912")
@Scope("prototype")
/**
 * eloan額度查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L7912 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil iDateUtil;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	public ClFacService clFacService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		this.info("active L7912 ");
		this.totaVo.init(titaVo);

		String iCustId = titaVo.getParam("CustId");
		int iAcDate = titaVo.getEntDyI();

		// 會計日前一月
		iDateUtil.init();
		iDateUtil.setDate_1(iAcDate);
		iDateUtil.setMons(-1);
		int iAcDate_1M = iDateUtil.getCalenderDay();

		// 會計日前半年
		iDateUtil.init();
		iDateUtil.setDate_1(iAcDate);
		iDateUtil.setMons(-6);
		int iAcDate_6M = iDateUtil.getCalenderDay();

		// 會計日前1年
		iDateUtil.init();
		iDateUtil.setDate_1(iAcDate);
		iDateUtil.setYears(-1);
		int iAcDate_1Y = iDateUtil.getCalenderDay();

		// 會計日前7天
		iDateUtil.init();
		iDateUtil.setDate_1(iAcDate);
		iDateUtil.setDays(-7);
		int iAcDate_7D = iDateUtil.getCalenderDay();

		int iCustNo = 0;
		CustMain tCustMain = new CustMain();

		// 抓取戶號
		tCustMain = custMainService.custIdFirst(iCustId, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "此身分證/統一編號不存在");
		} else {
			iCustNo = custMainService.custIdFirst(iCustId, titaVo).getCustNo();
		}
		Slice<FacMain> slFacMain = facMainService.facmCustNoRange(iCustNo, iCustNo, 1, 999, 0, Integer.MAX_VALUE, titaVo);
		List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
		if (lFacMain == null) {
			throw new LogicException(titaVo, "E0001", "額度不存在");
		}
		for (FacMain fac : lFacMain) {
			TotaVo totaVo2 = new TotaVo();
			totaVo2.init(titaVo);

			int wkMaturityDate = 0;
			// 半年內曾滯繳一期以上
			int wkOverdue1MFlag = 0;
			// 近一年有二期以上延遲逾7日（含）繳款
			int wkOverdue7DFlag = 0;
			int wkOverdue7DCnt = 0;
			int wkDueDate = 0;

			Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, fac.getFacmNo(), fac.getFacmNo(), 1, 900, 0, Integer.MAX_VALUE, titaVo);
			List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
			if (lLoanBorMain != null) {
				for (LoanBorMain ln : lLoanBorMain) {
					if (ln.getStatus() == 0) {
						if (ln.getMaturityDate() > wkMaturityDate) {
							wkMaturityDate = ln.getMaturityDate();
						}
						if (ln.getNextPayIntDate() <= iAcDate_1M) {
							wkOverdue1MFlag = 1;
						}
						if (ln.getNextPayIntDate() <= iAcDate_7D) {
							wkOverdue7DCnt = 1;
						}
					}
				}
			}
			this.info("fac acdate " + wkOverdue7DCnt + ":" + fac.getFacmNo() + ";" + wkOverdue1MFlag);
			Slice<LoanBorTx> slLoanBorTx = loanBorTxService.findDueDateRange(iCustNo, fac.getFacmNo(), fac.getFacmNo(), 1, 900, iAcDate_1Y + 19110000, iAcDate + 19110000, 0, Integer.MAX_VALUE,
					titaVo);
			List<LoanBorTx> lLoanBorTx = slLoanBorTx == null ? null : slLoanBorTx.getContent();
			if (lLoanBorTx != null) {
				for (LoanBorTx tx : lLoanBorTx) {
					// 正常交易、計息交易、應繳日小於入帳日、不同應繳日
					if (tx.getTitaHCode().equals("0") && tx.getIntStartDate() > 0 && tx.getDueDate() < tx.getEntryDate() && wkDueDate != tx.getDueDate()) {
						wkDueDate = tx.getDueDate();
						iDateUtil.setDate_1(tx.getDueDate());
						iDateUtil.setDate_2(tx.getEntryDate());
						iDateUtil.dateDiffSp();
						if (tx.getDueDate() >= iAcDate_6M && iDateUtil.getMons() >= 1) {
							wkOverdue1MFlag = 1;
						}
						if (tx.getDueDate() >= iAcDate_1Y && iDateUtil.getDays() >= 7) {
							wkOverdue7DCnt++;
						}
						this.info("dateUtil mon = " + iDateUtil.getMons());
						this.info("dateUtil day = " + iDateUtil.getDays());
					}
				}

				this.info("fac acdate " + wkOverdue7DCnt + ":" + fac.getFacmNo() + ";" + wkOverdue1MFlag);
			}
			if (wkOverdue7DCnt >= 2) {
				wkOverdue7DFlag = 1;
			}

			this.totaVo.init(titaVo);
			totaVo2.putParam("OCustNo", fac.getCustNo());
			totaVo2.putParam("OFacmNo", fac.getFacmNo());
			totaVo2.putParam("OApplNo", fac.getApplNo());
			totaVo2.putParam("OAcctCode", fac.getAcctCode());
			totaVo2.putParam("OLineAmt", fac.getLineAmt());
			totaVo2.putParam("OLoanTermYy", fac.getLoanTermYy());
			totaVo2.putParam("OLoanTermMm", fac.getLoanTermMm());
			totaVo2.putParam("OLoanTermDd", fac.getLoanTermDd());
			if (fac.getUtilDeadline() == 0) {
				totaVo2.putParam("OUtilDeadline", "");
			} else {
				totaVo2.putParam("OUtilDeadline", Integer.valueOf(fac.getUtilDeadline()));
			}
			if (fac.getMaturityDate() == 0) {
				totaVo2.putParam("OFacMaturityDate", "");
			} else {
				totaVo2.putParam("OFacMaturityDate", Integer.valueOf(fac.getMaturityDate()));
			}
			totaVo2.putParam("OBaseRateCode", fac.getBaseRateCode());
			totaVo2.putParam("ORecycleCode", fac.getRecycleCode());
			if (fac.getRecycleDeadline() == 0) {
				totaVo2.putParam("ORecycleDeadline", "");
			} else {
				totaVo2.putParam("ORecycleDeadline", Integer.valueOf(fac.getRecycleDeadline()));
			}
			totaVo2.putParam("ORateIncr", fac.getRateIncr());
			totaVo2.putParam("OLoanBal", fac.getUtilAmt());
			totaVo2.putParam("OApproveRate", fac.getApproveRate());
			if (fac.getFirstDrawdownDate() == 0) {
				totaVo2.putParam("OFirstDrawdownDate", "");
			} else {
				totaVo2.putParam("OFirstDrawdownDate", Integer.valueOf(fac.getFirstDrawdownDate()));
			}
			if (wkMaturityDate == 0) {
				totaVo2.putParam("OMaturityDate", "");
			} else {
				totaVo2.putParam("OMaturityDate", Integer.valueOf(wkMaturityDate));
			}
			if (fac.getRecycleCode().equals("1")) {
				totaVo2.putParam("ORecycleCode1", "2");
			} else {
				totaVo2.putParam("ORecycleCode1", "1");
			}
			totaVo2.putParam("OUtilBal", fac.getUtilBal());
			totaVo2.putParam("OCurrencyCode", fac.getCurrencyCode());
			totaVo2.putParam("OFireOfficer", fac.getFireOfficer());
			totaVo2.putParam("OOverdue1MFlag", wkOverdue1MFlag);
			totaVo2.putParam("OOverdue7DFlag", wkOverdue7DFlag);

			Slice<ClFac> slClFac = clFacService.facmNoEq(iCustNo, fac.getFacmNo(), 0, Integer.MAX_VALUE, titaVo);
			List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();
			if (lClFac != null) {
				for (ClFac cl : lClFac) {
					OccursList occursList = new OccursList();
					this.info("cl" + cl);
					occursList.putParam("OOClCode1", cl.getClCode1());
					occursList.putParam("OOClCode2", cl.getClCode2());
					occursList.putParam("OOClNo", cl.getClNo());
					totaVo2.addOccursList(occursList);
				}
			}
			this.addList(totaVo2);
		}

		return this.sendList();
	}
}