package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfDetail;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfDetailRepositoryDay extends JpaRepository<PfDetail, Long> {

  // PerfDate>= , AND PerfDate<= 
  public Slice<PfDetail> findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int perfDate_0, int perfDate_1, Pageable pageable);

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND WorkMonth >= ,AND WorkMonth <=
  public Slice<PfDetail> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAscBorxNoAsc(int custNo_0, int facmNo_1, int facmNo_2, int workMonth_3, int workMonth_4, Pageable pageable);

  // WorkMonth >= ,AND WorkMonth <=
  public Slice<PfDetail> findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int workMonth_0, int workMonth_1, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND BorxNo = 
  public Slice<PfDetail> findAllByCustNoIsAndFacmNoIsAndBormNoIsAndBorxNoIsOrderByPerfDateAsc(int custNo_0, int facmNo_1, int bormNo_2, int borxNo_3, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo =
  public Slice<PfDetail> findAllByCustNoIsAndFacmNoIsAndBormNoIs(int custNo_0, int facmNo_1, int bormNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfDetail> findByLogNo(Long logNo);

}

