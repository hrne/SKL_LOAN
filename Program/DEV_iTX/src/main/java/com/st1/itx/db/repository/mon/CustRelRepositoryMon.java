package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustRel;
import com.st1.itx.db.domain.CustRelId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustRelRepositoryMon extends JpaRepository<CustRel, CustRelId> {

  // CustUKey = 
  public Slice<CustRel> findAllByCustUKeyIs(String custUKey_0, Pageable pageable);

  // RelUKey = 
  public Slice<CustRel> findAllByRelUKeyIs(String relUKey_0, Pageable pageable);

  // RelCode %
  public Slice<CustRel> findAllByRelCodeLikeOrderByRelCodeAsc(String relCode_0, Pageable pageable);

  // CustUKey = ,AND Enable =
  public Slice<CustRel> findAllByCustUKeyIsAndEnableIs(String custUKey_0, String enable_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CustRel> findByCustRelId(CustRelId custRelId);

}

