package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface BankRmtfRepositoryMon extends JpaRepository<BankRmtf, BankRmtfId> {

  // TitaEntdy = ,AND TitaTlrNo = ,AND TitaTxtNo =
  public Optional<BankRmtf> findTopByTitaEntdyIsAndTitaTlrNoIsAndTitaTxtNoIs(int titaEntdy_0, String titaTlrNo_1, String titaTxtNo_2);

  // EntryDate >= ,AND AcDate <= 
  public Slice<BankRmtf> findAllByEntryDateGreaterThanEqualAndAcDateLessThanEqual(int entryDate_0, int acDate_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BankRmtf> findByBankRmtfId(BankRmtfId bankRmtfId);

}

