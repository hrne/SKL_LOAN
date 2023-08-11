package com.st1.itx.db.repository.online;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.SlipMedia;
import com.st1.itx.db.domain.SlipMediaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface SlipMediaRepository extends JpaRepository<SlipMedia, SlipMediaId> {

  // AcDate = ,AND BatchNo = ,AND MediaSeq =
  public Slice<SlipMedia> findAllByAcDateIsAndBatchNoIsAndMediaSeqIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(int acDate_0, int batchNo_1, int mediaSeq_2, Pageable pageable);

  // AcDate = ,AND BatchNo = 
  public Slice<SlipMedia> findAllByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqAscMediaSlipNoAscAcBookCodeAscSeqAsc(int acDate_0, int batchNo_1, Pageable pageable);

  // AcDate = ,AND BatchNo = 
  public Optional<SlipMedia> findTopByAcDateIsAndBatchNoIsOrderByAcDateAscBatchNoAscMediaSeqDesc(int acDate_0, int batchNo_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<SlipMedia> findBySlipMediaId(SlipMediaId slipMediaId);

}

