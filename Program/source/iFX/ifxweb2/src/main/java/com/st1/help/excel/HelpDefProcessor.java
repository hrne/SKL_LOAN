package com.st1.help.excel;

public interface HelpDefProcessor {
	void process(HelpDef def) throws Exception;

	String output() throws Exception;
}
