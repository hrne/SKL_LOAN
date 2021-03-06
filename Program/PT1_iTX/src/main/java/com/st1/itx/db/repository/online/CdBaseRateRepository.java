package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.CdBaseRateId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBaseRateRepository extends JpaRepository<CdBaseRate, CdBaseRateId> {

  // CurrencyCode = ,AND BaseRateCode = ,AND EffectDate >= ,AND EffectDate <=
  public Slice<CdBaseRate> findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3, Pageable pageable);

  // CurrencyCode = ,AND BaseRateCode = ,AND EffectDate >= ,AND EffectDate <=
  public Optional<CdBaseRate> findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3);

  // CurrencyCode = ,AND BaseRateCode = ,AND EffectDate >= ,AND EffectDate <=
  public Optional<CdBaseRate> findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(String currencyCode_0, String baseRateCode_1, int effectDate_2, int effectDate_3);

  // CurrencyCode = ,AND BaseRateCode >= ,AND BaseRateCode <= ,AND EffectDate >= ,AND EffectDate <=
  public Slice<CdBaseRate> findAllByCurrencyCodeIsAndBaseRateCodeGreaterThanEqualAndBaseRateCodeLessThanEqualAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByBaseRateCodeAscEffectDateAsc(String currencyCode_0, String baseRateCode_1, String baseRateCode_2, int effectDate_3, int effectDate_4, Pageable pageable);

  // CurrencyCode = ,AND BaseRateCode = ,AND EffectFlag = ,AND EffectDate >=
  public Slice<CdBaseRate> findAllByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsAndEffectDateGreaterThanEqualOrderByEffectDateAsc(String currencyCode_0, String baseRateCode_1, int effectFlag_2, int effectDate_3, Pageable pageable);

  // CurrencyCode = ,AND BaseRateCode = ,AND EffectFlag = 
  public Optional<CdBaseRate> findTopByCurrencyCodeIsAndBaseRateCodeIsAndEffectFlagIsOrderByEffectDateDesc(String currencyCode_0, String baseRateCode_1, int effectFlag_2);

  // CurrencyCode = ,AND BaseRateCode =
  public Slice<CdBaseRate> findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateAsc(String currencyCode_0, String baseRateCode_1, Pageable pageable);

  // CurrencyCode = ,AND BaseRateCode =
  public Slice<CdBaseRate> findAllByCurrencyCodeIsAndBaseRateCodeIsOrderByEffectDateDesc(String currencyCode_0, String baseRateCode_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdBaseRate> findByCdBaseRateId(CdBaseRateId cdBaseRateId);

}

