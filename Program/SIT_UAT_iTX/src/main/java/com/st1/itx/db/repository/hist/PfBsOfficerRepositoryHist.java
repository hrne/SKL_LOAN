package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.domain.PfBsOfficerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfBsOfficerRepositoryHist extends JpaRepository<PfBsOfficer, PfBsOfficerId> {

  // WorkMonth= 
  public Slice<PfBsOfficer> findAllByWorkMonthIs(int workMonth_0, Pageable pageable);

  // WorkMonth<= , AND WorkMonth>= 
  public Slice<PfBsOfficer> findAllByWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(int workMonth_0, int workMonth_1, Pageable pageable);

  // EmpNo= , AND WorkMonth=
  public Slice<PfBsOfficer> findAllByEmpNoIsAndWorkMonthIs(String empNo_0, int workMonth_1, Pageable pageable);

  // EmpNo= 
  public Slice<PfBsOfficer> findAllByEmpNoIs(String empNo_0, Pageable pageable);

  // EmpNo= , AND WorkMonth<= , AND WorkMonth>= 
  public Slice<PfBsOfficer> findAllByEmpNoIsAndWorkMonthLessThanEqualAndWorkMonthGreaterThanEqualOrderByWorkMonthAsc(String empNo_0, int workMonth_1, int workMonth_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfBsOfficer> findByPfBsOfficerId(PfBsOfficerId pfBsOfficerId);

}

