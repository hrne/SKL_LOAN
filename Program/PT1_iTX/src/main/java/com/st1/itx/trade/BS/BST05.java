package com.st1.itx.trade.BS;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.trade.L7.L7205p;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * 測試
 * 
 * @author st1
 *
 */
@Service("BST05")
@Scope("prototype")
public class BST05 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public L7205p l7205p;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BST05 run  ");
		String[] strAr = titaVo.getParam("Parm").split(",");
		int iYearMonth = this.parse.stringToInteger(strAr[0]);
		this.info("iYearMonth=" + iYearMonth);
		l7205p.updMonthlyLM042Statis(titaVo, iYearMonth + 191100);
		l7205p.updMonthlyLM055AssetLoss(titaVo, iYearMonth + 191100);
		return null;

	}

}
