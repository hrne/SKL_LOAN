package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.L7206Cust;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface L7206CustRepository extends JpaRepository<L7206Cust, Long> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<L7206Cust> findByLogNo(Long logNo);

  // 更新利害關係人借款人資料
  @Procedure(value = "\"Usp_L7_L7206Cust_Ins\"")
  public void uspL7L7206custIns(String EmpNo);

}

