package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.BatxHead;
import com.st1.itx.db.domain.BatxHeadId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxHeadRepository extends JpaRepository<BatxHead, BatxHeadId> {

  // AcDate >= ,AND AcDate <= 
  public Slice<BatxHead> findAllByAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByBatchNoAsc(int acDate_0, int acDate_1, Pageable pageable);

  // AcDate = 
  public Optional<BatxHead> findTopByAcDateIsOrderByBatchNoDesc(int acDate_0);

  // AcDate = ,AND TitaTxCd = ,AND BatxExeCode <> 
  public Optional<BatxHead> findTopByAcDateIsAndTitaTxCdIsAndBatxExeCodeNotOrderByBatchNoDesc(int acDate_0, String titaTxCd_1, String batxExeCode_2);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BatxHead> findByBatxHeadId(BatxHeadId batxHeadId);

}

