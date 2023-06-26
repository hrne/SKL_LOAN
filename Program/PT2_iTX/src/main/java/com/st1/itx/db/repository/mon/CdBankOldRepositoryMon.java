package com.st1.itx.db.repository.mon;


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

import com.st1.itx.db.domain.CdBankOld;
import com.st1.itx.db.domain.CdBankOldId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBankOldRepositoryMon extends JpaRepository<CdBankOld, CdBankOldId> {

  // BankCode %
  public Slice<CdBankOld> findAllByBankCodeLikeOrderByBankCodeAsc(String bankCode_0, Pageable pageable);

  // BankCode % ,AND BranchCode %
  public Slice<CdBankOld> findAllByBankCodeLikeAndBranchCodeLikeOrderByBankCodeAscBranchCodeAsc(String bankCode_0, String branchCode_1, Pageable pageable);

  // BankItem %
  public Slice<CdBankOld> findAllByBankItemLikeOrderByBankCodeAsc(String bankItem_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdBankOld> findByCdBankOldId(CdBankOldId cdBankOldId);

}

