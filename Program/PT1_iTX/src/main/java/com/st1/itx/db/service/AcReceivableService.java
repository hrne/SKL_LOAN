package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcReceivable;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcReceivableId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcReceivableService {

  /**
   * findByPrimaryKey
   *
   * @param acReceivableId PK
   * @param titaVo Variable-Length Argument
   * @return AcReceivable AcReceivable
   */
  public AcReceivable findById(AcReceivableId acReceivableId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClsFlag = ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND CustNo &gt;= ,AND CustNo &lt;= 
   *
   * @param clsFlag_0 clsFlag_0
   * @param branchNo_1 branchNo_1
   * @param currencyCode_2 currencyCode_2
   * @param acNoCode_3 acNoCode_3
   * @param acSubCode_4 acSubCode_4
   * @param acDtlCode_5 acDtlCode_5
   * @param custNo_6 custNo_6
   * @param custNo_7 custNo_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvClsFlagEq(int clsFlag_0, String branchNo_1, String currencyCode_2, String acNoCode_3, String acSubCode_4, String acDtlCode_5, int custNo_6, int custNo_7, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND AcctFlag = ,AND FacmNo &gt;=
   *
   * @param custNo_0 custNo_0
   * @param acctFlag_1 acctFlag_1
   * @param facmNo_2 facmNo_2
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public AcReceivable acrvFacmNoFirst(int custNo_0, int acctFlag_1, int facmNo_2, TitaVo... titaVo);

  /**
   * AcctCode = ,AND CustNo = ,AND RvNo = 
   *
   * @param acctCode_0 acctCode_0
   * @param custNo_1 custNo_1
   * @param rvNo_2 rvNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvRvNoEq(String acctCode_0, int custNo_1, String rvNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * ClsFlag = ,AND CustNo = ,AND AcctFlag = ,AND FacmNo &gt;= ,AND FacmNo &lt;=
   *
   * @param clsFlag_0 clsFlag_0
   * @param custNo_1 custNo_1
   * @param acctFlag_2 acctFlag_2
   * @param facmNo_3 facmNo_3
   * @param facmNo_4 facmNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvFacmNoRange(int clsFlag_0, int custNo_1, int acctFlag_2, int facmNo_3, int facmNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * AcctCode =, AND ClsFlag = ,AND OpenAcDate &lt;
   *
   * @param acctCode_0 acctCode_0
   * @param clsFlag_1 clsFlag_1
   * @param openAcDate_2 openAcDate_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvOpenAcDateLq(String acctCode_0, int clsFlag_1, int openAcDate_2, int index, int limit, TitaVo... titaVo);

  /**
   * ClsFlag = , AND AcctCode ^i
   *
   * @param clsFlag_0 clsFlag_0
   * @param acctCode_1 acctCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> UseL5074(int clsFlag_0, List<String> acctCode_1, int index, int limit, TitaVo... titaVo);

  /**
   * ClsFlag = ,AND AcctCode = ,AND CustNo &gt;= ,AND CustNo &lt;=
   *
   * @param clsFlag_0 clsFlag_0
   * @param acctCode_1 acctCode_1
   * @param custNo_2 custNo_2
   * @param custNo_3 custNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acctCodeEq(int clsFlag_0, String acctCode_1, int custNo_2, int custNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND OpenAcDate &gt;= ,AND OpenAcDate &lt;= 
   *
   * @param branchNo_0 branchNo_0
   * @param currencyCode_1 currencyCode_1
   * @param acNoCode_2 acNoCode_2
   * @param acSubCode_3 acSubCode_3
   * @param acDtlCode_4 acDtlCode_4
   * @param openAcDate_5 openAcDate_5
   * @param openAcDate_6 openAcDate_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvOpenAcDateRange(String branchNo_0, String currencyCode_1, String acNoCode_2, String acSubCode_3, String acDtlCode_4, int openAcDate_5, int openAcDate_6, int index, int limit, TitaVo... titaVo);

  /**
   * AcctCode = ,AND CustNo = ,AND FacmNo = ,AND OpenAcDate =
   *
   * @param acctCode_0 acctCode_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param openAcDate_3 openAcDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public AcReceivable useL2670First(String acctCode_0, int custNo_1, int facmNo_2, int openAcDate_3, TitaVo... titaVo);

  /**
   * AcctCode = ,AND CustNo = ,AND FacmNo &gt;= ,AND FacmNo &lt;= ,AND ClsFlag &gt;= ,AND ClsFlag &lt;=
   *
   * @param acctCode_0 acctCode_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param facmNo_3 facmNo_3
   * @param clsFlag_4 clsFlag_4
   * @param clsFlag_5 clsFlag_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> useL2062Eq(String acctCode_0, int custNo_1, int facmNo_2, int facmNo_3, int clsFlag_4, int clsFlag_5, int index, int limit, TitaVo... titaVo);

  /**
   * ClsFlag = ,AND AcBookCode = ,AND AcSubBookCode %,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND CustNo &gt;= ,AND CustNo &lt;= 
   *
   * @param clsFlag_0 clsFlag_0
   * @param acBookCode_1 acBookCode_1
   * @param acSubBookCode_2 acSubBookCode_2
   * @param branchNo_3 branchNo_3
   * @param currencyCode_4 currencyCode_4
   * @param acNoCode_5 acNoCode_5
   * @param acSubCode_6 acSubCode_6
   * @param acDtlCode_7 acDtlCode_7
   * @param custNo_8 custNo_8
   * @param custNo_9 custNo_9
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvClsFlagSubBook(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, String branchNo_3, String currencyCode_4, String acNoCode_5, String acSubCode_6, String acDtlCode_7, int custNo_8, int custNo_9, int index, int limit, TitaVo... titaVo);

  /**
   * ClsFlag = ,AND AcBookCode = ,AND AcSubBookCode % ,AND CustNo = ,AND AcctFlag = ,AND FacmNo &gt;= ,AND FacmNo &lt;=
   *
   * @param clsFlag_0 clsFlag_0
   * @param acBookCode_1 acBookCode_1
   * @param acSubBookCode_2 acSubBookCode_2
   * @param custNo_3 custNo_3
   * @param acctFlag_4 acctFlag_4
   * @param facmNo_5 facmNo_5
   * @param facmNo_6 facmNo_6
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvFacmNoSubBook(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, int custNo_3, int acctFlag_4, int facmNo_5, int facmNo_6, int index, int limit, TitaVo... titaVo);

  /**
   * ClsFlag = ,AND AcBookCode = ,AND AcSubBookCode % ,AND AcctCode = ,AND CustNo &gt;= ,AND CustNo &lt;=
   *
   * @param clsFlag_0 clsFlag_0
   * @param acBookCode_1 acBookCode_1
   * @param acSubBookCode_2 acSubBookCode_2
   * @param acctCode_3 acctCode_3
   * @param custNo_4 custNo_4
   * @param custNo_5 custNo_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acctCodeSubBook(int clsFlag_0, String acBookCode_1, String acSubBookCode_2, String acctCode_3, int custNo_4, int custNo_5, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND AcctFlag &gt;= ,AND AcctFlag &lt;= ,AND RvNo %
   *
   * @param custNo_0 custNo_0
   * @param acctFlag_1 acctFlag_1
   * @param acctFlag_2 acctFlag_2
   * @param rvNo_3 rvNo_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> useL2064Eq(int custNo_0, int acctFlag_1, int acctFlag_2, String rvNo_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND AcctFlag &gt;= ,AND AcctFlag &lt;= ,AND RvNo %
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param acctFlag_2 acctFlag_2
   * @param acctFlag_3 acctFlag_3
   * @param rvNo_4 rvNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> useL2r58Eq(int custNo_0, int facmNo_1, int acctFlag_2, int acctFlag_3, String rvNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo &gt;= ,AND CustNo &lt;= ,AND ClsFlag = ,AND ReceivableFlag = ,AND RvNo %
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param clsFlag_2 clsFlag_2
   * @param receivableFlag_3 receivableFlag_3
   * @param rvNo_4 rvNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> useBs902Eq(int custNo_0, int custNo_1, int clsFlag_2, int receivableFlag_3, String rvNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * AcctCode % ,AND CustNo = ,AND FacmNo = ,AND RvNo = 
   *
   * @param acctCode_0 acctCode_0
   * @param custNo_1 custNo_1
   * @param facmNo_2 facmNo_2
   * @param rvNo_3 rvNo_3
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public AcReceivable acctCodeLikeFirst(String acctCode_0, int custNo_1, int facmNo_2, String rvNo_3, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode %,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND CustNo &gt;= ,AND CustNo &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param branchNo_2 branchNo_2
   * @param currencyCode_3 currencyCode_3
   * @param acNoCode_4 acNoCode_4
   * @param acSubCode_5 acSubCode_5
   * @param acDtlCode_6 acDtlCode_6
   * @param custNo_7 custNo_7
   * @param custNo_8 custNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvClsFlag2SubBook(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int custNo_7, int custNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND CustNo = ,AND AcctFlag = ,AND FacmNo &gt;= ,AND FacmNo &lt;=
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param custNo_2 custNo_2
   * @param acctFlag_3 acctFlag_3
   * @param facmNo_4 facmNo_4
   * @param facmNo_5 facmNo_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acrvFacmNo2SubBook(String acBookCode_0, String acSubBookCode_1, int custNo_2, int acctFlag_3, int facmNo_4, int facmNo_5, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND AcSubBookCode % ,AND AcctCode = ,AND CustNo &gt;= ,AND CustNo &lt;=
   *
   * @param acBookCode_0 acBookCode_0
   * @param acSubBookCode_1 acSubBookCode_1
   * @param acctCode_2 acctCode_2
   * @param custNo_3 custNo_3
   * @param custNo_4 custNo_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcReceivable AcReceivable of List
   */
  public Slice<AcReceivable> acctCode2SubBook(String acBookCode_0, String acSubBookCode_1, String acctCode_2, int custNo_3, int custNo_4, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AcReceivable
   * 
   * @param acReceivableId key
   * @param titaVo Variable-Length Argument
   * @return AcReceivable AcReceivable
   */
  public AcReceivable holdById(AcReceivableId acReceivableId, TitaVo... titaVo);

  /**
   * hold By AcReceivable
   * 
   * @param acReceivable key
   * @param titaVo Variable-Length Argument
   * @return AcReceivable AcReceivable
   */
  public AcReceivable holdById(AcReceivable acReceivable, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param acReceivable Entity
   * @param titaVo Variable-Length Argument
   * @return AcReceivable Entity
   * @throws DBException exception
   */
  public AcReceivable insert(AcReceivable acReceivable, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param acReceivable Entity
   * @param titaVo Variable-Length Argument
   * @return AcReceivable Entity
   * @throws DBException exception
   */
  public AcReceivable update(AcReceivable acReceivable, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param acReceivable Entity
   * @param titaVo Variable-Length Argument
   * @return AcReceivable Entity
   * @throws DBException exception
   */
  public AcReceivable update2(AcReceivable acReceivable, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param acReceivable Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AcReceivable acReceivable, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param acReceivable Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AcReceivable> acReceivable, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param acReceivable Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AcReceivable> acReceivable, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param acReceivable Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AcReceivable> acReceivable, TitaVo... titaVo) throws DBException;

}
