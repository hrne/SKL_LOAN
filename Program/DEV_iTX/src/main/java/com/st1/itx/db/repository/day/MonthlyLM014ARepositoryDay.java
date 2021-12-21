package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM014A;
import com.st1.itx.db.domain.MonthlyLM014AId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM014ARepositoryDay extends JpaRepository<MonthlyLM014A, MonthlyLM014AId> {

	// DataYM =
	public Slice<MonthlyLM014A> findAllByDataYMIs(int dataYM_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<MonthlyLM014A> findByMonthlyLM014AId(MonthlyLM014AId monthlyLM014AId);

}
