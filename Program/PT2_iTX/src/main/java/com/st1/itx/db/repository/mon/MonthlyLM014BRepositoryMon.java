package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM014B;
import com.st1.itx.db.domain.MonthlyLM014BId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM014BRepositoryMon extends JpaRepository<MonthlyLM014B, MonthlyLM014BId> {

	// DataYM =
	public Slice<MonthlyLM014B> findAllByDataYMIs(int dataYM_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<MonthlyLM014B> findByMonthlyLM014BId(MonthlyLM014BId monthlyLM014BId);

}
