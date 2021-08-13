package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdPerformance;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.CdPerformanceId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface CdPerformanceService {

  /**
   * findByPrimaryKey
   *
   * @param cdPerformanceId PK
   * @param titaVo Variable-Length Argument
   * @return CdPerformance CdPerformance
   */
  public CdPerformance findById(CdPerformanceId cdPerformanceId, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdPerformance CdPerformance of List
   */
  public Slice<CdPerformance> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth = ,AND PieceCode &gt;= ,AND PieceCode &lt;=
   *
   * @param workMonth_0 workMonth_0
   * @param pieceCode_1 pieceCode_1
   * @param pieceCode_2 pieceCode_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdPerformance CdPerformance of List
   */
  public Slice<CdPerformance> findPieceCode(int workMonth_0, String pieceCode_1, String pieceCode_2, int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth =
   *
   * @param workMonth_0 workMonth_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice CdPerformance CdPerformance of List
   */
  public Slice<CdPerformance> findWorkMonth(int workMonth_0, int index, int limit, TitaVo... titaVo);

  /**
   * WorkMonth &lt;= 
   *
   * @param workMonth_0 workMonth_0
   * @param titaVo Variable-Length Argument
   * @return Slice CdPerformance CdPerformance of List
   */
  public CdPerformance findWorkMonthFirst(int workMonth_0, TitaVo... titaVo);

  /**
   * hold By CdPerformance
   * 
   * @param cdPerformanceId key
   * @param titaVo Variable-Length Argument
   * @return CdPerformance CdPerformance
   */
  public CdPerformance holdById(CdPerformanceId cdPerformanceId, TitaVo... titaVo);

  /**
   * hold By CdPerformance
   * 
   * @param cdPerformance key
   * @param titaVo Variable-Length Argument
   * @return CdPerformance CdPerformance
   */
  public CdPerformance holdById(CdPerformance cdPerformance, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param cdPerformance Entity
   * @param titaVo Variable-Length Argument
   * @return CdPerformance Entity
   * @throws DBException exception
   */
  public CdPerformance insert(CdPerformance cdPerformance, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param cdPerformance Entity
   * @param titaVo Variable-Length Argument
   * @return CdPerformance Entity
   * @throws DBException exception
   */
  public CdPerformance update(CdPerformance cdPerformance, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param cdPerformance Entity
   * @param titaVo Variable-Length Argument
   * @return CdPerformance Entity
   * @throws DBException exception
   */
  public CdPerformance update2(CdPerformance cdPerformance, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param cdPerformance Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(CdPerformance cdPerformance, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param cdPerformance Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<CdPerformance> cdPerformance, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param cdPerformance Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<CdPerformance> cdPerformance, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param cdPerformance Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<CdPerformance> cdPerformance, TitaVo... titaVo) throws DBException;

}
