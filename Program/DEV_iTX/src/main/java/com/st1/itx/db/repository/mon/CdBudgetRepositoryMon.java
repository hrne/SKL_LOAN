package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdBudget;
import com.st1.itx.db.domain.CdBudgetId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBudgetRepositoryMon extends JpaRepository<CdBudget, CdBudgetId> {

  // Year >= ,AND Year <= ,AND Month >= ,AND Month <= 
  public Slice<CdBudget> findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(int year_0, int year_1, int month_2, int month_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdBudget> findByCdBudgetId(CdBudgetId cdBudgetId);

}

