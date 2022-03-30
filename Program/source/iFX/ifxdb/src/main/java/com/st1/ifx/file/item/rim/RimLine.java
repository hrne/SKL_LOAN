package com.st1.ifx.file.item.rim;

import java.io.Serializable;
import com.st1.ifx.domain.LocalRim;
import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "tableName", "updateFlag", "key", "data" })
public class RimLine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6882707079461380323L;

	@Cobol("X,8")
	String tableName;

	@Cobol("X,1")
	String updateFlag;

	@Cobol("X,20")
	String key;

	@Cobol("X,5000")
	String data;

	public LocalRim toLocalRim() {
		LocalRim r = new LocalRim();
		r.setTableName(tableName);
		r.setXey(key);
		// 已在hosttran中 依照8+1+20長度去除右邊多餘欄位
		if (data.endsWith("$")) {
			r.setData(data.substring(0, data.length() - 1));
		} else
			r.setData(data);
		return r;
	}

	public boolean isDeleteTable() {
		return this.updateFlag.equals("9");
	}

	public boolean isDeleteRecord() {
		return this.updateFlag.equals("3");
	}

	public boolean isinsertupdate() {
		return this.updateFlag.equals("5");
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RimLine [tableName=" + tableName + "updateFlag=" + updateFlag + "key=" + key + "data=" + data + "]";
	}
}
