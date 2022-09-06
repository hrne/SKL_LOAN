package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxArchiveTable;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.TxArchiveTableId;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface TxArchiveTableService {

	/**
	 * findByPrimaryKey
	 *
	 * @param txArchiveTableId PK
	 * @param titaVo           Variable-Length Argument
	 * @return TxArchiveTable TxArchiveTable
	 */
	public TxArchiveTable findById(TxArchiveTableId txArchiveTableId, TitaVo... titaVo);

	/**
	 * findAll
	 *
	 * @param index  Page Index
	 * @param limit  Page Data Limit
	 * @param titaVo Variable-Length Argument
	 * @return Slice TxArchiveTable TxArchiveTable of List
	 */
	public Slice<TxArchiveTable> findAll(int index, int limit, TitaVo... titaVo);

	/**
	 * hold By TxArchiveTable
	 * 
	 * @param txArchiveTableId key
	 * @param titaVo           Variable-Length Argument
	 * @return TxArchiveTable TxArchiveTable
	 */
	public TxArchiveTable holdById(TxArchiveTableId txArchiveTableId, TitaVo... titaVo);

	/**
	 * hold By TxArchiveTable
	 * 
	 * @param txArchiveTable key
	 * @param titaVo         Variable-Length Argument
	 * @return TxArchiveTable TxArchiveTable
	 */
	public TxArchiveTable holdById(TxArchiveTable txArchiveTable, TitaVo... titaVo);

	/**
	 * Insert
	 * 
	 * @param txArchiveTable Entity
	 * @param titaVo         Variable-Length Argument
	 * @return TxArchiveTable Entity
	 * @throws DBException exception
	 */
	public TxArchiveTable insert(TxArchiveTable txArchiveTable, TitaVo... titaVo) throws DBException;

	/**
	 * Update
	 * 
	 * @param txArchiveTable Entity
	 * @param titaVo         Variable-Length Argument
	 * @return TxArchiveTable Entity
	 * @throws DBException exception
	 */
	public TxArchiveTable update(TxArchiveTable txArchiveTable, TitaVo... titaVo) throws DBException;

	/**
	 * Update2
	 * 
	 * @param txArchiveTable Entity
	 * @param titaVo         Variable-Length Argument
	 * @return TxArchiveTable Entity
	 * @throws DBException exception
	 */
	public TxArchiveTable update2(TxArchiveTable txArchiveTable, TitaVo... titaVo) throws DBException;

	/**
	 * Delete
	 * 
	 * @param txArchiveTable Entity
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void delete(TxArchiveTable txArchiveTable, TitaVo... titaVo) throws DBException;

	/**
	 * Insert All For List
	 * 
	 * @param txArchiveTable Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void insertAll(List<TxArchiveTable> txArchiveTable, TitaVo... titaVo) throws DBException;

	/**
	 * Update All For List
	 * 
	 * @param txArchiveTable Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void updateAll(List<TxArchiveTable> txArchiveTable, TitaVo... titaVo) throws DBException;

	/**
	 * Delete All For List
	 * 
	 * @param txArchiveTable Entity of List
	 * @param titaVo         Variable-Length Argument
	 * @throws DBException exception
	 */
	public void deleteAll(List<TxArchiveTable> txArchiveTable, TitaVo... titaVo) throws DBException;

	/**
	 * Stored Procedure<br>
	 * 封存結清且領清償證明滿五年之交易明細
	 * 
	 * @param tbsdyf int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L6_ArchiveFiveYearTx_Copy(int tbsdyf, String empNo, TitaVo... titaVo);

	/**
	 * Stored Procedure<br>
	 * 將結清且領清償證明滿五年之已封存交易明細搬回連線環境
	 * 
	 * @param custNo int
	 * @param facmNo int
	 * @param bormNo int
	 * @param tbsdyf int
	 * @param empNo  String
	 * @param titaVo Variable-Length Argument
	 *
	 */
	public void Usp_L6_UnarchiveFiveYearTx_Copy(int custNo, int facmNo, int bormNo, int tbsdyf, String empNo, TitaVo... titaVo);

}
