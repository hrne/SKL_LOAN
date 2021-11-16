package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

//import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

import com.st1.itx.util.MySpring;

/**
 * Tita<br>
 */

@Service("L5511")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5511 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5511.class);

	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("Run L5511");

		this.totaVo.init(titaVo);



//		MySpring.newTask("BS511", this.txBuffer, titaVo);
		MySpring.newTask("L5511Batch", this.txBuffer, titaVo);
		
		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");

		this.addList(this.totaVo);
		return this.sendList();
	}

}