package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicB093;
import com.st1.itx.db.domain.JcicB093Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicB093RepositoryMon extends JpaRepository<JcicB093, JcicB093Id> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicB093> findByJcicB093Id(JcicB093Id jcicB093Id);

  // (每月日終批次)維護 JcicB093 每月聯徵動產及貴重物品擔保品明細檔
  @Procedure(value = "\"Usp_L8_JcicB093_Upd\"")
  public void uspL8Jcicb093Upd(int TBSDYF, String EmpNo);

}

