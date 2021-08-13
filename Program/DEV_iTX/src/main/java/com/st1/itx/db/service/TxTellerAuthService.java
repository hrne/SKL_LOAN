package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxTellerAuth;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxTellerAuthId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxTellerAuthService {

  /**
   * findByPrimaryKey
   *
   * @param txTellerAuthId PK
   * @param titaVo Variable-Length Argument
   * @return TxTellerAuth TxTellerAuth
   */
  public TxTellerAuth findById(TxTellerAuthId txTellerAuthId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTellerAuth TxTellerAuth of List
   */
  public Slice<TxTellerAuth> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * TlrNo = 
   *
   * @param tlrNo_0 tlrNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTellerAuth TxTellerAuth of List
   */
  public Slice<TxTellerAuth> findByTlrNo(String tlrNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * AuthNo =
   *
   * @param authNo_0 authNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTellerAuth TxTellerAuth of List
   */
  public Slice<TxTellerAuth> findByAuthNo(String authNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxTellerAuth
   * 
   * @param txTellerAuthId key
   * @param titaVo Variable-Length Argument
   * @return TxTellerAuth TxTellerAuth
   */
  public TxTellerAuth holdById(TxTellerAuthId txTellerAuthId, TitaVo... titaVo);

  /**
   * hold By TxTellerAuth
   * 
   * @param txTellerAuth key
   * @param titaVo Variable-Length Argument
   * @return TxTellerAuth TxTellerAuth
   */
  public TxTellerAuth holdById(TxTellerAuth txTellerAuth, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txTellerAuth Entity
   * @param titaVo Variable-Length Argument
   * @return TxTellerAuth Entity
   * @throws DBException exception
   */
  public TxTellerAuth insert(TxTellerAuth txTellerAuth, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txTellerAuth Entity
   * @param titaVo Variable-Length Argument
   * @return TxTellerAuth Entity
   * @throws DBException exception
   */
  public TxTellerAuth update(TxTellerAuth txTellerAuth, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txTellerAuth Entity
   * @param titaVo Variable-Length Argument
   * @return TxTellerAuth Entity
   * @throws DBException exception
   */
  public TxTellerAuth update2(TxTellerAuth txTellerAuth, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txTellerAuth Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxTellerAuth txTellerAuth, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txTellerAuth Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxTellerAuth> txTellerAuth, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txTellerAuth Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxTellerAuth> txTellerAuth, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txTellerAuth Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxTellerAuth> txTellerAuth, TitaVo... titaVo) throws DBException;

}
