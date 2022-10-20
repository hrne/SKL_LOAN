package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClMainRepositoryDay extends JpaRepository<ClMain, ClMainId> {

  // ClCode1 = 
  public Slice<ClMain> findAllByClCode1IsOrderByClCode2AscClNoAsc(int clCode1_0, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = 
  public Slice<ClMain> findAllByClCode1IsAndClCode2IsOrderByClNoAsc(int clCode1_0, int clCode2_1, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = ,AND ClNo =
  public Slice<ClMain> findAllByClCode1IsAndClCode2IsAndClNoIs(int clCode1_0, int clCode2_1, int clNo_2, Pageable pageable);

  // CustUKey = 
  public Slice<ClMain> findAllByCustUKeyIs(String custUKey_0, Pageable pageable);

  // ClCode1 >= ,AND ClCode1 <= ,AND ClTypeCode >= ,AND ClTypeCode <= ,AND ClNo >= ,AND ClNo <=
  public Slice<ClMain> findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqual(int clCode1_0, int clCode1_1, String clTypeCode_2, String clTypeCode_3, int clNo_4, int clNo_5, Pageable pageable);

  // ClCode1 >= ,AND ClCode1 <= ,AND ClTypeCode >= ,AND ClTypeCode <= ,AND ClNo >= ,AND ClNo <= ,AND CustUKey = 
  public Slice<ClMain> findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndCustUKeyIs(int clCode1_0, int clCode1_1, String clTypeCode_2, String clTypeCode_3, int clNo_4, int clNo_5, String custUKey_6, Pageable pageable);

  // ClCode1 >= ,AND ClCode1 <= ,AND ClCode2 >= ,AND ClCode2 <= ,AND ClNo >= ,AND ClNo <= ,AND ClTypeCode >= ,AND ClTypeCode <=
  public Slice<ClMain> findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndClCode2GreaterThanEqualAndClCode2LessThanEqualAndClNoGreaterThanEqualAndClNoLessThanEqualAndClTypeCodeGreaterThanEqualAndClTypeCodeLessThanEqual(int clCode1_0, int clCode1_1, int clCode2_2, int clCode2_3, int clNo_4, int clNo_5, String clTypeCode_6, String clTypeCode_7, Pageable pageable);

  // ClCode1 = ,AND ClCode2 = 
  public Optional<ClMain> findTopByClCode1IsAndClCode2IsOrderByClNoDesc(int clCode1_0, int clCode2_1);

  // ClCode1 >= ,AND ClCode1 <= ,AND CustUKey = 
  public Slice<ClMain> findAllByClCode1GreaterThanEqualAndClCode1LessThanEqualAndCustUKeyIsOrderByClCode1AscClCode2AscClNoAsc(int clCode1_0, int clCode1_1, String custUKey_2, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<ClMain> findByClMainId(ClMainId clMainId);

}

