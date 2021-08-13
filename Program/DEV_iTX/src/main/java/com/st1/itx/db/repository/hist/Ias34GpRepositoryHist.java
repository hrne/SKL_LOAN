package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias34Gp;
import com.st1.itx.db.domain.Ias34GpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34GpRepositoryHist extends JpaRepository<Ias34Gp, Ias34GpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ias34Gp> findByIas34GpId(Ias34GpId ias34GpId);

  // (月底日日終批次)維護 Ias34Gp IAS34資料欄位清單G檔
  @Procedure(value = "\"Usp_L7_Ias34Gp_Upd\"")
  public void uspL7Ias34gpUpd(int TBSDYF, String EmpNo);

}

