package com.st1.itx.db.repository.online;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClMovables;
import com.st1.itx.db.domain.ClMovablesId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClMovablesRepository extends JpaRepository<ClMovables, ClMovablesId> {

	// ClCode1 =
	public Slice<ClMovables> findAllByClCode1Is(int clCode1_0, Pageable pageable);

	// ClCode1 = ,AND ClCode2 =
	public Slice<ClMovables> findAllByClCode1IsAndClCode2Is(int clCode1_0, int clCode2_1, Pageable pageable);

	// ClCode1 = ,AND ClCode2 >= ,AND ClCode2 <= ,AND ClNo =
	public Slice<ClMovables> findAllByClCode1IsAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoIs(int clCode1_0, int clCode2_1, int clCode2_2, int clNo_3, Pageable pageable);

	// ProductBrand = ,AND ProductSpec = ,AND OwnerCustUKey =
	public Slice<ClMovables> findAllByProductBrandIsAndProductSpecIsAndOwnerCustUKeyIs(String productBrand_0, String productSpec_1, String ownerCustUKey_2, Pageable pageable);

	// ProductBrand =
	public Slice<ClMovables> findAllByProductBrandIs(String productBrand_0, Pageable pageable);

	// LicenseNo =
	public Slice<ClMovables> findAllByLicenseNoIs(String licenseNo_0, Pageable pageable);

	// EngineSN =
	public Slice<ClMovables> findAllByEngineSNIs(String engineSN_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<ClMovables> findByClMovablesId(ClMovablesId clMovablesId);

}
