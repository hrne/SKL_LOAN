package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AcLoanRenew;
import com.st1.itx.db.domain.AcLoanRenewId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcLoanRenewRepositoryMon extends JpaRepository<AcLoanRenew, AcLoanRenewId> {

	// CustNo = ,AND NewFacmNo >= ,AND NewFacmNo <= ,AND NewBormNo >= ,AND NewBormNo
	// <=
	public Slice<AcLoanRenew> findAllByCustNoIsAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndNewBormNoGreaterThanEqualAndNewBormNoLessThanEqualOrderByNewFacmNoAscNewBormNoAsc(int custNo_0,
			int newFacmNo_1, int newFacmNo_2, int newBormNo_3, int newBormNo_4, Pageable pageable);

	// CustNo >= ,AND CustNo <= ,AND OldFacmNo >= ,AND OldFacmNo <= ,AND NewFacmNo
	// >= ,AND NewFacmNo <= , AND AcDate >=, AND AcDate <=
	public Slice<AcLoanRenew> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndOldFacmNoGreaterThanEqualAndOldFacmNoLessThanEqualAndNewFacmNoGreaterThanEqualAndNewFacmNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAscAcDateAsc(
			int custNo_0, int custNo_1, int oldFacmNo_2, int oldFacmNo_3, int newFacmNo_4, int newFacmNo_5, int acDate_6, int acDate_7, Pageable pageable);

	// CustNo =
	public Slice<AcLoanRenew> findAllByCustNoIsOrderByCustNoAscOldFacmNoAscOldBormNoAscNewFacmNoAscNewBormNoAsc(int custNo_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<AcLoanRenew> findByAcLoanRenewId(AcLoanRenewId acLoanRenewId);

}
