package com.st1.itx.db.repository.hist;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdCityRate;
import com.st1.itx.db.domain.CdCityRateId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCityRateRepositoryHist extends JpaRepository<CdCityRate, CdCityRateId> {

  // CityCode = ,AND EffectYYMM >= ,AND EffectYYMM <=
  public Slice<CdCityRate> findAllByCityCodeIsAndEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByEffectYYMMDesc(String cityCode_0, int effectYYMM_1, int effectYYMM_2, Pageable pageable);

  // EffectYYMM =
  public Slice<CdCityRate> findAllByEffectYYMMIsOrderByCityCodeAsc(int effectYYMM_0, Pageable pageable);

  // EffectYYMM >= ,AND EffectYYMM <=
  public Slice<CdCityRate> findAllByEffectYYMMGreaterThanEqualAndEffectYYMMLessThanEqualOrderByEffectYYMMDesc(int effectYYMM_0, int effectYYMM_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdCityRate> findByCdCityRateId(CdCityRateId cdCityRateId);

}

