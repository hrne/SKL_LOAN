package com.st1.ifx.file.excel.item.help;

public interface HelpDefProcessor<T> {
	T process(HelpDef def) throws Exception;
}
