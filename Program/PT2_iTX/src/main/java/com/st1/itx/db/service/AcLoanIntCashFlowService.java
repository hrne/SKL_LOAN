package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcLoanIntCashFlow;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.AcLoanIntCashFlowId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface AcLoanIntCashFlowService {

  /**
   * findByPrimaryKey
   *
   * @param acLoanIntCashFlowId PK
   * @param titaVo Variable-Length Argument
   * @return AcLoanIntCashFlow AcLoanIntCashFlow
   */
  public AcLoanIntCashFlow findById(AcLoanIntCashFlowId acLoanIntCashFlowId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcLoanIntCashFlow AcLoanIntCashFlow of List
   */
  public Slice<AcLoanIntCashFlow> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * YearMonth =  
   *
   * @param yearMonth_0 yearMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice AcLoanIntCashFlow AcLoanIntCashFlow of List
   */
  public Slice<AcLoanIntCashFlow> findYearMonthEq(int yearMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By AcLoanIntCashFlow
   * 
   * @param acLoanIntCashFlowId key
   * @param titaVo Variable-Length Argument
   * @return AcLoanIntCashFlow AcLoanIntCashFlow
   */
  public AcLoanIntCashFlow holdById(AcLoanIntCashFlowId acLoanIntCashFlowId, TitaVo... titaVo);

  /**
   * hold By AcLoanIntCashFlow
   * 
   * @param acLoanIntCashFlow key
   * @param titaVo Variable-Length Argument
   * @return AcLoanIntCashFlow AcLoanIntCashFlow
   */
  public AcLoanIntCashFlow holdById(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param acLoanIntCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanIntCashFlow Entity
   * @throws DBException exception
   */
  public AcLoanIntCashFlow insert(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param acLoanIntCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanIntCashFlow Entity
   * @throws DBException exception
   */
  public AcLoanIntCashFlow update(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param acLoanIntCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return AcLoanIntCashFlow Entity
   * @throws DBException exception
   */
  public AcLoanIntCashFlow update2(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param acLoanIntCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(AcLoanIntCashFlow acLoanIntCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param acLoanIntCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<AcLoanIntCashFlow> acLoanIntCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param acLoanIntCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<AcLoanIntCashFlow> acLoanIntCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param acLoanIntCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<AcLoanIntCashFlow> acLoanIntCashFlow, TitaVo... titaVo) throws DBException;

}
