package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM052Loss;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM052LossRepository extends JpaRepository<MonthlyLM052Loss, Integer> {

  // YearMonth >= ,AND YearMonth <=
  public Slice<MonthlyLM052Loss> findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualOrderByYearMonthAsc(int yearMonth_0, int yearMonth_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyLM052Loss> findByYearMonth(int yearMonth);

}

