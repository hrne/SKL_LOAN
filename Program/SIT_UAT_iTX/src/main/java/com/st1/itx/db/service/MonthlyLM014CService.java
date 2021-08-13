package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM014C;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM014CId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM014CService {

  /**
   * findByPrimaryKey
   *
   * @param monthlyLM014CId PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014C MonthlyLM014C
   */
  public MonthlyLM014C findById(MonthlyLM014CId monthlyLM014CId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM014C MonthlyLM014C of List
   */
  public Slice<MonthlyLM014C> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * DataYM = 
   *
   * @param dataYM_0 dataYM_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM014C MonthlyLM014C of List
   */
  public Slice<MonthlyLM014C> DataYMEq(int dataYM_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM014C
   * 
   * @param monthlyLM014CId key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014C MonthlyLM014C
   */
  public MonthlyLM014C holdById(MonthlyLM014CId monthlyLM014CId, TitaVo... titaVo);

  /**
   * hold By MonthlyLM014C
   * 
   * @param monthlyLM014C key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014C MonthlyLM014C
   */
  public MonthlyLM014C holdById(MonthlyLM014C monthlyLM014C, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM014C Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014C Entity
   * @throws DBException exception
   */
  public MonthlyLM014C insert(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM014C Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014C Entity
   * @throws DBException exception
   */
  public MonthlyLM014C update(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM014C Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM014C Entity
   * @throws DBException exception
   */
  public MonthlyLM014C update2(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM014C Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM014C monthlyLM014C, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM014C Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM014C> monthlyLM014C, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM014C Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM014C> monthlyLM014C, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM014C Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM014C> monthlyLM014C, TitaVo... titaVo) throws DBException;

}
