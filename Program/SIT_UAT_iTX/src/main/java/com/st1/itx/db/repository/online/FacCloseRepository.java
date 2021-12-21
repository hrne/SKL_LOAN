package com.st1.itx.db.repository.online;

import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacCloseRepository extends JpaRepository<FacClose, FacCloseId> {

	// CustNo =
	public Slice<FacClose> findAllByCustNoIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(int custNo_0, Pageable pageable);

	// CustNo = ,AND FacmNo =
	public Slice<FacClose> findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscCloseDateAsc(int custNo_0, int facmNo_1, Pageable pageable);

	// CloseDate >= ,AND CloseDate <=
	public Slice<FacClose> findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(int closeDate_0, int closeDate_1, Pageable pageable);

	// CustNo =
	public Optional<FacClose> findTopByCustNoIsOrderByCloseNoDesc(int custNo_0);

	// EntryDate =
	public Slice<FacClose> findAllByEntryDateIsOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(int entryDate_0, Pageable pageable);

	// CloseDate = ,AND CloseNo >= ,AND CloseNo <= ,AND CarLoan >= ,AND CarLoan <=
	public Slice<FacClose> findAllByCloseDateIsAndCloseNoGreaterThanEqualAndCloseNoLessThanEqualAndCarLoanGreaterThanEqualAndCarLoanLessThanEqualOrderByCustNoAscFacmNoAscCloseDateAsc(int closeDate_0,
			int closeNo_1, int closeNo_2, int carLoan_3, int carLoan_4, Pageable pageable);

	// CustNo = ,AND FacmNo = ,AND FunCode ^i
	public Optional<FacClose> findTopByCustNoIsAndFacmNoIsAndFunCodeInOrderByCloseNoDesc(int custNo_0, int facmNo_1, List<String> funCode_2);

	// ApplDate >= ,AND ApplDate <=
	public Slice<FacClose> findAllByApplDateGreaterThanEqualAndApplDateLessThanEqualOrderByCustNoAscFacmNoAscCloseNoAscCloseDateAsc(int applDate_0, int applDate_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<FacClose> findByFacCloseId(FacCloseId facCloseId);

}
