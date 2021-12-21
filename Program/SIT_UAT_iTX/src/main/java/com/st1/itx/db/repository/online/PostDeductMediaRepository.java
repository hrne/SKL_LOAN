package com.st1.itx.db.repository.online;

import java.util.Optional;

import java.math.BigDecimal;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PostDeductMedia;
import com.st1.itx.db.domain.PostDeductMediaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PostDeductMediaRepository extends JpaRepository<PostDeductMedia, PostDeductMediaId> {

	// AcDate = , AND BatchNo = , AND DetailSeq =
	public Optional<PostDeductMedia> findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(int acDate_0, String batchNo_1, int detailSeq_2);

	// PostUserNo = ,AND RepayAmt = ,AND OutsrcRemark =
	public Optional<PostDeductMedia> findTopByPostUserNoIsAndRepayAmtIsAndOutsrcRemarkIsOrderByMediaDateDesc(String postUserNo_0, BigDecimal repayAmt_1, String outsrcRemark_2);

	// MediaDate =
	public Slice<PostDeductMedia> findAllByMediaDateIsOrderByMediaSeqAsc(int mediaDate_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<PostDeductMedia> findByPostDeductMediaId(PostDeductMediaId postDeductMediaId);

}
