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

import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.AcMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcMainRepositoryDay extends JpaRepository<AcMain, AcMainId> {

  // AcBookCode = ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND AcDate >= ,AND AcDate <= 
  public Slice<AcMain> findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAsc(String acBookCode_0, String branchNo_1, String currencyCode_2, String acNoCode_3, String acSubCode_4, String acDtlCode_5, int acDate_6, int acDate_7, Pageable pageable);

  // AcBookCode = ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode >= ,AND AcNoCode <= ,AND AcSubCode >= ,AND AcSubCode <= ,AND AcDtlCode >= ,AND AcDtlCode <= 
  public Slice<AcMain> findAllByAcBookCodeIsAndBranchNoIsAndCurrencyCodeIsAndAcDateIsAndAcNoCodeGreaterThanEqualAndAcNoCodeLessThanEqualAndAcSubCodeGreaterThanEqualAndAcSubCodeLessThanEqualAndAcDtlCodeGreaterThanEqualAndAcDtlCodeLessThanEqualOrderByAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(String acBookCode_0, String branchNo_1, String currencyCode_2, int acDate_3, String acNoCode_4, String acNoCode_5, String acSubCode_6, String acSubCode_7, String acDtlCode_8, String acDtlCode_9, Pageable pageable);

  // AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND AcDate >= ,AND AcDate <= 
  public Slice<AcMain> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAscAcSubBookCodeAsc(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int acDate_7, int acDate_8, Pageable pageable);

  // AcDate =  
  public Slice<AcMain> findAllByAcDateIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAscAcNoCodeAscAcSubCodeAscAcDtlCodeAsc(int acDate_0, Pageable pageable);

  // AcDate = ,AND AcctCode =
  public Slice<AcMain> findAllByAcDateIsAndAcctCodeIsOrderByAcBookCodeAscBranchNoAscCurrencyCodeAsc(int acDate_0, String acctCode_1, Pageable pageable);

  // AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode =  ,AND AcDate >= ,AND AcDate <= 
  public Slice<AcMain> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAscAcSubBookCodeAscAcNoCodeAsc(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, int acDate_6, int acDate_7, Pageable pageable);

  // AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate >= ,AND AcDate <= 
  public Slice<AcMain> findAllByAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcDateGreaterThanEqualAndAcDateLessThanEqualOrderByAcDateAscAcSubBookCodeAscAcNoCodeAsc(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, int acDate_5, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<AcMain> findByAcMainId(AcMainId acMainId);

}

