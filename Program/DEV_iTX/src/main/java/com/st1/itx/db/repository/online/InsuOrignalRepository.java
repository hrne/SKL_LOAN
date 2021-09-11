package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuOrignalId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface InsuOrignalRepository extends JpaRepository<InsuOrignal, InsuOrignalId> {

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Optional<InsuOrignal> findTopByClCode1IsAndClCode2IsAndClNoIsOrderByInsuEndDateDesc(int clCode1_0, int clCode2_1, int clNo_2);

  // InsuEndDate >= , AND InsuEndDate <=
  public Slice<InsuOrignal> findAllByInsuEndDateGreaterThanEqualAndInsuEndDateLessThanEqualOrderByOrigInsuNoAscEndoInsuNoAsc(int insuEndDate_0, int insuEndDate_1, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<InsuOrignal> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND OrigInsuNo =
  public Slice<InsuOrignal> findAllByClCode1IsAndClCode2IsAndClNoIsAndOrigInsuNoIs(int clCode1_0, int clCode2_1, int clNo_2, String origInsuNo_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<InsuOrignal> findByInsuOrignalId(InsuOrignalId insuOrignalId);

}

