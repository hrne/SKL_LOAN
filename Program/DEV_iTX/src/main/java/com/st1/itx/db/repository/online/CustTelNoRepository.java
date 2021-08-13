package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustTelNo;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustTelNoRepository extends JpaRepository<CustTelNo, String> {

  // CustUKey = 
  public Slice<CustTelNo> findAllByCustUKeyIsOrderByCreateDateAsc(String custUKey_0, Pageable pageable);

  // CustUKey = ,AND TelTypeCode = 
  public Optional<CustTelNo> findTopByCustUKeyIsAndTelTypeCodeIsOrderByCreateDateAsc(String custUKey_0, String telTypeCode_1);

  // TelTypeCode = ,AND TelNo = 
  public Slice<CustTelNo> findAllByTelTypeCodeIsAndTelNoIsOrderByCustUKeyAscCreateDateAsc(String telTypeCode_0, String telNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CustTelNo> findByTelNoUKey(String telNoUKey);

}

