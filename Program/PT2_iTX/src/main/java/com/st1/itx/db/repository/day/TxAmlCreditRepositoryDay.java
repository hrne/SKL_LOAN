package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxAmlCredit;
import com.st1.itx.db.domain.TxAmlCreditId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlCreditRepositoryDay extends JpaRepository<TxAmlCredit, TxAmlCreditId> {

  // ReviewType ^i ,AND DataDt >= ,AND DataDt <= ,AND ProcessType %
  public Slice<TxAmlCredit> findAllByReviewTypeInAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeOrderByDataDtAscCustKeyAsc(List<String> reviewType_0, int dataDt_1, int dataDt_2, String processType_3, Pageable pageable);

  // ReviewType ^i ,AND DataDt >= ,AND DataDt <= ,AND ProcessType % ,AND ProcessCount =
  public Slice<TxAmlCredit> findAllByReviewTypeInAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountIsOrderByDataDtAscCustKeyAsc(List<String> reviewType_0, int dataDt_1, int dataDt_2, String processType_3, int processCount_4, Pageable pageable);

  // ReviewType ^i ,AND DataDt >= ,AND DataDt <= ,AND ProcessType % ,AND ProcessCount >
  public Slice<TxAmlCredit> findAllByReviewTypeInAndDataDtGreaterThanEqualAndDataDtLessThanEqualAndProcessTypeLikeAndProcessCountGreaterThanOrderByDataDtAscCustKeyAsc(List<String> reviewType_0, int dataDt_1, int dataDt_2, String processType_3, int processCount_4, Pageable pageable);

  // DataDt =
  public Slice<TxAmlCredit> findAllByDataDtIs(int dataDt_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxAmlCredit> findByTxAmlCreditId(TxAmlCreditId txAmlCreditId);

}

