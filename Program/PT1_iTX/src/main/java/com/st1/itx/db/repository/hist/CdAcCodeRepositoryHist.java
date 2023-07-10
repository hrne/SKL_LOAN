package com.st1.itx.db.repository.hist;


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
public interface CdAcCodeRepositoryHist extends JpaRepository<CdAcCode, CdAcCodeId> {

  // AcctCode = 
  public Optional<CdAcCode> findTopByAcctCodeIs(String acctCode_0);

  // AcNoCode >= ,AND AcNoCode <= ,AND AcSubCode >= ,AND AcSubCode <= ,AND AcDtlCode >= ,AND AcDtlCode <= 
  public Slice<CdAcCode> findAllByAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(String acNoCode_0, String acNoCode_1, String acSubCode_2, String acSubCode_3, String acDtlCode_4, String acDtlCode_5, Pageable pageable);

  // AcNoCodeOld >= ,AND AcNoCodeOld <= ,AND AcSubCode >= ,AND AcSubCode <= ,AND AcDtlCode >= ,AND AcDtlCode <= 
  public Slice<CdAcCode> findAllByAcNoCodeOldGreaterThanEqualAndAcNoCodeOldLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeOldAscAcSubCodeAscAcDtlCodeAsc(String acNoCodeOld_0, String acNoCodeOld_1, String acSubCode_2, String acSubCode_3, String acDtlCode_4, String acDtlCode_5, Pageable pageable);

  // AcNoCode >= ,AND AcNoCode <= ,AND AcSubCode >= ,AND AcSubCode <= ,AND AcDtlCode >= ,AND AcDtlCode <= ,AND AcNoItem%
  public Slice<CdAcCode> findAllByAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualAndAcNoItemLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(String acNoCode_0, String acNoCode_1, String acSubCode_2, String acSubCode_3, String acDtlCode_4, String acDtlCode_5, String acNoItem_6, Pageable pageable);

  // AcNoItem%
  public Slice<CdAcCode> findAllByAcNoItemLikeOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(String acNoItem_0, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<CdAcCode> findByCdAcCodeId(CdAcCodeId cdAcCodeId);

}

