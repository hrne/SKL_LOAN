package com.st1.util.cbl;

@FieldList({ "tag", "action" })
public class MyLineHeader {
	@Cobol("X,15")
	private String tag;
	@Cobol("X,1")
	private String action;

	@Override
	public String toString() {
		return "MyLineHeader [tag=" + tag + ", action=" + action + "]";
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
