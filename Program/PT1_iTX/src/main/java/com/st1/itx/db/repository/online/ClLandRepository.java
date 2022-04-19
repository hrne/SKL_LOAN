package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClLandRepository extends JpaRepository<ClLand, ClLandId> {

	// ClCode1 =
	public Slice<ClLand> findAllByClCode1Is(int clCode1_0, Pageable pageable);

	// ClCode1 = ,AND ClCode2 =
	public Slice<ClLand> findAllByClCode1IsAndClCode2Is(int clCode1_0, int clCode2_1, Pageable pageable);

	// ClCode1 = ,AND ClCode2 = ,AND ClNo =
	public Slice<ClLand> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

	// CityCode = ,AND AreaCode = ,AND IrCode = ,AND LandNo1 >= ,AND LandNo1 <= ,AND
	// LandNo2 >= ,AND LandNo2 <=
	public Slice<ClLand> findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1GreaterThanEqualAndLandNo1LessThanEqualAndLandNo2GreaterThanEqualAndLandNo2LessThanEqual(String cityCode_0,
			String areaCode_1, String irCode_2, String landNo1_3, String landNo1_4, String landNo2_5, String landNo2_6, Pageable pageable);

	// CityCode = ,AND AreaCode = ,AND IrCode = ,AND LandNo1 = ,AND LandNo2 =
	public Slice<ClLand> findAllByCityCodeIsAndAreaCodeIsAndIrCodeIsAndLandNo1IsAndLandNo2IsOrderByClCode1AscClCode2AscClNoAsc(String cityCode_0, String areaCode_1, String irCode_2, String landNo1_3,
			String landNo2_4, Pageable pageable);

	// ClCode1 = ,AND ClCode2 = ,AND ClNo =
	public Optional<ClLand> findTopByClCode1IsAndClCode2IsAndClNoIsOrderByLandSeqDesc(int clCode1_0, int clCode2_1, int clNo_2);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ClLand> findByClLandId(ClLandId clLandId);

}
