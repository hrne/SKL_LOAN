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

import com.st1.itx.db.domain.TxInquiry;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxInquiryRepositoryHist extends JpaRepository<TxInquiry, Long> {

  // CalDate >= ,AND CalDate <= 
  public Slice<TxInquiry> findAllByCalDateGreaterThanEqualAndCalDateLessThanEqualOrderByCreateDateAsc(int calDate_0, int calDate_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxInquiry> findByLogNo(Long logNo);

}

