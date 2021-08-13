package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdWorkMonthId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdWorkMonthService {

  /**
   * findByPrimaryKey
   *
   * @param cdWorkMonthId PK
   * @param titaVo Variable-Length Argument
   * @return CdWorkMonth CdWorkMonth
   */
  public CdWorkMonth findById(CdWorkMonthId cdWorkMonthId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdWorkMonth CdWorkMonth of List
   */
  public Slice<CdWorkMonth> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Year &gt;= ,AND Year &lt;= ,AND Month &gt;= ,AND Month &lt;= 
   *
   * @param year_0 year_0
   * @param year_1 year_1
   * @param month_2 month_2
   * @param month_3 month_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdWorkMonth CdWorkMonth of List
   */
  public Slice<CdWorkMonth> findYearMonth(int year_0, int year_1, int month_2, int month_3, int index, int limit, TitaVo... titaVo);

  /**
   * StartDate &lt;= ,AND EndDate &gt;= 
   *
   * @param startDate_0 startDate_0
   * @param endDate_1 endDate_1
   * @param titaVo Variable-Length Argument
   * @return Slice CdWorkMonth CdWorkMonth of List
   */
  public CdWorkMonth findDateFirst(int startDate_0, int endDate_1, TitaVo... titaVo);

  /**
   * hold By CdWorkMonth
   * 
   * @param cdWorkMonthId key
   * @param titaVo Variable-Length Argument
   * @return CdWorkMonth CdWorkMonth
   */
  public CdWorkMonth holdById(CdWorkMonthId cdWorkMonthId, TitaVo... titaVo);

  /**
   * hold By CdWorkMonth
   * 
   * @param cdWorkMonth key
   * @param titaVo Variable-Length Argument
   * @return CdWorkMonth CdWorkMonth
   */
  public CdWorkMonth holdById(CdWorkMonth cdWorkMonth, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdWorkMonth Entity
   * @param titaVo Variable-Length Argument
   * @return CdWorkMonth Entity
   * @throws DBException exception
   */
  public CdWorkMonth insert(CdWorkMonth cdWorkMonth, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdWorkMonth Entity
   * @param titaVo Variable-Length Argument
   * @return CdWorkMonth Entity
   * @throws DBException exception
   */
  public CdWorkMonth update(CdWorkMonth cdWorkMonth, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdWorkMonth Entity
   * @param titaVo Variable-Length Argument
   * @return CdWorkMonth Entity
   * @throws DBException exception
   */
  public CdWorkMonth update2(CdWorkMonth cdWorkMonth, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdWorkMonth Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdWorkMonth cdWorkMonth, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdWorkMonth Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdWorkMonth> cdWorkMonth, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdWorkMonth Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdWorkMonth> cdWorkMonth, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdWorkMonth Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdWorkMonth> cdWorkMonth, TitaVo... titaVo) throws DBException;

}
