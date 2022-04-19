package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;

@Service("L3R15")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L3R15 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L3R15.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R15 ");
		this.totaVo.init(titaVo);

		int iCustNo = Integer.valueOf(titaVo.get("CustNo").trim());
		int iFacmNo = Integer.valueOf(titaVo.get("FacmNo").trim());
		int iBormNo = Integer.valueOf(titaVo.get("BormNo").trim());

		LoanBorMainId loanBorMainId = new LoanBorMainId();
		loanBorMainId.setCustNo(iCustNo);
		loanBorMainId.setFacmNo(iFacmNo);
		loanBorMainId.setBormNo(iBormNo);

		LoanBorMain loanBorMain = loanBorMainService.findById(loanBorMainId, titaVo);
		if (loanBorMain == null) {
			throw new LogicException("E0001", "放款資料");
		}

		CustMain custMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (custMain == null) {
			throw new LogicException("E0001", "客戶資料");
		}

		this.totaVo.putParam("CustName", custMain.getCustName());

		this.addList(this.totaVo);
		return this.sendList();
	}
}