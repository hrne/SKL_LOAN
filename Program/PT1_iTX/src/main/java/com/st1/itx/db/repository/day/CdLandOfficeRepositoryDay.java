package com.st1.itx.db.repository.day;


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

import com.st1.itx.db.domain.CdLandOffice;
import com.st1.itx.db.domain.CdLandOfficeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdLandOfficeRepositoryDay extends JpaRepository<CdLandOffice, CdLandOfficeId> {

  // CityCode = ,AND LandOfficeCode =
  public Slice<CdLandOffice> findAllByCityCodeIsAndLandOfficeCodeIsOrderByRecWordAsc(String cityCode_0, String landOfficeCode_1, Pageable pageable);

  // CityCode = ,AND LandOfficeCode =
  public Optional<CdLandOffice> findTopByCityCodeIsAndLandOfficeCodeIsOrderByRecWordDesc(String cityCode_0, String landOfficeCode_1);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdLandOffice> findByCdLandOfficeId(CdLandOfficeId cdLandOfficeId);

}

