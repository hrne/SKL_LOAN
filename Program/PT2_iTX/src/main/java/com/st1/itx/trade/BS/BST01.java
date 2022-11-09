package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 日始作業 <br>
 * 執行時機：patch DueAmt
 * 
 * @author Lai
 * @version 1.0.0
 */

@Component("BST01")
@Scope("prototype")
public class BST01 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public CdBaseRateService cdBaseRateService;
	@Autowired
	public FacProdService facProdService;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	LoanCom loanCom;

	@Autowired
	DateUtil dDateUtil;

	private FacProd tFacProd;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanRateChange tLoanRateChange;
	private LoanRateChangeId tLoanRateChangeId;
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private List<FacProdStepRate> lFacProdStepRate = new ArrayList<FacProdStepRate>();
	private TitaVo titaVo = new TitaVo();
	private int iAcdate = 0;
	private String iUpdate = "";

	// work area
	private int wkBormNo = 0;
	private String sProdStepNo = "";
	int wkStartDate = 0;
	int wkEffectDate = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BST01 ......");
		// 維護利率變動檔
		this.titaVo = titaVo;
		loanCom.setTxBuffer(this.txBuffer);
		// Parm = 20220407,Y/N
		String[] strAr = titaVo.getParam("Parm").split(",");
		iAcdate = this.parse.stringToInteger(strAr[0]);
		iUpdate = strAr[1]; // 是否更新
		List<String> lTitaHCode = new ArrayList<String>();
		lTitaHCode.add("0");
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.findAcDateRange(iAcdate, iAcdate, lTitaHCode, 0,
				Integer.MAX_VALUE, titaVo);

		for (LoanBorTx ln : slLoanBorTx.getContent()) {
			if ("3203".equals(ln.getTxDescCode()) || "部分償還本金".equals(ln.getDesc())) {
				LoanBorMain tLoanBorMain = loanBorMainService
						.holdById(new LoanBorMainId(ln.getCustNo(), ln.getFacmNo(), ln.getBormNo()), titaVo);
				if ("3".equals(tLoanBorMain.getAmortizedCode()) || "4".equals(tLoanBorMain.getAmortizedCode())) {
					int wkGracePeriod = loanCom.getGracePeriod(tLoanBorMain.getAmortizedCode(),
							tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(), tLoanBorMain.getSpecificDate(),
							tLoanBorMain.getSpecificDd(), tLoanBorMain.getGraceDate());
					int wkPaidTerms = 0;
					if (tLoanBorMain.getPrevPayIntDate() > tLoanBorMain.getSpecificDate()) {
						wkPaidTerms = loanCom.getGracePeriod(tLoanBorMain.getAmortizedCode(),
								tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
								tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
								tLoanBorMain.getPrevPayIntDate());
					}
					// 剩餘還本期數
					int wkDueTerms = wkPaidTerms > wkGracePeriod ? tLoanBorMain.getTotalPeriod() - wkPaidTerms
							: tLoanBorMain.getTotalPeriod() - wkGracePeriod;
					// 重算期金
					BigDecimal wkNewDueAmt = loanDueAmtCom.getDueAmt(tLoanBorMain.getLoanBal(),
							tLoanBorMain.getStoreRate(), tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
							wkDueTerms, 0, tLoanBorMain.getPayIntFreq(), tLoanBorMain.getFinalBal(), titaVo);
					this.info("BST01 ID=" + tLoanBorMain.getLoanBorMainId() + ", NewDueAmt=" + wkNewDueAmt + "/"
							+ tLoanBorMain.getDueAmt() + ", PaidTerms=" + wkPaidTerms + "/"
							+ tLoanBorMain.getPaidTerms());

					if ("Y".equals(iUpdate)) {
						tLoanBorMain.setDueAmt(wkNewDueAmt);
						tLoanBorMain.setPaidTerms(wkPaidTerms);
						try {
							loanBorMainService.update(tLoanBorMain, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", ""); // 更新資料時，發生錯誤
						}
					}
				}
			}
		}

		// commitEnd
		this.batchTransaction.commit();

		/*---------- Step 2. 系統換日過帳(含年初損益類結轉) ----------*/

		// end
		return null;
	}

}