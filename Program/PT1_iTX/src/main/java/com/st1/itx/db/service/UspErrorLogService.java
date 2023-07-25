package com.st1.itx.db.service;

import java.util.List;

import java.math.BigDecimal;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.UspErrorLog;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface UspErrorLogService {

  /**
   * findByPrimaryKey
   *
   * @param logUkey PK
   * @param titaVo Variable-Length Argument
   * @return UspErrorLog UspErrorLog
   */
  public UspErrorLog findById(String logUkey, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice UspErrorLog UspErrorLog of List
   */
  public Slice<UspErrorLog> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * LogDate &gt;= ,AND LogDate &lt;=
   *
   * @param logDate_0 logDate_0
   * @param logDate_1 logDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice UspErrorLog UspErrorLog of List
   */
  public Slice<UspErrorLog> findByLogDate(int logDate_0, int logDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * LogDate &gt;= ,AND LogDate &lt;= ,AND UspName %
   *
   * @param logDate_0 logDate_0
   * @param logDate_1 logDate_1
   * @param uspName_2 uspName_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice UspErrorLog UspErrorLog of List
   */
  public Slice<UspErrorLog> findByLogDateAndUspName(int logDate_0, int logDate_1, String uspName_2, int index, int limit, TitaVo... titaVo);

  /**
   * JobTxSeq = 
   *
   * @param jobTxSeq_0 jobTxSeq_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice UspErrorLog UspErrorLog of List
   */
  public Slice<UspErrorLog> findByJobTxSeq(String jobTxSeq_0, int index, int limit, TitaVo... titaVo);

  /**
   * hold By UspErrorLog
   * 
   * @param logUkey key
   * @param titaVo Variable-Length Argument
   * @return UspErrorLog UspErrorLog
   */
  public UspErrorLog holdById(String logUkey, TitaVo... titaVo);

  /**
   * hold By UspErrorLog
   * 
   * @param uspErrorLog key
   * @param titaVo Variable-Length Argument
   * @return UspErrorLog UspErrorLog
   */
  public UspErrorLog holdById(UspErrorLog uspErrorLog, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param uspErrorLog Entity
   * @param titaVo Variable-Length Argument
   * @return UspErrorLog Entity
   * @throws DBException exception
   */
  public UspErrorLog insert(UspErrorLog uspErrorLog, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param uspErrorLog Entity
   * @param titaVo Variable-Length Argument
   * @return UspErrorLog Entity
   * @throws DBException exception
   */
  public UspErrorLog update(UspErrorLog uspErrorLog, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param uspErrorLog Entity
   * @param titaVo Variable-Length Argument
   * @return UspErrorLog Entity
   * @throws DBException exception
   */
  public UspErrorLog update2(UspErrorLog uspErrorLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param uspErrorLog Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(UspErrorLog uspErrorLog, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param uspErrorLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<UspErrorLog> uspErrorLog, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param uspErrorLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<UspErrorLog> uspErrorLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param uspErrorLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<UspErrorLog> uspErrorLog, TitaVo... titaVo) throws DBException;

}
