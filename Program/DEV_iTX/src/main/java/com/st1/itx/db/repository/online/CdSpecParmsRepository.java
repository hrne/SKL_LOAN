package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdSpecParms;
import com.st1.itx.db.domain.CdSpecParmsId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdSpecParmsRepository extends JpaRepository<CdSpecParms, CdSpecParmsId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdSpecParms> findByCdSpecParmsId(CdSpecParmsId cdSpecParmsId);

}

