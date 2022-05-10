package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.domain.LoanCustRmkId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanCustRmkRepositoryMon extends JpaRepository<LoanCustRmk, LoanCustRmkId> {

  // CustNo = 
  public Slice<LoanCustRmk> findAllByCustNoIsOrderByAcDateDescRmkNoAsc(int custNo_0, Pageable pageable);

  // CustNo = ,AND AcDate =  
  public Optional<LoanCustRmk> findTopByCustNoIsAndAcDateIsOrderByRmkNoDesc(int custNo_0, int acDate_1);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND BorxNo = 
  public Slice<LoanCustRmk> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoIsOrderByCreateDateAsc(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, Pageable pageable);

  // BorxNo >= ,AND BorxNo <= 
  public Slice<LoanCustRmk> findAllByBorxNoGreaterThanEqualAndBorxNoLessThanEqualOrderByCreateDateAsc(int borxNo_0, int borxNo_1, Pageable pageable);

  // CustNo >= ,AND CustNo <= ,AND BorxNo >= ,AND BorxNo <= 
  public Slice<LoanCustRmk> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndBorxNoGreaterThanEqualAndBorxNoLessThanEqual(int custNo_0, int custNo_1, int borxNo_2, int borxNo_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanCustRmk> findByLoanCustRmkId(LoanCustRmkId loanCustRmkId);

}

