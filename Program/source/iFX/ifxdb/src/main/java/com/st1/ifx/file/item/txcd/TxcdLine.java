package com.st1.ifx.file.item.txcd;

import com.st1.ifx.domain.Txcd;
import com.st1.ifx.file.item.LineHeader;
import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "header", "txcd" })
public class TxcdLine {
	@Cobol
	private LineHeader header = new LineHeader();
	@Cobol
	private Txcd txcd = new Txcd();

	public LineHeader getHeader() {
		return header;
	}

	public void setHeader(LineHeader header) {
		this.header = header;
	}

	public Txcd getTxcd() {
		return txcd;
	}

	public void setTxcd(Txcd txcd) {
		this.txcd = txcd;
	}

	@Override
	public String toString() {
		return "TxcdFile [header=" + header + ", txcd=" + txcd + "]";
	}

}
