package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxArchiveTable;
import com.st1.itx.db.domain.TxArchiveTableId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxArchiveTableRepositoryMon extends JpaRepository<TxArchiveTable, TxArchiveTableId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxArchiveTable> findByTxArchiveTableId(TxArchiveTableId txArchiveTableId);

  // 封存結清且領清償證明滿五年之交易明細
  @Procedure(value = "\"Usp_L6_ArchiveFiveYearTx_Copy\"")
  public void uspL6ArchivefiveyeartxCopy(int tbsdyf,  String empNo);

  // 將結清且領清償證明滿五年之已封存交易明細搬回連線環境
  @Procedure(value = "\"Usp_L6_UnarchiveFiveYearTx_Copy\"")
  public void uspL6UnarchivefiveyeartxCopy(int custNo,  int facmNo,  int bormNo,  int tbsdyf,  String empNo);

}

