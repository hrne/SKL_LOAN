package com.st1.ifx.file.item.sbctl;

import com.st1.ifx.domain.Sbctl;
import com.st1.ifx.file.item.LineHeader;
import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "header", "sbctl" })
public class SbctlLine {

	@Cobol
	private LineHeader header = new LineHeader();
	@Cobol
	private Sbctl sbctl = new Sbctl();

	public LineHeader getHeader() {
		return header;
	}

	public void setHeader(LineHeader header) {
		this.header = header;
	}

	public Sbctl getSbctl() {
		if (sbctl != null && sbctl.getTlrno() != null) {
			sbctl.setTlrno(sbctl.getTlrno().trim());
		}
		return sbctl;
	}

	public void setSbctl(Sbctl sbctl) {
		this.sbctl = sbctl;
	}

	@Override
	public String toString() {
		return "SbctlLine [header=" + header + ", sbctl=" + sbctl + "]";
	}

}
