package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM052Loss;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM052LossService {

  /**
   * findByPrimaryKey
   *
   * @param yearMonth PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052Loss MonthlyLM052Loss
   */
  public MonthlyLM052Loss findById(int yearMonth, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM052Loss MonthlyLM052Loss of List
   */
  public Slice<MonthlyLM052Loss> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth &gt;= ,AND YearMonth &lt;=
   *
   * @param yearMonth_0 yearMonth_0
   * @param yearMonth_1 yearMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM052Loss MonthlyLM052Loss of List
   */
  public Slice<MonthlyLM052Loss> findYearMonth(int yearMonth_0, int yearMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM052Loss
   * 
   * @param yearMonth key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052Loss MonthlyLM052Loss
   */
  public MonthlyLM052Loss holdById(int yearMonth, TitaVo... titaVo);

  /**
   * hold By MonthlyLM052Loss
   * 
   * @param monthlyLM052Loss key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052Loss MonthlyLM052Loss
   */
  public MonthlyLM052Loss holdById(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM052Loss Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052Loss Entity
   * @throws DBException exception
   */
  public MonthlyLM052Loss insert(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM052Loss Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052Loss Entity
   * @throws DBException exception
   */
  public MonthlyLM052Loss update(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM052Loss Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052Loss Entity
   * @throws DBException exception
   */
  public MonthlyLM052Loss update2(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM052Loss Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM052Loss monthlyLM052Loss, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM052Loss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM052Loss> monthlyLM052Loss, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM052Loss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM052Loss> monthlyLM052Loss, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM052Loss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM052Loss> monthlyLM052Loss, TitaVo... titaVo) throws DBException;

}
