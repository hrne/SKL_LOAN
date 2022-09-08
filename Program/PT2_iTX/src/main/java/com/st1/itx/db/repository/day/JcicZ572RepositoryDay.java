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

import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ572RepositoryDay extends JpaRepository<JcicZ572, JcicZ572Id> {

  // CustId=
  public Slice<JcicZ572> findAllByCustIdIsOrderByCustIdAscApplyDateDescPayDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ572> findAllByApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ572> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescPayDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND PayDate= , AND BankId=
  public Slice<JcicZ572> findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, int payDate_3, String bankId_4, Pageable pageable);

  // Ukey=
  public Optional<JcicZ572> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND PayDate= , AND BankId=
  public Optional<JcicZ572> findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndPayDateIsAndBankIdIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, int payDate_3, String bankId_4);

  // ActualFilingDate= , AND ActualFilingMark=
  public Slice<JcicZ572> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ572> findByJcicZ572Id(JcicZ572Id jcicZ572Id);

}

