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

import com.st1.itx.db.domain.TxAmlRating;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAmlRatingRepositoryHist extends JpaRepository<TxAmlRating, Long> {

  // CaseNo = ,
  public Slice<TxAmlRating> findAllByCaseNoIsOrderByCaseNoAsc(String caseNo_0, Pageable pageable);

  // CaseNo = ,
  public Optional<TxAmlRating> findTopByCaseNoIsOrderByCaseNoAsc(String caseNo_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxAmlRating> findByLogNo(Long logNo);

}

