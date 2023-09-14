package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM052AssetClass;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.MonthlyLM052AssetClassId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface MonthlyLM052AssetClassService {

  /**
   * findByPrimaryKey
   *
   * @param monthlyLM052AssetClassId PK
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052AssetClass MonthlyLM052AssetClass
   */
  public MonthlyLM052AssetClass findById(MonthlyLM052AssetClassId monthlyLM052AssetClassId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM052AssetClass MonthlyLM052AssetClass of List
   */
  public Slice<MonthlyLM052AssetClass> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth = 
   *
   * @param yearMonth_0 yearMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice MonthlyLM052AssetClass MonthlyLM052AssetClass of List
   */
  public Slice<MonthlyLM052AssetClass> findYearMonthAll(int yearMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By MonthlyLM052AssetClass
   * 
   * @param monthlyLM052AssetClassId key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052AssetClass MonthlyLM052AssetClass
   */
  public MonthlyLM052AssetClass holdById(MonthlyLM052AssetClassId monthlyLM052AssetClassId, TitaVo... titaVo);

  /**
   * hold By MonthlyLM052AssetClass
   * 
   * @param monthlyLM052AssetClass key
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052AssetClass MonthlyLM052AssetClass
   */
  public MonthlyLM052AssetClass holdById(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param monthlyLM052AssetClass Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052AssetClass Entity
   * @throws DBException exception
   */
  public MonthlyLM052AssetClass insert(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param monthlyLM052AssetClass Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052AssetClass Entity
   * @throws DBException exception
   */
  public MonthlyLM052AssetClass update(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param monthlyLM052AssetClass Entity
   * @param titaVo Variable-Length Argument
   * @return MonthlyLM052AssetClass Entity
   * @throws DBException exception
   */
  public MonthlyLM052AssetClass update2(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param monthlyLM052AssetClass Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(MonthlyLM052AssetClass monthlyLM052AssetClass, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param monthlyLM052AssetClass Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<MonthlyLM052AssetClass> monthlyLM052AssetClass, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param monthlyLM052AssetClass Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<MonthlyLM052AssetClass> monthlyLM052AssetClass, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param monthlyLM052AssetClass Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<MonthlyLM052AssetClass> monthlyLM052AssetClass, TitaVo... titaVo) throws DBException;

  /**
   * Stored Procedure<br>
   * 
   * @param  tbsdyf int
   * @param  empNo String
   * @param  jobTxSeq String
   * @param titaVo Variable-Length Argument
   *
   */
  public void Usp_L9_MonthlyLM052AssetClass_Ins(int tbsdyf,  String empNo,  String jobTxSeq, TitaVo... titaVo);

}
