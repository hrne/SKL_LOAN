package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.BankRelationCompany;
import com.st1.itx.db.domain.BankRelationCompanyId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BankRelationCompanyRepositoryDay extends JpaRepository<BankRelationCompany, BankRelationCompanyId> {

  // CompanyId =
  public Slice<BankRelationCompany> findAllByCompanyIdIs(String companyId_0, Pageable pageable);

  // CustId = 
  public Optional<BankRelationCompany> findTopByCustIdIs(String custId_0);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<BankRelationCompany> findByBankRelationCompanyId(BankRelationCompanyId bankRelationCompanyId);

}

