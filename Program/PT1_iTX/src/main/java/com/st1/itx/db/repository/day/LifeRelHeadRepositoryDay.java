package com.st1.itx.db.repository.day;


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

import com.st1.itx.db.domain.LifeRelHead;
import com.st1.itx.db.domain.LifeRelHeadId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LifeRelHeadRepositoryDay extends JpaRepository<LifeRelHead, LifeRelHeadId> {

  // AcDate = 
  public Slice<LifeRelHead> findAllByAcDateIs(int acDate_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LifeRelHead> findByLifeRelHeadId(LifeRelHeadId lifeRelHeadId);

}

