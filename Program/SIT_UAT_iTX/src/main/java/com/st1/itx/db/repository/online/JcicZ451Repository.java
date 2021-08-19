package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.domain.JcicZ451Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ451Repository extends JpaRepository<JcicZ451, JcicZ451Id> {

  // CustId=
  public Slice<JcicZ451> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ451> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ451> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND DelayYM=
  public Slice<JcicZ451> findAllBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int delayYM_4, Pageable pageable);

  // Ukey=
  public Optional<JcicZ451> findTopByUkeyIs(String ukey_0);

  // SubmitKey= , AND CustId= , AND ApplyDate= , AND CourtCode= , AND DelayYM=
  public Optional<JcicZ451> findTopBySubmitKeyIsAndCustIdIsAndApplyDateIsAndCourtCodeIsAndDelayYMIsOrderByCreateDateDesc(String submitKey_0, String custId_1, int applyDate_2, String courtCode_3, int delayYM_4);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ451> findByJcicZ451Id(JcicZ451Id jcicZ451Id);

}

