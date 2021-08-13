package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsHp;
import com.st1.itx.db.domain.LoanIfrsHpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsHpRepository extends JpaRepository<LoanIfrsHp, LoanIfrsHpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIfrsHp> findByLoanIfrsHpId(LoanIfrsHpId loanIfrsHpId);

  // (月底日日終批次)維護 LoanIfrsHp IFRS9欄位清單8
  @Procedure(value = "\"Usp_L7_LoanIfrsHp_Upd\"")
  public void uspL7LoanifrshpUpd(int TBSDYF, String EmpNo);

}

