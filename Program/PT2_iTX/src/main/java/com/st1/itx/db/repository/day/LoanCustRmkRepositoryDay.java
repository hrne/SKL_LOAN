package com.st1.itx.db.repository.day;


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
public interface LoanCustRmkRepositoryDay extends JpaRepository<LoanCustRmk, LoanCustRmkId> {

  // CustNo = 
  public Slice<LoanCustRmk> findAllByCustNoIsOrderByAcDateDescRmkNoAsc(int custNo_0, Pageable pageable);

  // CustNo = ,AND AcDate =  
  public Optional<LoanCustRmk> findTopByCustNoIsAndAcDateIsOrderByRmkNoDesc(int custNo_0, int acDate_1);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND BorxNo =
  public Slice<LoanCustRmk> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoIsOrderByCreateDateAsc(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanCustRmk> findByLoanCustRmkId(LoanCustRmkId loanCustRmkId);

}

