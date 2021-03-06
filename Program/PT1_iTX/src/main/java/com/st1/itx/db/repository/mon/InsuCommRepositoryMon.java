package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.InsuComm;
import com.st1.itx.db.domain.InsuCommId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuCommRepositoryMon extends JpaRepository<InsuComm, InsuCommId> {

	// InsuYearMonth >= ,AND InsuYearMonth <=
	public Slice<InsuComm> findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByNowInsuNoAscInsuCateAsc(int insuYearMonth_0, int insuYearMonth_1, Pageable pageable);

	// InsuYearMonth = , AND CommDate >= , AND CommDate <=
	public Slice<InsuComm> findAllByInsuYearMonthIsAndCommDateGreaterThanEqualAndCommDateLessThanEqualOrderByNowInsuNoAscInsuCateAsc(int insuYearMonth_0, int commDate_1, int commDate_2,
			Pageable pageable);

	// CustNo =
	public Slice<InsuComm> findAllByCustNoIsOrderByInsuYearMonthAscNowInsuNoAscInsuCateAsc(int custNo_0, Pageable pageable);

	// FireOfficer =
	public Slice<InsuComm> findAllByFireOfficerIsOrderByInsuYearMonthAscNowInsuNoAscInsuCateAsc(String fireOfficer_0, Pageable pageable);

	// EmpId =
	public Slice<InsuComm> findAllByEmpIdIsOrderByInsuYearMonthAscNowInsuNoAscInsuCateAsc(String empId_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<InsuComm> findByInsuCommId(InsuCommId insuCommId);

}
