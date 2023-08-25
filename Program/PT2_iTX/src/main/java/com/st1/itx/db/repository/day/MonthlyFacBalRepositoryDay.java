package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyFacBal;
import com.st1.itx.db.domain.MonthlyFacBalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyFacBalRepositoryDay extends JpaRepository<MonthlyFacBal, MonthlyFacBalId> {

  // ClCustNo=, AND ClFacmNo=
  public Slice<MonthlyFacBal> findAllByClCustNoIsAndClFacmNoIs(int clCustNo_0, int clFacmNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyFacBal> findByMonthlyFacBalId(MonthlyFacBalId monthlyFacBalId);

  // (月底日日終批次)維護 MonthlyFacBal 額度月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyFacBal_Upd\"")
  public void uspL9MonthlyfacbalUpd(int YYYYMM,  String empNo);

}

