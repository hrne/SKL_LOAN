package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ050Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ050RepositoryDay extends JpaRepository<JcicZ050, JcicZ050Id> {

  // CustId=
  public Slice<JcicZ050> findAllByCustIdIsOrderByCustIdAscRcDateDescPayDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ050> findAllByRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ050> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescPayDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // CustId= , AND TranKey ^i
  public Slice<JcicZ050> findAllByCustIdIsAndTranKeyInOrderByCustIdAscRcDateDescPayDateDesc(String custId_0, List<String> tranKey_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ050> findByJcicZ050Id(JcicZ050Id jcicZ050Id);

}

