package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.DailyLoanBal;
import com.st1.itx.db.domain.DailyLoanBalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface DailyLoanBalRepository extends JpaRepository<DailyLoanBal, DailyLoanBalId> {

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND DataDate <= 
  public Optional<DailyLoanBal> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndDataDateLessThanEqualOrderByDataDateDesc(int custNo_0, int facmNo_1, int bormNo_2, int dataDate_3);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<DailyLoanBal> findByDailyLoanBalId(DailyLoanBalId dailyLoanBalId);

  // (日終批次)維護 DailyLoanBal每日放款餘額檔
  @Procedure(value = "\"Usp_L9_DailyLoanBal_Upd\"")
  public void uspL9DailyloanbalUpd(int tbsdyf,  String empNo, int mfbsdyf);

}

