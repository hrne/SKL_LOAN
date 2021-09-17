package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.domain.ReltMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ReltMainRepositoryMon extends JpaRepository<ReltMain, ReltMainId> {

  // ReltId =
  public Optional<ReltMain> findTopByReltIdIs(String reltId_0);

  // ReltId =
  public Slice<ReltMain> findAllByReltIdIs(String reltId_0, Pageable pageable);

  // CaseNo =
  public Optional<ReltMain> findTopByCaseNoIs(int caseNo_0);

  // CustNo =
  public Slice<ReltMain> findAllByCustNoIsOrderByCaseNoAsc(int custNo_0, Pageable pageable);

  // CustNo =
  public Optional<ReltMain> findTopByCustNoIs(int custNo_0);

  // CaseNo =
  public Slice<ReltMain> findAllByCaseNoIsOrderByCustNoAsc(int caseNo_0, Pageable pageable);

  // CaseNo = ,AND CustNo = 
  public Slice<ReltMain> findAllByCaseNoIsAndCustNoIs(int caseNo_0, int custNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ReltMain> findByReltMainId(ReltMainId reltMainId);

}

