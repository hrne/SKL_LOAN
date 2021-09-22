package com.st1.itx.db.repository.online;


import java.util.Optional;

import java.math.BigDecimal;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.EmpDeductMediaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface EmpDeductMediaRepository extends JpaRepository<EmpDeductMedia, EmpDeductMediaId> {

  // AcDate = ,AND BatchNo = ,AND DetailSeq = 
  public Optional<EmpDeductMedia> findTopByAcDateIsAndBatchNoIsAndDetailSeqIs(int acDate_0, String batchNo_1, int detailSeq_2);

  // MediaKind = ,AND CustNo = ,AND EntryDate = ,AND RepayCode = ,AND RepayAmt = 
  public Optional<EmpDeductMedia> findTopByMediaKindIsAndCustNoIsAndEntryDateIsAndRepayCodeIsAndRepayAmtIs(String mediaKind_0, int custNo_1, int entryDate_2, int repayCode_3, BigDecimal repayAmt_4);

  // MediaDate >= , AND MediaDate <= , AND MediaKind = 
  public Slice<EmpDeductMedia> findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualAndMediaKindIsOrderByMediaSeqAsc(int mediaDate_0, int mediaDate_1, String mediaKind_2, Pageable pageable);

  // AcDate = ,AND PerfMonth = ,AND FlowCode = 
  public Slice<EmpDeductMedia> findAllByAcDateIsAndPerfMonthIsAndFlowCodeIsOrderByMediaSeqAsc(int acDate_0, int perfMonth_1, String flowCode_2, Pageable pageable);

  // MediaDate = ,AND MediaKind = 
  public Optional<EmpDeductMedia> findTopByMediaDateIsAndMediaKindIsOrderByMediaSeqDesc(int mediaDate_0, String mediaKind_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<EmpDeductMedia> findByEmpDeductMediaId(EmpDeductMediaId empDeductMediaId);

}

