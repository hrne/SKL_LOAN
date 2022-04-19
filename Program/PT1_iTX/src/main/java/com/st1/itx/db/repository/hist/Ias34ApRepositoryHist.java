package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.Ias34Ap;
import com.st1.itx.db.domain.Ias34ApId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface Ias34ApRepositoryHist extends JpaRepository<Ias34Ap, Ias34ApId> {

  // CustNo = ,AND FacmNo = ,AND DataYM =
  public Slice<Ias34Ap> findAllByCustNoIsAndFacmNoIsAndDataYMIs(int custNo_0, int facmNo_1, int dataYM_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<Ias34Ap> findByIas34ApId(Ias34ApId ias34ApId);

  // (月底日日終批次)維護 IAS34 欄位清單A檔
  @Procedure(value = "\"Usp_L7_Ias34Ap_Upd\"")
  public void uspL7Ias34apUpd(int TBSDYF, String EmpNo, int NewAcFg);

}

