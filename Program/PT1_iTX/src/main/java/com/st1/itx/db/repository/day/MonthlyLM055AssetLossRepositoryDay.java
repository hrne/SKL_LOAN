package com.st1.itx.db.repository.day;


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

import com.st1.itx.db.domain.MonthlyLM055AssetLoss;
import com.st1.itx.db.domain.MonthlyLM055AssetLossId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM055AssetLossRepositoryDay extends JpaRepository<MonthlyLM055AssetLoss, MonthlyLM055AssetLossId> {

  // YearMonth = 
  public Slice<MonthlyLM055AssetLoss> findAllByYearMonthIs(int yearMonth_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyLM055AssetLoss> findByMonthlyLM055AssetLossId(MonthlyLM055AssetLossId monthlyLM055AssetLossId);

}

