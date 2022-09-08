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

import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ570Repository extends JpaRepository<JcicZ570, JcicZ570Id> {

  // CustId=
  public Slice<JcicZ570> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ570> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ570> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // CustId=, AND ApplyDate = ,AND SubmitKey = 
  public Slice<JcicZ570> findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2, Pageable pageable);

  // Ukey=
  public Optional<JcicZ570> findTopByUkeyIs(String ukey_0);

  // CustId=, AND ApplyDate = ,AND SubmitKey = 
  public Optional<JcicZ570> findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2);

  // ActualFilingDate= , AND ActualFilingMark= 
  public Slice<JcicZ570> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ570> findByJcicZ570Id(JcicZ570Id jcicZ570Id);

}

