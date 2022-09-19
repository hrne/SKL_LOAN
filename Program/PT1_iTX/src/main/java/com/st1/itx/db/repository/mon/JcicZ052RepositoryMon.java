package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ052Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ052RepositoryMon extends JpaRepository<JcicZ052, JcicZ052Id> {

  // CustId=
  public Slice<JcicZ052> findAllByCustIdIsOrderByCustIdAscRcDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ052> findAllByRcDateIsOrderByCustIdAscRcDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ052> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate=
  public Slice<JcicZ052> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, Pageable pageable);

  // Ukey=
  public Optional<JcicZ052> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate=
  public Optional<JcicZ052> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2);

  // SubmitKey= , AND CustId= 
  public Slice<JcicZ052> findAllBySubmitKeyIsAndCustIdIsOrderByCreateDateDesc(String submitKey_0, String custId_1, Pageable pageable);

  // ActualFilingDate= , AND ActualFilingMark=
  public Slice<JcicZ052> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ052> findByJcicZ052Id(JcicZ052Id jcicZ052Id);

}

