package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClStock;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClStockId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClStockService {

  /**
   * findByPrimaryKey
   *
   * @param clStockId PK
   * @param titaVo Variable-Length Argument
   * @return ClStock ClStock
   */
  public ClStock findById(ClStockId clStockId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClStock ClStock of List
   */
  public Slice<ClStock> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = 
   *
   * @param clCode1_0 clCode1_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClStock ClStock of List
   */
  public Slice<ClStock> findClCode1(int clCode1_0, int index, int limit, TitaVo... titaVo);

  /**
   * ClCode1 = ,AND ClCode2 = 
   *
   * @param clCode1_0 clCode1_0
   * @param clCode2_1 clCode2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClStock ClStock of List
   */
  public Slice<ClStock> findClCode2(int clCode1_0, int clCode2_1, int index, int limit, TitaVo... titaVo);

  /**
   * StockCode = ,AND OwnerId =
   *
   * @param stockCode_0 stockCode_0
   * @param ownerId_1 ownerId_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClStock ClStock of List
   */
  public Slice<ClStock> findUnique(String stockCode_0, String ownerId_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClStock
   * 
   * @param clStockId key
   * @param titaVo Variable-Length Argument
   * @return ClStock ClStock
   */
  public ClStock holdById(ClStockId clStockId, TitaVo... titaVo);

  /**
   * hold By ClStock
   * 
   * @param clStock key
   * @param titaVo Variable-Length Argument
   * @return ClStock ClStock
   */
  public ClStock holdById(ClStock clStock, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clStock Entity
   * @param titaVo Variable-Length Argument
   * @return ClStock Entity
   * @throws DBException exception
   */
  public ClStock insert(ClStock clStock, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clStock Entity
   * @param titaVo Variable-Length Argument
   * @return ClStock Entity
   * @throws DBException exception
   */
  public ClStock update(ClStock clStock, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clStock Entity
   * @param titaVo Variable-Length Argument
   * @return ClStock Entity
   * @throws DBException exception
   */
  public ClStock update2(ClStock clStock, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clStock Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClStock clStock, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clStock Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClStock> clStock, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clStock Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClStock> clStock, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clStock Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClStock> clStock, TitaVo... titaVo) throws DBException;

}
