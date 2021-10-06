package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FinReportDebt;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FinReportDebtId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportDebtService {

  /**
   * findByPrimaryKey
   *
   * @param finReportDebtId PK
   * @param titaVo Variable-Length Argument
   * @return FinReportDebt FinReportDebt
   */
  public FinReportDebt findById(FinReportDebtId finReportDebtId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FinReportDebt FinReportDebt of List
   */
  public Slice<FinReportDebt> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = 
   *
   * @param custUKey_0 custUKey_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FinReportDebt FinReportDebt of List
   */
  public Slice<FinReportDebt> findCustUKey(String custUKey_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustUKey = ,AND StartYY =
   *
   * @param custUKey_0 custUKey_0
   * @param startYY_1 startYY_1
   * @param titaVo Variable-Length Argument
   * @return Slice FinReportDebt FinReportDebt of List
   */
  public FinReportDebt findCustUKeyYearFirst(String custUKey_0, int startYY_1, TitaVo... titaVo);

  /**
   * hold By FinReportDebt
   * 
   * @param finReportDebtId key
   * @param titaVo Variable-Length Argument
   * @return FinReportDebt FinReportDebt
   */
  public FinReportDebt holdById(FinReportDebtId finReportDebtId, TitaVo... titaVo);

  /**
   * hold By FinReportDebt
   * 
   * @param finReportDebt key
   * @param titaVo Variable-Length Argument
   * @return FinReportDebt FinReportDebt
   */
  public FinReportDebt holdById(FinReportDebt finReportDebt, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param finReportDebt Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportDebt Entity
   * @throws DBException exception
   */
  public FinReportDebt insert(FinReportDebt finReportDebt, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param finReportDebt Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportDebt Entity
   * @throws DBException exception
   */
  public FinReportDebt update(FinReportDebt finReportDebt, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param finReportDebt Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportDebt Entity
   * @throws DBException exception
   */
  public FinReportDebt update2(FinReportDebt finReportDebt, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param finReportDebt Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FinReportDebt finReportDebt, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param finReportDebt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FinReportDebt> finReportDebt, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param finReportDebt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FinReportDebt> finReportDebt, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param finReportDebt Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FinReportDebt> finReportDebt, TitaVo... titaVo) throws DBException;

}
