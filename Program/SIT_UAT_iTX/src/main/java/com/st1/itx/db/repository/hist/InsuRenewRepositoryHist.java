package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuRenewRepositoryHist extends JpaRepository<InsuRenew, InsuRenewId> {

  // AcDate = 
  public Slice<InsuRenew> findAllByAcDateIsOrderByInsuEndDateDescInsuStartDateAsc(int acDate_0, Pageable pageable);

  // AcDate = ,AND RepayCode = 
  public Slice<InsuRenew> findAllByAcDateIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int acDate_0, int repayCode_1, Pageable pageable);

  // InsuYearMonth = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, Pageable pageable);

  // InsuYearMonth = ,AND RepayCode = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndRepayCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int repayCode_1, Pageable pageable);

  // InsuYearMonth = ,AND AcDate = ,AND StatusCode = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int acDate_1, int statusCode_2, Pageable pageable);

  // InsuYearMonth = ,AND AcDate > ,AND StatusCode = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int acDate_1, int statusCode_2, Pageable pageable);

  // InsuYearMonth = ,AND StatusCode =
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int statusCode_1, Pageable pageable);

  // InsuYearMonth = ,AND RenewCode = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int renewCode_1, Pageable pageable);

  // InsuYearMonth = ,AND RepayCode = ,AND AcDate = ,AND StatusCode = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int repayCode_1, int acDate_2, int statusCode_3, Pageable pageable);

  // InsuYearMonth = ,AND RepayCode = ,AND AcDate > ,AND StatusCode = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndRepayCodeIsAndAcDateGreaterThanAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int repayCode_1, int acDate_2, int statusCode_3, Pageable pageable);

  // InsuYearMonth = ,AND RepayCode = ,AND StatusCode =
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndRepayCodeIsAndStatusCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int repayCode_1, int statusCode_2, Pageable pageable);

  // InsuYearMonth = ,AND RepayCode = ,AND RenewCode = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndRepayCodeIsAndRenewCodeIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int repayCode_1, int renewCode_2, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<InsuRenew> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // CustNo = 
  public Slice<InsuRenew> findAllByCustNoIsOrderByFacmNoAscInsuEndDateDescInsuStartDateAsc(int custNo_0, Pageable pageable);

  // CustNo = ,AND FacmNo = 
  public Slice<InsuRenew> findAllByCustNoIsAndFacmNoIsOrderByInsuEndDateDescInsuStartDateAsc(int custNo_0, int facmNo_1, Pageable pageable);

  // NowInsuNo = 
  public Slice<InsuRenew> findAllByNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(String nowInsuNo_0, Pageable pageable);

  // InsuCompany = 
  public Slice<InsuRenew> findAllByInsuCompanyIsOrderByInsuEndDateDescInsuStartDateAsc(String insuCompany_0, Pageable pageable);

  // InsuTypeCode = 
  public Slice<InsuRenew> findAllByInsuTypeCodeIsOrderByInsuEndDateDescInsuStartDateAsc(String insuTypeCode_0, Pageable pageable);

  // InsuEndDate >= ,AND InsuEndDate <= 
  public Slice<InsuRenew> findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByInsuEndDateDescInsuStartDateAsc(int insuEndDate_0, int insuEndDate_1, Pageable pageable);

  // CustNo = ,AND FacmNo = ,AND PrevInsuNo = 
  public Optional<InsuRenew> findTopByCustNoIsAndFacmNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(int custNo_0, int facmNo_1, String prevInsuNo_2);

  // InsuYearMonth = ,AND CustNo = ,AND FacmNo = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndCustNoIsAndFacmNoIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int custNo_1, int facmNo_2, Pageable pageable);

  // InsuYearMonth = ,AND ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int clCode1_1, int clCode2_2, int clNo_3, Pageable pageable);

  // InsuYearMonth = ,AND RenewCode = ,AND AcDate >= ,AND AcDate <=
  public Slice<InsuRenew> findAllByInsuYearMonthIsAndRenewCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int renewCode_1, int acDate_2, int acDate_3, Pageable pageable);

  // InsuYearMonth >= ,AND InsuYearMonth <= 
  public Slice<InsuRenew> findAllByInsuYearMonthGreaterThanEqualAndInsuYearMonthLessThanEqualOrderByInsuEndDateDescInsuStartDateAsc(int insuYearMonth_0, int insuYearMonth_1, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND NowInsuNo = 
  public Optional<InsuRenew> findTopByClCode1IsAndClCode2IsAndClNoIsAndNowInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(int clCode1_0, int clCode2_1, int clNo_2, String nowInsuNo_3);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND PrevInsuNo = 
  public Slice<InsuRenew> findAllByClCode1IsAndClCode2IsAndClNoIsAndPrevInsuNoIsOrderByInsuEndDateDescInsuStartDateAsc(int clCode1_0, int clCode2_1, int clNo_2, String prevInsuNo_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<InsuRenew> findByInsuRenewId(InsuRenewId insuRenewId);

}

