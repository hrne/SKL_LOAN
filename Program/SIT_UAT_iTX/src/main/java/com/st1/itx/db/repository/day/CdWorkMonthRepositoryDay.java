package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.CdWorkMonthId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdWorkMonthRepositoryDay extends JpaRepository<CdWorkMonth, CdWorkMonthId> {

  // Year >= ,AND Year <= ,AND Month >= ,AND Month <= 
  public Slice<CdWorkMonth> findAllByYearGreaterThanEqualAndYearLessThanEqualAndMonthGreaterThanEqualAndMonthLessThanEqualOrderByYearAscMonthAsc(int year_0, int year_1, int month_2, int month_3, Pageable pageable);

  // StartDate <= ,AND EndDate >= 
  public Optional<CdWorkMonth> findTopByStartDateLessThanEqualAndEndDateGreaterThanEqual(int startDate_0, int endDate_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdWorkMonth> findByCdWorkMonthId(CdWorkMonthId cdWorkMonthId);

}

