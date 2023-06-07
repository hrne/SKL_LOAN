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

import com.st1.itx.db.domain.FacCaseAppl;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacCaseApplRepositoryMon extends JpaRepository<FacCaseAppl, Integer> {

  // ApplNo >= ,AND ApplNo <= ,AND ProcessCode >= ,AND ProcessCode <= 
  public Slice<FacCaseAppl> findAllByApplNoGreaterThanEqualAndApplNoLessThanEqualAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(int applNo_0, int applNo_1, String processCode_2, String processCode_3, Pageable pageable);

  // CustUKey = ,AND ProcessCode >= ,AND ProcessCode <= 
  public Slice<FacCaseAppl> findAllByCustUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(String custUKey_0, String processCode_1, String processCode_2, Pageable pageable);

  // GroupUKey = ,AND ProcessCode >= ,AND ProcessCode <= 
  public Slice<FacCaseAppl> findAllByGroupUKeyIsAndProcessCodeGreaterThanEqualAndProcessCodeLessThanEqualOrderByApplNoAsc(String groupUKey_0, String processCode_1, String processCode_2, Pageable pageable);

  // GroupUKey = ,AND ApplNo >= ,AND ApplNo <=
  public Optional<FacCaseAppl> findTopByGroupUKeyIsAndApplNoGreaterThanEqualAndApplNoLessThanEqualOrderByApplNoDesc(String groupUKey_0, int applNo_1, int applNo_2);

  // CreditSysNo =
  public Optional<FacCaseAppl> findTopByCreditSysNoIsOrderByApplDateAsc(int creditSysNo_0);

  // SyndNo = 
  public Slice<FacCaseAppl> findAllBySyndNoIsOrderByApplNoAsc(int syndNo_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FacCaseAppl> findByApplNo(int applNo);

}

