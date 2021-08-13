package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * ChequeAcct=9,9
 * ChequeNo=9,7
 */
/**
 * L3943 支票內容查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3943")
@Scope("prototype")
public class L3943 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L3943.class);

	/* DB服務注入 */
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public CdBankService cdBankService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3943 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iChequeAcct = this.parse.stringToInteger(titaVo.getParam("ChequeAcct"));
		int iChequeNo = this.parse.stringToInteger(titaVo.getParam("ChequeNo"));

		// 查詢支票檔
		LoanCheque tLoanCheque = loanChequeService.findById(new LoanChequeId(iChequeAcct, iChequeNo), titaVo);
		if (tLoanCheque == null) {
			throw new LogicException(titaVo, "E0001", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo); // 查詢資料不存在
		}
		this.totaVo.putParam("OCustNo", tLoanCheque.getCustNo());
		// 查詢客戶資料主檔
		CustMain tCustMain = custMainService.custNoFirst(tLoanCheque.getCustNo(), tLoanCheque.getCustNo(), titaVo);
		if (tCustMain == null) {
			this.totaVo.putParam("OCustNoX", "");
		} else {
			this.totaVo.putParam("OCustNoX", tCustMain.getCustName());
		}
		this.totaVo.putParam("OChequeName", tLoanCheque.getChequeName());
		this.totaVo.putParam("OChequeAcct", tLoanCheque.getChequeAcct());
		this.totaVo.putParam("OReceiveDate", tLoanCheque.getReceiveDate());
		this.totaVo.putParam("OChequeNo", tLoanCheque.getChequeNo());
		this.totaVo.putParam("OAcDate", tLoanCheque.getAcDate());
		this.totaVo.putParam("OChequeDate", tLoanCheque.getChequeDate());
		this.totaVo.putParam("OEntryDate", tLoanCheque.getEntryDate());
		// 查詢行庫代號檔
		String iBankCode1 = FormatUtil.padX(tLoanCheque.getBankCode().trim(), 7);
		String bankCode1 = FormatUtil.padX(iBankCode1, 3);
		String branchCode1 = FormatUtil.right(iBankCode1, 4);
		CdBank tCdBank = cdBankService.findById(new CdBankId(bankCode1, branchCode1), titaVo);
		if (tCdBank == null) {
			this.totaVo.putParam("OBankItem", "");
			this.totaVo.putParam("OBranchItem", "");
		} else {
			this.totaVo.putParam("OBankItem", tCdBank.getBankItem());
			this.totaVo.putParam("OBranchItem", tCdBank.getBranchItem());
		}
		this.totaVo.putParam("OAreaCode", tLoanCheque.getAreaCode());
		this.totaVo.putParam("OOutsideCode", tLoanCheque.getOutsideCode());
		this.totaVo.putParam("OProcessCode", tLoanCheque.getProcessCode());
		this.totaVo.putParam("OStatusCode", tLoanCheque.getStatusCode());
		this.totaVo.putParam("OCurrencyCode", tLoanCheque.getCurrencyCode());
		this.totaVo.putParam("OChequeAmt", tLoanCheque.getChequeAmt());
		this.totaVo.putParam("OBktwFlag", tLoanCheque.getBktwFlag());
		this.totaVo.putParam("OTsibFlag", tLoanCheque.getTsibFlag());
		this.totaVo.putParam("OMediaFlag", tLoanCheque.getMediaFlag());
		this.totaVo.putParam("OUsageCode", tLoanCheque.getUsageCode());
		this.totaVo.putParam("OServiceCenter", tLoanCheque.getServiceCenter());
		this.totaVo.putParam("OCreditorId", tLoanCheque.getCreditorId());
		// 查詢行庫代號檔
		String iBankCode2 = FormatUtil.padX(tLoanCheque.getCreditorBankCode().trim(), 7);
		String bankCode2 = FormatUtil.padX(iBankCode2, 3);
		String branchCode2 = FormatUtil.right(iBankCode2, 4);
		tCdBank = cdBankService.findById(new CdBankId(bankCode2, branchCode2), titaVo);
		if (tCdBank == null) {
			this.totaVo.putParam("OCreditorBankItem", " ");
		} else {
			this.totaVo.putParam("OCreditorBankItem", tCdBank.getBankItem());
		}
		this.totaVo.putParam("OOtherAcctCode", tLoanCheque.getOtherAcctCode());
		this.totaVo.putParam("OReceiptNo", tLoanCheque.getReceiptNo());
		this.totaVo.putParam("OKinbr", tLoanCheque.getKinbr());
		this.totaVo.putParam("OTellerNo", tLoanCheque.getTellerNo());
		this.totaVo.putParam("OTxtNo", tLoanCheque.getTxtNo());

		this.addList(this.totaVo);
		return this.sendList();
	}
}