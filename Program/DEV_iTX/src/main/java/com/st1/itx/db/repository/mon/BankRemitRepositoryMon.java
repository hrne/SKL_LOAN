package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.BankRemitId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRemitRepositoryMon extends JpaRepository<BankRemit, BankRemitId> {

  // AcDate >= ,AND AcDate <= 
  public Slice<BankRemit> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(int acDate_0, int acDate_1, Pageable pageable);

  // CustNo = 
  public Slice<BankRemit> findAllByCustNoIs(int custNo_0, Pageable pageable);

  // AcDate = ,AND BatchNo = ,AND DrawdownCode >= ,AND DrawdownCode <= ,AND StatusCode >= ,AND StatusCode <= 
  public Slice<BankRemit> findAllByAcDateIsAndBatchNoIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int acDate_0, String batchNo_1, int drawdownCode_2, int drawdownCode_3, int statusCode_4, int statusCode_5, Pageable pageable);

  // AcDate = ,AND DrawdownCode >= ,AND DrawdownCode <= ,AND StatusCode >= ,AND StatusCode <= 
  public Slice<BankRemit> findAllByAcDateIsAndDrawdownCodeGreaterThanEqualAndDrawdownCodeLessThanEqualAndStatusCodeGreaterThanEqualAndStatusCodeLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int acDate_0, int drawdownCode_1, int drawdownCode_2, int statusCode_3, int statusCode_4, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BankRemit> findByBankRemitId(BankRemitId bankRemitId);

}

