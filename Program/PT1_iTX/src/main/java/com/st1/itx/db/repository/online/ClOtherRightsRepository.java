package com.st1.itx.db.repository.online;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClOtherRights;
import com.st1.itx.db.domain.ClOtherRightsId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClOtherRightsRepository extends JpaRepository<ClOtherRights, ClOtherRightsId> {

  // ClCode1 = 
  public Slice<ClOtherRights> findAllByClCode1IsOrderByClCode2AscClNoAscSeqAsc(int clCode1_0, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = 
  public Slice<ClOtherRights> findAllByClCode1IsAndClCode2IsOrderByClNoAscSeqAsc(int clCode1_0, int clCode2_1, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo = 
  public Slice<ClOtherRights> findAllByClCode1IsAndClCode2IsAndClNoIsOrderBySeqAsc(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // ClCode1 >= ,AND ClCode1 <= ,AND ClCode2 >= ,AND ClCode2 <= ,AND ClNo >= ,AND ClNo <=
  public Slice<ClOtherRights> findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualOrderByClCode1AscClCode2AscClNoAscSeqAsc(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, Pageable pageable);

  // ChoiceDate = ,AND LastUpdateEmpNo =
  public Slice<ClOtherRights> findAllByChoiceDateIsAndLastUpdateEmpNoIsOrderByCustNoAscCloseNoAsc(int choiceDate_0, String lastUpdateEmpNo_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClOtherRights> findByClOtherRightsId(ClOtherRightsId clOtherRightsId);

}

