package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FinReportProfit;
import com.st1.itx.db.domain.FinReportProfitId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportProfitRepositoryHist extends JpaRepository<FinReportProfit, FinReportProfitId> {

	// Ukey =
	public Optional<FinReportProfit> findTopByUkeyIs(String ukey_0);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<FinReportProfit> findByFinReportProfitId(FinReportProfitId finReportProfitId);

}
