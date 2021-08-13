package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ifrs9FacData;
import com.st1.itx.db.domain.Ifrs9FacDataId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ifrs9FacDataRepositoryMon extends JpaRepository<Ifrs9FacData, Ifrs9FacDataId> {

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ifrs9FacData> findByIfrs9FacDataId(Ifrs9FacDataId ifrs9FacDataId);

  // (月底日日終批次)維護IFRS9額度資料檔
  @Procedure(value = "\"Usp_L7_Ifrs9FacData_Upd\"")
  public void uspL7Ifrs9facdataUpd(int TBSDYF, String EmpNo);

}

