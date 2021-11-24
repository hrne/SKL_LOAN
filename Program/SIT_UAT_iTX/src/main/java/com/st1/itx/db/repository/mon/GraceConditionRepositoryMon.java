package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.GraceCondition;
import com.st1.itx.db.domain.GraceConditionId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface GraceConditionRepositoryMon extends JpaRepository<GraceCondition, GraceConditionId> {

  // CustNo <= ,AND CustNo >= 
  public Slice<GraceCondition> findAllByCustNoLessThanEqualAndCustNoGreaterThanEqual(int custNo_0, int custNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<GraceCondition> findByGraceConditionId(GraceConditionId graceConditionId);

}

