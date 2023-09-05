package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfReward;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfRewardRepositoryHist extends JpaRepository<PfReward, Long> {

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= 
  public Slice<PfReward> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, int facmNo_2, Pageable pageable);

  // CustNo = 
  public Slice<PfReward> findAllByCustNoIsOrderByFacmNoAsc(int custNo_0, Pageable pageable);

  // CustNo = ,AND FacmNo = 
  public Slice<PfReward> findAllByCustNoIsAndFacmNoIs(int custNo_0, int facmNo_1, Pageable pageable);

  // Introducer=
  public Slice<PfReward> findAllByIntroducerIsOrderByCustNoAscFacmNoAscBormNoAsc(String introducer_0, Pageable pageable);

  // WorkMonth>= , AND WorkMonth<=
  public Slice<PfReward> findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAscWorkMonthDesc(int workMonth_0, int workMonth_1, Pageable pageable);

  // Introducer= , AND WorkMonth>= , AND WorkMonth<=
  public Slice<PfReward> findAllByIntroducerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(String introducer_0, int workMonth_1, int workMonth_2, Pageable pageable);

  // PerfDate >= , AND PerfDate<= 
  public Slice<PfReward> findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int perfDate_0, int perfDate_1, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND PerfDate = ,AND RepayType = ,AND PieceCode = 
  public Optional<PfReward> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5);

  // CustNo = ,AND FacmNo = ,AND BormNo = 
  public Slice<PfReward> findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(int custNo_0, int facmNo_1, int bormNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfReward> findByLogNo(Long logNo);

}

