package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("LC899")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LC899 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LC899.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC899 >> " + titaVo.get("Prgm").toString().trim());
		this.totaVo.init(titaVo);

		String runmode = titaVo.getParam("RunMode").trim();
		String prgm = titaVo.get("Prgm").toString().trim();

		if ("1".equals(runmode)) {

			TradeBuffer x = (TradeBuffer) MySpring.getBean(prgm);
			x.setLoggerFg("1", "com.st1.itx.trade." + titaVo.getTxCode().substring(0, 2) + "." + titaVo.getTxCode());
			x.setTxBuffer(this.txBuffer);
			this.info(titaVo.toString());
			this.info(txBuffer.getTxCom().toString());
//		this.totaVoList.addAll(x.run(titaVo));
			x.run(titaVo);

			this.txBuffer = x.getTxBuffer();
		} else if ("2".equals(runmode)) {
			MySpring.newTask(prgm, this.txBuffer, titaVo);
		} else if ("3".equals(runmode)) {
			MySpring.newTask(prgm, this.txBuffer, titaVo, true);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}