package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ041Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ041RepositoryDay extends JpaRepository<JcicZ041, JcicZ041Id> {

  // CustId=
  public Slice<JcicZ041> findAllByCustIdIsOrderByCustIdAscRcDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ041> findAllByRcDateIsOrderByCustIdAscRcDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ041> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate=
  public Slice<JcicZ041> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, Pageable pageable);

  // Ukey=
  public Optional<JcicZ041> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate=
  public Optional<JcicZ041> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2);

  // ActualFilingDate= , AND ActualFilingMark= 
  public Slice<JcicZ041> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ041> findByJcicZ041Id(JcicZ041Id jcicZ041Id);

}

