package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Component("L5061")
@Scope("prototype")

/**
 * 催收催繳明細表
 * 
 * @author Fegie
 * @version 1.0.0
 */

public class L5061 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5061.class);
	
	@Autowired
	public L5061TelReport iL5061TelReport;
	
	@Autowired
	public L5061MeetReport iL5061MeetReport;
	
	@Autowired
	public L5061LetterReport iL5061LetterReport;
	
	@Autowired
	public L5061AllReport iL5061AllReport;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5061 ");
		this.totaVo.init(titaVo);
		
		int chooseFlag = Integer.valueOf(titaVo.getParam("OptionCode"));
		
		switch(chooseFlag) {
		case 1:
			iL5061TelReport.exec(titaVo);
			break;
		case 2:
			iL5061MeetReport.exec(titaVo);
			break;
		case 3:
			iL5061LetterReport.exec(titaVo);
			break;
		case 5:
			iL5061AllReport.exec(titaVo);
			break;
		}
		totaVo.putParam("OFlag",1);
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}