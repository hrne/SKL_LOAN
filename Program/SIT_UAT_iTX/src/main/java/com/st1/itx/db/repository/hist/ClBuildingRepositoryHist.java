package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingRepositoryHist extends JpaRepository<ClBuilding, ClBuildingId> {

  // ClCode1 = 
  public Slice<ClBuilding> findAllByClCode1Is(int clCode1_0, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = 
  public Slice<ClBuilding> findAllByClCode1IsAndClCode2Is(int clCode1_0, int clCode2_1, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClBuilding> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // BdLocation = 
  public Slice<ClBuilding> findAllByBdLocationIsOrderByClCode1AscClCode2AscClNoAsc(String bdLocation_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClBuilding> findByClBuildingId(ClBuildingId clBuildingId);

}

