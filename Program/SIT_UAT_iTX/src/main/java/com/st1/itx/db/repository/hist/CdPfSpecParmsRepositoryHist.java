package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdPfSpecParms;
import com.st1.itx.db.domain.CdPfSpecParmsId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdPfSpecParmsRepositoryHist extends JpaRepository<CdPfSpecParms, CdPfSpecParmsId> {

  // ConditionCode = 
  public Slice<CdPfSpecParms> findAllByConditionCodeIsOrderByConditionAsc(String conditionCode_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdPfSpecParms> findByCdPfSpecParmsId(CdPfSpecParmsId cdPfSpecParmsId);

}

