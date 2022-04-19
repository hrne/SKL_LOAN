package com.st1.itx.batch.scheduled;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;

public class ScheduledBase {
	private final Logger logger;

	@Autowired
	public TxBizDateService txBizDateService;

	@Autowired
	public DateUtil dateUtil;

	public ScheduledBase() {
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
			logger = LoggerFactory.getLogger(enclosingClass.getName());
		else
			logger = LoggerFactory.getLogger(getClass().getName());

	}

	public void info(String msg) {
		if (ThreadVariable.isLogger())
			logger.info(msg);
	}

	public void warn(String msg) {
		if (ThreadVariable.isLogger())
			logger.warn(msg);
	}

	public void error(String msg) {
		logger.error(msg);
	}

	public void mustInfo(String msg) {
		logger.info(msg);
	}

	public TxBizDate changeBatchDate(TitaVo titaVo, String datecode, String date) throws LogicException {

		this.info("LC800 proc ... ");

		TxBizDate tTxBizDate = txBizDateService.holdById(datecode, titaVo);

		dateUtil.init();

		boolean newfg = false;
		if (tTxBizDate == null) {
			newfg = true;
			tTxBizDate = new TxBizDate();
		}
		this.info("LC800 newfg = " + newfg);

		dateUtil.setDate_1(date);

		TxBizDate tTxBizDate2 = dateUtil.getForTxBizDate();

		this.info("TxBizDate = " + tTxBizDate2.toString());

		tTxBizDate.setDateCode(datecode);
		tTxBizDate.setDayOfWeek(tTxBizDate2.getDayOfWeek());
		tTxBizDate.setTbsDy(tTxBizDate2.getTbsDy());
		tTxBizDate.setNbsDy(tTxBizDate2.getNbsDy());
		tTxBizDate.setNnbsDy(tTxBizDate2.getNnbsDy());
		tTxBizDate.setLbsDy(tTxBizDate2.getLbsDy());
		tTxBizDate.setLmnDy(tTxBizDate2.getLmnDy());
		tTxBizDate.setTmnDy(tTxBizDate2.getTmnDy());
		tTxBizDate.setMfbsDy(tTxBizDate2.getMfbsDy());
		tTxBizDate.setTbsDyf(tTxBizDate2.getTbsDyf());
		tTxBizDate.setNbsDyf(tTxBizDate2.getNbsDyf());
		tTxBizDate.setNnbsDyf(tTxBizDate2.getNnbsDyf());
		tTxBizDate.setLbsDyf(tTxBizDate2.getLbsDyf());
		tTxBizDate.setLmnDyf(tTxBizDate2.getLmnDyf());
		tTxBizDate.setTmnDyf(tTxBizDate2.getTmnDyf());
		tTxBizDate.setMfbsDyf(tTxBizDate2.getMfbsDyf());

		try {
			if (newfg) {
				this.info("LC800 insert this = " + tTxBizDate.toString());
				txBizDateService.insert(tTxBizDate, titaVo);
			} else {
				this.info("LC800 update this = " + tTxBizDate.toString());
				txBizDateService.update(tTxBizDate, titaVo);
			}
		} catch (DBException e) {
			if (newfg) {
				throw new LogicException(titaVo, "EC002", "系統日期檔(TxBizDate)=" + datecode + "/" + e.getMessage());
			} else {
				throw new LogicException(titaVo, "EC003", "系統日期檔(TxBizDate)=" + datecode + "/" + e.getMessage());
			}
		}
		this.info("LC800 proc finished.");
		return tTxBizDate;

	}

	public void callJob(List<String> jobIdLi, TitaVo titaVo) {
		if (!Objects.isNull(jobIdLi) && jobIdLi.size() > 0)
			for (String jobId : jobIdLi) {
				JobParameters params = new JobParametersBuilder().addDate(ContentName.batchDate, new Date()).addString(ContentName.jobId, jobId).addString(ContentName.tlrno, titaVo.getTlrNo())
						.addString("excuteMode", "0").addString(ContentName.dataBase, titaVo.getDataBase()).addString(ContentName.parent, titaVo.getTxcd())
						.addString(ContentName.loggerFg, ThreadVariable.isLogger() + "").addString("txSeq", titaVo.getJobTxSeq()).toJobParameters();
				MySpring.jobLaunch(jobId, params);
			}
	}

	public void callJob(String jobId, TitaVo titaVo) {
		if (!Objects.isNull(jobId) && !jobId.trim().isEmpty()) {
			JobParameters params = new JobParametersBuilder().addDate(ContentName.batchDate, new Date()).addString(ContentName.jobId, jobId).addString(ContentName.tlrno, titaVo.getTlrNo())
					.addString("excuteMode", "0").addString(ContentName.dataBase, titaVo.getDataBase()).addString(ContentName.parent, titaVo.getTxcd())
					.addString(ContentName.loggerFg, ThreadVariable.isLogger() + "").addString("txSeq", titaVo.getJobTxSeq()).toJobParameters();
			MySpring.jobLaunch(jobId, params);
		}

	}
}
