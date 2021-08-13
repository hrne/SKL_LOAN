package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanSynd;
import com.st1.itx.db.domain.LoanSyndId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanSyndRepositoryDay extends JpaRepository<LoanSynd, LoanSyndId> {

  // CustNo >= ,AND CustNo <= ,AND LeadingBank % ,AND SigningDate >= ,AND SigningDate <= ,AND DrawdownStartDate >= ,AND DrawdownStartDate <= ,AND DrawdownEndDate >= ,AND DrawdownEndDate <= 
  public Slice<LoanSynd> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndLeadingBankLikeAndSigningDateGreaterThanEqualAndSigningDateLessThanEqualAndDrawdownStartDateGreaterThanEqualAndDrawdownStartDateLessThanEqualAndDrawdownEndDateGreaterThanEqualAndDrawdownEndDateLessThanEqualOrderByCustNoAscLeadingBankAscSigningDateAscDrawdownStartDateAsc(int custNo_0, int custNo_1, String leadingBank_2, int signingDate_3, int signingDate_4, int drawdownStartDate_5, int drawdownStartDate_6, int drawdownEndDate_7, int drawdownEndDate_8, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanSynd> findByLoanSyndId(LoanSyndId loanSyndId);

}

