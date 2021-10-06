package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FinReportRate;
import com.st1.itx.db.domain.FinReportRateId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportRateRepositoryHist extends JpaRepository<FinReportRate, FinReportRateId> {

  // CustUKey = 
  public Slice<FinReportRate> findAllByCustUKeyIs(String custUKey_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FinReportRate> findByFinReportRateId(FinReportRateId finReportRateId);

}

