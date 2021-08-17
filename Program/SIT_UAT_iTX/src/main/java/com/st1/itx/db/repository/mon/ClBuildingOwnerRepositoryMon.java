package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClBuildingOwner;
import com.st1.itx.db.domain.ClBuildingOwnerId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBuildingOwnerRepositoryMon extends JpaRepository<ClBuildingOwner, ClBuildingOwnerId> {

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClBuildingOwner> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // OwnerCustUKey = 
  public Slice<ClBuildingOwner> findAllByOwnerCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(String ownerCustUKey_0, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Optional<ClBuildingOwner> findTopByClCode1IsAndClCode2IsAndClNoIsOrderByOwnerRelCodeAsc(int clCode1_0, int clCode2_1, int clNo_2);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClBuildingOwner> findByClBuildingOwnerId(ClBuildingOwnerId clBuildingOwnerId);

}

