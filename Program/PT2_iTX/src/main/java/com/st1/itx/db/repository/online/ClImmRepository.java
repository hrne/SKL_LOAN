package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClImm;
import com.st1.itx.db.domain.ClImmId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClImmRepository extends JpaRepository<ClImm, ClImmId> {

	// ClCode1 =
	public Slice<ClImm> findAllByClCode1Is(int clCode1_0, Pageable pageable);

	// ClCode1 = ,AND ClCode2 =
	public Slice<ClImm> findAllByClCode1IsAndClCode2Is(int clCode1_0, int clCode2_1, Pageable pageable);

	// SettingStat >= ,AND SettingStat <= ,AND ClStat >= ,AND ClStat <=
	public Slice<ClImm> findAllBySettingStatGreaterThanEqualAndSettingStatLessThanEqualAndClStatGreaterThanEqualAndClStatLessThanEqual(String settingStat_0, String settingStat_1, String clStat_2,
			String clStat_3, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ClImm> findByClImmId(ClImmId clImmId);

}
