package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdCity;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCityRepository extends JpaRepository<CdCity, String> {

  // UnitCode >= ,AND UnitCode <=
  public Slice<CdCity> findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByCityCodeAsc(String unitCode_0, String unitCode_1, Pageable pageable);

  // CityCode >= ,AND CityCode <=
  public Slice<CdCity> findAllByCityCodeGreaterThanEqualAndCityCodeLessThanEqualOrderByCityCodeAsc(String cityCode_0, String cityCode_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdCity> findByCityCode(String cityCode);

}

