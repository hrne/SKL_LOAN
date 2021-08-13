package com.st1.itx.db.repository.mon;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdLandOffice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdLandOfficeRepositoryMon extends JpaRepository<CdLandOffice, String> {

	// City =
	public Slice<CdLandOffice> findAllByCityIsOrderByLandOfficeCodeAsc(String city_0, Pageable pageable);

	// Town =
	public Slice<CdLandOffice> findAllByTownIsOrderByLandOfficeCodeAsc(String town_0, Pageable pageable);

	// CityCode =
	public Slice<CdLandOffice> findAllByCityCodeIsOrderByLandOfficeCodeAsc(String cityCode_0, Pageable pageable);

	// CityCode = ,AND AreaCode =
	public Slice<CdLandOffice> findAllByCityCodeIsAndAreaCodeIsOrderByLandOfficeCodeAsc(String cityCode_0, String areaCode_1, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdLandOffice> findByLandOfficeCode(String landOfficeCode);

}
