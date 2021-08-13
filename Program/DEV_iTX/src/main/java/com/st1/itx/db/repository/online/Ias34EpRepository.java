package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias34Ep;
import com.st1.itx.db.domain.Ias34EpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34EpRepository extends JpaRepository<Ias34Ep, Ias34EpId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ias34Ep> findByIas34EpId(Ias34EpId ias34EpId);

  // (月底日日終批次)維護 IAS34 欄位清單E檔
  @Procedure(value = "\"Usp_L7_Ias34Ep_Upd\"")
  public void uspL7Ias34epUpd(int TBSDYF, String EmpNo, int NewAcFg);

}

