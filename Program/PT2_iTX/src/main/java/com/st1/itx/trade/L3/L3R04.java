package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L3R04 尋找支票檔資料,檢查該支票是否已存在
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R04")
@Scope("prototype")
public class L3R04 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public CdBankService cdBankService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R04 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iTxCode = titaVo.getParam("RimTxCode");
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		int iChequeAcct = this.parse.stringToInteger(titaVo.getParam("RimChequeAcct"));
		int iChequeNo = this.parse.stringToInteger(titaVo.getParam("RimChequeNo"));

		// 檢查輸入資料
		if (iTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", ""); // 交易代號不可為空白
		}
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "功能 = " + iFuncCode); // 功能選擇錯誤
		}

		// 查詢支票檔
		LoanCheque tLoanCheque = loanChequeService.findById(new LoanChequeId(iChequeAcct, iChequeNo), titaVo);
		if (tLoanCheque == null) {
			if (!(iFuncCode == 1 && iTxCode.equals("L3210"))) { // 暫收款登錄
				throw new LogicException(titaVo, "E0001", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo); // 查詢資料不存在
			}
			this.totaVo.putParam("L3r04CustNo", 0);
			this.totaVo.putParam("L3r04ChequeName", "");
			this.totaVo.putParam("L3r04ChequeAcct", 0);
			this.totaVo.putParam("L3r04EntryDate", 0);
			this.totaVo.putParam("L3r04ChequeNo", 0);
			this.totaVo.putParam("L3r04AcDate", 0);
			this.totaVo.putParam("L3r04ChequeDate", 0);
			this.totaVo.putParam("L3r04LastUpdate", "");
			this.totaVo.putParam("L3r04BankCode", "");
			this.totaVo.putParam("L3r04BankItem", "");
			this.totaVo.putParam("L3r04BranchItem", "");
			this.totaVo.putParam("L3r04AreaCode", 0);
			this.totaVo.putParam("L3r04OutsideCode", 0);
			this.totaVo.putParam("L3r04ProcessCode", 0);
			this.totaVo.putParam("L3r04StatusCode", 0);
			this.totaVo.putParam("L3r04CurrencyCode", "");
			this.totaVo.putParam("L3r04ChequeAmt", new BigDecimal(0));
			this.totaVo.putParam("L3r04BktwFlag", "");
			this.totaVo.putParam("L3r04TsibFlag", "");
			this.totaVo.putParam("L3r04MediaFlag", "");
			this.totaVo.putParam("L3r04UsageCode", 0);
			this.totaVo.putParam("L3r04ServiceCenter", "");
			this.totaVo.putParam("L3r04CreditorId", "");
			this.totaVo.putParam("L3r04CreditorBankCode", "");
			this.totaVo.putParam("L3r04CreditorBankItem", "");
			this.totaVo.putParam("L3r04OtherAcctCode", "");
			this.totaVo.putParam("L3r04ReceiptNo", "");
			this.totaVo.putParam("L3r04Kinbr", "");
			this.totaVo.putParam("L3r04TellerNo", "");
			this.totaVo.putParam("L3r04TxtNo", 0);
		} else {
			if (iFuncCode == 1 && iTxCode.equals("L3210")) { // 暫收款登錄
				throw new LogicException(titaVo, "E0012", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo); // 該筆資料已存在
			}
			if (iFuncCode == 5 && iTxCode.equals("L3220")
					&& !(tLoanCheque.getStatusCode().equals("0") || tLoanCheque.getStatusCode().equals("5"))) { // 暫收款退還
				String sErr = "";
				switch (tLoanCheque.getStatusCode()) {
				case "1":
					sErr = "已兌現入帳";
					break;
				case "2":
					sErr = "已退票";
					break;
				case "3":
					sErr = "已抽票";
					break;
				case "4":
					sErr = "已兌現入帳";
					break;
				}
				throw new LogicException(titaVo, "E3058",
						"支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo + " 票據狀況碼 = " + sErr); // 該票據狀況碼非未處理與即期票
			}
			this.totaVo.putParam("L3r04CustNo", tLoanCheque.getCustNo());
			this.totaVo.putParam("L3r04ChequeName", tLoanCheque.getChequeName());
			this.totaVo.putParam("L3r04ChequeAcct", tLoanCheque.getChequeAcct());
			this.totaVo.putParam("L3r04EntryDate", tLoanCheque.getEntryDate());
			this.totaVo.putParam("L3r04ChequeNo", tLoanCheque.getChequeNo());
			this.totaVo.putParam("L3r04AcDate", tLoanCheque.getAcDate());
			this.totaVo.putParam("L3r04ChequeDate", tLoanCheque.getChequeDate());
			this.totaVo.putParam("L3r04LastUpdate", this.parse.timeStampToString(tLoanCheque.getLastUpdate()));
			this.totaVo.putParam("L3r04BankCode", tLoanCheque.getBankCode());
			// 查詢行庫代號檔
			String iBankCode1 = FormatUtil.padX(tLoanCheque.getBankCode().trim(), 7);
			String bankCode1 = FormatUtil.padX(iBankCode1, 3);
			String branchCode1 = FormatUtil.right(iBankCode1, 4);
			CdBank tCdBank = cdBankService.findById(new CdBankId(bankCode1, branchCode1), titaVo);
			if (tCdBank == null) {
				this.totaVo.putParam("L3r04BankItem", "");
				this.totaVo.putParam("L3r04BranchItem", "");
			} else {
				this.totaVo.putParam("L3r04BankItem", tCdBank.getBankItem());
				this.totaVo.putParam("L3r04BranchItem", tCdBank.getBranchItem());
			}
			this.totaVo.putParam("L3r04AreaCode", tLoanCheque.getAreaCode());
			this.totaVo.putParam("L3r04OutsideCode", tLoanCheque.getOutsideCode());
			this.totaVo.putParam("L3r04ProcessCode", tLoanCheque.getProcessCode());
			this.totaVo.putParam("L3r04StatusCode", tLoanCheque.getStatusCode());
			this.totaVo.putParam("L3r04CurrencyCode", tLoanCheque.getCurrencyCode());
			this.totaVo.putParam("L3r04ChequeAmt", tLoanCheque.getChequeAmt());
			this.totaVo.putParam("L3r04BktwFlag", tLoanCheque.getBktwFlag());
			this.totaVo.putParam("L3r04TsibFlag", tLoanCheque.getTsibFlag());
			this.totaVo.putParam("L3r04MediaFlag", tLoanCheque.getMediaFlag());
			this.totaVo.putParam("L3r04UsageCode", tLoanCheque.getUsageCode());
			this.totaVo.putParam("L3r04ServiceCenter", tLoanCheque.getServiceCenter());
			this.totaVo.putParam("L3r04CreditorId", tLoanCheque.getCreditorId());
			this.totaVo.putParam("L3r04CreditorBankCode", tLoanCheque.getCreditorBankCode());
			// 查詢行庫代號檔
			String iBankCode2 = FormatUtil.padX(tLoanCheque.getCreditorBankCode().trim(), 7);
			String bankCode2 = FormatUtil.padX(iBankCode2, 3);
			String branchCode2 = FormatUtil.right(iBankCode2, 4);
			tCdBank = cdBankService.findById(new CdBankId(bankCode2, branchCode2), titaVo);
			if (tCdBank == null) {
				this.totaVo.putParam("L3r04CreditorBankItem", " ");
			} else {
				this.totaVo.putParam("L3r04CreditorBankItem", tCdBank.getBankItem());
			}
			this.totaVo.putParam("L3r04OtherAcctCode", tLoanCheque.getOtherAcctCode());
			this.totaVo.putParam("L3r04ReceiptNo", tLoanCheque.getReceiptNo());
			this.totaVo.putParam("L3r04Kinbr", tLoanCheque.getKinbr());
			this.totaVo.putParam("L3r04TellerNo", tLoanCheque.getTellerNo());
			this.totaVo.putParam("L3r04TxtNo", tLoanCheque.getTxtNo());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}