package com.st1.ifx.hcomm.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.ifx.domain.Journal;
import com.st1.ifx.service.JournalService;

@Component
@Scope("prototype")
public class SimpleJournal {
	static final Logger logger = LoggerFactory.getLogger(SimpleJournal.class);
	@Autowired
	private JournalService jnlService;
	private Journal jnl = null;

	String entDay;
	String brn;
	String tlrno;
	String txno;
	String txcode;
	String supno = null;
	String level = " ";
	long jnlId = -1;

	public void prelogJournal(String tita) {

		jnl = new Journal();

		jnl.setBusdate(entDay);
		jnl.setBrn(brn);
		jnl.setTlrno(tlrno);
		jnl.setTxno(txno);
		jnl.setTxcode(txcode);
		jnl.setTranStatus(-1);
		jnl.setTita(tita);
		// jnl.setTitaResv(resv); // 不需要處理resv
		jnl.setSupno(supno);
		jnl.setLvel(level);
		jnl.setSendTimes(1);

		jnl.touch();
		jnl = this.jnlService.save(jnl);
		jnlId = jnl.getId();
		logger.info("p jnl id:" + jnlId);
	}

	public void updateJournal(boolean success, String msgid, String errmsg, String mrkey) {

		if (this.jnl == null) {
			jnl = jnlService.get(this.jnlId);
		}
		if (success) {
			jnl.setTranStatus(100);
		} else {
			jnl.setTranStatus(1);
		}
		// imsloginsock 最後有再設定這兩項,故需要更新
		// xx003 好像沒有序號的概念
		jnl.setBusdate(entDay);
		// jnl.setTxno(txno);

		jnl.setMsgid(msgid);
		jnl.setMrkey(mrkey);
		jnl.setErrmsg(errmsg);
		jnl.touch();
		jnlService.save(jnl);
		logger.info("u jnl id:" + jnlId);

	}

	public void writeJournal(String msgid) {
		jnl = new Journal();

		jnl.setBusdate(entDay);
		jnl.setBrn(brn);
		jnl.setTlrno(tlrno);
		jnl.setTxno(txno);
		jnl.setTxcode(msgid); // msgid txcode
		jnl.setTranStatus(-1);
		// jnl.setTita(tita);
		// jnl.setTitaResv(resv); // 不需要處理resv
		// jnl.setSupno(supno);
		jnl.setSendTimes(1);

		jnl.setTranStatus(100);
		jnl.setBusdate(entDay);

		jnl.setMsgid(msgid);
		jnl.touch();
		jnlService.save(jnl);
		jnlId = jnl.getId();
		logger.info("w jnl id:" + jnlId);
	}

	public String getEntDay() {
		return entDay;
	}

	public void setEntDay(String entDay) {
		this.entDay = entDay;
	}

	public String getBrn() {
		return brn;
	}

	public void setBrn(String brn) {
		this.brn = brn;
	}

	public String getTlrno() {
		return tlrno;
	}

	public void setTlrno(String tlrno) {
		this.tlrno = tlrno;
	}

	public String getTxno() {
		return txno;
	}

	public void setTxno(String txno) {
		this.txno = txno;
	}

	public String getTxcode() {
		return txcode;
	}

	public void setTxcode(String txcode) {
		this.txcode = txcode;
	}

	public String getSupno() {
		return supno;
	}

	public void setSupno(String supno) {
		this.supno = supno;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public long getJnlId() {
		return jnlId;
	}

	public void setJnlId(long jnlId) {
		this.jnlId = jnlId;
	}

}
