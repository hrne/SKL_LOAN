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
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.date.DateUtil;

@Service("L5CollListUpd")
@Scope("step")
/**
 * (日終批次)維護 CollList 法催紀錄清單檔
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class L5CollListUpd extends BatchBase implements Tasklet, InitializingBean {

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	JobMainService sJobMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "999999");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		String txSeq = chunkContext.getStepContext().getStepExecution().getExecutionContext().getString("txSeq");
		this.titaVo.putParam("JobTxSeq", txSeq);
		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active L5CollListUpd ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		String empNo = titaVo.getTlrNo();

		this.info("L5CollListUpd empNo = " + empNo);

		String txtNo = titaVo.getTxtNo();

		if (txtNo == null || txtNo.isEmpty()) {
			txtNo = "99999999";
		}

		this.info("L5CollListUpd txtNo = " + txtNo);

		// 此為日終維護,讀onlineDB
//		this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		// 2020-09-29 Wei 新增:多傳一個參數"前第六個營業日"(last6bsdyf)
		// ex: 本營業日為"2020/9/28",前第六個營業日為"2020/9/18"
		int l6bsdyf = dateUtil.getbussDate(tbsdyf, -6);
		this.info("L5CollListUpd l6bsdyf = " + l6bsdyf);

		// 2020-10-13 Wei 新增:多傳一個參數"前第七個營業日"(last7bsdyf)
		// ex: 本營業日為"2020/9/28",前第六個營業日為"2020/9/17"
		int l7bsdyf = dateUtil.getbussDate(tbsdyf, -7);
		this.info("L5CollListUpd l7bsdyf = " + l7bsdyf);

		String txSeq = titaVo.getParam("JobTxSeq");

		// 2021-11-16 Wei 修改:增加參數傳入交易序號
		sJobMainService.Usp_L5_CollList_Upd(tbsdyf, empNo, txtNo, l6bsdyf, l7bsdyf, txSeq, titaVo);
	}

}