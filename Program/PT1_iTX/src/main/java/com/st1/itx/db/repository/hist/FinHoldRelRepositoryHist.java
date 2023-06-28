package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FinHoldRel;
import com.st1.itx.db.domain.FinHoldRelId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinHoldRelRepositoryHist extends JpaRepository<FinHoldRel, FinHoldRelId> {

  // AcDate = 
  public Slice<FinHoldRel> findAllByAcDateIs(int acDate_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FinHoldRel> findByFinHoldRelId(FinHoldRelId finHoldRelId);

  // 
  @Procedure(value = "\"Usp_L7_FinHoldRel_Upd\"")
  public void uspL7FinholdrelUpd(String empNo);

}

