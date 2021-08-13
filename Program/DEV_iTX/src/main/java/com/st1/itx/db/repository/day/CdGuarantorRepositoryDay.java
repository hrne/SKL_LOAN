package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdGuarantor;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdGuarantorRepositoryDay extends JpaRepository<CdGuarantor, String> {

  // GuaRelJcic >= ,AND GuaRelJcic <=
  public Optional<CdGuarantor> findTopByGuaRelJcicGreaterThanEqualAndGuaRelJcicLessThanEqual(String guaRelJcic_0, String guaRelJcic_1);

  // GuaRelCode >= ,AND GuaRelCode <= 
  public Slice<CdGuarantor> findAllByGuaRelCodeGreaterThanEqualAndGuaRelCodeLessThanEqualOrderByGuaRelCodeAsc(String guaRelCode_0, String guaRelCode_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdGuarantor> findByGuaRelCode(String guaRelCode);

}

