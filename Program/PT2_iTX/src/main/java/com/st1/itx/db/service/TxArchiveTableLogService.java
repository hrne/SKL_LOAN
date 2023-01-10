package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxArchiveTableLog;
import org.springframework.data.domain.Slice;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxArchiveTableLogService {

  /**
   * findByPrimaryKey
   *
   * @param logNo PK
   * @param titaVo Variable-Length Argument
   * @return TxArchiveTableLog TxArchiveTableLog
   */
  public TxArchiveTableLog findById(Long logNo, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxArchiveTableLog TxArchiveTableLog of List
   */
  public Slice<TxArchiveTableLog> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * Type = ,AND TableName = ,AND ExecuteDate = ,AND DataFrom= ,AND DataTo = ,AND BatchNo = ,AND CustNo = ,AND FacmNo = ,AND BormNo =
   *
   * @param type_0 type_0
   * @param tableName_1 tableName_1
   * @param executeDate_2 executeDate_2
   * @param dataFrom_3 dataFrom_3
   * @param dataTo_4 dataTo_4
   * @param batchNo_5 batchNo_5
   * @param custNo_6 custNo_6
   * @param facmNo_7 facmNo_7
   * @param bormNo_8 bormNo_8
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice TxArchiveTableLog TxArchiveTableLog of List
   */
  public Slice<TxArchiveTableLog> findLogs(String type_0, String tableName_1, int executeDate_2, String dataFrom_3, String dataTo_4, int batchNo_5, int custNo_6, int facmNo_7, int bormNo_8, int index, int limit, TitaVo... titaVo);

  /**
   * hold By TxArchiveTableLog
   * 
   * @param logNo key
   * @param titaVo Variable-Length Argument
   * @return TxArchiveTableLog TxArchiveTableLog
   */
  public TxArchiveTableLog holdById(Long logNo, TitaVo... titaVo);

  /**
   * hold By TxArchiveTableLog
   * 
   * @param txArchiveTableLog key
   * @param titaVo Variable-Length Argument
   * @return TxArchiveTableLog TxArchiveTableLog
   */
  public TxArchiveTableLog holdById(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param txArchiveTableLog Entity
   * @param titaVo Variable-Length Argument
   * @return TxArchiveTableLog Entity
   * @throws DBException exception
   */
  public TxArchiveTableLog insert(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param txArchiveTableLog Entity
   * @param titaVo Variable-Length Argument
   * @return TxArchiveTableLog Entity
   * @throws DBException exception
   */
  public TxArchiveTableLog update(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param txArchiveTableLog Entity
   * @param titaVo Variable-Length Argument
   * @return TxArchiveTableLog Entity
   * @throws DBException exception
   */
  public TxArchiveTableLog update2(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param txArchiveTableLog Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param txArchiveTableLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param txArchiveTableLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param txArchiveTableLog Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException;

}
