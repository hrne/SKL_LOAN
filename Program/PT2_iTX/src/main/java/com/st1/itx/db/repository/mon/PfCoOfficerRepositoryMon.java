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

import com.st1.itx.db.domain.PfCoOfficer;
import com.st1.itx.db.domain.PfCoOfficerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfCoOfficerRepositoryMon extends JpaRepository<PfCoOfficer, PfCoOfficerId> {

  // EmpNo = ,AND EffectiveDate >= ,AND EffectiveDate <=
  public Optional<PfCoOfficer> findTopByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(String empNo_0, int effectiveDate_1, int effectiveDate_2);

  // EmpNo = 
  public Slice<PfCoOfficer> findAllByEmpNoIsOrderByEffectiveDateDesc(String empNo_0, Pageable pageable);

  // EmpNo = 
  public Optional<PfCoOfficer> findTopByEmpNoIsOrderByEffectiveDateDesc(String empNo_0);

  // EffectiveDate >= ,AND EffectiveDate <=
  public Slice<PfCoOfficer> findAllByEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEmpNoAscEffectiveDateDesc(int effectiveDate_0, int effectiveDate_1, Pageable pageable);

  // EmpNo = ,AND EffectiveDate >= ,AND EffectiveDate <=
  public Slice<PfCoOfficer> findAllByEmpNoIsAndEffectiveDateGreaterThanEqualAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(String empNo_0, int effectiveDate_1, int effectiveDate_2, Pageable pageable);

  // EffectiveDate >
  public Slice<PfCoOfficer> findAllByEffectiveDateGreaterThanOrderByEffectiveDateDesc(int effectiveDate_0, Pageable pageable);

  // IneffectiveDate <
  public Slice<PfCoOfficer> findAllByIneffectiveDateLessThanOrderByEffectiveDateDesc(int ineffectiveDate_0, Pageable pageable);

  // EffectiveDate >= 
  public Slice<PfCoOfficer> findAllByEffectiveDateGreaterThanEqualOrderByEffectiveDateDesc(int effectiveDate_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfCoOfficer> findByPfCoOfficerId(PfCoOfficerId pfCoOfficerId);

}

