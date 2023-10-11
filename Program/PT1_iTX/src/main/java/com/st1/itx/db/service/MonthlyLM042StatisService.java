package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM042Statis;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM042StatisId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM042StatisService {

  /**
   * findByPrimaryKey
   *
   * @param monthlyLM042StatisId PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM042Statis MonthlyLM042Statis
   */
  public MonthlyLM042Statis findById(MonthlyLM042StatisId monthlyLM042StatisId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM042Statis MonthlyLM042Statis of List
   */
  public Slice<MonthlyLM042Statis> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth = 
   *
   * @param yearMonth_0 yearMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM042Statis MonthlyLM042Statis of List
   */
  public Slice<MonthlyLM042Statis> findYearMonthAll(int yearMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth = ,AND LoanItem = ,AND RelatedCode = ,AND AssetClass = 
   *
   * @param yearMonth_0 yearMonth_0
   * @param loanItem_1 loanItem_1
   * @param relatedCode_2 relatedCode_2
   * @param assetClass_3 assetClass_3
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM042Statis MonthlyLM042Statis of List
   */
  public Slice<MonthlyLM042Statis> findItem(int yearMonth_0, String loanItem_1, String relatedCode_2, String assetClass_3, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM042Statis
   * 
   * @param monthlyLM042StatisId key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM042Statis MonthlyLM042Statis
   */
  public MonthlyLM042Statis holdById(MonthlyLM042StatisId monthlyLM042StatisId, TitaVo... titaVo);

  /**
   * hold By MonthlyLM042Statis
   * 
   * @param monthlyLM042Statis key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM042Statis MonthlyLM042Statis
   */
  public MonthlyLM042Statis holdById(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM042Statis Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM042Statis Entity
   * @throws DBException exception
   */
  public MonthlyLM042Statis insert(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM042Statis Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM042Statis Entity
   * @throws DBException exception
   */
  public MonthlyLM042Statis update(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM042Statis Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM042Statis Entity
   * @throws DBException exception
   */
  public MonthlyLM042Statis update2(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM042Statis Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM042Statis monthlyLM042Statis, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM042Statis Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM042Statis> monthlyLM042Statis, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM042Statis Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM042Statis> monthlyLM042Statis, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM042Statis Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM042Statis> monthlyLM042Statis, TitaVo... titaVo) throws DBException;

}
