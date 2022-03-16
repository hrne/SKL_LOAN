package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanSynd;
import com.st1.itx.db.domain.LoanSyndId;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.LoanSyndItemService;
import com.st1.itx.db.service.LoanSyndService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.parse.Parse;
/*
 * L2600 聯貸案訂約登錄
 * a.若聯貸案訂約之ID不等於動撥時之ID或訂約為母公司，但為子公司動撥時，請輸入聯貸動撥之子公司。
 */

/*
 * Tita
 * FuncCode=9,1
 * CustId=X,10
 * CustNo=9,7
 * SyndNo=9,3
 * LeadIngBank=X,7
 * SigningDate=9,7
 * DrawdownStartDate=9,7
 * DrawdownEndDate=9,7
 * CommitFeeFlag=X,1
 * CurrencyCode=X,3
 * TimSyndAmt=9,14.2
 * imPartAmt=9,14.2
 * AgentBank=X,7
 * CentralBankPercent=9,3
 * MasterCustId=X,10
 * SubCustId1=X,10
 * SubCustId2=X,10
 * SubCustId3=X,10
 * SubCustId4=X,10
 * SubCustId5=X,10
 * SubCustId6=X,10
 */
/**
 * L2600 聯貸案訂約登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2600")
@Scope("prototype")
public class L2600 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdGseqService cdGseqService;
	@Autowired
	public LoanSyndService loanSyndService;
	@Autowired
	public LoanSyndItemService loanSyndItemService;
	@Autowired
	public FacCaseApplService facCaseApplService;
	// 銷帳處理
	@Autowired
	public AcReceivableCom acReceivableCom;

	@Autowired
	Parse parse;
	@Autowired
	GSeqCom gGSeqCom;

	private TitaVo titaVo = new TitaVo();
	private int iFuncCode;
	private int iSyndNo;
	private String iSyndTypeCode;
	private String iSyndName;
	private String iLeadingBank;
	private String iAgentBank;
	private int iSigningDate;
	private String iCurrencyCode;
	private BigDecimal iSyndAmt;
	private BigDecimal iPartAmt;
	private BigDecimal iPartRate;

	// work area
	private int wkSyndNo = 0;
	private int wkTbsDy;
	private TempVo tTempVo = new TempVo();
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanSynd tLoanSynd = new LoanSynd();
	private LoanSyndId tLoanSyndId = new LoanSyndId();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2600 ");

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;

		wkTbsDy = this.txBuffer.getTxCom().getTbsdy();

		// 取得輸入資料
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode")); // 功能
		iSyndNo = this.parse.stringToInteger(titaVo.getParam("SyndNo")); // 聯貸案編號
		iSyndName = titaVo.getParam("SyndName"); // 國內或國際聯貸
		iSyndTypeCode = titaVo.getParam("SyndTypeCode"); // 國內或國際聯貸
		iLeadingBank = titaVo.getParam("LeadingBank"); // 主辦行
		iAgentBank = titaVo.getParam("AgentBank"); // 擔保品管理行
		iSigningDate = this.parse.stringToInteger(titaVo.getParam("SigningDate")); // 簽約日
		iPartRate = this.parse.stringToBigDecimal(titaVo.getParam("PartRate")); // 參貸費率
		iCurrencyCode = titaVo.getParam("CurrencyCode"); // 幣別
		iSyndAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimSyndAmt")); // 聯貸總金額
		iPartAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimPartAmt")); // 參貸金額

		wkSyndNo = iSyndNo;

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "功能 = " + iFuncCode); // 功能選擇錯誤
		}

		// 更新聯貸案訂約檔
		switch (iFuncCode) {
		case 1: // 新增
		case 3: // 拷貝
			GetSyndNoRoutine(); // 取的聯貸案序號
			tLoanSyndId.setSyndNo(wkSyndNo);
			tLoanSynd.setSyndNo(wkSyndNo);

			moveLoanSynd();
			try {
				loanSyndService.insert(tLoanSynd);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				}
			}
			break;
		case 2: // 修改
			tLoanSynd = loanSyndService.holdById(iSyndNo);
			if (tLoanSynd == null) {
				throw new LogicException(titaVo, "E0006", "聯貸訂約檔  聯貸案序號 = " + iSyndNo); // 鎖定資料時，發生錯誤
			}
			moveLoanSynd();
			try {
				loanSyndService.update(tLoanSynd);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0003", e.getErrorMsg()); // 修改資料不存在
			}
			break;
		case 4: // 刪除
			tLoanSynd = loanSyndService.holdById(iSyndNo);
			if (tLoanSynd == null) {
				throw new LogicException(titaVo, "E0006", "聯貸訂約檔  聯貸案序號 = " + iSyndNo); // 鎖定資料時，發生錯誤
			}
			// 刪除時 有案件申請使用時error
			Slice<FacCaseAppl> slFacCaseAppl = facCaseApplService.syndNoEq(iSyndNo, 0, 1, titaVo);
			if (slFacCaseAppl != null) {
				throw new LogicException(titaVo, "E0008", "案件申請檔有使用此聯貸案編號  聯貸案編號 =  " + iSyndNo); // 刪除資料時，發生錯誤
			}
			try {
				loanSyndService.delete(tLoanSynd);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0004", e.getErrorMsg()); // 刪除資料不存在
			}
			break;
		case 5: // 查詢
			break;
		}

		this.totaVo.putParam("SyndNo", wkSyndNo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 取的聯貸案序號
	private void GetSyndNoRoutine() throws LogicException {
		this.info("   GetSyndNoRoutine ...");

		int WkTbsYy = this.txBuffer.getTxCom().getTbsdy() / 10000;
		// 新增時由電腦產生,營業日之民國年(3位)+3位之流水號
		if (wkSyndNo == 0) {
			wkSyndNo = gGSeqCom.getSeqNo(0, 1, "L2", "0003", 999, titaVo);
			wkSyndNo = (WkTbsYy % 1000) * 1000 + wkSyndNo;
		}

		this.info("   SyndNo  =  " + wkSyndNo);

	}

	private void moveLoanSynd() throws LogicException {
		tLoanSynd.setSyndTypeCodeFlag(iSyndTypeCode);
		tLoanSynd.setSyndName(iSyndName);
		tLoanSynd.setLeadingBank(iLeadingBank);
		tLoanSynd.setSigningDate(iSigningDate);
		tLoanSynd.setPartRate(iPartRate);
		tLoanSynd.setCurrencyCode(iCurrencyCode);
		tLoanSynd.setSyndAmt(iSyndAmt);
		tLoanSynd.setPartAmt(iPartAmt);
		tLoanSynd.setAgentBank(iAgentBank);
	}

}