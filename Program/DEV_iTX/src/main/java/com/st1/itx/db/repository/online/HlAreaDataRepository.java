package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.HlAreaData;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlAreaDataRepository extends JpaRepository<HlAreaData, String> {

  // AreaUnitNo = 
  public Slice<HlAreaData> findAllByAreaUnitNoIsOrderByAreaUnitNoAsc(String areaUnitNo_0, Pageable pageable);

  // AreaChiefEmpNo = 
  public Slice<HlAreaData> findAllByAreaChiefEmpNoIsOrderByAreaUnitNoAsc(String areaChiefEmpNo_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<HlAreaData> findByAreaUnitNo(String areaUnitNo);

}

