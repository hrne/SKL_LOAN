package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias39LoanCommit;
import com.st1.itx.db.domain.Ias39LoanCommitId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias39LoanCommitRepositoryHist extends JpaRepository<Ias39LoanCommit, Ias39LoanCommitId> {

  // DataYm = ,AND CustNo = ,AND FacmNo = ,AND ApplNo = 
  public Slice<Ias39LoanCommit> findAllByDataYmIsAndCustNoIsAndFacmNoIsAndApplNoIs(int dataYm_0, int custNo_1, int facmNo_2, int applNo_3, Pageable pageable);

  // DataYm = 
  public Slice<Ias39LoanCommit> findAllByDataYmIsOrderByAcBookCodeAsc(int dataYm_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ias39LoanCommit> findByIas39LoanCommitId(Ias39LoanCommitId ias39LoanCommitId);

  // LM011 更新IAS39放款承諾明細檔
  @Procedure(value = "\"Usp_L7_Ias39LoanCommit_Upd\"")
  public void uspL7Ias39loancommitUpd(int tbsdyf,  String empNo);

}

