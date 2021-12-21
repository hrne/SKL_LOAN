package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustCross;
import com.st1.itx.db.domain.CustCrossId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustCrossRepositoryMon extends JpaRepository<CustCross, CustCrossId> {

	// CustUKey =
	public Slice<CustCross> findAllByCustUKeyIsOrderByCustUKeyAscSubCompanyCodeAsc(String custUKey_0, Pageable pageable);

	// CustUKey = ,AND SubCompanyCode =
	public Optional<CustCross> findTopByCustUKeyIsAndSubCompanyCodeIs(String custUKey_0, String subCompanyCode_1);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CustCross> findByCustCrossId(CustCrossId custCrossId);

}
