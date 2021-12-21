package com.st1.itx.db.repository.day;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Cp;
import com.st1.itx.db.domain.LoanIfrs9CpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9CpRepositoryDay extends JpaRepository<LoanIfrs9Cp, LoanIfrs9CpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrs9Cp> findByLoanIfrs9CpId(LoanIfrs9CpId loanIfrs9CpId);

	// (月底日日終批次)維護 LoanIfrsCp IFRS9資料欄位清單3
	@Procedure(value = "\"Usp_L7_LoanIfrs9Cp_Upd\"")
	public void uspL7Loanifrs9cpUpd(int TBSDYF, String EmpNo);

}
