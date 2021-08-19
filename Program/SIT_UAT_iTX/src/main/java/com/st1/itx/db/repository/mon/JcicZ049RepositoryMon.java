package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.domain.JcicZ049Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ049RepositoryMon extends JpaRepository<JcicZ049, JcicZ049Id> {

  // CustId=
  public Slice<JcicZ049> findAllByCustIdIsOrderByCustIdAscRcDateDesc(String custId_0, Pageable pageable);

  // RcDate=
  public Slice<JcicZ049> findAllByRcDateIsOrderByCustIdAscRcDateDesc(int rcDate_0, Pageable pageable);

  // CustId= , AND RcDate=
  public Slice<JcicZ049> findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(String custId_0, int rcDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND RcDate=
  public Slice<JcicZ049> findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2, Pageable pageable);

  // Ukey=
  public Optional<JcicZ049> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND RcDate=
  public Optional<JcicZ049> findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int rcDate_2);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ049> findByJcicZ049Id(JcicZ049Id jcicZ049Id);

}

