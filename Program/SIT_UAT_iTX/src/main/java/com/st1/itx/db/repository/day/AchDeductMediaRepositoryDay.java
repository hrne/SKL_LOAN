package com.st1.itx.db.repository.day;

import java.util.Optional;

import java.math.BigDecimal;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AchDeductMedia;
import com.st1.itx.db.domain.AchDeductMediaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AchDeductMediaRepositoryDay extends JpaRepository<AchDeductMedia, AchDeductMediaId> {

	// AcDate = , AND BatchNo = , AND DetailSeq =
	public Optional<AchDeductMedia> findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(int acDate_0, String batchNo_1, int detailSeq_2);

	// CustNo = ,AND FacmNo = ,AND AchRepayCode = ,AND PrevIntDate = ,AND RepayAmt =
	public Optional<AchDeductMedia> findTopByCustNoIsAndFacmNoIsAndAchRepayCodeIsAndPrevIntDateIsAndRepayAmtIsOrderByMediaDateDesc(int custNo_0, int facmNo_1, String achRepayCode_2, int prevIntDate_3,
			BigDecimal repayAmt_4);

	// MediaDate = , AND MediaKind =
	public Slice<AchDeductMedia> findAllByMediaDateIsAndMediaKindIsOrderByMediaSeqAsc(int mediaDate_0, String mediaKind_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<AchDeductMedia> findByAchDeductMediaId(AchDeductMediaId achDeductMediaId);

}
