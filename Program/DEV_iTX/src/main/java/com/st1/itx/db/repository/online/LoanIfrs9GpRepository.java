package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Gp;
import com.st1.itx.db.domain.LoanIfrs9GpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9GpRepository extends JpaRepository<LoanIfrs9Gp, LoanIfrs9GpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIfrs9Gp> findByLoanIfrs9GpId(LoanIfrs9GpId loanIfrs9GpId);

  // (月底日日終批次)維護 LoanIfrsGp IFRS9資料欄位清單7
  @Procedure(value = "\"Usp_L7_LoanIfrs9Gp_Upd\"")
  public void uspL7Loanifrs9gpUpd(int TBSDYF, String EmpNo);

}

