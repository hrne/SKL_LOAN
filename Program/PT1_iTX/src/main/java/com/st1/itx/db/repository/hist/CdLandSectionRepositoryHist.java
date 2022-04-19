package com.st1.itx.db.repository.hist;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdLandSection;
import com.st1.itx.db.domain.CdLandSectionId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdLandSectionRepositoryHist extends JpaRepository<CdLandSection, CdLandSectionId> {

	// CityCode =
	public Slice<CdLandSection> findAllByCityCodeIsOrderByCityCodeAscIrCodeAsc(String cityCode_0, Pageable pageable);

	// CityCode = ,AND AreaCode =
	public Slice<CdLandSection> findAllByCityCodeIsAndAreaCodeIsOrderByCityCodeAscIrCodeAsc(String cityCode_0, String areaCode_1, Pageable pageable);

	// LandOfficeCode =
	public Slice<CdLandSection> findAllByLandOfficeCodeIsOrderByCityCodeAscIrCodeAsc(String landOfficeCode_0, Pageable pageable);

	// Hold
	@Lock(value = LockModeType.PESSIMISTIC_READ)
	@Transactional(readOnly = false)
	public Optional<CdLandSection> findByCdLandSectionId(CdLandSectionId cdLandSectionId);

}
