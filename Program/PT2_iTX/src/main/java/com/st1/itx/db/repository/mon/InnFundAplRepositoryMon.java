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

import com.st1.itx.db.domain.InnFundApl;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InnFundAplRepositoryMon extends JpaRepository<InnFundApl, Integer> {

  // AcDate >= ,AND AcDate <= 
  public Slice<InnFundApl> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(int acDate_0, int acDate_1, Pageable pageable);

  // AcDate <= ,AND ResrvStndrd > 
  public Optional<InnFundApl> findTopByAcDateLessThanEqualAndResrvStndrdGreaterThanOrderByAcDateDesc(int acDate_0, BigDecimal resrvStndrd_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<InnFundApl> findByAcDate(int acDate);

}

