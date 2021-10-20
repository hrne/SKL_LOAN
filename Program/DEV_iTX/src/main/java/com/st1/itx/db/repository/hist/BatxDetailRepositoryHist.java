package com.st1.itx.db.repository.hist;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface BatxDetailRepositoryHist extends JpaRepository<BatxDetail, BatxDetailId> {

  // CustNo = ,AND FacmNo = ,AND RepayCode = 
  public Slice<BatxDetail> findAllByCustNoIsAndFacmNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, int facmNo_1, int repayCode_2, Pageable pageable);

  // CustNo = ,AND FacmNo = 
  public Slice<BatxDetail> findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, int facmNo_1, Pageable pageable);

  // CustNo = 
  public Slice<BatxDetail> findAllByCustNoIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, Pageable pageable);

  // RepayCode = 
  public Slice<BatxDetail> findAllByRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int repayCode_0, Pageable pageable);

  // CustNo = ,AND RepayCode = 
  public Slice<BatxDetail> findAllByCustNoIsAndRepayCodeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, int repayCode_1, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND RepayCode = ,AND RepayType = 
  public Slice<BatxDetail> findAllByCustNoIsAndFacmNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, int facmNo_1, int repayCode_2, int repayType_3, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND RepayType = 
  public Slice<BatxDetail> findAllByCustNoIsAndFacmNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, int facmNo_1, int repayType_2, Pageable pageable);

  // CustNo = ,AND RepayType = 
  public Slice<BatxDetail> findAllByCustNoIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, int repayType_1, Pageable pageable);

  // RepayCode = ,AND RepayType = 
  public Slice<BatxDetail> findAllByRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int repayCode_0, int repayType_1, Pageable pageable);

  // CustNo = ,AND RepayCode = ,AND RepayType = 
  public Slice<BatxDetail> findAllByCustNoIsAndRepayCodeIsAndRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int custNo_0, int repayCode_1, int repayType_2, Pageable pageable);

  // RepayType = 
  public Slice<BatxDetail> findAllByRepayTypeIsOrderByCustNoAscFacmNoAscRepayCodeAsc(int repayType_0, Pageable pageable);

  // AcDate = ,AND BatchNo = 
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIs(int acDate_0, String batchNo_1, Pageable pageable);

  // AcDate = ,AND BatchNo = ,AND TitaTlrNo = 
  public Slice<BatxDetail> findAllByAcDateIsAndBatchNoIsAndTitaTlrNoIs(int acDate_0, String batchNo_1, String titaTlrNo_2, Pageable pageable);

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
  public Slice<BatxDetail> findAllByAcDateIsAndFileNameIsOrderByBatchNoDescRecordSeqAsc(int acDate_0, String fileName_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BatxDetail> findByBatxDetailId(BatxDetailId batxDetailId);

}

