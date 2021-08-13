package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfBsDetail;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfBsDetailRepository extends JpaRepository<PfBsDetail, Long> {

  // CustNo = ,AND FacmNo >= ,AND FacmNo <=
  public Slice<PfBsDetail> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByPerfDateAscFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, int facmNo_2, Pageable pageable);

  // BsOfficer = ,AND WorkMonth>= ,AND WorkMonth <=
  public Slice<PfBsDetail> findAllByBsOfficerIsAndWorkMonthGreaterThanEqualAndWorkMonthLessThanEqual(String bsOfficer_0, int workMonth_1, int workMonth_2, Pageable pageable);

  // BsOfficer= ,AND WorkMonth=
  public Slice<PfBsDetail> findAllByBsOfficerIsAndWorkMonthIs(String bsOfficer_0, int workMonth_1, Pageable pageable);

  // PerfDate>= ,AND PerfDate <=
  public Slice<PfBsDetail> findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByPerfDateAscDeptCodeAscBsOfficerAsc(int perfDate_0, int perfDate_1, Pageable pageable);

  // BsOfficer= ,AND PerfDate>= ,AND PerfDate <=
  public Slice<PfBsDetail> findAllByBsOfficerIsAndPerfDateGreaterThanEqualAndPerfDateLessThanEqual(String bsOfficer_0, int perfDate_1, int perfDate_2, Pageable pageable);

  // PerfDate>= ,AND PerfDate <=
  public Slice<PfBsDetail> findAllByPerfDateGreaterThanEqualAndPerfDateLessThanEqualOrderByCustNoAscFacmNoAsc(int perfDate_0, int perfDate_1, Pageable pageable);

  // WorkMonth >= , AND WorkMonth<=
  public Slice<PfBsDetail> findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByCustNoAscFacmNoAscBormNoAsc(int workMonth_0, int workMonth_1, Pageable pageable);

  // CustNo = ,AND FacmNo =
  public Slice<PfBsDetail> findAllByCustNoIsAndFacmNoIsOrderByCustNoAscFacmNoAscBormNoAsc(int custNo_0, int facmNo_1, Pageable pageable);

  // CustNo = 
  public Slice<PfBsDetail> findAllByCustNoIsOrderByCustNoAscFacmNoAscBormNoAsc(int custNo_0, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND BormNo = ,AND PerfDate = ,AND RepayType = ,AND PieceCode = 
  public Optional<PfBsDetail> findTopByCustNoIsAndFacmNoIsAndBormNoIsAndPerfDateIsAndRepayTypeIsAndPieceCodeIs(int custNo_0, int facmNo_1, int bormNo_2, int perfDate_3, int repayType_4, String pieceCode_5);

  // CustNo = ,AND FacmNo = ,AND BormNo = 
  public Optional<PfBsDetail> findTopByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateDesc(int custNo_0, int facmNo_1, int bormNo_2);

  // CustNo = ,AND FacmNo = ,AND BormNo = 
  public Slice<PfBsDetail> findAllByCustNoIsAndFacmNoIsAndBormNoIsOrderByPerfDateAsc(int custNo_0, int facmNo_1, int bormNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfBsDetail> findByLogNo(Long logNo);

}

