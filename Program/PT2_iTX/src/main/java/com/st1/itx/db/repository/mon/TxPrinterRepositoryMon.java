package com.st1.itx.db.repository.mon;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxPrinter;
import com.st1.itx.db.domain.TxPrinterId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxPrinterRepositoryMon extends JpaRepository<TxPrinter, TxPrinterId> {

  // StanIp = 
  public Slice<TxPrinter> findAllByStanIpIsOrderByStanIpAscFileCodeAsc(String stanIp_0, Pageable pageable);

  // StanIp = ,AND SourceEnv =
  public Slice<TxPrinter> findAllByStanIpIsAndSourceEnvIsOrderByStanIpAscFileCodeAsc(String stanIp_0, String sourceEnv_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxPrinter> findByTxPrinterId(TxPrinterId txPrinterId);

}

