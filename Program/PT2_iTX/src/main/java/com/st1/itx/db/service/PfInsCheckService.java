package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.PfInsCheck;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.PfInsCheckId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface PfInsCheckService {

  /**
   * findByPrimaryKey
   *
   * @param pfInsCheckId PK
   * @param titaVo Variable-Length Argument
   * @return PfInsCheck PfInsCheck
   */
  public PfInsCheck findById(PfInsCheckId pfInsCheckId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfInsCheck PfInsCheck of List
   */
  public Slice<PfInsCheck> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CheckWorkMonth= ,AND Kind=
   *
   * @param checkWorkMonth_0 checkWorkMonth_0
   * @param kind_1 kind_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice PfInsCheck PfInsCheck of List
   */
  public Slice<PfInsCheck> findCheckWorkMonthEq(int checkWorkMonth_0, int kind_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By PfInsCheck
   * 
   * @param pfInsCheckId key
   * @param titaVo Variable-Length Argument
   * @return PfInsCheck PfInsCheck
   */
  public PfInsCheck holdById(PfInsCheckId pfInsCheckId, TitaVo... titaVo);

  /**
   * hold By PfInsCheck
   * 
   * @param pfInsCheck key
   * @param titaVo Variable-Length Argument
   * @return PfInsCheck PfInsCheck
   */
  public PfInsCheck holdById(PfInsCheck pfInsCheck, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param pfInsCheck Entity
   * @param titaVo Variable-Length Argument
   * @return PfInsCheck Entity
   * @throws DBException exception
   */
  public PfInsCheck insert(PfInsCheck pfInsCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param pfInsCheck Entity
   * @param titaVo Variable-Length Argument
   * @return PfInsCheck Entity
   * @throws DBException exception
   */
  public PfInsCheck update(PfInsCheck pfInsCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param pfInsCheck Entity
   * @param titaVo Variable-Length Argument
   * @return PfInsCheck Entity
   * @throws DBException exception
   */
  public PfInsCheck update2(PfInsCheck pfInsCheck, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param pfInsCheck Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(PfInsCheck pfInsCheck, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param pfInsCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<PfInsCheck> pfInsCheck, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param pfInsCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<PfInsCheck> pfInsCheck, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param pfInsCheck Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<PfInsCheck> pfInsCheck, TitaVo... titaVo) throws DBException;

}
