package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM052Ovdu;
import com.st1.itx.db.domain.MonthlyLM052OvduId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM052OvduRepositoryMon extends JpaRepository<MonthlyLM052Ovdu, MonthlyLM052OvduId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyLM052Ovdu> findByMonthlyLM052OvduId(MonthlyLM052OvduId monthlyLM052OvduId);

  // 
  @Procedure(value = "\"Usp_L9_MonthlyLM052Ovdu_Ins\"")
  public void uspL9Monthlylm052ovduIns(int tbsdyf,  String empNo);

}

