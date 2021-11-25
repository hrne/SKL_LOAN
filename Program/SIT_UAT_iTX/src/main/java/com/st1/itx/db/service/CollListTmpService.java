package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CollListTmp;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CollListTmpId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CollListTmpService {

  /**
   * findByPrimaryKey
   *
   * @param collListTmpId PK
   * @param titaVo Variable-Length Argument
   * @return CollListTmp CollListTmp
   */
  public CollListTmp findById(CollListTmpId collListTmpId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CollListTmp CollListTmp of List
   */
  public Slice<CollListTmp> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By CollListTmp
   * 
   * @param collListTmpId key
   * @param titaVo Variable-Length Argument
   * @return CollListTmp CollListTmp
   */
  public CollListTmp holdById(CollListTmpId collListTmpId, TitaVo... titaVo);

  /**
   * hold By CollListTmp
   * 
   * @param collListTmp key
   * @param titaVo Variable-Length Argument
   * @return CollListTmp CollListTmp
   */
  public CollListTmp holdById(CollListTmp collListTmp, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param collListTmp Entity
   * @param titaVo Variable-Length Argument
   * @return CollListTmp Entity
   * @throws DBException exception
   */
  public CollListTmp insert(CollListTmp collListTmp, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param collListTmp Entity
   * @param titaVo Variable-Length Argument
   * @return CollListTmp Entity
   * @throws DBException exception
   */
  public CollListTmp update(CollListTmp collListTmp, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param collListTmp Entity
   * @param titaVo Variable-Length Argument
   * @return CollListTmp Entity
   * @throws DBException exception
   */
  public CollListTmp update2(CollListTmp collListTmp, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param collListTmp Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CollListTmp collListTmp, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param collListTmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CollListTmp> collListTmp, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param collListTmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CollListTmp> collListTmp, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param collListTmp Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CollListTmp> collListTmp, TitaVo... titaVo) throws DBException;

}
