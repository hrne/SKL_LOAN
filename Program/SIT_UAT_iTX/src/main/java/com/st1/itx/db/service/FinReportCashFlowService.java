package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.FinReportCashFlow;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.FinReportCashFlowId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface FinReportCashFlowService {

  /**
   * findByPrimaryKey
   *
   * @param finReportCashFlowId PK
   * @param titaVo Variable-Length Argument
   * @return FinReportCashFlow FinReportCashFlow
   */
  public FinReportCashFlow findById(FinReportCashFlowId finReportCashFlowId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice FinReportCashFlow FinReportCashFlow of List
   */
  public Slice<FinReportCashFlow> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * hold By FinReportCashFlow
   * 
   * @param finReportCashFlowId key
   * @param titaVo Variable-Length Argument
   * @return FinReportCashFlow FinReportCashFlow
   */
  public FinReportCashFlow holdById(FinReportCashFlowId finReportCashFlowId, TitaVo... titaVo);

  /**
   * hold By FinReportCashFlow
   * 
   * @param finReportCashFlow key
   * @param titaVo Variable-Length Argument
   * @return FinReportCashFlow FinReportCashFlow
   */
  public FinReportCashFlow holdById(FinReportCashFlow finReportCashFlow, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param finReportCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportCashFlow Entity
   * @throws DBException exception
   */
  public FinReportCashFlow insert(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param finReportCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportCashFlow Entity
   * @throws DBException exception
   */
  public FinReportCashFlow update(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param finReportCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return FinReportCashFlow Entity
   * @throws DBException exception
   */
  public FinReportCashFlow update2(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param finReportCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(FinReportCashFlow finReportCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param finReportCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<FinReportCashFlow> finReportCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param finReportCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<FinReportCashFlow> finReportCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param finReportCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<FinReportCashFlow> finReportCashFlow, TitaVo... titaVo) throws DBException;

}
