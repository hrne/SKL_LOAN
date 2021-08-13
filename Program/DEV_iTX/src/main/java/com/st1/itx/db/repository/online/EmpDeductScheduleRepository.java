package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.domain.EmpDeductScheduleId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface EmpDeductScheduleRepository extends JpaRepository<EmpDeductSchedule, EmpDeductScheduleId> {

  // WorkMonth = 
  public Slice<EmpDeductSchedule> findAllByWorkMonthIs(int workMonth_0, Pageable pageable);

  // AgType1 = 
  public Slice<EmpDeductSchedule> findAllByAgType1Is(String agType1_0, Pageable pageable);

  // EntryDate >= ,AND EntryDate <= 
  public Slice<EmpDeductSchedule> findAllByEntryDateGreaterThanEqualAndEntryDateLessThanEqual(int entryDate_0, int entryDate_1, Pageable pageable);

  // MediaDate >= ,AND MediaDate <=  
  public Slice<EmpDeductSchedule> findAllByMediaDateGreaterThanEqualAndMediaDateLessThanEqualOrderByAgType1Asc(int mediaDate_0, int mediaDate_1, Pageable pageable);

  // WorkMonth >= ,AND WorkMonth <=  
  public Slice<EmpDeductSchedule> findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByWorkMonthAscAgType1Asc(int workMonth_0, int workMonth_1, Pageable pageable);

  // WorkMonth >= ,AND WorkMonth <= 
  public Slice<EmpDeductSchedule> findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualOrderByAgType1AscWorkMonthAsc(int workMonth_0, int workMonth_1, Pageable pageable);

  // WorkMonth >= ,AND WorkMonth <= ,AND AgType1 =  
  public Slice<EmpDeductSchedule> findAllByWorkMonthGreaterThanEqualAndWorkMonthLessThanEqualAndAgType1IsOrderByAgType1AscWorkMonthAsc(int workMonth_0, int workMonth_1, String agType1_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<EmpDeductSchedule> findByEmpDeductScheduleId(EmpDeductScheduleId empDeductScheduleId);

}

