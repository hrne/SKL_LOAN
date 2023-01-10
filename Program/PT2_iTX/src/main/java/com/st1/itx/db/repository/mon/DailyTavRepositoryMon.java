package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.DailyTav;
import com.st1.itx.db.domain.DailyTavId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface DailyTavRepositoryMon extends JpaRepository<DailyTav, DailyTavId> {

  // CustNo = ,AND AcDate >= ,AND AcDate <=
  public Slice<DailyTav> findAllByCustNoIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(int custNo_0, int acDate_1, int acDate_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<DailyTav> findByDailyTavId(DailyTavId dailyTavId);

}

