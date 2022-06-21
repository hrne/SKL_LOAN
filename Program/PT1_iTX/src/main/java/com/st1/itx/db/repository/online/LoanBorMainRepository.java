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

import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanBorMainRepository extends JpaRepository<LoanBorMain, LoanBorMainId> {

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= 
  public Slice<LoanBorMain> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, Pageable pageable);

  // CustNo = ,AND FacmNo ^i ,AND BormNo >= ,AND BormNo <= 
  public Slice<LoanBorMain> findAllByCustNoIsAndFacmNoInAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAsc(int custNo_0, List<Integer> facmNo_1, int bormNo_2, int bormNo_3, Pageable pageable);

  // DrawdownDate >= ,AND DrawdownDate <= ,AND BormNo >= ,AND BormNo <= ,AND Status ^i
  public Slice<LoanBorMain> findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndStatusInOrderByDrawdownDateAscCustNoAscFacmNoAscBormNoAsc(int drawdownDate_0, int drawdownDate_1, int bormNo_2, int bormNo_3, List<Integer> status_4, Pageable pageable);

  // NextPayIntDate >= ,AND NextPayIntDate <= ,AND Status = 
  public Slice<LoanBorMain> findAllByNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndStatusIsOrderByCustNoAscFacmNoAscBormNoAscNextPayIntDateAsc(int nextPayIntDate_0, int nextPayIntDate_1, int status_2, Pageable pageable);

  // Status ^i ,AND DrawdownDate >= ,AND DrawdownDate <=
  public Slice<LoanBorMain> findAllByStatusInAndDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(List<Integer> status_0, int drawdownDate_1, int drawdownDate_2, Pageable pageable);

  // AmortizedCode = ,AND Status = ,AND NextPayIntDate >= ,AND NextPayIntDate <= ,AND BormNo >= ,AND BormNo <= 
  public Slice<LoanBorMain> findAllByAmortizedCodeIsAndStatusIsAndNextPayIntDateGreaterThanEqualAndNextPayIntDateLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualOrderByFacmNoAscBormNoAscNextPayIntDateAsc(String amortizedCode_0, int status_1, int nextPayIntDate_2, int nextPayIntDate_3, int bormNo_4, int bormNo_5, Pageable pageable);

  // Status ^i ,AND CustNo = ,AND FacmNo >= ,AND FacmNo <=
  public Slice<LoanBorMain> findAllByStatusInAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByFacmNoAscBormNoAsc(List<Integer> status_0, int custNo_1, int facmNo_2, int facmNo_3, Pageable pageable);

  // CustNo = ,AND FacmNo = 
  public Slice<LoanBorMain> findAllByCustNoIsAndFacmNoIsOrderByBormNoAsc(int custNo_0, int facmNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanBorMain> findByLoanBorMainId(LoanBorMainId loanBorMainId);

}

