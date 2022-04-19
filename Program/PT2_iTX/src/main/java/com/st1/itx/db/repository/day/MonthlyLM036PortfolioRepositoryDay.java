package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM036Portfolio;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM036PortfolioRepositoryDay extends JpaRepository<MonthlyLM036Portfolio, Integer> {

	// DataMonth >= , AND DataMonth <=
	public Slice<MonthlyLM036Portfolio> findAllByDataMonthGreaterThanEqualAndDataMonthLessThanEqualOrderByDataMonthAsc(int dataMonth_0, int dataMonth_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<MonthlyLM036Portfolio> findByDataMonth(int dataMonth);

	// 維護 MonthlyLM036Portfolio LM036Portfolio
	@Procedure(value = "\"Usp_L9_MonthlyLM036Portfolio_Ins\"")
	public void uspL9Monthlylm036portfolioIns(int TBSDYF, String EmpNo);

}
