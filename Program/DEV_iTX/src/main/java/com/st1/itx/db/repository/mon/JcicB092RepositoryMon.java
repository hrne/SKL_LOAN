package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB092;
import com.st1.itx.db.domain.JcicB092Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB092RepositoryMon extends JpaRepository<JcicB092, JcicB092Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicB092> findByJcicB092Id(JcicB092Id jcicB092Id);

  // (每月日終批次)維護 JcicB092 每月聯徵不動產擔保品明細檔
  @Procedure(value = "\"Usp_L8_JcicB092_Upd\"")
  public void uspL8Jcicb092Upd(int TBSDYF, String EmpNo);

}

