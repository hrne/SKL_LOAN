package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.MonthlyLM032;
import com.st1.itx.db.domain.MonthlyLM032Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM032RepositoryHist extends JpaRepository<MonthlyLM032, MonthlyLM032Id> {

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<MonthlyLM032> findByMonthlyLM032Id(MonthlyLM032Id monthlyLM032Id);

	// (月底日日終批次)維護MonthlyLM032月報工作檔
	@Procedure(value = "\"Usp_L9_MonthlyLM032_Upd\"")
	public void uspL9Monthlylm032Upd(int TBSDYF, String empNo, int LMBSDYF);

}
