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

import com.st1.itx.db.domain.ClNoMap;
import com.st1.itx.db.domain.ClNoMapId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClNoMapRepositoryMon extends JpaRepository<ClNoMap, ClNoMapId> {

  // GdrId1 = ,AND GdrId2 = ,AND GdrNum = 
  public Slice<ClNoMap> findAllByGdrId1IsAndGdrId2IsAndGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(int gdrId1_0, int gdrId2_1, int gdrNum_2, Pageable pageable);

  // GdrId1 = ,AND GdrId2 = ,AND GdrNum = ,AND LgtSeq =
  public Slice<ClNoMap> findAllByGdrId1IsAndGdrId2IsAndGdrNumIsAndLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(int gdrId1_0, int gdrId2_1, int gdrNum_2, int lgtSeq_3, Pageable pageable);

  // MainGdrId1 = ,AND MainGdrId2 = ,AND MainGdrNum = ,AND MainLgtSeq =
  public Slice<ClNoMap> findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsAndMainLgtSeqIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(int mainGdrId1_0, int mainGdrId2_1, int mainGdrNum_2, int mainLgtSeq_3, Pageable pageable);

  // MainGdrId1 = ,AND MainGdrId2 = ,AND MainGdrNum = 
  public Slice<ClNoMap> findAllByMainGdrId1IsAndMainGdrId2IsAndMainGdrNumIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(int mainGdrId1_0, int mainGdrId2_1, int mainGdrNum_2, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClNoMap> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGdrId1AscGdrId2AscGdrNumAscLgtSeqAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClNoMap> findByClNoMapId(ClNoMapId clNoMapId);

}

