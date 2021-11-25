package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClBuildingPublic;
import com.st1.itx.db.domain.ClBuildingPublicId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingPublicRepositoryMon extends JpaRepository<ClBuildingPublic, ClBuildingPublicId> {

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClBuildingPublic> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // PublicBdNo1 = 
  public Slice<ClBuildingPublic> findAllByPublicBdNo1Is(int publicBdNo1_0, Pageable pageable);

  // PublicBdNo1 = ,AND PublicBdNo2 = 
  public Slice<ClBuildingPublic> findAllByPublicBdNo1IsAndPublicBdNo2Is(int publicBdNo1_0, int publicBdNo2_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClBuildingPublic> findByClBuildingPublicId(ClBuildingPublicId clBuildingPublicId);

}

