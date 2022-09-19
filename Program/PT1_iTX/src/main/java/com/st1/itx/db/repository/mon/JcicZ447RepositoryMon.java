package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ447;
import com.st1.itx.db.domain.JcicZ447Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ447RepositoryMon extends JpaRepository<JcicZ447, JcicZ447Id> {

  // CustId=
  public Slice<JcicZ447> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ447> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ447> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode=
  public Slice<JcicZ447> findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ447> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= 
  public Optional<JcicZ447> findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3);

  // ActualFilingDate= , AND ActualFilingMark= 
  public Slice<JcicZ447> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ447> findByJcicZ447Id(JcicZ447Id jcicZ447Id);

}

