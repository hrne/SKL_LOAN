package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ046Repository extends JpaRepository<JcicZ046, JcicZ046Id> {

  // CustId=
  public Slice<JcicZ046> findAllByCustIdIsOrderByCustIdAscRcDateDescCloseDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ046> findAllByRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ046> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescCloseDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // CustId= , AND RcDate= , AND SubmitKey=
  public Slice<JcicZ046> findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByCustIdAscRcDateDescCloseDateDesc(String custId_0, int rcDate_1, String submitKey_2, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate= , AND CloseDate=
  public Slice<JcicZ046> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, int closeDate_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ046> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate= , AND CloseDate=
  public Optional<JcicZ046> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndCloseDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, int closeDate_3);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ046> findByJcicZ046Id(JcicZ046Id jcicZ046Id);

}

