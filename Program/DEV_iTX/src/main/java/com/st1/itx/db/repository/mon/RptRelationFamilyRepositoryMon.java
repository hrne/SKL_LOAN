package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.RptRelationFamily;
import com.st1.itx.db.domain.RptRelationFamilyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface RptRelationFamilyRepositoryMon extends JpaRepository<RptRelationFamily, RptRelationFamilyId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<RptRelationFamily> findByRptRelationFamilyId(RptRelationFamilyId rptRelationFamilyId);

}

