package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxArchiveTableLog;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxArchiveTableLogId;

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
	 * @param txArchiveTableLogId PK
	 * @param titaVo              Variable-Length Argument
	 * @return TxArchiveTableLog TxArchiveTableLog
	 */
	public TxArchiveTableLog findById(TxArchiveTableLogId txArchiveTableLogId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxArchiveTableLog TxArchiveTableLog of List
	 */
	public Slice<TxArchiveTableLog> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxArchiveTableLog
	 * 
	 * @param txArchiveTableLogId key
	 * @param titaVo              Variable-Length Argument
	 * @return TxArchiveTableLog TxArchiveTableLog
	 */
	public TxArchiveTableLog holdById(TxArchiveTableLogId txArchiveTableLogId, TitaVo... titaVo);

	/**
	 * hold By TxArchiveTableLog
	 * 
	 * @param txArchiveTableLog key
	 * @param titaVo            Variable-Length Argument
	 * @return TxArchiveTableLog TxArchiveTableLog
	 */
	public TxArchiveTableLog holdById(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txArchiveTableLog Entity
	 * @param titaVo            Variable-Length Argument
	 * @return TxArchiveTableLog Entity
	 * @throws DBException exception
	 */
	public TxArchiveTableLog insert(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txArchiveTableLog Entity
	 * @param titaVo            Variable-Length Argument
	 * @return TxArchiveTableLog Entity
	 * @throws DBException exception
	 */
	public TxArchiveTableLog update(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txArchiveTableLog Entity
	 * @param titaVo            Variable-Length Argument
	 * @return TxArchiveTableLog Entity
	 * @throws DBException exception
	 */
	public TxArchiveTableLog update2(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txArchiveTableLog Entity
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxArchiveTableLog txArchiveTableLog, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txArchiveTableLog Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txArchiveTableLog Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txArchiveTableLog Entity of List
	 * @param titaVo            Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxArchiveTableLog> txArchiveTableLog, TitaVo... titaVo) throws DBException;

}
