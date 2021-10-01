package com.st1.itx.db.repository.mon;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanChequeRepositoryMon extends JpaRepository<LoanCheque, LoanChequeId> {

  // ChequeDate >= ,AND ChequeDate <= ,AND ChequeAcct >= ,AND ChequeAcct <= ,AND ChequeNo >= ,AND ChequeNo <=
  public Slice<LoanCheque> findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(int chequeDate_0, int chequeDate_1, int chequeAcct_2, int chequeAcct_3, int chequeNo_4, int chequeNo_5, Pageable pageable);

  // CustNo = ,AND StatusCode ^i ,AND  ChequeDate >= ,AND ChequeDate <=
  public Slice<LoanCheque> findAllByCustNoIsAndStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(int custNo_0, List<String> statusCode_1, int chequeDate_2, int chequeDate_3, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND StatusCode ^i 
  public Slice<LoanCheque> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndStatusCodeIn(int acDate_0, int acDate_1, List<String> statusCode_2, Pageable pageable);

  // StatusCode ^i ,AND  ChequeDate >= ,AND ChequeDate <=
  public Slice<LoanCheque> findAllByStatusCodeInAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeDateAscChequeAcctAscChequeNoAsc(List<String> statusCode_0, int chequeDate_1, int chequeDate_2, Pageable pageable);

  // ChequeDate >= ,AND ChequeDate <= ,AND ChequeAcct >= ,AND ChequeAcct <= ,AND ChequeNo >= ,AND ChequeNo <= ,AND StatusCode =
  public Slice<LoanCheque> findAllByChequeDateGreaterThanEqualAndChequeDateLessThanEqualAndChequeAcctGreaterThanEqualAndChequeAcctLessThanEqualAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndStatusCodeIsOrderByChequeDateAscCustNoAscChequeAcctAscChequeNoAsc(int chequeDate_0, int chequeDate_1, int chequeAcct_2, int chequeAcct_3, int chequeNo_4, int chequeNo_5, String statusCode_6, Pageable pageable);

  // ReceiveDate >= ,AND ReceiveDate <= ,AND StatusCode ^i 
  public Slice<LoanCheque> findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndStatusCodeIn(int receiveDate_0, int receiveDate_1, List<String> statusCode_2, Pageable pageable);

  // CustNo >= ,AND CustNo <= ,AND StatusCode ^i ,AND ChequeNo >= ,AND ChequeNo <= ,AND  ChequeDate >= ,AND ChequeDate <=
  public Slice<LoanCheque> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndStatusCodeInAndChequeNoGreaterThanEqualAndChequeNoLessThanEqualAndChequeDateGreaterThanEqualAndChequeDateLessThanEqualOrderByChequeAcctAscChequeNoAsc(int custNo_0, int custNo_1, List<String> statusCode_2, int chequeNo_3, int chequeNo_4, int chequeDate_5, int chequeDate_6, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanCheque> findByLoanChequeId(LoanChequeId loanChequeId);

}

