package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Jp;
import com.st1.itx.db.domain.LoanIfrs9JpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9JpRepositoryDay extends JpaRepository<LoanIfrs9Jp, LoanIfrs9JpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIfrs9Jp> findByLoanIfrs9JpId(LoanIfrs9JpId loanIfrs9JpId);

  // (月底日日終批次)維護 LoanIfrsJp IFRS9欄位清單10
  @Procedure(value = "\"Usp_L7_LoanIfrs9Jp_Upd\"")
  public void uspL7Loanifrs9jpUpd(int TBSDYF, String EmpNo);

}

