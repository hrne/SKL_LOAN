package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Ap;
import com.st1.itx.db.domain.LoanIfrs9ApId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9ApRepositoryDay extends JpaRepository<LoanIfrs9Ap, LoanIfrs9ApId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrs9Ap> findByLoanIfrs9ApId(LoanIfrs9ApId loanIfrs9ApId);

	// (月底日日終批次)維護 LoanIfrsAp IFRS9欄位清單1
	@Procedure(value = "\"Usp_L7_LoanIfrs9Ap_Upd\"")
	public void uspL7Loanifrs9apUpd(int TBSDYF, String EmpNo, int NewAcFg);

}
