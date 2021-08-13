package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustDataCtrl;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustDataCtrlRepository extends JpaRepository<CustDataCtrl, Integer> {

  // CustNo = ,AND Enable=
  public Slice<CustDataCtrl> findAllByCustNoIsAndEnableIs(int custNo_0, String enable_1, Pageable pageable);

  // CustUKey = 
  public Slice<CustDataCtrl> findAllByCustUKeyIs(String custUKey_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CustDataCtrl> findByCustNo(int custNo);

}

