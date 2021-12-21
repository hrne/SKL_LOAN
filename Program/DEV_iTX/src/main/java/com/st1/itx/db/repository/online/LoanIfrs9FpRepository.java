package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanIfrs9Fp;
import com.st1.itx.db.domain.LoanIfrs9FpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanIfrs9FpRepository extends JpaRepository<LoanIfrs9Fp, LoanIfrs9FpId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanIfrs9Fp> findByLoanIfrs9FpId(LoanIfrs9FpId loanIfrs9FpId);

	// (月底日日終批次)維護 LoanIfrsFp IFRS9資料欄位清單6
	@Procedure(value = "\"Usp_L7_LoanIfrs9Fp_Upd\"")
	public void uspL7Loanifrs9fpUpd(int TBSDYF, String EmpNo);

}
