package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCashFlow;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdCashFlowId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdCashFlowService {

  /**
   * findByPrimaryKey
   *
   * @param cdCashFlowId PK
   * @param titaVo Variable-Length Argument
   * @return CdCashFlow CdCashFlow
   */
  public CdCashFlow findById(CdCashFlowId cdCashFlowId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCashFlow CdCashFlow of List
   */
  public Slice<CdCashFlow> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * DataYearMonth &gt;= ,AND DataYearMonth &lt;=
   *
   * @param dataYearMonth_0 dataYearMonth_0
   * @param dataYearMonth_1 dataYearMonth_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdCashFlow CdCashFlow of List
   */
  public Slice<CdCashFlow> findDataYearMonth(int dataYearMonth_0, int dataYearMonth_1, int index, int limit, TitaVo... titaVo);

  /**
   * BranchNo = ,AND DataYearMonth = 
   *
   * @param branchNo_0 branchNo_0
   * @param dataYearMonth_1 dataYearMonth_1
   * @param titaVo Variable-Length Argument
   * @return Slice CdCashFlow CdCashFlow of List
   */
  public CdCashFlow findDataYearMonthFirst(String branchNo_0, int dataYearMonth_1, TitaVo... titaVo);

  /**
   * hold By CdCashFlow
   * 
   * @param cdCashFlowId key
   * @param titaVo Variable-Length Argument
   * @return CdCashFlow CdCashFlow
   */
  public CdCashFlow holdById(CdCashFlowId cdCashFlowId, TitaVo... titaVo);

  /**
   * hold By CdCashFlow
   * 
   * @param cdCashFlow key
   * @param titaVo Variable-Length Argument
   * @return CdCashFlow CdCashFlow
   */
  public CdCashFlow holdById(CdCashFlow cdCashFlow, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return CdCashFlow Entity
   * @throws DBException exception
   */
  public CdCashFlow insert(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return CdCashFlow Entity
   * @throws DBException exception
   */
  public CdCashFlow update(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @return CdCashFlow Entity
   * @throws DBException exception
   */
  public CdCashFlow update2(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdCashFlow Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdCashFlow cdCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdCashFlow> cdCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdCashFlow> cdCashFlow, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdCashFlow Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdCashFlow> cdCashFlow, TitaVo... titaVo) throws DBException;

}
