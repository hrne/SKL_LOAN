package com.st1.itx.trade.L1;

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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L1R09")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R09 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1R09.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R09 ");
		this.totaVo.init(titaVo);

		// tita
		String iCustId = titaVo.getParam("RimCustId");
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 功能
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		// new table
		CustMain tCustMain = new CustMain();
		if (iCustNo > 0) {
			tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		}
		if (!iCustId.isEmpty()) {
			tCustMain = sCustMainService.custIdFirst(iCustId, titaVo);
		}

		// funcd1檢查是否存在客戶主檔 不存在拋錯
		if (tCustMain == null && iFunCd == 1) {
			throw new LogicException(titaVo, "E2003", "不存在於客戶主檔。");
			// funcd5調統編姓名查無資料則給空白
		} else if (tCustMain == null && iFunCd == 5) {
			tCustMain = new CustMain();
		}

		this.totaVo.putParam("L1r09CustName", tCustMain.getCustName());
		this.totaVo.putParam("L1r09CustNo", tCustMain.getCustNo());
		this.totaVo.putParam("L1r09CustId", tCustMain.getCustId());

		this.addList(this.totaVo);
		return this.sendList();
	}
}