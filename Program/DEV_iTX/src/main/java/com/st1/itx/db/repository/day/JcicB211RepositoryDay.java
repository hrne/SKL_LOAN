package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB211;
import com.st1.itx.db.domain.JcicB211Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB211RepositoryDay extends JpaRepository<JcicB211, JcicB211Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicB211> findByJcicB211Id(JcicB211Id jcicB211Id);

  // (每日日終批次)維護 JcicB211 聯徵每日授信餘額變動資料檔
  @Procedure(value = "\"Usp_L8_JcicB211_Upd\"")
  public void uspL8Jcicb211Upd(int TBSDYF, String EmpNo);

}

