package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CollRemind;
import com.st1.itx.db.domain.CollRemindId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollRemindRepositoryHist extends JpaRepository<CollRemind, CollRemindId> {

  // CaseCode= , AND CustNo = , AND FacmNo = , AND CondCode =
  public Slice<CollRemind> findAllByCaseCodeIsAndCustNoIsAndFacmNoIsAndCondCodeIsOrderByRemindDateDesc(String caseCode_0, int custNo_1, int facmNo_2, String condCode_3, Pageable pageable);

  // CaseCode= , AND CustNo = , AND CondCode =
  public Slice<CollRemind> findAllByCaseCodeIsAndCustNoIsAndCondCodeIsOrderByRemindDateDesc(String caseCode_0, int custNo_1, String condCode_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CollRemind> findByCollRemindId(CollRemindId collRemindId);

}

