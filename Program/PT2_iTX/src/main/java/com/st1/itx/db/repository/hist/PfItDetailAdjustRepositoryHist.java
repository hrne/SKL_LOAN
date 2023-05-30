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

import com.st1.itx.db.domain.PfItDetailAdjust;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfItDetailAdjustRepositoryHist extends JpaRepository<PfItDetailAdjust, Long> {

  // CustNo = ,AND FacmNo = ,AND BormNo = 
  public Optional<PfItDetailAdjust> findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByWorkMonthAsc(int custNo_0, int facmNo_1, int bormNo_2);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND WorkMonth =
  public Optional<PfItDetailAdjust> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndWorkMonthIs(int custNo_0, int facmNo_1, int bormNo_2, int workMonth_3);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfItDetailAdjust> findByLogNo(Long logNo);

}

