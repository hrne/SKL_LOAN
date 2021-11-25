package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClLandReason;
import com.st1.itx.db.domain.ClLandReasonId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClLandReasonRepositoryDay extends JpaRepository<ClLandReason, ClLandReasonId> {

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Optional<ClLandReason> findTopByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqDesc(int clCode1_0, int clCode2_1, int clNo_2);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClLandReason> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscReasonSeqAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClLandReason> findByClLandReasonId(ClLandReasonId clLandReasonId);

}

