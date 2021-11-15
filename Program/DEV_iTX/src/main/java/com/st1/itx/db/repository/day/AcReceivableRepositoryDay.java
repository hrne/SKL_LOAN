package com.st1.itx.db.repository.day;


import java.util.Optional;

import java.util.List;
import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcReceivableRepositoryDay extends JpaRepository<AcReceivable, AcReceivableId> {

  // ClsFlag = ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND CustNo >= ,AND CustNo <= 
  public Slice<AcReceivable> findAllByClsFlagIsAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(int clsFlag_0, String branchNo_1, String currencyCode_2, String acNoCode_3, String acSubCode_4, String acDtlCode_5, int custNo_6, int custNo_7, Pageable pageable);

  // CustNo = ,AND AcctFlag = ,AND FacmNo >=
  public Optional<AcReceivable> findTopByCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualOrderByFacmNoDescRvNoDesc(int custNo_0, int acctFlag_1, int facmNo_2);

  // AcctCode = ,AND CustNo = ,AND RvNo = 
  public Slice<AcReceivable> findAllByAcctCodeIsAndCustNoIsAndRvNoIsOrderByFacmNoAsc(String acctCode_0, int custNo_1, String rvNo_2, Pageable pageable);

  // ClsFlag = ,AND CustNo = ,AND AcctFlag = ,AND FacmNo >= ,AND FacmNo <=
  public Slice<AcReceivable> findAllByClsFlagIsAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByOpenAcDateAsc(int clsFlag_0, int custNo_1, int acctFlag_2, int facmNo_3, int facmNo_4, Pageable pageable);

  // AcctCode =, AND ClsFlag = ,AND OpenAcDate <
  public Slice<AcReceivable> findAllByAcctCodeIsAndClsFlagIsAndOpenAcDateLessThanOrderByCustNoAscFacmNoAscRvNoAsc(String acctCode_0, int clsFlag_1, int openAcDate_2, Pageable pageable);

  // ClsFlag = , AND AcctCode ^i
  public Slice<AcReceivable> findAllByClsFlagIsAndAcctCodeInOrderByCustNoAsc(int clsFlag_0, List<String> acctCode_1, Pageable pageable);

  // ClsFlag = ,AND AcctCode = ,AND CustNo >= ,AND CustNo <=
  public Slice<AcReceivable> findAllByClsFlagIsAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(int clsFlag_0, String acctCode_1, int custNo_2, int custNo_3, Pageable pageable);

  // BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND OpenAcDate >= ,AND OpenAcDate <= 
  public Slice<AcReceivable> findAllByBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndOpenAcDateGreaterThanEqualAndOpenAcDateLessThanEqualOrderByOpenAcDateAscCustNoAscFacmNoAsc(String branchNo_0, String currencyCode_1, String acNoCode_2, String acSubCode_3, String acDtlCode_4, int openAcDate_5, int openAcDate_6, Pageable pageable);

  // AcctCode = ,AND CustNo = ,AND FacmNo = ,AND OpenAcDate =
  public Optional<AcReceivable> findTopByAcctCodeIsAndCustNoIsAndFacmNoIsAndOpenAcDateIsOrderByRvNoDesc(String acctCode_0, int custNo_1, int facmNo_2, int openAcDate_3);

  // AcctCode = ,AND CustNo = ,AND FacmNo >= ,AND FacmNo <= ,AND ClsFlag >= ,AND ClsFlag <=
  public Slice<AcReceivable> findAllByAcctCodeIsAndCustNoIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualAndClsFlagGreaterThanEqualAndClsFlagLessThanEqual(String acctCode_0, int custNo_1, int facmNo_2, int facmNo_3, int clsFlag_4, int clsFlag_5, Pageable pageable);

  // ClsFlag = ,AND AcBookCode = ,AND AcSubBookCode %,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND CustNo >= ,AND CustNo <= 
  public Slice<AcReceivable> findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndBranchNoIsAndCurrencyCodeIsAndAcNoCodeIsAndAcSubCodeIsAndAcDtlCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, String branchNo_3, String currencyCode_4, String acNoCode_5, String acSubCode_6, String acDtlCode_7, int custNo_8, int custNo_9, Pageable pageable);

  // ClsFlag = ,AND AcBookCode = ,AND AcSubBookCode % ,AND CustNo = ,AND AcctFlag = ,AND FacmNo >= ,AND FacmNo <=
  public Slice<AcReceivable> findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndCustNoIsAndAcctFlagIsAndFacmNoGreaterThanEqualAndFacmNoLessThanEqualOrderByOpenAcDateAsc(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, int custNo_3, int acctFlag_4, int facmNo_5, int facmNo_6, Pageable pageable);

  // ClsFlag = ,AND AcBookCode = ,AND AcSubBookCode % ,AND AcctCode = ,AND CustNo >= ,AND CustNo <=
  public Slice<AcReceivable> findAllByClsFlagIsAndAcBookCodeIsAndAcSubBookCodeLikeAndAcctCodeIsAndCustNoGreaterThanEqualAndCustNoLessThanEqualOrderByCustNoAscFacmNoAsc(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, String acctCode_3, int custNo_4, int custNo_5, Pageable pageable);

  // CustNo = ,AND AcctFlag >= ,AND AcctFlag <= ,AND RvNo %
  public Slice<AcReceivable> findAllByCustNoIsAndAcctFlagGreaterThanEqualAndAcctFlagLessThanEqualAndRvNoLikeOrderByFacmNoAsc(int custNo_0, int acctFlag_1, int acctFlag_2, String rvNo_3, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<AcReceivable> findByAcReceivableId(AcReceivableId acReceivableId);

}

