package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdStock;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdStockService {

  /**
   * findByPrimaryKey
   *
   * @param stockCode PK
   * @param titaVo Variable-Length Argument
   * @return CdStock CdStock
   */
  public CdStock findById(String stockCode, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdStock CdStock of List
   */
  public Slice<CdStock> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdStock
   * 
   * @param stockCode key
   * @param titaVo Variable-Length Argument
   * @return CdStock CdStock
   */
  public CdStock holdById(String stockCode, TitaVo... titaVo);

  /**
   * hold By CdStock
   * 
   * @param cdStock key
   * @param titaVo Variable-Length Argument
   * @return CdStock CdStock
   */
  public CdStock holdById(CdStock cdStock, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdStock Entity
   * @param titaVo Variable-Length Argument
   * @return CdStock Entity
   * @throws DBException exception
   */
  public CdStock insert(CdStock cdStock, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdStock Entity
   * @param titaVo Variable-Length Argument
   * @return CdStock Entity
   * @throws DBException exception
   */
  public CdStock update(CdStock cdStock, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdStock Entity
   * @param titaVo Variable-Length Argument
   * @return CdStock Entity
   * @throws DBException exception
   */
  public CdStock update2(CdStock cdStock, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdStock Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdStock cdStock, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdStock Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdStock> cdStock, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdStock Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdStock> cdStock, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdStock Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdStock> cdStock, TitaVo... titaVo) throws DBException;

}
