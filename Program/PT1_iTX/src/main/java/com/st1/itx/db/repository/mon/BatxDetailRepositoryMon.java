package com.st1.itx.db.repository.mon;


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

import com.st1.itx.db.domain.BatxDetail;
import com.st1.itx.db.domain.BatxDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxDetailRepositoryMon extends JpaRepository<BatxDetail, BatxDetailId> {

  // CustNo = ,AND AcDate = ,AND ProcStsCode ^i
  public Slice<BatxDetail> findAllByCustNoIsAndAcDateIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, int acDate_1, List<String> procStsCode_2, Pageable pageable);

  // AcDate = ,AND BatchNo = 
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(int acDate_0, String batchNo_1, Pageable pageable);

  // AcDate = ,AND BatchNo = ,AND TitaTlrNo = 
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsAndTitaTlrNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int acDate_0, String batchNo_1, String titaTlrNo_2, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CustNo >= ,AND CustNo <= ,AND RepayCode >= ,AND RepayCode <= ,AND ProcStsCode ^i 
  public Slice<BatxDetail> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndRepayCodeGreaterThanEqualAndRepayCodeLessThanEqualAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeAscDetailSeqAsc(int acDate_0, int acDate_1, int custNo_2, int custNo_3, int repayCode_4, int repayCode_5, List<String> procStsCode_6, Pageable pageable);

  // AcDate = ,AND BatchNo = 
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsOrderByMediaDateAscMediaKindAscMediaSeqAsc(int acDate_0, String batchNo_1, Pageable pageable);

  // AcDate = ,AND FileName = ,AND CustNo = ,AND RepayAmt = ,AND ProcStsCode ^i
  public Optional<BatxDetail> findTopByAcDateIsAndFileNameIsAndCustNoIsAndRepayAmtIsAndProcStsCodeInOrderByBatchNoDescDetailSeqDesc(int acDate_0, String fileName_1, int custNo_2, BigDecimal repayAmt_3, List<String> procStsCode_4);

  // AcDate = ,AND BatchNo = ,AND CustNo = ,AND ProcStsCode ^i
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(int acDate_0, String batchNo_1, int custNo_2, List<String> procStsCode_3, Pageable pageable);

  // AcDate = ,AND BatchNo = ,AND CustNo = ,AND ProcStsCode ^i
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsAndCustNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(int acDate_0, String batchNo_1, int custNo_2, List<String> procStsCode_3, Pageable pageable);

  // AcDate = ,AND BatchNo = ,AND ProcStsCode ^i
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(int acDate_0, String batchNo_1, List<String> procStsCode_2, Pageable pageable);

  // AcDate = ,AND BatchNo = ,AND ProcStsCode ^i
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(int acDate_0, String batchNo_1, List<String> procStsCode_2, Pageable pageable);

  // AcDate = ,AND BatchNo = ,AND ReconCode = ,AND ProcStsCode ^i
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoAscFacmNoAscRepayCodeDescDetailSeqAsc(int acDate_0, String batchNo_1, String reconCode_2, List<String> procStsCode_3, Pageable pageable);

  // AcDate = ,AND BatchNo = ,AND ReconCode = ,AND ProcStsCode ^i
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsAndReconCodeIsAndProcStsCodeInOrderByCustNoDescFacmNoDescRepayCodeAscDetailSeqDesc(int acDate_0, String batchNo_1, String reconCode_2, List<String> procStsCode_3, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND RepayCode = 
  public Slice<BatxDetail> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsOrderByBatchNoDescDetailSeqAsc(int acDate_0, int acDate_1, int repayCode_2, Pageable pageable);

  // AcDate = ,AND FileName = 
  public Slice<BatxDetail> findAllByAcDateIsAndFileNameIsOrderByBatchNoDesc(int acDate_0, String fileName_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BatxDetail> findByBatxDetailId(BatxDetailId batxDetailId);

}

