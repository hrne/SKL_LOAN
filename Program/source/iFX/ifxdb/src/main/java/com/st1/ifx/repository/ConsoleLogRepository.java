package com.st1.ifx.repository;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.st1.ifx.domain.ConsoleLog;

public interface ConsoleLogRepository extends PagingAndSortingRepository<ConsoleLog, Long> {
	Page<ConsoleLog> findByBrnoAndDate(String brno, Date date, Pageable pageable);
}
