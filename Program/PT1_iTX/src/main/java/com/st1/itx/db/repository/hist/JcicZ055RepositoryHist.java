package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.domain.JcicZ055Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ055RepositoryHist extends JpaRepository<JcicZ055, JcicZ055Id> {

  // CustId=
  public Slice<JcicZ055> findAllByCustIdIsOrderByCustIdAscClaimDateDesc(String custId_0, Pageable pageable);

  // ClaimDate=
  public Slice<JcicZ055> findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(int claimDate_0, Pageable pageable);

  // CustId= , AND ClaimDate=
  public Slice<JcicZ055> findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(String custId_0, int claimDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND ClaimDate= , AND CourtCode=
  public Slice<JcicZ055> findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(String submitKey_0, String custId_1, int claimDate_2, String courtCode_3, Pageable pageable);

  // SubmitKey= , AND CustId= , AND CaseStatus= , AND ClaimDate= , AND CourtCode=
  public Slice<JcicZ055> findAllBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, String caseStatus_2, int claimDate_3, String courtCode_4, Pageable pageable);

  // Ukey=
  public Optional<JcicZ055> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND CaseStatus= , AND ClaimDate= , AND CourtCode=
  public Optional<JcicZ055> findTopBySubmitKeyIsAndCustIdIsAndCaseStatusIsAndClaimDateIsAndCourtCodeIsOrderByCreateDateDesc(String submitKey_0, String custId_1, String caseStatus_2, int claimDate_3, String courtCode_4);

  // ActualFilingDate= , AND ActualFilingMark= 
  public Slice<JcicZ055> findAllByActualFilingDateIsAndActualFilingMarkIsOrderByCreateDateDesc(int actualFilingDate_0, String actualFilingMark_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ055> findByJcicZ055Id(JcicZ055Id jcicZ055Id);

}

