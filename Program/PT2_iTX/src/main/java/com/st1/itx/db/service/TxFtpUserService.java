package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxFtpUser;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxFtpUserService {

  /**
   * findByPrimaryKey
   *
   * @param userId PK
   * @param titaVo Variable-Length Argument
   * @return TxFtpUser TxFtpUser
   */
  public TxFtpUser findById(String userId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxFtpUser TxFtpUser of List
   */
  public Slice<TxFtpUser> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxFtpUser
   * 
   * @param userId key
   * @param titaVo Variable-Length Argument
   * @return TxFtpUser TxFtpUser
   */
  public TxFtpUser holdById(String userId, TitaVo... titaVo);

  /**
   * hold By TxFtpUser
   * 
   * @param txFtpUser key
   * @param titaVo Variable-Length Argument
   * @return TxFtpUser TxFtpUser
   */
  public TxFtpUser holdById(TxFtpUser txFtpUser, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txFtpUser Entity
   * @param titaVo Variable-Length Argument
   * @return TxFtpUser Entity
   * @throws DBException exception
   */
  public TxFtpUser insert(TxFtpUser txFtpUser, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txFtpUser Entity
   * @param titaVo Variable-Length Argument
   * @return TxFtpUser Entity
   * @throws DBException exception
   */
  public TxFtpUser update(TxFtpUser txFtpUser, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txFtpUser Entity
   * @param titaVo Variable-Length Argument
   * @return TxFtpUser Entity
   * @throws DBException exception
   */
  public TxFtpUser update2(TxFtpUser txFtpUser, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txFtpUser Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxFtpUser txFtpUser, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txFtpUser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxFtpUser> txFtpUser, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txFtpUser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxFtpUser> txFtpUser, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txFtpUser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxFtpUser> txFtpUser, TitaVo... titaVo) throws DBException;

}
