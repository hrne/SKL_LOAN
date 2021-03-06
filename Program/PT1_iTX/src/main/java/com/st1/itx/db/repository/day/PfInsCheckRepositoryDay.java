package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfInsCheck;
import com.st1.itx.db.domain.PfInsCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfInsCheckRepositoryDay extends JpaRepository<PfInsCheck, PfInsCheckId> {

	// CheckWorkMonth= ,AND Kind=
	public Slice<PfInsCheck> findAllByCheckWorkMonthIsAndKindIsOrderByCustNoAscFacmNoAsc(int checkWorkMonth_0, int kind_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<PfInsCheck> findByPfInsCheckId(PfInsCheckId pfInsCheckId);

}
