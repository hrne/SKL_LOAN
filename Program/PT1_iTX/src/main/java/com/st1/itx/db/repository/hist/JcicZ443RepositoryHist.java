package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ443RepositoryHist extends JpaRepository<JcicZ443, JcicZ443Id> {

  // CustId=
  public Slice<JcicZ443> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ443> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ443> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
  public Slice<JcicZ443> findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, Pageable pageable);

  // Ukey=
  public Optional<JcicZ443> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
  public Optional<JcicZ443> findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ443> findByJcicZ443Id(JcicZ443Id jcicZ443Id);

}

