package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClParkingType;
import com.st1.itx.db.domain.ClParkingTypeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClParkingTypeRepository extends JpaRepository<ClParkingType, ClParkingTypeId> {

	// ClCode1 = ,AND ClCode2 = ,AND ClNo =
	public Slice<ClParkingType> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ClParkingType> findByClParkingTypeId(ClParkingTypeId clParkingTypeId);

}
