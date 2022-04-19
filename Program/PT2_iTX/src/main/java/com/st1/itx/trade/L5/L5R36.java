package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R36")
@Scope("prototype")
/**
 *
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R36 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.totaVo.init(titaVo);

		int iCustNo = Integer.valueOf(titaVo.getParam("RimCustNo"));
		CustMain iCustMain = new CustMain();

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		iCustMain = iCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);

		if (iCustMain == null) {
			throw new LogicException(titaVo, "E0001", "");
		} else {
			totaVo.putParam("L5R36CustName", iCustMain.getCustName());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}