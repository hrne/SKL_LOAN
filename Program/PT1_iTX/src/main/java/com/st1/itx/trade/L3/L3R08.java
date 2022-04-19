package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.parse.Parse;

/**
 * L3R08 查詢應繳日變更的利息
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R08")
@Scope("prototype")
public class L3R08 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public TxTempService txTempService;
	@Autowired
	public LoanIntDetailService loanIntDetailService;
	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcPaymentCom acPaymentCom;

	private int iFKey;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iNewSpecificDate;
	private String iTxCode = "";

	// work area
	private int wkTbsDy;
	private int wkFacmNoStart = 1;
	private int wkFacmNoEnd = 999;
	private int wkBormNoStart = 1;
	private int wkBormNoEnd = 900;
	private int wkTotaCount = 0;
	private int wkLoanPrevIntDate = 0;
	private BigDecimal oTotalInterest = BigDecimal.ZERO;
	private List<LoanBorMain> lLoanBorMain;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R08 ");
		this.totaVo.init(titaVo);
		loanCom.setTxBuffer(this.txBuffer);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);

		wkTbsDy = this.txBuffer.getTxCom().getTbsdy();

		// 取得輸入資料
		iTxCode = titaVo.getParam("RimTxCode");
		iFKey = this.parse.stringToInteger(titaVo.getParam("RimFKey"));
		iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		iNewSpecificDate = this.parse.stringToInteger(titaVo.getParam("RimSpecificDate"));

		this.info("   iCustNo =  " + iCustNo);
		this.info("   iFacmNo =  " + iFacmNo);
		this.info("   iBormNo =  " + iBormNo);
		this.info("   iNewSpecificDate =  " + iNewSpecificDate);

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", " 放款主檔"); // 查詢資料不存在
		}
		for (LoanBorMain ln : lLoanBorMain) {
			wkLoanPrevIntDate = ln.getPrevPayIntDate() == 0 ? ln.getDrawdownDate() : ln.getPrevPayIntDate();
			if (ln.getStatus() != 0) {
				if (iFacmNo > 0 && iBormNo > 0) {
					throw new LogicException(titaVo, "E3063", ""); // 該筆放款戶況非正常戶
				} else {
					continue;
				}
			}
			if (ln.getActFg() == 1 && iFKey == 0) {
				throw new LogicException(titaVo, "E0021", " 放款主檔 戶號 = " + ln.getCustNo() + " 額度編號 =  " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆資料待放行中
			}
			if (ln.getNextPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
				throw new LogicException(titaVo, "E3062", " 應繳息日 = " + ln.getNextPayIntDate()); // 有1期(含)以上期款未繳,不可變更繳款日
			}
			// 應收息迄日iNewSpecificDate>上次繳息日且小於下次繳息日
			if (iNewSpecificDate < wkLoanPrevIntDate || iNewSpecificDate >= ln.getNextPayIntDate()) {
				throw new LogicException(titaVo, "E3075", ln.getCustNo() + "-" + ln.getFacmNo() + "-" + ln.getBormNo() + " 上次繳息迄日 = " + ln.getPrevPayIntDate() + " 下次應繳息日 = " + ln.getNextPayIntDate()); // 變更應繳日需落在上次繳息迄日與下次應繳息日之內
			}
			// 計息
			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iNewSpecificDate, 1, wkTbsDy, titaVo);
			loanCalcRepayIntCom.getRepayInt(titaVo);
			loanCalcRepayIntCom.getInterest();
			oTotalInterest = oTotalInterest.add(loanCalcRepayIntCom.getInterest());
			wkTotaCount++;
		}
		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3076", ""); // 查無可變更應繳日的資料
		}

		this.totaVo.putParam("L3r08Interest", oTotalInterest);

		this.addList(this.totaVo);
		return this.sendList();
	}
}