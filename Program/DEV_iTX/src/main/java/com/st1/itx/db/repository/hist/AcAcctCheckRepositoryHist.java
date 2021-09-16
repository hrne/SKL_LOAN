package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AcAcctCheck;
import com.st1.itx.db.domain.AcAcctCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcAcctCheckRepositoryHist extends JpaRepository<AcAcctCheck, AcAcctCheckId> {

  // AcDate = 
  public Slice<AcAcctCheck> findAllByAcDateIsOrderByAcctCodeAsc(int acDate_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<AcAcctCheck> findByAcAcctCheckId(AcAcctCheckId acAcctCheckId);

  // (放款關帳 )維護 AcAcctCheck 會計業務檢核檔
  @Procedure(value = "\"Usp_L6_AcAcctCheck_Upd\"")
  public void uspL6AcacctcheckUpd(int tbsdyf,  String empNo);

}

