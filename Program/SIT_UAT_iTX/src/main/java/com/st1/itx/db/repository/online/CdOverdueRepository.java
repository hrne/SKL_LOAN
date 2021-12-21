package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdOverdue;
import com.st1.itx.db.domain.CdOverdueId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdOverdueRepository extends JpaRepository<CdOverdue, CdOverdueId> {

	// OverdueSign =
	public Slice<CdOverdue> findAllByOverdueSignIs(String overdueSign_0, Pageable pageable);

	// OverdueCode =
	public Slice<CdOverdue> findAllByOverdueCodeIs(String overdueCode_0, Pageable pageable);

	// Enable =
	public Slice<CdOverdue> findAllByEnableIs(String enable_0, Pageable pageable);

	// OverdueSign >= ,AND OverdueSign <= ,AND OverdueCode >= ,AND OverdueCode <=
	public Slice<CdOverdue> findAllByOverdueSignGreaterThanEqualAndOverdueSignLessThanEqualAndOverdueCodeGreaterThanEqualAndOverdueCodeLessThanEqualOrderByOverdueSignAscOverdueCodeAsc(
			String overdueSign_0, String overdueSign_1, String overdueCode_2, String overdueCode_3, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdOverdue> findByCdOverdueId(CdOverdueId cdOverdueId);

}
