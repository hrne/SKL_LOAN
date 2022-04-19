package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L4452")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4452 extends TradeBuffer {
	@Autowired
	public BankDeductDtlService bankDeductDtlService;
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4452 ");
		int iFunctionCode = parse.stringToInteger(titaVo.getParam("FunctionCode"));
		int iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		int iOpItem = parse.stringToInteger(titaVo.getParam("OpItem"));
		this.totaVo.init(titaVo);
		if (iFunctionCode == 1) {
			Slice<BankDeductDtl> slBankDeductDtl = null;
			switch (iOpItem) {
			case 1: // ach
				slBankDeductDtl = bankDeductDtlService.repayBankNotEq("700", iEntryDate, iEntryDate, this.index, this.limit, titaVo);
				break;
			case 2: // post
				slBankDeductDtl = bankDeductDtlService.repayBankEq("700", iEntryDate, iEntryDate, this.index, this.limit, titaVo);
				break;
			default: // all
				slBankDeductDtl = bankDeductDtlService.entryDateRng(iEntryDate, iEntryDate, this.index, this.limit, titaVo);
				break;
			}

			if (slBankDeductDtl == null) {
				throw new LogicException("E0001", "銀行扣款資料不存在"); // 查詢資料不存在
			}

			ArrayList<BankDeductDtl> lBankDeductDtl = new ArrayList<BankDeductDtl>();
			lBankDeductDtl = new ArrayList<BankDeductDtl>(slBankDeductDtl.getContent());
			for (BankDeductDtl tBankDeductDtl : lBankDeductDtl) {
				if (!"".equals(tBankDeductDtl.getMediaCode())) {
					throw new LogicException("E0015", "已產製媒體，需重製媒體碼後再產製"); // 檢查錯誤
				}
			}
		}
		// 執行交易
		MySpring.newTask("L4452Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}