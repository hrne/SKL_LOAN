package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.HlAreaLnYg6Pt;
import com.st1.itx.db.domain.HlAreaLnYg6PtId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlAreaLnYg6PtRepositoryDay extends JpaRepository<HlAreaLnYg6Pt, HlAreaLnYg6PtId> {

	// CalDate =
	public Slice<HlAreaLnYg6Pt> findAllByCalDateIs(int calDate_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<HlAreaLnYg6Pt> findByHlAreaLnYg6PtId(HlAreaLnYg6PtId hlAreaLnYg6PtId);

}
