package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegAppr01Repository extends JpaRepository<NegAppr01, NegAppr01Id> {

  // CustNo= 
  public Slice<NegAppr01> findAllByCustNoIsOrderByExportDateDescCustNoAsc(int custNo_0, Pageable pageable);

  // CustNo= , AND ExportDate =
  public Slice<NegAppr01> findAllByCustNoIsAndExportDateIsOrderByExportDateDescCustNoAsc(int custNo_0, int exportDate_1, Pageable pageable);

  // CustNo= , AND ExportDate>= , AND ExportDate <=
  public Slice<NegAppr01> findAllByCustNoIsAndExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(int custNo_0, int exportDate_1, int exportDate_2, Pageable pageable);

  // ExportDate=
  public Slice<NegAppr01> findAllByExportDateIsOrderByExportDateDescCustNoAsc(int exportDate_0, Pageable pageable);

  // ExportDate>= , AND ExportDate <=
  public Slice<NegAppr01> findAllByExportDateGreaterThanEqualAndExportDateLessThanEqualOrderByExportDateDescCustNoAsc(int exportDate_0, int exportDate_1, Pageable pageable);

  // BringUpDate=
  public Slice<NegAppr01> findAllByBringUpDateIs(int bringUpDate_0, Pageable pageable);

  // BatchTxtNo = , AND RemitBank = , AND ApprDate=
  public Slice<NegAppr01> findAllByBatchTxtNoIsAndRemitBankIsAndApprDateIs(String batchTxtNo_0, String remitBank_1, int apprDate_2, Pageable pageable);

  // CustNo = , AND CaseSeq = 
  public Slice<NegAppr01> findAllByCustNoIsAndCaseSeqIs(int custNo_0, int caseSeq_1, Pageable pageable);

  // CustNo = , AND CaseSeq = , AND FinCode=
  public Slice<NegAppr01> findAllByCustNoIsAndCaseSeqIsAndFinCodeIs(int custNo_0, int caseSeq_1, String finCode_2, Pageable pageable);

  // AcDate = , AND TitaTlrNo = ,AND TitaTxtNo=
  public Slice<NegAppr01> findAllByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(int acDate_0, String titaTlrNo_1, int titaTxtNo_2, Pageable pageable);

  // CustNo = , AND CaseSeq = , AND ExportDate=
  public Slice<NegAppr01> findAllByCustNoIsAndCaseSeqIsAndExportDateIsOrderByFinCodeAscCreateDateDesc(int custNo_0, int caseSeq_1, int exportDate_2, Pageable pageable);

  // ExportDate= , AND FinCode=
  public Slice<NegAppr01> findAllByExportDateIsAndFinCodeIsOrderByCustNoAsc(int exportDate_0, String finCode_1, Pageable pageable);

  // BringUpDate>= 
  public Optional<NegAppr01> findTopByBringUpDateGreaterThanEqualOrderByBringUpDateDesc(int bringUpDate_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<NegAppr01> findByNegAppr01Id(NegAppr01Id negAppr01Id);

}

