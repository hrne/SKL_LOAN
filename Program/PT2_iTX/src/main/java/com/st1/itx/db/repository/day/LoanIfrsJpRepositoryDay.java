package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsJp;
import com.st1.itx.db.domain.LoanIfrsJpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsJpRepositoryDay extends JpaRepository<LoanIfrsJp, LoanIfrsJpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrsJp> findByLoanIfrsJpId(LoanIfrsJpId loanIfrsJpId);

	// (月底日日終批次)維護 LoanIfrsJp IFRS9欄位清單10
	@Procedure(value = "\"Usp_L7_LoanIfrsJp_Upd\"")
	public void uspL7LoanifrsjpUpd(int TBSDYF, String EmpNo);

}
