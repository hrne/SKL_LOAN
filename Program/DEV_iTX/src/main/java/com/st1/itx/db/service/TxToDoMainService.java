package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxToDoMain;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxToDoMainService {

  /**
   * findByPrimaryKey
   *
   * @param itemCode PK
   * @param titaVo Variable-Length Argument
   * @return TxToDoMain TxToDoMain
   */
  public TxToDoMain findById(String itemCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxToDoMain TxToDoMain of List
   */
  public Slice<TxToDoMain> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * AutoFg = ,AND ExcuteTxcd = 
   *
   * @param autoFg_0 autoFg_0
   * @param excuteTxcd_1 excuteTxcd_1
   * @param titaVo Variable-Length Argument
   * @return Slice TxToDoMain TxToDoMain of List
   */
  public TxToDoMain excuteTxcdFirst(String autoFg_0, String excuteTxcd_1, TitaVo... titaVo);

  /**
   * hold By TxToDoMain
   * 
   * @param itemCode key
   * @param titaVo Variable-Length Argument
   * @return TxToDoMain TxToDoMain
   */
  public TxToDoMain holdById(String itemCode, TitaVo... titaVo);

  /**
   * hold By TxToDoMain
   * 
   * @param txToDoMain key
   * @param titaVo Variable-Length Argument
   * @return TxToDoMain TxToDoMain
   */
  public TxToDoMain holdById(TxToDoMain txToDoMain, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txToDoMain Entity
   * @param titaVo Variable-Length Argument
   * @return TxToDoMain Entity
   * @throws DBException exception
   */
  public TxToDoMain insert(TxToDoMain txToDoMain, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txToDoMain Entity
   * @param titaVo Variable-Length Argument
   * @return TxToDoMain Entity
   * @throws DBException exception
   */
  public TxToDoMain update(TxToDoMain txToDoMain, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txToDoMain Entity
   * @param titaVo Variable-Length Argument
   * @return TxToDoMain Entity
   * @throws DBException exception
   */
  public TxToDoMain update2(TxToDoMain txToDoMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txToDoMain Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxToDoMain txToDoMain, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txToDoMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxToDoMain> txToDoMain, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txToDoMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxToDoMain> txToDoMain, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txToDoMain Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxToDoMain> txToDoMain, TitaVo... titaVo) throws DBException;

}
