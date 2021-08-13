package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L9811")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9811 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9811.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9811 ");
		this.totaVo.init(titaVo);

		String job = "";
		job = titaVo.getParam("ReportCode");

		this.info("L9811 job = " + job);
		if (!job.isEmpty()) {
			titaVo.setBatchJobId("j" + job);
		}
		this.info("L9811 titaVo.getBatchJobId() = " + titaVo.getBatchJobId());
		this.addList(this.totaVo);
		return this.sendList();
	}

}