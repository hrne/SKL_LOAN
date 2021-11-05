package com.st1.itx.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.dataVO.TxCom;
import com.st1.itx.db.transaction.BaseTransaction;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.maintain.Cs70UpDBS;
import com.st1.itx.maintain.Cs80UpDBS;
import com.st1.itx.maintain.MainProcess;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.JsonConvert;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.AcEnterCom;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.format.EloanConver;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.log.SysLogger;

/**
 * ApControl
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Controller("apControl")
@Scope("prototype")
public class ApControl extends SysLogger {

	@Autowired
	public JsonConvert jsonConvert;

	@Autowired
	public EloanConver eloanConver;

	@Autowired
	public BaseTransaction baseTransaction;

	@Autowired
	public TitaVo titaVo;

	@Autowired
	public TotaVo totaVo;

	@Autowired
	public MainProcess mainProcess;

	@Autowired
	public Cs70UpDBS cs70UpDBS;

	@Autowired
	public Cs80UpDBS cs80UpDBS;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public AcEnterCom acEnterCom;

	@Autowired
	public TxAmlCom txAmlCom;

	private TxBuffer txBuffer;

	private TotaVoList totaVoList = new TotaVoList();

	private boolean RspFlag = false;

	public String tota = "";

	@PostConstruct
	public void init() {
	}

	public String callTrade(String tita) throws Exception {

		/* Time watch */
		StopWatch watch = new StopWatch();
		watch.start();

		if (this.stringToVo(tita)) {
			try {
				/* mainProcess CS10 */

				// eric 2020.6.25
//				mainProcess.setTitaVo(this.titaVo);
				mainProcess.init(this.titaVo);

//				if (!this.titaVo.isTxcdInq())
				mainProcess.cs10Process();
				this.txBuffer = mainProcess.getTxBuffer();

				/* After base check if status true call trade next */
				/* CS80 */
				try {
					StopWatch watchT = new StopWatch();
					watchT.start();
					this.callAp();
					watchT.stop();
					this.info("Trade execution time " + watchT.getTotalTimeMillis() + " Millisecond");
				} catch (LogicException e) {
					this.totaVoList.clear();
					this.totaVoList.add(e.getErrorMsg(this.titaVo));
					baseTransaction.rollBack();
				} catch (BeansException e) {
					this.totaVoList.clear();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error(errors.toString());
					this.totaVoList.add(new LogicException("CE000", "APCTL異常或無此核心程式").getErrorMsg(this.titaVo));
					baseTransaction.rollBack();
				} catch (InvalidDataAccessResourceUsageException e) {
					this.totaVoList.clear();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error(errors.toString());
					this.totaVoList.add(new LogicException("CE000", "DB 錯誤，請洽資訊人員...").getErrorMsg(this.titaVo));
					baseTransaction.rollBack();
				} catch (Throwable e) {
					this.totaVoList.clear();
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error(errors.toString());
					this.totaVoList.add(new LogicException("CE000", "程式邏輯錯誤，請洽資訊人員...").getErrorMsg(this.titaVo));
					baseTransaction.rollBack();
				}
				if (this.totaVoList.size() > 0 && this.totaVoList.get(0).isError()) {
					TxCom txCom = this.txBuffer.getTxCom();
					txCom.setErrorMsg(this.totaVoList.get(0).getErrorMsg());
					this.txBuffer.setTxCom(txCom);
					txCom = null;
				}
			} catch (LogicException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());

				this.totaVoList.clear();
				this.totaVoList.add(e.getErrorMsg(this.titaVo));
				baseTransaction.rollBack();
			} catch (JpaSystemException e) {
				this.totaVoList.clear();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				this.totaVoList.add(new LogicException("CE000", "DB 異常 : " + e.getMessage()).getErrorMsg(this.titaVo));
				baseTransaction.rollBack();
			} catch (Throwable e) {
				this.totaVoList.clear();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				this.totaVoList.add(new LogicException("CE000", errors.toString()).getErrorMsg(this.titaVo));
				baseTransaction.rollBack();
			} finally {
				this.txBuffer = mainProcess.getTxBuffer();
			}
		}

		if (!RspFlag) {
			try {
				this.doCs80();
			} catch (LogicException e) {
				this.totaVoList.clear();
				this.totaVoList.add(e.getErrorMsg(this.titaVo));
				baseTransaction.rollBack();
			} catch (Throwable e) {
				this.totaVoList.clear();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				this.totaVoList.add(new LogicException("CE000", errors.toString()).getErrorMsg(this.titaVo));
				baseTransaction.rollBack();
			}
		}

		// tota 處理

		boolean isDone = false;
		try {
			this.info("Tota List : " + this.totaVoList);
			if (!this.titaVo.isEloan())
				tota = jsonConvert.deserializationJsonString(this.titaVo, this.totaVoList.getIfxList());
			else {
				if (this.totaVoList.get(0).isError())
					tota = "[{\"Status\":\"E\", \"ErrMsg\":\"" + this.totaVoList.get(0).getErrorMsg() + "\"}]";
				else
					tota = new ObjectMapper().writeValueAsString(this.totaVoList);

				/* 補ELoan前五位總長 */
				tota = FormatUtil.pad9(Integer.toString(tota.getBytes("UTF-8").length + 5), 5) + tota;
			}
			baseTransaction.commitEnd();
			isDone = true;
		} catch (LogicException e) {
			this.totaVoList.clear();
			this.totaVoList.add(e.getErrorMsg(this.titaVo));

			baseTransaction.rollBackEnd();
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());

			this.totaVoList.clear();
			this.totaVoList.add(new LogicException("CE000", ".tom Not Found!!!").getErrorMsg(this.titaVo));
			baseTransaction.rollBackEnd();
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());

			this.totaVoList.clear();
			this.totaVoList.add(new LogicException("CE000", errors.toString()).getErrorMsg(this.titaVo));
			baseTransaction.rollBackEnd();

		} finally {
			if (!isDone)
				tota = jsonConvert.deserializationJsonString(this.titaVo, this.totaVoList.getIfxList());
		}

		watch.stop();
		this.mustInfo("Total execution time " + watch.getTotalTimeMillis() + " Millisecond");

		if (!this.titaVo.getBatchJobId().isEmpty())
			this.callBatchJob(this.titaVo.getBatchJobId());

		return tota;
	}

	public void callTrade(TitaVo titaVo) {
		this.info("callTrade2...");
		this.baseTransaction.newInit();
		this.titaVo = titaVo;

		/* Time watch */
		StopWatch watch = new StopWatch();
		watch.start();

		try {
			/* mainProcess CS10 */
			// eric 2020.6.25
//				mainProcess.setTitaVo(this.titaVo);
			mainProcess.init(this.titaVo);

			if (!this.titaVo.isTxcdInq())
				mainProcess.cs10Process();
			this.txBuffer = mainProcess.getTxBuffer();

			/* After base check if status true call trade next */
			/* CS80 */
			try {
				this.callAp();
			} catch (LogicException e) {
				this.info(e.getMessage());
				this.totaVoList.clear();
				this.totaVoList.add(e.getErrorMsg(this.titaVo));
				baseTransaction.rollBack();
			} catch (BeansException e) {
				this.info(e.getMessage());
				this.totaVoList.clear();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				this.totaVoList.add(new LogicException("CE000", "APCTL異常或無此核心程式").getErrorMsg(this.titaVo));
				baseTransaction.rollBack();
			} catch (Throwable e) {
				this.info(e.getMessage());
				this.totaVoList.clear();
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				this.totaVoList.add(new LogicException("CE000", errors.toString()).getErrorMsg(this.titaVo));
				baseTransaction.rollBack();
			}
		} catch (LogicException e) {
			this.info(e.getMessage());
			this.totaVoList.clear();
			this.totaVoList.add(e.getErrorMsg(this.titaVo));
			baseTransaction.rollBack();
		} catch (Throwable e) {
			this.totaVoList.clear();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			this.totaVoList.add(new LogicException("CE000", errors.toString()).getErrorMsg(this.titaVo));
			baseTransaction.rollBack();
		} finally {
			this.txBuffer = mainProcess.getTxBuffer();
		}

		try {
			this.doCs80();
			baseTransaction.commitEnd();
		} catch (LogicException e) {
			this.info(e.getMessage());
			this.totaVoList.clear();
			this.totaVoList.add(e.getErrorMsg(this.titaVo));
			baseTransaction.rollBackEnd();
		} catch (Throwable e) {
			this.info(e.getMessage());
			this.totaVoList.clear();
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			this.totaVoList.add(new LogicException("CE000", errors.toString()).getErrorMsg(this.titaVo));
			baseTransaction.rollBackEnd();
		}

		watch.stop();
		this.mustInfo("Total execution time " + watch.getTotalTimeMillis() + " Millisecond");
	}

	private void callAp() throws LogicException, BeansException {
		if (!this.titaVo.isHcodeSendOut() && !this.titaVo.isHcodeReject()) {
			TradeBuffer x = (TradeBuffer) MySpring.getBean(this.titaVo.getTxCode());
			// x.setLoggerFg("1", "com.st1.itx.trade." +
			// this.titaVo.getTxCode().substring(0, 2) + "." + this.titaVo.getTxCode());
			x.setTxBuffer(this.txBuffer);
			this.totaVoList.addAll(x.run(this.titaVo));
			this.txBuffer = x.getTxBuffer();

			// 需主管授權處理
			// Lai
			if (!this.titaVo.isTxcdInq())
				if ((this.titaVo.getActFgI() == 1 && this.titaVo.getRelCodeI() == 2) || ((this.titaVo.getActFgI() == 1 || this.titaVo.getActFgI() == 3) && this.titaVo.getRelCodeI() == 4)) {
					if (this.titaVo.isHcodeNormal() || this.titaVo.isHcodeModify()) {
						TotaVo vo = new TotaVo();
						vo.init(this.titaVo);
						if (this.txBuffer.getTxCom().getSubmitFg() == 0) {
							vo.setWarnMsg("本交易需主管放行");
						} else {
							vo.setWarnMsg("本交易需提交");
						}
						this.totaVoList.add(vo);
					}
				}

			// Eric
			// 需主管授權
			this.info("ApControl.callAp rspList.size = " + this.txBuffer.getRspList().size());
			if (this.txBuffer.getRspList().size() > 0 && (this.titaVo.getSupCode() == null || "".equals(this.titaVo.getSupCode()))) {

				RspFlag = true;

				this.totaVo.init(this.titaVo);
				this.totaVo.setMsgId("EC999");
				for (HashMap<String, String> map : this.txBuffer.getRspList()) {
					String no = map.get("no");
					String msg = map.get("msg");
					this.info("ApControl.callAp rspList = " + no + "/" + msg);
					OccursList occursList = new OccursList();
					occursList.putParam("NO", no);
					occursList.putParam("MSG", msg);

					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);
				}

				//去除重複
				this.totaVo.getOccursList().stream().distinct().collect(Collectors.toList());
				
				this.totaVoList.clear();
				this.totaVoList.add(totaVo);

				baseTransaction.rollBack();

				return;
			}

			// 需輸入交易理由
			this.info("Trade Reason Siz : [" + this.txBuffer.getReasonLi().size() + "]");
			if (this.txBuffer.getReasonLi().size() > 0 && this.titaVo.getReason().isEmpty() && this.titaVo.isActfgEntry() && (this.titaVo.isHcodeNormal() || this.titaVo.isHcodeModify())) {
				RspFlag = true;

				this.totaVo.init(this.titaVo);
				this.totaVo.setMsgId("EC998");
				for (String s : this.txBuffer.getReasonLi()) {
					OccursList occursList = new OccursList();
					occursList.putParam("Reason", s);
					this.totaVo.addOccursList(occursList);
				}
				this.totaVoList.clear();
				this.totaVoList.add(this.totaVo);

				baseTransaction.rollBack();

				return;
			}

			this.Cs70UpDbs();
		} else {
			this.txAmlCom.setTxBuffer(this.txBuffer);
			this.txAmlCom.hcodeSendOut(this.titaVo);
		}
	}

	private void Cs70UpDbs() throws LogicException {
		this.info("Cs70UpDbs = " + this.titaVo.getTxCode());
		this.cs70UpDBS.setTxBuffer(this.txBuffer);
		this.cs70UpDBS.setTitaVo(this.titaVo);
		this.cs70UpDBS.setTotaVoList(this.totaVoList);
		this.cs70UpDBS.exec();

		if (!this.titaVo.isTxcdInq())
			this.doAcEnterCom();
	}

	private void doCs80() throws LogicException {
		this.info("doCs80...");
		this.cs80UpDBS.setTxBuffer(this.txBuffer);
		this.cs80UpDBS.setTitaVo(this.titaVo);
		this.cs80UpDBS.setTotaVoList(this.totaVoList);
		this.cs80UpDBS.exec();
	}

	private boolean stringToVo(String tita) {
		try {
			/* String to JSON */

			if (tita.indexOf("\"TLRNO\":\"E-LOAN\"") != -1)
				this.titaVo = this.eloanConver.exec(tita);
			else {
				this.titaVo = this.titaVo.getVo(tita);

				if ("XXR99".equals(this.titaVo.getTxCode()))
					this.titaVo.remove("IP");

				this.titaVo.getBodyLenAndAdd();
			}
			return true;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());

			this.totaVoList.add(new LogicException("CE000", "JSON轉換失敗").getErrorMsg(this.titaVo));
			return false;
		}
	}

	private void doAcEnterCom() throws LogicException {
		this.info("Cs70UpDbs.DoAcEnter..." + this.titaVo.getEntDyI() + "/" + this.titaVo.getOrgEntdyI());

		txToDoCom.setTxBuffer(this.txBuffer);
		txToDoCom.run(this.titaVo);

		TxCom txCom = this.txBuffer.getTxCom();

		int AcCnt = 0;

		if (this.txBuffer.getAcDetailList() == null || this.txBuffer.getAcDetailList().size() == 0) {
			AcCnt = this.txBuffer.getTxCom().getAcCnt();
		} else {
			AcCnt = this.txBuffer.getAcDetailList().size();
		}

		this.info("Cs70UpDbs.AcCnt=" + AcCnt);

		if (AcCnt == 0) {
			return;
		}

		if (this.titaVo.isHcodeModify())
			txCom.setBookAcHcode(0);
		if (this.titaVo.isHcodeNormal())
			txCom.setBookAcHcode(0);
		if (this.titaVo.isHcodeErase()) {
			if (this.titaVo.getEntDyI() != this.titaVo.getOrgEntdyI()) {
				txCom.setBookAcHcode(2);
			} else {
				txCom.setBookAcHcode(1);
			}
		}

		this.txBuffer.setTxCom(txCom);
		this.acEnterCom.setTxBuffer(this.txBuffer);
		this.totaVoList.addAll(this.acEnterCom.run(this.titaVo));
	}

	public TotaVoList getTotaVoList() {
		return this.totaVoList;
	}

	public void setTotaVoList(TotaVoList totaVoList) {
		this.totaVoList = totaVoList;
	}

	private void callBatchJob(String jobId) {
		this.info("Job List : " + jobId);
		if (jobId.split(";").length > 1) {
			String jobIds[] = jobId.split(";");
			for (String job : jobIds) {
				JobParameters params = new JobParametersBuilder().addDate(ContentName.batchDate, new Date()).addString(ContentName.jobId, job).addString(ContentName.tlrno, this.titaVo.getTlrNo())
						.addString("excuteMode", "0").addString(ContentName.dataBase, this.titaVo.getDataBase()).addString(ContentName.parent, this.titaVo.getTxcd())
						.addString(ContentName.loggerFg, ThreadVariable.isLogger() + "").toJobParameters();
				MySpring.jobLaunch(job, params);
			}
		} else {
			JobParameters params = new JobParametersBuilder().addDate(ContentName.batchDate, new Date()).addString(ContentName.jobId, jobId).addString(ContentName.tlrno, this.titaVo.getTlrNo())
					.addString("excuteMode", "0").addString(ContentName.dataBase, this.titaVo.getDataBase()).addString(ContentName.parent, this.titaVo.getTxcd())
					.addString(ContentName.loggerFg, ThreadVariable.isLogger() + "").toJobParameters();
			MySpring.jobLaunch(jobId, params);
		}
	}
}
