package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.YearlyHouseLoanInt;
import com.st1.itx.db.domain.YearlyHouseLoanIntId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface YearlyHouseLoanIntRepositoryDay extends JpaRepository<YearlyHouseLoanInt, YearlyHouseLoanIntId> {

  // YearMonth=
  public Slice<YearlyHouseLoanInt> findAllByYearMonthIsOrderByYearMonthAscCustNoAscFacmNoAsc(int yearMonth_0, Pageable pageable);

  // CustNo=
  public Slice<YearlyHouseLoanInt> findAllByCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(int custNo_0, Pageable pageable);

  // YearMonth= ,AND CustNo=
  public Slice<YearlyHouseLoanInt> findAllByYearMonthIsAndCustNoIsOrderByYearMonthAscCustNoAscFacmNoAsc(int yearMonth_0, int custNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<YearlyHouseLoanInt> findByYearlyHouseLoanIntId(YearlyHouseLoanIntId yearlyHouseLoanIntId);

}

