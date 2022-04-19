package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.LoanNotYetId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanNotYetRepository extends JpaRepository<LoanNotYet, LoanNotYetId> {

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND YetDate>= ,AND YetDate<= ,AND CloseDate>= ,AND CloseDate<=
  public Slice<LoanNotYet> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndYetDateGreaterThanEqualAndYetDateLessThanEqualAndCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscNotYetCodeAsc(int custNo_0, int facmNo_1, int facmNo_2, int yetDate_3, int yetDate_4, int closeDate_5, int closeDate_6, Pageable pageable);

  // CustNo =
  public Slice<LoanNotYet> findAllByCustNoIsOrderByFacmNoAsc(int custNo_0, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND NotYetCode =
  public Slice<LoanNotYet> findAllByCustNoIsAndFacmNoIsAndNotYetCodeIs(int custNo_0, int facmNo_1, String notYetCode_2, Pageable pageable);

  // CloseDate = ,AND YetDate <=
  public Slice<LoanNotYet> findAllByCloseDateIsAndYetDateLessThanEqualOrderByCustNoAscFacmNoAscYetDateAsc(int closeDate_0, int yetDate_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanNotYet> findByLoanNotYetId(LoanNotYetId loanNotYetId);

}

