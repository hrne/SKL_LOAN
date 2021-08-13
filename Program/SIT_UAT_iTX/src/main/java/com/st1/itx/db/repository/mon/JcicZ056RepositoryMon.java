package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ056Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ056RepositoryMon extends JpaRepository<JcicZ056, JcicZ056Id> {

  // CustId=
  public Slice<JcicZ056> findAllByCustIdIsOrderByCustIdAscClaimDateDesc(String custId_0, Pageable pageable);

  // ClaimDate=
  public Slice<JcicZ056> findAllByClaimDateIsOrderByCustIdAscClaimDateDesc(int claimDate_0, Pageable pageable);

  // CustId= , AND ClaimDate=
  public Slice<JcicZ056> findAllByCustIdIsAndClaimDateIsOrderByCustIdAscClaimDateDesc(String custId_0, int claimDate_1, Pageable pageable);

  // SubmitKey= , AND CustId= , AND ClaimDate= , AND CourtCode=
  public Slice<JcicZ056> findAllBySubmitKeyIsAndCustIdIsAndClaimDateIsAndCourtCodeIsOrderByCustIdAscClaimDateDescCaseStatusAsc(String submitKey_0, String custId_1, int claimDate_2, String courtCode_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ056> findByJcicZ056Id(JcicZ056Id jcicZ056Id);

}

