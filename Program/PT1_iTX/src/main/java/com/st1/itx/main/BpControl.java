package com.st1.itx.main;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.db.transaction.BaseTransaction;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.maintain.Cs80UpDBS;
import com.st1.itx.maintain.MainProcess;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.JsonConvert;
import com.st1.itx.util.MySpring;

@Controller("bpControl")
@Scope("prototype")
public class BpControl extends CommBuffer implements Runnable {
	@Autowired
	public JsonConvert jsonConvert;

	@Autowired
	public BaseTransaction baseTransaction;

	@Autowired
	public TitaVo titaVo;

	@Autowired
	public MainProcess mainProcess;

	@Autowired
	public Cs80UpDBS cs80UpDBS;

	private TxBuffer txBuffer;

	private TotaVoList totaVoList = new TotaVoList();

	@PostConstruct
	public void init() {
		this.info("apControl init...");
	}

	@Override
	public void run() {
		try {
			if (this.getLoggerFg())
				ThreadVariable.setObject(ContentName.loggerFg, true);
			else
				ThreadVariable.setObject(ContentName.loggerFg, false);
			this.exec();
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
	}

	@Override
	public void exec() throws LogicException {
		try {
			this.callTrade();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
	}

	public void callTrade() throws Exception {
		boolean status = true;

		/* Time watch */
		StopWatch watch = new StopWatch();
		watch.start();

		try {
			/* mainProcess CS10 */
			mainProcess.setTitaVo(this.titaVo);
			if (!this.titaVo.getTxcd().equals("XX003"))
				mainProcess.cs10Process();
			this.txBuffer = mainProcess.getTxBuffer();

			/* After base check if status true call trade next */
			/* CS80 */
			try {
				this.callAp();

				this.doCs80();
//				mainProcess.mtRecord();
			} catch (LogicException e) {
				this.totaVoList.clear();
				this.totaVoList.add(e.getErrorMsg(this.titaVo));
				status = !status;
			} catch (BeansException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				this.totaVoList.add(new LogicException("CE000", "APCTL異常或無此核心程式").getErrorMsg(this.titaVo));
				status = !status;
			} catch (Throwable e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
				this.totaVoList.add(new LogicException("CE000", errors.toString()).getErrorMsg(this.titaVo));
				status = !status;
			}
		} catch (LogicException e) {
			this.totaVoList.add(e.getErrorMsg(this.titaVo));
			status = !status;
		} finally {
			if (status)
				this.baseTransaction.commitEnd();
			else
				this.baseTransaction.rollBackEnd();
		}

		watch.stop();
		this.info("Total execution time " + watch.getTotalTimeMillis() + " Millisecond");
	}

	private void callAp() throws LogicException, BeansException, DBException {
		TradeBuffer x = (TradeBuffer) MySpring.getBean(this.titaVo.getTxCode());
		x.setLoggerFg("1", "com.st1.itx.trade." + this.titaVo.getTxCode().substring(0, 2) + "." + this.titaVo.getTxCode());
		x.setTxBuffer(this.txBuffer);
		this.info(this.titaVo.toString());
		this.totaVoList.addAll(x.run(this.titaVo));

		this.txBuffer = x.getTxBuffer();
	}

	private void doCs80() throws LogicException, DBException {
		this.info("doCs80...");
		this.cs80UpDBS.setTxBuffer(this.txBuffer);
		this.cs80UpDBS.setTitaVo(this.titaVo);
		this.cs80UpDBS.setTotaVoList(this.totaVoList);
		this.cs80UpDBS.exec();
	}

}
