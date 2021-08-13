package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsAp;
import com.st1.itx.db.domain.LoanIfrsApId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsApRepository extends JpaRepository<LoanIfrsAp, LoanIfrsApId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIfrsAp> findByLoanIfrsApId(LoanIfrsApId loanIfrsApId);

  // (月底日日終批次)維護 LoanIfrsAp IFRS9欄位清單1
  @Procedure(value = "\"Usp_L7_LoanIfrsAp_Upd\"")
  public void uspL7LoanifrsapUpd(int TBSDYF, String EmpNo, int NewAcFg);

}

