package com.st1.itx.db.repository.mon;


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
public interface CdIndustryRepositoryMon extends JpaRepository<CdIndustry, String> {

  // MainType >= ,AND MainType <= 
  public Slice<CdIndustry> findAllByMainTypeGreaterThanEqualAndMainTypeLessThanEqual(String mainType_0, String mainType_1, Pageable pageable);

  // IndustryCode %
  public Slice<CdIndustry> findAllByIndustryCodeLikeOrderByIndustryCodeAsc(String industryCode_0, Pageable pageable);

  // IndustryItem %
  public Slice<CdIndustry> findAllByIndustryItemLikeOrderByIndustryCodeAsc(String industryItem_0, Pageable pageable);

  // IndustryCode % ,OR IndustryItem %
  public Slice<CdIndustry> findAllByIndustryCodeLikeOrIndustryItemLikeOrderByIndustryCodeAsc(String industryCode_0, String industryItem_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdIndustry> findByIndustryCode(String industryCode);

}

