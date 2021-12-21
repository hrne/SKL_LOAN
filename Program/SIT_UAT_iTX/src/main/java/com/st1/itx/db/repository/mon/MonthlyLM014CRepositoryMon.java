package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM014C;
import com.st1.itx.db.domain.MonthlyLM014CId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM014CRepositoryMon extends JpaRepository<MonthlyLM014C, MonthlyLM014CId> {

	// DataYM =
	public Slice<MonthlyLM014C> findAllByDataYMIs(int dataYM_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<MonthlyLM014C> findByMonthlyLM014CId(MonthlyLM014CId monthlyLM014CId);

}
