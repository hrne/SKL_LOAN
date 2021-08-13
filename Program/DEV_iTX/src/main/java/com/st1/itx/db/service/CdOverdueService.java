package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdOverdue;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdOverdueId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdOverdueService {

  /**
   * findByPrimaryKey
   *
   * @param cdOverdueId PK
   * @param titaVo Variable-Length Argument
   * @return CdOverdue CdOverdue
   */
  public CdOverdue findById(CdOverdueId cdOverdueId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdOverdue CdOverdue of List
   */
  public Slice<CdOverdue> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * OverdueSign = 
   *
   * @param overdueSign_0 overdueSign_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdOverdue CdOverdue of List
   */
  public Slice<CdOverdue> findOverdueSign(String overdueSign_0, int index, int limit, TitaVo... titaVo);

  /**
   * OverdueCode =
   *
   * @param overdueCode_0 overdueCode_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdOverdue CdOverdue of List
   */
  public Slice<CdOverdue> findOverdueCode(String overdueCode_0, int index, int limit, TitaVo... titaVo);

  /**
   * Enable = 
   *
   * @param enable_0 enable_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdOverdue CdOverdue of List
   */
  public Slice<CdOverdue> findEnable(String enable_0, int index, int limit, TitaVo... titaVo);

  /**
   * OverdueSign &gt;= ,AND OverdueSign &lt;= ,AND OverdueCode &gt;= ,AND OverdueCode &lt;= 
   *
   * @param overdueSign_0 overdueSign_0
   * @param overdueSign_1 overdueSign_1
   * @param overdueCode_2 overdueCode_2
   * @param overdueCode_3 overdueCode_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdOverdue CdOverdue of List
   */
  public Slice<CdOverdue> overdueCodeRange(String overdueSign_0, String overdueSign_1, String overdueCode_2, String overdueCode_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdOverdue
   * 
   * @param cdOverdueId key
   * @param titaVo Variable-Length Argument
   * @return CdOverdue CdOverdue
   */
  public CdOverdue holdById(CdOverdueId cdOverdueId, TitaVo... titaVo);

  /**
   * hold By CdOverdue
   * 
   * @param cdOverdue key
   * @param titaVo Variable-Length Argument
   * @return CdOverdue CdOverdue
   */
  public CdOverdue holdById(CdOverdue cdOverdue, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdOverdue Entity
   * @param titaVo Variable-Length Argument
   * @return CdOverdue Entity
   * @throws DBException exception
   */
  public CdOverdue insert(CdOverdue cdOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdOverdue Entity
   * @param titaVo Variable-Length Argument
   * @return CdOverdue Entity
   * @throws DBException exception
   */
  public CdOverdue update(CdOverdue cdOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdOverdue Entity
   * @param titaVo Variable-Length Argument
   * @return CdOverdue Entity
   * @throws DBException exception
   */
  public CdOverdue update2(CdOverdue cdOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdOverdue Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdOverdue cdOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdOverdue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdOverdue> cdOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdOverdue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdOverdue> cdOverdue, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdOverdue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdOverdue> cdOverdue, TitaVo... titaVo) throws DBException;

}
