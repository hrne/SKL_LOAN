package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClStock;
import com.st1.itx.db.domain.ClStockId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClStockRepositoryDay extends JpaRepository<ClStock, ClStockId> {

  // ClCode1 = 
  public Slice<ClStock> findAllByClCode1Is(int clCode1_0, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = 
  public Slice<ClStock> findAllByClCode1IsAndClCode2Is(int clCode1_0, int clCode2_1, Pageable pageable);

  // StockCode = ,AND OwnerCustUKey =
  public Slice<ClStock> findAllByStockCodeIsAndOwnerCustUKeyIs(String stockCode_0, String ownerCustUKey_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClStock> findByClStockId(ClStockId clStockId);

}

