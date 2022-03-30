/*製作檔案放入inbox,for中心FTP不知甚麼原因無法做到*/
package com.ifx.fmt;

import java.io.Serializable;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "filename", "filedata" })
public class FormCS004 implements Serializable {
	private static final long serialVersionUID = 1867782388557547652L;

	// @Cobol("X,4")
	// String brno;

	@Cobol("X,50")
	String filename;

	@Cobol("X,10000")
	String filedata;

	public String getFilename() {
		return filename;
	}

	public String getFiledata() {
		return filedata;
	}

	// 檔案名稱
	public void setFilename(String filename) {
		// this.filename = filename.replaceAll("\\s+$","").replaceAll("\0+$","");
		this.filename = filename;
	}

	// 檔案內容
	public void setFiledata(String filedata) {
		// this.filedata = filedata.replaceAll("\\s+$","").replaceAll("\0+$","");
		this.filedata = filedata;
	}

	@Override
	public String toString() {
		return "FormCS004 [ filename=" + filename + "filedata=" + filedata + "]";
	}

}
