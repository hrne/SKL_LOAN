package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdVarValue;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdVarValueRepositoryMon extends JpaRepository<CdVarValue, Integer> {

	// YearMonth >= ,AND YearMonth <=
	public Slice<CdVarValue> findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(int yearMonth_0, int yearMonth_1, Pageable pageable);

	// YearMonth <=
	public Optional<CdVarValue> findTopByYearMonthLessThanEqualOrderByYearMonthDesc(int yearMonth_0);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdVarValue> findByYearMonth(int yearMonth);

}
