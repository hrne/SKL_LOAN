package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.PfDeparment;
import com.st1.itx.db.domain.PfDeparmentId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfDeparmentRepositoryMon extends JpaRepository<PfDeparment, PfDeparmentId> {

  // DeptCode= 
  public Slice<PfDeparment> findAllByDeptCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(String deptCode_0, Pageable pageable);

  // DistCode=
  public Slice<PfDeparment> findAllByDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(String distCode_0, Pageable pageable);

  // UnitCode=
  public Slice<PfDeparment> findAllByUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(String unitCode_0, Pageable pageable);

  // DeptCode= , AND DistCode=
  public Slice<PfDeparment> findAllByDeptCodeIsAndDistCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(String deptCode_0, String distCode_1, Pageable pageable);

  // DeptCode= , AND UnitCode=
  public Slice<PfDeparment> findAllByDeptCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(String deptCode_0, String unitCode_1, Pageable pageable);

  // DistCode= , AND UnitCode=
  public Slice<PfDeparment> findAllByDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(String distCode_0, String unitCode_1, Pageable pageable);

  // DeptCode= , AND DistCode= , AND UnitCode=
  public Slice<PfDeparment> findAllByDeptCodeIsAndDistCodeIsAndUnitCodeIsOrderByDeptCodeAscDistCodeAscUnitCodeAsc(String deptCode_0, String distCode_1, String unitCode_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<PfDeparment> findByPfDeparmentId(PfDeparmentId pfDeparmentId);

}

