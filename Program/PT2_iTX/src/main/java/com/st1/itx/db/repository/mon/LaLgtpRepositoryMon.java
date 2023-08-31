package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.LaLgtp;
import com.st1.itx.db.domain.LaLgtpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface LaLgtpRepositoryMon extends JpaRepository<LaLgtp, LaLgtpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<LaLgtp> findByLaLgtpId(LaLgtpId laLgtpId);

}

