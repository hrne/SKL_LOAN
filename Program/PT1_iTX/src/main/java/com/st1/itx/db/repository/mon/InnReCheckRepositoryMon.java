package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.InnReCheck;
import com.st1.itx.db.domain.InnReCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InnReCheckRepositoryMon extends JpaRepository<InnReCheck, InnReCheckId> {

	// YearMonth = ,AND ConditionCode = ,AND CustNo >= ,AND CustNo <=
	public Slice<InnReCheck> findAllByYearMonthIsAndConditionCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(int yearMonth_0, int conditionCode_1, int custNo_2,
			int custNo_3, Pageable pageable);

	// YearMonth >= ,AND YearMonth <= ,AND CustNo >= ,AND CustNo <=
	public Slice<InnReCheck> findAllByYearMonthGreaterThanEqualAndYearMonthLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByYearMonthAscConditionCodeAscCustNoAscFacmNoAsc(
			int yearMonth_0, int yearMonth_1, int custNo_2, int custNo_3, Pageable pageable);

	// TraceMonth >= ,AND TraceMonth <=
	public Slice<InnReCheck> findAllByTraceMonthGreaterThanEqualAndTraceMonthLessThanEqualOrderByYearMonthAscConditionCodeAscCustNoAscFacmNoAsc(int traceMonth_0, int traceMonth_1, Pageable pageable);

	// ConditionCode = ,AND YearMonth=
	public Slice<InnReCheck> findAllByConditionCodeIsAndYearMonthIsOrderByConditionCodeAscCustNoAscFacmNoAsc(int conditionCode_0, int yearMonth_1, Pageable pageable);

	// ConditionCode = ,AND YearMonth= ,AND CustNo=
	public Slice<InnReCheck> findAllByConditionCodeIsAndYearMonthIsAndCustNoIsOrderByConditionCodeAscCustNoAscFacmNoAsc(int conditionCode_0, int yearMonth_1, int custNo_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<InnReCheck> findByInnReCheckId(InnReCheckId innReCheckId);

	// (????????????)?????? InnReCheck ?????????????????????
	@Procedure(value = "\"Usp_L5_InnReCheck_Upd\"")
	public void uspL5InnrecheckUpd(int tbsdyf, String empNo);

}
