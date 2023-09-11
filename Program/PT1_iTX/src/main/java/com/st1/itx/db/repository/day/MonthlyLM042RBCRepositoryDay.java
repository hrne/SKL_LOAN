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

import com.st1.itx.db.domain.MonthlyLM042RBC;
import com.st1.itx.db.domain.MonthlyLM042RBCId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM042RBCRepositoryDay extends JpaRepository<MonthlyLM042RBC, MonthlyLM042RBCId> {

  // YearMonth = 
  public Slice<MonthlyLM042RBC> findAllByYearMonthIs(int yearMonth_0, Pageable pageable);

  // YearMonth = ,AND LoanType = ,AND LoanItem = ,AND RelatedCode = 
  public Slice<MonthlyLM042RBC> findAllByYearMonthIsAndLoanTypeIsAndLoanItemIsAndRelatedCodeIs(int yearMonth_0, String loanType_1, String loanItem_2, String relatedCode_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyLM042RBC> findByMonthlyLM042RBCId(MonthlyLM042RBCId monthlyLM042RBCId);

}

