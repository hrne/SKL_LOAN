package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ifrs9LoanData;
import com.st1.itx.db.domain.Ifrs9LoanDataId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ifrs9LoanDataRepositoryHist extends JpaRepository<Ifrs9LoanData, Ifrs9LoanDataId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ifrs9LoanData> findByIfrs9LoanDataId(Ifrs9LoanDataId ifrs9LoanDataId);

  // (月底日日終批次)維護IFRS9撥款資料檔
  @Procedure(value = "\"Usp_L7_Ifrs9LoanData_Upd\"")
  public void uspL7Ifrs9loandataUpd(int TBSDYF, String EmpNo);

}

