package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Dp;
import com.st1.itx.db.domain.LoanIfrs9DpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9DpRepositoryDay extends JpaRepository<LoanIfrs9Dp, LoanIfrs9DpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrs9Dp> findByLoanIfrs9DpId(LoanIfrs9DpId loanIfrs9DpId);

	// (月底日日終批次)維護 LoanIfrsDp IFRS9欄位清單4
	@Procedure(value = "\"Usp_L7_LoanIfrs9Dp_Upd\"")
	public void uspL7Loanifrs9dpUpd(int TBSDYF, String EmpNo);

}
