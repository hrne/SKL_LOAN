package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.SlipMedia2022;
import com.st1.itx.db.domain.SlipMedia2022Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface SlipMedia2022RepositoryDay extends JpaRepository<SlipMedia2022, SlipMedia2022Id> {

  // AcDate = ,AND BatchNo = ,AND MediaSeq = ,AND LatestFlag = 
  public Slice<SlipMedia2022> findAllByAcDateIsAndBatchNoIsAndMediaSeqIsAndLatestFlagIsOrderByMediaSlipNoAscSeqAsc(int acDate_0, int batchNo_1, int mediaSeq_2, String latestFlag_3, Pageable pageable);

  // AcDate = ,AND BatchNo = 
  public Slice<SlipMedia2022> findAllByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(int acDate_0, int batchNo_1, Pageable pageable);

  // AcDate = ,AND BatchNo = 
  public Optional<SlipMedia2022> findTopByAcDateIsAndBatchNoIsOrderByMediaSlipNoAscSeqAsc(int acDate_0, int batchNo_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<SlipMedia2022> findBySlipMedia2022Id(SlipMedia2022Id slipMedia2022Id);

  // 傳票號碼回寫會計明細檔
  @Procedure(value = "\"Usp_L7_SlipMedia_Upd\"")
  public void uspL7SlipmediaUpd(int tbsdyf,  String empNo, int acDate, int batchNo);

}

