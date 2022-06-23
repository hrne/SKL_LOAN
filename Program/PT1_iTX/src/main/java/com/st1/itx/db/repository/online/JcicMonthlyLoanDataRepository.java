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

import com.st1.itx.db.domain.JcicMonthlyLoanData;
import com.st1.itx.db.domain.JcicMonthlyLoanDataId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicMonthlyLoanDataRepository extends JpaRepository<JcicMonthlyLoanData, JcicMonthlyLoanDataId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicMonthlyLoanData> findByJcicMonthlyLoanDataId(JcicMonthlyLoanDataId jcicMonthlyLoanDataId);

  // (月底日日終批次)維護 JcicMonthlyLoanData 聯徵放款月報資料檔
  @Procedure(value = "\"Usp_L8_JcicMonthlyLoanData_Upd\"")
  public void uspL8JcicmonthlyloandataUpd(int TBSDYF, String EmpNo);

}

