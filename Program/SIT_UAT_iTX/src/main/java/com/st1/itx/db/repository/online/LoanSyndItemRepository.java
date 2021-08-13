package com.st1.itx.db.repository.online;


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
public interface LoanSyndItemRepository extends JpaRepository<LoanSyndItem, LoanSyndItemId> {

  // CustNo = ,AND SyndNo =
  public Slice<LoanSyndItem> findAllByCustNoIsAndSyndNoIsOrderByItemAsc(int custNo_0, int syndNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LoanSyndItem> findByLoanSyndItemId(LoanSyndItemId loanSyndItemId);

}

