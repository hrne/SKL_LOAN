package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClBatch;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.ClBatchId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface ClBatchService {

  /**
   * findByPrimaryKey
   *
   * @param clBatchId PK
   * @param titaVo Variable-Length Argument
   * @return ClBatch ClBatch
   */
  public ClBatch findById(ClBatchId clBatchId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBatch ClBatch of List
   */
  public Slice<ClBatch> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * GroupNo = 
   *
   * @param groupNo_0 groupNo_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice ClBatch ClBatch of List
   */
  public Slice<ClBatch> findGroupNo(String groupNo_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By ClBatch
   * 
   * @param clBatchId key
   * @param titaVo Variable-Length Argument
   * @return ClBatch ClBatch
   */
  public ClBatch holdById(ClBatchId clBatchId, TitaVo... titaVo);

  /**
   * hold By ClBatch
   * 
   * @param clBatch key
   * @param titaVo Variable-Length Argument
   * @return ClBatch ClBatch
   */
  public ClBatch holdById(ClBatch clBatch, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param clBatch Entity
   * @param titaVo Variable-Length Argument
   * @return ClBatch Entity
   * @throws DBException exception
   */
  public ClBatch insert(ClBatch clBatch, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param clBatch Entity
   * @param titaVo Variable-Length Argument
   * @return ClBatch Entity
   * @throws DBException exception
   */
  public ClBatch update(ClBatch clBatch, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param clBatch Entity
   * @param titaVo Variable-Length Argument
   * @return ClBatch Entity
   * @throws DBException exception
   */
  public ClBatch update2(ClBatch clBatch, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param clBatch Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(ClBatch clBatch, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param clBatch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<ClBatch> clBatch, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param clBatch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<ClBatch> clBatch, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param clBatch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<ClBatch> clBatch, TitaVo... titaVo) throws DBException;

}
