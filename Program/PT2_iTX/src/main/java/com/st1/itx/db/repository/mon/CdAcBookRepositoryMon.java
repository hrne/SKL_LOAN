package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdAcBook;
import com.st1.itx.db.domain.CdAcBookId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAcBookRepositoryMon extends JpaRepository<CdAcBook, CdAcBookId> {

	// AcBookCode = ,AND AssignSeq >=
	public Slice<CdAcBook> findAllByAcBookCodeIsAndAssignSeqGreaterThanEqualOrderByAssignSeqAsc(String acBookCode_0, int assignSeq_1, Pageable pageable);

	// AcBookCode = ,AND AcSubBookCode =
	public Slice<CdAcBook> findAllByAcBookCodeIsAndAcSubBookCodeIsOrderByAcSubBookCodeAsc(String acBookCode_0, String acSubBookCode_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdAcBook> findByCdAcBookId(CdAcBookId cdAcBookId);

}
