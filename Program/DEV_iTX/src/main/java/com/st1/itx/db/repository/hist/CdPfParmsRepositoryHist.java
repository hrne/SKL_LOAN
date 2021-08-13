package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdPfParms;
import com.st1.itx.db.domain.CdPfParmsId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdPfParmsRepositoryHist extends JpaRepository<CdPfParms, CdPfParmsId> {

  // ConditionCode1 = 
  public Slice<CdPfParms> findAllByConditionCode1IsOrderByConditionCode2AscConditionAsc(String conditionCode1_0, Pageable pageable);

  // ConditionCode1 = ,AND ConditionCode2 = 
  public Slice<CdPfParms> findAllByConditionCode1IsAndConditionCode2IsOrderByConditionAsc(String conditionCode1_0, String conditionCode2_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdPfParms> findByCdPfParmsId(CdPfParmsId cdPfParmsId);

}

