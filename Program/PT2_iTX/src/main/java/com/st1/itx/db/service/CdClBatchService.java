package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdClBatch;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdClBatchService {

  /**
   * findByPrimaryKey
   *
   * @param applNo PK
   * @param titaVo Variable-Length Argument
   * @return CdClBatch CdClBatch
   */
  public CdClBatch findById(int applNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdClBatch CdClBatch of List
   */
  public Slice<CdClBatch> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdClBatch
   * 
   * @param applNo key
   * @param titaVo Variable-Length Argument
   * @return CdClBatch CdClBatch
   */
  public CdClBatch holdById(int applNo, TitaVo... titaVo);

  /**
   * hold By CdClBatch
   * 
   * @param cdClBatch key
   * @param titaVo Variable-Length Argument
   * @return CdClBatch CdClBatch
   */
  public CdClBatch holdById(CdClBatch cdClBatch, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdClBatch Entity
   * @param titaVo Variable-Length Argument
   * @return CdClBatch Entity
   * @throws DBException exception
   */
  public CdClBatch insert(CdClBatch cdClBatch, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdClBatch Entity
   * @param titaVo Variable-Length Argument
   * @return CdClBatch Entity
   * @throws DBException exception
   */
  public CdClBatch update(CdClBatch cdClBatch, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdClBatch Entity
   * @param titaVo Variable-Length Argument
   * @return CdClBatch Entity
   * @throws DBException exception
   */
  public CdClBatch update2(CdClBatch cdClBatch, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdClBatch Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdClBatch cdClBatch, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdClBatch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdClBatch> cdClBatch, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdClBatch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdClBatch> cdClBatch, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdClBatch Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdClBatch> cdClBatch, TitaVo... titaVo) throws DBException;

}
