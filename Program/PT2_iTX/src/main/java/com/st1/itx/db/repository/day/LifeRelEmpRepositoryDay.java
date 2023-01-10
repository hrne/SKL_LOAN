package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LifeRelEmp;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LifeRelEmpRepositoryDay extends JpaRepository<LifeRelEmp, String> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LifeRelEmp> findByEmpId(String empId);

}

