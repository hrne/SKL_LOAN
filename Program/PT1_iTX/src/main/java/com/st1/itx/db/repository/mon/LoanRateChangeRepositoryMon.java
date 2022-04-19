package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanRateChangeRepositoryMon extends JpaRepository<LoanRateChange, LoanRateChangeId> {

	// AcDate = ,AND TellerNo = ,AND TxtNo =
	public Slice<LoanRateChange> findAllByAcDateIsAndTellerNoIsAndTxtNoIs(int acDate_0, String tellerNo_1, String txtNo_2, Pageable pageable);

	// CustNo = ,AND FacmNo = ,AND BormNo = ,AND EffectDate <=
	public Optional<LoanRateChange> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateLessThanEqualOrderByEffectDateDesc(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3);

	// CustNo = ,AND FacmNo = ,AND BormNo = ,AND EffectDate >=
	public Optional<LoanRateChange> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3);

	// CustNo = ,AND FacmNo = ,AND BormNo = ,AND EffectDate >=
	public Slice<LoanRateChange> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3,
			Pageable pageable);

	// CustNo = ,AND FacmNo = ,AND BormNo = ,AND EffectDate =
	public Optional<LoanRateChange> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndEffectDateIs(int custNo_0, int facmNo_1, int bormNo_2, int effectDate_3);

	// CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND
	// EffectDate >= ,AND EffectDate <=
	public Slice<LoanRateChange> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(
			int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int effectDate_5, int effectDate_6, Pageable pageable);

	// CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND
	// EffectDate >= ,AND EffectDate <=
	public Slice<LoanRateChange> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByFacmNoAscBormNoAscEffectDateAsc(
			int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int effectDate_5, int effectDate_6, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<LoanRateChange> findByLoanRateChangeId(LoanRateChangeId loanRateChangeId);

}
