package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ573Repository extends JpaRepository<JcicZ573, JcicZ573Id> {

  // CustId=
  public Slice<JcicZ573> findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ573> findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ573> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // Ukey=
  public Optional<JcicZ573> findTopByUkeyIs(String ukey_0);

  // CustId=, AND ApplyDate = ,AND SubmitKey = ,AND PayDate = 
  public Slice<JcicZ573> findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2, int payDate_3, Pageable pageable);

  // CustId=, AND ApplyDate = ,AND SubmitKey = ,AND PayDate = 
  public Optional<JcicZ573> findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsAndPayDateIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2, int payDate_3);

  // ActualFilingDate= , AND ActualFilingMark= 
  public Slice<JcicZ573> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ573> findByJcicZ573Id(JcicZ573Id jcicZ573Id);

}

