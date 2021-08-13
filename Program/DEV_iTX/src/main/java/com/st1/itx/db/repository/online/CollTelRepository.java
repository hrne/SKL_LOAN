package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CollTel;
import com.st1.itx.db.domain.CollTelId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollTelRepository extends JpaRepository<CollTel, CollTelId> {

  // AcDate>= , AND AcDate<= ,AND CaseCode= ,AND CustNo= ,AND FacmNo= ,
  public Slice<CollTel> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, int facmNo_4, Pageable pageable);

  // CaseCode= ,AND CustNo= ,AND FacmNo= ,
  public Slice<CollTel> findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByTelDateDesc(String caseCode_0, int custNo_1, int facmNo_2, Pageable pageable);

  // AcDate>= , AND AcDate<= ,AND CaseCode= ,AND CustNo= 
  public Slice<CollTel> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByTelDateDesc(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, Pageable pageable);

  // CaseCode= ,AND CustNo= 
  public Slice<CollTel> findAllByCaseCodeIsAndCustNoIsOrderByTelDateDesc(String caseCode_0, int custNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CollTel> findByCollTelId(CollTelId collTelId);

}

