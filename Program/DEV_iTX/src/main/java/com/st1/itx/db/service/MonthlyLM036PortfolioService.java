package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM036Portfolio;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM036PortfolioService {

  /**
   * findByPrimaryKey
   *
   * @param dataMonth PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM036Portfolio MonthlyLM036Portfolio
   */
  public MonthlyLM036Portfolio findById(int dataMonth, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM036Portfolio MonthlyLM036Portfolio of List
   */
  public Slice<MonthlyLM036Portfolio> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * DataMonth &gt;= , AND DataMonth &lt;= 
   *
   * @param dataMonth_0 dataMonth_0
   * @param dataMonth_1 dataMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM036Portfolio MonthlyLM036Portfolio of List
   */
  public Slice<MonthlyLM036Portfolio> findDataMonthBetween(int dataMonth_0, int dataMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM036Portfolio
   * 
   * @param dataMonth key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM036Portfolio MonthlyLM036Portfolio
   */
  public MonthlyLM036Portfolio holdById(int dataMonth, TitaVo... titaVo);

  /**
   * hold By MonthlyLM036Portfolio
   * 
   * @param monthlyLM036Portfolio key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM036Portfolio MonthlyLM036Portfolio
   */
  public MonthlyLM036Portfolio holdById(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM036Portfolio Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM036Portfolio Entity
   * @throws DBException exception
   */
  public MonthlyLM036Portfolio insert(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM036Portfolio Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM036Portfolio Entity
   * @throws DBException exception
   */
  public MonthlyLM036Portfolio update(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM036Portfolio Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM036Portfolio Entity
   * @throws DBException exception
   */
  public MonthlyLM036Portfolio update2(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM036Portfolio Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM036Portfolio monthlyLM036Portfolio, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM036Portfolio Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM036Portfolio> monthlyLM036Portfolio, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM036Portfolio Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM036Portfolio> monthlyLM036Portfolio, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM036Portfolio Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM036Portfolio> monthlyLM036Portfolio, TitaVo... titaVo) throws DBException;

}
