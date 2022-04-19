package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.RelsMain;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RelsMainRepositoryDay extends JpaRepository<RelsMain, String> {

	// RelsId =
	public Optional<RelsMain> findTopByRelsIdIs(String relsId_0);

	// RelsType =
	public Slice<RelsMain> findAllByRelsTypeIs(int relsType_0, Pageable pageable);

	// RelsName =
	public Optional<RelsMain> findTopByRelsNameIs(String relsName_0);

	// RelsName =
	public Slice<RelsMain> findAllByRelsNameIs(String relsName_0, Pageable pageable);

	// RelsId =
	public Slice<RelsMain> findAllByRelsIdIs(String relsId_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<RelsMain> findByRelsUKey(String relsUKey);

}
