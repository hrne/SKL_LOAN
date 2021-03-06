package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM052LoanAsset;
import com.st1.itx.db.domain.MonthlyLM052LoanAssetId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM052LoanAssetRepositoryHist extends JpaRepository<MonthlyLM052LoanAsset, MonthlyLM052LoanAssetId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<MonthlyLM052LoanAsset> findByMonthlyLM052LoanAssetId(MonthlyLM052LoanAssetId monthlyLM052LoanAssetId);

	//
	@Procedure(value = "\"Usp_L9_MonthlyLM052LoanAsset_Ins\"")
	public void uspL9Monthlylm052loanassetIns(int tbsdyf, String empNo);

}
