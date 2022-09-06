package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Bp;
import com.st1.itx.db.domain.LoanIfrs9BpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9BpRepository extends JpaRepository<LoanIfrs9Bp, LoanIfrs9BpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrs9Bp> findByLoanIfrs9BpId(LoanIfrs9BpId loanIfrs9BpId);

	// (月底日日終批次)維護 LoanIfrsBp IFRS9資料欄位清單2
	@Procedure(value = "\"Usp_L7_LoanIfrs9Bp_Upd\"")
	public void uspL7Loanifrs9bpUpd(int TBSDYF, String EmpNo);

}
