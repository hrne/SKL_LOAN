package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxCruiser;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxCruiserId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxCruiserService {

  /**
   * findByPrimaryKey
   *
   * @param txCruiserId PK
   * @param titaVo Variable-Length Argument
   * @return TxCruiser TxCruiser
   */
  public TxCruiser findById(TxCruiserId txCruiserId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxCruiser TxCruiser of List
   */
  public Slice<TxCruiser> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Status =
   *
   * @param status_0 status_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxCruiser TxCruiser of List
   */
  public Slice<TxCruiser> FindAllByStatus(String status_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxCruiser
   * 
   * @param txCruiserId key
   * @param titaVo Variable-Length Argument
   * @return TxCruiser TxCruiser
   */
  public TxCruiser holdById(TxCruiserId txCruiserId, TitaVo... titaVo);

  /**
   * hold By TxCruiser
   * 
   * @param txCruiser key
   * @param titaVo Variable-Length Argument
   * @return TxCruiser TxCruiser
   */
  public TxCruiser holdById(TxCruiser txCruiser, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txCruiser Entity
   * @param titaVo Variable-Length Argument
   * @return TxCruiser Entity
   * @throws DBException exception
   */
  public TxCruiser insert(TxCruiser txCruiser, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txCruiser Entity
   * @param titaVo Variable-Length Argument
   * @return TxCruiser Entity
   * @throws DBException exception
   */
  public TxCruiser update(TxCruiser txCruiser, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txCruiser Entity
   * @param titaVo Variable-Length Argument
   * @return TxCruiser Entity
   * @throws DBException exception
   */
  public TxCruiser update2(TxCruiser txCruiser, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txCruiser Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxCruiser txCruiser, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txCruiser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxCruiser> txCruiser, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txCruiser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxCruiser> txCruiser, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txCruiser Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxCruiser> txCruiser, TitaVo... titaVo) throws DBException;

}
