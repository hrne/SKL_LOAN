package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ062Log;
import com.st1.itx.db.domain.JcicZ062LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ062LogRepository extends JpaRepository<JcicZ062Log, JcicZ062LogId> {

  // Ukey=
  public Optional<JcicZ062Log> findTopByUkeyIsOrderByCreateDateDesc(String ukey_0);

  // Ukey=
  public Slice<JcicZ062Log> findAllByUkeyIsOrderByCreateDateDesc(String ukey_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ062Log> findByJcicZ062LogId(JcicZ062LogId jcicZ062LogId);

}

