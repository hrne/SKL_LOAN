package com.st1.itx.trade.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.MySpring;

@Service("EodFinal")
@Scope("step")
/**
 * (日終批次) 判斷是否為月底日,若是,執行LC701 月底日日終維護
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class EodFinal extends BatchBase implements Tasklet, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "BAT001");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// // logger = LoggerFactory.getLogger(EodFinal.class);

		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active EodFinal");

		// 帳務日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日
		int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();

		// 此為日終維護,讀onlineDB
//		this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		this.info("EodFinal tbsdyf : " + tbsdyf);
		this.info("EodFinal mfbsdyf : " + mfbsdyf);

		// 每月月底日才執行
		if (tbsdyf == mfbsdyf) {
			this.info("EodFinal 本日為月底日,執行月底日日終維護.");

			MySpring.newTask("LC701", this.txBuffer, titaVo);

			String yearMonth = String.valueOf((tbsdyf / 100));

			if (yearMonth.length() >= 2) {
				yearMonth = yearMonth.substring(yearMonth.length() - 2);

				// 每年年底日才執行
				if (yearMonth.equals("12")) {
					this.info("EodFinal 本日為年底日,執行月底日日終維護.");

					MySpring.newTask("LC702", this.txBuffer, titaVo);
				}
			}
		}

		this.info("EodFinal exit.");

	}

}