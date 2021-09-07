package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClImmRankDetail;
import com.st1.itx.db.domain.ClImmRankDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClImmRankDetailRepositoryHist extends JpaRepository<ClImmRankDetail, ClImmRankDetailId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClImmRankDetail> findByClImmRankDetailId(ClImmRankDetailId clImmRankDetailId);

}

