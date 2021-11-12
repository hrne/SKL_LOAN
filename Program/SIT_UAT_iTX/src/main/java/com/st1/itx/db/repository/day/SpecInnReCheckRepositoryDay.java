package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.SpecInnReCheck;
import com.st1.itx.db.domain.SpecInnReCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface SpecInnReCheckRepositoryDay extends JpaRepository<SpecInnReCheck, SpecInnReCheckId> {

  // CustNo =
  public Slice<SpecInnReCheck> findAllByCustNoIsOrderByFacmNoAsc(int custNo_0, Pageable pageable);

  // CustNo = ,AND FacmNo = 
  public Slice<SpecInnReCheck> findAllByCustNoIsAndFacmNoIs(int custNo_0, int facmNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<SpecInnReCheck> findBySpecInnReCheckId(SpecInnReCheckId specInnReCheckId);

  // (日終批次)維護 InnReCheck 覆審案件明細檔 
  @Procedure(value = "\"Usp_L5_InnReCheck_Upd\"")
  public void uspL5InnrecheckUpd(int tbsdyf,  String empNo);

}

