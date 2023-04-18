package com.st1.itx.db.repository.day;


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

import com.st1.itx.db.domain.BatxOthers;
import com.st1.itx.db.domain.BatxOthersId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxOthersRepositoryDay extends JpaRepository<BatxOthers, BatxOthersId> {

  // AcDate >= ,AND AcDate <= , AND BatchNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIs(int acDate_0, int acDate_1, String batchNo_2, Pageable pageable);

  // AcDate >= ,AND AcDate <= , AND BatchNo = , AND RepayCode = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIs(int acDate_0, int acDate_1, String batchNo_2, int repayCode_3, Pageable pageable);

  // AcDate >= ,AND AcDate <= , AND BatchNo = , AND CreateEmpNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndCreateEmpNoIs(int acDate_0, int acDate_1, String batchNo_2, String createEmpNo_3, Pageable pageable);

  // AcDate >= ,AND AcDate <= , AND BatchNo = , AND RepayCode = , AND CreateEmpNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(int acDate_0, int acDate_1, String batchNo_2, int repayCode_3, String createEmpNo_4, Pageable pageable);

  // AcDate =  ,AND BatchNo =
  public Optional<BatxOthers> findTopByAcDateIsAndBatchNoIsOrderByDetailSeqDesc(int acDate_0, String batchNo_1);

  // AcDate >= ,AND AcDate <= 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqual(int acDate_0, int acDate_1, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND RepayCode = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIs(int acDate_0, int acDate_1, int repayCode_2, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CreateEmpNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCreateEmpNoIs(int acDate_0, int acDate_1, String createEmpNo_2, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND RepayCode = ,AND CreateEmpNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndRepayCodeIsAndCreateEmpNoIs(int acDate_0, int acDate_1, int repayCode_2, String createEmpNo_3, Pageable pageable);

  // TitaEntdy = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Optional<BatxOthers> findTopByTitaEntdyIsAndTitaTlrNoIsAndTitaTxtNoIs(int titaEntdy_0, String titaTlrNo_1, String titaTxtNo_2);

  // AcDate >= ,AND AcDate <= ,AND CustNo = , AND BatchNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIs(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CustNo = , AND BatchNo = , AND RepayCode = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIs(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, int repayCode_4, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CustNo = , AND BatchNo = , AND CreateEmpNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndCreateEmpNoIs(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, String createEmpNo_4, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CustNo = , AND BatchNo = , AND RepayCode = , AND CreateEmpNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndBatchNoIsAndRepayCodeIsAndCreateEmpNoIs(int acDate_0, int acDate_1, int custNo_2, String batchNo_3, int repayCode_4, String createEmpNo_5, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CustNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIs(int acDate_0, int acDate_1, int custNo_2, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CustNo = , AND RepayCode = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIs(int acDate_0, int acDate_1, int custNo_2, int repayCode_3, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CustNo = , AND CreateEmpNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndCreateEmpNoIs(int acDate_0, int acDate_1, int custNo_2, String createEmpNo_3, Pageable pageable);

  // AcDate >= ,AND AcDate <= ,AND CustNo = , AND RepayCode = ,AND CreateEmpNo = 
  public Slice<BatxOthers> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCustNoIsAndRepayCodeIsAndCreateEmpNoIs(int acDate_0, int acDate_1, int custNo_2, int repayCode_3, String createEmpNo_4, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BatxOthers> findByBatxOthersId(BatxOthersId batxOthersId);

}

