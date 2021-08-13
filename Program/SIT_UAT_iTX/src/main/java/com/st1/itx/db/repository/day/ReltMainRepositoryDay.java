package com.st1.itx.db.repository.day;


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
public interface ReltMainRepositoryDay extends JpaRepository<ReltMain, ReltMainId> {

  // ReltId =
  public Optional<ReltMain> findTopByReltIdIs(String reltId_0);

  // ReltId =
  public Slice<ReltMain> findAllByReltIdIs(String reltId_0, Pageable pageable);

  // CaseNo =
  public Optional<ReltMain> findTopByCaseNoIs(int caseNo_0);

  // CustNo =
  public Slice<ReltMain> findAllByCustNoIs(int custNo_0, Pageable pageable);

  // CaseNo = ,AND CustNo = ,AND ReltId = 
  public Optional<ReltMain> findTopByCaseNoIsAndCustNoIsAndReltIdIs(int caseNo_0, int custNo_1, String reltId_2);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ReltMain> findByReltMainId(ReltMainId reltMainId);

}

