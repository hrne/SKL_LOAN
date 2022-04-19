package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxArchiveTableLog;
import com.st1.itx.db.domain.TxArchiveTableLogId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxArchiveTableLogRepositoryDay extends JpaRepository<TxArchiveTableLog, TxArchiveTableLogId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxArchiveTableLog> findByTxArchiveTableLogId(TxArchiveTableLogId txArchiveTableLogId);

}

