package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegFinAcct;
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R53")
@Scope("prototype")
/**
 * L5R53 抓取合併日與合併銀行
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L5R53 extends TradeBuffer {

	@Autowired
	NegFinAcctService negFinAcctService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R53 ");
		this.totaVo.init(titaVo);

		String iFinCode = titaVo.getParam("RimFinCode");

		NegFinAcct tNegFinAcct = negFinAcctService.findById(iFinCode, titaVo);
		if (tNegFinAcct == null) {
			throw new LogicException("E2003", "債務協商債權機構帳戶檔");// 查無資料
		}
		// 檢核 合併銀行不可為空白或合併日需=0
		if (tNegFinAcct.getNewFinCode().isEmpty()) {
			throw new LogicException("E0015", "合併銀行不可為空");// 檢查錯誤
		}
		if (tNegFinAcct.getExecuteDate() > 0) {
			throw new LogicException("E0015", "已執行。 執行日期；" + tNegFinAcct.getExecuteDate());// 檢查錯誤
		}

		this.totaVo.putParam("L5R53FinItem", tNegFinAcct.getFinItem());
		this.totaVo.putParam("L5R53NewFinCode", tNegFinAcct.getNewFinCode());
		this.totaVo.putParam("L5R53MergerDate", tNegFinAcct.getMergerDate());

		this.addList(this.totaVo);
		return this.sendList();

	}

}