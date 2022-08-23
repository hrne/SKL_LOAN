package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ063Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ063RepositoryDay extends JpaRepository<JcicZ063, JcicZ063Id> {

  // CustId=
  public Slice<JcicZ063> findAllByCustIdIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ063> findAllByRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ063> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDescChangePayDateDescClosedDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate=
  public Slice<JcicZ063> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3, Pageable pageable);

  // Ukey=
  public Optional<JcicZ063> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate= , AND ChangePayDate=
  public Optional<JcicZ063> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsAndChangePayDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, int changePayDate_3);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ063> findByJcicZ063Id(JcicZ063Id jcicZ063Id);

}

