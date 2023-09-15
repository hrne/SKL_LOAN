package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM055AssetLoss;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM055AssetLossId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM055AssetLossService {

  /**
   * findByPrimaryKey
   *
   * @param monthlyLM055AssetLossId PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM055AssetLoss MonthlyLM055AssetLoss
   */
  public MonthlyLM055AssetLoss findById(MonthlyLM055AssetLossId monthlyLM055AssetLossId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM055AssetLoss MonthlyLM055AssetLoss of List
   */
  public Slice<MonthlyLM055AssetLoss> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth = 
   *
   * @param yearMonth_0 yearMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM055AssetLoss MonthlyLM055AssetLoss of List
   */
  public Slice<MonthlyLM055AssetLoss> findYearMonthAll(int yearMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM055AssetLoss
   * 
   * @param monthlyLM055AssetLossId key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM055AssetLoss MonthlyLM055AssetLoss
   */
  public MonthlyLM055AssetLoss holdById(MonthlyLM055AssetLossId monthlyLM055AssetLossId, TitaVo... titaVo);

  /**
   * hold By MonthlyLM055AssetLoss
   * 
   * @param monthlyLM055AssetLoss key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM055AssetLoss MonthlyLM055AssetLoss
   */
  public MonthlyLM055AssetLoss holdById(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM055AssetLoss Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM055AssetLoss Entity
   * @throws DBException exception
   */
  public MonthlyLM055AssetLoss insert(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM055AssetLoss Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM055AssetLoss Entity
   * @throws DBException exception
   */
  public MonthlyLM055AssetLoss update(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM055AssetLoss Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM055AssetLoss Entity
   * @throws DBException exception
   */
  public MonthlyLM055AssetLoss update2(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM055AssetLoss Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM055AssetLoss monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM055AssetLoss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM055AssetLoss> monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM055AssetLoss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM055AssetLoss> monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM055AssetLoss Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM055AssetLoss> monthlyLM055AssetLoss, TitaVo... titaVo) throws DBException;

}
