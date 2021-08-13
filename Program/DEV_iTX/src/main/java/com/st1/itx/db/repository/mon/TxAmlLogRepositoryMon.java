package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxAmlLog;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlLogRepositoryMon extends JpaRepository<TxAmlLog, Long> {

  // BrNo = ,AND ConfirmStatus = ,AND Entdy >= ,AND Entdy <= 
  public Slice<TxAmlLog> findAllByBrNoIsAndConfirmStatusIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(String brNo_0, String confirmStatus_1, int entdy_2, int entdy_3, Pageable pageable);

  // BrNo = ,AND Entdy >= ,AND Entdy <= 
  public Slice<TxAmlLog> findAllByBrNoIsAndEntdyGreaterThanEqualAndEntdyLessThanEqualOrderByCreateDateAsc(String brNo_0, int entdy_1, int entdy_2, Pageable pageable);

  // Entdy = ,AND TransactionId = 
  public Optional<TxAmlLog> findTopByEntdyIsAndTransactionIdIsOrderByLogNoAsc(int entdy_0, String transactionId_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxAmlLog> findByLogNo(Long logNo);

}

