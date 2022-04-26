package com.st1.itx.trade.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.MySpring;

/**
 * 每日資料庫複製<BR>
 * <BR>
 * 本支程式發動複製Table的StoredProcedure<BR>
 * 每日將Online環境的資料複製到日報環境<BR>
 * <BR>
 * 月底營業日時，多複製一次到月報環境<BR>
 * <BR>
 * 應注意事項：<BR>
 * 1.新增、修改、刪除Table時，應同步上版至所有環境(Online/Day/Month/History)<BR>
 * 2.修改Table欄位時，應檢視負責複製該Table的StoredProcedure是否會受影響<BR>
 * 若有，需一併修改StoredProcedure並與修改Table之語法同時上版<BR>
 * 3.新增Table時，應製作複製該Table的StoredProcedure，並修改本支java程式，增加發動該支StoredProcedure的語句<BR>
 * 新增的StoredProcedure與修改後的本支程式需與Table同時上版<BR>
 * 4.刪除Table時，應製作drop procedure的語法，並與刪除Talbe的語法同時上版<BR>
 * <BR>
 * 註:相關的StoredProcedure都以Usp_Cp_開頭
 * 
 * @author ST1-Chih Wei
 * @version 1.0.0
 */
@Service("DailyCopy")
@Scope("step")
public class DailyCopy extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	JobMainService sJobMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "999999");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("DailyCopy 日報環境開始複製");

		TitaVo tempTitaVo = (TitaVo) this.titaVo.clone();

		// 複製到日報環境
		tempTitaVo.putParam(ContentName.dataBase, ContentName.onDay);

		doCopy(tempTitaVo);
		this.info("DailyCopy 日報環境複製完成");

		// 帳務日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日
		int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();

		this.info("DailyCopy tbsdyf : " + tbsdyf);
		this.info("DailyCopy mfbsdyf : " + mfbsdyf);

		// 每月月底日才執行
		if (tbsdyf == mfbsdyf) {
			this.info("DailyCopy 月報環境開始複製");
			tempTitaVo.putParam(ContentName.dataBase, ContentName.onMon);
			// 複製到月報環境
			doCopy(tempTitaVo);
			this.info("DailyCopy 月報環境複製完成");
		}

		this.info("DailyCopy exit.");
	}

	private void doCopy(TitaVo tempTitaVo) throws LogicException {

		String targetEnv = tempTitaVo.getParam(ContentName.dataBase);
		this.info("DailyCopy doCopy 目標環境: " + tempTitaVo.getParam(ContentName.dataBase));

		// 防呆
		if (targetEnv == null || targetEnv.isEmpty() || targetEnv.equals(ContentName.onLine)) {
			this.info("DailyCopy doCopy 目標環境是onLine 不可複製");
			return;
		}

		MySpring.newTask("DailyCopyProcess", this.txBuffer, tempTitaVo);
	}
}