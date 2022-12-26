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

import com.st1.itx.db.domain.BankRmtf;
import com.st1.itx.db.domain.BankRmtfId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRmtfRepositoryHist extends JpaRepository<BankRmtf, BankRmtfId> {

  // AcDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Optional<BankRmtf> findTopByAcDateIsAndTitaTlrNoIsAndTitaTxtNoIs(int acDate_0, String titaTlrNo_1, String titaTxtNo_2);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BankRmtf> findByBankRmtfId(BankRmtfId bankRmtfId);

}

