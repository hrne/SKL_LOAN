package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdSupv;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdSupvRepositoryDay extends JpaRepository<CdSupv, String> {

  // SupvReasonLevel = 
  public Slice<CdSupv> findAllBySupvReasonLevelIs(String supvReasonLevel_0, Pageable pageable);

  // SupvReasonCode >= ,AND SupvReasonCode <= 
  public Slice<CdSupv> findAllBySupvReasonCodeGreaterThanEqualAndSupvReasonCodeLessThanEqualOrderBySupvReasonCodeAsc(String supvReasonCode_0, String supvReasonCode_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdSupv> findBySupvReasonCode(String supvReasonCode);

}

