package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FinReportDebt;
import com.st1.itx.db.domain.FinReportDebtId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportDebtRepositoryMon extends JpaRepository<FinReportDebt, FinReportDebtId> {

  // CustUKey = 
  public Slice<FinReportDebt> findAllByCustUKeyIs(String custUKey_0, Pageable pageable);

  // CustUKey = ,AND StartYY =
  public Optional<FinReportDebt> findTopByCustUKeyIsAndStartYYIs(String custUKey_0, int startYY_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FinReportDebt> findByFinReportDebtId(FinReportDebtId finReportDebtId);

}

