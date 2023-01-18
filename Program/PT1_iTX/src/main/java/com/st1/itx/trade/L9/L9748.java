package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * L9748
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Service("L9748")
@Scope("prototype")
public class L9748 extends TradeBuffer {

	@Autowired
	L9748Report l9748Report;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9748 ");
		this.totaVo.init(titaVo);

		l9748Report.exec(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}