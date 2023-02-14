package com.st1.itx.db.repository.day;


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

import com.st1.itx.db.domain.TxApLog;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxApLogRepositoryDay extends JpaRepository<TxApLog, Long> {

  // UserID =
  public Slice<TxApLog> findAllByUserIDIsOrderByUserIDAsc(String userID_0, Pageable pageable);

  // Entdy =
  public Slice<TxApLog> findAllByEntdyIsOrderByEntdyAsc(int entdy_0, Pageable pageable);

  // Entdy =,AND UserID =
  public Slice<TxApLog> findAllByEntdyIsAndUserIDIsOrderByEntdyAscUserIDAsc(int entdy_0, String userID_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxApLog> findByAutoSeq(Long autoSeq);

}

