package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdPerformance;
import com.st1.itx.db.domain.CdPerformanceId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdPerformanceRepository extends JpaRepository<CdPerformance, CdPerformanceId> {

	// WorkMonth = ,AND PieceCode >= ,AND PieceCode <=
	public Slice<CdPerformance> findAllByWorkMonthIsAndPieceCodeGreaterThanEqualAndPieceCodeLessThanEqualOrderByPieceCodeAsc(int workMonth_0, String pieceCode_1, String pieceCode_2,
			Pageable pageable);

	// WorkMonth =
	public Slice<CdPerformance> findAllByWorkMonthIs(int workMonth_0, Pageable pageable);

	// WorkMonth <=
	public Optional<CdPerformance> findTopByWorkMonthLessThanEqualOrderByWorkMonthDesc(int workMonth_0);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdPerformance> findByCdPerformanceId(CdPerformanceId cdPerformanceId);

}
