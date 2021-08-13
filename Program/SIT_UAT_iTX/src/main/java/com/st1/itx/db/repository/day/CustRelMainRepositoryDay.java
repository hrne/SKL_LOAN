package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CustRelMain;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CustRelMainRepositoryDay extends JpaRepository<CustRelMain, String> {

  // CustRelId=
  public Optional<CustRelMain> findTopByCustRelIdIs(String custRelId_0);

  // CustRelName=
  public Optional<CustRelMain> findTopByCustRelNameIs(String custRelName_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CustRelMain> findByUkey(String ukey);

}

