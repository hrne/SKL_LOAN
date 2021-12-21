package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FinReportCashFlow;
import com.st1.itx.db.domain.FinReportCashFlowId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportCashFlowRepositoryMon extends JpaRepository<FinReportCashFlow, FinReportCashFlowId> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<FinReportCashFlow> findByFinReportCashFlowId(FinReportCashFlowId finReportCashFlowId);

}
