package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ForeclosureFee;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ForeclosureFeeRepositoryMon extends JpaRepository<ForeclosureFee, Integer> {

  // CustNo = 
  public Slice<ForeclosureFee> findAllByCustNoIsOrderByCustNoAscReceiveDateAsc(int custNo_0, Pageable pageable);

  // ReceiveDate >= ,AND ReceiveDate <=
  public Slice<ForeclosureFee> findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualOrderByCustNoAscReceiveDateAsc(int receiveDate_0, int receiveDate_1, Pageable pageable);

  // RecordNo >= ,AND RecordNo <=
  public Optional<ForeclosureFee> findTopByRecordNoGreaterThanEqualAndRecordNoLessThanEqualOrderByRecordNoDesc(int recordNo_0, int recordNo_1);

  // ReceiveDate >= ,AND ReceiveDate <= ,AND CustNo >= ,AND CustNo <=
  public Slice<ForeclosureFee> findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscReceiveDateAsc(int receiveDate_0, int receiveDate_1, int custNo_2, int custNo_3, Pageable pageable);

  // OpenAcDate >= ,AND OpenAcDate <=
  public Slice<ForeclosureFee> findAllByOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByCustNoAscReceiveDateAsc(int openAcDate_0, int openAcDate_1, Pageable pageable);

  // CloseDate >= ,AND CloseDate <=
  public Slice<ForeclosureFee> findAllByCloseDateGreaterThanEqualAndCloseDateLessThanEqualOrderByCustNoAscReceiveDateAsc(int closeDate_0, int closeDate_1, Pageable pageable);

  // OverdueDate >= ,AND OverdueDate <=
  public Slice<ForeclosureFee> findAllByOverdueDateGreaterThanEqualAndOverdueDateLessThanEqualOrderByCustNoAscReceiveDateAsc(int overdueDate_0, int overdueDate_1, Pageable pageable);

  // CustNo =  ,And FacmNo = ,AND CloseDate = 
  public Slice<ForeclosureFee> findAllByCustNoIsAndFacmNoIsAndCloseDateIsOrderByRecordNoAsc(int custNo_0, int facmNo_1, int closeDate_2, Pageable pageable);

  // ReceiveDate >= ,AND ReceiveDate <= , AND CloseDate =
  public Slice<ForeclosureFee> findAllByReceiveDateGreaterThanEqualAndReceiveDateLessThanEqualAndCloseDateIsOrderByCustNoAscReceiveDateAsc(int receiveDate_0, int receiveDate_1, int closeDate_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ForeclosureFee> findByRecordNo(int recordNo);

}

