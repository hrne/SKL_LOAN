package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.BankAuthAct;
import com.st1.itx.db.domain.BankAuthActId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankAuthActRepository extends JpaRepository<BankAuthAct, BankAuthActId> {

	// CustNo = , AND RepayAcct = , AND FacmNo >=, AND FacmNo <=
	public Slice<BankAuthAct> findAllByCustNoIsAndRepayAcctIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(int custNo_0, String repayAcct_1, int facmNo_2, int facmNo_3,
			Pageable pageable);

	// CustNo = , AND FacmNo =
	public Slice<BankAuthAct> findAllByCustNoIsAndFacmNoIs(int custNo_0, int facmNo_1, Pageable pageable);

	// CustNo = , AND RepayAcct = , AND RepayBank = , AND FacmNo >=, AND FacmNo <=
	public Slice<BankAuthAct> findAllByCustNoIsAndRepayAcctIsAndRepayBankIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAsc(int custNo_0, String repayAcct_1, String repayBank_2,
			int facmNo_3, int facmNo_4, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<BankAuthAct> findByBankAuthActId(BankAuthActId bankAuthActId);

}
