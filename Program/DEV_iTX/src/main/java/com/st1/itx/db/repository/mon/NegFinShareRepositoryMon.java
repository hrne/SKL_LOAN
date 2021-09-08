package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.NegFinShare;
import com.st1.itx.db.domain.NegFinShareId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegFinShareRepositoryMon extends JpaRepository<NegFinShare, NegFinShareId> {

  // CustNo = , AND CaseSeq = 
  public Slice<NegFinShare> findAllByCustNoIsAndCaseSeqIsOrderByCustNoDescCaseSeqDesc(int custNo_0, int caseSeq_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<NegFinShare> findByNegFinShareId(NegFinShareId negFinShareId);

}

