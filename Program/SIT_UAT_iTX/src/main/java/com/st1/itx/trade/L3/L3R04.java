package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/*
 * Tita
 * RimTxCode=X,5
 * RimFuncCode=X,1 1:新增 2:修改 3:拷貝 4:刪除 5:查詢
 * RimChequeAcct=9,9
 * RimChequeNo=9,7
 */
/**
 * L3R04 尋找支票檔資料,檢查該支票是否已存在
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R04")
@Scope("prototype")
public class L3R04 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3R04.class);

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
			throw new LogicException(titaVo, "E0009", "L3R03"); // 交易代號不可為空白
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
			this.totaVo.putParam("OCustNo", 0);
			this.totaVo.putParam("OChequeName", "");
			this.totaVo.putParam("OChequeAcct", 0);
			this.totaVo.putParam("OEntryDate", 0);
			this.totaVo.putParam("OChequeNo", 0);
			this.totaVo.putParam("OAcDate", 0);
			this.totaVo.putParam("OChequeDate", 0);
			this.totaVo.putParam("OLastUpdate", "");
			this.totaVo.putParam("OBankCode", "");
			this.totaVo.putParam("OBankItem", "");
			this.totaVo.putParam("OBranchItem", "");
			this.totaVo.putParam("OAreaCode", 0);
			this.totaVo.putParam("OOutsideCode", 0);
			this.totaVo.putParam("OProcessCode", 0);
			this.totaVo.putParam("OStatusCode", 0);
			this.totaVo.putParam("OCurrencyCode", "");
			this.totaVo.putParam("OChequeAmt", new BigDecimal(0));
			this.totaVo.putParam("OBktwFlag", "");
			this.totaVo.putParam("OTsibFlag", "");
			this.totaVo.putParam("OMediaFlag", "");
			this.totaVo.putParam("OUsageCode", 0);
			this.totaVo.putParam("OServiceCenter", "");
			this.totaVo.putParam("OCreditorId", "");
			this.totaVo.putParam("OCreditorBankCode", "");
			this.totaVo.putParam("OCreditorBankItem", "");
			this.totaVo.putParam("OOtherAcctCode", "");
			this.totaVo.putParam("OReceiptNo", "");
			this.totaVo.putParam("OKinbr", "");
			this.totaVo.putParam("OTellerNo", "");
			this.totaVo.putParam("OTxtNo", 0);
		} else {
			if (iFuncCode == 1 && iTxCode.equals("L3210")) { // 暫收款登錄
				throw new LogicException(titaVo, "E0012", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo); // 該筆資料已存在
			}
			if (iFuncCode == 5 && iTxCode.equals("L3220") && !tLoanCheque.getStatusCode().equals("0")) { // 暫收款退還
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
				throw new LogicException(titaVo, "E3058", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo + " 票據狀況碼 = " + sErr); // 該票據狀況碼非為未處理
			}
			this.totaVo.putParam("OCustNo", tLoanCheque.getCustNo());
			this.totaVo.putParam("OChequeName", tLoanCheque.getChequeName());
			this.totaVo.putParam("OChequeAcct", tLoanCheque.getChequeAcct());
			this.totaVo.putParam("OEntryDate", tLoanCheque.getEntryDate());
			this.totaVo.putParam("OChequeNo", tLoanCheque.getChequeNo());
			this.totaVo.putParam("OAcDate", tLoanCheque.getAcDate());
			this.totaVo.putParam("OChequeDate", tLoanCheque.getChequeDate());
			this.totaVo.putParam("OLastUpdate", this.parse.timeStampToString(tLoanCheque.getLastUpdate()));
			this.totaVo.putParam("OBankCode", tLoanCheque.getBankCode());
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
			this.totaVo.putParam("OCreditorBankCode", tLoanCheque.getCreditorBankCode());
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
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}