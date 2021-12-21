package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClEva;
import com.st1.itx.db.domain.ClEvaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClEvaRepositoryMon extends JpaRepository<ClEva, ClEvaId> {

	// ClCode1 = ,AND ClCode2 = ,AND ClNo =
	public Slice<ClEva> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByEvaNoAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

	// ClCode1 = ,AND ClCode2 = ,AND ClNo =
	public Optional<ClEva> findTopByClCode1IsAndClCode2IsAndClNoIsOrderByEvaNoDesc(int clCode1_0, int clCode2_1, int clNo_2);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ClEva> findByClEvaId(ClEvaId clEvaId);

}
