package com.st1.ifx.file.item;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "tag", "action" })
public class LineHeader {
	@Cobol("X,8")
	private String tag;
	@Cobol("9,1")
	private int action;
	public static final int ACTION_MERGE = 0;
	public static final int ACTION_DELETE = 1;
	public static final int ACTION_REMOVE_ALL = 9;

	@Override
	public String toString() {
		return "LineHeader [tag=" + tag + ", action=" + action + "]";
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}
