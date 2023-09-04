package com.st1.itx.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.config.AstrMapper;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JobMain;
import com.st1.itx.db.domain.TxCruiser;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.service.TxCruiserService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.log.SysLogger;

@Component("scheduledProcess")
@Scope("singleton")
public class ScheduledProcess extends SysLogger {

	@Autowired
	private TxCruiserService txCruiserService;

	@Autowired
	private JobMainService jobMainService;

	@Autowired
	private WebClient webClient;

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private AstrMapper astrMapper;

	@Scheduled(cron = "0 0 2 * * ?")
	public void reLoadAstar() {
		this.mustInfo("reLoadAstrMapper >>>");
		astrMapper.init();
	}

	@Scheduled(fixedDelay = 60000)
	private void lookUpTxCruiser() {
		this.mustInfo("Active lookUpTxCruiser Every 60 Second...");
		Slice<TxCruiser> txCruiserSi = null;
		try {
			txCruiserSi = txCruiserService.FindAllByStatus("U", 0, Integer.MAX_VALUE);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("txCruiserService.FindAllByStatus error:" + errors.toString());
		}

		if (txCruiserSi != null)
			for (TxCruiser txs : txCruiserSi.getContent()) {
				this.mustInfo(txs.toString());

				Timestamp startTime = txs.getCreateDate();
				Timestamp endTime = new Timestamp(new Date().getTime());
				int hours = (int) ((endTime.getTime() - startTime.getTime()) / (1000 * 60 * 60));
				int minutes = (int) (((endTime.getTime() - startTime.getTime()) / 1000 - hours * (60 * 60)) / 60);
				int second = (int) ((endTime.getTime() - startTime.getTime()) / 1000 - hours * (60 * 60) - minutes * 60);

				this.mustInfo(txs.getTxCruiserId() + "Has Been " + hours + " Hours " + minutes + " Minutes " + second + " Second..");
				if (hours >= 3) {
					txs.setStatus("F");
					try {
						txCruiserService.update(txs);
						continue;
					} catch (DBException e) {
						this.error(e.getErrorMsg());
					} catch (Exception e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						this.error(errors.toString());
					}
				}

				Slice<JobMain> jobMainSi = jobMainService.findAllByTxSeq(txs.getTxSeq(), 0, Integer.MAX_VALUE);
				boolean isFinish = true;
				boolean isBroken = false;
				boolean isEodFlow = false;
				if (jobMainSi != null) {
					for (JobMain jm : jobMainSi.getContent()) {
						isFinish = !jm.getStatus().equals("S") ? false : isFinish;
						isBroken = jm.getStatus().equals("F") ? true : isBroken;

						if ("eodFlow".equals(jm.getJobCode()) && isBroken)
							isEodFlow = true;
					}

					try {
						ThreadVariable.setObject(ContentName.empnot, txs.getTlrNo());
						if (isFinish) {
							txs.setStatus("S");
							txCruiserService.update(txs);
						}

						if (isBroken) {
							txs.setStatus("F");
							txCruiserService.update(txs);
						}
					} catch (DBException e) {
						StringWriter errors = new StringWriter();
						e.printStackTrace(new PrintWriter(errors));
						this.error(errors.toString());
					} finally {
						if (isFinish) {
							if ("1".equals(txs.getSendMSgChainOff()))
								webClient.sendPost(dateUtil.getNowStringBc(), "2300", txs.getTlrNo(), "N", "LC009", txs.getTlrNo(), txs.getTxCode() + " 執行成功", new TitaVo());
							else
								webClient.sendPost(dateUtil.getNowStringBc(), "2300", txs.getTlrNo(), "Y", "LC009", txs.getTlrNo(), txs.getTxCode() + " 執行成功", new TitaVo());
						}
						if (isBroken) {
							if (isEodFlow)
								webClient.sendTicker("0000", "00001", "209912312300", "夜間批次執行中斷", false, new TitaVo());
							else
								webClient.sendPost(dateUtil.getNowStringBc(), "2300", txs.getTlrNo(), "Y", "L6970", txs.getTxSeq(), txs.getTxCode() + " 執行失敗, 請至L6970查看", new TitaVo());
						}
					}
				}
			}
		ThreadVariable.clearThreadLocal();
	}
}
