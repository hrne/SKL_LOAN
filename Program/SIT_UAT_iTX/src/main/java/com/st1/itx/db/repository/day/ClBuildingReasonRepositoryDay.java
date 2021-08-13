package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClBuildingReason;
import com.st1.itx.db.domain.ClBuildingReasonId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingReasonRepositoryDay extends JpaRepository<ClBuildingReason, ClBuildingReasonId> {

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Optional<ClBuildingReason> findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(int clCode1_0, int clCode2_1, int clNo_2);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClBuildingReason> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClBuildingReason> findByClBuildingReasonId(ClBuildingReasonId clBuildingReasonId);

}

