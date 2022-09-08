package com.st1.itx.db.repository.online;


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

import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ062Repository extends JpaRepository<JcicZ062, JcicZ062Id> {

  // CustId=
  public Slice<JcicZ062> findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ062> findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ062> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate= 
  public Slice<JcicZ062> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ062> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate=
  public Optional<JcicZ062> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3);

  // ActualFilingDate= , AND ActualFilingMark= 
  public Slice<JcicZ062> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ062> findByJcicZ062Id(JcicZ062Id jcicZ062Id);

}

