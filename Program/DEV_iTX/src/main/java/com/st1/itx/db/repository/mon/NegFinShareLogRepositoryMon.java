package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.NegFinShareLog;
import com.st1.itx.db.domain.NegFinShareLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegFinShareLogRepositoryMon extends JpaRepository<NegFinShareLog, NegFinShareLogId> {

	// CustNo = , AND CaseSeq =
	public Slice<NegFinShareLog> findAllByCustNoIsAndCaseSeqIsOrderByCustNoAscCaseSeqAscSeqDescFinCodeAsc(int custNo_0, int caseSeq_1, Pageable pageable);

	// CustNo = , AND CaseSeq = ,AND Seq =
	public Slice<NegFinShareLog> findAllByCustNoIsAndCaseSeqIsAndSeqIs(int custNo_0, int caseSeq_1, int seq_2, Pageable pageable);

	// CustNo =
	public Slice<NegFinShareLog> findAllByCustNoIsOrderByCaseSeqDescSeqDescFinCodeAsc(int custNo_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<NegFinShareLog> findByNegFinShareLogId(NegFinShareLogId negFinShareLogId);

}
