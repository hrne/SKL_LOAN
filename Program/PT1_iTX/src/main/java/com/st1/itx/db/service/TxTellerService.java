package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxTeller;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxTellerService {

  /**
   * findByPrimaryKey
   *
   * @param tlrNo PK
   * @param titaVo Variable-Length Argument
   * @return TxTeller TxTeller
   */
  public TxTeller findById(String tlrNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTeller TxTeller of List
   */
  public Slice<TxTeller> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * BrNo = ,AND TlrNo %
   *
   * @param brNo_0 brNo_0
   * @param tlrNo_1 tlrNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTeller TxTeller of List
   */
  public Slice<TxTeller> findByL6041(String brNo_0, String tlrNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * BrNo = ,AND GroupNo &gt;=,AND GroupNo &lt;=,AND LevelFg&gt;=,AND LevelFg&lt;=
   *
   * @param brNo_0 brNo_0
   * @param groupNo_1 groupNo_1
   * @param groupNo_2 groupNo_2
   * @param levelFg_3 levelFg_3
   * @param levelFg_4 levelFg_4
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTeller TxTeller of List
   */
  public Slice<TxTeller> findByGroupNo(String brNo_0, String groupNo_1, String groupNo_2, int levelFg_3, int levelFg_4, int index, int limit, TitaVo... titaVo);

  /**
   * TlrNo %
   *
   * @param tlrNo_0 tlrNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxTeller TxTeller of List
   */
  public Slice<TxTeller> findByTlrNo(String tlrNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxTeller
   * 
   * @param tlrNo key
   * @param titaVo Variable-Length Argument
   * @return TxTeller TxTeller
   */
  public TxTeller holdById(String tlrNo, TitaVo... titaVo);

  /**
   * hold By TxTeller
   * 
   * @param txTeller key
   * @param titaVo Variable-Length Argument
   * @return TxTeller TxTeller
   */
  public TxTeller holdById(TxTeller txTeller, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txTeller Entity
   * @param titaVo Variable-Length Argument
   * @return TxTeller Entity
   * @throws DBException exception
   */
  public TxTeller insert(TxTeller txTeller, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txTeller Entity
   * @param titaVo Variable-Length Argument
   * @return TxTeller Entity
   * @throws DBException exception
   */
  public TxTeller update(TxTeller txTeller, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txTeller Entity
   * @param titaVo Variable-Length Argument
   * @return TxTeller Entity
   * @throws DBException exception
   */
  public TxTeller update2(TxTeller txTeller, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txTeller Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxTeller txTeller, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txTeller Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxTeller> txTeller, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txTeller Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxTeller> txTeller, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txTeller Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxTeller> txTeller, TitaVo... titaVo) throws DBException;

}
