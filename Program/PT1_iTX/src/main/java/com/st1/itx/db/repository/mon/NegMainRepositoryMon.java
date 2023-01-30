package com.st1.itx.db.repository.mon;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface NegMainRepositoryMon extends JpaRepository<NegMain, NegMainId> {

  // CaseKindCode= , AND CustLoanKind= , AND Status= , AND CustNo=
  public Slice<NegMain> findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsAndCustNoIsOrderByCustNoDescCaseSeqAsc(String caseKindCode_0, String custLoanKind_1, String status_2, int custNo_3, Pageable pageable);

  // CaseKindCode= , AND CustLoanKind= , AND Status=
  public Slice<NegMain> findAllByCaseKindCodeIsAndCustLoanKindIsAndStatusIsOrderByCustNoDescCaseSeqAsc(String caseKindCode_0, String custLoanKind_1, String status_2, Pageable pageable);

  // CustNo=
  public Slice<NegMain> findAllByCustNoIsOrderByCustNoDescCaseSeqAsc(int custNo_0, Pageable pageable);

  // CaseKindCode=
  public Slice<NegMain> findAllByCaseKindCodeIsOrderByCustNoDescCaseSeqAsc(String caseKindCode_0, Pageable pageable);

  // CustLoanKind=
  public Slice<NegMain> findAllByCustLoanKindIsOrderByCustNoDescCaseSeqAsc(String custLoanKind_0, Pageable pageable);

  // Status=
  public Slice<NegMain> findAllByStatusIsOrderByCustNoDescCaseSeqAsc(String status_0, Pageable pageable);

  // CustNo=
  public Optional<NegMain> findTopByCustNoIsOrderByCaseSeqDesc(int custNo_0);

  // Status= ,AND CustNo= 
  public Optional<NegMain> findTopByStatusIsAndCustNoIsOrderByCaseSeqAsc(String status_0, int custNo_1);

  // Status^i , AND IsMainFin= , AND NextPayDate>= , AND NextPayDate<= , AND CustNo=
  public Slice<NegMain> findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualAndCustNoIsOrderByCustNoDescCaseSeqAsc(List<String> status_0, String isMainFin_1, int nextPayDate_2, int nextPayDate_3, int custNo_4, Pageable pageable);

  // Status^i , AND IsMainFin= , AND NextPayDate>= , AND NextPayDate<= 
  public Slice<NegMain> findAllByStatusInAndIsMainFinIsAndNextPayDateGreaterThanEqualAndNextPayDateLessThanEqualOrderByCustNoDescCaseSeqAsc(List<String> status_0, String isMainFin_1, int nextPayDate_2, int nextPayDate_3, Pageable pageable);

  // CustNo= , AND ApplDate= , AND MainFinCode= 
  public Slice<NegMain> findAllByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqAsc(int custNo_0, int applDate_1, String mainFinCode_2, Pageable pageable);

  // CustNo= , AND ApplDate= , AND MainFinCode= 
  public Optional<NegMain> findTopByCustNoIsAndApplDateIsAndMainFinCodeIsOrderByCustNoDescCaseSeqDesc(int custNo_0, int applDate_1, String mainFinCode_2);

  // CustNo= , AND CaseKindCode= 
  public Slice<NegMain> findAllByCustNoIsAndCaseKindCodeIsOrderByCaseSeqDesc(int custNo_0, String caseKindCode_1, Pageable pageable);

  // PayerCustNo=
  public Optional<NegMain> findTopByPayerCustNoIs(int payerCustNo_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<NegMain> findByNegMainId(NegMainId negMainId);

}

