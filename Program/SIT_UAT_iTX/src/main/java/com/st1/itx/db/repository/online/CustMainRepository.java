package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustMain;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustMainRepository extends JpaRepository<CustMain, String> {

  // CustId %
  public Slice<CustMain> findAllByCustIdLikeOrderByCustIdAsc(String custId_0, Pageable pageable);

  // CustId =
  public Optional<CustMain> findTopByCustIdIs(String custId_0);

  // CustNo >= ,AND CustNo <=
  public Optional<CustMain> findTopByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoDesc(int custNo_0, int custNo_1);

  // CustNo >= ,AND CustNo <=
  public Slice<CustMain> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAsc(int custNo_0, int custNo_1, Pageable pageable);

  // CustName =
  public Slice<CustMain> findAllByCustNameIs(String custName_0, Pageable pageable);

  // CustName %
  public Slice<CustMain> findAllByCustNameLikeOrderByCustNoAsc(String custName_0, Pageable pageable);

  // EmpNo =
  public Slice<CustMain> findAllByEmpNoIs(String empNo_0, Pageable pageable);

  // EmpNo >= 
  public Optional<CustMain> findTopByEmpNoGreaterThanEqualOrderByEmpNoDesc(String empNo_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CustMain> findByCustUKey(String custUKey);

}

