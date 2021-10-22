package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L560B")
@Scope("prototype")
/**
 * 列印債協通知單
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L560B extends TradeBuffer {
	
	@Autowired
	public L560BReport txReport;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L560B ");
		this.totaVo.init(titaVo);
		String adjFlag = titaVo.getBtnIndex(); //0-存證信函;1-延遲繳款通知函;2-繳款通知函
		this.info("按鈕編號==="+adjFlag);
		txReport.run(titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}
}