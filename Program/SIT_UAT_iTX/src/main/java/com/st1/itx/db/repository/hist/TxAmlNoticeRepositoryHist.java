package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxAmlNotice;
import com.st1.itx.db.domain.TxAmlNoticeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlNoticeRepositoryHist extends JpaRepository<TxAmlNotice, TxAmlNoticeId> {

	// DataDt = ,AND CustKey =
	public Slice<TxAmlNotice> findAllByDataDtIsAndCustKeyIsOrderByProcessSnoAsc(int dataDt_0, String custKey_1, Pageable pageable);

	// DataDt =
	public Slice<TxAmlNotice> findAllByDataDtIsOrderByDataDtAscCustKeyAscProcessSnoAsc(int dataDt_0, Pageable pageable);

	// ProcessDate = ,
	public Slice<TxAmlNotice> findAllByProcessDateIsOrderByDataDtAscCustKeyAscProcessSnoAsc(int processDate_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<TxAmlNotice> findByTxAmlNoticeId(TxAmlNoticeId txAmlNoticeId);

}
