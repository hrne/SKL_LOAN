package com.st1.ifx.service;

import org.springframework.data.domain.Page;

import com.st1.ifx.domain.ConsoleLog;

public interface ConsoleLogService {
	public void save(String orig, String brno, String level, String text);

	public Page<ConsoleLog> getConsoleLog(String brno, java.sql.Date date, Integer pageNumber);
}
