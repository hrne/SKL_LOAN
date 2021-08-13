package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.TxAuthority;
import com.st1.itx.db.domain.TxAuthorityId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxAuthorityRepository extends JpaRepository<TxAuthority, TxAuthorityId> {

  // AuthNo =
  public Slice<TxAuthority> findAllByAuthNoIsOrderByTranNoAsc(String authNo_0, Pageable pageable);

  // AuthNo = ,AND TranNo <>%
  public Slice<TxAuthority> findAllByAuthNoIsAndTranNoNotLikeOrderByTranNoAsc(String authNo_0, String tranNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<TxAuthority> findByTxAuthorityId(TxAuthorityId txAuthorityId);

}

