package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdBcm;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdBcmRepository extends JpaRepository<CdBcm, String> {

  // DeptCode >= ,AND DeptCode <=
  public Slice<CdBcm> findAllByDeptCodeGreaterThanEqualAndDeptCodeLessThanEqualOrderByDeptCodeAsc(String deptCode_0, String deptCode_1, Pageable pageable);

  // DistCode >= ,AND DistCode <=
  public Slice<CdBcm> findAllByDistCodeGreaterThanEqualAndDistCodeLessThanEqualOrderByDistCodeAsc(String distCode_0, String distCode_1, Pageable pageable);

  // UnitCode >= ,AND UnitCode <=
  public Slice<CdBcm> findAllByUnitCodeGreaterThanEqualAndUnitCodeLessThanEqualOrderByUnitCodeAsc(String unitCode_0, String unitCode_1, Pageable pageable);

  // DeptCode =
  public Optional<CdBcm> findTopByDeptCodeIsOrderByUnitCodeAsc(String deptCode_0);

  // DistCode =
  public Optional<CdBcm> findTopByDistCodeIsOrderByUnitCodeAsc(String distCode_0);

  // UnitManager =
  public Slice<CdBcm> findAllByUnitManagerIs(String unitManager_0, Pageable pageable);

  // DeptManager =
  public Slice<CdBcm> findAllByDeptManagerIs(String deptManager_0, Pageable pageable);

  // DistManager =
  public Slice<CdBcm> findAllByDistManagerIs(String distManager_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdBcm> findByUnitCode(String unitCode);

}

