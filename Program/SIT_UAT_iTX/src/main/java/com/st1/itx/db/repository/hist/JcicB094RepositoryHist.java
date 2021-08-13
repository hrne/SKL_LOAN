package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB094;
import com.st1.itx.db.domain.JcicB094Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB094RepositoryHist extends JpaRepository<JcicB094, JcicB094Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicB094> findByJcicB094Id(JcicB094Id jcicB094Id);

  // (每月日終批次)維護 JcicB094 每月聯徵股票擔保品明細檔
  @Procedure(value = "\"Usp_L8_JcicB094_Upd\"")
  public void uspL8Jcicb094Upd(int TBSDYF, String EmpNo);

}

