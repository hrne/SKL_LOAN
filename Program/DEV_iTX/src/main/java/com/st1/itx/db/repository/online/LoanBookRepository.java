package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBookId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanBookRepository extends JpaRepository<LoanBook, LoanBookId> {

  // BookDate >= ,AND BookDate <=
  public Slice<LoanBook> findAllByBookDateGreaterThanEqualAndBookDateLessThanEqual(int bookDate_0, int bookDate_1, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo =
  public Optional<LoanBook> findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByBookDateAsc(int custNo_0, int facmNo_1, int bormNo_2);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND BookDate >= ,AND BookDate <=
  public Slice<LoanBook> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBookDateGreaterThanEqualAndBookDateLessThanEqualOrderByBookDateAsc(int custNo_0, int facmNo_1, int bormNo_2, int bookDate_3, int bookDate_4, Pageable pageable);

  // CustNo >= ,AND CustNo <= ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <=
  public Slice<LoanBook> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByCustNoAscFacmNoAscBormNoAscBookDateAsc(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, int bormNo_4, int bormNo_5, Pageable pageable);

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <=
  public Optional<LoanBook> findTopByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByBookDateDesc(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanBook> findByLoanBookId(LoanBookId loanBookId);

}

