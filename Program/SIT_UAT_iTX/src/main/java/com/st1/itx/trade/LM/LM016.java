package com.st1.itx.trade.LM;

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

@Service("LM016")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
//public class LM016 extends BatchBase implements Tasklet, InitializingBean {
//
//	@Autowired
//	LM016Report lm016report;
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		;
//	}
//
//	@Override
//	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//		logger = LoggerFactory.getLogger(LM016.class);
//		return this.exec(contribution, "M");
//	}
//
//	@Override
//	public void run() throws LogicException {
//		this.info("active LM016 ");
//		lm016report.setParentTranCode(this.getParent());
//		lm016report.exec(titaVo);
//	}
//
//}

public class LM016 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LM016.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM016 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("LM016p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}