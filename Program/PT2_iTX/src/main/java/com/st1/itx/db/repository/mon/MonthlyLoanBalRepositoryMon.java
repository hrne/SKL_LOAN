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

import com.st1.itx.db.domain.MonthlyLoanBal;
import com.st1.itx.db.domain.MonthlyLoanBalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLoanBalRepositoryMon extends JpaRepository<MonthlyLoanBal, MonthlyLoanBalId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyLoanBal> findByMonthlyLoanBalId(MonthlyLoanBalId monthlyLoanBalId);

  // (月底日日終批次)維護MonthlyLoanBal每月放款餘額檔
  @Procedure(value = "\"Usp_L9_MonthlyLoanBal_Upd\"")
  public void uspL9MonthlyloanbalUpd(int TBSDYF, String empNo);

}

