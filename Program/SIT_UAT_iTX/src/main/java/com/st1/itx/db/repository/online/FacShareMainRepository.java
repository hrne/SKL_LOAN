package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.FacShareMain;
import com.st1.itx.db.domain.FacShareMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FacShareMainRepository extends JpaRepository<FacShareMain, FacShareMainId> {

  // CreditSysNo = 
  public Slice<FacShareMain> findAllByCreditSysNoIsOrderByCustNoAsc(int creditSysNo_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<FacShareMain> findByFacShareMainId(FacShareMainId facShareMainId);

}

