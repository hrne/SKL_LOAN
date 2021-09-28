package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LoanSyndItem;
import com.st1.itx.db.domain.LoanSyndItemId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LoanSyndItemRepositoryDay extends JpaRepository<LoanSyndItem, LoanSyndItemId> {

  // SyndNo =
  public Slice<LoanSyndItem> findAllBySyndNoIsOrderBySyndSeqAsc(int syndNo_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanSyndItem> findByLoanSyndItemId(LoanSyndItemId loanSyndItemId);

}

