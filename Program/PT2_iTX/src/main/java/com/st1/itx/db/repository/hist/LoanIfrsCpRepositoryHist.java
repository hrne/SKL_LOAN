package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrsCp;
import com.st1.itx.db.domain.LoanIfrsCpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrsCpRepositoryHist extends JpaRepository<LoanIfrsCp, LoanIfrsCpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrsCp> findByLoanIfrsCpId(LoanIfrsCpId loanIfrsCpId);

	// (月底日日終批次)維護 LoanIfrsCp IFRS9資料欄位清單3
	@Procedure(value = "\"Usp_L7_LoanIfrsCp_Upd\"")
	public void uspL7LoanifrscpUpd(int TBSDYF, String EmpNo);

}
