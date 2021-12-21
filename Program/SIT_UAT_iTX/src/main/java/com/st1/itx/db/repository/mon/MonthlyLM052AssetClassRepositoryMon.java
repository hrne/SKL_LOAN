package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM052AssetClass;
import com.st1.itx.db.domain.MonthlyLM052AssetClassId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM052AssetClassRepositoryMon extends JpaRepository<MonthlyLM052AssetClass, MonthlyLM052AssetClassId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<MonthlyLM052AssetClass> findByMonthlyLM052AssetClassId(MonthlyLM052AssetClassId monthlyLM052AssetClassId);

	//
	@Procedure(value = "\"Usp_L9_MonthlyLM052AssetClass_Ins\"")
	public void uspL9Monthlylm052assetclassIns(int tbsdyf, String empNo);

}
