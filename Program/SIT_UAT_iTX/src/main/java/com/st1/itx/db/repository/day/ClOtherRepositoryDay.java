package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClOther;
import com.st1.itx.db.domain.ClOtherId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClOtherRepositoryDay extends JpaRepository<ClOther, ClOtherId> {

  // ClCode1 = 
  public Slice<ClOther> findAllByClCode1Is(int clCode1_0, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = 
  public Slice<ClOther> findAllByClCode1IsAndClCode2Is(int clCode1_0, int clCode2_1, Pageable pageable);

  // IssuingId = ,AND DocNo = ,AND OwnerCustUKey =
  public Slice<ClOther> findAllByIssuingIdIsAndDocNoIsAndOwnerCustUKeyIs(String issuingId_0, String docNo_1, String ownerCustUKey_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClOther> findByClOtherId(ClOtherId clOtherId);

}

