package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB204;
import com.st1.itx.db.domain.JcicB204Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB204RepositoryHist extends JpaRepository<JcicB204, JcicB204Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicB204> findByJcicB204Id(JcicB204Id jcicB204Id);

  // (每日日終批次)維護 JcicB204 聯徵授信餘額日報檔
  @Procedure(value = "\"Usp_L8_JcicB204_Upd\"")
  public void uspL8Jcicb204Upd(int TBSDYF, String EmpNo);

}

