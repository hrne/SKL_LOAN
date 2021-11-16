package com.st1.itx.trade.L9;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.ReportCom;

@Service("L9806")
@Scope("prototype")
/**
 * 
 * 
 * @author Xiang Wei Huang
 * @version 1.0.0
 */
public class L9806 extends TradeBuffer {	
	
	@Autowired
	ReportCom reportCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		
		this.totaVo.init(titaVo);
		
		reportCom.executeReports(titaVo, "L9806");

		this.addList(this.totaVo);
		return this.sendList();
	}
}