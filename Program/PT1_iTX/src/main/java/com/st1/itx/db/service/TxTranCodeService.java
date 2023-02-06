package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxTranCode;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxTranCodeService {

  /**
   * findByPrimaryKey
   *
   * @param tranNo PK
   * @param titaVo Variable-Length Argument
   * @return TxTranCode TxTranCode
   */
  public TxTranCode findById(String tranNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTranCode TxTranCode of List
   */
  public Slice<TxTranCode> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * TranNo %
   *
   * @param tranNo_0 tranNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTranCode TxTranCode of List
   */
  public Slice<TxTranCode> TranNoLike(String tranNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxTranCode
   * 
   * @param tranNo key
   * @param titaVo Variable-Length Argument
   * @return TxTranCode TxTranCode
   */
  public TxTranCode holdById(String tranNo, TitaVo... titaVo);

  /**
   * hold By TxTranCode
   * 
   * @param txTranCode key
   * @param titaVo Variable-Length Argument
   * @return TxTranCode TxTranCode
   */
  public TxTranCode holdById(TxTranCode txTranCode, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txTranCode Entity
   * @param titaVo Variable-Length Argument
   * @return TxTranCode Entity
   * @throws DBException exception
   */
  public TxTranCode insert(TxTranCode txTranCode, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txTranCode Entity
   * @param titaVo Variable-Length Argument
   * @return TxTranCode Entity
   * @throws DBException exception
   */
  public TxTranCode update(TxTranCode txTranCode, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txTranCode Entity
   * @param titaVo Variable-Length Argument
   * @return TxTranCode Entity
   * @throws DBException exception
   */
  public TxTranCode update2(TxTranCode txTranCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txTranCode Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxTranCode txTranCode, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txTranCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxTranCode> txTranCode, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txTranCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxTranCode> txTranCode, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txTranCode Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxTranCode> txTranCode, TitaVo... titaVo) throws DBException;

}
