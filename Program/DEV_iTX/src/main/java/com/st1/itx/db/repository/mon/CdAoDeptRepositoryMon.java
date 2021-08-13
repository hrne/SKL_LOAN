package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdAoDept;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAoDeptRepositoryMon extends JpaRepository<CdAoDept, String> {

  // EmployeeNo >= ,AND EmployeeNo <= 
  public Slice<CdAoDept> findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(String employeeNo_0, String employeeNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdAoDept> findByEmployeeNo(String employeeNo);

}

