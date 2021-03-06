package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClImmRankDetail;
import com.st1.itx.db.domain.ClImmRankDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClImmRankDetailRepositoryDay extends JpaRepository<ClImmRankDetail, ClImmRankDetailId> {

	// ClCode1 = ,AND ClCode2 = ,AND ClNo =
	public Slice<ClImmRankDetail> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ClImmRankDetail> findByClImmRankDetailId(ClImmRankDetailId clImmRankDetailId);

}
