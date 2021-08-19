package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ053Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ053RepositoryHist extends JpaRepository<JcicZ053, JcicZ053Id> {

  // CustId=
  public Slice<JcicZ053> findAllByCustIdIsOrderByCustIdAscRcDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ053> findAllByRcDateIsOrderByCustIdAscRcDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ053> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode=
  public Slice<JcicZ053> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ053> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate= , AND MaxMainCode=
  public Optional<JcicZ053> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndMaxMainCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, String maxMainCode_3);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ053> findByJcicZ053Id(JcicZ053Id jcicZ053Id);

}

