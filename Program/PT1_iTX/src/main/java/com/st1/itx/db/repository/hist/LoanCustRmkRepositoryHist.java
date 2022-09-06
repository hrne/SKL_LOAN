package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanCustRmk;
import com.st1.itx.db.domain.LoanCustRmkId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanCustRmkRepositoryHist extends JpaRepository<LoanCustRmk, LoanCustRmkId> {

	// CustNo =
	public Slice<LoanCustRmk> findAllByCustNoIsOrderByCustNoAscAcDateAscRmkNoAsc(int custNo_0, Pageable pageable);

	// RmkCode =
	public Slice<LoanCustRmk> findAllByRmkCodeIsOrderByCustNoAscAcDateAscRmkNoAsc(String rmkCode_0, Pageable pageable);

	// CustNo =
	public Optional<LoanCustRmk> findTopByCustNoIsOrderByRmkNoDesc(int custNo_0);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanCustRmk> findByLoanCustRmkId(LoanCustRmkId loanCustRmkId);

}
