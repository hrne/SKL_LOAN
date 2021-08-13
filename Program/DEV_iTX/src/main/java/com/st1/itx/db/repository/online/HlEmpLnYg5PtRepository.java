package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.HlEmpLnYg5Pt;
import com.st1.itx.db.domain.HlEmpLnYg5PtId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface HlEmpLnYg5PtRepository extends JpaRepository<HlEmpLnYg5Pt, HlEmpLnYg5PtId> {

  // WorkYM = 
  public Slice<HlEmpLnYg5Pt> findAllByWorkYMIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(String workYM_0, Pageable pageable);

  // AreaUnitNo = 
  public Slice<HlEmpLnYg5Pt> findAllByAreaUnitNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(String areaUnitNo_0, Pageable pageable);

  // HlEmpNo =
  public Slice<HlEmpLnYg5Pt> findAllByHlEmpNoIsOrderByWorkYMAscAreaUnitNoAscHlEmpNoAsc(String hlEmpNo_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<HlEmpLnYg5Pt> findByHlEmpLnYg5PtId(HlEmpLnYg5PtId hlEmpLnYg5PtId);

}

