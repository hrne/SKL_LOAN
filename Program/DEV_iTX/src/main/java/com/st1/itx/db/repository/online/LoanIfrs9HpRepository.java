package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Hp;
import com.st1.itx.db.domain.LoanIfrs9HpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9HpRepository extends JpaRepository<LoanIfrs9Hp, LoanIfrs9HpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIfrs9Hp> findByLoanIfrs9HpId(LoanIfrs9HpId loanIfrs9HpId);

  // (月底日日終批次)維護 LoanIfrsHp IFRS9欄位清單8
  @Procedure(value = "\"Usp_L7_LoanIfrs9Hp_Upd\"")
  public void uspL7Loanifrs9hpUpd(int TBSDYF, String EmpNo);

}

