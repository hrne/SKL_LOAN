package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsFp;
import com.st1.itx.db.domain.LoanIfrsFpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsFpRepositoryMon extends JpaRepository<LoanIfrsFp, LoanIfrsFpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrsFp> findByLoanIfrsFpId(LoanIfrsFpId loanIfrsFpId);

	// (月底日日終批次)維護 LoanIfrsFp IFRS9資料欄位清單6
	@Procedure(value = "\"Usp_L7_LoanIfrsFp_Upd\"")
	public void uspL7LoanifrsfpUpd(int TBSDYF, String EmpNo);

}
