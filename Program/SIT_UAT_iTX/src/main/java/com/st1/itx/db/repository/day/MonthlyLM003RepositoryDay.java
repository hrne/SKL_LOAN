package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM003;
import com.st1.itx.db.domain.MonthlyLM003Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM003RepositoryDay extends JpaRepository<MonthlyLM003, MonthlyLM003Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyLM003> findByMonthlyLM003Id(MonthlyLM003Id monthlyLM003Id);

  // (月底日日終批次)維護MonthlyLM003月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyLM003_Upd\"")
  public void uspL9Monthlylm003Upd(int TBSDYF, String empNo);

}

