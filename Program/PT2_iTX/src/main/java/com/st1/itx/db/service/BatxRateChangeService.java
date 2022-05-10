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
   * AdjDate = ,AND TxKind = ,AND RateKeyInCode = 
   *
   * @param adjDate_0 adjDate_0
   * @param txKind_1 txKind_1
   * @param rateKeyInCode_2 rateKeyInCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> adjCodeEq(int adjDate_0, int txKind_1, int rateKeyInCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * AdjDate &gt;= ,AND AdjDate &lt;= ,AND CustCode &gt;= ,AND CustCode &lt;= ,AND TxKind = ,AND AdjCode &gt;= ,AND AdjCode &lt;= ,AND ConfirmFlag = 
   *
   * @param adjDate_0 adjDate_0
   * @param adjDate_1 adjDate_1
   * @param custCode_2 custCode_2
   * @param custCode_3 custCode_3
   * @param txKind_4 txKind_4
   * @param adjCode_5 adjCode_5
   * @param adjCode_6 adjCode_6
   * @param confirmFlag_7 confirmFlag_7
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> findL4321Report(int adjDate_0, int adjDate_1, int custCode_2, int custCode_3, int txKind_4, int adjCode_5, int adjCode_6, int confirmFlag_7, int index, int limit, TitaVo... titaVo);

  /**
   * AdjDate = ,AND TitaTlrNo = ,AND TitaTxtNo =
   *
   * @param adjDate_0 adjDate_0
   * @param titaTlrNo_1 titaTlrNo_1
   * @param titaTxtNo_2 titaTxtNo_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public Slice<BatxRateChange> findL4320Erase(int adjDate_0, String titaTlrNo_1, String titaTxtNo_2, int index, int limit, TitaVo... titaVo);

  /**
   * CustNo = ,AND FacmNo = ,AND BormNo = ,AND PreNextAdjDate =
   *
   * @param custNo_0 custNo_0
   * @param facmNo_1 facmNo_1
   * @param bormNo_2 bormNo_2
   * @param preNextAdjDate_3 preNextAdjDate_3
   * @param titaVo Variable-Length Argument
   * @return Slice BatxRateChange BatxRateChange of List
   */
  public BatxRateChange findL2980printFirst(int custNo_0, int facmNo_1, int bormNo_2, int preNextAdjDate_3, TitaVo... titaVo);

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
