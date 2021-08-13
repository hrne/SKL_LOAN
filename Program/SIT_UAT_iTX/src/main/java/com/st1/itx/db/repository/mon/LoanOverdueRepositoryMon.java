package com.st1.itx.db.repository.mon;

import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanOverdueRepositoryMon extends JpaRepository<LoanOverdue, LoanOverdueId> {

	// CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND
	// OvduNo >= ,AND OvduNo <=,AND Status ^i
	public Slice<LoanOverdue> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndOvduNoGreaterThanEqualAndOvduNoLessThanEqualAndStatusInOrderByAcDateAscFacmNoAscBormNoAsc(
			int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int ovduNo_5, int ovduNo_6, List<Integer> status_7, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanOverdue> findByLoanOverdueId(LoanOverdueId loanOverdueId);

}
