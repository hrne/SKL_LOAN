package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.domain.PfCoOfficerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfCoOfficerRepository extends JpaRepository<PfCoOfficer, PfCoOfficerId> {

  // EmpNo = ,AND EffectiveDate >= ,AND EffectiveDate <=
  public Optional<PfCoOfficer> findTopByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(String empNo_0, int effectiveDate_1, int effectiveDate_2);

  // EmpNo = 
  public Slice<PfCoOfficer> findAllByEmpNoIsOrderByEffectiveDateDesc(String empNo_0, Pageable pageable);

  // EmpNo = 
  public Optional<PfCoOfficer> findTopByEmpNoIsOrderByEffectiveDateDesc(String empNo_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfCoOfficer> findByPfCoOfficerId(PfCoOfficerId pfCoOfficerId);

}

