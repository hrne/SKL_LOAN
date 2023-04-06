package com.st1.itx.db.repository.hist;


import java.util.Optional;

import java.util.List;
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
public interface CustDataCtrlRepositoryHist extends JpaRepository<CustDataCtrl, Integer> {

  // CustNo = 
  public Slice<CustDataCtrl> findAllByCustNoIs(int custNo_0, Pageable pageable);

  // CustUKey = 
  public Slice<CustDataCtrl> findAllByCustUKeyIs(String custUKey_0, Pageable pageable);

  // CustId = 
  public Slice<CustDataCtrl> findAllByCustIdIs(String custId_0, Pageable pageable);

  // ApplMark ^i ,AND CustNo = 
  public Slice<CustDataCtrl> findAllByApplMarkInAndCustNoIs(List<Integer> applMark_0, int custNo_1, Pageable pageable);

  // ApplMark ^i ,AND CustUKey = 
  public Slice<CustDataCtrl> findAllByApplMarkInAndCustUKeyIs(List<Integer> applMark_0, String custUKey_1, Pageable pageable);

  // ApplMark ^i ,AND CustId =
  public Slice<CustDataCtrl> findAllByApplMarkInAndCustIdIs(List<Integer> applMark_0, String custId_1, Pageable pageable);

  // ApplMark ^i
  public Slice<CustDataCtrl> findAllByApplMarkInOrderByCustNoAsc(List<Integer> applMark_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CustDataCtrl> findByCustNo(int custNo);

}

