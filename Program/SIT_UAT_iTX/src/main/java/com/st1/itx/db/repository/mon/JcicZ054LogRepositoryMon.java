package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ054Log;
import com.st1.itx.db.domain.JcicZ054LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ054LogRepositoryMon extends JpaRepository<JcicZ054Log, JcicZ054LogId> {

  // Ukey=
  public Optional<JcicZ054Log> findTopByUkeyIsOrderByCreateDateDesc(String ukey_0);

  // Ukey=
  public Slice<JcicZ054Log> findAllByUkeyIsOrderByCreateDateDesc(String ukey_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ054Log> findByJcicZ054LogId(JcicZ054LogId jcicZ054LogId);

}

