package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.RelsFamily;
import com.st1.itx.db.domain.RelsFamilyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RelsFamilyRepositoryMon extends JpaRepository<RelsFamily, RelsFamilyId> {

	// RelsUKey =
	public Slice<RelsFamily> findAllByRelsUKeyIsOrderByRelsSeqAsc(String relsUKey_0, Pageable pageable);

	// RelsUKey =
	public Optional<RelsFamily> findTopByRelsUKeyIsOrderByRelsSeqDesc(String relsUKey_0);

	// FamilyId =
	public Slice<RelsFamily> findAllByFamilyIdIs(String familyId_0, Pageable pageable);

	// FamilyName =
	public Slice<RelsFamily> findAllByFamilyNameIs(String familyName_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<RelsFamily> findByRelsFamilyId(RelsFamilyId relsFamilyId);

}
