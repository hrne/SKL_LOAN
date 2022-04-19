package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdCashFlow;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCashFlowRepositoryDay extends JpaRepository<CdCashFlow, Integer> {

	// DataYearMonth >= ,AND DataYearMonth <=
	public Slice<CdCashFlow> findAllByDataYearMonthGreaterThanEqualAndDataYearMonthLessThanEqualOrderByDataYearMonthAsc(int dataYearMonth_0, int dataYearMonth_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdCashFlow> findByDataYearMonth(int dataYearMonth);

}
