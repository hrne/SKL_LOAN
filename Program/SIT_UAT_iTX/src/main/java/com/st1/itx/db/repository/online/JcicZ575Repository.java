package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ575Repository extends JpaRepository<JcicZ575, JcicZ575Id> {

  // CustId=
  public Slice<JcicZ575> findAllByCustIdIsOrderByApplyDateDescSubmitKeyDescBankIdDescUkeyDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ575> findAllByApplyDateIsOrderByCustIdAscSubmitKeyDescBankIdDescUkeyDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ575> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDescSubmitKeyDescBankIdDescUkeyDesc(String custId_0, int applyDate_1, Pageable pageable);

  // CustId=, AND ApplyDate = ,AND SubmitKey = ,AND BankId = 
  public Slice<JcicZ575> findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsAndBankIdIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2, String bankId_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ575> findTopByUkeyIs(String ukey_0);

  // CustId=, AND ApplyDate = ,AND SubmitKey = ,AND BankId = 
  public Optional<JcicZ575> findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsAndBankIdIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2, String bankId_3);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ575> findByJcicZ575Id(JcicZ575Id jcicZ575Id);

}

