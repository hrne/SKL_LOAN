package com.st1.itx.db.repository.mon;

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
public interface HlAreaLnYg6PtRepositoryMon extends JpaRepository<HlAreaLnYg6Pt, HlAreaLnYg6PtId> {

	// WorkYM =
	public Slice<HlAreaLnYg6Pt> findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAsc(String workYM_0, Pageable pageable);

	// AreaUnitNo =
	public Slice<HlAreaLnYg6Pt> findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAsc(String areaUnitNo_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<HlAreaLnYg6Pt> findByHlAreaLnYg6PtId(HlAreaLnYg6PtId hlAreaLnYg6PtId);

}
