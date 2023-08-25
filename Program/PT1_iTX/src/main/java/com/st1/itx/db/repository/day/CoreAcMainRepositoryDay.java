package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CoreAcMain;
import com.st1.itx.db.domain.CoreAcMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CoreAcMainRepositoryDay extends JpaRepository<CoreAcMain, CoreAcMainId> {

  // AcDate = 
  public Slice<CoreAcMain> findAllByAcDateIs(int acDate_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CoreAcMain> findByCoreAcMainId(CoreAcMainId coreAcMainId);

}

