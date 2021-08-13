package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdIndustry;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdIndustryRepositoryHist extends JpaRepository<CdIndustry, String> {

  // MainType >= ,AND MainType <= 
  public Slice<CdIndustry> findAllByMainTypeGreaterThanEqualAndMainTypeLessThanEqual(String mainType_0, String mainType_1, Pageable pageable);

  // IndustryCode >= ,AND IndustryCode <= 
  public Slice<CdIndustry> findAllByIndustryCodeGreaterThanEqualAndIndustryCodeLessThanEqualOrderByIndustryCodeAsc(String industryCode_0, String industryCode_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdIndustry> findByIndustryCode(String industryCode);

}

