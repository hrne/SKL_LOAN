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

import com.st1.itx.db.domain.CdComm;
import com.st1.itx.db.domain.CdCommId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCommRepositoryHist extends JpaRepository<CdComm, CdCommId> {

  // CdType = ,AND CdItem = ,AND EffectDate >= ,AND EffectDate <=
  public Optional<CdComm> findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateDesc(String cdType_0, String cdItem_1, int effectDate_2, int effectDate_3);

  // CdType = ,AND CdItem = ,AND EffectDate >= ,AND EffectDate <=
  public Optional<CdComm> findTopByCdTypeIsAndCdItemIsAndEffectDateGreaterThanEqualAndEffectDateLessThanEqualOrderByEffectDateAsc(String cdType_0, String cdItem_1, int effectDate_2, int effectDate_3);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdComm> findByCdCommId(CdCommId cdCommId);

}

