package com.st1.itx.tradeService;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.db.transaction.BatchTransaction;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.format.FormatUtil;

/**
 * TradeBuffer
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
public abstract class TradeBuffer implements TradeIn {
	private Logger logger;

//	@Autowired
	public TxBuffer txBuffer;

	@Autowired
	public TotaVo totaVo;

	public BatchTransaction batchTransaction;

	public int index = 0;

	public int limit = 500;

	private boolean loggerFg = false;

	private TotaVoList totaList = new TotaVoList();

	public TradeBuffer() {
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
			logger = LoggerFactory.getLogger(enclosingClass.getName());
		else
			logger = LoggerFactory.getLogger(getClass().getName());

		loggerFg = ThreadVariable.isLogger();
	}

	public TxBuffer getTxBuffer() {
		return this.txBuffer;
	}

	public void setTxBuffer(TxBuffer txBuffer) {
		this.txBuffer = txBuffer;
	}

	public boolean getLoggerFg() {
		return loggerFg;
	}

	public void setLoggerFg(String loggerFg) {
		this.loggerFg = loggerFg != null && loggerFg.equals("1") ? true : false;
	}

	public void setLoggerFg(String loggerFg, String name) {
		if (logger == null && name != null)
			logger = LoggerFactory.getLogger(name);
		this.loggerFg = loggerFg != null && loggerFg.equals("1") ? true : false;
	}

	public void addList(TotaVo totaVo) {
		this.totaList.add((TotaVo) totaVo.clone());
	}

	public void addAllList(ArrayList<TotaVo> totaList) {
		this.totaList.addAll(totaList);
	}

	public ArrayList<TotaVo> sendList() {
		return this.totaList;
	}

	public void mustInfo(String msg) {
		logger.info(FormatUtil.padX(ThreadVariable.getEmpNot(), 10) + msg);
	}

	public void info(String msg) {
		if (ThreadVariable.isLogger())
			logger.info(FormatUtil.padX(ThreadVariable.getEmpNot(), 10) + msg);
	}

	public void warn(String msg) {
		if (ThreadVariable.isLogger())
			logger.warn(FormatUtil.padX(ThreadVariable.getEmpNot(), 10) + msg);
	}

	public void error(String msg) {
		logger.error(FormatUtil.padX(ThreadVariable.getEmpNot(), 10) + msg);
	}

	public int setIndexNext() {
		this.index++;
		return this.index;
	}

	public BatchTransaction getBatchTransaction() {
		return batchTransaction;
	}

	public void setBatchTransaction(BatchTransaction batchTransaction) {
		this.batchTransaction = batchTransaction;
	}

	@Override
	public abstract ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException;

}