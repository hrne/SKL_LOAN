package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5910")
@Scope("prototype")
/**
 * 撥款件貸款成數統計資料產生
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5910 extends TradeBuffer {
	@Autowired
	public L5910Report iL5910Report;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L5912 ");
		this.totaVo.init(titaVo);

		long sno1 = 0;
		sno1 = iL5910Report.exec(titaVo);
		totaVo.put("ExcelSnoM", "" + sno1);

		this.addList(this.totaVo);
		return this.sendList();

	}
}