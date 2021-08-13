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

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanSynd;
import com.st1.itx.db.domain.LoanSyndId;
import com.st1.itx.db.domain.LoanSyndItem;
import com.st1.itx.db.domain.LoanSyndItemId;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanSyndItemService;
import com.st1.itx.db.service.LoanSyndService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;
/*
 * L3600 聯貸案訂約登錄
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
 * L3600 聯貸案訂約登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3600")
@Scope("prototype")
public class L3600 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3600.class);

	/* DB服務注入 */
	@Autowired
	public CdGseqService cdGseqService;
	@Autowired
	public TxTempService txTempService;
	@Autowired
	public LoanSyndService loanSyndService;
	@Autowired
	public LoanSyndItemService loanSyndItemService;

	@Autowired
	public CustMainService custMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	GSeqCom gGSeqCom;

	private TitaVo titaVo = new TitaVo();
	private int iFuncCode;
	private String iCustId;
	private int iCustNo;
	private int iSyndNo;
	private String iGuaId;
	private String iLeadingBank;
	private int iSigningDate;
	private int iDrawdownStartDate;
	private int iDrawdownEndDate;
	private String iCommitFeeFlag;
	private String iCurrencyCode;
	private BigDecimal iSyndAmt;
	private BigDecimal iPartAmt;
	private String iAgentBank;
	private int iCentralBankPercent;
	private String iMasterCustId;
	private String iSubCustId1;
	private String iSubCustId2;
	private String iSubCustId3;
	private String iSubCustId4;
	private String iSubCustId5;
	private String iSubCustId6;
	private BigDecimal iPartRate;
	private int iCreditPeriod;

	// work area
	private int wkCustNo = 0;
	private int wkSyndNo = 0;
	private int wkBorxNo = 1;
	private int wkTbsDy;
	private CustMain tCustMain;
	// private TxTemp tTxTemp;
	// private TxTempId tTxTempId;
	private TempVo tTempVo = new TempVo();
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanSynd tLoanSynd = new LoanSynd();
	private LoanSyndId tLoanSyndId = new LoanSyndId();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3600 ");

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		loanCom.setTxBuffer(this.txBuffer);

		wkTbsDy = this.txBuffer.getTxCom().getTbsdy();

		// 取得輸入資料
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		iCustId = titaVo.getParam("CustId");
		iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		iSyndNo = this.parse.stringToInteger(titaVo.getParam("SyndNo"));
		iGuaId = titaVo.getParam("GuaId");
		iLeadingBank = titaVo.getParam("LeadingBank");
		iSigningDate = this.parse.stringToInteger(titaVo.getParam("SigningDate"));
		iDrawdownStartDate = this.parse.stringToInteger(titaVo.getParam("DrawdownStartDate"));
		iDrawdownEndDate = this.parse.stringToInteger(titaVo.getParam("DrawdownEndDate"));
		iCommitFeeFlag = titaVo.getParam("CommitFeeFlag");
		iPartRate = this.parse.stringToBigDecimal(titaVo.getParam("PartRate"));
		iCurrencyCode = titaVo.getParam("CurrencyCode");
		iSyndAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimSyndAmt"));
		iPartAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimPartAmt"));
		iAgentBank = titaVo.getParam("AgentBank");
		iCreditPeriod = this.parse.stringToInteger(titaVo.getParam("CreditPeriod"));
		// iGuaId
		// iPartRate
		// iCreditPeriod

		iCentralBankPercent = this.parse.stringToInteger(titaVo.getParam("CentralBankPercent"));
		iMasterCustId = "";
		iSubCustId1 = "";
		iSubCustId2 = "";
		iSubCustId3 = "";
		iSubCustId4 = "";
		iSubCustId5 = "";
		iSubCustId6 = "";

		wkCustNo = iCustNo;
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
			tLoanSyndId.setCustNo(wkCustNo);
			tLoanSyndId.setSyndNo(wkSyndNo);
			tLoanSynd.setCustNo(wkCustNo);
			tLoanSynd.setSyndNo(wkSyndNo);
			tLoanSynd.setLoanSyndId(tLoanSyndId);
			wkBorxNo = 1;
			moveLoanSynd();
			try {
				loanSyndService.insert(tLoanSynd);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				}
			}
			// 新增放款交易內容檔
			addLoanBorTxRoutine();
			break;
		case 2: // 修改 商品參數生效,只允許修改商品狀態、商品截止日期
			tLoanSynd = loanSyndService.holdById(new LoanSyndId(iCustNo, iSyndNo));
			if (tLoanSynd == null) {
				throw new LogicException(titaVo, "E0006", "聯貸訂約檔 戶號 = " + iCustNo + " 聯貸案序號 = " + iSyndNo); // 鎖定資料時，發生錯誤
			}
			wkBorxNo = tLoanSynd.getLastBorxNo() + 1;
			moveLoanSynd();
			try {
				loanSyndService.update(tLoanSynd);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0003", e.getErrorMsg()); // 修改資料不存在
			}
			// 新增放款交易內容檔
			addLoanBorTxRoutine();
			break;
		case 4: // 刪除
			tLoanSynd = loanSyndService.holdById(new LoanSyndId(iCustNo, iSyndNo));
			if (tLoanSynd == null) {
				throw new LogicException(titaVo, "E0006", "聯貸訂約檔 戶號 = " + iCustNo + " 聯貸案序號 = " + iSyndNo); // 鎖定資料時，發生錯誤
			}
			wkBorxNo = tLoanSynd.getLastBorxNo() + 1;
			// 新增放款交易內容檔
			addLoanBorTxRoutine();
			try {
				loanSyndService.delete(tLoanSynd);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0004", e.getErrorMsg()); // 刪除資料不存在
			}
			break;
		case 5: // 查詢
			break;
		}

		this.totaVo.putParam("CustNo", wkCustNo);
		this.totaVo.putParam("SyndNo", wkSyndNo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 取的聯貸案序號
	private void GetSyndNoRoutine() throws LogicException {
		this.info("   GetSyndNoRoutine ...");

		tCustMain = custMainService.holdById(loanCom.getCustUKey(iCustId, titaVo));
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0006", "客戶資料主檔 身份證字號/統一編號 = " + iCustId); // 鎖定資料時，發生錯誤
		}
		if (tCustMain.getCustNo() == 0) {
			wkCustNo = gGSeqCom.getSeqNo(0, 0, "L2", "0001", 9999999, titaVo);
			tCustMain.setCustNo(wkCustNo);
		}
		wkSyndNo = tCustMain.getLastSyndNo() + 1;
		tCustMain.setLastSyndNo(wkSyndNo);
		try {
			custMainService.update(tCustMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "客戶資料主檔 身份證字號/統一編號 = " + iCustId); // 更新資料時，發生錯誤
		}
	}

	private void moveLoanSynd() throws LogicException {
		tLoanSynd.setLastBorxNo(wkBorxNo);
		tLoanSynd.setCustUKey(loanCom.getCustUKey(iCustId, titaVo));
		tLoanSynd.setGuaUKey(loanCom.getCustUKey(iGuaId, titaVo));
		tLoanSynd.setLeadingBank(iLeadingBank);
		tLoanSynd.setSigningDate(iSigningDate);
		tLoanSynd.setDrawdownStartDate(iDrawdownStartDate);
		tLoanSynd.setDrawdownEndDate(iDrawdownEndDate);
		tLoanSynd.setCommitFeeFlag("");
		tLoanSynd.setPartRate(iPartRate);
		tLoanSynd.setCurrencyCode(iCurrencyCode);
		tLoanSynd.setSyndAmt(iSyndAmt);
		tLoanSynd.setPartAmt(iPartAmt);
		tLoanSynd.setAgentBank(iAgentBank);
		tLoanSynd.setCreditPeriod(iCreditPeriod);
		tLoanSynd.setCentralBankPercent(iCentralBankPercent);
		tLoanSynd.setMasterCustUkey("");
		tLoanSynd.setSubCustUkey1("");
		tLoanSynd.setSubCustUkey2("");
		tLoanSynd.setSubCustUkey3("");
		tLoanSynd.setSubCustUkey4("");
		tLoanSynd.setSubCustUkey5("");
		tLoanSynd.setSubCustUkey6("");

		DeleteLoanSyndItemRoutine();
		// 更新動撥條件檔
		LoanSyndItem tLoanSyndItem = new LoanSyndItem();

		for (int i = 1; i <= 5; i++) {
			this.info("Item=" + titaVo.get("Item" + i));

			if (!"".equals(titaVo.getParam("Item" + i))) {
				String Item = titaVo.get("Item" + i);
				tLoanSyndItem = new LoanSyndItem();
				tLoanSyndItem.setLoanSyndItemId(new LoanSyndItemId(wkCustNo, wkSyndNo, Item));
				tLoanSyndItem.setRate(parse.stringToBigDecimal(titaVo.get("Rate" + i)));
				tLoanSyndItem.setIncr(parse.stringToBigDecimal(titaVo.get("Incr" + i)));
				tLoanSyndItem.setUseDate(parse.stringToInteger(titaVo.get("UseDate" + i)));
				tLoanSyndItem.setMaturityDate(parse.stringToInteger(titaVo.get("MaturityDate" + i)));
				try {
					loanSyndItemService.insert(tLoanSyndItem, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E2009", "聯貸案動撥條件檔"); // 新增資料時，發生錯誤
				}
			} else {
				break;
			}
		}

	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, wkCustNo, 999, wkSyndNo, wkBorxNo, titaVo);
		switch (iFuncCode) {
		case 1: // 新增
		case 3: // 拷貝
			tLoanBorTx.setDesc("聯貸案訂約登錄－新增");
			break;
		case 2: // 修改
			tLoanBorTx.setDesc("聯貸案訂約登錄－修改");
			break;
		case 4: // 刪除
			tLoanBorTx.setDesc("聯貸案訂約登錄－刪除");
			break;
		}
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("FuncCode", iFuncCode);
		tTempVo.putParam("GuaId", iGuaId);
		tTempVo.putParam("LeadingBank", tLoanSynd.getLeadingBank());
		tTempVo.putParam("SigningDate", tLoanSynd.getSigningDate());
		tTempVo.putParam("DrawdownStartDate", tLoanSynd.getDrawdownStartDate());
		tTempVo.putParam("DrawdownEndDate", tLoanSynd.getDrawdownEndDate());
		tTempVo.putParam("PartRate", iPartRate);
		tTempVo.putParam("SyndAmt", tLoanSynd.getSyndAmt());
		tTempVo.putParam("PartAmt", tLoanSynd.getPartAmt());
		tTempVo.putParam("AgentBank", tLoanSynd.getAgentBank());
		tTempVo.putParam("CreditPeriod", tLoanSynd.getCreditPeriod());
		tTempVo.putParam("CentralBankPercent", tLoanSynd.getCentralBankPercent());
		for (int i = 1; i <= 5; i++) {
			if (titaVo.get("Item" + i) != null) {
				tTempVo.putParam("Item" + i, titaVo.get("Item" + i));
				tTempVo.putParam("Rate" + i, titaVo.get("Rate" + i));
				tTempVo.putParam("Incr" + i, titaVo.get("Incr" + i));
				tTempVo.putParam("UseDate" + i, titaVo.get("UseDate" + i));
				tTempVo.putParam("MaturityDate" + i, titaVo.get("MaturityDate" + i));
			} else {
				break;
			}
		}
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 刪除聯貸案動撥條件檔
	private void DeleteLoanSyndItemRoutine() throws LogicException {
		this.info("DeleteLoanSyndItemRoutine ...");

		Slice<LoanSyndItem> slLoanSyndItem = loanSyndItemService.findSyndNo(wkCustNo, wkSyndNo, 0, Integer.MAX_VALUE,
				titaVo);
		List<LoanSyndItem> lLoanSyndItem = slLoanSyndItem == null ? null : slLoanSyndItem.getContent();
		if (lLoanSyndItem != null && lLoanSyndItem.size() > 0) {
			try {
				loanSyndItemService.deleteAll(lLoanSyndItem, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "聯貸案動撥條件檔"); // 刪除資料時，發生錯誤
			}
		}

	}
}