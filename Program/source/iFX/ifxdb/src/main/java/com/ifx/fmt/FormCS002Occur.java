package com.ifx.fmt;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "brno", "tlrno" })
public class FormCS002Occur {
	public boolean isValid() {
		return !isNullOrEmpty(brno) && !brno.equals("0000") && !isNullOrEmpty(tlrno) && !tlrno.equals("  ");
	}

	private boolean isNullOrEmpty(String s) {
		return s == null || s.length() == 0;
	}

	@Cobol("X,4")
	String brno;

	@Cobol("X,2")
	String tlrno;

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getTlrno() {
		return tlrno;
	}

	public void setTlrno(String tlrno) {
		this.tlrno = tlrno;
	}

	@Override
	public String toString() {
		return "FormCS002Occur [brno=" + brno + ", tlrno=" + tlrno + "]";
	}
}
