package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustRelDetail;
import com.st1.itx.db.domain.CustRelDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustRelDetailRepository extends JpaRepository<CustRelDetail, CustRelDetailId> {

  // CustRelMainUKey= 
  public Slice<CustRelDetail> findAllByCustRelMainUKeyIsOrderByCreateDateAsc(String custRelMainUKey_0, Pageable pageable);

  // RelId=
  public Slice<CustRelDetail> findAllByRelIdIsOrderByCreateDateAsc(String relId_0, Pageable pageable);

  // RelId=
  public Optional<CustRelDetail> findTopByRelIdIs(String relId_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CustRelDetail> findByCustRelDetailId(CustRelDetailId custRelDetailId);

}

