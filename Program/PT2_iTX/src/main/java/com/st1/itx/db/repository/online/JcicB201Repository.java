package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB201;
import com.st1.itx.db.domain.JcicB201Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB201Repository extends JpaRepository<JcicB201, JcicB201Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicB201> findByJcicB201Id(JcicB201Id jcicB201Id);

  // (月底日日終批次)維護 JcicB201 聯徵授信餘額月報資料檔
  @Procedure(value = "\"Usp_L8_JcicB201_Upd\"")
  public void uspL8Jcicb201Upd(int TBSDYF, String EmpNo);

}

