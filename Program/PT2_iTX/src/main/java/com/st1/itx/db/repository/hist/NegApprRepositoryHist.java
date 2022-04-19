package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.NegAppr;
import com.st1.itx.db.domain.NegApprId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegApprRepositoryHist extends JpaRepository<NegAppr, NegApprId> {

	// YyyyMm>= , AND YyyyMm<=
	public Slice<NegAppr> findAllByYyyyMmGreaterThanEqualAndYyyyMmLessThanEqualOrderByYyyyMmAscKindCodeAsc(int yyyyMm_0, int yyyyMm_1, Pageable pageable);

	// YyyyMm=
	public Slice<NegAppr> findAllByYyyyMmIsOrderByYyyyMmAscKindCodeAsc(int yyyyMm_0, Pageable pageable);

	// ExportDate = , OR ApprAcDate = , OR BringUpDate =
	public Slice<NegAppr> findAllByExportDateIsOrApprAcDateIsOrBringUpDateIsOrderByYyyyMmAscKindCodeAsc(int exportDate_0, int apprAcDate_1, int bringUpDate_2, Pageable pageable);

	// BringUpDate =
	public Slice<NegAppr> findAllByBringUpDateIsOrderByYyyyMmAscKindCodeAsc(int bringUpDate_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<NegAppr> findByNegApprId(NegApprId negApprId);

}
