package com.st1.itx.db.repository.mon;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CollList;
import com.st1.itx.db.domain.CollListId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollListRepositoryMon extends JpaRepository<CollList, CollListId> {

  // ClCustNo=, AND ClFacmNo=
  public Slice<CollList> findAllByClCustNoIsAndClFacmNoIs(int clCustNo_0, int clFacmNo_1, Pageable pageable);

  // CustNo >= ,AND CustNo <= ,AND FacmNo >= ,AND FacmNo <= ,AND Status ^i
  public Slice<CollList> findAllByCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndStatusIn(int custNo_0, int custNo_1, int facmNo_2, int facmNo_3, List<Integer> status_4, Pageable pageable);

  // OvduDays >= ,AND OvduDays <= 
  public Slice<CollList> findAllByOvduDaysGreaterThanEqualAndOvduDaysLessThanEqual(int ovduDays_0, int ovduDays_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CollList> findByCollListId(CollListId collListId);

  // (日終批次)維護 CollList 法催紀錄清單檔
  @Procedure(value = "\"Usp_L5_CollList_Upd\"")
  public void uspL5ColllistUpd(int tbsdyf,  String empNo);

}

