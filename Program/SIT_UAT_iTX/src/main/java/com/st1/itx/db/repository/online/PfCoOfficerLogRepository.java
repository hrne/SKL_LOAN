package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfCoOfficerLog;
import com.st1.itx.db.domain.PfCoOfficerLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfCoOfficerLogRepository extends JpaRepository<PfCoOfficerLog, PfCoOfficerLogId> {

  // EmpNo= ,AND EffectiveDate=
  public Slice<PfCoOfficerLog> findAllByEmpNoIsAndEffectiveDateIsOrderBySerialNoAsc(String empNo_0, int effectiveDate_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfCoOfficerLog> findByPfCoOfficerLogId(PfCoOfficerLogId pfCoOfficerLogId);

}

