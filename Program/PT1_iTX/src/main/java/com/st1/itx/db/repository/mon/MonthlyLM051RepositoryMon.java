package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM051;
import com.st1.itx.db.domain.MonthlyLM051Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM051RepositoryMon extends JpaRepository<MonthlyLM051, MonthlyLM051Id> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<MonthlyLM051> findByMonthlyLM051Id(MonthlyLM051Id monthlyLM051Id);

	// (月底日日終批次)維護MonthlyLM051月報工作檔
	@Procedure(value = "\"Usp_L9_MonthlyLM051_Upd\"")
	public void uspL9Monthlylm051Upd(int TBSDYF, String empNo);

}
