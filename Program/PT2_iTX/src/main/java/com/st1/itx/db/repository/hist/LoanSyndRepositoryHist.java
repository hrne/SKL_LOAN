package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanSynd;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanSyndRepositoryHist extends JpaRepository<LoanSynd, Integer> {

  // SyndNo >= ,AND SyndNo <= ,AND LeadingBank % ,AND SigningDate >= ,AND SigningDate <=
  public Slice<LoanSynd> findAllBySyndNoGreaterThanEqualAndSyndNoLessThanEqualAndLeadingBankLikeAndSigningDateGreaterThanEqualAndSigningDateLessThanEqualOrderBySyndNoAsc(int syndNo_0, int syndNo_1, String leadingBank_2, int signingDate_3, int signingDate_4, Pageable pageable);

  // SigningDate >= ,AND SigningDate<=
  public Slice<LoanSynd> findAllBySigningDateGreaterThanEqualAndSigningDateLessThanEqualOrderBySyndNoAsc(int signingDate_0, int signingDate_1, Pageable pageable);

  // LeadingBank =
  public Slice<LoanSynd> findAllByLeadingBankIsOrderBySyndNoAsc(String leadingBank_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanSynd> findBySyndNo(int syndNo);

}

