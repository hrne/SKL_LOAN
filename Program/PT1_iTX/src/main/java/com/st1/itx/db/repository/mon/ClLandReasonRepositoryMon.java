package com.st1.itx.db.repository.mon;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
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
public interface ClLandReasonRepositoryMon extends JpaRepository<ClLandReason, ClLandReasonId> {

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND LandSeq =
  public Optional<ClLandReason> findTopByClCode1IsAndClCode2IsAndClNoIsAndLandSeqIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(int clCode1_0, int clCode2_1, int clNo_2, int landSeq_3);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND LandSeq =
  public Slice<ClLandReason> findAllByClCode1IsAndClCode2IsAndClNoIsAndLandSeqIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(int clCode1_0, int clCode2_1, int clNo_2, int landSeq_3, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClLandReason> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscLandSeqAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClLandReason> findByClLandReasonId(ClLandReasonId clLandReasonId);

}

