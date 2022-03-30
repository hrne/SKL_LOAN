package com.ifx.fmt;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.domain.Ticker;
import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.CobolProcessor;
import com.st1.util.cbl.FieldList;

@FieldList({ "brno", "tcikno", "vdate", "vtime", "text" })
public class FormCS001 implements Serializable {
	static final Logger logger = LoggerFactory.getLogger(FormCS001.class);
	private static final long serialVersionUID = 1867782388557547652L;

	@Cobol("X,4")
	String brno;

	@Cobol("X,5")
	String tcikno;

	@Cobol("X,8")
	String vdate;

	@Cobol("X,4")
	String vtime;

	@Cobol("X,200")
	String text;

	@Cobol("X,15")
	String id;

	public Ticker toTicker() {
		logger.info("in toTicker!");
		Ticker ticker = new Ticker();
		logger.info("in toTicker!-1");
		ticker.setBrno(brno);
		logger.info("in toTicker!-2");
		ticker.setTickno(tcikno);
		logger.info("in toTicker!-2-2");
		ticker.setStopTime(Long.parseLong(vdate + vtime));
		logger.info("in toTicker!-3");
		ticker.setContent(text.trim());
		logger.info("in toTicker!-4");
		return ticker;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getTcikno() {
		return tcikno;
	}

	public void setTcikno(String tcikno) {
		this.tcikno = tcikno;
	}

	public String getVdate() {
		return vdate;
	}

	public void setVdate(String vdate) {
		this.vdate = vdate;
	}

	public String getVtime() {
		return vtime;
	}

	public void setVtime(String vtime) {
		this.vtime = vtime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "FormCS001 [brno=" + brno + ", tickno=" + tcikno + ", vdate=" + vdate + ", vtime=" + vtime + ", text="
				+ text + "]";
	}

	public static void main(String[] args) {
		String one = "505020201403111756";
		StringBuilder sb = new StringBuilder();
		sb.append(one);
		for (int i = 0; i < 20; i++)
			sb.append("abcdefghi=");
		FormCS001 form = new FormCS001();
		try {
			CobolProcessor.parse(sb.toString(), form);

		} catch (Exception e) {
			;
		}
	}

}
