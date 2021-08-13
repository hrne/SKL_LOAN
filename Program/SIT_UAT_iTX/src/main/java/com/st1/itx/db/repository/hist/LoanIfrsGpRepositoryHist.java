package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsGp;
import com.st1.itx.db.domain.LoanIfrsGpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsGpRepositoryHist extends JpaRepository<LoanIfrsGp, LoanIfrsGpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIfrsGp> findByLoanIfrsGpId(LoanIfrsGpId loanIfrsGpId);

  // (月底日日終批次)維護 LoanIfrsGp IFRS9資料欄位清單7
  @Procedure(value = "\"Usp_L7_LoanIfrsGp_Upd\"")
  public void uspL7LoanifrsgpUpd(int TBSDYF, String EmpNo);

}

