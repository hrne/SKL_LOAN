package com.st1.itx.db.repository.day;


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

import com.st1.itx.db.domain.PfIntranetAdjust;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfIntranetAdjustRepositoryDay extends JpaRepository<PfIntranetAdjust, Long> {

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND WorkMonth =
  public Optional<PfIntranetAdjust> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndWorkMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, int bormNo_2, int workMonth_3);

  // WorkMonth =
  public Slice<PfIntranetAdjust> findAllByWorkMonthIsOrderByCustNoAscFacmNoAscBormNoAsc(int workMonth_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfIntranetAdjust> findByLogNo(Long logNo);

}

