package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxAttachment;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAttachmentRepository extends JpaRepository<TxAttachment, Long> {

  // TranNo = ,AND MrKey =
  public Slice<TxAttachment> findAllByTranNoIsAndMrKeyIsOrderByCreateDateDesc(String tranNo_0, String mrKey_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxAttachment> findByFileNo(Long fileNo);

}

