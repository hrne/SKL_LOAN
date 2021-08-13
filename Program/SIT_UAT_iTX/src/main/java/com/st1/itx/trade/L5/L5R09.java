package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R09")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R09 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5R09.class);
	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R09 ");
		this.totaVo.init(titaVo);
		String iTxCode = titaVo.getParam("RimTxCode");// 交易代號
		String CustId = titaVo.getParam("RimCustId").trim();// 員工編號
		String CustNo = titaVo.getParam("RimCustNo").trim();// 戶號

		this.info("L5R09 iTxCode=[" + iTxCode + "]");
		int iCustNo = 0;
		if (CustNo != null && CustNo.length() != 0) {
			iCustNo = Integer.parseInt(CustNo);
		}
		// 檢查輸入資料
		if (iTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L5R09"); // 交易代號不可為空白
		}

		if (CustId.isEmpty() && iCustNo == 0) {
			throw new LogicException(titaVo, "E2051", "L5R09"); // 統一編號,戶號須擇一輸入
		}
		/* DB服務 */
		CustMain tCustMain = new CustMain();
		if (CustId.isEmpty()) {
			tCustMain = custMainService.custNoFirst(iCustNo, iCustNo);
		} else {
			tCustMain = custMainService.custIdFirst(CustId);
		}
		if (tCustMain != null) {
			this.totaVo.putParam("L5r09CustId", tCustMain.getCustId());
			this.totaVo.putParam("L5r09CustNo", tCustMain.getCustNo());
		} else {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔"); // 查詢資料不存在
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}