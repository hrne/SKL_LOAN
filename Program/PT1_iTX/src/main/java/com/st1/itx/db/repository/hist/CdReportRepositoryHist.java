package com.st1.itx.db.repository.hist;


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
public interface CdReportRepositoryHist extends JpaRepository<CdReport, String> {

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

  // GroupNo=, AND FormNo %
  public Slice<CdReport> findAllByGroupNoIsAndFormNoLikeOrderByGroupNoAscFormNoAsc(String groupNo_0, String formNo_1, Pageable pageable);

  // Cycle=, AND GroupNo %
  public Slice<CdReport> findAllByCycleIsAndGroupNoLikeOrderByGroupNoAscFormNoAsc(int cycle_0, String groupNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdReport> findByFormNo(String formNo);

}

