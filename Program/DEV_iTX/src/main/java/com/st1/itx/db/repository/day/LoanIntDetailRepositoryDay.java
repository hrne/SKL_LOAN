package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.domain.LoanIntDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIntDetailRepositoryDay extends JpaRepository<LoanIntDetail, LoanIntDetailId> {

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND BreachGetCode = 
  public Slice<LoanIntDetail> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndBreachGetCodeIsOrderByAcDateAsc(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, String breachGetCode_5, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND AcDate = ,AND TlrNo = ,AND TxtNo =
  public Slice<LoanIntDetail> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTlrNoIsAndTxtNoIsOrderByAcDateAsc(int custNo_0, int facmNo_1, int bormNo_2, int acDate_3, String tlrNo_4, String txtNo_5, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIntDetail> findByLoanIntDetailId(LoanIntDetailId loanIntDetailId);

}

