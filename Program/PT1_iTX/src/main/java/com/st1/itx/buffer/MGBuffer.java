package com.st1.itx.buffer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.dataVO.MgCurr;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.util.log.SysLogger;

/**
 * MGBuffer
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Component("mgBuffer")
@Scope("singleton")
public class MGBuffer extends SysLogger {
	private TxBizDate txBizDate;

	private TxBizDate nTxBizDate;

	private MgCurr mgCurr;

	public TxBizDate getTxBizDate() {
		return this.txBizDate;
	}

	public void setTxBizDate(TxBizDate txBizDate) {
		this.info("set TxBizDate....");
		this.info(txBizDate.toString());
		this.txBizDate = txBizDate;
	}

	public TxBizDate getnTxBizDate() {
		return nTxBizDate;
	}

	public void setnTxBizDate(TxBizDate nTxBizDate) {
		this.info("set nTxBizDate...");
		this.info(nTxBizDate.toString());
		this.nTxBizDate = nTxBizDate;
	}

	public MgCurr getMgCurr() {
		return this.mgCurr;
	}

	public void setMgCurr(MgCurr mgCurr) {
		this.info("set MgCurr....");
		this.info(mgCurr.toString());
		this.mgCurr = (MgCurr) mgCurr.clone();
	}
}
