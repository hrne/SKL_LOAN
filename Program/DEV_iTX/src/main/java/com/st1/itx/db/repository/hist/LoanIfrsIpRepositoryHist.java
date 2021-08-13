package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsIp;
import com.st1.itx.db.domain.LoanIfrsIpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsIpRepositoryHist extends JpaRepository<LoanIfrsIp, LoanIfrsIpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanIfrsIp> findByLoanIfrsIpId(LoanIfrsIpId loanIfrsIpId);

  // (月底日日終批次)維護 LoanIfrsIp IFRS9欄位清單9
  @Procedure(value = "\"Usp_L7_LoanIfrsIp_Upd\"")
  public void uspL7LoanifrsipUpd(int TBSDYF, String EmpNo, int NewAcFg);

}

