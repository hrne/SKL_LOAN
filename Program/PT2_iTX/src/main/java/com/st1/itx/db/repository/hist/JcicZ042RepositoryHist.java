package com.st1.itx.db.repository.hist;


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

import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ042RepositoryHist extends JpaRepository<JcicZ042, JcicZ042Id> {

  // CustId=
  public Slice<JcicZ042> findAllByCustIdIsOrderByCustIdAscRcDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ042> findAllByRcDateIsOrderByCustIdAscRcDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ042> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode=
  public Slice<JcicZ042> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ042> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode=
  public Optional<JcicZ042> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3);

  // SubmitKey= , AND CustId= , AND RcDate= 
  public Slice<JcicZ042> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, Pageable pageable);

  // ActualFilingDate= , AND ActualFilingMark= 
  public Slice<JcicZ042> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ042> findByJcicZ042Id(JcicZ042Id jcicZ042Id);

}

