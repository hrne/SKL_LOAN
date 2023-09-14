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

import com.st1.itx.db.domain.MonthlyFacBal;
import com.st1.itx.db.domain.MonthlyFacBalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyFacBalRepositoryHist extends JpaRepository<MonthlyFacBal, MonthlyFacBalId> {

  // ClCustNo=, AND ClFacmNo=
  public Slice<MonthlyFacBal> findAllByClCustNoIsAndClFacmNoIs(int clCustNo_0, int clFacmNo_1, Pageable pageable);

  // YearMonth = 
  public Slice<MonthlyFacBal> findAllByYearMonthIs(int yearMonth_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<MonthlyFacBal> findByMonthlyFacBalId(MonthlyFacBalId monthlyFacBalId);

  // (月底日日終批次)維護 MonthlyFacBal 額度月報工作檔
  @Procedure(value = "\"Usp_L9_MonthlyFacBal_Upd\"")
  public void uspL9MonthlyfacbalUpd(int YYYYMM,  String empNo);

  // 上傳檔案(L7交易)並更新MonthlyFacBal相關欄位
  @Procedure(value = "\"Usp_L7_UploadToMothlyFacBal_Upd\"")
  public void uspL7UploadtomothlyfacbalUpd(int YYYYMM,  String txcd,  String empNo,  String jobTxSeq);

}

