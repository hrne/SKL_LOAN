package com.st1.itx.db.repository.mon;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClFacId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClFacRepositoryMon extends JpaRepository<ClFac, ClFacId> {

  // ClCode1 = 
  public Slice<ClFac> findAllByClCode1IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int clCode1_0, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = 
  public Slice<ClFac> findAllByClCode1IsAndClCode2IsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int clCode1_0, int clCode2_1, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo =
  public Slice<ClFac> findAllByClCode1IsAndClCode2IsAndClNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // ApproveNo = 
  public Slice<ClFac> findAllByApproveNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int approveNo_0, Pageable pageable);

  // CustNo = ,AND FacmNo = 
  public Slice<ClFac> findAllByCustNoIsAndFacmNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int custNo_0, int facmNo_1, Pageable pageable);

  // CustNo = 
  public Slice<ClFac> findAllByCustNoIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int custNo_0, Pageable pageable);

  // ApproveNo >= ,AND ApproveNo <= ,AND FacmNo >= ,AND FacmNo <=
  public Slice<ClFac> findAllByApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int approveNo_0, int approveNo_1, int facmNo_2, int facmNo_3, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = ,AND ApproveNo >= ,AND ApproveNo <= ,AND CustNo >= ,AND CustNo <= ,AND FacmNo >= ,AND FacmNo <= 
  public Slice<ClFac> findAllByClCode1IsAndClCode2IsAndClNoIsAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int clCode1_0, int clCode2_1, int clNo_2, int approveNo_3, int approveNo_4, int custNo_5, int custNo_6, int facmNo_7, int facmNo_8, Pageable pageable);

  // ClCode1 >= ,AND ClCode1 <= ,AND ClCode2 >= ,AND ClCode2 <= ,AND ClNo >= ,AND ClNo <= ,AND CustNo >= ,AND CustNo <= ,AND FacmNo >= ,AND FacmNo <= ,AND ApproveNo >= ,AND ApproveNo <= 
  public Slice<ClFac> findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustNoGreaterThanEqualAndCustNoLessThanEqualAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndApproveNoGreaterThanEqualAndApproveNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, int custNo_6, int custNo_7, int facmNo_8, int facmNo_9, int approveNo_10, int approveNo_11, Pageable pageable);

  // CustNo = ,AND FacmNo >= ,AND FacmNo <= 
  public Slice<ClFac> findAllByCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int custNo_0, int facmNo_1, int facmNo_2, Pageable pageable);

  // CustNo = ,AND FacmNo =  ,AND MainFlag = 
  public Optional<ClFac> findTopByCustNoIsAndFacmNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int custNo_0, int facmNo_1, String mainFlag_2);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo =,AND CustNo = ,AND FacmNo = 
  public Slice<ClFac> findAllByClCode1IsAndClCode2IsAndClNoIsAndCustNoIsAndFacmNoIs(int clCode1_0, int clCode2_1, int clNo_2, int custNo_3, int facmNo_4, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo =,AND MainFlag = 
  public Optional<ClFac> findTopByClCode1IsAndClCode2IsAndClNoIsAndMainFlagIsOrderByClCode1AscClCode2AscClNoAscApproveNoAsc(int clCode1_0, int clCode2_1, int clNo_2, String mainFlag_3);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo =
  public Slice<ClFac> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClFac> findByClFacId(ClFacId clFacId);

}

