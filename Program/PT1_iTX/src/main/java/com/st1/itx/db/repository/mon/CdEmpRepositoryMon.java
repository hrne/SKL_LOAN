package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdEmp;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdEmpRepositoryMon extends JpaRepository<CdEmp, String> {

  // EmployeeNo >= ,AND EmployeeNo <= 
  public Slice<CdEmp> findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualOrderByEmployeeNoAsc(String employeeNo_0, String employeeNo_1, Pageable pageable);

  // EmployeeNo % 
  public Slice<CdEmp> findAllByEmployeeNoLikeOrderByEmployeeNoAsc(String employeeNo_0, Pageable pageable);

  // AgentId =
  public Optional<CdEmp> findTopByAgentIdIs(String agentId_0);

  // CenterCode = 
  public Slice<CdEmp> findAllByCenterCodeIsOrderByEmployeeNoAsc(String centerCode_0, Pageable pageable);

  // Fullname =
  public Slice<CdEmp> findAllByFullnameIsOrderByEmployeeNoAsc(String fullname_0, Pageable pageable);

  // Fullname %
  public Slice<CdEmp> findAllByFullnameLikeOrderByEmployeeNoAsc(String fullname_0, Pageable pageable);

  // CenterCode = , AND AgCurInd = 
  public Slice<CdEmp> findAllByCenterCodeIsAndAgCurIndIsOrderByEmployeeNoAsc(String centerCode_0, String agCurInd_1, Pageable pageable);

  // EmployeeNo % , AND AgCurInd = 
  public Slice<CdEmp> findAllByEmployeeNoLikeAndAgCurIndIsOrderByEmployeeNoAsc(String employeeNo_0, String agCurInd_1, Pageable pageable);

  // Fullname % , AND AgCurInd = 
  public Slice<CdEmp> findAllByFullnameLikeAndAgCurIndIsOrderByEmployeeNoAsc(String fullname_0, String agCurInd_1, Pageable pageable);

  // EmployeeNo >= ,AND EmployeeNo <= , AND AgCurInd = 
  public Slice<CdEmp> findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgCurIndIsOrderByEmployeeNoAsc(String employeeNo_0, String employeeNo_1, String agCurInd_2, Pageable pageable);

  // CenterCode = , AND AgStatusCode = 
  public Slice<CdEmp> findAllByCenterCodeIsAndAgStatusCodeIsOrderByEmployeeNoAsc(String centerCode_0, String agStatusCode_1, Pageable pageable);

  // EmployeeNo % , AND AgStatusCode = 
  public Slice<CdEmp> findAllByEmployeeNoLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(String employeeNo_0, String agStatusCode_1, Pageable pageable);

  // Fullname % , AND AgStatusCode = 
  public Slice<CdEmp> findAllByFullnameLikeAndAgStatusCodeIsOrderByEmployeeNoAsc(String fullname_0, String agStatusCode_1, Pageable pageable);

  // EmployeeNo >= ,AND EmployeeNo <= , AND AgStatusCode = 
  public Slice<CdEmp> findAllByEmployeeNoGreaterThanEqualAndEmployeeNoLessThanEqualAndAgStatusCodeIsOrderByEmployeeNoAsc(String employeeNo_0, String employeeNo_1, String agStatusCode_2, Pageable pageable);

  // AgentCode =
  public Optional<CdEmp> findTopByAgentCodeIsOrderByInputDateDesc(String agentCode_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdEmp> findByEmployeeNo(String employeeNo);

}

