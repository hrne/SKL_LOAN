package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.service.BankAuthActService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4940")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4940 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4940.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BankAuthActService bankAuthActService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4940 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		List<BankAuthAct> lBankAuthAct = new ArrayList<BankAuthAct>();

		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
//		String repayBank = titaVo.getParam("RepayBank");
		String repayAcct = titaVo.getParam("RepayAcct");

		Slice<BankAuthAct> sBankAuthAct = null;

		sBankAuthAct = bankAuthActService.authCheck(custNo, repayAcct, 0, 999, this.index, this.limit, titaVo);

		lBankAuthAct = sBankAuthAct == null ? null : sBankAuthAct.getContent();

		if (lBankAuthAct != null && lBankAuthAct.size() != 0) {
			for(BankAuthAct tBankAuthAct : lBankAuthAct) {
				OccursList occursList = new OccursList();
				
				occursList.putParam("OOCustNo", tBankAuthAct.getCustNo());
				occursList.putParam("OOFacmNo", tBankAuthAct.getFacmNo());
				occursList.putParam("OOAuthType", tBankAuthAct.getAuthType());
				occursList.putParam("OORepayAcct", tBankAuthAct.getRepayAcct());
				occursList.putParam("OORepayBank", tBankAuthAct.getRepayBank());
				occursList.putParam("OOStatus", tBankAuthAct.getStatus());
				occursList.putParam("OODepCode", tBankAuthAct.getPostDepCode());
				occursList.putParam("OOAcctNoSeq", tBankAuthAct.getAcctSeq());

				totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "無符合資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}