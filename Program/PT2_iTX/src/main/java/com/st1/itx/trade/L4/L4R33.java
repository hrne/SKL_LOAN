package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.BankRmtfId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.BankRmtfService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimAcDate=9,7
 * RimBatchNo=X,6
 * RimDetailSeq=9,6
 */
@Service("L4R33") // 匯款轉帳檔明細
@Scope("prototype")
/**
 *
 *
 * @author Linda
 * @version 1.0.0
 */
public class L4R33 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public BankRmtfService sBankRmtfService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R33 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		int iFAcDate = iAcDate + 19110000;
		String iBatchNo = titaVo.getParam("BatchNo");
		int iDetailSeq = this.parse.stringToInteger(titaVo.getParam("DetailSeq"));
		
		// 檢查輸入資料
		if ( !(iDetailSeq > 0) ) {
			throw new LogicException(titaVo, "E0019", "L4R33明細序號不可空白"); 
		}
		if (iBatchNo.isEmpty()) {
			throw new LogicException(titaVo, "E0019", "L4R33批號不可空白"); 
		}
		if ( !(iAcDate > 0) ) {
			throw new LogicException(titaVo, "E0019", "L4R33會計日不可空白");
		}

		// for L4926匯款轉帳檔查詢 連動L4206匯款轉帳檔明細資料
		BankRmtfId BankRmtfId = new BankRmtfId();
		BankRmtfId.setAcDate(iFAcDate);
		BankRmtfId.setBatchNo(iBatchNo);
		BankRmtfId.setDetailSeq(iDetailSeq);
		
		BankRmtf tBankRmtf = sBankRmtfService.findById(BankRmtfId);

		/* 如有找到資料 */
		if (tBankRmtf != null) {
			this.totaVo.putParam("L4R33CustNo", tBankRmtf.getCustNo());
			this.totaVo.putParam("L4R33RepayType", tBankRmtf.getRepayType());
			this.totaVo.putParam("L4R33RepayAmt", tBankRmtf.getRepayAmt());
			this.totaVo.putParam("L4R33DepAcctNo", tBankRmtf.getDepAcctNo());
			this.totaVo.putParam("L4R33EntryDate", tBankRmtf.getEntryDate());
			this.totaVo.putParam("L4R33DscptCode", tBankRmtf.getDscptCode());
			this.totaVo.putParam("L4R33VirtualAcctNo", tBankRmtf.getVirtualAcctNo());
			this.totaVo.putParam("L4R33WithdrawAmt", tBankRmtf.getWithdrawAmt());
			this.totaVo.putParam("L4R33DepositAmt", tBankRmtf.getDepositAmt());
			this.totaVo.putParam("L4R33Balance", tBankRmtf.getBalance());
			this.totaVo.putParam("L4R33RemintBank", tBankRmtf.getRemintBank());
			this.totaVo.putParam("L4R33TraderInfo", tBankRmtf.getTraderInfo());
			this.totaVo.putParam("L4R33AmlRsp", tBankRmtf.getAmlRsp());
			this.totaVo.putParam("L4R33ReconCode", tBankRmtf.getReconCode());
			this.totaVo.putParam("L4R33TitaTlrNo", tBankRmtf.getTitaTlrNo());
			this.totaVo.putParam("L4R33TitaTxtNo", tBankRmtf.getTitaTxtNo());
			int iCustNo = tBankRmtf.getCustNo();
			if (iCustNo>0) {
				// 查詢客戶資料主檔
				CustMain tCustMain = new CustMain();
				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
				if (tCustMain != null) {
					this.totaVo.putParam("L4R33CustName", tCustMain.getCustName().replace("$n", ""));// 戶名
				} else {
					this.totaVo.putParam("L4R33CustName", ""); // 戶名
				}
			}
		} 

		this.addList(this.totaVo);
		return this.sendList();
	}

}