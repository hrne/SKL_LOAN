package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM014B;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM014BId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM014BService {

  /**
   * findByPrimaryKey
   *
   * @param monthlyLM014BId PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014B MonthlyLM014B
   */
  public MonthlyLM014B findById(MonthlyLM014BId monthlyLM014BId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM014B MonthlyLM014B of List
   */
  public Slice<MonthlyLM014B> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * DataYM = 
   *
   * @param dataYM_0 dataYM_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM014B MonthlyLM014B of List
   */
  public Slice<MonthlyLM014B> DataYMEq(int dataYM_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM014B
   * 
   * @param monthlyLM014BId key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014B MonthlyLM014B
   */
  public MonthlyLM014B holdById(MonthlyLM014BId monthlyLM014BId, TitaVo... titaVo);

  /**
   * hold By MonthlyLM014B
   * 
   * @param monthlyLM014B key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014B MonthlyLM014B
   */
  public MonthlyLM014B holdById(MonthlyLM014B monthlyLM014B, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM014B Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014B Entity
   * @throws DBException exception
   */
  public MonthlyLM014B insert(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM014B Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014B Entity
   * @throws DBException exception
   */
  public MonthlyLM014B update(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM014B Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014B Entity
   * @throws DBException exception
   */
  public MonthlyLM014B update2(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM014B Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM014B monthlyLM014B, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM014B Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM014B> monthlyLM014B, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM014B Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM014B> monthlyLM014B, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM014B Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM014B> monthlyLM014B, TitaVo... titaVo) throws DBException;

}
