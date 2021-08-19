package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ060Log;
import com.st1.itx.db.domain.JcicZ060LogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ060LogRepositoryHist extends JpaRepository<JcicZ060Log, JcicZ060LogId> {

  // Ukey=
  public Optional<JcicZ060Log> findTopByUkeyIsOrderByCreateDateDesc(String ukey_0);

  // Ukey=
  public Slice<JcicZ060Log> findAllByUkeyIsOrderByCreateDateDesc(String ukey_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ060Log> findByJcicZ060LogId(JcicZ060LogId jcicZ060LogId);

}

