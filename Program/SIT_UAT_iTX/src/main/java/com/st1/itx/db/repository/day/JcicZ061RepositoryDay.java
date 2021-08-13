package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ061Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ061RepositoryDay extends JpaRepository<JcicZ061, JcicZ061Id> {

  // CustId=
  public Slice<JcicZ061> findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ061> findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ061> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ061> findByJcicZ061Id(JcicZ061Id jcicZ061Id);

}

