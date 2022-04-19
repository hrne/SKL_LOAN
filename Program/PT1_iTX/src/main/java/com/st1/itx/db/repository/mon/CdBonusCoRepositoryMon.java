package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdBonusCo;
import com.st1.itx.db.domain.CdBonusCoId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBonusCoRepositoryMon extends JpaRepository<CdBonusCo, CdBonusCoId> {

	// WorkMonth= ,AND ConditionCode = ,AND Condition >= ,AND Condition <=
	public Slice<CdBonusCo> findAllByWorkMonthIsAndConditionCodeIsAndConditionGreaterThanEqualAndConditionLessThanEqualOrderByWorkMonthAscConditionCodeAscConditionAsc(int workMonth_0,
			int conditionCode_1, String condition_2, String condition_3, Pageable pageable);

	// WorkMonth>= ,AND WorkMonth<=
	public Slice<CdBonusCo> findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAsc(int workMonth_0, int workMonth_1, Pageable pageable);

	// WorkMonth<=
	public Optional<CdBonusCo> findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(int workMonth_0);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdBonusCo> findByCdBonusCoId(CdBonusCoId cdBonusCoId);

}
