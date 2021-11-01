package com.st1.itx.db.repository.hist;


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

import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.domain.MlaundryRecordId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MlaundryRecordRepositoryHist extends JpaRepository<MlaundryRecord, MlaundryRecordId> {

  // RecordDate >= ,AND RecordDate <= ,AND ActualRepayDate >= ,AND ActualRepayDate <= 
  public Slice<MlaundryRecord> findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualAndActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(int recordDate_0, int recordDate_1, int actualRepayDate_2, int actualRepayDate_3, Pageable pageable);

  // RecordDate >= ,AND RecordDate <= 
  public Slice<MlaundryRecord> findAllByRecordDateGreaterThanEqualAndRecordDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(int recordDate_0, int recordDate_1, Pageable pageable);

  // ActualRepayDate >= ,AND ActualRepayDate <=
  public Slice<MlaundryRecord> findAllByActualRepayDateGreaterThanEqualAndActualRepayDateLessThanEqualOrderByRecordDateAscActualRepayDateAsc(int actualRepayDate_0, int actualRepayDate_1, Pageable pageable);

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND BormNo >= ,AND BormNo <= ,AND RepayDate >=
  public Slice<MlaundryRecord> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndBormNoGreaterThanEqualAndBormNoLessThanEqualAndRepayDateGreaterThanEqualOrderByRepayDateAsc(int custNo_0, int facmNo_1, int facmNo_2, int bormNo_3, int bormNo_4, int repayDate_5, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MlaundryRecord> findByMlaundryRecordId(MlaundryRecordId mlaundryRecordId);

}

