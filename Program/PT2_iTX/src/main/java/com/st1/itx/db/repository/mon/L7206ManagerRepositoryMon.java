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

import com.st1.itx.db.domain.L7206Manager;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface L7206ManagerRepositoryMon extends JpaRepository<L7206Manager, Long> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<L7206Manager> findByLogNo(Long logNo);

  // 更新利害關係人負責人資料
  @Procedure(value = "\"Usp_L7_L7206Manager_Ins\"")
  public void uspL7L7206managerIns(String EmpNo);

}

