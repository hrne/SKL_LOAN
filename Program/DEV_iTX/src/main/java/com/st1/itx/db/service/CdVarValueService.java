package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdVarValue;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdVarValueService {

  /**
   * findByPrimaryKey
   *
   * @param yearMonth PK
   * @param titaVo Variable-Length Argument
   * @return CdVarValue CdVarValue
   */
  public CdVarValue findById(int yearMonth, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdVarValue CdVarValue of List
   */
  public Slice<CdVarValue> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth &gt;= ,AND YearMonth &lt;= 
   *
   * @param yearMonth_0 yearMonth_0
   * @param yearMonth_1 yearMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdVarValue CdVarValue of List
   */
  public Slice<CdVarValue> findYearMonth(int yearMonth_0, int yearMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By CdVarValue
   * 
   * @param yearMonth key
   * @param titaVo Variable-Length Argument
   * @return CdVarValue CdVarValue
   */
  public CdVarValue holdById(int yearMonth, TitaVo... titaVo);

  /**
   * hold By CdVarValue
   * 
   * @param cdVarValue key
   * @param titaVo Variable-Length Argument
   * @return CdVarValue CdVarValue
   */
  public CdVarValue holdById(CdVarValue cdVarValue, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdVarValue Entity
   * @param titaVo Variable-Length Argument
   * @return CdVarValue Entity
   * @throws DBException exception
   */
  public CdVarValue insert(CdVarValue cdVarValue, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdVarValue Entity
   * @param titaVo Variable-Length Argument
   * @return CdVarValue Entity
   * @throws DBException exception
   */
  public CdVarValue update(CdVarValue cdVarValue, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdVarValue Entity
   * @param titaVo Variable-Length Argument
   * @return CdVarValue Entity
   * @throws DBException exception
   */
  public CdVarValue update2(CdVarValue cdVarValue, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdVarValue Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdVarValue cdVarValue, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdVarValue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdVarValue> cdVarValue, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdVarValue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdVarValue> cdVarValue, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdVarValue Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdVarValue> cdVarValue, TitaVo... titaVo) throws DBException;

}
