package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CollLaw;
import com.st1.itx.db.domain.CollLawId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollLawRepositoryMon extends JpaRepository<CollLaw, CollLawId> {

  // AcDate>= , AND AcDate<= ,AND CaseCode= ,AND CustNo= ,AND FacmNo= ,
  public Slice<CollLaw> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, int facmNo_4, Pageable pageable);

  // CaseCode= ,AND CustNo= ,AND FacmNo= ,
  public Slice<CollLaw> findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByAcDateDesc(String caseCode_0, int custNo_1, int facmNo_2, Pageable pageable);

  // AcDate>= , AND AcDate<= ,AND CaseCode= ,AND CustNo= 
  public Slice<CollLaw> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByAcDateDesc(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, Pageable pageable);

  // CaseCode= ,AND CustNo= 
  public Slice<CollLaw> findAllByCaseCodeIsAndCustNoIsOrderByAcDateDesc(String caseCode_0, int custNo_1, Pageable pageable);

  // CaseCode= ,AND CustNo= ,AND FacmNo= ,
  public Optional<CollLaw> findTopByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByRecordDateDescAcDateDesc(String caseCode_0, int custNo_1, int facmNo_2);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CollLaw> findByCollLawId(CollLawId collLawId);

}

