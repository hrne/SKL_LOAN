package com.st1.itx.db.repository.hist;


import java.util.Optional;

import java.math.BigDecimal;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacProdAcctFee;
import com.st1.itx.db.domain.FacProdAcctFeeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacProdAcctFeeRepositoryHist extends JpaRepository<FacProdAcctFee, FacProdAcctFeeId> {

  // ProdNo =  ,AND LoanLow >= ,AND LoanLow <=
  public Slice<FacProdAcctFee> findAllByProdNoIsAndLoanLowGreaterThanEqualAndLoanLowLessThanEqual(String prodNo_0, BigDecimal loanLow_1, BigDecimal loanLow_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FacProdAcctFee> findByFacProdAcctFeeId(FacProdAcctFeeId facProdAcctFeeId);

}

