package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcDetail;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcDetailId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcDetailService {

  /**
   * findByPrimaryKey
   *
   * @param acDetailId PK
   * @param titaVo Variable-Length Argument
   * @return AcDetail AcDetail
   */
  public AcDetail findById(AcDetailId acDetailId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * RelDy = ,AND RelTxseq = 
   *
   * @param relDy_0 relDy_0
   * @param relTxseq_1 relTxseq_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlRelTxseqEq(int relDy_0, String relTxseq_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND AcDate &gt;= ,AND AcDate &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acNoCode_4 acNoCode_4
   * @param acSubCode_5 acSubCode_5
   * @param acDtlCode_6 acDtlCode_6
   * @param acDate_7 acDate_7
   * @param acDate_8 acDate_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlAcDateRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int acDate_7, int acDate_8, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND SumNo &gt;= ,AND SumNo &lt;= 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param acNoCode_3 acNoCode_3
   * @param acNoCode_4 acNoCode_4
   * @param sumNo_5 sumNo_5
   * @param sumNo_6 sumNo_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlSumNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String sumNo_5, String sumNo_6, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaTlrNo &gt;= ,AND TitaTlrNo &lt;= 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param acNoCode_3 acNoCode_3
   * @param acNoCode_4 acNoCode_4
   * @param titaTlrNo_5 titaTlrNo_5
   * @param titaTlrNo_6 titaTlrNo_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlTitaTlrNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaTlrNo_5, String titaTlrNo_6, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaBatchNo &gt;= ,AND TitaBatchNo &lt;= 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param acNoCode_3 acNoCode_3
   * @param acNoCode_4 acNoCode_4
   * @param titaBatchNo_5 titaBatchNo_5
   * @param titaBatchNo_6 titaBatchNo_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlTitaBatchNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaBatchNo_5, String titaBatchNo_6, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND DscptCode &gt;= ,AND DscptCode &lt;= 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param acNoCode_3 acNoCode_3
   * @param acNoCode_4 acNoCode_4
   * @param dscptCode_5 dscptCode_5
   * @param dscptCode_6 dscptCode_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlDscptCodeRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String dscptCode_5, String dscptCode_6, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND SlipBatNo &gt;= ,AND SlipBatNo &lt;= 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param acNoCode_3 acNoCode_3
   * @param acNoCode_4 acNoCode_4
   * @param slipBatNo_5 slipBatNo_5
   * @param slipBatNo_6 slipBatNo_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlSlipBatNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, int slipBatNo_5, int slipBatNo_6, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaSecNo &gt;= ,AND TitaSecNo &lt;= 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param acNoCode_3 acNoCode_3
   * @param acNoCode_4 acNoCode_4
   * @param titaSecNo_5 titaSecNo_5
   * @param titaSecNo_6 titaSecNo_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlTitaSecNoRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, String titaSecNo_5, String titaSecNo_6, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param acNoCode_3 acNoCode_3
   * @param acNoCode_4 acNoCode_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlAcNoCodeRange(String branchNo_0, String currencyCode_1, int acDate_2, String acNoCode_3, String acNoCode_4, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND CustNo = 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param custNo_3 custNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlCustNo(String branchNo_0, String currencyCode_1, int acDate_2, int custNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaTlrNo = 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param titaTlrNo_3 titaTlrNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlTitaTlrNo(String branchNo_0, String currencyCode_1, int acDate_2, String titaTlrNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaBatchNo = 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param titaBatchNo_3 titaBatchNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlTitaBatchNo(String branchNo_0, String currencyCode_1, int acDate_2, String titaBatchNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaTxCd = 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param titaTxCd_3 titaTxCd_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlTitaTxCd(String branchNo_0, String currencyCode_1, int acDate_2, String titaTxCd_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND SlipBatNo = 
   *
   * @param acDate_0 acDate_0
   * @param slipBatNo_1 slipBatNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> findL9RptData(int acDate_0, int slipBatNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * AcctCode = ,AND CustNo = ,AND RvNo = ,
   *
   * @param acctCode_0 acctCode_0
   * @param custNo_1 custNo_1
   * @param rvNo_2 rvNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> findL2613(String acctCode_0, int custNo_1, String rvNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND TitaKinbr = ,AND TitaTlrNo = ,AND TitaTxtNo = 
   *
   * @param acDate_0 acDate_0
   * @param titaKinbr_1 titaKinbr_1
   * @param titaTlrNo_2 titaTlrNo_2
   * @param titaTxtNo_3 titaTxtNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> findTxtNoEq(int acDate_0, String titaKinbr_1, String titaTlrNo_2, int titaTxtNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND TitaBatchNo = 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acDate_2 acDate_2
   * @param titaBatchNo_3 titaBatchNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> findL4101(String branchNo_0, String currencyCode_1, int acDate_2, String titaBatchNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND CustNo = ,AND FacmNo = ,AND AcDate &gt;= ,AND AcDate &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acNoCode_4 acNoCode_4
   * @param acSubCode_5 acSubCode_5
   * @param acDtlCode_6 acDtlCode_6
   * @param custNo_7 custNo_7
   * @param facmNo_8 facmNo_8
   * @param acDate_9 acDate_9
   * @param acDate_10 acDate_10
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> findL6908(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int custNo_7, int facmNo_8, int acDate_9, int acDate_10, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND AcctFlag = ,AND AcDate &gt;= ,AND AcDate &lt;= 
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param acctFlag_3 acctFlag_3
   * @param acDate_4 acDate_4
   * @param acDate_5 acDate_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> bormNoAcDateRange(int custNo_0, int facmNo_1, int bormNo_2, int acctFlag_3, int acDate_4, int acDate_5, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookAcNoCodeRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND SumNo &gt;= ,AND SumNo &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param sumNo_7 sumNo_7
   * @param sumNo_8 sumNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookSumNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String sumNo_7, String sumNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaTlrNo &gt;= ,AND TitaTlrNo &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param titaTlrNo_7 titaTlrNo_7
   * @param titaTlrNo_8 titaTlrNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookTitaTlrNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaTlrNo_7, String titaTlrNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaBatchNo &gt;= ,AND TitaBatchNo &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param titaBatchNo_7 titaBatchNo_7
   * @param titaBatchNo_8 titaBatchNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookTitaBatchNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaBatchNo_7, String titaBatchNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND DscptCode &gt;= ,AND DscptCode &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param dscptCode_7 dscptCode_7
   * @param dscptCode_8 dscptCode_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookDscptCodeRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String dscptCode_7, String dscptCode_8, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND SlipBatNo &gt;= ,AND SlipBatNo &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param slipBatNo_7 slipBatNo_7
   * @param slipBatNo_8 slipBatNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookSlipBatNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, int slipBatNo_7, int slipBatNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaSecNo &gt;= ,AND TitaSecNo &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param titaSecNo_7 titaSecNo_7
   * @param titaSecNo_8 titaSecNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookTitaSecNoRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaSecNo_7, String titaSecNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND RvNo %
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param rvNo_7 rvNo_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookAcNoCodeRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String rvNo_7, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND SumNo &gt;= ,AND SumNo &lt;= ,AND RvNo %
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param sumNo_7 sumNo_7
   * @param sumNo_8 sumNo_8
   * @param rvNo_9 rvNo_9
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookSumNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String sumNo_7, String sumNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaTlrNo &gt;= ,AND TitaTlrNo &lt;= ,AND RvNo %
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param titaTlrNo_7 titaTlrNo_7
   * @param titaTlrNo_8 titaTlrNo_8
   * @param rvNo_9 rvNo_9
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookTitaTlrNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaTlrNo_7, String titaTlrNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaBatchNo &gt;= ,AND TitaBatchNo &lt;= ,AND RvNo %
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param titaBatchNo_7 titaBatchNo_7
   * @param titaBatchNo_8 titaBatchNo_8
   * @param rvNo_9 rvNo_9
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookTitaBatchNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaBatchNo_7, String titaBatchNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND DscptCode &gt;= ,AND DscptCode &lt;= ,AND RvNo %
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param dscptCode_7 dscptCode_7
   * @param dscptCode_8 dscptCode_8
   * @param rvNo_9 rvNo_9
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookDscptCodeRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String dscptCode_7, String dscptCode_8, String rvNo_9, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND SlipBatNo &gt;= ,AND SlipBatNo &lt;= ,AND RvNo %
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param slipBatNo_7 slipBatNo_7
   * @param slipBatNo_8 slipBatNo_8
   * @param rvNo_9 rvNo_9
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookSlipBatNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, int slipBatNo_7, int slipBatNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND TitaSecNo &gt;= ,AND TitaSecNo &lt;= ,AND RvNo %
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acDate_4 acDate_4
   * @param acNoCode_5 acNoCode_5
   * @param acNoCode_6 acNoCode_6
   * @param titaSecNo_7 titaSecNo_7
   * @param titaSecNo_8 titaSecNo_8
   * @param rvNo_9 rvNo_9
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> SubBookTitaSecNoRange1(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, int acDate_4, String acNoCode_5, String acNoCode_6, String titaSecNo_7, String titaSecNo_8, String rvNo_9, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode =  ,AND AcDate &gt;= ,AND AcDate &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acNoCode_4 acNoCode_4
   * @param acSubCode_5 acSubCode_5
   * @param acDate_6 acDate_6
   * @param acDate_7 acDate_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcDetail AcDetail of List
   */
  public Slice<AcDetail> acdtlAcDateRange2(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, int acDate_6, int acDate_7, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AcDetail
   * 
   * @param acDetailId key
   * @param titaVo Variable-Length Argument
   * @return AcDetail AcDetail
   */
  public AcDetail holdById(AcDetailId acDetailId, TitaVo... titaVo);

  /**
   * hold By AcDetail
   * 
   * @param acDetail key
   * @param titaVo Variable-Length Argument
   * @return AcDetail AcDetail
   */
  public AcDetail holdById(AcDetail acDetail, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param acDetail Entity
   * @param titaVo Variable-Length Argument
   * @return AcDetail Entity
   * @throws DBException exception
   */
  public AcDetail insert(AcDetail acDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param acDetail Entity
   * @param titaVo Variable-Length Argument
   * @return AcDetail Entity
   * @throws DBException exception
   */
  public AcDetail update(AcDetail acDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param acDetail Entity
   * @param titaVo Variable-Length Argument
   * @return AcDetail Entity
   * @throws DBException exception
   */
  public AcDetail update2(AcDetail acDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param acDetail Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AcDetail acDetail, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param acDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AcDetail> acDetail, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param acDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AcDetail> acDetail, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param acDetail Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AcDetail> acDetail, TitaVo... titaVo) throws DBException;

}
