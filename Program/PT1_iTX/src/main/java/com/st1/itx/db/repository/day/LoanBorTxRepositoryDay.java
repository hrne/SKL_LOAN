package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanBorTxRepositoryDay extends JpaRepository<LoanBorTx, LoanBorTxId> {

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND AcDate >= ,AND AcDate <=,AND Displayflag  ^i
  public Slice<LoanBorTx> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndAcDateGreaterThanEqualAndAcDateLessThanEqualAndDisplayflagInOrderByAcDateAscTitaTlrNoAscTitaTxtNoAscDisplayflagAscFacmNoAscBormNoAscCreateDateAscDisplayflagAsc(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int acDate_5, int acDate_6, List<String> displayflag_7, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo >= ,AND BormNo <= 
  public Slice<LoanBorTx> findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBormNoAsc(int custNo_0, int facmNo_1, int bormNo_2, int bormNo_3, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND BorxNo >= ,AND BorxNo <=  
  public Slice<LoanBorTx> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoGreaterThanEqualAndBorxNoLessThanEqualOrderByBorxNoAsc(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, int borxNo_4, Pageable pageable);

  // AcDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Optional<LoanBorTx> findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(int acDate_0, String titaTlrNo_1, String titaTxtNo_2);

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND EntryDate >= ,AND EntryDate <=,AND Displayflag  ^i
  public Slice<LoanBorTx> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByAcDateAscTitaKinBrAscTitaTlrNoAscTitaTxtNoAscCreateDateAscDisplayflagAsc(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int entryDate_5, int entryDate_6, List<String> displayflag_7, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND AcDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Optional<LoanBorTx> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(int custNo_0, int facmNo_1, int bormNo_2, int acDate_3, String titaTlrNo_4, String titaTxtNo_5);

  // CustNo = ,AND FacmNo = ,AND BormNo = 
  public Optional<LoanBorTx> findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBorxNoDesc(int custNo_0, int facmNo_1, int bormNo_2);

  // CustNo = ,AND FacmNo = 
  public Slice<LoanBorTx> findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(int custNo_0, int facmNo_1, Pageable pageable);

  // CustNo = ,AND AcDate = ,AND TitaKinBr = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Slice<LoanBorTx> findAllByCustNoIsAndAcDateIsAndTitaKinBrIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByFacmNoAscBormNoAsc(int custNo_0, int acDate_1, String titaKinBr_2, String titaTlrNo_3, String titaTxtNo_4, Pageable pageable);

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND DueDate >= ,AND DueDate <= 
  public Slice<LoanBorTx> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndDueDateGreaterThanEqualAndDueDateLessThanEqualOrderByDueDateAscFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int dueDate_5, int dueDate_6, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND TitaHCode ^i
  public Slice<LoanBorTx> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndTitaHCodeInOrderByAcDateAscCustNoAscFacmNoAscBormNoAscBorxNoAsc(int acDate_0, int acDate_1, List<String> titaHCode_2, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo >= ,AND BormNo <= ,AND IntEndDate = ,AND TitaHCode ^i ,AND AcDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Slice<LoanBorTx> findAllByCustNoIsAndFacmNoIsAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndIntEndDateIsAndTitaHCodeInAndAcDateIsAndTitaTlrNoIsAndTitaTxtNoIsOrderByBormNoAsc(int custNo_0, int facmNo_1, int bormNo_2, int bormNo_3, int intEndDate_4, List<String> titaHCode_5, int acDate_6, String titaTlrNo_7, String titaTxtNo_8, Pageable pageable);

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND EntryDate >= ,AND EntryDate <=,AND Displayflag  ^i
  public Slice<LoanBorTx> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndEntryDateGreaterThanEqualAndEntryDateLessThanEqualAndDisplayflagInOrderByIntEndDateDescAcDateAscTitaKinBrAscTitaTlrNoAscTitaTxtNoAsc(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int entryDate_5, int entryDate_6, List<String> displayflag_7, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanBorTx> findByLoanBorTxId(LoanBorTxId loanBorTxId);

}

