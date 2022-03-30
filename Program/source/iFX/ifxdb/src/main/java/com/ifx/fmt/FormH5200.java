package com.ifx.fmt;

import java.io.Serializable;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "brno", "ftbsdy", "msgtyp", "osn", "rcvsta", "tobox", "curbox", "text", "errsrn" })
public class FormH5200 implements Serializable {
	private static final long serialVersionUID = -9126844557778586280L;

	@Cobol("9,4")
	String brno;

	@Cobol("9,8")
	String ftbsdy;

	@Cobol("X,4")
	String msgtyp;

	@Cobol("9,6")
	String osn;

	@Cobol("9,2")
	String rcvsta;

	@Cobol("9,2")
	String tobox;

	@Cobol("9,2")
	String curbox;

	@Cobol("X,1000")
	String text;

	@Cobol("X,6")
	String errsrn;

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getFtbsdy() {
		return ftbsdy;
	}

	public void setFtbsdy(String ftbsdy) {
		this.ftbsdy = ftbsdy;
	}

	public String getMsgtyp() {
		return msgtyp;
	}

	public void setMsgtyp(String msgtyp) {
		this.msgtyp = msgtyp;
	}

	public String getOsn() {
		return osn;
	}

	public void setOsn(String osn) {
		this.osn = osn;
	}

	public String getRcvsta() {
		return rcvsta;
	}

	public void setRcvsta(String rcvsta) {
		this.rcvsta = rcvsta;
	}

	public String getTobox() {
		return tobox;
	}

	public void setTobox(String tobox) {
		this.tobox = tobox;
	}

	public String getCurbox() {
		return curbox;
	}

	public void setCurbox(String curbox) {
		this.curbox = curbox;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getErrsrn() {
		return errsrn;
	}

	public void setErrsrn(String errsrn) {
		this.errsrn = errsrn;
	}

	@Override
	public String toString() {
		return "FormH5200 [brno=" + brno + ", ftbsdy=" + ftbsdy + ", msgtyp=" + msgtyp + ", osn=" + osn + ", rcvsta="
				+ rcvsta + ", tobox=" + tobox + ", curbox=" + curbox + ", text=" + text + ", errsrn=" + errsrn + "]";
	}

}