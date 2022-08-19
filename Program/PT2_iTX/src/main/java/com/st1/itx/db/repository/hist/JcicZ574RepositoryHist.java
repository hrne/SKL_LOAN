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

import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ574Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ574RepositoryHist extends JpaRepository<JcicZ574, JcicZ574Id> {

  // CustId=
  public Slice<JcicZ574> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ574> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ574> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // Ukey=
  public Optional<JcicZ574> findTopByUkeyIs(String ukey_0);

  // CustId=, AND ApplyDate = ,AND SubmitKey = 
  public Slice<JcicZ574> findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2, Pageable pageable);

  // CustId=, AND ApplyDate = ,AND SubmitKey = 
  public Optional<JcicZ574> findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ574> findByJcicZ574Id(JcicZ574Id jcicZ574Id);

}

