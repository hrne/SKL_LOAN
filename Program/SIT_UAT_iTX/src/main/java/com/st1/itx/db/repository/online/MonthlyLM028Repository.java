package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM028;
import com.st1.itx.db.domain.MonthlyLM028Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM028Repository extends JpaRepository<MonthlyLM028, MonthlyLM028Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyLM028> findByMonthlyLM028Id(MonthlyLM028Id monthlyLM028Id);

  // (月底日日終批次)維護MonthlyLM028月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyLM028_Upd\"")
  public void uspL9Monthlylm028Upd(int TBSDYF, String empNo);

}

