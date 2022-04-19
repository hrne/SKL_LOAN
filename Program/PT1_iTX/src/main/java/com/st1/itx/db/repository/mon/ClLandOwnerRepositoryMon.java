package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.domain.ClLandOwnerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClLandOwnerRepositoryMon extends JpaRepository<ClLandOwner, ClLandOwnerId> {

	// ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND LandSeq =
	public Slice<ClLandOwner> findAllByClCode1IsAndClCode2IsAndClNoIsAndLandSeqIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(int clCode1_0, int clCode2_1, int clNo_2, int landSeq_3,
			Pageable pageable);

	// OwnerCustUKey =
	public Slice<ClLandOwner> findAllByOwnerCustUKeyIs(String ownerCustUKey_0, Pageable pageable);

	// ClCode1 = ,AND ClCode2 = ,AND ClNo =
	public Slice<ClLandOwner> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ClLandOwner> findByClLandOwnerId(ClLandOwnerId clLandOwnerId);

}
