package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FinHoldRel;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinHoldRelService {

  /**
   * findByPrimaryKey
   *
   * @param id PK
   * @param titaVo Variable-Length Argument
   * @return FinHoldRel FinHoldRel
   */
  public FinHoldRel findById(String id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FinHoldRel FinHoldRel of List
   */
  public Slice<FinHoldRel> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By FinHoldRel
   * 
   * @param id key
   * @param titaVo Variable-Length Argument
   * @return FinHoldRel FinHoldRel
   */
  public FinHoldRel holdById(String id, TitaVo... titaVo);

  /**
   * hold By FinHoldRel
   * 
   * @param finHoldRel key
   * @param titaVo Variable-Length Argument
   * @return FinHoldRel FinHoldRel
   */
  public FinHoldRel holdById(FinHoldRel finHoldRel, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param finHoldRel Entity
   * @param titaVo Variable-Length Argument
   * @return FinHoldRel Entity
   * @throws DBException exception
   */
  public FinHoldRel insert(FinHoldRel finHoldRel, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param finHoldRel Entity
   * @param titaVo Variable-Length Argument
   * @return FinHoldRel Entity
   * @throws DBException exception
   */
  public FinHoldRel update(FinHoldRel finHoldRel, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param finHoldRel Entity
   * @param titaVo Variable-Length Argument
   * @return FinHoldRel Entity
   * @throws DBException exception
   */
  public FinHoldRel update2(FinHoldRel finHoldRel, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param finHoldRel Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FinHoldRel finHoldRel, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param finHoldRel Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FinHoldRel> finHoldRel, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param finHoldRel Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FinHoldRel> finHoldRel, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param finHoldRel Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FinHoldRel> finHoldRel, TitaVo... titaVo) throws DBException;

}
