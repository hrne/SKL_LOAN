package com.st1.ifx.file.item.general;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.ifx.domain.CodeList;
import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "help", "id", "opfg", "disp", "ki", "content" })
public class GeneralHLine implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(GeneralHLine.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 6976725456744071897L;

	/**
	 * 
	 */

	@Cobol("X,30")
	String help;

	@Cobol("X,30")
	String id; // segment

	@Cobol("X,1")
	String opfg = "";

	@Cobol("X,5")
	String disp = "";

	@Cobol("X,20")
	String ki = "";

	@Cobol("X,300")
	String content = "";

	public CodeList toCodeList() {
		CodeList r = new CodeList();
		r.setHelp(help.trim());
		r.setSegment(id.trim());
		r.setXey(ki.trim());
		if (disp == "00000" || disp.trim() == "") {
			r.setDisplayOrder(0);
		} else {
			r.setDisplayOrder(Integer.parseInt(disp));
		}
		logger.info("help:" + help);
		logger.info("id:" + id);
		logger.info("opfg:" + opfg);
		logger.info("ki:" + ki);
		logger.info("content:" + content.trim());
		logger.info("disp:" + disp);
		if (content.endsWith("$")) {
			r.setContent(content.trim().substring(0, content.trim().length() - 1));
		} else
			r.setContent(content.trim());
		return r;
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

	public String getDisp() {
		return disp;
	}

	public void setDisp(String disp) {
		this.disp = disp;
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

	public boolean isDeleteRecord() {
		return this.opfg.equals("3");
	}

	public boolean isinsertupdate() {
		return this.opfg.equals("5");
	}

	public boolean isDeleteSegment() {
		// TODO Auto-generated method stub
		return this.opfg.equals("7");
	}
}
