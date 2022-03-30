package com.st1.ifx.file.item.general;

import java.io.Serializable;
import java.util.Locale;

import com.st1.ifx.domain.CodeList;
import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "help", "opfg", "ki", "content" })
public class GeneralLine implements Serializable {
	private static final long serialVersionUID = 5764920515370625991L;

	@Cobol("X,30")
	String help;

	String id; // segment

	@Cobol("X,1")
	String opfg = "";

	@Cobol("X,20")
	String ki = "";

	@Cobol("X,300")
	String content = "";

	int displayOrder = -1;

	public CodeList toCodeList() {
		CodeList r = new CodeList();
		r.setHelp(help);
		r.setSegment(id);
		r.setXey(ki);
		if (content.endsWith("$")) {
			r.setContent(content.substring(0, content.length() - 1));
		} else
			r.setContent(content);
		return r;
	}

	public static GeneralLine makeDeleteAll(String help) {
		GeneralLine g = new GeneralLine();
		g.help = help;
		g.opfg = "9";
		return g;
	}

	public static GeneralLine makeHead(String help, String[] names) {
		GeneralLine g = new GeneralLine();
		String head = "HEAD";
		g.help = help;
		g.ki = head;
		g.content = join(names, ",");
		return g;
	}

	public static GeneralLine makeData(String help, String ki, String[] values) {
		GeneralLine g = new GeneralLine();
		g.help = help;
		g.ki = ki;
		g.content = join(values, ",");
		return g;
	}

	private static String join(String r[], String d) {
		if (r.length == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		int i;
		for (i = 0; i < r.length - 1; i++)
			sb.append(r[i] + d);
		return sb.toString() + r[i];
	}

	/***
	 * 判斷是否為指令行 因應吃檔案時有可能key空白，但又不是CommandLine， 新增key是空但是opfg不是空 才等於CommandLine
	 * 
	 * @return
	 */
	public boolean isCommandLine() {
		return (ki.isEmpty() && !opfg.isEmpty());
	}

	public boolean isHead() {
		return ki != null && ki.toUpperCase(Locale.TAIWAN).equals("HEAD");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpfg() {
		return opfg;
	}

	public void setOpfg(String opfg) {
		this.opfg = opfg;
	}

	public String getKi() {
		return ki;
	}

	public void setKi(String ki) {
		this.ki = ki;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	public boolean isDeleteRecord() {
		return this.opfg.equals("3");
	}

	public boolean isinsertupdate() {
		return this.opfg.equals("5");
	}

	@Override
	public String toString() {
		return "GeneralLine [help=" + help + ", id=" + id + ", opfg=" + opfg + ", ki=" + ki + ", content=" + content
				+ ", displayOrder=" + displayOrder + "]";
	}

}
