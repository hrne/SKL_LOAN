package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.GraceCondition;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.GraceConditionId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface GraceConditionService {

  /**
   * findByPrimaryKey
   *
   * @param graceConditionId PK
   * @param titaVo Variable-Length Argument
   * @return GraceCondition GraceCondition
   */
  public GraceCondition findById(GraceConditionId graceConditionId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice GraceCondition GraceCondition of List
   */
  public Slice<GraceCondition> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustNo&lt;= ,AND CustNo&gt;= 
   *
   * @param custNo_0 custNo_0
   * @param custNo_1 custNo_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice GraceCondition GraceCondition of List
   */
  public Slice<GraceCondition> CustNoEq(int custNo_0, int custNo_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By GraceCondition
   * 
   * @param graceConditionId key
   * @param titaVo Variable-Length Argument
   * @return GraceCondition GraceCondition
   */
  public GraceCondition holdById(GraceConditionId graceConditionId, TitaVo... titaVo);

  /**
   * hold By GraceCondition
   * 
   * @param graceCondition key
   * @param titaVo Variable-Length Argument
   * @return GraceCondition GraceCondition
   */
  public GraceCondition holdById(GraceCondition graceCondition, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param graceCondition Entity
   * @param titaVo Variable-Length Argument
   * @return GraceCondition Entity
   * @throws DBException exception
   */
  public GraceCondition insert(GraceCondition graceCondition, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param graceCondition Entity
   * @param titaVo Variable-Length Argument
   * @return GraceCondition Entity
   * @throws DBException exception
   */
  public GraceCondition update(GraceCondition graceCondition, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param graceCondition Entity
   * @param titaVo Variable-Length Argument
   * @return GraceCondition Entity
   * @throws DBException exception
   */
  public GraceCondition update2(GraceCondition graceCondition, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param graceCondition Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(GraceCondition graceCondition, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param graceCondition Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<GraceCondition> graceCondition, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param graceCondition Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<GraceCondition> graceCondition, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param graceCondition Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<GraceCondition> graceCondition, TitaVo... titaVo) throws DBException;

}
