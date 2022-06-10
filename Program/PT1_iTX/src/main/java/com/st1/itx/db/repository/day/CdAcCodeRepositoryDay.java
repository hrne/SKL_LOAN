package com.st1.itx.db.repository.day;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdAcCodeRepositoryDay extends JpaRepository<CdAcCode, CdAcCodeId> {

  // AcctCode = 
  public Optional<CdAcCode> findTopByAcctCodeIs(String acctCode_0);

  // AcNoCode >= ,AND AcNoCode <= ,AND AcSubCode >= ,AND AcSubCode <= ,AND AcDtlCode >= ,AND AcDtlCode <= 
  public Slice<CdAcCode> findAllByAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(String acNoCode_0, String acNoCode_1, String acSubCode_2, String acSubCode_3, String acDtlCode_4, String acDtlCode_5, Pageable pageable);

  // AcNoCodeOld >= ,AND AcNoCodeOld <= ,AND AcSubCode >= ,AND AcSubCode <= ,AND AcDtlCode >= ,AND AcDtlCode <= 
  public Slice<CdAcCode> findAllByAcNoCodeOldGreaterThanEqualAndAcNoCodeOldLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeOldAscAcSubCodeAscAcDtlCodeAsc(String acNoCodeOld_0, String acNoCodeOld_1, String acSubCode_2, String acSubCode_3, String acDtlCode_4, String acDtlCode_5, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdAcCode> findByCdAcCodeId(CdAcCodeId cdAcCodeId);

}

