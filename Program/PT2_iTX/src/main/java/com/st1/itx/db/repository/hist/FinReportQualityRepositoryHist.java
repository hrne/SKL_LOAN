package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FinReportQuality;
import com.st1.itx.db.domain.FinReportQualityId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportQualityRepositoryHist extends JpaRepository<FinReportQuality, FinReportQualityId> {

  // CustUKey = 
  public Slice<FinReportQuality> findAllByCustUKeyIs(String custUKey_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FinReportQuality> findByFinReportQualityId(FinReportQualityId finReportQualityId);

}

