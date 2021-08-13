package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.BatxRateChange;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.BatxRateChangeId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface BatxRateChangeService {

  /**
   * findByPrimaryKey
   *
   * @param batxRateChangeId PK
   * @param titaVo Variable-Length Argument
   * @return BatxRateChange BatxRateChange
   */
  public BatxRateChange findById(BatxRateChangeId batxRateChangeId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AdjDate &gt;= ,AND AdjDate &lt;= ,AND BaseRateCode = 
   *
   * @param adjDate_0 adjDate_0
   * @param adjDate_1 adjDate_1
   * @param baseRateCode_2 baseRateCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> baseRateCodeEq(int adjDate_0, int adjDate_1, String baseRateCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * AdjDate &gt;= ,AND AdjDate &lt;= ,AND CustCode &gt;= ,AND CustCode &lt;= 
   *
   * @param adjDate_0 adjDate_0
   * @param adjDate_1 adjDate_1
   * @param custCode_2 custCode_2
   * @param custCode_3 custCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> custCodeEq(int adjDate_0, int adjDate_1, int custCode_2, int custCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * CustCode &gt;= ,AND CustCode &lt;= ,AND TxKind &gt;= ,AND TxKind &lt;= ,AND AdjCode &gt;= ,AND AdjCode &lt;=   ,AND AdjDate &gt;= ,AND AdjDate &lt;=
   *
   * @param custCode_0 custCode_0
   * @param custCode_1 custCode_1
   * @param txKind_2 txKind_2
   * @param txKind_3 txKind_3
   * @param adjCode_4 adjCode_4
   * @param adjCode_5 adjCode_5
   * @param adjDate_6 adjDate_6
   * @param adjDate_7 adjDate_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> findL4931AEq(int custCode_0, int custCode_1, int txKind_2, int txKind_3, int adjCode_4, int adjCode_5, int adjDate_6, int adjDate_7, int index, int limit, TitaVo... titaVo);

  /**
   * AdjDate &gt;= ,AND AdjDate &lt;= ,AND AdjCode &gt;= ,AND  AdjCode &lt;= ,AND RateKeyInCode &gt;= ,AND RateKeyInCode &lt;= 
   *
   * @param adjDate_0 adjDate_0
   * @param adjDate_1 adjDate_1
   * @param adjCode_2 adjCode_2
   * @param adjCode_3 adjCode_3
   * @param rateKeyInCode_4 rateKeyInCode_4
   * @param rateKeyInCode_5 rateKeyInCode_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> adjCodeEq(int adjDate_0, int adjDate_1, int adjCode_2, int adjCode_3, int rateKeyInCode_4, int rateKeyInCode_5, int index, int limit, TitaVo... titaVo);

  /**
   * AdjDate &gt;= ,AND AdjDate &lt;= ,AND CustCode &gt;= ,AND CustCode &lt;= ,AND TxKind = ,AND ConfirmFlag = 
   *
   * @param adjDate_0 adjDate_0
   * @param adjDate_1 adjDate_1
   * @param custCode_2 custCode_2
   * @param custCode_3 custCode_3
   * @param txKind_4 txKind_4
   * @param confirmFlag_5 confirmFlag_5
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> findL4321Report(int adjDate_0, int adjDate_1, int custCode_2, int custCode_3, int txKind_4, int confirmFlag_5, int index, int limit, TitaVo... titaVo);

  /**
   * hold By BatxRateChange
   * 
   * @param batxRateChangeId key
   * @param titaVo Variable-Length Argument
   * @return BatxRateChange BatxRateChange
   */
  public BatxRateChange holdById(BatxRateChangeId batxRateChangeId, TitaVo... titaVo);

  /**
   * hold By BatxRateChange
   * 
   * @param batxRateChange key
   * @param titaVo Variable-Length Argument
   * @return BatxRateChange BatxRateChange
   */
  public BatxRateChange holdById(BatxRateChange batxRateChange, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param batxRateChange Entity
   * @param titaVo Variable-Length Argument
   * @return BatxRateChange Entity
   * @throws DBException exception
   */
  public BatxRateChange insert(BatxRateChange batxRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param batxRateChange Entity
   * @param titaVo Variable-Length Argument
   * @return BatxRateChange Entity
   * @throws DBException exception
   */
  public BatxRateChange update(BatxRateChange batxRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param batxRateChange Entity
   * @param titaVo Variable-Length Argument
   * @return BatxRateChange Entity
   * @throws DBException exception
   */
  public BatxRateChange update2(BatxRateChange batxRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param batxRateChange Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(BatxRateChange batxRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param batxRateChange Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<BatxRateChange> batxRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param batxRateChange Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<BatxRateChange> batxRateChange, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param batxRateChange Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<BatxRateChange> batxRateChange, TitaVo... titaVo) throws DBException;

}
