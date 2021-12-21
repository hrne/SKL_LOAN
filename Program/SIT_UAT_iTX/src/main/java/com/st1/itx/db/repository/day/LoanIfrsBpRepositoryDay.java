package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsBp;
import com.st1.itx.db.domain.LoanIfrsBpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsBpRepositoryDay extends JpaRepository<LoanIfrsBp, LoanIfrsBpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrsBp> findByLoanIfrsBpId(LoanIfrsBpId loanIfrsBpId);

	// (月底日日終批次)維護 LoanIfrsBp IFRS9資料欄位清單2
	@Procedure(value = "\"Usp_L7_LoanIfrsBp_Upd\"")
	public void uspL7LoanifrsbpUpd(int TBSDYF, String EmpNo);

}
