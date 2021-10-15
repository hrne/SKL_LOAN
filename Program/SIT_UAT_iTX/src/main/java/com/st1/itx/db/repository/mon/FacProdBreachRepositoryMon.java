package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacProdBreach;
import com.st1.itx.db.domain.FacProdBreachId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdBreachRepositoryMon extends JpaRepository<FacProdBreach, FacProdBreachId> {

	// BreachNo = ,AND BreachCode >= ,AND BreachCode <=
	public Slice<FacProdBreach> findAllByBreachNoIsAndBreachCodeGreaterThanEqualAndBreachCodeLessThanEqual(String breachNo_0, String breachCode_1, String breachCode_2, Pageable pageable);

	// BreachNo = ,AND BreachCode = ,AND MonthStart <= ,AND MonthEnd >
	public Optional<FacProdBreach> findTopByBreachNoIsAndBreachCodeIsAndMonthStartLessThanEqualAndMonthEndGreaterThan(String breachNo_0, String breachCode_1, int monthStart_2, int monthEnd_3);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<FacProdBreach> findByFacProdBreachId(FacProdBreachId facProdBreachId);

}
