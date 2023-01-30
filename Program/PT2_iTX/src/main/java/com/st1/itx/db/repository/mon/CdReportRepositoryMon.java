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

import com.st1.itx.db.domain.CdReport;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdReportRepositoryMon extends JpaRepository<CdReport, String> {

  // Cycle = 
  public Slice<CdReport> findAllByCycleIs(int cycle_0, Pageable pageable);

  // Enable = 
  public Slice<CdReport> findAllByEnableIs(String enable_0, Pageable pageable);

  // FormNo %
  public Slice<CdReport> findAllByFormNoLikeOrderByFormNoAsc(String formNo_0, Pageable pageable);

  // FormName %
  public Slice<CdReport> findAllByFormNameLikeOrderByFormNoAsc(String formName_0, Pageable pageable);

  // FormNo =
  public Optional<CdReport> findTopByFormNoIs(String formNo_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdReport> findByFormNo(String formNo);

}

