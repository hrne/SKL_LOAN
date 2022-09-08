package com.st1.itx.db.repository.mon;


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

import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.domain.JcicZ571Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ571RepositoryMon extends JpaRepository<JcicZ571, JcicZ571Id> {

  // CustId=
  public Slice<JcicZ571> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ571> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ571> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND BankId=
  public Slice<JcicZ571> findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String bankId_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ571> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND BankId= 
  public Optional<JcicZ571> findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndBankIdIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String bankId_3);

  // ActualFilingDate= , AND ActualFilingMark= 
  public Slice<JcicZ571> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ571> findByJcicZ571Id(JcicZ571Id jcicZ571Id);

}

