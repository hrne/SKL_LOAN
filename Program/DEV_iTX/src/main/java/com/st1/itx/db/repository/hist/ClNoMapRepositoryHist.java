package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface ClNoMapRepositoryHist extends JpaRepository<ClNoMap, ClNoMapId> {

  // GDRID1 = ,AND GDRID2 = ,AND GDRNUM = 
  public Slice<ClNoMap> findAllByGDRID1IsAndGDRID2IsAndGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(int gDRID1_0, int gDRID2_1, int gDRNUM_2, Pageable pageable);

  // MainGDRID1 = ,AND MainGDRID2 = ,AND MainGDRNUM = ,AND MainLGTSEQ =
  public Slice<ClNoMap> findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsAndMainLGTSEQIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(int mainGDRID1_0, int mainGDRID2_1, int mainGDRNUM_2, int mainLGTSEQ_3, Pageable pageable);

  // MainGDRID1 = ,AND MainGDRID2 = ,AND MainGDRNUM = 
  public Slice<ClNoMap> findAllByMainGDRID1IsAndMainGDRID2IsAndMainGDRNUMIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(int mainGDRID1_0, int mainGDRID2_1, int mainGDRNUM_2, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClNoMap> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByGDRID1AscGDRID2AscGDRNUMAscLGTSEQAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClNoMap> findByClNoMapId(ClNoMapId clNoMapId);

}

