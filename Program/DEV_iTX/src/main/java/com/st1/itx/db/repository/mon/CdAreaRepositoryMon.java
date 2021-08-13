package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdArea;
import com.st1.itx.db.domain.CdAreaId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAreaRepositoryMon extends JpaRepository<CdArea, CdAreaId> {

  // CityCode >= ,AND CityCode <=
  public Slice<CdArea> findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(String cityCode_0, String cityCode_1, Pageable pageable);

  // CityCode >= ,AND CityCode <= ,AND AreaCode >= ,AND AreaCode <= 
  public Slice<CdArea> findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualAndAreaCodeGreaterThanEqualAndAreaCodeLessThanEqualOrderByCityCodeAscAreaCodeAsc(String cityCode_0, String cityCode_1, String areaCode_2, String areaCode_3, Pageable pageable);

  // Zip3 = 
  public Optional<CdArea> findTopByZip3IsOrderByCityCodeAscAreaCodeAsc(String zip3_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdArea> findByCdAreaId(CdAreaId cdAreaId);

}

