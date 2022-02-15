package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcMain;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcMainId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcMainService {

  /**
   * findByPrimaryKey
   *
   * @param acMainId PK
   * @param titaVo Variable-Length Argument
   * @return AcMain AcMain
   */
  public AcMain findById(AcMainId acMainId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcMain AcMain of List
   */
  public Slice<AcMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND BranchNo = ,AND CurrencyCode = ,AND AcNoCode = ,AND AcSubCode = ,AND AcDtlCode = ,AND AcDate &gt;= ,AND AcDate &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param branchNo_1 branchNo_1
   * @param currencyCode_2 currencyCode_2
   * @param acNoCode_3 acNoCode_3
   * @param acSubCode_4 acSubCode_4
   * @param acDtlCode_5 acDtlCode_5
   * @param acDate_6 acDate_6
   * @param acDate_7 acDate_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcMain AcMain of List
   */
  public Slice<AcMain> acmainAcDateRange(String acBookCode_0, String branchNo_1, String currencyCode_2, String acNoCode_3, String acSubCode_4, String acDtlCode_5, int acDate_6, int acDate_7, int index, int limit, TitaVo... titaVo);

  /**
   * AcBookCode = ,AND BranchNo = ,AND CurrencyCode = ,AND AcDate = ,AND AcNoCode &gt;= ,AND AcNoCode &lt;= ,AND AcSubCode &gt;= ,AND AcSubCode &lt;= ,AND AcDtlCode &gt;= ,AND AcDtlCode &lt;= 
   *
   * @param acBookCode_0 acBookCode_0
   * @param branchNo_1 branchNo_1
   * @param currencyCode_2 currencyCode_2
   * @param acDate_3 acDate_3
   * @param acNoCode_4 acNoCode_4
   * @param acNoCode_5 acNoCode_5
   * @param acSubCode_6 acSubCode_6
   * @param acSubCode_7 acSubCode_7
   * @param acDtlCode_8 acDtlCode_8
   * @param acDtlCode_9 acDtlCode_9
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcMain AcMain of List
   */
  public Slice<AcMain> acmainAcCodeRange(String acBookCode_0, String branchNo_1, String currencyCode_2, int acDate_3, String acNoCode_4, String acNoCode_5, String acSubCode_6, String acSubCode_7, String acDtlCode_8, String acDtlCode_9, int index, int limit, TitaVo... titaVo);

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
   * @return Slice AcMain AcMain of List
   */
  public Slice<AcMain> acmainAcBookCodeRange(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, String acDtlCode_6, int acDate_7, int acDate_8, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate =  
   *
   * @param acDate_0 acDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcMain AcMain of List
   */
  public Slice<AcMain> acmainAcDateEq(int acDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * AcDate = ,AND AcctCode =
   *
   * @param acDate_0 acDate_0
   * @param acctCode_1 acctCode_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcMain AcMain of List
   */
  public Slice<AcMain> acctCodeEq(int acDate_0, String acctCode_1, int index, int limit, TitaVo... titaVo);

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
   * @return Slice AcMain AcMain of List
   */
  public Slice<AcMain> acmainAcBookCodeRange2(String acBookCode_0, String acSubBookCode_1, String branchNo_2, String currencyCode_3, String acNoCode_4, String acSubCode_5, int acDate_6, int acDate_7, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AcMain
   * 
   * @param acMainId key
   * @param titaVo Variable-Length Argument
   * @return AcMain AcMain
   */
  public AcMain holdById(AcMainId acMainId, TitaVo... titaVo);

  /**
   * hold By AcMain
   * 
   * @param acMain key
   * @param titaVo Variable-Length Argument
   * @return AcMain AcMain
   */
  public AcMain holdById(AcMain acMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param acMain Entity
   * @param titaVo Variable-Length Argument
   * @return AcMain Entity
   * @throws DBException exception
   */
  public AcMain insert(AcMain acMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param acMain Entity
   * @param titaVo Variable-Length Argument
   * @return AcMain Entity
   * @throws DBException exception
   */
  public AcMain update(AcMain acMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param acMain Entity
   * @param titaVo Variable-Length Argument
   * @return AcMain Entity
   * @throws DBException exception
   */
  public AcMain update2(AcMain acMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param acMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AcMain acMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param acMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AcMain> acMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param acMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AcMain> acMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param acMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AcMain> acMain, TitaVo... titaVo) throws DBException;

}
