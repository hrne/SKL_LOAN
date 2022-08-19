package com.st1.itx.db.repository.day;


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

import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ051RepositoryDay extends JpaRepository<JcicZ051, JcicZ051Id> {

  // CustId=
  public Slice<JcicZ051> findAllByCustIdIsOrderByCustIdAscRcDateDescDelayYMDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ051> findAllByRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ051> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescDelayYMDesc(String custId_0, int rcDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate= , AND DelayYM= 
  public Slice<JcicZ051> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIs(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3, Pageable pageable);

  // CustId= , AND RcDate= , AND SubmitKey=
  public Slice<JcicZ051> findAllByCustIdIsAndRcDateIsAndSubmitKeyIsOrderByDelayYMDesc(String custId_0, int rcDate_1, String submitKey_2, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate= , AND DelayYM= 
  public Slice<JcicZ051> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ051> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate= , AND DelayYM= 
  public Optional<JcicZ051> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndDelayYMIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, int delayYM_3);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ051> findByJcicZ051Id(JcicZ051Id jcicZ051Id);

}

