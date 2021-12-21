package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
/*
 * L3923 應繳日試算
 * a.此功能供欲變更應繳日時,試算其應補繳之利息.
 * b.如撥款序號未輸入時,擇該額度下每一筆撥款均列入變更試算;如額度編號未輸入時,擇該戶號下每一筆撥款均列入試算.
 * c.有1期(含)以上期款未繳,不可變更繳款日
 * d.變更應繳日需落在上次繳息迄日與下次應繳息日之內
 */
/*
 * Tita
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * NewSpecificDd=9,2
 * NewSpecificDate=9,7
 * EntryDate=9,7
 * CurrencyCode=X,3
 */

/**
 * L3923 應繳日試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3923")
@Scope("prototype")
public class L3923 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3923.class);

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	BaTxCom baTxCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3923 ");
		this.totaVo.init(titaVo);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iNewPayIntDate = this.parse.stringToInteger(titaVo.getParam("NewSpecificDate"));
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		int iNewSpecificDd = this.parse.stringToInteger(titaVo.getParam("NewSpecificDd"));
		this.info("   iNewPayIntDate(NewSpecificDate)= " + iNewPayIntDate);
		this.info("   wkNewSpecificDd= " + iNewSpecificDd);

		// work area
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
		ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		int wkTotaCount = 0;
		BigDecimal oLoanBal = BigDecimal.ZERO;
		int oIntStartDate = 9991231;
		int oIntEndDate = 0;
		BigDecimal oRate = BigDecimal.ZERO;
		BigDecimal oPrincipal = BigDecimal.ZERO;
		BigDecimal oInterest = BigDecimal.ZERO;
		BigDecimal oDelayInt = BigDecimal.ZERO;
		BigDecimal oBreachAmt = BigDecimal.ZERO;
		int wkNewSpecificDate = 0;
		int wkNewRepaidDate = 0;
		int wkTermNo = 0;
		int wkPayIntDate = 0;
		int wkNextPayIntDate = 0;
		int wkNextRepayDate = 0;
		int wkMons = 0;
		int wkRestPeriod = 0;
		int wkGracePeriod = 0;
		BigDecimal wkDueAmt = BigDecimal.ZERO;

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}
		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 0, BigDecimal.ZERO, titaVo); // 00-費用全部(已到期);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 400; // 100 + 122 * 500 = 48900

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart, wkBormNoEnd, this.index, this.limit, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		for (LoanBorMain ln : lLoanBorMain) {
			if (ln.getStatus() != 0) {
				continue;
			}
//			if (ln.getNextPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
//				continue;
//			}

			// 新指定基準日期
			wkNewSpecificDate = loanCom.getSpecificDate(iNewSpecificDd, ln.getFirstDueDate(), ln.getPayIntFreq());

			// 入帳日大於上次繳息日則繳至入帳日的原應繳日
			wkPayIntDate = ln.getPrevPayIntDate() == 0 ? ln.getSpecificDate() : ln.getPrevPayIntDate();

			if (iEntryDate > wkPayIntDate) {
				wkTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), iEntryDate);
				if (wkTermNo > 0) {
					wkPayIntDate = loanCom.getPayIntEndDate(ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), wkTermNo, ln.getMaturityDate());
				}
			}

			// 以新指定基準日期計算至應繳日的期數
			wkNextPayIntDate = wkNewSpecificDate;
			wkTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), wkNewSpecificDate, iNewSpecificDd, wkPayIntDate);
			if (wkTermNo > 0) {
				wkNextPayIntDate = loanCom.getPayIntEndDate(ln.getFreqBase(), ln.getPayIntFreq(), wkNewSpecificDate, iNewSpecificDd, wkTermNo, ln.getMaturityDate());
			}
			// 如新應繳日 < 原應繳日，則期數 + 1;
			if (wkNextPayIntDate < wkPayIntDate) {
				wkNextPayIntDate = loanCom.getPayIntEndDate(ln.getFreqBase(), ln.getPayIntFreq(), wkNewSpecificDate, iNewSpecificDd, wkTermNo + 1, ln.getMaturityDate());
			}
			// 計算應繳金額
			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, wkNextPayIntDate, 1, iEntryDate, titaVo);
			lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);

			// 輸出
			oLoanBal = oLoanBal.add(loanCalcRepayIntCom.getLoanBal());
			oRate = loanCalcRepayIntCom.getStoreRate();
			oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
			oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
			oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
			oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());

			// 重算期金(log only)
			wkRestPeriod = loanCom.getTermNo(1, ln.getFreqBase(), ln.getRepayFreq(), wkNextPayIntDate, iNewSpecificDd, ln.getMaturityDate());

			//
			wkGracePeriod = ln.getRepaidPeriod() > 0 ? 0 : ln.getGracePeriod();
			wkDueAmt = loanDueAmtCom.getDueAmt(loanCalcRepayIntCom.getLoanBal(), loanCalcRepayIntCom.getStoreRate(), ln.getAmortizedCode(), ln.getFreqBase(), wkRestPeriod, wkGracePeriod,
					ln.getPayIntFreq(), ln.getFinalBal(), titaVo);
			this.info("   wkOldSpecificDate = " + ln.getSpecificDate());
			this.info("   wkNewSpecificDate = " + wkNewSpecificDate);
			this.info("   wknewPayIntDate = " + iNewPayIntDate);
			this.info("   wkNextPayIntDate = " + wkNextPayIntDate);
			this.info("   wkNewRepaidDate = " + wkNewRepaidDate);
			this.info("   wkNextRepayDate  = " + wkNextRepayDate);
			this.info("   wkMons        = " + wkMons);
			this.info("   wkRestPeriod = " + wkRestPeriod);
			this.info("   wkDueAmt      = " + wkDueAmt);
			for (CalcRepayIntVo c : lCalcRepayIntVo) {
				OccursList occursList = new OccursList();
				oIntStartDate = c.getStartDate() < oIntStartDate ? c.getStartDate() : oIntStartDate;
				oIntEndDate = c.getEndDate() > oIntEndDate ? c.getEndDate() : oIntEndDate;
				occursList.putParam("OOFacmNo", ln.getFacmNo());
				occursList.putParam("OOBormNo", ln.getBormNo());
				occursList.putParam("OOIntStartDate", c.getStartDate());
				occursList.putParam("OOIntEndDate", c.getEndDate());
				occursList.putParam("OOAmount", c.getAmount());
				occursList.putParam("OORate", c.getStoreRate());
				occursList.putParam("OOPrincipal", c.getPrincipal());
				occursList.putParam("OOInterest", c.getInterest());
				occursList.putParam("OODelayInt", c.getDelayInt());
				occursList.putParam("OOBreachAmt", c.getBreachAmt());
				occursList.putParam("OOLoanBal", c.getLoanBal());
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
				wkTotaCount++;
			}
		}
		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3070", ""); // 查無可計息的放款資料
		}
		this.totaVo.putParam("OLoanBal", oLoanBal);
		this.totaVo.putParam("OIntStartDate", oIntStartDate == 9991231 ? 0 : oIntStartDate);
		this.totaVo.putParam("OIntEndDate", oIntEndDate);
		this.totaVo.putParam("ORate", oRate);
		this.totaVo.putParam("OInterest", oInterest);
		this.totaVo.putParam("ODelayInt", oDelayInt);
		this.totaVo.putParam("OShortfall", baTxCom.getShortfall());
		this.totaVo.putParam("OShortfallInt", baTxCom.getShortfallInterest());
		this.totaVo.putParam("OShortfallPrin", baTxCom.getShortfallPrincipal());
		this.totaVo.putParam("OShortCloseBreach", baTxCom.getShortCloseBreach());
		this.totaVo.putParam("OExcessive", baTxCom.getExcessive());
		this.totaVo.putParam("OTempTax", baTxCom.getTempTax());

		this.addList(this.totaVo);
		return this.sendList();
	}
}