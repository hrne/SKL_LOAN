package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R54")
@Scope("prototype")
/**
 * L5R54
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L5R54 extends TradeBuffer {

	@Autowired
	NegMainService negMainService;
	@Autowired
	CustMainService custMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R54 ");
		this.totaVo.init(titaVo);

		int iCustNo = Integer.parseInt(titaVo.getParam("RimCustNo"));
		NegMain tNegMain = negMainService.statusFirst("0", iCustNo, titaVo);
		if (tNegMain == null) {
			throw new LogicException("E2003", "債務協商案件主檔");// 查無資料
		}
		CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException("E2003", "客戶主檔");// 查無資料
		}
		this.totaVo.putParam("L5R54CaseSeq", tNegMain.getCaseSeq());
		this.totaVo.putParam("L5R54CustName", tCustMain.getCustName());
		this.totaVo.putParam("L5R54PrincipalBal", tNegMain.getPrincipalBal());
		this.totaVo.putParam("L5R54AccuOverAmt", tNegMain.getAccuOverAmt());
		this.totaVo.putParam("L5R54DueAmt", tNegMain.getDueAmt());
		this.totaVo.putParam("L5R54PayIntDate", tNegMain.getPayIntDate());

		this.addList(this.totaVo);
		return this.sendList();

	}

}