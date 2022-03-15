package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxRateChangeRepositoryHist extends JpaRepository<BatxRateChange, BatxRateChangeId> {

  // AdjDate >= ,AND AdjDate <= ,AND BaseRateCode = 
  public Slice<BatxRateChange> findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndBaseRateCodeIs(int adjDate_0, int adjDate_1, String baseRateCode_2, Pageable pageable);

  // AdjDate >= ,AND AdjDate <= ,AND CustCode >= ,AND CustCode <= 
  public Slice<BatxRateChange> findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqual(int adjDate_0, int adjDate_1, int custCode_2, int custCode_3, Pageable pageable);

  // CustCode >= ,AND CustCode <= ,AND TxKind >= ,AND TxKind <= ,AND AdjCode >= ,AND AdjCode <=   ,AND AdjDate >= ,AND AdjDate <=
  public Slice<BatxRateChange> findAllByCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindGreaterThanEqualAndTxKindLessThanEqualAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndAdjDateGreaterThanEqualAndAdjDateLessThanEqual(int custCode_0, int custCode_1, int txKind_2, int txKind_3, int adjCode_4, int adjCode_5, int adjDate_6, int adjDate_7, Pageable pageable);

  // AdjDate = ,AND TxKind = ,AND RateKeyInCode = 
  public Slice<BatxRateChange> findAllByAdjDateIsAndTxKindIsAndRateKeyInCodeIsOrderByCustNoAscFacmNoAscBormNoAsc(int adjDate_0, int txKind_1, int rateKeyInCode_2, Pageable pageable);

  // AdjDate >= ,AND AdjDate <= ,AND CustCode >= ,AND CustCode <= ,AND TxKind = ,AND AdjCode >= ,AND AdjCode <= ,AND ConfirmFlag = 
  public Slice<BatxRateChange> findAllByAdjDateGreaterThanEqualAndAdjDateLessThanEqualAndCustCodeGreaterThanEqualAndCustCodeLessThanEqualAndTxKindIsAndAdjCodeGreaterThanEqualAndAdjCodeLessThanEqualAndConfirmFlagIsOrderByCustNoAscFacmNoAscBormNoAsc(int adjDate_0, int adjDate_1, int custCode_2, int custCode_3, int txKind_4, int adjCode_5, int adjCode_6, int confirmFlag_7, Pageable pageable);

  // AdjDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Slice<BatxRateChange> findAllByAdjDateIsAndTitaTlrNoIsAndTitaTxtNoIs(int adjDate_0, String titaTlrNo_1, String titaTxtNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BatxRateChange> findByBatxRateChangeId(BatxRateChangeId batxRateChangeId);

}

