/*Swift 自動來電列印電文*/
package com.ifx.fmt;

import java.io.Serializable;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "swifttota" })
public class FormCS003 implements Serializable {
	private static final long serialVersionUID = 1867782388557547652L;

	@Cobol("X,12000")
	String swifttota;

	public String getSwifttota() {
		return swifttota;
	}

	public void setSwifttota(String swifttota) {
		// 1個mq,1個電文,故可右邊去空白
		this.swifttota = swifttota.replaceAll("\\s+$", "").replaceAll("\0+$", "");
	}

	@Override
	public String toString() {
		return "FormCS003 [swifttota=" + swifttota + "]";
	}

}
