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

import com.st1.itx.db.domain.PfItDetail;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfItDetailRepositoryDay extends JpaRepository<PfItDetail, Long> {

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= 
  public Slice<PfItDetail> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, int facmNo_2, Pageable pageable);

  // WorkMonth >= ,AND WorkMonth<=
  public Slice<PfItDetail> findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int workMonth_0, int workMonth_1, Pageable pageable);

  // CustNo = ,AND FacmNo =
  public Slice<PfItDetail> findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, Pageable pageable);

  // CustNo = 
  public Slice<PfItDetail> findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(int custNo_0, Pageable pageable);

  // DrawdownDate >= ,AND DrawdownDate<=
  public Slice<PfItDetail> findAllByDrawdownDateGreaterThanEqualAndDrawdownDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int drawdownDate_0, int drawdownDate_1, Pageable pageable);

  // PerfDate>= ,AND PerfDate<= 
  public Slice<PfItDetail> findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int perfDate_0, int perfDate_1, Pageable pageable);

  // RewardDate= ,AND MediaFg = 
  public Slice<PfItDetail> findAllByRewardDateIsAndMediaFgIsOrderByIntroducerAscCustNoAscFacmNoAscBormNoAsc(int rewardDate_0, int mediaFg_1, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND PerfDate = ,AND RepayType = ,AND PieceCode = 
  public Optional<PfItDetail> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5);

  // CustNo = ,AND FacmNo = ,AND BormNo = 
  public Optional<PfItDetail> findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDescLogNoDesc(int custNo_0, int facmNo_1, int bormNo_2);

  // CustNo = ,AND FacmNo = ,AND BormNo = 
  public Optional<PfItDetail> findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAscLogNoAsc(int custNo_0, int facmNo_1, int bormNo_2);

  // CustNo = ,AND FacmNo = ,AND BormNo = 
  public Slice<PfItDetail> findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(int custNo_0, int facmNo_1, int bormNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfItDetail> findByLogNo(Long logNo);

}

