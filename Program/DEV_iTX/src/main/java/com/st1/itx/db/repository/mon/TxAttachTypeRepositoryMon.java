package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxAttachType;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAttachTypeRepositoryMon extends JpaRepository<TxAttachType, Long> {

  // TranNo = 
  public Slice<TxAttachType> findAllByTranNoIsOrderByTypeItemAsc(String tranNo_0, Pageable pageable);

  // TranNo = ,AND TypeItem =
  public Optional<TxAttachType> findTopByTranNoIsAndTypeItemIs(String tranNo_0, String typeItem_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxAttachType> findByTypeNo(Long typeNo);

}

