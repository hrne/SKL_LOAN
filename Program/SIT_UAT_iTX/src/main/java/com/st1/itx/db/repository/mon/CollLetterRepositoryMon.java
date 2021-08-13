package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CollLetter;
import com.st1.itx.db.domain.CollLetterId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollLetterRepositoryMon extends JpaRepository<CollLetter, CollLetterId> {

  // AcDate>= , AND AcDate<= ,AND CaseCode= ,AND CustNo= ,AND FacmNo= ,
  public Slice<CollLetter> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, int facmNo_4, Pageable pageable);

  // CaseCode= ,AND CustNo= ,AND FacmNo= ,
  public Slice<CollLetter> findAllByCaseCodeIsAndCustNoIsAndFacmNoIsOrderByMailDateDesc(String caseCode_0, int custNo_1, int facmNo_2, Pageable pageable);

  // AcDate>= , AND AcDate<= ,AND CaseCode= ,AND CustNo= 
  public Slice<CollLetter> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualAndCaseCodeIsAndCustNoIsOrderByMailDateDesc(int acDate_0, int acDate_1, String caseCode_2, int custNo_3, Pageable pageable);

  // CaseCode= ,AND CustNo= 
  public Slice<CollLetter> findAllByCaseCodeIsAndCustNoIsOrderByMailDateDesc(String caseCode_0, int custNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CollLetter> findByCollLetterId(CollLetterId collLetterId);

}

