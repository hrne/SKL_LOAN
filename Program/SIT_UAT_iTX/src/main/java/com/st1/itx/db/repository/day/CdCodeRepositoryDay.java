package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCodeRepositoryDay extends JpaRepository<CdCode, CdCodeId> {

  // DefCode = ,AND Code % 
  public Slice<CdCode> findAllByDefCodeIsAndCodeLikeOrderByCodeAsc(String defCode_0, String code_1, Pageable pageable);

  // DefCode = ,AND DefType = ,AND Code %
  public Slice<CdCode> findAllByDefCodeIsAndDefTypeIsAndCodeLikeOrderByCodeAsc(String defCode_0, int defType_1, String code_2, Pageable pageable);

  // DefCode <> ,AND DefType = ,AND Code %, AND Item %
  public Slice<CdCode> findAllByDefCodeNotAndDefTypeIsAndCodeLikeAndItemLikeOrderByDefCodeAscCodeAsc(String defCode_0, int defType_1, String code_2, String item_3, Pageable pageable);

  // DefType = 
  public Slice<CdCode> findAllByDefTypeIsOrderByDefCodeAscCodeAsc(int defType_0, Pageable pageable);

  // DefType = ,AND DefCode = 
  public Slice<CdCode> findAllByDefTypeIsAndDefCodeIsOrderByDefCodeAscCodeAsc(int defType_0, String defCode_1, Pageable pageable);

  // DefType = ,AND DefCode = ,AND Code = 
  public Optional<CdCode> findTopByDefTypeIsAndDefCodeIsAndCodeIsOrderByDefCodeAscCodeAsc(int defType_0, String defCode_1, String code_2);

  // DefType = ,AND DefCode = ,AND Code ^i
  public Slice<CdCode> findAllByDefTypeIsAndDefCodeIsAndCodeInOrderByDefCodeAscCodeAsc(int defType_0, String defCode_1, List<String> code_2, Pageable pageable);

  // DefCode = ,AND Item %
  public Slice<CdCode> findAllByDefCodeIsAndItemLikeOrderByCodeAsc(String defCode_0, String item_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdCode> findByCdCodeId(CdCodeId cdCodeId);

}

