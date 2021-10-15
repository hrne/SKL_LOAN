package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.FacProdStepRateId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdStepRateRepositoryMon extends JpaRepository<FacProdStepRate, FacProdStepRateId> {

	// ProdNo = ,AND MonthStart >= ,AND MonthStart <=
	public Slice<FacProdStepRate> findAllByProdNoIsAndMonthStartGreaterThanEqualAndMonthStartLessThanEqual(String prodNo_0, int monthStart_1, int monthStart_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<FacProdStepRate> findByFacProdStepRateId(FacProdStepRateId facProdStepRateId);

}
