package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ448;
import com.st1.itx.db.domain.JcicZ448Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ448RepositoryDay extends JpaRepository<JcicZ448, JcicZ448Id> {

  // CustId=
  public Slice<JcicZ448> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ448> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ448> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
  public Slice<JcicZ448> findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4, Pageable pageable);

  // Ukey=
  public Optional<JcicZ448> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND MaxMainCode=
  public Optional<JcicZ448> findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndMaxMainCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, String maxMainCode_4);

  // ActualFilingDate= , AND ActualFilingMark=
  public Slice<JcicZ448> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ448> findByJcicZ448Id(JcicZ448Id jcicZ448Id);

}

