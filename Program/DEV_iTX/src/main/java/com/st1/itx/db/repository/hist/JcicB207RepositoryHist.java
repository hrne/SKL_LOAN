package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB207;
import com.st1.itx.db.domain.JcicB207Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB207RepositoryHist extends JpaRepository<JcicB207, JcicB207Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicB207> findByJcicB207Id(JcicB207Id jcicB207Id);

  // (每月日終批次)維護 JcicB207 聯徵授信戶基本資料檔
  @Procedure(value = "\"Usp_L8_JcicB207_Upd\"")
  public void uspL8Jcicb207Upd(int TBSDYF, String EmpNo);

}

