package com.st1.ifx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.st1.ifx.domain.Journal;

public interface JournalRepository extends JpaRepository<Journal, Long> {
	List<Journal> findByBusdateAndBrnAndTlrnoOrderByTxnoDesc(String busdate, String brn, String tlrno);

	List<Journal> findByBusdateAndBrnAndTlrnoAndTxno(String busdate, String brn, String tlrno, String txno);

	List<Journal> findByCalDayAndBrnAndTlrnoAndTxno(int calDay, String brn, String tlrno, String txno);
}
