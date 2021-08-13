package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsDp;
import com.st1.itx.db.domain.LoanIfrsDpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsDpRepositoryMon extends JpaRepository<LoanIfrsDp, LoanIfrsDpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIfrsDp> findByLoanIfrsDpId(LoanIfrsDpId loanIfrsDpId);

  // (月底日日終批次)維護 LoanIfrsDp IFRS9欄位清單4
  @Procedure(value = "\"Usp_L7_LoanIfrsDp_Upd\"")
  public void uspL7LoanifrsdpUpd(int TBSDYF, String EmpNo);

}

