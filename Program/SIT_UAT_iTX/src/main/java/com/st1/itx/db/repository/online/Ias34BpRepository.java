package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias34Bp;
import com.st1.itx.db.domain.Ias34BpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34BpRepository extends JpaRepository<Ias34Bp, Ias34BpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ias34Bp> findByIas34BpId(Ias34BpId ias34BpId);

  // (月底日日終批次)維護 IAS34 欄位清單B檔
  @Procedure(value = "\"Usp_L7_Ias34Bp_Upd\"")
  public void uspL7Ias34bpUpd(int TBSDYF, String EmpNo);

}

