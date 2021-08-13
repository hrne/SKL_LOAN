package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ReltFamily;
import com.st1.itx.db.domain.ReltFamilyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ReltFamilyRepository extends JpaRepository<ReltFamily, ReltFamilyId> {

	// ReltUKey =
	public Slice<ReltFamily> findAllByReltUKeyIsOrderByReltSeqAsc(String reltUKey_0, Pageable pageable);

	// ReltUKey =
	public Optional<ReltFamily> findTopByReltUKeyIsOrderByReltSeqDesc(String reltUKey_0);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ReltFamily> findByReltFamilyId(ReltFamilyId reltFamilyId);

}
