package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * 夜間批次重新執行
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L687A")
@Scope("prototype")
public class L687A extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L687A ");
		this.totaVo.init(titaVo);

		if (titaVo.containsKey("OOJobCode")) {
			String jobName = titaVo.getParam("OOJobCode");
			
			this.info("L687A doExecution ... jobName = " + (jobName == null ? "" : jobName));

			if (jobName == null || jobName.isEmpty()) {
				this.error("L687A doExecution() got empty jobName");
				throw new LogicException("EC009", "欲發動的批次程式名稱為空白");
			} else {
				titaVo.setBatchJobId(jobName);
			}
		}

		this.info("L687A exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}
}