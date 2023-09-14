package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegTransRepositoryDay extends JpaRepository<NegTrans, NegTransId> {

  // CustNo = 
  public Slice<NegTrans> findAllByCustNoIsOrderByCaseSeqDescAcDateDesc(int custNo_0, Pageable pageable);

  // AcDate >= , AND AcDate <=
  public Slice<NegTrans> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(int acDate_0, int acDate_1, Pageable pageable);

  // CustNo = , AND AcDate >= , AND AcDate <=
  public Slice<NegTrans> findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(int custNo_0, int acDate_1, int acDate_2, Pageable pageable);

  // EntryDate >= , AND EntryDate <=
  public Slice<NegTrans> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(int entryDate_0, int entryDate_1, Pageable pageable);

  // CustNo = , AND EntryDate >= , AND EntryDate <=
  public Slice<NegTrans> findAllByCustNoIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByCustNoAscCaseSeqDescAcDateDesc(int custNo_0, int entryDate_1, int entryDate_2, Pageable pageable);

  // TxStatus>= , AND TxStatus<=
  public Slice<NegTrans> findAllByTxStatusGreaterThanEqualAndTxStatusLessThanEqual(int txStatus_0, int txStatus_1, Pageable pageable);

  // TxStatus=  , AND RepayDate=
  public Slice<NegTrans> findAllByTxStatusIsAndRepayDateIs(int txStatus_0, int repayDate_1, Pageable pageable);

  // RepayDate>= , AND RepayDate<=
  public Slice<NegTrans> findAllByRepayDateGreaterThanEqualAndRepayDateLessThanEqual(int repayDate_0, int repayDate_1, Pageable pageable);

  // RepayDate=
  public Slice<NegTrans> findAllByRepayDateIs(int repayDate_0, Pageable pageable);

  // ExportDate>= , AND ExportDate<=
  public Slice<NegTrans> findAllByExportDateGreaterThanEqualAndExportDateLessThanEqual(int exportDate_0, int exportDate_1, Pageable pageable);

  // ExportDate=
  public Slice<NegTrans> findAllByExportDateIs(int exportDate_0, Pageable pageable);

  // ExportAcDate=
  public Slice<NegTrans> findAllByExportAcDateIsOrderByCaseSeqDescAcDateDesc(int exportAcDate_0, Pageable pageable);

  // ThisEntdy = , AND ThisKinbr = , AND ThisTlrNo = , AND ThisTxtNo = , AND ThisSeqNo = 
  public Slice<NegTrans> findAllByThisEntdyIsAndThisKinbrIsAndThisTlrNoIsAndThisTxtNoIsAndThisSeqNoIsOrderByThisEntdyAscThisSeqNoAsc(int thisEntdy_0, String thisKinbr_1, String thisTlrNo_2, String thisTxtNo_3, String thisSeqNo_4, Pageable pageable);

  // AcDate =, AND TitaTxtNo =
  public Slice<NegTrans> findAllByAcDateIsAndTitaTxtNoIs(int acDate_0, int titaTxtNo_1, Pageable pageable);

  // CustNo = , AND CaseSeq =
  public Slice<NegTrans> findAllByCustNoIsAndCaseSeqIsOrderByEntryDateDescAcDateDesc(int custNo_0, int caseSeq_1, Pageable pageable);

  // CustNo = , AND CaseSeq = , AND EntryDate >= , AND EntryDate <=
  public Slice<NegTrans> findAllByCustNoIsAndCaseSeqIsAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualOrderByEntryDateDescAcDateDesc(int custNo_0, int caseSeq_1, int entryDate_2, int entryDate_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<NegTrans> findByNegTransId(NegTransId negTransId);

}

