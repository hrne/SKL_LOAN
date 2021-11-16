package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxControl;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxControlService {

  /**
   * findByPrimaryKey
   *
   * @param code PK
   * @param titaVo Variable-Length Argument
   * @return TxControl TxControl
   */
  public TxControl findById(String code, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxControl TxControl of List
   */
  public Slice<TxControl> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxControl
   * 
   * @param code key
   * @param titaVo Variable-Length Argument
   * @return TxControl TxControl
   */
  public TxControl holdById(String code, TitaVo... titaVo);

  /**
   * hold By TxControl
   * 
   * @param txControl key
   * @param titaVo Variable-Length Argument
   * @return TxControl TxControl
   */
  public TxControl holdById(TxControl txControl, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txControl Entity
   * @param titaVo Variable-Length Argument
   * @return TxControl Entity
   * @throws DBException exception
   */
  public TxControl insert(TxControl txControl, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txControl Entity
   * @param titaVo Variable-Length Argument
   * @return TxControl Entity
   * @throws DBException exception
   */
  public TxControl update(TxControl txControl, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txControl Entity
   * @param titaVo Variable-Length Argument
   * @return TxControl Entity
   * @throws DBException exception
   */
  public TxControl update2(TxControl txControl, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txControl Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxControl txControl, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txControl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxControl> txControl, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txControl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxControl> txControl, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txControl Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxControl> txControl, TitaVo... titaVo) throws DBException;

}
