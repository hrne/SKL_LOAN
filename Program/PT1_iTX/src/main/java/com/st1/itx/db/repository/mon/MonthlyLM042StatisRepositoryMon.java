package com.st1.itx.db.repository.mon;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM042Statis;
import com.st1.itx.db.domain.MonthlyLM042StatisId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM042StatisRepositoryMon extends JpaRepository<MonthlyLM042Statis, MonthlyLM042StatisId> {

  // YearMonth = 
  public Slice<MonthlyLM042Statis> findAllByYearMonthIs(int yearMonth_0, Pageable pageable);

  // YearMonth = ,AND LoanItem = ,AND RelatedCode = ,AND AssetClass = 
  public Slice<MonthlyLM042Statis> findAllByYearMonthIsAndLoanItemIsAndRelatedCodeIsAndAssetClassIs(int yearMonth_0, String loanItem_1, String relatedCode_2, String assetClass_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyLM042Statis> findByMonthlyLM042StatisId(MonthlyLM042StatisId monthlyLM042StatisId);

}

