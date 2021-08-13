package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB090;
import com.st1.itx.db.domain.JcicB090Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB090RepositoryHist extends JpaRepository<JcicB090, JcicB090Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicB090> findByJcicB090Id(JcicB090Id jcicB090Id);

  // (每月日終批次)維護 JcicB090 每月聯徵擔保品關聯檔資料檔
  @Procedure(value = "\"Usp_L8_JcicB090_Upd\"")
  public void uspL8Jcicb090Upd(int TBSDYF, String EmpNo);

}

