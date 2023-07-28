package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L9721")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9721 extends TradeBuffer {
	@Autowired
	CdWorkMonthService cdWorkMonthService;
	@Autowired
	Parse parse;

	private String TXCD = "L9721";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + TXCD);
		this.totaVo.init(titaVo);
		int year = parse.stringToInteger(titaVo.getParam("InputYear")) + 1911;
		int month = parse.stringToInteger(titaVo.getParam("InputMonth"));

		// 工作月(西曆)
		CdWorkMonth tCdWorkMonth = cdWorkMonthService.findById(new CdWorkMonthId(year,month), titaVo);
		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔"); // 查詢資料不存在
		}
		
        titaVo.putParam("InputEndDate" , tCdWorkMonth.getEndDate() + 19110000);
        
		MySpring.newTask(TXCD + "p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}